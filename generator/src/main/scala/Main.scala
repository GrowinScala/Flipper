import generator.generate.Generator._
import generator.utils._

object Main {

  def main(args: Array[String]): Unit = {
    //    val table = Map("Country" -> List("Portugal", "Spain", "UK"), "Language" -> List("Portuguese", "English"), "Weather" -> List("Sunny", "Rainning"))
    //    val tableList = List("Portugal", "Spain", "UK")
    //    val somethingElse = "heeeey"
    val contentMap =
    Map(
      "name" -> Content("name", "John Doe", Header1(), "bigHeader"),
      "countries" -> Content("countries", "This is a piece of code ", Code(), "list")
    )


    val contentJSON =
      """
        |{
        |   "name"  : {
        |             "fieldName" : "name",
        |             "fieldValue" : "John Doe",
        |             "fieldType" : {
        |                             "link": "www.growin.pt"
        |                          },
        |             "formattingID" : "bigHeader"
        |            },
        |  "phones" : {
        |             "fieldName" : "phones",
        |             "fieldValue" : [12345, 54321],
        |             "fieldType" : "UnorderedList",
        |             "formattingID" : "list"
        |           },
        |}
      """.stripMargin

    val cssString =
      """
        |body {
        |       background-color: lightgreen;
        |     }
        |
        |.bigHeader {
        |            color: blue;
        |            font-size: 20pt;
        |            text-align : center;
        |            font-family: corbel;
        |            font-weight: bold;
        |}
        |.list {
        |       color: red;
        |       font-size: 10pt;
        | }
      """.stripMargin


    convertJSONtoPDF(contentJSON)
  }
}
