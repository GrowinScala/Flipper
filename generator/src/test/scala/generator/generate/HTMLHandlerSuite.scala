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
      "name" -> Content("name", "John Doe", Header1(), "bigHeader"),
    )

  val configMap =
    Map(
      "bigHeader" -> Config("blue", "20", "center", "corbel", "bold")
    )


  /**
    * Tests that passing a supported/corrected HTML tag will return the corresponding HTMLTag equivelant
    */
  test("Calling extractHTMLTag with correct HTMLTag") {
    assert(
      extractFieldType("h1") == Header1() &&
        extractFieldType("h2") == Header2() &&
        extractFieldType("H3") == Header3() &&
        extractFieldType("orderedList") == OrderedList() &&
        extractFieldType("UnOrDerEdlIsT") == UnorderedList() &&
        extractFieldType("table") == Table() &&
        extractFieldType("p") == Paragraph() &&
        extractFieldType("span") == Text() &&
        extractFieldType("code") == Code() &&
        extractFieldType(Map("link" -> "www.growin.pt")) == Link("www.growin.pt")
    )
  }

  /**
    * Tests that passing an unsupported value to extractHTMLTag (unsupported FieldType tag) will result in a IllegalArgumentException.
    * Example: { "fieldType" : "header20" }
    */
  test("Calling extractHTMLTag with unsupported value - 1") {
    val caught =
      intercept[IllegalArgumentException] {
        extractFieldType("h11")
      }
    assert(caught.getMessage equals "The value specified for fieldType is not supported")
  }

  /**
    * Tests that passing an unsupported value to extractHTMLTag (not passing a link attribute when passing a fieldType object)
    * will result in a IllegalArgumentException.
    * Example: { "fieldType" : { "unsuportedAttribute" : "www.growin.pt" } }
    */
  test("Calling extractHTMLTag with unsupported value - 2") {
    val caught =
      intercept[IllegalArgumentException] {
        extractFieldType(Map("unsuportedAttribute" -> "www.growin.pt"))
      }
    assert(caught.getMessage equals "You must supply fieldType with a link attribute")
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
