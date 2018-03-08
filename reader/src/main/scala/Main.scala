object Main {

  import Extractor._

  def main(args: Array[String]): Unit = {
    val filepath: String = "/Users/Lucas Fischer/Documents/Flipper/reader/resources/test.pdf"
    val text = readPDF(filepath)

    //    println(getMatchedValues(text, List("name")))
    val pairs = getMatchedValues(text, List("name"))
    val aux = pairs.map(pair => {
      val setOfValues = pair._2
      //      setOfValues.map(value => value.foreach(c => println(c, c == '\n')))
      (pair._1, setOfValues.map(value => value.filter(_ != '\n')))
    })
//    for (pair <- aux) {
//      pair._2.foreach(println)
//    }
    val aux2 = aux.map(pair => (pair._1, pair._2.map(a => a)))
    //    val x = "siodjaoisd\njdaiosjdas\n"
    //    println(x.filter(_ != '\n'))
  }
}
