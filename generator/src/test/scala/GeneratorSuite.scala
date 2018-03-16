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
  test("Calling convertJSONtoPDF with invalid JSON String") {
    val nullJson = convertJSONtoPDF(null)
    val emptyJson = convertJSONtoPDF("")
    val badJson1 = convertJSONtoPDF("{}")
    val badJson2 = convertJSONtoPDF(""" {"name" :  """)
    assert(!nullJson && !emptyJson && !badJson1 && !badJson2)
  }

  /**
    * Tests that calling writeHTML with a valid JSON String will return a valid URI for the generated html file
    */
  test("calling convertJSONtoPDF with a valid JSON String") {
    val validURI = convertJSONtoPDF(""" {"name":"Lucas"}  """)
    assert(validURI)
  }
}
