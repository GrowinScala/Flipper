package parser.conversion

/**
  * Enumeration for the supported file type conversions in reader.Converter.scala convertPDFtoIMG
  */
abstract class FileType(){
  override def toString : String
}

case class PNG() extends FileType{
  override def toString: String = "png"
}
case class JPG() extends FileType{
  override def toString: String = "jpg"
}
case class JEPG() extends FileType{
  override def toString: String = "jpeg"
}
case class GIF() extends FileType{
  override def toString: String = "gif"
}
