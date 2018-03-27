import java.io.File

import ReaderConverter.conversion.FileType
import ReaderConverter.conversion.Converter._

object Main {


  def main(args: Array[String]): Unit = {
    val filepath = new File("./converter/resources/cv.pdf")
    convertPDFtoIMG(filepath, FileType.jpeg)
    convertPDFtoODT(filepath)
  }
}
