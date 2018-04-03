
object Main {

  import generator.Generator._

  def main(args: Array[String]): Unit = {
    val objMap: Map[String,Any] = Map("name" -> "Margarida", "age" -> 25)
    convertMapToPDF(objMap)
  }
}
