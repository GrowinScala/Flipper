import java.io.{File, IOException}

import javax.imageio.ImageIO
import net.sourceforge.tess4j.Tesseract
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject

/**
  * Singleton Object that implements all the image processing functionalities
  */
//noinspection TypeCheckCanBeMatch,SpellCheckingInspection
object ImageProcessing {

  /**
    * Method that upon receiving a text file will try to read the text from it using Tess4J tesseract library
    *
    * @param file - The image file to read
    * @return an Option wrapping a String containing the images text. Returns None in case of exception
    */
  def readImageText(file: File): Option[String] = {
    val instance = new Tesseract()
    try {
      Option(instance.doOCR(file))
    } catch {
      case e: Exception => e.printStackTrace(); None
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

    //TODO Remove all the files in ./target/images first
    //    val dir = new File("./target/images")
    //    val files = dir.listFiles.filter(_.isFile).toList
    //    files.foreach(f => f.delete())

    val numPages = document.getNumberOfPages
    //Using a mutable List for return a List of files (images) found in the pdf document
    //This was implemented mutably because pRes.getXObjectNames returns a Java Iterator[CosName] and there is no .map/.flatMap fucntion to it, only .forEach
    var mutableFilesList: List[File] = List() //mutable
    for (i <- 0 until numPages) {
      val page = document.getPage(i)
      val pRes = page.getResources
      pRes.getXObjectNames.forEach(r => {
        val o = pRes.getXObject(r)
        if (o.isInstanceOf[PDImageXObject]) {
          val file = new File("./target/images/Page" + (i + 1) + "_" + System.nanoTime() + ".png")
          //          val image = o.asInstanceOf[PDImageXObject].getImage
          //          saveGridImage(file, image)
          try {
            ImageIO.write(o.asInstanceOf[PDImageXObject].getImage, "png", file)
          } catch {
            case io: IOException => io.printStackTrace(); None
          }
          mutableFilesList = mutableFilesList :+ file
        }
      })
    }
    Option(mutableFilesList)
  }

  //No improvment on OCR results :(
  //  def saveGridImage(output: File, gridImage: BufferedImage): Unit = {
  //    val iw = ImageIO.getImageWritersByFormatName("png")
  //    while (iw.hasNext) {
  //      val writer = iw.next
  //      val writeParam = writer.getDefaultWriteParam
  //      val typeSpecifier = ImageTypeSpecifier.createFromBufferedImageType(BufferedImage.TYPE_INT_RGB)
  //      val metadata = writer.getDefaultImageMetadata(typeSpecifier, writeParam)
  //
  //      setDPI(metadata)
  //      val stream = ImageIO.createImageOutputStream(output)
  //      try {
  //        writer.setOutput(stream)
  //        writer.write(metadata, new IIOImage(gridImage, null, metadata), writeParam)
  //      } finally {
  //        stream.close()
  //      }
  //    }
  //  }
  //
  //  def setDPI(metadata: IIOMetadata): Unit = {
  //    val dotsPerMili = 1.0 * 1000 / 10 / 25.4
  //    val horiz = new IIOMetadataNode("HorizontalPixelSize")
  //    horiz.setAttribute("value", dotsPerMili.toString)
  //
  //    val vert = new IIOMetadataNode("VerticalPixelSize")
  //    vert.setAttribute("value", dotsPerMili.toString)
  //
  //    val dim = new IIOMetadataNode("Dimension")
  //    dim.appendChild(horiz)
  //    dim.appendChild(vert)
  //
  //    val root = new IIOMetadataNode("javax_imageio_1.0")
  //    root.appendChild(dim)
  //    metadata.mergeTree("javax_imageio_1.0", root)
  //  }
}
