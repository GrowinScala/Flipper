package generator.generate

import java.io._

import com.itextpdf.text.Document
import com.itextpdf.text.pdf.PdfWriter
import com.itextpdf.tool.xml.XMLWorkerHelper
import org.json4s._
import FileHandler._
import HTMLHandler._
import org.json4s.native.JsonMethods._
import generator.utils.{Config, Content, H1}

/**
  * Singleton object that implements the functions regarding the pdf file generation
  */
object Generator {

  type Keyword = String


  /**
    * Method that receives a JSON string and parses it into a Map[String,Any]
    *
    * @param json - The JSON string to be parsed
    * @return a Map[String,Any] with the information parsed from the JSON
    */
  def convertJSONtoMap(json: String): Option[Map[Keyword, Any]] = {
    try {
      Some(parse(json).values.asInstanceOf[Map[String, Any]])
    } catch {
      case e: Exception => e.printStackTrace(); None
    }
  }

  /**
    * Method that converts an object (Map[Keyword, Content])) into a PDF by calling the a common method internalMapConverter
    *
    * @param content - An object containing all the keywords and their Content(a object containig fieldName, fieldValue and formattingType)
    * @param cssFile - A CSS file containing all the CSS code to be added to the HTML file
    * @return a Boolean with information specifying if the conversion was successful or not
    */
  def convertMapToPDF(content: Map[Keyword, Content], cssFile: File): Boolean = {
    val bufferedSourceOption = loadCSSFile(cssFile)
    bufferedSourceOption match {
      case Some(bufferedSource) =>
        val cssString = bufferedSource.getLines.mkString
        internalMapConverter(content, cssString)
      case _ => false
    }
  }

  /**
    * Method that converts an object (Map[Keyword, Content])) into a PDF by calling the a common method internalMapConverter
    *
    * @param content   - An object containing all the keywords and their Content(a object containig fieldName, fieldValue and formattingType)
    * @param cssString - A String containing all the CSS code to be added to the HTML file
    * @return a Boolean with information specifying if the conversion was successful or not
    */
  def convertMapToPDF(content: Map[Keyword, Content], cssString: String): Boolean = {
    internalMapConverter(content, cssString)
  }

  /**
    * Method that converts an object (Map[Keyword, Content])) into a PDF by calling the a common method internalMapConverter
    *
    * @param content    - An object containing all the keywords and their Content(a object containig fieldName, fieldValue and formattingType)
    * @param formatting - An object containing all the user defined formattings specifying how each keyword in the content object
    * @return a Boolean with information specifying if the conversion was successful or not
    */
  def convertMapToPDF(content: Map[Keyword, Content], formatting: Map[String, Config]): Boolean = {
    internalMapConverter(content, createCssString(content, formatting))
  }

  def convertJSONtoPDF(contentJSON: String, formattingJSON: String): Boolean = {
    val convertedContent = jsonToContent(contentJSON)
    val convertedFormatting = jsonToFormatting(formattingJSON)
    println(convertedContent)
    println(convertedFormatting)
    false
  }

  private def jsonToContent(contentJSON: String): Option[Map[Keyword, Content]] = {
    val parsedJSONOpt = convertJSONtoMap(contentJSON)
    parsedJSONOpt match {
      case Some(parsedJSON) =>
        val returnMap = parsedJSON.map { case (keyword, content) =>
          content match {
            case contentMap: Map[String, Any] =>
              val fieldName = contentMap.getOrElse("fieldName", "").toString
              val fieldValue = contentMap.getOrElse("fieldValue", "")
              val htmlTag = stringToHTMLTag(contentMap.getOrElse("HTMLTag", "").toString)
              val cssClass = contentMap.getOrElse("cssClass", "").toString

              (keyword, Content(fieldName, fieldValue, htmlTag, cssClass))

            case _ => ("", Content("", "", H1()))
          }
        }.filter(_._1.nonEmpty).toList.toMap
        Some(returnMap)
      case None => None
    }
  }

  private def jsonToFormatting(formattingJSON: String): Option[Map[Keyword, Config]] = {
    val parsedJSONOpt = convertJSONtoMap(formattingJSON)
    parsedJSONOpt match {
      case Some(parsedJSON) =>
        val formattingMap = parsedJSON.map { case (keyword, conf) =>
          conf match {
            case configMap: Map[String, Any] =>
              val textColor = configMap.getOrElse("textColor", "").toString
              val fontSize = configMap.getOrElse("fontSize", "").toString
              val textAlignment = configMap.getOrElse("textAlignment", "").toString
              val fontFamily = configMap.getOrElse("fontFamily", "").toString
              val fontWeight = configMap.getOrElse("fontWeight", "").toString

              (keyword, Config(textColor, fontSize, textAlignment, fontFamily, fontWeight))

            case _ => ("", Config())
          }
        }.filter(_._1.nonEmpty).toList.toMap
        Some(formattingMap)
      case None => None
    }
  }

  /**
    * Auxaliary method that actually converts an object (Map[Keyword, Content])) into a PDF.
    * This method achieve its objective by first converting the input object into a HTML file, and then into a PDF file
    *
    * @param content   - An object containing all the keywords and their Content(a object containig fieldName, fieldValue and formattingType)
    * @param cssString - A String containing all the CSS code to be added to the HTML file
    * @return a Boolean with information specifying if the conversion was successful or not
    */
  private def internalMapConverter(content: Map[Keyword, Content], cssString: String): Boolean = {
    val htmlString = writeHTMLString(content, cssString) //generator.generate the HTML String
    val filePath = generateDocument(htmlString) //Create a HTML file from that HTML String
    convertHTMLToPDF(filePath) //Convert the HTML file into a PDF file
  }

  /**
    * Method that implements the conversion from a HTML file to a PDF file.
    * This method is called by all convertJSONtoPDF overloads
    *
    * @param htmlURI - The HTML URI specifying where the this document is placed
    * @return a Boolean saying if the conversion from HTML to PDF was successful or not
    */
  private def convertHTMLToPDF(htmlURI: String): Boolean = {
    if (htmlURI.nonEmpty) {
      try {
        val document = new Document()
        val writer = PdfWriter.getInstance(document, new FileOutputStream("html.pdf"))
        document.open()
        XMLWorkerHelper.getInstance().parseXHtml(writer, document, new FileInputStream(htmlURI))
        document.close()
        cleanHTMLDir()
        true
      } catch {
        case e: Exception => e.printStackTrace(); false
      }
    } else false
  }
}


