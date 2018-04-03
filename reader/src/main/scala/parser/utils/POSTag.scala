package parser.utils

trait Specification

/**
  * Class that represents the users choice of passing a list of possible values for a given keyword
  */
case class Options(keyword: String, possibilities: List[String]) extends Specification {
  //  val selected : List[String]
}

/**
  * Enumeration for the standard POS tags which translate to both Portuguese and English
  */
trait POSTag extends Specification {
  val value: String
}

/*
   POSTag Subtypes, represents the different POSTag possibilities
 */

object Adjective extends POSTag {
  val value = "ADJ"
}

object ProperNoun extends POSTag {
  val value = "PN"
}

object Noun extends POSTag {
  val value = "N"
}

object PluralNoun extends POSTag {
  val value = "NPLR"
}

object Verb extends POSTag {
  val value = "VB"
}

object VerbPastParticiple extends POSTag {
  val value = "VBN"
}

object VerbGerund extends POSTag {
  val value = "VBG"
}

object Number extends POSTag {
  val value = "NUM"
}

object Adverb extends POSTag {
  val value = "ADV"
}

//TODO Support more POS tags
