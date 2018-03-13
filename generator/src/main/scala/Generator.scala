import java.awt.Color
import java.io.{File, FileInputStream, FileOutputStream}

import com.itextpdf.text.Document
import com.itextpdf.text.pdf.PdfWriter
import com.itextpdf.tool.xml.XMLWorkerHelper
import org.apache.pdfbox.pdmodel.common.PDRectangle
import org.apache.pdfbox.pdmodel.font.PDType1Font
import org.apache.pdfbox.pdmodel.{PDDocument, PDPage, PDPageContentStream}

object Generator {


  def createPDF(text:String):Unit={
    val document = new PDDocument()
    val page = new PDPage(PDRectangle.A4)
    document.addPage(page)

    val test = "I am trying to create a PDF file with a lot of text contents in the document. I am using PDFBox"

    val contentStream = new PDPageContentStream(document,page)
    contentStream.setFont(PDType1Font.HELVETICA_BOLD, 20)
//    contentStream.setNonStrokingColor(Color.blue)
    contentStream.beginText()
    contentStream.newLineAtOffset(100, 700)
    contentStream.showText(test)
    contentStream.endText()
    contentStream.close()

    document.save("Blank_Document.pdf")
    document.close()
  }

  def convertHTMLtoPDF(html:String): Unit ={
    val document = new Document()
    val writer = PdfWriter.getInstance(document,new FileOutputStream("src/output/html.pdf"))
    document.open()
    XMLWorkerHelper.getInstance().parseXHtml(writer, document, new FileInputStream(html))
    document.close()
  }

}
