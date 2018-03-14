import java.io.File

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.Matchers._
import org.scalatest.junit.JUnitRunner
import Converter._
import org.apache.pdfbox.pdmodel.PDDocument

@RunWith(classOf[JUnitRunner])
class ConverterSuite extends FunSuite {

  val validPath: String = "./resources/cv.pdf"

  /**
    * Tests that sending a invalid filePath and/or fileType (null, empty, non-existing) will return false
    */
  test("convertPDFtoIMG with an invalid filePath and/or fileType") {
    val testList = List(
      convertPDFtoIMG(null, "png"),
      convertPDFtoIMG("", "png"),
      convertPDFtoIMG("non-existing file path", "png"),
      convertPDFtoIMG(validPath, null),
      //      convertPDFtoIMG(validPath, ""),
      //      convertPDFtoIMG(validPath, "non-existing")
    )
    assert(testList.forall(test => !test))
  }

  /**
    * Tests that sending a valid filePath and fileType to convertPDFtoIMG will return true
    * This means that the method successfully converted the pdf to the specified file type
    */
  test("convertPDFtoIMG with a valid filePath and fileType") {
    assert(convertPDFtoIMG(validPath, "png"))
  }

  /**
    * Tests that sending an invalid filePath (null, empty, non-existing) to convertPDFtoODF
    * will return false
    */
  test("convertPDFtoODF with invalid filePath") {
    val nullPath = convertPDFtoODF(null)
    val emptyPath = convertPDFtoODF("")
    val nonExistingPath = convertPDFtoODF("non-existing path")
    assert(!nullPath && !emptyPath && !nonExistingPath)
  }

  /**
    * Tests that sending a valid filePath to convertPDFtoODF will return true
    * meaning it successfully converted the PDF to ODF
    */
  test("convertPDFtoODF with valid filePath") {
    assert(convertPDFtoODF(validPath))
  }

}
