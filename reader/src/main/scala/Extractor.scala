import java.io.File
import java.text.Normalizer

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper

import scala.util.matching.Regex

object Extractor {

  type Keyword = String

  val knownRegEx: Map[Keyword, Regex] = Map(
    "name" -> new Regex("(?:name is)\\s+((?:[A-Z]\\w+\\s*){1,2})"),
    "age" -> "(?:I'm|I am)\\s+(\\d{1,2})".r
  )

  def readPDF(filePath: String): String = {
    val pdf = PDDocument.load(new File(filePath))
    val document = new PDFTextStripper

    val str = Normalizer.normalize(document.getText(pdf), Normalizer.Form.NFD)
      .replaceAll("[\\p{InCombiningDiacriticalMarks}]", "")
    pdf.close()
    str
  }

  def getJSON(text: String, keywords: List[String], clientRegEx: Map[String, String] = null): List[String] = {
    keywords.flatMap(key => {
      if (knownRegEx.contains(key)) knownRegEx(key).findAllIn(text).matchData.map(_.group(1))
      else
        List("ups")
    })
  }
}
