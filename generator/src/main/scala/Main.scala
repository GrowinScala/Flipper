import java.io.File

object Main {

  import Generator._

  def main(args: Array[String]): Unit = {
    //    println(convertJSONtoPDF(""" {"name": "Lucas", "age" : 21 }""", new File("test.css")))
        println(convertJSONtoPDF(""" {"name": "Lucas", "age" : 21 }""", "p { color: green; } "))
    //    println(convertJSONtoPDF(""" {"name": "Lucas", "age" : 21 }""", Config("green")))
  }

}
