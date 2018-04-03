package generator

import java.io.File
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import Generator._

@RunWith(classOf[JUnitRunner])
class GeneratorSuite extends FunSuite {



  /**
    * Tests that calling convertJSONtoPDF with an invalid JSON String (null, empty)
    * Or with a non-existing additional CSS file will return an empty String
    */
  test("Calling convertObjtoPDF with invalid Map") {
    val nullMap = convertObjtoPDF(null)
    val emptyMap = convertObjtoPDF(Map())
    val nonExistingCSS = convertObjtoPDF(Map("name" -> List("Lucas", "Margarida"), "age" -> 21), new File("non-existing URI"))
    assert(!nullMap && !emptyMap && !nonExistingCSS)
  }

  /**
    * Tests that calling convertJSONtoPDF with a valid JSON String, and an additional CSS will return a valid URI for the generated html file
    */
  test("calling convertObjtoPDF with a valid Map") {
    val validURI = convertObjtoPDF(Map("name" -> List("Lucas", "Margarida")))
    val validCSSFile = convertObjtoPDF(Map("name" -> List("Lucas", "Margarida")), new File("test.css"))
    val validCSSString = convertObjtoPDF(Map("name" -> List("Lucas", "Margarida")), "p { color : green }")
    val validConfig = convertObjtoPDF(Map("name" -> List("Lucas", "Margarida")), Config("green"))
    assert(validURI && validCSSFile && validCSSString && validConfig)
  }

  /**
    * Tests that calling convertObjtoPDF with an invalid JSON String (null, empty, or badly formatted)
    * Or with a non-existing additional CSS file will return an empty String
    */
  test("Calling convertJSONtoPDF with invalid JSON String") {
    val nullJson = convertJSONtoPDF(null)
    val emptyJson = convertJSONtoPDF("")
    val badJson1 = convertJSONtoPDF("{}")
    val badJson2 = convertJSONtoPDF(""" {"name" :  """)
    val nonExistingCSS = convertJSONtoPDF(""" {"name":"Lucas"}  """, new File("non-existing URI"))
    assert(!nullJson && !emptyJson && !badJson1 && !badJson2 && !nonExistingCSS)
  }

  /**
    * Tests that calling convertJSONtoPDF with a valid JSON String, and an additional CSS will return a valid URI for the generated html file
    */
  test("calling convertJSONtoPDF with a valid JSON String") {
   val validURI = convertJSONtoPDF(""" {"name":"Lucas"}  """)
    val validCSSFile = convertJSONtoPDF(""" {"name":"Lucas"}  """, new File("test.css"))
    val validCSSString = convertJSONtoPDF(""" {"name":"Lucas"}  """, "p { color : green }")
    val validConfig = convertJSONtoPDF(""" {"name":"Lucas"}  """, Config("green"))
    assert(validURI && validCSSFile && validCSSString && validConfig)
  }
}
