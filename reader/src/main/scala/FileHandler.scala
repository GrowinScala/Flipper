import java.io.File

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
      val pdf: PDDocument = PDDocument.load(file)
      Option(pdf)
    } catch {
      case e: Exception => e.printStackTrace(); None
    }
  }

}
