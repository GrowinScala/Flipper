package parser.extraction

import java.io.File
import java.text.Normalizer

import parser.utils.ImageProcessing._
import parser.utils.SpellChecker._
import parser.utils.OpenNLP._
import parser.utils._
import org.apache.pdfbox.text.PDFTextStripper

import scala.util.matching.Regex
import FileHandler._

import scala.annotation.tailrec
import scala.io.Source

/**
  * Singleton object that implements all the functionality regarding the extraction of information from a PDF document
  */
object Extractor {

  type Keyword = String
  type MatchedPair = Map[Keyword, Seq[String]]

  /**
    * Method that given a file path (maybe change to a real file) will load that PDF file and read the text from it
    *
    * @param file - File to be loaded and parsed
    * @return An Option wrapping a String containing all the text found in the document. Returns None in case of Exception
    */
  def readPDF(file: File, readImages: Boolean = true): Option[String] = {
    val pdfOption = loadPDF(file)
    pdfOption match {
      case Some(pdf) =>
        val document: PDFTextStripper = new PDFTextStripper
        val str = Normalizer.normalize(document.getText(pdf), Normalizer.Form.NFD)
          .replaceAll("[\\p{InCombiningDiacriticalMarks}]", "")

        val imgText =
          if (readImages) {
            val imageList = extractImgs(pdf).getOrElse(List())
            val imageTexts = imageList.map(img => readImageText(img).getOrElse("")).mkString

            correctText(imageTexts)
          } else ""

        pdf.close()
        cleanImageDir()
        val joinedText = imgText + str
        if (joinedText.nonEmpty) Some(joinedText)
        else None
      case _ => None
    }
  }

  /**
    * Method that will iterate through a list of given keywords and will try to obtain a value for that keyword
    *
    * @param text        - Text in which to look for values for the specified keywords
    * @param keywords    - List containing all the keywords we want to find values for
    * @param clientRegEx - Optional parameter - If the client already has a predefined Regular Expression for a given key
    *                    use that regular expression instead of ours
    * @throws IllegalArgumentException If the keywords list is empty
    * @return List containing pairs of Keywords and a List (non-repeating) of values found for that keyword
    */
  @throws[IllegalArgumentException]
  def getAllMatchedValues(text: Option[String], keywords: Map[Keyword, Specification], clientRegEx: Map[Keyword, Regex] = Map()): MatchedPair = {
    require(keywords.nonEmpty, "The list of keywords should not be empty")
    text match {
      case Some(t) =>
        if (t.nonEmpty) {
          val knownRegEx: Map[String, Regex] = importRegExFile(t) //load correct RegEx map
          val matched: MatchedPair = keywords.map { case (key, spec) =>
            spec match {
              case tag: POSTag =>

                //If the client sent a custom RegEx to use on this key, use it
                if (clientRegEx.contains(key)) //&& clientRegEx != null ??
                  (key, clientRegEx(key).findAllIn(t).matchData.map(_.group(1)).toList.distinct)

                //if we already know a good RegEx for this keyword, use it
                else if (knownRegEx.contains(key))
                  (key, knownRegEx(key).findAllIn(t).matchData.map(_.group(1)).toList.distinct)

                else findKeywordInText(key, tag, t) //to be changed, here we need to manually search for the keywords in the text

              case multiOp: MultipleOf =>
                (key, getOptions(text, key, multiOp.possibilities))
              case oneOp: OneOf =>
                val valuesFound = getOptions(text, key, oneOp.possibilities)
                if (valuesFound.nonEmpty) (key, List(valuesFound.head))
                else (key, List())
            }
          }
          filterNewLines(matched)
        }
        else {
          Map()
        }
      case None => Map()
    }
  }

  /**
    * Method that will iterate through a list of given keywords and will try to obtain only the first value it finds for a given
    * keyword, representing a single JSON object
    *
    * @param text        - Text in which to look for values for the specified keywords
    * @param keywords    - List containing all the keywords we want to find values for
    * @param clientRegEx - Optional parameter - If the client already has a predefined Regular Expression for a given key
    * @throws IllegalArgumentException If the keywords list is empty
    * @return A List containing pairs of keywords with a single matched value
    */
  @throws[IllegalArgumentException]
  def getSingleMatchedValue(text: Option[String], keywords: Map[Keyword, Specification], clientRegEx: Map[Keyword, Regex] = Map()): MatchedPair = {
    require(keywords.nonEmpty, "The list of keywords should not be empty")
    text match {
      case Some(_) =>
        getAllMatchedValues(text, keywords, clientRegEx).map { case (k, v) =>
          v.headOption match {
            case Some(entry) => (k, Seq(entry))
            case None => (k, Seq())
          }
        }
      case _ => Map()
    }
  }

  /**
    * Method that will iterate through a list of given keywords and will try to obtain a list containing
    * sub-lists that have all keywords and only one value for each of them (representing a single JSON object for each of the sub-lists)
    *
    * @param text        - Text in which to look for values for the specified keywords
    * @param keywords    - List containing all the keywords we want to find values for
    * @param clientRegEx - Optional parameter - If the client already has a predefined Regular Expression for a given key
    * @throws IllegalArgumentException If the keywords list is empty
    * @return A List containing sub-lists of pairs of keywords with single matched values
    */
  @throws[IllegalArgumentException]
  def getAllObjects(text: Option[String], keywords: Map[Keyword, Specification], clientRegEx: Map[Keyword, Regex] = Map()): List[MatchedPair] = {
    require(keywords.nonEmpty, "The list of keywords should not be empty")
    text match {
      case Some(t) =>
        if (t.nonEmpty) {
          val matchedValues = getAllMatchedValues(text, keywords, clientRegEx)
          val mostFound = matchedValues.maxBy(_._2.size)._2.size //Gets the size of the pair that has the most values
          val mappedValues = for (i <- 0 until mostFound; (_, listMatched) <- matchedValues) yield {
            if (listMatched.size > i) //Prevent array out of bounds exception
              List(listMatched(i))
            else List()
          }
          val keywordList: List[String] = keywords.keys.toList
          val joinedValues = mappedValues.zipWithIndex.map { case (key, index) => Map(keywordList(index % keywords.size) -> key) }.grouped(keywords.size).toList
          joinedValues.map(_.flatten.toMap)
        }
        else {
          List()
        }
      case None => List()
    }
  }


  /**
    * Method that encapsulates the entire process of finding values for the given keywords list and converting the MatchedPair type to a JSON Object
    *
    * @param text        - Text in which to look for values for the specified keywords
    * @param keywords    - List containing all the keywords we want to find values for
    * @param flag        - Optional flag with information on how to return non-existing values
    * @param clientRegEx - Optional parameter - If the client already has a predefined Regular Expression for a given key
    * @throws IllegalArgumentException If the keywords list is empty
    * @return a List of Strings representing a JSON object for each MatchedPair type
    */
  @throws[IllegalArgumentException]
  def getJSONObjects(text: Option[String], keywords: Map[Keyword, Specification], flag: String = "empty", clientRegEx: Map[Keyword, Regex] = Map()): List[String] = {
    require(keywords.nonEmpty, "The list of keywords should not be empty")
    val objs = getAllObjects(text, keywords, clientRegEx)
    objs.map(makeJSONString(_, flag))
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
    * @param flag     - Optional flag with information on how to return non-existing values
    * @return a JSON string
    */
  def makeJSONString(listJSON: MatchedPair, flag: String = "empty"): String = {
    def isAllDigits(x: String) = try {
      x.toDouble; true
    } catch {
      case _: Exception => false
    }

    lazy val quote = "\""

    val pseudoJSON = listJSON.map { case (key, matchedList) =>
      matchedList match {
        case _: List[String] =>
          if (matchedList.nonEmpty) {
            if (matchedList.size > 1) {
              val left = s"${quote + key + quote} : "
              val right = "[" + matchedList.map(str => if (isAllDigits(str)) str + ", " else s"${quote + str + quote}, ").mkString + "]"
              (left + right).replaceAll(", ]", "]") //Remove trailing commas
            } else {
              lazy val headValue = matchedList.head
              if (isAllDigits(headValue))
                s"${quote + key + quote} : $headValue"
              else
                s"${quote + key + quote} : ${quote + headValue + quote}"
            }
          } else {
            flag match {
              case "empty" => s"${quote + key + quote} : ${quote + quote}"
              case "null" => s"${quote + key + quote} : null"
            }
          }
        case _ =>
          flag match {
            case "empty" => s"${quote + key + quote} : ${quote + quote}"
            case "null" => s"${quote + key + quote} : null"
          }
      }
    }

    flag match {
      case "remove" => pseudoJSON.filter(_ != ()).mkString("{", ", ", "}")
      case _ => pseudoJSON.mkString("{", ", ", "}")
    }
  }

  /**
    * Method that gets all keywords and respective values from know form and returns a JSON string
    *
    * @param text - Text in which to look for key-value pairs
    * @return - A JSON String containing all the information in the text passed by arguments
    */
  def getJSONFromForm(text: Option[String]): String = {
    val textContent = text.getOrElse("")
    val formRegex = "(.+):\\s+(.+)".r
    val form = formRegex.findAllIn(textContent).matchData.map(l => (l.group(1), List(l.group(2)))).toMap
    makeJSONString(form)
  }

  /**
    * Method that will remove all the new line characters from the list of values obtain from a keyword
    *
    * @param matchedValues - List of pairs of Keyword and the values obtained for that keyword
    * @return The same list as passed by parameter but with no new line characters
    */
  private def filterNewLines(matchedValues: MatchedPair): MatchedPair = {
    matchedValues.map { case (key, matchedList) =>
      val setOfValues = matchedList
      (key, setOfValues.map(_.replaceAll("[\\r\\n]", "").trim)) //remove all new line characters and trim all elements
    }
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
  private def findKeywordInText(keyword: Keyword, tag: POSTag, text: String): (Keyword, List[String]) = {
    val (splittedWords, tags) = tagText(text)

    val arrLength = splittedWords.length

    //Iterate through the words (that have been slipped by whitespaces)
    //if we find a word that equal to the passed keyword
    // then search from that point forward for a word whose POS tag matches the one passed by arguments
    val valuesList: List[String] = (for (i <- splittedWords.indices if splittedWords(i).toLowerCase == keyword.toLowerCase) yield {
      if (i < arrLength) {
        val wordList = for (j <- i + 1 until arrLength if tags(j) == tag.value) yield splittedWords(j)
        if (wordList.nonEmpty) wordList.head else ""
      } else {
        "" //In case the keyword found is the last word in the text we're not going to find a value for it
      }
    }).toList

    val badStr = " .,;:"
    val cleanList: List[String] = valuesList.filter(_ != "").map(s => strClean(s, badStr))

    (keyword, cleanList)
  }

  /**
    * Method that initializes the regular expressions from the given language identified in the pdf
    *
    * @param text - The text extracted from the pdf document
    * @return - A Map containing all RegEx defined for each keyword
    */
  private def importRegExFile(text: String): Map[Keyword, Regex] = {
    val lang = detectLanguage(text) match {
      case "por" => "por"
      case _ => "eng"
    }
    val file = getClass.getResourceAsStream("/regex/" + lang + ".txt")
    //    val file = getClass.getResource("/regex/"+lang+".txt")
    //    val fileLines = Source.fromFile(file.toURI).getLines //Should we try and catch a file not found exception here ?
    val fileLines = Source.fromInputStream(file).getLines
    fileLines.map(l => {
      val splitLine = l.split(";")
      splitLine(0) -> splitLine(1).r
    }).toMap
  }

  /**
    * Method that takes 2 input strings, one to clean up and one with the possible characters to be removed.
    * This method removes all the unwanted characters in the beginning and end of a string
    *
    * @param s   - String to clean up
    * @param bad - String with the characters to be rejected
    * @return - Clean string
    */
  private def strClean(s: String, bad: String): String = {

    @tailrec def start(n: Int): String =
      if (n == s.length) ""
      else if (bad.indexOf(s.charAt(n)) < 0) end(n, s.length)
      else start(1 + n)

    @tailrec def end(a: Int, n: Int): String =
      if (n <= a) s.substring(a, n)
      else if (bad.indexOf(s.charAt(n - 1)) < 0) s.substring(a, n)
      else end(a, n - 1)

    start(0)
  }

  /**
    * Find in text one or more of the options given by the user realted to the keyword
    *
    * @param text    - The text in which to look for the value
    * @param keyword - The keyword to find the value for
    * @param opList  - List of options to choose from
    * @return - A list of all the matched options found
    */
  def getOptions(text: Option[String], keyword: Keyword, opList: List[String]): List[String] = {
    text match {
      case Some(t) =>
        if (t.toLowerCase.contains(keyword.toLowerCase)) {
          val found = for (op <- opList if t.toLowerCase.contains(op.toLowerCase)) yield op
          found
        } else {
          List()
        }
      case None => List()
    }
  }

}