import java.awt.Color
import java.io.{File, FileInputStream, FileOutputStream, PrintWriter}

import com.itextpdf.text.Document
import com.itextpdf.text.pdf.PdfWriter
import com.itextpdf.tool.xml.XMLWorkerHelper
import org.apache.pdfbox.pdmodel.common.PDRectangle
import org.apache.pdfbox.pdmodel.font.PDType1Font
import org.apache.pdfbox.pdmodel.{PDDocument, PDPage, PDPageContentStream}

object Generator {


  //  def createPDF(text: String): Unit = {
  //    val document = new PDDocument()
  //    val page = new PDPage(PDRectangle.A4)
  //    document.addPage(page)
  //
  //    //    val test = "I am trying to create a PDF file with a lot of text contents in the document. I am using PDFBox"
  //
  //    val contentStream = new PDPageContentStream(document, page)
  //    contentStream.setFont(PDType1Font.HELVETICA_BOLD, 20)
  //    //    contentStream.setNonStrokingColor(Color.blue)
  //    contentStream.beginText()
  //    contentStream.newLineAtOffset(100, 700)
  //    contentStream.showText(text)
  //    contentStream.endText()
  //    contentStream.close()
  //
  //    document.save("Blank_Document.pdf")
  //    document.close()
  //  }

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

  def writeHTML(json: String): String = {
    //    val pw = new PrintWriter(new File("index.html"))
    //    pw.write(text.toString)
    //    pw.close()
    ???
  }

}
