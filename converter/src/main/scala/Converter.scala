import java.io.File
import javax.imageio.ImageIO

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.rendering.PDFRenderer

object Converter {


  /**
    * Method that converts a pdf file into a png image
    *
    * @param filePath - String containing the URI to the pdf file
    */
  def convertPDFtoPNG(filePath: String): Unit = {
        val pdf = PDDocument.load(new File(filePath))
        val catalog = pdf.getDocumentCatalog
        val renderer = new PDFRenderer(pdf)
        val image = renderer.renderImage(0)
        ImageIO.write(image, "png", new File("./target/images/Converted_" + System.nanoTime() + ".png"))
      }

}
