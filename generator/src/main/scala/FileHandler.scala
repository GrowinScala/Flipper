import java.io.File
import scala.io.{BufferedSource, Source}

/**
  * Singleton object that handles all operations regarding file input
  */
object FileHandler {

  /**
    * Method that loads a CSS file from the file passed by arguments
    *
    * @param file - The file to load the CSS from
    * @return a BufferedSource object representing the loaded CSS file
    */
  def loadCSSFile(file: File): Option[BufferedSource] = {
    try {
      Option(Source.fromFile(file))
    } catch {
      case e: Exception => e.printStackTrace(); None
    }
  }

}
