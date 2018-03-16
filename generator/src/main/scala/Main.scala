object Main {

  import Generator._

  def main(args: Array[String]): Unit = {
    convertJSONtoPDF(""" {"name": "Lucas", "age" : 21 }""")
  }
}
