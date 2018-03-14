object Main {

  import Extractor._

  def main(args: Array[String]): Unit = {
    val filepath: String = "./reader/resources/imgTester.pdf"
    val text = readPDF(filepath)
    println(getAllMatchedValues(text, List(("name", "NNP"), ("weight", "CD"))))
    //    val objs = getAllObjects(text, List(("name", "NNP"), ("weight", "CD")))
    //    objs.foreach(o => println(makeJSONString(o)))
  }
}