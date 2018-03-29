import java.io.File

import parser.utils.POSTag

object Main {

  import parser.extraction.Extractor._

  def main(args: Array[String]): Unit = {
    val filepath = new File("./reader/src/main/resources/MegaTester.pdf")
    val text = readPDF(filepath)
//    println(text)
//    getJSONObjects(text, List(("name", POSTag.PN), ("weight", POSTag.NUM)), "null").foreach(println)
//    val objs = getAllObjects(text, List(("name", POSTag.PN), ("weight", POSTag.NUM)))
//    println(objs)
    //    objs.foreach(println)
//        objs.foreach(o => println(makeJSONString(o)))
        println(makeJSONString(List(("name", List("Lucas", "Margarida")), ("weight", List("31", "12", "3")))))
  }
}