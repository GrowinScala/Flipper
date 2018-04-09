package parser.extraction

import java.io.File

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.Matchers._
import org.scalatest.junit.JUnitRunner
import parser.extraction.Extractor._
import parser.utils._

@RunWith(classOf[JUnitRunner])
class ExtractorSuite extends FunSuite {

  val file = new File("./reader/src/main/resources/test.pdf")
  val text: Option[String] = readPDF(file)
  val colorText = Some("In this example text we will want to find a specific color." +
                       " Here are some of the options: blue , red, green, orange." +
                       " These are just some examples.")

  /**
    * Tests if the result of calling getAllMatchedValues is correct or not
    */
  test("Find Expected Value") {
    val matchedValues = getAllMatchedValues(text, Map("name" -> ProperNoun(), "age" -> Number()))
    val expectedValue = "Margarida Reis"
    if (matchedValues.isEmpty) fail("getAllMatchedValues returned an empty List")
    else assert(matchedValues.head._2.head == expectedValue)
  }

  /**
    * Tests that passing a list of possible values to getAllMatchedValues will return one of the possibilities passed (assuming it exists in the text)
    */
  test("Find expected value from one of possible values") {
    val matchedValues = getAllMatchedValues(colorText, Map("color" -> OneOf(List("blue", "red"))))
    val expectedValue = "blue"
    assert(matchedValues.head._2.head == expectedValue)
  }

  /**
    * Tests that passing a list of possible values to getAllMatchedValues will return multiple of the possibilities passed (assuming it exists in the text)
    */
  test("Find expected values from multiple of possible values") {
    val matchedValues = getAllMatchedValues(colorText, Map("color" -> MultipleOf(List("blue", "red"))))
    val expectedValue = List("blue", "red")
    matchedValues.head._2 should equal(expectedValue)
  }

  /**
    * Tests that passing a list of possible values that don't exist in the text will return an empty list in both OneOf and MultipleOf
    */
  test("OneOf/MultipleOf does not find any value") {
    val oneOfValues = getAllMatchedValues(colorText, Map("color" -> OneOf( List("does not exist"))))
    val multipleValues = getAllMatchedValues(colorText, Map("color" -> MultipleOf( List("does not exist"))))
    assert(oneOfValues.head._2.isEmpty && multipleValues.head._2.isEmpty)
  }

  /**
    * Tests that passing a OneOf object with an empty possibilities list will result in an IllegalArgumentException
    */
  test("OneOf with an empty possibilities list") {
    assertThrows[IllegalArgumentException] {
      getAllMatchedValues(colorText, Map("color" -> OneOf( List())))
    }
  }

  /**
    * Tests that passing a MultipleOf object with an empty possibilities list will result in an IllegalArgumentException
    */
  test("MultipleOf with an empty possibilities list") {
    assertThrows[IllegalArgumentException] {
      getAllMatchedValues(colorText, Map("color" -> MultipleOf( List())))
    }
  }

  /**
    * Tests that passing an empty keywords map will result in a IllegalArgumentException
    */
  test("Get all matched values with an empty keyword Map") {
    assertThrows[IllegalArgumentException](
      getAllMatchedValues(text, Map())
    )
  }

  /**
    * Tests that passing a null or empty text will return an empty list
    */
  test("Get all matched values with an empty String") {
    val emptyValues = getAllMatchedValues(Option(""), Map("name" -> ProperNoun(), "age" -> Number()))
    assert(emptyValues.isEmpty)
  }


  /**
    * Tests if the result of calling getAllMatchedValues with a keyword that does not exist in the text is empty
    */
  test("Search for non-existing keyword") {
    val matchedValues = getAllMatchedValues(text, Map("color" -> Noun()))
    if (matchedValues.isEmpty) fail("Matched value came back as an Empty List")
    else assert(matchedValues.head._2.isEmpty)
  }

  /**
    * Tests if getSingleMatchedValue returns only one value for all keys
    */
  test("Search single value") {
    val matchedValue = getSingleMatchedValue(text, Map("name" -> ProperNoun(), "age" -> Number()))
    assert(matchedValue.forall(_._2.size == 1))
  }

  /**
    * Tests that passing an empty text string to getSingleMatchedValue will return an empty list
    */
  test("Get single value with an empty text string") {
    val emptyText = getSingleMatchedValue(Option(""), Map("name" -> ProperNoun(), "age" -> Number()))
    assert(emptyText.isEmpty)
  }


  /**
    * Tests that passing an empty keywords map to getSingleMatchedValue will result in a IlllegalArgumentException
    */
  test("Get single value with an empty keywords Map") {
    assertThrows[IllegalArgumentException](
      getSingleMatchedValue(text, Map())
    )
  }


  /**
    * Tests if getSingleMatchedValue returns an empty value list if the given keyword does not exist in the text
    */
  test("Search single value with an non-existing keyword") {
    val matchedValue = getSingleMatchedValue(text, Map("color" -> Noun()))
    if (matchedValue.isEmpty) fail("Matched value came back as an Empty List")
    else assert(matchedValue.head._2.isEmpty)
  }

  /**
    * Tests that passing an empty or null text to getAllObjects will return an empty list
    */
  test("Get all objects with null or empty text") {
    val emptyObjs = getAllObjects(Option(""), Map("name" -> ProperNoun()))
    assert(emptyObjs.isEmpty)
  }


  /**
    * Tests that passing an empty list to getAllObjects will result in a IllegalArgumentException
    */
  test("Get all objects with an empty keywords map") {
    assertThrows[IllegalArgumentException] {
      getAllObjects(text, Map())
    }
  }

  /**
    * Tests if getAllObjects returns a list with the correct information
    */
  test("Gets All Objects") {
    val pseudoJsonObjs = getAllObjects(text, Map("name" -> ProperNoun()))
    val expected = List(
      Map("name" -> List("Margarida Reis")),
      Map("name" -> List("Lucas")),
      Map("name" -> List("Albertina")),
      Map("name" -> List("Justino Alberto"))
    )
    if (pseudoJsonObjs.isEmpty) fail("getAllObjects returned an empty List")
    else pseudoJsonObjs should equal(expected)
  }

  /**
    * Tests that making a JSON String with an empty list will return an empty JSON object ("{}")
    */
  test("Make JSON string with an empty list") {
    assert(makeJSONString(Map()) == "{}")
  }

  /**
    * Tests if the matched values with the given keywords generates the correct JSON strings for all the expected objects
    */
  test("Get All JSON Objects") {
    val expected = List(
      "{\"name\" : \"Margarida Reis\", \"age\" : 25}",
      "{\"name\" : \"Lucas\", \"age\" : 21}",
      "{\"name\" : \"Albertina\", \"age\" : \"\"}",
      "{\"name\" : \"Justino Alberto\", \"age\" : \"\"}"
    )
    getJSONObjects(text, Map("name" -> ProperNoun(), "age" -> Number())) should equal(expected)
  }
}
