import java.io.FileNotFoundException

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.Matchers._
import org.scalatest.junit.JUnitRunner
import Extractor._

@RunWith(classOf[JUnitRunner])
class ExtractorSuite extends FunSuite {

  val filepath: String = "/Users/Lucas Fischer/Documents/Flipper/reader/resources/test.pdf"
  val text: String = readPDF(filepath)

  /**
    * Tests if reading an existing file produces a non empty string
    */
  test("Read existing file") {
    val filepath = "/Users/Lucas Fischer/Documents/Flipper/reader/resources/something.pdf" //change USER
    assert(readPDF(filepath).nonEmpty)
  }

  /**
    * Tests if reading from a non existing file produces the expected result (null)
    */
  test("Read non existing file") {
    val fp = "Not a valid URI"
    assert(readPDF(fp) == null)
  }

  /**
    * Tests if the result of calling getAllMatchedValues is correct or not
    */
  test("Find Expected Value") {
    val matchedValues = getAllMatchedValues(text, List("name", "age"))
    val expectedValue = "Margarida Reis"
    //    val expectedValue = List(("name", List("Margarida Reis", "Lucas", "Albertina", "Justino Alberto")), ("age", List(25, 21)))
    //    matchedValues should equal(expectedValue)
    assert(matchedValues.head._2.head == expectedValue)
  }

  /**
    * Tests if the result of calling getAllMatchedValues with a keyword that does not exist in the text is empty
    */
  test("Search for non-existing keyword") {
    val matchedValues = getAllMatchedValues(text, List("color"))
    assert(matchedValues.head._2.isEmpty)
  }

  /**
    * Tests if getSingleMatchedValue returns only one value for all keys
    */
  test("Search single value") {
    val matchedValue = getSingleMatchedValue(text, List("name", "age"))
    assert(matchedValue.forall(_._2.size == 1))
  }

  /**
    * Tests if getSingleMatchedValue returns an empty value list if the given keyword does not exist in the text
    */
  test("Search single value with an non-existing keyword") {
    val matchedValue = getSingleMatchedValue(text, List("color"))
    assert(matchedValue.head._2.isEmpty)
  }

  /**
    * Tests if getAllObjects returns a list with the correct information
    */
  test("Gets All Objects") {
    val pseudoJsonObjs = getAllObjects(text, List("name"))
    val expected = List(
      List(("name", List("Margarida Reis"))),
      List(("name", List("Lucas"))),
      List(("name", List("Albertina"))),
      List(("name", List("Justino Alberto"))),
      List(("name", List("Not Defined")))
    )
    pseudoJsonObjs should equal(expected)
  }

  /**
    * Test if the matched values with the given keywords generate the correct JSON string
    */
  test("Make JSON String") {
    val matchedValues = getAllMatchedValues(text, List("name", "age"))
    val expectedJSON = "{\"name\":\"[Margarida Reis, Lucas, Albertina, Justino Alberto]\", \"age\":\"[25, 21]\"}"
    assert(makeJSONString(matchedValues) == expectedJSON)
  }

  /**
    * Tests if the matched values with the given keywords generates the correct JSON strings for all the expected objects
    */
  test("Get All JSON Objects") {
    val expected = List(
      "{\"name\": \"Margarida Reis\", \"age\": \"25\"}",
      "{\"name\": \"Lucas\", \"age\": \"21\"}",
      "{\"name\": \"Albertina\", \"age\": \"Not Defined\"}",
      "{\"name\": \"Justino Alberto\", \"age\": \"Not Defined\"}",
      "{\"name\": \"Not Defined\", \"age\": \"Not Defined\"}"
    )
    val matchedValues = getAllObjects(text, List("name", "age"))
    matchedValues.map(makeJSONString) should equal(expected)
  }


}
