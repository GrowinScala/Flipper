package generator.utils

/**
  *
  */
trait FieldType

case class Header1() extends FieldType

case class Header2() extends FieldType

case class Header3() extends FieldType

case class Header4() extends FieldType

case class Header5() extends FieldType

case class Header6() extends FieldType

case class OrderedList() extends FieldType

case class UnorderedList() extends FieldType

case class Table() extends FieldType

case class Paragraph() extends FieldType

case class Text() extends FieldType

case class Code() extends FieldType

case class Link(link: String) extends FieldType
