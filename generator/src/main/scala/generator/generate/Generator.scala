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
  type ContentMap = Map[Keyword, Content]
  type ConfigMap = Map[String, Config]
  type JSONString = String


  /**
    * Method that receives a JSON string and parses it into a Map[String,Any]
    *
    * @param json - The JSON string to be parsed
    * @return a Map[String,Any] with the information parsed from the JSON
    */
  def convertJSONtoMap(json: JSONString): Option[Map[Keyword, Any]] = { //TODO Maybe change this to private
    try {
      Some(parse(json).values.asInstanceOf[Map[String, Any]])
    } catch {
      case e: Exception => e.printStackTrace(); None
    }
  }

  /**
    * Method that converts an object (ContentMap) into a PDF by calling the a common method internalMapConverter
    *
    * @param content - An object containing all the keywords and their Content(a object containig fieldName, fieldValue and formattingType)
    * @return a Boolean with information specifying if the conversion was successful or not
    */
  def convertMapToPDF(content: ContentMap): Boolean = {
    internalMapConverter(content, "")
  }

  /**
    * Method that converts an object (ContentMap) into a PDF by calling the a common method internalMapConverter
    *
    * @param content - An object containing all the keywords and their Content(a object containig fieldName, fieldValue and formattingType)
    * @param cssFile - A CSS file containing all the CSS code to be added to the HTML file
    * @return a Boolean with information specifying if the conversion was successful or not
    */
  def convertMapToPDF(content: ContentMap, cssFile: File): Boolean = {
    convertWithFile(content, cssFile)
  }

  /**
    * Method that converts an object (ContentMap) into a PDF by calling the a common method internalMapConverter
    *
    * @param content   - An object containing all the keywords and their Content(a object containig fieldName, fieldValue and formattingType)
    * @param cssString - A String containing all the CSS code to be added to the HTML file
    * @return a Boolean with information specifying if the conversion was successful or not
    */
  def convertMapToPDF(content: ContentMap, cssString: String): Boolean = {
    internalMapConverter(content, cssString)
  }

  /**
    * Method that converts an object (ContentMap) into a PDF by calling the a common method internalMapConverter
    *
    * @param content    - An object containing all the keywords and their Content(a object containig fieldName, fieldValue and formattingType)
    * @param formatting - An object containing all the user defined formattings specifying how each keyword in the content object
    * @return a Boolean with information specifying if the conversion was successful or not
    */
  def convertMapToPDF(content: ContentMap, formatting: ConfigMap): Boolean = {
    internalMapConverter(content, createCssString(content, formatting))
  }

  /**
    * Method that converts a JSON string, with information regarding the content to be displayed in the PDF file, into a PDF file
    *
    * @param contentJSON - JSON string with information regarding the content to be displayed in the PDF file
    * @return a Boolean specifying if the conversion was successful or not
    */
  def convertJSONtoPDF(contentJSON: JSONString): Boolean = {
    val convertedContentOpt = jsonToContent(contentJSON) //Try to convert the JSON content into a ContentMap
    convertedContentOpt match {
      case Some(convertContent) => internalMapConverter(convertContent, "")
      case _ => false
    }
  }

  /**
    * Method that converts a JSON string with information regarding the content to be displayed in the PDF file, and a
    * JSON string with information regarding how that information should be displayed, into a PDF file
    *
    * @param contentJSON    - JSON string with information regarding the content to be displayed in the PDF file
    * @param formattingJSON - JSON string with information regarding how the content should be displayed
    * @return a Boolean specifying if the conversion was successful or not
    */
  def convertJSONtoPDF(contentJSON: JSONString, formattingJSON: JSONString): Boolean = {
    val convertedContentOpt = jsonToContent(contentJSON) //Try to convert the JSON content into a ContentMap
    val convertedFormattingOpt = jsonToFormatting(formattingJSON) //Try to convert the JSON formatting into a ContentFormatting
    if (convertedContentOpt.isDefined && convertedFormattingOpt.isDefined) { //If both Options are defined (Some) then proceed with conversion
      val convertedContent = convertedContentOpt.get
      internalMapConverter(convertedContent, createCssString(convertedContent, convertedFormattingOpt.get))
    } else false
  }

  /**
    * Method that converts a JSON string with information regarding the content to be displayed in the PDF file, and a
    * JSON string with information regarding how that information should be displayed, into a PDF file
    *
    * @param contentJSON - JSON string with information regarding the content to be displayed in the PDF file
    * @param cssFile     - A CSS file containing all the CSS code to be added to the HTML file
    * @return a Boolean specifying if the conversion was successful or not
    */
  def convertJSONtoPDF(contentJSON: JSONString, cssFile: File): Boolean = {
    val convertedContentOpt = jsonToContent(contentJSON) //Try to convert the JSON content into a ContentMap
    convertedContentOpt match {
      case Some(convertedContent) => convertWithFile(convertedContent, cssFile)
      case _ => false
    }
  }

  /**
    * Method that converts a JSON string with information regarding the content to be displayed in the PDF file, and a
    * JSON string with information regarding how that information should be displayed, into a PDF file
    *
    * @param contentJSON - JSON string with information regarding the content to be displayed in the PDF file
    * @param cssString   - A String containing all the CSS code to be added to the HTML file
    * @return a Boolean specifying if the conversion was successful or not
    */
  def convertJSONtoPDFWithCSS(contentJSON: JSONString, cssString: String): Boolean = { //TODO how do i remove the unnecessary withCSS part with out conflict with line 80
    val convertedContentOpt = jsonToContent(contentJSON) //Try to convert the JSON content into a ContentMap
    convertedContentOpt match {
      case Some(convertedContent) => internalMapConverter(convertedContent, cssString)
      case _ => false
    }
  }

  /**
    * Method that takes a ContentMap and a CSS file, reads the CSS file and then converts the ContentMap and CSS code
    * into a PDF file
    *
    * @param content - An object containing all the keywords and their Content(a object containig fieldName, fieldValue and formattingType)
    * @param cssFile - A CSS file containing all the CSS code to be added to the HTML file
    * @return a Boolean with information specifying if the conversion was successful or not
    */
  private def convertWithFile(content: ContentMap, cssFile: File): Boolean = {
    val bufferedSourceOption = loadCSSFile(cssFile)
    bufferedSourceOption match {
      case Some(bufferedSource) =>
        val cssString = bufferedSource.getLines.mkString
        internalMapConverter(content, cssString)
      case _ => false
    }
  }

  /**
    * Method that tries to convert a JSON string into a Map of keywords and Content objects
    *
    * @param contentJSON - The JSON string to be converted
    * @return a Map of keywords and Content objects
    */
  private def jsonToContent(contentJSON: JSONString): Option[ContentMap] = {
    val parsedJSONOpt = convertJSONtoMap(contentJSON)
    parsedJSONOpt match {
      case Some(parsedJSON) =>
        val returnMap = parsedJSON.map { case (keyword, content) =>
          content match {
            case contentMap: Map[String, Any] =>
              val fieldName = contentMap.getOrElse("fieldName", "N/A").toString
              val fieldValue = contentMap.getOrElse("fieldValue", "N/A") //TODO Maybe change N/A ??
            val htmlEntity = stringToHTMLEntity(contentMap.getOrElse("htmlEntity", "").toString)
              val cssClass = contentMap.getOrElse("cssClass", "").toString

              (keyword, Content(fieldName, fieldValue, htmlEntity, cssClass))

            case _ => ("", Content("", "", H1()))
          }
        }.filter(_._1.nonEmpty).toList.toMap
        Some(returnMap)
      case None => None
    }
  }

  /**
    * Method that tries to convert a JSON string into a Map of keywords and Config objects
    *
    * @param formattingJSON - The JSON string to be converted
    * @return a Map of keywords and Config objects
    */
  private def jsonToFormatting(formattingJSON: JSONString): Option[ConfigMap] = {
    val parsedJSONOpt = convertJSONtoMap(formattingJSON)
    parsedJSONOpt match {
      case Some(parsedJSON) =>
        val formattingMap = parsedJSON.map { case (keyword, conf) =>
          conf match {
            case configMap: Map[String, Any] =>
              val color = configMap.getOrElse("color", "").toString
              val fontSize = configMap.getOrElse("fontSize", "").toString
              val textAlignment = configMap.getOrElse("textAlignment", "").toString
              val fontFamily = configMap.getOrElse("fontFamily", "").toString
              val fontWeight = configMap.getOrElse("fontWeight", "").toString

              (keyword, Config(color, fontSize, textAlignment, fontFamily, fontWeight))

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
  private def internalMapConverter(content: ContentMap, cssString: String): Boolean = {
    val htmlString = writeHTMLString(content, cssString) //Generate the HTML String
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


