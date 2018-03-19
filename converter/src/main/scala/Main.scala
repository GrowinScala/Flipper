import javax.imageio.ImageIO

object Main {

  import Converter._

  def main(args: Array[String]): Unit = {
    val filepath: String = "/Users/Margarida Reis/Desktop/MegaTester.pdf"
    convertPDFtoIMG(filepath, FileType.jpeg)
//    convertPDFtoODF(filepath)
  }
}
