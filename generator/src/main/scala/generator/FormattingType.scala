package generator

/**
  *
  */
trait FormattingType

case class H1() extends FormattingType

case class H2() extends FormattingType

case class H3() extends FormattingType

case class OrderedList() extends FormattingType

case class UnOrderedList() extends FormattingType

case class Table() extends FormattingType

case class P() extends FormattingType

case class Text() extends FormattingType
