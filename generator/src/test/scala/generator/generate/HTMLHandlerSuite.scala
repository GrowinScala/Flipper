package generator.generate

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import generator.generate.HTMLHandler._
import generator.utils._

@RunWith(classOf[JUnitRunner])
class HTMLHandlerSuite extends FunSuite {

  /**
    * Tests that passing a supported/corrected HTML entity will return the corresponding FormattingType eqiuvelant
    */
  test("calling stringToHTMLTag with correct HTML Entity") {
    assert(
      stringToHTMLTag("h1") == H1() &&
        stringToHTMLTag("h2") == H2() &&
        stringToHTMLTag("H3") == H3() &&
        stringToHTMLTag("orderedList") == OrderedList() &&
        stringToHTMLTag("UnOrDerEdlIsT") == UnOrderedList() &&
        stringToHTMLTag("table") == Table() &&
        stringToHTMLTag("p") == P() &&
        stringToHTMLTag("span") == Text() &&
        stringToHTMLTag("somethin-Else") == Text()
    )
  }
}
