import java.io.File

import parser.conversion.FileType
import parser.conversion.Converter._

object Main {

  def main(args: Array[String]): Unit = {
    val filepath = new File("./converter/resources/cv.pdf")
    convertPDFtoIMG(filepath, FileType.jpeg)
    convertPDFtoODT(filepath)
  }
}
