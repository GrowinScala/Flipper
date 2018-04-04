package parser.utils

trait Specification

/**
  * Class that represents the users choice of passing a list of possible values for a given keyword
  */
case class MultipleChoice(possibilities: List[String]) extends Specification {
//  vav selected: List[String]  //TODO
  val options: List[String] = possibilities
}

case class OneOff(possibilities: List[String]) extends Specification {
//  val selected: String  //TODO
  val options: List[String] = possibilities
}

/**
  * Enumeration for the standard POS tags which translate to both Portuguese and English
  */
abstract class POSTag(val value: String) extends Specification

/*
   POSTag Subtypes, represents the different POSTag possibilities
 */

case class Adjective() extends POSTag("ADJ")

case class ProperNoun() extends POSTag("PN")

case class Noun() extends POSTag("N")

case class PluralNoun() extends POSTag("NPLR")

case class Verb() extends POSTag("VB")

case class VerbPastParticiple() extends POSTag("VBN")

case class VerbGerund() extends POSTag("VBG")

case class Number() extends POSTag("NUM")

case class Adverb() extends POSTag("ADV")

//TODO Support more POS tags
