import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.Matchers._
import org.scalatest.junit.JUnitRunner
import Generator._

@RunWith(classOf[JUnitRunner])
class GeneratorSuite extends FunSuite {

  /**
    * Tests that calling writeHTML with an invalid JSON String (null, empty, or badly formatted) will return an empty String
    */
  test("writeHTML with invalid JSON String") {
    val emptyJson = writeHTML("")
    val badJson1 = writeHTML("{}")
    val badJson2 = writeHTML(""" {"name" :  """)
    assert(emptyJson.isEmpty && badJson1.isEmpty && badJson2.isEmpty)
  }

  /**
    * Test the calling witeHTML with an invalid JSON String return NullPointerException
    */
  test("writeHTML with null JSON String") {
    assertThrows[NullPointerException](
      writeHTML(null)
    )
  }

  /**
    * Tests that calling writeHTML with a valid JSON String will return a valid URI for the generated html file
    */
  test("writeHTML with a valid JSON String") {
    val validURI = writeHTML(""" {"name":"Lucas"}  """)
    assert(validURI != "" && validURI.endsWith(".html"))
  }

  /**
    * Tests that calling convertHTMLtoPDF with an invalid URI (null, empty, or non-existing) will return false
    */
  test("convertHTMLtoPDF with an invalid URI") {
    val emptyConvert = convertHTMLtoPDF("")
    val nonExistingConvert = convertHTMLtoPDF("non-existing-uri")
    assert(!emptyConvert && !nonExistingConvert)
  }

  /**
    * Test that calling convertHTMLtoPDF with null URI
    */
  test("convertHTMLtoPDF with null URI") {
    assertThrows[NullPointerException](
      convertHTMLtoPDF(null)
    )
  }

  /**
    * Tests that calling convertHTMltoPDF with a valid URI will return true saying the file was successfully created
    */
  test("convertHTMLtoPDF with a valid URI") {
    val validURI = convertHTMLtoPDF(writeHTML(""" {"name":"Lucas"} """))
    assert(validURI)
  }
}
