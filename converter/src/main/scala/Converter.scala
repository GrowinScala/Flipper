import java.io.{File, PrintWriter}
import javax.imageio.ImageIO
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.rendering.PDFRenderer
import ImageProcessing._
import com.google.common.collect.Iterators
import org.fit.pdfdom.{PDFDomTree, PDFDomTreeConfig}
import org.odftoolkit.odfdom.`type`.Color
import org.odftoolkit.simple.TextDocument
import org.odftoolkit.simple.style.{Font, StyleTypeDefinitions}
import scala.io.Source

/**
  * Singleton object that implements the functions regarding the conversion of pdf into other file types
  */
object Converter {

  /**
    * Method that converts a pdf file into a png image
    *
    * @param filePath - String containing the URI to the pdf file
    * @param fileType - FileType with one of the possible file types of the Enum FileType to be converted into
    * @return - A Boolean saying if the conversion was successful
    * @throws IllegalArgumentException if the specified fileType did not equal the supported file types
    */
  @throws[IllegalArgumentException]
  def convertPDFtoIMG(filePath: String, fileType: FileType.Value): Boolean = {
    require(fileType != null, "File type must be one of png, jpg, gif or jpeg")

    try {
      val pdf = PDDocument.load(new File(filePath))
      val renderer = new PDFRenderer(pdf)
      for (i <- 0 until pdf.getNumberOfPages) {
        val image = renderer.renderImage(i)
        ImageIO.write(image, fileType.toString, new File("./target/images/Converted_Page" + i + "_" + System.nanoTime() + "." + fileType.toString))
      }
      true
    } catch {
      case e: Exception => e.printStackTrace(); false
    }
  }

  /**
    * Method that creates a odf file with the information taken from a pdf (Note: does not maintain image and text order)
    *
    * @param filePath - String containing the URI to the pdf file
    * @return - A Boolean saying if the conversion was successful
    */
  def convertPDFtoODT(filePath: String): Boolean = {
    try {
      convertPDFtoHTML(filePath)
      convertHTMLtoODT(filePath)
      true
    } catch {
      case e: Exception => e.printStackTrace(); false
    }
  }

  def convertPDFtoHTML(filePath:String): Unit ={
    val outfile = "out.html"
    val config = PDFDomTreeConfig.createDefaultConfig
    val document = PDDocument.load(new File(filePath))
    val parser = new PDFDomTree(config)
    val output = new PrintWriter(outfile, "utf-8")
    parser.writeText(document, output)
    output.close()
    document.close()
  }

  def convertHTMLtoODT(filePath:String): Unit ={
    val bufferedSource = Source.fromFile(new File("out.html"))
    val htmlLines = bufferedSource.getLines.toList
    val pdf = PDDocument.load(new File(filePath))
    val odt= TextDocument.newTextDocument()
    val regexLine = "<(/body|div class=\"page\"|div class=\"p\"|img).*(?:>|)".r
    val regexPara = ".*<.*style=\"top:(\\d+).*font-family:(.*);font-size:(\\d+).*\">(.*)</div>".r
    val regexColo = ".*;color:(.{7});.*".r
    extractImgs(pdf)
    pdf.close()
    bufferedSource.close()
    val dir = new File("./target/images")
    var imgs = dir.listFiles.filter(_.isFile).toList //TODO Make recusrsive

    for(i <- htmlLines.indices){
      val line = htmlLines(i)
      val group = regexLine.findAllIn(line).matchData.map(_.group(1)).toList.mkString("")
      val parTotal:Int = Iterators.size(odt.getParagraphIterator)
      val par = odt.getParagraphByIndex(parTotal-1,false)

      group match {
        // New Page
        case "div class=\"page\"" => if(line.contains("page_0")){ } else { odt.addPageBreak() }
        // Text Paragraph
        case "div class=\"p\"" =>
          val prevLine = htmlLines(i-1)
          val regexPara(top,fontType,fontSize,text) = line
          val colorStr = if(line.contains("color")){ regexColo.findFirstMatchIn(line).map(_.group(1)).mkString("") } else {"#000000"}
          val color = Color.valueOf(colorStr)
          if(prevLine.contains("div class=\"p\"")){
            val regexPara(topP,_,_,_) = prevLine
            if(topP.toInt < top.toInt){
              val newPar = odt.addParagraph(text)
              newPar.setFont(new Font(fontType, StyleTypeDefinitions.FontStyle.REGULAR, Integer.parseInt(fontSize), color))
            }
            else{
              par.appendTextContent(" "+text)
            }
          } else {
            val newPar = odt.addParagraph(text)
            newPar.setFont(new Font(fontType, StyleTypeDefinitions.FontStyle.REGULAR, Integer.parseInt(fontSize), color))
          }
        // Image
        case "img" =>
          val img = imgs.last
          println(imgs.size)
          println(img.toURI)
          imgs = imgs.dropRight(1)
          odt.addParagraph("")
          odt.newImage(img.toURI)
          odt.addParagraph("")
        // Else
        case _ =>
      }
    }

    odt.save("out.odt")
    odt.close()
    cleanImageDir()
  }

  /**
    * Method that deletes all files from the image folder
    */
  private def cleanImageDir(){
    val dir = new File("./target/images")
    val files = dir.listFiles.filter(_.isFile).toList
    files.foreach(_.delete)
  }
}