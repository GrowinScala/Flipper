package reader.utils

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.Matchers._
import org.scalatest.junit.JUnitRunner
import reader.utils.OpenNLP._

@RunWith(classOf[JUnitRunner])
class OpenNLPSuite extends FunSuite {

  /**
    * Tests that OpenNLP will detect the language on a reasonably larger text
    */
  test("OpenNLP detects correct language") {
    val text = "This is a random example to demonstrate that Open NLP will detect the correct language. It's really smart"
    assert(detectLanguage(text) == "eng")
  }

  /**
    * Tests that in a really small text, OpenNLP will not correctly determine the language used in the text
    */
  test("OpenNLP fails to detect the correct language") {
    assert(detectLanguage("Hi") != "eng")
  }

  /**
    * Tests that OpenNLP gives the correct result with a "normal" text
    */
  test("OpenNLP gives the correct results") {
    val text = "An example of possible results"
    val (words, tags) = tagText(text)
    val expectedWords = Array("An", "example", "of", "possible", "results")
    val expectedTags = Array("DT", "N", "IN", "ADJ", "NPLR")

    words should equal(expectedWords)
    tags should equal(expectedTags)
  }

  /**
    * Tests that on an empty string, OpenNLP will give 0 words and 0 tags
    */
  test("tagText on an empty string") {
    val (words, tags) = tagText("")
    assert(words.length == 0 && tags.length == 0)
  }
}
