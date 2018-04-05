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
case class MultipleOf(possibilities: List[String], isMultiple: Boolean = false) extends Specification {
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
case class OneOf(possibilities: List[String], isMultiple: Boolean = false) extends Specification {
  require(possibilities.nonEmpty)
}

/**
  * Enumeration for the standard POS tags which translate to both Portuguese and English
  */
abstract class POSTag(val isMultiple: Boolean, val value: String) extends Specification

/*
   POSTag Subtypes, represents the different POSTag possibilities
 */

case class Adjective(override val isMultiple: Boolean = false) extends POSTag(isMultiple, "ADJ")

case class ProperNoun(override val isMultiple: Boolean = false) extends POSTag(isMultiple, "PN")

case class Noun(override val isMultiple: Boolean = false) extends POSTag(isMultiple,"N")

case class PluralNoun(override val isMultiple: Boolean = false) extends POSTag(isMultiple,"NPLR")

case class Verb(override val isMultiple: Boolean = false) extends POSTag(isMultiple,"VB")

case class VerbPastParticiple(override val isMultiple: Boolean = false) extends POSTag(isMultiple,"VBN")

case class VerbGerund(override val isMultiple: Boolean = false) extends POSTag(isMultiple,"VBG")

case class Number(override val isMultiple: Boolean = false) extends POSTag(isMultiple,"NUM")

case class Adverb(override val isMultiple: Boolean = false) extends POSTag(isMultiple,"ADV")

//TODO Support more POS tags
