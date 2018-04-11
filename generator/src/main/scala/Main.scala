import generator.generate.Generator._
import generator.utils._

object Main {

  def main(args: Array[String]): Unit = {
    //    val table = Map("Country" -> List("Portugal", "Spain", "UK"), "Language" -> List("Portuguese", "English"), "Weather" -> List("Sunny", "Rainning"))
    //    val tableList = List("Portugal", "Spain", "UK")
    //    val somethingElse = "heeeey"
    val contentMap =
    Map(
      "name" -> Content("name", "John Doe", H1(), "bigHeader"),
      "countries" -> Content("countries", List(123, 321), UnorderedList(), "list")
    )

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


    convertMapToPDF(contentMap, cssString)
  }
}
