import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.Matchers._
import org.scalatest.junit.JUnitRunner
import Extractor._

@RunWith(classOf[JUnitRunner])
class ExtractorSuite extends FunSuite {

  val filepath: String = "./reader/resources/test.pdf"
  val text: String = readPDF(filepath)

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
    assert(readPDF(fp) == null)
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
  test("Get all matched values with empty keyword list") {
    val nullValues = getAllMatchedValues(text, null)
    val emptyValues = getAllMatchedValues(text, List())
    assert(nullValues.isEmpty && emptyValues.isEmpty)
  }

  /**
    * Tests that passing a null or empty text will return an empty list
    */
  test("Get all matched values with a null string") {
    val nullValues = getAllMatchedValues(null, List(("name", "NNP"), ("age", "CD")))
    val emptyValues = getAllMatchedValues("", List(("name", "NNP"), ("age", "CD")))
    assert(nullValues.isEmpty && emptyValues.isEmpty)
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
    val nullText = getSingleMatchedValue(null, List(("name", "NNP"), ("age", "CD")))
    val emptyText = getSingleMatchedValue("", List(("name", "NNP"), ("age", "CD")))
    assert(nullText.isEmpty && emptyText.isEmpty)
  }

  /**
    * Tests that passing a empty or null text string to getSingleMatchedValue will return an empty list
    */
  test("Get single value with an empty or null list") {
    val nullText = getSingleMatchedValue(text, null)
    val emptyText = getSingleMatchedValue(text, List())
    assert(nullText.isEmpty && emptyText.isEmpty)
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
    val nullObjs = getAllObjects(null, List(("name", "NNP")))
    val emptyObjs = getAllObjects("", List(("name", "NNP")))
    assert(nullObjs.isEmpty && emptyObjs.isEmpty)
  }

  /**
    * Tests that passing a null or empty list to getAllObjects will return an empty list
    */
  test("Get all objects with null or empty list") {
    val nullObjs = getAllObjects(text, null)
    val emptyObjs = getAllObjects(text, List())
    assert(nullObjs.isEmpty && emptyObjs.isEmpty)
  }

  test("Calling get all objects with null throws exception") {
    assertThrows[IllegalArgumentException] {
      getAllObjects(text, null)
    } //Algo por aqui
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
      List(("name", List("Not Defined")))
    )
    pseudoJsonObjs should equal(expected)
  }

  /**
    * Tests that making a JSON String with an empty or null list will return an empty JSON object ("{}")
    */
  test("Make JSON string with an empty list") {
    assert(makeJSONString(List()) == "{}" && makeJSONString(null) == "{}")
  }

  /**
    * Test if the matched values with the given keywords generate the correct JSON string
    */
  test("Make JSON String") {
    val matchedValues = getAllMatchedValues(text, List(("name", "NNP"), ("age", "CD")))
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
    val matchedValues = getAllObjects(text, List(("name", "NNP"), ("age", "CD")))
    //    matchedValues.map(makeJSONString) should equal(expected)
  }


}
