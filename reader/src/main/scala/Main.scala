import java.io.File

import parser.utils._

object Main {

  import parser.extraction.Extractor._

  def main(args: Array[String]): Unit = {
    val filepath = new File("./reader/src/main/resources/test.pdf")
    val text = readPDF(filepath)
    val l = getJSONObjects(text, Map("name" -> MultipleOf(List("Lucas", "Albertina"))))

    println(l)
    //    val options = getOptions(text,"skills",List("Bla","Ble","Bli","Blo","Blu"))
    //    options.foreach(println(_))

  }
}