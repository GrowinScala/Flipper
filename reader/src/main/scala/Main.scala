object Main {

  import Extractor._
  import OpenNLP._

  def main(args: Array[String]): Unit = {
    val filepath: String = "/Users/Margarida Reis/Desktop/weight.pdf"
    val text = readPDF(filepath)
    println(getAllMatchedValues(text,List(("name","NNP"),("weight","CD"))))
  }
}