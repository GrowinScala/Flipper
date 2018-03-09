object Main {

  import Extractor._

  def main(args: Array[String]): Unit = {
    val filepath: String = "/Users/Lucas Fischer/Desktop/cv.pdf"
    val text = readPDF(filepath)
    println(makeJSONString(getAllMatchedValues(text, List("name", "age", "mail"))))
  }
}