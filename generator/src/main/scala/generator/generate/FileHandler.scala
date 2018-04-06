package generator.generate

import java.io.{File, PrintWriter}

import scala.io.{BufferedSource, Source}

/**
  * Singleton object that handles all operations regarding file input
  */
private[generate] object FileHandler {

  /**
    * Method that loads a CSS file from the file passed by arguments
    *
    * @param file - The file to load the CSS from
    * @return a BufferedSource object representing the loaded CSS file
    */
  def loadCSSFile(file: File): Option[BufferedSource] = {
    try {
      Some(Source.fromFile(file))
    } catch {
      case e: Exception => e.printStackTrace(); None
    }
  }

  /**
    * Method that handles the writting of the HTML file and returns the file path of the generated file
    *
    * @param htmlString - HTML code (in a String) to be placed inside the HTML file
    * @return the file path of the newly created HTML file
    */
  def generateDocument(htmlString: String): String = {
    val dir = new File("./target/htmlPages")
    if (!dir.exists) dir.mkdirs
    val filePath = "./target/htmlPages/" + System.nanoTime() + ".html"
    val pw = new PrintWriter(new File(filePath)) //prints the HTML code to the html file
    pw.write(htmlString)
    pw.close()
    filePath
  }

  /**
    * Method that deletes all the files in the htmlPages directory
    */
  def cleanHTMLDir() {
    val dir = new File("./target/htmlPages")
    if (dir.exists) {
      val files = dir.listFiles.filter(_.isFile).toList
      files.foreach(_.delete)
    }
  }

}
