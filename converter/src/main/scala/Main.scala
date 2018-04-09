import java.io.File

import parser.conversion.{FileType, PNG}
import parser.conversion.Converter._

object Main {

  def main(args: Array[String]): Unit = {
    val filepath = new File("./converter/src/main/resources/cv.pdf")
    convertPDFtoIMG(filepath, PNG())
//    println(PNG().toString)
//    convertPDFtoODT(filepath)
  }
}
