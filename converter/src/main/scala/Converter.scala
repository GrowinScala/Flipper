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
    * @return - A Boolean saying if the conversion was successful
    * @throws IllegalArgumentException if the specified fileType did not equal the supported file types
    */
  @throws[IllegalArgumentException]
  def convertPDFtoIMG(filePath: String, fileType: String): Boolean = {
    require(fileType == "png" || fileType == "jpg" || fileType == "gif" || fileType == "jpeg", "File type must be one of png, jpg, gif or jpeg")
    //    if (fileType != "png" && fileType != "jpg" && fileType != "gif" && fileType != "jpeg") false

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
      case e: Exception => e.printStackTrace(); false
    }
  }

  /**
    * Method that creates a odf file with the information taken from a pdf (Note: does not maintain image and text order)
    *
    * @param filePath - String containing the URI to the pdf file
    * @return - A Boolean saying if the conversion was successful
    */
  def convertPDFtoODF(filePath: String): Boolean = {
    try {
      val text = readPDF(filePath, readImages = false)
      val textContent = text.getOrElse("")
      val pdf = PDDocument.load(new File(filePath))
      val imgFiles = extractImgs(pdf)
      val imgFilesContent = imgFiles.getOrElse(List())
      val odf = OdfTextDocument.newTextDocument()
      textContent.split("\n").foreach(l => odf.newParagraph(l))
      imgFilesContent.foreach(i => {
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
      case e: Exception =>e.printStackTrace();false
    }
  }
}