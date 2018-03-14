import java.io.{File, FileInputStream, FileOutputStream, PrintWriter}
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
    * @param htmlURI - The URI of the html document (It's location in the project directory)
    */
  def convertHTMLtoPDF(htmlURI: String): Boolean = {
    try {
      val document = new Document()
      val writer = PdfWriter.getInstance(document, new FileOutputStream("html.pdf")) //TODO change destination folder to something else
      document.open()
      XMLWorkerHelper.getInstance().parseXHtml(writer, document, new FileInputStream(htmlURI))
      document.close()
      true
    } catch {
      case e: Exception => false
    }
  }

  /**
    * Method that takes a JSON string as parameter and parses it to write an html file
    *
    * @param json - The JSON string to be parsed
    * @return the URI of the created html file
    */
  def writeHTML(json: String): String = {
    var jsonMap: Map[String, Any] = Map()
    try {
      jsonMap = parse(json).values.asInstanceOf[Map[String, Any]] //parses JSON string to a Map[String, Any]
    } catch {
      case e: Exception => jsonMap = Map() //parsing failed, something's wrong with the json string
    }
    if (jsonMap.nonEmpty) {
      val kvParagraph = jsonMap.map(pair => p(pair._1 + " : " + pair._2)).toList
      val htmlString = html(head(), body(kvParagraph)) //generates the html code to go on the html file
      val filepath = "./target/htmlPages/" + System.nanoTime() + ".html"
      val pw = new PrintWriter(new File(filepath)) //prints the html code to the html file
      pw.write(htmlString.toString)
      pw.close()
      filepath
    } else "" //TODO Or change this to null ???
  }

}
