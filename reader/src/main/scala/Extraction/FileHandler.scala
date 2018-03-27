package Extraction

import java.io.File
import com.sksamuel.scrimage.Image
import org.apache.pdfbox.pdmodel.PDDocument

/**
  * Singleton object that handles all operations regarding file input
  */
object FileHandler {


  /**
    * Method that loads the file received as input and returns the loaded document
    *
    * @param file - File to be loaded
    * @return a PDDocument object representing the loaded PDF document
    */
  def loadPDF(file: File): Option[PDDocument] = {
    try {
      Option(PDDocument.load(file))
    } catch {
      case e: Exception => e.printStackTrace(); None
    }
  }

  /**
    * Method that loads a picture from a file and returns a Scrimage Image object representing the loaded image
    *
    * @param file - The image file to be loaded
    * @return an Image object representing the loaded image from the given file
    */
  def loadImage(file: File): Option[Image] = {
    try {
      Option(Image.fromFile(file))
    } catch {
      case e: Exception => e.printStackTrace(); None
    }
  }

  /**
    * Method that deletes all files from the image folder
    */
   private[Extraction] def cleanImageDir() {
    val dir = new File("./target/images")
    if (dir.exists) {
      val files = dir.listFiles.filter(_.isFile).toList
      files.foreach(_.delete)
    }
  }

}
