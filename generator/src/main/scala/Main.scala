object Main {

  import Generator._

  def main(args: Array[String]): Unit = {
    convertHTMLtoPDF(writeHTML(""" {"name": "Lucas", "age" : "21" }"""))
  }
}
