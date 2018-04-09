package parser.extraction

import java.io.File
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import parser.extraction.FileHandler._

@RunWith(classOf[JUnitRunner])
class FileHandlerSuite extends FunSuite {

  /**
    * Tests that calling loadPDF with an invalid file will return None
    */
  test("loadPDF with an invalid file") {
    val nullPDF = loadPDF(null)
    val badPDF = loadPDF(new File("non existing URI"))
    assert(nullPDF.isEmpty && badPDF.isEmpty)

  }

  /**
    * Tests that calling loadPDF with a valid file will not return None
    */
  test("loadPDF with a valid file") {
    assert(loadPDF(new File("./reader/src/main/resources/something.pdf")).isDefined)
  }

  /**
    * Tests that calling loadImage with an invalid file will return None
    */
  test("loadImage with an invalid file") {
    val nullImage = loadImage(null)
    val badImage = loadImage(new File("non existing URI"))
    val notAnImage = loadImage(new File("./reader/src/main/resources/something.pdf"))

    assert(nullImage.isEmpty && badImage.isEmpty && notAnImage.isEmpty)
  }

  /**
    * Tests that calling loadImage with a valid file will not return None
    */
  test("loadImage with a valid file") {
    assert(loadImage(new File("./reader/src/main/resources/testImg.PNG")).isDefined)
  }
}
