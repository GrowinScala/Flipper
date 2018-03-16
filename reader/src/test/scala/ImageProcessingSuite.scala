import java.io.File

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.Matchers._
import org.scalatest.junit.JUnitRunner
import ImageProcessing._
import org.apache.pdfbox.pdmodel.PDDocument

//noinspection ComparingLength
@RunWith(classOf[JUnitRunner])
class ImageProcessingSuite extends FunSuite {

  val filepath: String = "./reader/resources/imgTester.pdf"
  val pdf: PDDocument = PDDocument.load(new File(filepath))

  /**
    * Tests that extractImgs returns an empty list of images when reading a pdf with no images
    */
  test("extractImgs returns empty list") {
    val fp = "./reader/resources/test.pdf"
    val pdf = PDDocument.load(new File(fp))
    assert(extractImgs(pdf).isEmpty)
  }

  /**
    * Tests that sending a null file to extractImgs with return an empty list
    */
  test("extractImgs returns an empty list with a null file") {
//    assertThrows[NullPointerException](
//      extractImgs(null)
//    )
    assert(extractImgs(null).isEmpty)
  }

  /**
    * Tests that sending a file with, for example, two images, will return a list of files with size two
    */
  test("extractImgs returns correct number of images") {
    assert(extractImgs(pdf).size == 2)
  }

  /**
    * Tests that sending a null file to readImageText will return an empty string ("")
    */
  test("readImageText with a null file") {
//    assertThrows[NullPointerException](
//      readImageText(null)
//    )
    assert(readImageText(null) == "")
  }

  /**
    * Tests that sending a image of Growin's logo will return a close guess of the text in the image
    */
  test("readImageText returns a close guess of the images text") {
    val growinLogo = extractImgs(pdf).head
    assert(readImageText(growinLogo).substring(0, 5) == "growi")
  }

}
