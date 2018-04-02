package parser.extraction

import java.io.File
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.Matchers._
import org.scalatest.junit.JUnitRunner
import parser.extraction.Extractor._
import parser.utils.POSTag

@RunWith(classOf[JUnitRunner])
class ExtractorSuite extends FunSuite {

  val file = new File("./reader/src/main/resources/test.pdf")
  val text: Option[String] = readPDF(file)
/*
  /**
    * Tests if the result of calling getAllMatchedValues is correct or not
    */
  test("Find Expected Value") {
    val matchedValues = getAllMatchedValues(text, Map("name" -> POSTag.PN, "age" -> POSTag.NUM))
    val expectedValue = "Margarida Reis"
    if (matchedValues.isEmpty) fail("getAllMatchedValues returned an empty List")
    else assert(matchedValues.head._2.head == expectedValue)
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
    val emptyValues = getAllMatchedValues(Option(""), Map("name" -> POSTag.PN, "age" -> POSTag.NUM))
    assert(emptyValues.isEmpty)
  }


  /**
    * Tests if the result of calling getAllMatchedValues with a keyword that does not exist in the text is empty
    */
  test("Search for non-existing keyword") {
    val matchedValues = getAllMatchedValues(text, Map("color" -> POSTag.N))
    if (matchedValues.isEmpty) fail("Matched value came back as an Empty List")
    else assert(matchedValues.head._2.isEmpty)
  }

  /**
    * Tests if getSingleMatchedValue returns only one value for all keys
    */
  test("Search single value") {
    val matchedValue = getSingleMatchedValue(text, Map("name" -> POSTag.PN, "age" -> POSTag.NUM))
    assert(matchedValue.forall(_._2.size == 1))
  }

  /**
    * Tests that passing an empty text string to getSingleMatchedValue will return an empty list
    */
  test("Get single value with an empty text string") {
    val emptyText = getSingleMatchedValue(Option(""), Map("name" -> POSTag.PN, "age" -> POSTag.NUM))
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
    val matchedValue = getSingleMatchedValue(text, Map("color" -> POSTag.N))
    if (matchedValue.isEmpty) fail("Matched value came back as an Empty List")
    else assert(matchedValue.head._2.isEmpty)
  }

  /**
    * Tests that passing an empty or null text to getAllObjects will return an empty list
    */
  test("Get all objects with null or empty text") {
    val emptyObjs = getAllObjects(Option(""), Map("name" -> POSTag.PN))
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
    val pseudoJsonObjs = getAllObjects(text, Map("name" -> POSTag.PN))
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
*/
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
//    val matchedValues = getAllObjects(text, Map("name" -> POSTag.PN, "age" -> POSTag.NUM))
//    if (matchedValues.isEmpty) fail("getAllObjects returned an empty List")
//    else matchedValues.map(mp => makeJSONString(mp)) should equal(expected)
    val keys = Map("name" -> POSTag.PN, "age" -> POSTag.NUM)
    val x = getJSONObjects(text, Map("name" -> POSTag.PN, "age" -> POSTag.NUM))
    println(x.grouped(keys.size).toList)

//    getJSONObjects(text, Map("name" -> POSTag.PN, "age" -> POSTag.NUM)) should equal(expected)
  }
}
