import java.io.File

import parser.utils._

object Main {

  import parser.extraction.Extractor._

  def main(args: Array[String]): Unit = {
    val filepath = new File("./reader/src/main/resources/MegaTester.pdf")
    val text = readPDF(filepath)
    val l = getAllMatchedValues(text, Map("name" -> ProperNoun()))

    println(l)
    //    val options = getOptions(text,"skills",List("Bla","Ble","Bli","Blo","Blu"))
    //    options.foreach(println(_))

  }
}