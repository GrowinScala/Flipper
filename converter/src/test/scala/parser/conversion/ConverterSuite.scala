package parser.conversion

import java.io.File
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import Converter._

@RunWith(classOf[JUnitRunner])
class
ConverterSuite extends FunSuite {

  val validFile = new File("./converter/src/main/resources/cv.pdf")

  /**
    * Tests that sending a valid filePath and fileType to convertPDFtoIMG will return true
    * This means that the method successfully converted the pdf to the specified file type
    */
  test("convertPDFtoIMG with a valid filePath and fileType") {
    assert(convertPDFtoIMG(validFile, PNG()))
  }

  /**
    * Tests that sending an invalid filePath convertPDFtoIMG will return false
    * This means that the method did not convert the pdf to the specified file type
    */
  test("convertPDFtoIMG with invalid filePath") {
    assert(!convertPDFtoIMG(new File(""), PNG()))
  }

  /**
    * Tests that sending a valid filePath to convertPDFtoODF will return true
    * meaning it successfully converted the PDF to ODF
    */
  test("convertPDFtoODT with valid filePath") {
    assert(convertPDFtoODT(validFile))
  }

  /**
    * Tests that sending an invalid filePath to convertPDFtoODF will return false
    * meaning it did not convert the PDF to ODF
    */
  test("convertPDFtoODT with invalid filePath") {
    assert(!convertPDFtoODT(new File("")))
  }
}
