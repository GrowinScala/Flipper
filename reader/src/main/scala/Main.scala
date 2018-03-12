object Main {

  import Extractor._

  def main(args: Array[String]): Unit = {
    val filepath: String = "/Users/Margarida Reis/Documents/Flipper/reader/resources/test.pdf"
    val text = readPDF(filepath)
    println(makeJSONString(getAllMatchedValues(text, List(("name","noun")))))
  }
}