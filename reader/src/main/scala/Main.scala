object Main {

  import Extractor._

  def main(args: Array[String]): Unit = {
    val filepath: String = "./reader/resources/images.pdf"
    val text = readPDF(filepath)
    //    val objs = getAllObjects(text, List(("name", POSTag.PN), ("weight", POSTag.NUM)))
    //    objs.foreach(println)
    //    objs.foreach(o => println(makeJSONString(o)))
    //    println(makeJSONString(List(("name", List("Lucas", "Margarida")), ("weight", List("31", "12", "3")))))
  }
}