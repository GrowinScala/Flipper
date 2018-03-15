object Main {

  import Extractor._
  import OpenNLP._

  def main(args: Array[String]): Unit = {
    val filepath: String = "/Users/Lucas Fischer/Desktop/testEn.pdf"
    val text = readPDF(filepath)
    var pair = tagText(text)
    for (i <- pair._1.indices) {
      println("Palavra : " + pair._1(i) + " - Tag : " + pair._2(i))
    }
  }
}