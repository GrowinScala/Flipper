package utils

import java.io.{File, IOException}
import java.util.Iterator

import Extraction.FileHandler._
import com.sksamuel.scrimage.filter._
import com.sksamuel.scrimage.nio.PngWriter
import javax.imageio.ImageIO
import net.sourceforge.tess4j.Tesseract
import org.apache.pdfbox.cos.COSName
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject
import org.apache.pdfbox.pdmodel.{PDDocument, PDResources}

/**
  * Singleton Object that implements all the image processing functionalities
  */
 object ImageProcessing {

  /**
    * Method that upon receiving a text file will try to read the text from it using Tess4J tesseract library
    *
    * @param file - The image file to read
    * @return an Option wrapping a String containing the images text. Returns None in case of exception
    */
  def readImageText(file: File): Option[String] = {
    val imageOption = loadImage(file) //Create Scrimage Image from a file input stream

    imageOption match {
        
      case Some(image) =>
        val resized = image.scaleToWidth((image.width * 2.0).toInt) //scale the image to be 100% wider
      val instance = new Tesseract() //Initialize Tesseract
      val filterBW = ThresholdFilter(150) //Filter the image to black and white
      val dir = new File("./target/tempImages")
        if (!dir.exists) dir.mkdir

        //Obtain the processed image file
        val resizedFile = resized.filter(filterBW).output(new File("./target/tempImages/temp_" + System.nanoTime() + ".png"))(PngWriter.MaxCompression)
        val extractedText = Option(instance.doOCR(resizedFile)) //Apply the OCR to the processed image
        cleanImageDir()
        extractedText

      case _ => None
    }
  }

  /**
    * Method that receives a document (PDF File) and trys to extract all the images from that document
    * This method writtes the image to the outter most "target" directory and also returns a list containg all the image files
    *
    * @param document - The PDF file to extract the images from
    * @return An Option wrapping a list of image files extracted from the PDF. Returns None in case of exception
    */
  def extractImgs(document: PDDocument): Option[List[File]] = {

    /**
      * Internal method that iterates over the pages in a pdf document
      *
      * @param numPages - The number of pages in a given pdf document
      * @return a List containg all the image files found in a given pdf document
      */
    def iteratePages(numPages: Int): List[File] = {
      if (numPages == -1) Nil //stop condition
      else {
        val page = document.getPage(numPages)
        val pageResources = page.getResources
        val iterator = pageResources.getXObjectNames
        getFilesList(iterator.iterator(), pageResources) ::: iteratePages(numPages - 1)
      }
    }

    /**
      * Internal method that iterates over the page resources object names, in order to find the images in a specific page
      *
      * @param iterator      - Java Iterator to Iterate over the object names
      * @param pageResources - Resources of the specified page in wich to obtain the types of resources (images, text, graphs, etc.)
      * @return a List containing all the image files found in a specific page of a given pdf document
      */
    def getFilesList(iterator: Iterator[COSName], pageResources: PDResources): List[File] = {
      if (!iterator.hasNext) Nil //stop condition
      else {
        val nextIter = iterator.next
        val obj = pageResources.getXObject(nextIter)
        if (obj.isInstanceOf[PDImageXObject]) {
          val dir = new File("./target/images")
          val file = new File("./target/images/GeneratedImage_" + System.nanoTime() + ".png")
          if (!dir.exists) dir.mkdir
          try
            ImageIO.write(obj.asInstanceOf[PDImageXObject].getImage, "png", file)
          catch {
            case io: IOException => io.printStackTrace(); None
          }
          List(file) ::: getFilesList(iterator, pageResources)
        } else Nil
      }
    }

    val mutableFilesList: List[File] = iteratePages(document.getNumberOfPages - 1)
    Option(mutableFilesList)
  }

  /**
    * Method that deletes all files from the image folder
    */
  private def cleanImageDir() {
    val dir = new File("./target/tempImages")
    if (dir.exists) {
      val files = dir.listFiles.filter(_.isFile).toList
      files.foreach(_.delete)
    }
  }
}
