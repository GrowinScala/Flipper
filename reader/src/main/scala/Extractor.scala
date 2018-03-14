import java.io.File
import java.text.Normalizer
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import scala.util.matching.Regex
import OpenNLP._
import ImageProcessing._
import scala.io.Source

/**
  * Singleton object that implements all the functionalities regarding the extraction of information from a PDF document
  */
object Extractor {

  type Keyword = String
  type MatchedPair = List[(Keyword, List[String])]

  /**
    * Method that given a file path (maybe change to a real file) will load that PDF file and read the text from it
    *
    * @param filePath - String containing the URI for the file to be loaded
    * @return A String containing all the text found in the document
    */
  def readPDF(filePath: String, readImages: Boolean = true): String = {
    try {
      val pdf = PDDocument.load(new File(filePath))
      val document = new PDFTextStripper

      if (readImages) {
        val imagesList = extractImgs(pdf)
        //        imagesList.foreach(f => println(readImageText(f)))
      }
      //If we want to add the images text to str, we can do so, although its not very precise

      val str = Normalizer.normalize(document.getText(pdf), Normalizer.Form.NFD)
        .replaceAll("[\\p{InCombiningDiacriticalMarks}]", "")
      pdf.close()
      str
    } catch {
      case t: Throwable => null //we can remove this if we don't want the error message to appear
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
  def getAllMatchedValues(text: String, keywords: List[(Keyword, String)], clientRegEx: Map[Keyword, Regex] = Map()): MatchedPair = {
    if (keywords == null || text == null || keywords.isEmpty || text == "") return List() //or null
    val knownRegEx: Map[String, Regex] = importRegExFile(detectLanguage(text))
    val matched = keywords.map(key => {

      //If the client sent a custom RegEx to use on this key, use it
      if (clientRegEx.contains(key._1) && clientRegEx != null)
        (key._1, clientRegEx(key._1).findAllIn(text).matchData.map(_.group(1)).toList.distinct)

      //if we already know a good RegEx for this keyword, use it
      else if (knownRegEx.contains(key._1))
        (key._1, knownRegEx(key._1).findAllIn(text).matchData.map(_.group(1)).toList.distinct)

      else findKeywordInText(key._1, key._2, text) //to be changed, here we need to manually search for the keywords in the text
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
  def getSingleMatchedValue(text: String, keywords: List[(Keyword, String)], clientRegEx: Map[Keyword, Regex] = Map()): MatchedPair = {
    if (keywords == null || text == null || keywords.isEmpty || text == "") return List() //or null
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
  def getAllObjects(text: String, keywords: List[(Keyword, String)], clientRegEx: Map[Keyword, Regex] = Map()): List[MatchedPair] = {
    if (keywords == null || text == null || keywords.isEmpty || text == "") return List() //or null

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
    mappedValues.zipWithIndex.map(pair => (keywords(pair._2 % keywords.length)._1, pair._1)).toList.grouped(keywords.size).toList
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
    if (listJSON == null) return "{}"

    val pseudoJSON = listJSON.map(k =>
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
    * (or the client didn't send any).
    * This method uses Apaches openNLP for determining the POS Tag (Part of Speech) to make sure we return a correct value
    *
    * @param keyword - The keyword to find the value for
    * @param text    - The text in which to look for the value
    * @param tag     - The POS Tag of the value we want to return
    * @return A pair containing the keyword and a list of values found for that keyword
    */
  def findKeywordInText(keyword: Keyword, tag: String, text: String): (Keyword, List[String]) = {
    //TODO convert tags, the client could send Noun and we have to translate to NN, or force cliente to send the correct way
    val (splittedWords, tags) = tagText(text)

    lazy val arrLength = splittedWords.length

    //Iterate through the words (that have been slipped by whitespaces)
    //if we find a word that equal to the passed keyword
    // then search from that point forward for a word whose POS tag matches the one passed by arguments
    val valuesList: List[String] = (for (i <- splittedWords.indices if splittedWords(i).toLowerCase == keyword.toLowerCase) yield {
      if (i < arrLength) {
        for (j <- i + 1 until arrLength) {
          println(tags(j))
        }
        val wordList = for (j <- i + 1 until arrLength if tags(j) == tag) yield splittedWords(j)
        if (wordList.nonEmpty) wordList.head else null
      } else {
        null //In case the keyword found is the last word in the text we're not going to find a value for it
      }
    }).toList
    (keyword, valuesList.filter(_ != null))
  }

  /**
    * Method that initializes the regular expressions from the given language identified in the pdf
    *
    * @param lang - String containing the language identified
    * @return - A Map containing all RegEx defined for each keyword
    */
  def importRegExFile(lang: String): Map[Keyword, Regex] = {
    val fileLines = Source.fromFile("./reader/resources/regex/" + lang + ".txt").getLines
    fileLines.map(l => {
      val splitLine = l.split(";")
      splitLine(0) -> splitLine(1).r
    }).toMap
  }
}