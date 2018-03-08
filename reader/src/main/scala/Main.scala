object Main {

  import Extractor._

  def main(args: Array[String]): Unit = {
    val filepath: String = "/Users/Lucas Fischer/Documents/Flipper/reader/resources/test.pdf"
    val text = readPDF(filepath)

    println(getMatchedValues(text, List("name")))
  }
}
