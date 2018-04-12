package generator.generate

import generator.generate.Generator.{ConfigMap, ContentMap}
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
    * Tests that passing a supported/corrected HTML tag will return the corresponding HTMLTag eqiuvelant
    */
  test("Calling stringToHTMLTag with correct HTMLTag") {
    assert(
      stringToHTMLTag("h1") == H1() &&
        stringToHTMLTag("h2") == H2() &&
        stringToHTMLTag("H3") == H3() &&
        stringToHTMLTag("orderedList") == OrderedList() &&
        stringToHTMLTag("UnOrDerEdlIsT") == UnorderedList() &&
        stringToHTMLTag("table") == Table() &&
        stringToHTMLTag("p") == P() &&
        stringToHTMLTag("span") == Text() &&
        stringToHTMLTag("somethin-else") == Text()
    )
  }

  /**
    * Tests that calling createCssString with the correct input parameters will result in the expected CSS String
    */
  test("Correctly call createCssString") {
    val config2 =
      Map(
        "bigHeader" -> Config("blue")
      )

    val expected = ".bigHeader{ color: blue; text-align: center; font-weight: bold; font-family: corbel; font-size: 20pt;} "
    val expected2 = ".bigHeader{ color: blue; text-align: ; font-weight: ; font-family: ; font-size: -1pt;} "
    val result = createCssString(content, configMap)
    val result2 = createCssString(content, config2)
    assert(result.equals(expected) && result2.equals(expected2))
  }

  /**
    * Tests that calling createCssString with and empty ConfigMap/ContentMap will return an empty String
    */
  test("Calling createCssString with empty ConfigMap/ContentMap") {
    val emptyConf: ConfigMap = Map()
    val emptyContent: ContentMap = Map()
    assert(createCssString(content, emptyConf).isEmpty && createCssString(emptyContent, configMap).isEmpty)
  }

  /**
    * Tests that calling writeHTMLString with correct input parameters will return a correct HTML String
    */
  test("Correctly call writeHTMLString") {
    val cssString = ".bigHeader{ color: blue; text-align: center; font-weight: bold; font-family: corbel; font-size: 20pt;} "
    val expectedHTML1 = "<html><head> <style> .bigHeader{ color: blue; text-align: center; font-weight: bold; font-family: corbel; font-size: 20pt;}  </style> </head><body><h1 class=\"bigHeader\">name : John Doe</h1></body></html>"
    val expectedHTML2 = "<html><head> <style>  </style> </head><body><h1 class=\"bigHeader\">name : John Doe</h1></body></html>"
    assert(writeHTMLString(content, cssString).equals(expectedHTML1) && writeHTMLString(content, "").equals(expectedHTML2))
  }

  /**
    * Tests that caliing writeHTMLString with an empty content map will return a HTML code with an empty body
    */
  test("Calling writeHTMLString with an empty content map") {
    val emptyMap: ContentMap = Map()
    val expectedHTML = "<html><head> <style> .bigHeader{ color: blue; text-align: center; font-weight: bold; font-family: corbel; font-size: 20pt;}  </style> </head><body></body></html>"
    val cssString = ".bigHeader{ color: blue; text-align: center; font-weight: bold; font-family: corbel; font-size: 20pt;} "
    assert(writeHTMLString(emptyMap, cssString) equals expectedHTML)
  }
}
