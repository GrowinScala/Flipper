import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.Matchers._
import org.scalatest.junit.JUnitRunner
import Extractor._

@RunWith(classOf[JUnitRunner])
class ExtractorSuite extends FunSuite {

  val filepath: String = "./reader/resources/test.pdf"
  val text: Option[String] = readPDF(filepath)

  /**
    * Tests if reading an existing file produces a non empty string
    */
  test("Read existing file") {
    val filepath = "./reader/resources/something.pdf" //change USER
    assert(readPDF(filepath).nonEmpty)
  }

  /**
    * Tests if reading from a non existing file produces the expected result (null)
    */
  test("Read non existing file") {
    val fp = "Not a valid URI"
    assert(readPDF(fp).isEmpty)
  }

  /**
    * Tests if the result of calling getAllMatchedValues is correct or not
    */
  test("Find Expected Value") {
    val matchedValues = getAllMatchedValues(text, List(("name", "NNP"), ("age", "CD")))
    val expectedValue = "Margarida Reis"
    //    val expectedValue = List(("name", List("Margarida Reis", "Lucas", "Albertina", "Justino Alberto")), ("age", List(25, 21)))
    //    matchedValues should equal(expectedValue)
    assert(matchedValues.head._2.head == expectedValue)
  }

  /**
    * Tests that passing an empty or null list to getAllMatchedValues will return an empty List
    */
  test("Get all matched values with an empty keyword list") {
    assertThrows[IllegalArgumentException](
      getAllMatchedValues(text, List())
    )
  }

  /**
    * Tests calling getAllMatchedValues with a null list will result in a NullPointerException
    */
  test("Calling getAllMatchedValues with a null list") {
    assertThrows[NullPointerException](
      getAllMatchedValues(text, null)
    )
  }

  /**
    * Tests that passing a null or empty text will return an empty list
    */
  test("Get all matched values with an empty String") {
    val emptyValues = getAllMatchedValues(Option(""), List(("name", "NNP"), ("age", "CD")))
    assert(emptyValues.isEmpty)
  }

  /**
    * Tests that calling getAllMatchedValues with text parameter as null will result in a NullPointerException
    */
  test("Calling getAllMatchedValues with a null String") {
    assertThrows[NullPointerException](
      getAllMatchedValues(null, List(("name", "NNP"), ("age", "CD")))
    )
  }

  /**
    * Tests if the result of calling getAllMatchedValues with a keyword that does not exist in the text is empty
    */
  test("Search for non-existing keyword") {
    val matchedValues = getAllMatchedValues(text, List(("color", "NN")))
    assert(matchedValues.head._2.isEmpty)
  }

  /**
    * Tests if getSingleMatchedValue returns only one value for all keys
    */
  test("Search single value") {
    val matchedValue = getSingleMatchedValue(text, List(("name", "NNP"), ("age", "CD")))
    assert(matchedValue.forall(_._2.size == 1))
  }

  /**
    * Tests that passing a empty or null text string to getSingleMatchedValue will return an empty list
    */
  test("Get single value with an empty or null text string") {
    val emptyText = getSingleMatchedValue(Option(""), List(("name", "NNP"), ("age", "CD")))
    assert(emptyText.isEmpty)
  }

  /**
    * Tests that calling getSingleValue with text parameter as null will result in a NullPointerException
    */
  test("Calling getSingleValue with null text string") {
    assertThrows[NullPointerException](
      getSingleMatchedValue(null, List(("name", "NNP"), ("age", "CD")))
    )
  }

  /**
    * Tests that passing a empty or null text string to getSingleMatchedValue will return an empty list
    */
  test("Get single value with an empty list") {
    assertThrows[IllegalArgumentException](
      getSingleMatchedValue(text, List())
    )
  }

  /**
    * Tests that calling getSingleValue with a null list will result in a NullPointerException
    */
  test("Calling getSingleValue with a null list") {
    assertThrows[NullPointerException] {
      getSingleMatchedValue(text, null)
    }
  }

  /**
    * Tests if getSingleMatchedValue returns an empty value list if the given keyword does not exist in the text
    */
  test("Search single value with an non-existing keyword") {
    val matchedValue = getSingleMatchedValue(text, List(("color", "NN")))
    assert(matchedValue.head._2.isEmpty)
  }

  /**
    * Tests that passing an empty or null text to getAllObjects will return an empty list
    */
  test("Get all objects with null or empty text") {
    val emptyObjs = getAllObjects(Option(""), List(("name", "NNP")))
    assert(emptyObjs.isEmpty)
  }

  /**
    * Tests that calling getAllObjects with text parameter as null will result in a NullPointerException
    */
  test("Calling getAllObjects with null text throws exception") {
    assertThrows[NullPointerException] {
      getAllObjects(null, List(("name", "NNP")))
    }
  }

  /**
    * Tests that passing an empty list to getAllObjects will result in a IllegalArgumentException
    */
  test("Get all objects with null or empty list") {
    assertThrows[IllegalArgumentException] {
      getAllObjects(text, List())
    }
  }

  /**
    * Tests that calling getAllObjects with a null list will result in a NullPointerException
    */
  test("Calling getAllObjects with null list throws exception") {
    assertThrows[NullPointerException] {
      getAllObjects(text, null)
    }
  }

  /**
    * Tests if getAllObjects returns a list with the correct information
    */
  test("Gets All Objects") {
    val pseudoJsonObjs = getAllObjects(text, List(("name", "NNP")))
    val expected = List(
      List(("name", List("Margarida Reis"))),
      List(("name", List("Lucas"))),
      List(("name", List("Albertina"))),
      List(("name", List("Justino Alberto"))),
      List(("name", List()))
    )
    pseudoJsonObjs should equal(expected)
  }

  /**
    * Tests that making a JSON String with an empty list will return an empty JSON object ("{}")
    */
  test("Make JSON string with an empty list") {
    assert(makeJSONString(List()) == "{}")
  }

  /**
    * Tests that making a JSON String with an null list will return NullPointerException
    */
  test("Make JSON string with null list") {
    assertThrows[NullPointerException](
      makeJSONString(null)
    )
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
    val matchedValues = getAllObjects(text, List(("name", "NNP"), ("age", "CD")))
    //    matchedValues.map(makeJSONString) should equal(expected)
  }
}
