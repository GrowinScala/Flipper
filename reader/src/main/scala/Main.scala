object Main {

  import Extractor._
  import OpenNLP._

  def main(args: Array[String]): Unit = {
    val filepath: String = "./reader/resources/bla.pdf"
    val text = readPDF(filepath)
    println(getAllMatchedValues(text, List(("name", "NNP"), ("weight", "CD"), ("age", "CD"))))
  }
}