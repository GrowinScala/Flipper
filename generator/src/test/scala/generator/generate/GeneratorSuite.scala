package generator

import java.io.File

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import generate.Generator._
import generator.utils._


@RunWith(classOf[JUnitRunner])
class GeneratorSuite extends FunSuite {

  val content =
    Map(
      "name" -> Content("name", "John Doe", Header1(), "bigHeader"),
      "age" -> Content("age", List(20, 30), OrderedList(), "small")
    )
  val cssFile = new File("test.css")
  val cssString =
    """.bigHeader{
      |           color: blue;
      |           font-size: 20pt;
      |           text-align : center;
      |           font-family: corbel;
      |           font-weight: bold;
      |}
      |.small{
      |       color: red;
      |       font-size: 10pt;
      |}
      |""".stripMargin

  /**
    * Tests that passing a correct ContentMap and a correct CSS file/CSS String/ConfigMap
    * to convertMapToPDF will return true saying the conversion was successful
    */
  test("Correcty call convertMapToPDF") {
    val emptyCSS = ""
    val emptyConfig: ConfigMap = Map()

    val configMap =
      Map(
        "bigHeader" -> Config("blue", "20", "center", "corbel", "bold"),
        "small" -> SmallHeader()
      )
    val noConfig = convertMapToPDF(content)
    val convertWithCSSFile = convertMapToPDF(content, cssFile)
    val convertWithCSSStr = convertMapToPDF(content, cssString)
    val convertWithConfigMap = convertMapToPDF(content, configMap)
    val convertWithEmptyConf = convertMapToPDF(content, emptyConfig)
    val convertWithEmptyCSS = convertMapToPDF(content, emptyCSS)

    assert(
      noConfig &&
        convertWithConfigMap &&
        convertWithCSSStr &&
        convertWithCSSFile &&
        convertWithEmptyConf &&
        convertWithEmptyCSS
    )
  }

  /**
    * Tests that calling convertMapToPDF with an incorrect ContentMap/CSS File will return false saying the conversion was not successful
    */
  test("Incorrectly call convertMapToPDF") {
    val incorrectFile = new File("non-existing-file")
    val emptyContent: ContentMap = Map()

    assert(!convertMapToPDF(content, incorrectFile) && !convertMapToPDF(emptyContent, cssFile))
  }

  /**
    * Tests that calling converJSONtoPDF (and convertJSONtoPDFWithCSS) with correct input parameters will return true
    * saying the conversion was successful
    */
  test("Correctly call convertJSONtoPDF") {
    val contentJSON =
      """
        |{ "name" : {
        |             "fieldName" : "name",
        |             "fieldValue" : "something",
        |             "fieldType" : "Header1",
        |             "formattingID" : "bigHeader"
        |           }
        |}
      """.stripMargin
    val configJSON =
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

    val noConfig = convertJSONtoPDF(contentJSON)
    val convertWithConf = convertJSONtoPDF(contentJSON, configJSON)
    val convertWithCSSStr = convertJSONtoPDFWithCSS(contentJSON, cssString)
    val convertWithEmptyCSS = convertJSONtoPDFWithCSS(contentJSON, "")
    val convertWithCSSFile = convertJSONtoPDF(contentJSON, cssFile)
    assert(noConfig && convertWithConf && convertWithCSSStr && convertWithCSSFile && convertWithEmptyCSS)
  }

  /**
    * Tests that calling convertJSONtoPDF with an invalid fieldType (unsupported FieldType tag)
    * will result in a IllegalArgumentException.
    */
  test("Calling convertJSONtoPDF with an invalid fieldType - 1") {
    val contentJSON =
      """
        |{ "name" : {
        |             "fieldName" : "name",
        |             "fieldValue" : "something",
        |             "fieldType" : "Header20",
        |             "formattingID" : "bigHeader"
        |           }
        |}
      """.stripMargin
    val caught =
      intercept[IllegalArgumentException] {
        convertJSONtoPDF(contentJSON)
      }
    assert(caught.getMessage equals "The value specified for fieldType is not supported")
  }
//Teste
  /**
    * Tests that calling convertJSONtoPDF with an invalid fieldType (not passing a link attribute when passing a fieldType object)
    * will result in a IllegalArgumentException.
    */
  test("Calling convertJSONtoPDF with an invalid fieldType - 2") {
    val contentJSON =
      """
        |{ "name" : {
        |             "fieldName" : "name",
        |             "fieldValue" : "something",
        |             "fieldType" : { "unsupportedAttr" : "someValue" },
        |             "formattingID" : "bigHeader"
        |           }
        |}
      """.stripMargin
    val caught =
      intercept[IllegalArgumentException] {
        convertJSONtoPDF(contentJSON)
      }
    assert(caught.getMessage equals "You must supply fieldType object with a type attribute")
  }

  /**
    * Tests that incorrectly calling convertJSONtoPDF (empty content, empty config, wrong css file) will return false
    * saying the conversion was not successful
    */
  test("Incorrectly call convertJSONtoPDF") {
    val contentJSON =
      """
        |{ "name" : {
        |             "fieldName" : "name",
        |             "fieldValue" : "something",
        |             "fieldType" : "Header1",
        |             "formattingID" : "bigHeader"
        |           }
        |}
      """.stripMargin

    val configJSON =
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
    val emptyConfig = convertJSONtoPDF(contentJSON, "")
    val emptyContent = convertJSONtoPDF("", configJSON)
    val invalidFile = convertJSONtoPDF(contentJSON, new File("invalid-file"))

    assert(!emptyConfig && !emptyContent && !invalidFile)
  }
}
