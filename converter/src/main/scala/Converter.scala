import java.io.File
import javax.imageio.ImageIO

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.rendering.PDFRenderer

object Converter {


  /**
    * Method that converts a pdf file into a png image
    *
    * @param filePath - String containing the URI to the pdf file
    * @param fileType - String containing the file type to be converted into (Works with jpg, png and gif. More types need to be tested)
    */
  def convertPDFtoIMG(filePath: String,fileType: String): Unit = {
    val pdf = PDDocument.load(new File(filePath))
    val renderer = new PDFRenderer(pdf)
    for (i <- 0 until pdf.getNumberOfPages) {
      val image = renderer.renderImage(i)
      ImageIO.write(image, fileType, new File("./target/images/Converted_Page"+i+"_" + System.nanoTime() + "."+fileType))
    }
  }


  def convertPDFtoODF(filePath:String): Unit ={
    //TODO find way to create a odf file from a pdf
  }

}
