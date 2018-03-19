/**
  * Enumeration for the supported file type conversions in Converter.scala convertPDFtoIMG
  */
object FileType extends Enumeration {
  val png = Value("png")
  val jpg = Value("jpg")
  val jpeg = Value("jpeg")
  val gif = Value("gif")
}