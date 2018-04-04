import java.io.File

import parser.utils.{MultipleChoice, Number, OneOff, ProperNoun}

object Main {

  import parser.extraction.Extractor._

  def main(args: Array[String]): Unit = {
    val filepath = new File("/Users/Margarida Reis/Desktop/OpTester.pdf")
    val text = readPDF(filepath)
    val l = getAllMatchedValues(text, Map("name" -> ProperNoun(), "skills" -> MultipleChoice(List("Bla","Ble","Bli","Blo","Blu")), "marital status" -> OneOff(List("single","married")) ))

    println(l)
//    val options = getOptions(text,"skills",List("Bla","Ble","Bli","Blo","Blu"))
//    options.foreach(println(_))

  }
}