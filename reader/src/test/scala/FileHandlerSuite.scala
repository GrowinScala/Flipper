import java.io.File
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import Extraction.FileHandler._

@RunWith(classOf[JUnitRunner])
class FileHandlerSuite extends FunSuite {

  /**
    * Tests that calling loadPDF with an invalid file will return None
    */
  test("loadPDF with an invalid file") {
    val nullPDF = loadPDF(null)
    val emptyPDF = loadPDF(new File(""))
    val badPDF = loadPDF(new File("non existing URI"))
    assert(nullPDF.isEmpty && emptyPDF.isEmpty && badPDF.isEmpty)
  }

  /**
    *
    */
  test("loadPDF with a valid file") {
    assert(loadPDF(new File("./reader/resources/something.pdf")).nonEmpty)
  }
}
