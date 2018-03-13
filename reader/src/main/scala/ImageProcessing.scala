import java.io.File

import net.sourceforge.tess4j.Tesseract

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
}
