import java.io.File

import Converter.FileType

object Main {

  import Converter.Converter._

  def main(args: Array[String]): Unit = {
    val filepath = new File("/Users/Margarida Reis/Desktop/MegaTester.pdf")
    convertPDFtoIMG(filepath, FileType.jpeg)
    convertPDFtoODT(filepath)
  }
}
