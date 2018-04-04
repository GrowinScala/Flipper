package parser.utils

trait Specification

/**
  * Class that represents the users choice of passing a list of possible values for a given keyword.
  * It represents the ability of return one or more values from the input possible values list
  *
  * @param possibilities - List of possible values to be found in the text
  * @throws IllegalArgumentException If the possibilities list is empty
  */
@throws[IllegalArgumentException]
case class MultipleOf(possibilities: List[String]) extends Specification {
  require(possibilities.nonEmpty)
}

/**
  * Class that represents the users choice of passing a list of possible values and
  * It represents the ability of returning only one of the values from the possible values list
  *
  * @param possibilities - List of possible values to be found in the text
  * @throws IllegalArgumentException If the possibilities list is empty
  */
@throws[IllegalArgumentException]
case class OneOf(possibilities: List[String]) extends Specification {
  require(possibilities.nonEmpty)
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
