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
    val renderer = new PDFRenderer(pdf)
    for (i <- 0 until pdf.getNumberOfPages) {
      val image = renderer.renderImage(i)
      ImageIO.write(image, "png", new File("./target/images/Converted_Page"+i+"_" + System.nanoTime() + ".png"))
    }
  }

  /**
    * Method that converts a pdf file into a jpg image
    *
    * @param filePath - String containing the URI to the pdf file
    */
  def convertPDFtoJPG(filePath: String): Unit = {
    val pdf = PDDocument.load(new File(filePath))
    val renderer = new PDFRenderer(pdf)
    for (i <- 0 until pdf.getNumberOfPages) {
      val image = renderer.renderImage(i)
      ImageIO.write(image, "jpg", new File("./target/images/Converted_Page"+i+"_" + System.nanoTime() + ".jpg"))
    }
  }

  /**
    * Method that converts a pdf file into a odf image
    *
    * @param filePath - String containing the URI to the pdf file
    */
  def convertPDFtoODF(filePath:String): Unit ={
    //TODO find way to create a odf file from a pdf
  }

}
