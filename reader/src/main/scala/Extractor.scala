import java.io.File
import java.text.Normalizer

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper

object Extractor {

  def readPDF(filePath: String, keywords : List[String] = null, clientRegEx : Map[String , String] = null): String = {
    val pdf = PDDocument.load(new File(filePath))
    val document = new PDFTextStripper

    val str = Normalizer.normalize(document.getText(pdf), Normalizer.Form.NFD)
      .replaceAll("[\\p{InCombiningDiacriticalMarks}]", "")
    pdf.close()
    str
  }
}
