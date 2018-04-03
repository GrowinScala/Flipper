import java.io.File

object Main {

  import generator.Generator._

  def main(args: Array[String]): Unit = {
    //    println(convertJSONtoPDF(""" {"name": "Lucas", "age" : 21 }""", new File("test.css")))
    val objMap: Map[String,Any] = Map("name" -> List("Lucas", "Margarida"), "age" -> 21)
    convertObjtoPDF(objMap)
//    println(objMap)
    val json = """ {"name": ["Lucas", "Margarida"],  "age" : 21 }"""
    convertJSONtoPDF(json)
//        println(convertJSONtoPDF(""" {"name": "Lucas", "age" : 21 }""", "p { color: green; } "))
    //    println(convertJSONtoPDF(""" {"name": "Lucas", "age" : 21 }""", Generator.Config("green")))
  }

}
