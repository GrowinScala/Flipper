/**
  * Enumeration for the standard POS tags which translate to both Portuguese and English
  */
object POSTag extends Enumeration {
  val ADJ = Value("ADJ")
  val PN = Value("PN")
  val N = Value("N")
  val NPLR = Value("NPLR")
  val VB = Value("VB")
  val VBN = Value("VBN")
  val VBG = Value("VBG")
  val NUM = Value("NUM")
  val ADV = Value("ADV")
}
//TODO Support more POS tags
