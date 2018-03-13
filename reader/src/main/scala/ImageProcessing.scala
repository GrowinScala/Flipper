import java.io.File
import javax.imageio.ImageIO

import net.sourceforge.tess4j.Tesseract
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject
import org.apache.pdfbox.rendering.PDFRenderer

/**
  * Singleton Object that implements all the image processing functionalities
  */
//noinspection TypeCheckCanBeMatch
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
      instance.doOCR(file)
    } catch {
      case e: Exception => e.printStackTrace()
        null
    }
  }

  def extractImgs(document: PDDocument): List[File] = {
    val numPages = document.getNumberOfPages
    //Using a mutable List for return a List of files (images) found in the pdf document
    //This was implemented mutably because pRes.getXObjectNames returns a Java Iterator[CosName] and there is no .map/.flatMap fucntion to it, only .forEach
    var mutableFilesList: List[File] = List() //mutable
    for (i <- 0 until numPages) {
      val page = document.getPage(i)
      val pRes = page.getResources
      pRes.getXObjectNames.forEach(r => {
        val o = pRes.getXObject(r)
        if (o.isInstanceOf[PDImageXObject]) {
          val file = new File("./target/images/" + System.nanoTime() + ".png")
          ImageIO.write(o.asInstanceOf[PDImageXObject].getImage, "png", file)
          mutableFilesList = mutableFilesList :+ file
        }
      })
    }
    mutableFilesList
  }

  def convertToImg(filePath: String): Unit = {
    val pdf = PDDocument.load(new File(filePath))
    val catalog = pdf.getDocumentCatalog
    val renderer = new PDFRenderer(pdf)
    val image = renderer.renderImage(0)
    ImageIO.write(image, "png", new File("bitcoin-convertToImage-" + 0 + ".png"))
  }
}
