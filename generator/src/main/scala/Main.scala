import java.io.{File, PrintWriter}

object Main {

  import scalatags.Text.all._
  import Generator._

  def main(args: Array[String]): Unit = {
    val text = html(
      head(),
      body(
        p(fontSize := "50px")("this is a really long paragraph so i'm going to tell you the story of my life. You ready? It started, then I became an Engineer and died inside .")
      )
    )
    val pw = new PrintWriter(new File("index.html"))
    pw.write(text.toString)
    pw.close()
    convertHTMLtoPDF("index.html")
  }
}
