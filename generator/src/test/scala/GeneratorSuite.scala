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
    val nullJson = writeHTML(null)
    val emptyJson = writeHTML("")
    val badJson1 = writeHTML("{}")
    val badJson2 = writeHTML(""" {"name" :  """)
    assert(nullJson == "" && emptyJson == "" && badJson1 == "" && badJson2 == "")
  }

  /**
    * Tests that calling writeHTML with a valid JSON String will return a valid URI for the generated html file
    */
  test("writeHTML with a valid JSON String") {
    val validURI = writeHTML(""" {"name":"Lucas"}  """)
    assert(validURI != "" && validURI.endsWith(".html"))
  }
}
