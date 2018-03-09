import util.control.Breaks._
import java.io.File
import java.text.Normalizer
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper

import scala.util.matching.Regex

object Extractor {

  type Keyword = String
  type MatchedPair = List[(Keyword, List[String])]

  //A Map of keywords -> regular expressions that were tailored to obtain better results
  val knownRegEx: Map[Keyword, Regex] = Map(
    "name" -> "(?:name is|I'm|I am|(?:n|N)ame:|(?:(?i)Personal Information))\\s+((?:[A-Z]\\w+\\s?){1,2})".r,
    "age" -> "(?:I'm|I am)\\s+(\\d{1,2})".r,
    "mail" -> "((?:[a-z]|[0-9]|\\.|-|_)+@(?:[a-z]|\\.)+(?:pt|com|org|net|uk|co|eu))".r,
    "date" -> "((?:\\d{1,2}|\\d{4})(?:-|/)\\d{1,2}(?:-|/)(?:\\d{2}\\D|\\d{4}))".r,
    "phone" -> "(?:\\+44|\\(\\+44\\))\\s?(\\d{9,10})".r,
    "birth" -> "(?i)(?:Date of birth|birth date)\\s+((?:\\d{1,2}|\\d{4})(?:\\s|\\/|-)(?:\\w+|\\d{2})(?:\\s|\\/|-)(?:\\d{4}|\\d{1,2}\\D))".r,
    "gender" -> "(?i)(?:gender|sex)\\s+(?i)(Male|Female|f|m)".r,
    "zipcode" -> "(?i)((?:\\d|\\w){4}-(?:\\d|\\w){3})".r
  )

  /**
    * Method that given a file path (maybe change to a real file) will load that PDF file and read the text from it
    *
    * @param filePath - String containing the URI for the file to be loaded
    * @return A String containing all the text found in the document
    */
  def readPDF(filePath: String): String = {
    try {
      val pdf = PDDocument.load(new File(filePath))
      val document = new PDFTextStripper


      val str = Normalizer.normalize(document.getText(pdf), Normalizer.Form.NFD)
        .replaceAll("[\\p{InCombiningDiacriticalMarks}]", "")
      pdf.close()
      str
    } catch {
      case t: Throwable => t.printStackTrace() //we can remove this if we don't want the error message to appear
        null
    }
  }

  /**
    * Method that will iterate through a list of given keywords and will try to obtain a value for that keyword
    *
    * @param text        - Text from the PDF extracted from readPDF method
    * @param keywords    - List containing all the keywords we want to find values for
    * @param clientRegEx - Optional parameter - If the client already has a predefined Regular Expression for a given key
    *                    use that regular expression instead of ours
    * @return a List containing pairs of Keywords and a List (non-repeating) of values found for that keyword
    */
  def getAllMatchedValues(text: String, keywords: List[Keyword], clientRegEx: Map[Keyword, Regex] = Map()): MatchedPair = {
    val matched = keywords.map(key => {

      //If the client sent a custom RegEx to use on this key, use it
      if (clientRegEx.contains(key))
        (key, clientRegEx(key).findAllIn(text).matchData.map(_.group(1)).toList.distinct)

      //if we already know a good RegEx for this keyword, use it
      else if (knownRegEx.contains(key))
        (key, knownRegEx(key).findAllIn(text).matchData.map(_.group(1)).toList.distinct)


      else findKeywordInText(key, text) //to be changed, here we need to manually search for the keywords in the text
    })
    filterNewLines(matched)
  }

  /**
    * Method that operates like getAllMatchedValues but instead returns only the first element it finds for a given
    * keyword, representing a single JSON object
    *
    * @param text        - Text from the PDF extracted from readPDF method
    * @param keywords    - List containing all the keywords we want to find values for
    * @param clientRegEx - Optional parameter - If the client already has a predefined Regular Expression for a given key
    * @return A List containing pairs of keywords with a single matched value
    */
  def getSingleMatchedValue(text: String, keywords: List[Keyword], clientRegEx: Map[Keyword, Regex] = Map()): MatchedPair = {
    getAllMatchedValues(text, keywords, clientRegEx).map(pair => if (pair._2.nonEmpty) (pair._1, List(pair._2.head)) else (pair._1, List()))
  }

  /**
    * Method that will return a List of Matched Pairs. This method operates as getSingleMatcheValue, but instead
    * of returning just one object, returns a list containing all of them
    *
    * @param text        - Text from the PDF extracted from readPDF method
    * @param keywords    - List containing all the keywords we want to find values for
    * @param clientRegEx - Optional parameter - If the client already has a predefined Regular Expression for a given key
    * @return A List containing sublists of pairs of keywords with single matched values
    */
  def getAllObjects(text: String, keywords: List[Keyword], clientRegEx: Map[Keyword, Regex] = Map()): List[MatchedPair] = {
    def getListSizes(matchedValues: MatchedPair): List[(Keyword, Int)] = {
      for (m <- matchedValues) yield (m._1, m._2.size)
    }

    val matchedValues = getAllMatchedValues(text, keywords, clientRegEx)
    val mostfound = getListSizes(matchedValues).maxBy(_._2)

    val mappedValues = for (i <- 0 to mostfound._2; m <- matchedValues) yield {
      if (m._2.size > i)
        List(m._2(i))
      else List("Not Defined") //change to List(null) or List() ??
    }
    mappedValues.zipWithIndex.map(pair => (keywords(pair._2 % keywords.length), pair._1)).toList.grouped(keywords.size).toList
  }

  /**
    * Method that given a List of pairs of keywords and their respective values will create a string in JSON format
    *
    * This method receives an optional flag with information on how to return non existing values,
    * this flag can be :
    * "empty" (default) - returns an empty string
    * "null" - returns the value null (in quotations, can be changed)
    * "remove" - removes that specific field altogether
    *
    * @param listJSON - List of pairs of keywords and their respective values
    * @param flag     -Optional flag with information on how to return non-existing values
    * @return
    */
  def makeJSONString(listJSON: MatchedPair, flag: String = "empty"): String = {
    val str = listJSON.map(k =>
      if (k._2.nonEmpty) {
        if (k._2.size > 1) "\"" + k._1 + "\":\"" + k._2.mkString("[", ", ", "]") + "\""
        else "\"" + k._1 + "\": \"" + k._2.head + "\""
      } else {
        if (flag == "empty") {
          "\"" + k._1 + "\": \"\""
        } else if (flag == "null") {
          "\"" + k._1 + "\": \"null\""
        }
      }
    )
    val pseudoJSON = str
    if (flag == "remove") pseudoJSON.filter(_ != ()).mkString("{", ", ", "}") else pseudoJSON.mkString("{", ", ", "}")
  }

  /**
    * Method that will remove all the new line characters from the list of values obtain from a keyword
    *
    * @param matchedValues - List of pairs of Keyword and the values obtained for that keyword
    * @return The same list as passed by parameter but with no new line characters
    */
  def filterNewLines(matchedValues: MatchedPair): MatchedPair = {
    matchedValues.map(pair => {
      val setOfValues = pair._2
      (pair._1, setOfValues.map(_.replaceAll("[\\r\\n]", "").trim)) //remove all new line characters and trim all elements
    })
  }

  /**
    * Method that gets all keywords and respective values from know form and returns a JSON string
    *
    * @param text - Text from the PDF extracted from readPDF method
    * @return
    */
  def getJSONFromForm(text: String): String = {
    val formRegex = "(.+):\\s+(.+)".r
    val form = formRegex.findAllIn(text).matchData.map(l => (l.group(1), List(l.group(2)))).toList
    makeJSONString(form)
  }

  /**
    * Method that will try to find a value for a given keyword if we do not have any RegEx for that keyword
    * (or the client didn't send any)
    *
    * @param keyword - The keyword to find the value for
    * @param text    - The text in which to look for the value
    * @return A pair containing the keyword and a list of values found for that keyword
    */
  def findKeywordInText(keyword: Keyword, text: String): (Keyword, List[String]) = {
    val keyRegex = new Regex("(?i)" + keyword + "\\s+(\\w+)")
    (keyword, keyRegex.findAllIn(text).matchData.map(_.group(1)).toList.distinct)
  }

  //  val pageAmount = pdf.getNumberOfPages
  //  val page = pdf.getPage(0)
  //  val res = page.getResources
  //  val font = res.getFontNames
  //  val props = res.getPropertiesNames
  //  font.forEach(f=>println(res.getFont(f)))
  //  println("Props: "+props)

}
