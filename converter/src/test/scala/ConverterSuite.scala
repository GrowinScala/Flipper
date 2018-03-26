import java.io.File

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import Converter._

@RunWith(classOf[JUnitRunner])
class ConverterSuite extends FunSuite {

  val validFile = new File("./converter/resources/cv.pdf")

  /**
    * Tests that sending a invalid filePath will return false
    */
  test("convertPDFtoIMG with an invalid filePath") {
    val testList = List(
      convertPDFtoIMG(new File(""), FileType.png),
      convertPDFtoIMG(new File("non-existing file path"), FileType.png)
    )
    assert(testList.forall(test => !test))
  }

  /**
    * Test that sending a null filepath return false since the conversion was not successful
    */
  test("convertPDFtoIMG with null filepath") {
    val res = convertPDFtoIMG(null, FileType.png)
    assert(!res)
  }

  /**
    * Test that sending a null fileType returns IllegalArgumentException
    */
  test("convertPDFtoIMG with null fileType") {
    assertThrows[IllegalArgumentException](
      convertPDFtoIMG(validFile, null)
    )
  }

  /**
    * Tests that sending a valid filePath and fileType to convertPDFtoIMG will return true
    * This means that the method successfully converted the pdf to the specified file type
    */
  test("convertPDFtoIMG with a valid filePath and fileType") {
    assert(convertPDFtoIMG(validFile, FileType.png))
  }

  /**
    * Tests that sending an invalid filePath (null, empty, non-existing) to convertPDFtoODF
    * will return false
    */
  test("convertPDFtoODT with invalid filePath") {
    val nullPath = convertPDFtoODT(null)
    val emptyPath = convertPDFtoODT(new File(""))
    val nonExistingPath = convertPDFtoODT(new File("non-existing path"))
    assert(!nullPath && !emptyPath && !nonExistingPath)
  }

  /**
    * Tests that sending a valid filePath to convertPDFtoODF will return true
    * meaning it successfully converted the PDF to ODF
    */
  test("convertPDFtoODT with valid filePath") {
    assert(convertPDFtoODT(validFile))
  }
}
