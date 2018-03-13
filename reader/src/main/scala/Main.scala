object Main {

  import Extractor._

  def main(args: Array[String]): Unit = {
    val filepath: String = "/Users/Lucas Fischer/Desktop/imgTester.pdf"
    val text = readPDF(filepath)
//    val objs = getAllObjects(text, List(("name", "NNP"), ("weight", "CD")))
//    objs.foreach(o => println(makeJSONString(o)))
  }
}