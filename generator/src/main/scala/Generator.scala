import java.io._
import com.itextpdf.text.Document
import com.itextpdf.text.pdf.PdfWriter
import com.itextpdf.tool.xml.XMLWorkerHelper
import org.json4s._
import org.json4s.native.JsonMethods._
import scalatags.Text.all._
import scala.io.Source

/**
  * Singleton object that implements the functions regarding the pdf file generation
  */
object Generator {

  /**
    * Method that receives a JSON string to be parsed and converted into a HTML file, and then into a PDF file.
    * This method overload implements the user decision to not send any additional CSS
    *
    * @param json - The Json string to be converted into a PDF document
    * @return a Boolean saying if the conversion from JSON to PDF was successful or not
    */
  def convertJSONtoPDF(json: String): Boolean = {
    val htmlURI = writeHTML(json).getOrElse("")
    convertHTMLToPDF(htmlURI)
  }

  /**
    * Method that receives a JSON string to be parsed and converted into a HTML file, and then into a PDF file.
    * This method overload implements the user decision to send an additional CSS file to be included in the HTML file
    *
    * @param json    - The Json string to be converted into a PDF document
    * @param cssFile - The additional CSS file to be included in the HTML file
    * @return a Boolean saying if the conversion from JSON to PDF was successful or not
    */
  def convertJSONtoPDF(json: String, cssFile: File): Boolean = {
    val htmlURI = writeHTML(json, cssFile).getOrElse("")
    convertHTMLToPDF(htmlURI)
  }

  /**
    * Method that receives a JSON string to be parsed and converted into a HTML file, and then into a PDF file.
    * This method overload implements the user decision to send an additional String containing the desired CSS to be included
    *
    * @param json      - The Json string to be converted into a PDF document
    * @param cssString - The additional String containing the the CSS to be included in the HTML file
    * @return a Boolean saying if the conversion from JSON to PDF was successful or not
    */
  def convertJSONtoPDF(json: String, cssString: String): Boolean = {
    val htmlURI = writeHTML(json, cssString).getOrElse("")
    convertHTMLToPDF(htmlURI)
  }

  /**
    * Method that receives a JSON string to be parsed and converted into a HTML file, and then into a PDF file.
    * This method overload implements the user decision to send an additional config file specifying simple styling details to be implemented
    *
    * @param json   - The Json string to be converted into a PDF document
    * @param config - The config specifying simple styling details to be implemented in the PDF conversion
    * @return a Boolean saying if the conversion from JSON to PDF was successful or not
    */
  def convertJSONtoPDF(json: String, config: Config): Boolean = {
    val htmlURI = writeHTML(json, config).getOrElse("")
    convertHTMLToPDF(htmlURI)
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
        val writer = PdfWriter.getInstance(document, new FileOutputStream("html.pdf")) //TODO change destination folder to something else
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

  /**
    * Method that calls createHTML. This method overload implements the user decision of not sending additional CSS
    *
    * @param json - The JSON string to be parsed
    * @return An Option wrapping the URI of the created HTML file. Returns None in case of exception when parsing the JSON String
    */
  private def writeHTML(json: String): Option[String] = {
    createHtml(json)
  }

  /**
    * Method that calls createHTML. This method overload implements the user decision to send an additional CSS file
    *
    * @param json    - The JSON string to be parsed
    * @param cssFile - the URI of the created html file
    * @return An Option wrapping the URI of the created HTML file. Returns None in case of exception when parsing the JSON String
    */
  private def writeHTML(json: String, cssFile: File): Option[String] = {
    try {
      val bufferedSource = Source.fromFile(cssFile)
      val cssStr = bufferedSource.getLines.mkString
      createHtml(json, cssStr)
    } catch {
      case e: Exception => e.printStackTrace(); None
    }
  }

  /**
    * Method that calls createHTML.
    * This method overload implements the user decision to send an additional String containing the desired CSS
    *
    * @param json      - The JSON string to be parsed
    * @param cssString - The String containing the desired CSS to be included in the HTML file
    * @return An Option wrapping the URI of the created HTML file. Returns None in case of exception when parsing the JSON String
    */
  private def writeHTML(json: String, cssString: String): Option[String] = {
    createHtml(json, cssString)
  }

  /**
    * Method that calls createHTML.
    * This method overload implements the user decision to send an additional Config object containing simple styling details
    *
    * @param json   - The JSON string to be parsed
    * @param config - The Config object containing simple styling details
    * @return An Option wrapping the URI of the created HTML file. Returns None in case of exception when parsing the JSON String
    */
  private def writeHTML(json: String, config: Config): Option[String] = {
    val cssString =
      "body{" +
        "font-weight: " + config.fontWeight + ";" +
        " color: " + config.textColor + ";" +
        " font-family: " + config.fontFamily + ";" +
        " text-align: " + config.textAlignment + ";" +
        "font-size: " + config.fontSize + "pt;" +
        "}"
    createHtml(json, cssString)
  }

  /**
    * Method that implement's the creation and witting of the html file generated from the passed JSON string
    * This method is used by all writeHTML overloads
    *
    * @param json      - The JSON String to be parsed
    * @param cssString - A String containing all the desired CSS to be included in the HTML (to then be transformed to pdf)
    * @return An Option wrapping the URI of the created HTML file. Returns None in case of exception when parsing the JSON String
    */
  private def createHtml(json: String, cssString: String = ""
                        ): Option[String] = {
    try {
      val jsonMap = parse(json).values.asInstanceOf[Map[String, Any]] //parses JSON string to a Map[String, Any]
      if (jsonMap.nonEmpty) {
        val kvParagraph = jsonMap.map(pair => p(pair._1 + " : " + pair._2)).toList

        val htmlString =
          if (cssString.isEmpty) html(head(), body(kvParagraph)).toString //generate html code with no css
          else {
            val str = html(head(), body(kvParagraph)).toString
            val (left, right) = str.splitAt(12) //split html string at index 12, right in between the <head> tag
            s"$left <style> $cssString </style> $right" //creates the desired HTML code with a <style> tag containing the user-sent css
          }

        val filePath = "./target/htmlPages/" + System.nanoTime() + ".html"
        val pw = new PrintWriter(new File(filePath)) //prints the HTML code to the html file
        pw.write(htmlString)
        pw.close()
        Option(filePath)
      } else None
    } catch {
      case e: Exception => e.printStackTrace(); None
    }
  }

  /**
    * Method that deletes all the files in the htmlPages directory
    */
  private def cleanHTMLDir() {
    val dir = new File("./target/htmlPages")
    val files = dir.listFiles.filter(_.isFile).toList
    files.foreach(_.delete)
  }

}


