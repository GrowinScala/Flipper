import java.io.File

import parser.utils._

object Main {

  import parser.extraction.Extractor._

  def main(args: Array[String]): Unit = {
    val filepath = new File("/Users/Margarida Reis/Desktop/a.pdf")
    val oneofLst = List("single","married","divorced")
    val multiLst = List("Bla","Ble","Bli","Blo","Blu")
    val keywords = Map("name"-> ProperNoun(), "phone" -> Number(), "mail" -> Noun(true))
    val text = readPDF(filepath)
//    val l = getJSONFromForm(text)

//    println(l)
//    l.foreach(println)
    //    val options = getOptions(text,"skills",List("Bla","Ble","Bli","Blo","Blu"))
    //    options.foreach(println(_))

//    val list = getJSONSingle(text,keywords)

    println(list)
//    println(("\n").r.findAllMatchIn(text.get).length)

  }
}