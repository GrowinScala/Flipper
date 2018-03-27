package ReaderConverter.conversion

/**
  * Enumeration for the supported file type conversions in reader.Converter.scala convertPDFtoIMG
  */
object FileType extends Enumeration {
  val png: FileType.Value = Value("png")
  val jpg: FileType.Value = Value("jpg")
  val jpeg: FileType.Value = Value("jpeg")
  val gif: FileType.Value = Value("gif")
}