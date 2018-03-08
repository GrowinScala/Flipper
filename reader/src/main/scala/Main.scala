object Main {

  import Extractor._

  def main(args: Array[String]): Unit = {
    val filepath: String = "/Users/Margarida Reis/Documents/Flipper/reader/resources/test(2).pdf"
    val text = readPDF(filepath)

    println(makeJSONString(getMatchedValues(text, List("name","age","mail","date"))))
  }
}
