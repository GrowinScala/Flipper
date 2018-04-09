package generator.generate

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import generator.generate.HTMLHandler._
import generator.utils._

@RunWith(classOf[JUnitRunner])
class HTMLHandlerSuite extends FunSuite {

  val content =
    Map(
      "name" -> Content("name", "John Doe", H1(), "bigHeader"),
    )

  val configMap =
    Map(
      "bigHeader" -> Config("blue", "20", "center", "corbel", "bold")
    )


  /**
    * Tests that passing a supported/corrected HTML entity will return the corresponding FormattingType eqiuvelant
    */
  test("calling stringToHTMLTag with correct HTML Entity") {
    assert(
      stringToHTMLTag("h1") == H1() &&
        stringToHTMLTag("h2") == H2() &&
        stringToHTMLTag("H3") == H3() &&
        stringToHTMLTag("orderedList") == OrderedList() &&
        stringToHTMLTag("UnOrDerEdlIsT") == UnOrderedList() &&
        stringToHTMLTag("table") == Table() &&
        stringToHTMLTag("p") == P() &&
        stringToHTMLTag("span") == Text() &&
        stringToHTMLTag("somethin-else") == Text()
    )
  }

  /**
    * Tests that calling createCssString with the correct input parameters will result in the expected CSS String
    */
  test("correctly call createCssString") {
    val config2 =
      Map(
        "bigHeader" -> Config("blue")
      )

    val expected = ".bigHeader{ color: blue; text-align: center; font-weight: bold; font-family: corbel; font-size: 20pt;} "
    val expected2 = ".bigHeader{ color: blue; text-align: ; font-weight: ; font-family: ; font-size: pt;} "
    val result = createCssString(content, configMap)
    val result2 = createCssString(content, config2)
    assert(result.equals(expected) && result2.equals(expected2))
  }
}
