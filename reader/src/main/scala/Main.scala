object Main {

  import Extractor._

  def main(args: Array[String]): Unit = {
    //    val filepath: String = "/Users/Lucas Fischer/Desktop/MegaTester.pdf"
    //    val text = readPDF(filepath)
    //    val objs = getAllObjects(text, List(("name", "NNP"), ("weight", "CD")))
    //    objs.foreach(o => println(makeJSONString(o)))
    println(makeJSONString(List(("name", List("Lucas", "Margarida")), ("weight", List("31", "12", "3")))))
  }
}