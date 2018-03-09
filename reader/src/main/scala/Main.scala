object Main {

  import Extractor._

  def main(args: Array[String]): Unit = {
    val filepath: String = "/Users/Margarida Reis/Documents/Flipper/reader/resources/test.pdf"
    val text = readPDF(filepath)

    val obj = getAllObjects(text, List("name", "age", "mail", "date", "something"))
    //    println(makeJSONString(getSingleMatchedValue(text, List("name", "age", "mail", "date","something"))))
    //    println(getJSONFromForm(text))

    obj.foreach(o=>println(makeJSONString(o)))

  }
}
