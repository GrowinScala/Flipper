package generator.utils

/**
  *
  */
trait HTMLTag

case class H1() extends HTMLTag

case class H2() extends HTMLTag

case class H3() extends HTMLTag

case class OrderedList() extends HTMLTag

case class UnorderedList() extends HTMLTag

case class Table() extends HTMLTag

case class P() extends HTMLTag

case class Text() extends HTMLTag
