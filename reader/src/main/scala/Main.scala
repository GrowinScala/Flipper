object Main {

  import Extractor._

  def main(args: Array[String]): Unit = {
    val filepath: String = "/Users/Margarida Reis/Desktop/test(1).pdf"
    val text = readPDF(filepath)
    println(text)
    //    val objs = getAllObjects(text, List(("name", "NNP"), ("weight", "CD")))
    //    objs.foreach(o => println(makeJSONString(o)))
  }
}