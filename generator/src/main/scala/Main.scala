import generator.generate.Generator._
import generator.utils._

object Main {

  def main(args: Array[String]): Unit = {

    val contentMap =
      Map(
        "name" -> Content("name", "John Doe", Code(), "bigHeader"),
        "countries" -> Content("countries", "This is a piece of code ", Code(), "list")
      )

    val configMap =
      Map(
        "bigHeader" -> BigHeader(),
        "list" -> Config("red")
      )


    val contentJSON =
      """
        |{
        |   "name"  : {
        |             "fieldName" : "name",
        |             "fieldValue" : "John Doe",
        |             "fieldType" : {
        |                             "type":
        |                             "link": "www.growin.pt"
        |                          }
        |            }
        |  "phones" : {
        |             "fieldName" : "phones",
        |             "fieldValue" : [12345, 54321],
        |             "fieldType" : "UnorderedList"
        |           }
        |}
      """.stripMargin

    val cssString =
      """
        |body {
        |       background-color: black;
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


    convertMapToPDF(contentMap, cssString)
  }
}
