import java.io.File

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.Matchers._
import org.scalatest.junit.JUnitRunner
import Converter._
import org.apache.pdfbox.pdmodel.PDDocument

@RunWith(classOf[JUnitRunner])
class ConverterSuite extends FunSuite {

  val validPath: String = "./converter/resources/cv.pdf"

  /**
    * Tests that sending a invalid filePath will return false
    */
  test("convertPDFtoIMG with an invalid filePath") {
    val testList = List(
      convertPDFtoIMG("", "png"),
      convertPDFtoIMG("non-existing file path", "png")
    )
    assert(testList.forall(test => !test))
  }

  /**
    * Test that sending an empty fileType returns IllegalArgumentException
    */
  test("convertPDFtoIMG with empty fileType"){
    assertThrows[IllegalArgumentException](
      convertPDFtoIMG(validPath,"")
    )
  }

  /**
    * Test that sending an invalid fileType returns IllegalArgumentException
    */
  test("convertPDFtoImg with invalid fileType"){
    assertThrows[IllegalArgumentException](
      convertPDFtoIMG(validPath,"non-existing")
    )
  }

  /**
    * Test that sending a null filepath return false since the conversion was not successful
    */
  test("convertPDFtoIMG with null filepath"){
    val res = convertPDFtoIMG(null, "png")
    assert(!res)
  }

  /**
    * Test that sending a null fileType returns IllegalArgumentException
    */
  test("convertPDFtoIMG with null fileType"){
    assertThrows[IllegalArgumentException](
      convertPDFtoIMG(validPath,null)
    )
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
