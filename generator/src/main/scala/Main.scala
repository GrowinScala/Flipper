
object Main {

  import generator.Generator._

  def main(args: Array[String]): Unit = {
    val objMap = Map("name" -> List(1,2,3), "age" -> 25)
    convertMapToPDF(objMap)
  }
}
