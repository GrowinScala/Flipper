import java.io.File

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.Matchers._
import org.scalatest.junit.JUnitRunner
import Generator._

@RunWith(classOf[JUnitRunner])
class GeneratorSuite extends FunSuite {

  /**
    * Tests that calling convertJSONtoPDF with an invalid JSON String (null, empty, or badly formatted)
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
    assert(validURI && validCSSFile && validCSSString)
  }
}
