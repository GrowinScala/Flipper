import generator._

object Main {

  import generator.Generator._

  def main(args: Array[String]): Unit = {
    val content = Map("name" -> Content("name", "joni", "bigHeader"), "age" -> Content("age", List(20, 30, 40, 50, 60, 70), "small"))
    val formatting =
      Map("bigHeader" -> Config(H1(), "blue", 20, "center", "corbel", "bold"),
        "small" -> Config(OrderedList(), "red", 10))

    println(convertMapToPDF(content, formatting))
  }
}
