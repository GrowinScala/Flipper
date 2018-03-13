import java.io.File
import javax.imageio.ImageIO

import net.sourceforge.tess4j.Tesseract
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject
import org.apache.pdfbox.rendering.PDFRenderer

/**
  * Singleton Object that implements all the image processing functionalities
  */
object ImageProcessing {

  /**
    * Method that upon receiving a text file will try to read the text from it using Tess4J tesseract library
    *
    * @param file - The image file to read
    * @return a String containing the images text
    */
  def readImageText(file: File): String = {
    val instance = new Tesseract()
    try {
      instance.doOCR(new File("/Users/Lucas Fischer/Downloads/img.png"))
    } catch {
      case e: Exception => e.printStackTrace()
        null
    }
  }


  def extractImgs(document:PDDocument): Unit ={
    val numPages = document.getNumberOfPages

    val fileList:List[File] = for(i <- 0 until numPages) {
      val page = document.getPage(i)
      val pRes = page.getResources
      pRes.getXObjectNames.forEach(r => {
        val o = pRes.getXObject(r)
        if (o.isInstanceOf[PDImageXObject]){
          val file = new File("./target/images/"+System.nanoTime() + ".png")
          ImageIO.write(o.asInstanceOf[PDImageXObject].getImage, "png", file)
          file
        }
      })
    }
  }

  def convertToImg(filePath: String)={
    val pdf = PDDocument.load(new File(filePath))
    val catalog = pdf.getDocumentCatalog
    val renderer = new PDFRenderer(pdf)
    val image = renderer.renderImage(0)
    ImageIO.write(image, "png", new File("bitcoin-convertToImage-" + 0 + ".png"))
  }
}
