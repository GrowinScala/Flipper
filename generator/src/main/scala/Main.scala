import java.io.File

object Main {

  import generator.Generator._

  def main(args: Array[String]): Unit = {
    //    println(convertJSONtoPDF(""" {"name": "Lucas", "age" : 21 }""", new File("test.css")))
    val json = """ {"name": ["Lucas", "Margarida"],  "age" : 21 }"""
    val jsonMap = convertJSONtoObj(json)
    println(jsonMap)
    convertJSONtoPDF(json)
//        println(convertJSONtoPDF(""" {"name": "Lucas", "age" : 21 }""", "p { color: green; } "))
    //    println(convertJSONtoPDF(""" {"name": "Lucas", "age" : 21 }""", Generator.Config("green")))
  }

}
