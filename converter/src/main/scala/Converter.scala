import java.io.File
import javax.imageio.ImageIO
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.rendering.PDFRenderer
import org.odftoolkit.odfdom.doc.OdfTextDocument
import Extractor._
import ImageProcessing._

object Converter {

  /**
    * Method that converts a pdf file into a png image
    *
    * @param filePath - String containing the URI to the pdf file
    * @param fileType - String containing the file type to be converted into (Works with jpg, png and gif)
    */
  def convertPDFtoIMG(filePath: String,fileType: String): Unit = {
    val pdf = PDDocument.load(new File(filePath))
    val renderer = new PDFRenderer(pdf)
    for (i <- 0 until pdf.getNumberOfPages) {
      val image = renderer.renderImage(i)
      ImageIO.write(image, fileType, new File("./target/images/Converted_Page"+i+"_" + System.nanoTime() + "."+fileType))
    }
  }


  /**
    * Method that creates a odf file with the information taken from a pdf (Note: does not maintain format)
    *
    * @param filePath - String containing the URI to the pdf file
    */
  def convertPDFtoODF(filePath:String): Unit ={
    //TODO find way to create a odf file from a pdf
    val text = readPDF(filePath)
    val pdf = PDDocument.load(new File(filePath))
    val imgFiles = extractImgs(pdf)

    val odf = OdfTextDocument.newTextDocument()
    odf.addText(text)
    imgFiles.foreach(i => {
      odf.newParagraph()
      odf.newImage(i.toURI)
    })
    odf.save("test.odt")
  }

}
