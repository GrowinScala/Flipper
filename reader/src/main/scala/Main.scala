object Main {

  import Extractor._

  def main(args: Array[String]): Unit = {
    val filepath: String = "/Users/Margarida Reis/Documents/Flipper/reader/resources/test(1).pdf"
    val text = readPDF(filepath)
    println(makeJSONString(getSingleMatchedValue(text, List("name", "age", "mail", "date"))))
  }
}
