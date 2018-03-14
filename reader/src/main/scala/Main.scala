object Main {

  import Extractor._

  def main(args: Array[String]): Unit = {
    val filepath: String = "/Users/Margarida Reis/Desktop/MegaTester.pdf"
    val text = readPDF(filepath)
    println(getAllMatchedValues(text, List(("name", "NNP"), ("weight", "CD"))))
    //    objs.foreach(o => println(makeJSONString(o)))
  }
}