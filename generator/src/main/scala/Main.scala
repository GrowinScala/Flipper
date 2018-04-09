import generator.utils._
import generator.generate.Generator._

object Main {


  def main(args: Array[String]): Unit = {

    //    val content = Map("name" -> Content("name", "joni", H1(), "bigHeader"), "age" -> Content("age", List(20, 30, 40, 50, 60, 70), OrderedList(), "small"))
    //    val cssString = ".bigHeader{ color: blue; font-size: 20pt; text-align : center; font-family: corbel, font-weight: bold} .small{ color: red; font-size: 10pt}"
    //    val formatting =
    //      Map("bigHeader" -> Config("blue", "20", "center", "corbel", "bold"),
    //        "small" -> Config("red", "10"))
    //
    //    println(convertMapToPDF(content, cssString))

    val contentJSON =
      """
        |{ "name" : {
        |             "fieldName" : "name",
        |             "fieldValue" : "something",
        |             "HTMLTag" : "H1",
        |             "cssClass" : "asd"
        |           }
        |}
      """.stripMargin

    val fjson =
      """
        |{ "asd" : {
        |             "textColor" : "red",
        |             "fontSize"  : "20",
        |             "textAlignment" : "center",
        |             "fontFamily"  : "corbel",
        |             "fontWeight"  : "bold"
        |           }
        |}
      """.stripMargin
    println(convertJSONtoPDF(contentJSON, fjson))

  }
}
