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
  def convertHTMLtoPDF(htmlURI: String): Unit = {
    val document = new Document()
    val writer = PdfWriter.getInstance(document, new FileOutputStream("html.pdf")) //TODO change destination folder to something else
    document.open()
    XMLWorkerHelper.getInstance().parseXHtml(writer, document, new FileInputStream(htmlURI))
    document.close()
  }

  //TODO ADD DOCUMENTATION
  def writeHTML(json: String): String = {
    val jsonMap = parse(json).values.asInstanceOf[Map[String, Any]]
    val kvParagraph = jsonMap.map(pair => p(pair._1 + " : " + pair._2)).toList
    val htmlString = html(
      head(),
      body(kvParagraph)
    )
    val filepath = "./target/htmlPages/" + System.nanoTime() + ".html"
    val pw = new PrintWriter(new File(filepath))
    pw.write(htmlString.toString)
    pw.close()
    filepath
  }

}
