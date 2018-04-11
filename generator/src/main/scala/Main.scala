import java.util

import generator.generate.Generator._
import generator.utils._
import java.util.ArrayList

object Main {

  def main(args: Array[String]): Unit = {
    val s = new util.ArrayList[String]()
    s.add("1234")
    s.add("4321")
    val contentMap =
      Map(
        "name" -> Content("name", "John Doe", H1(), "bigHeader"),
        "phones" -> Content("phones", s, H1(), "list")
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
