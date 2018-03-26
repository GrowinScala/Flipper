import java.io.File

object Main {

  import Converter._

  def main(args: Array[String]): Unit = {
    val filepath = new File("/Users/Margarida Reis/Desktop/MegaTester.pdf")
    convertPDFtoIMG(filepath, FileType.jpeg)
    convertPDFtoODT(filepath)
  }
}
