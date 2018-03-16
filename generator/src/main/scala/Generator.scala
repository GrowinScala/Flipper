import java.io._

import com.itextpdf.text.Document
import com.itextpdf.text.pdf.PdfWriter
import com.itextpdf.tool.xml.XMLWorkerHelper
import org.json4s._
import org.json4s.native.JsonMethods._
import scalatags.Text.all._

object Generator {

  /**
    * Method that receives the URI of a html file and generates from it a pdf document
    *
    * @param json - The Json string to be converted into a PDF document
    * @return a Boolean saying if the conversion from HTML to PDF was successful or not
    */
  def convertJSONtoPDF(json: String): Boolean = {
    val htmlURI = writeHTML(json).getOrElse("")
    if (htmlURI.nonEmpty) {
      try {
        val document = new Document()
        val writer = PdfWriter.getInstance(document, new FileOutputStream("html.pdf")) //TODO change destination folder to something else
        document.open()
        XMLWorkerHelper.getInstance().parseXHtml(writer, document, new FileInputStream(htmlURI))
        document.close()
        true
      } catch {
        case e: Exception => e.printStackTrace(); false
      }
    } else false
  }

  /**
    * Method that takes a JSON string as parameter and parses it to write an html file
    *
    * @param json - The JSON string to be parsed
    * @return the URI of the created html file
    */
  private def writeHTML(json: String): Option[String] = {
    try {
      val jsonMap = parse(json).values.asInstanceOf[Map[String, Any]] //parses JSON string to a Map[String, Any]
      if (jsonMap.nonEmpty) {
        val kvParagraph = jsonMap.map(pair => p(pair._1 + " : " + pair._2)).toList
        val htmlString = html(head(), body(kvParagraph)) //generates the html code to go on the html file
        val filepath = "./target/htmlPages/" + System.nanoTime() + ".html"
        val pw = new PrintWriter(new File(filepath)) //prints the html code to the html file
        pw.write(htmlString.toString)
        pw.close()
        Option(filepath)
      } else None
    } catch {
      case e: Exception => e.printStackTrace(); None
    }
  }
}