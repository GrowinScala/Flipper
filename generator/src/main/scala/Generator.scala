import java.awt.Color

import org.apache.pdfbox.pdmodel.font.PDType1Font
import org.apache.pdfbox.pdmodel.{PDDocument, PDPage, PDPageContentStream}

object Generator {


  def createPDF(text:String):Unit={
    val document = new PDDocument()
    val page = new PDPage()
    document.addPage(page)

    //val font=PDType1Font.HELVETICA
    val contentStream = new PDPageContentStream(document,page)
    contentStream.setFont(PDType1Font.HELVETICA_BOLD, 20)

    contentStream.beginText()
    contentStream.newLineAtOffset(100, 700)
    contentStream.showText(text)
    contentStream.endText()
    contentStream.close()

    document.save("Blank_Document.pdf")
    document.close()
  }
}
