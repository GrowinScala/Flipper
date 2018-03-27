import java.io.File
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import Generator.FileHandler._

@RunWith(classOf[JUnitRunner])
class FileHandlerSuite extends FunSuite {
  /**
    * Tests that calling loadCSSFile with an invalid file will return None
    */
  test("loadPDF with an invalid file") {
    val nullPDF = loadCSSFile(null)
    val badPDF = loadCSSFile(new File("non existing URI"))
    assert(nullPDF.isEmpty && badPDF.isEmpty)
  }

  /**
    * Tests that calling loadCSSFile with a valid file will not return None
    */
  test("loadPDF with a valid file") {
    assert(loadCSSFile(new File("test.css")).isDefined)
  }
}
