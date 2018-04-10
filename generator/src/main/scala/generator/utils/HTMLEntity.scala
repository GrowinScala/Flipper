package generator.utils

/**
  *
  */
trait HTMLEntity

case class H1() extends HTMLEntity

case class H2() extends HTMLEntity

case class H3() extends HTMLEntity

case class OrderedList() extends HTMLEntity

case class UnorderedList() extends HTMLEntity

case class Table() extends HTMLEntity

case class P() extends HTMLEntity

case class Text() extends HTMLEntity
