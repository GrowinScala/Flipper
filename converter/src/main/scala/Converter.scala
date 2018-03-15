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
    * @param fileType - String containing the file type to be converted into (Works with jpg/jpeg, png and gif)
    */
  def convertPDFtoIMG(filePath: String, fileType: String): Boolean = {
    if(fileType != "png" && fileType != "jpg" && fileType != "gif" && fileType != "jpeg") return  false
//      TODO we believe ImageIO has a default for png when the fileType is incorrect, maybe create an Enum ?
    try {
      val pdf = PDDocument.load(new File(filePath))
      val renderer = new PDFRenderer(pdf)
      for (i <- 0 until pdf.getNumberOfPages) {
        val image = renderer.renderImage(i)
        ImageIO.write(image, fileType, new File("./target/images/Converted_Page" + i + "_" + System.nanoTime() + "." + fileType))
      }
      true
    } catch {
      case _: Exception => false
    }

  }

  /**
    * Method that creates a odf file with the information taken from a pdf (Note: does not maintain image and text order)
    *
    * @param filePath - String containing the URI to the pdf file
    */
  def convertPDFtoODF(filePath: String): Boolean = {
    try {
      val text = readPDF(filePath, readImages = false)
      val pdf = PDDocument.load(new File(filePath))
      val imgFiles = extractImgs(pdf)
      val odf = OdfTextDocument.newTextDocument()
      text.split("\n").foreach(l => odf.newParagraph(l))
      imgFiles.foreach(i => {
        odf.newParagraph()
        odf.newParagraph()
        odf.newParagraph()
        odf.newParagraph()
        odf.newParagraph()
        odf.newImage(i.toURI)
      })
      odf.save("test.odf")
      true
    } catch {
      case _: Exception => false
    }
  }
}