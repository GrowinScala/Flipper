import org.apache.pdfbox.pdmodel.{PDDocument, PDPage}

object Generator {


  def createPDF():Unit={
    val document = new PDDocument()
    document.addPage(new PDPage())

    document.save("Blank_Document.pdf")

    document.close()
  }
}
