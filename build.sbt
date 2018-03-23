name := "Flipper"

val basicSettings = Seq(
  version := "0.1",
  scalaVersion := "2.12.4",
  libraryDependencies += "org.apache.pdfbox" % "pdfbox" % "2.0.7",
  libraryDependencies += "junit" % "junit" % "4.10" % Test,
  libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.0-SNAP10" % Test,
  libraryDependencies += "org.apache.opennlp" % "opennlp-tools" % "1.8.4",
  libraryDependencies += "net.sourceforge.tess4j" % "tess4j" % "3.4.4",
  libraryDependencies += "com.itextpdf.tool" % "xmlworker" % "5.5.10",
  libraryDependencies += "com.itextpdf" % "itextpdf" % "5.5.10",
  libraryDependencies += "com.lihaoyi" %% "scalatags" % "0.6.7",
  libraryDependencies += "org.json4s" %% "json4s-native" % "3.6.0-M2",
  libraryDependencies += "org.odftoolkit" % "odfdom-java" % "0.8.7",
  libraryDependencies += "org.languagetool" % "language-en" % "4.0",
  libraryDependencies += "org.languagetool" % "language-pt" % "4.0",
  libraryDependencies += "net.htmlparser.jericho" % "jericho-html" % "3.4",
  libraryDependencies += "org.odftoolkit" % "simple-odf" % "0.6.6",
  libraryDependencies += "net.sf.cssbox" % "pdf2dom" % "1.7",
  libraryDependencies += "com.sksamuel.scrimage" %% "scrimage-core" % "3.0.0-alpha4",
  libraryDependencies += "com.sksamuel.scrimage" %% "scrimage-filters" % "3.0.0-alpha4"


//If we want to add back JavaCPP Tesseract presets (same results as Tess4J)
  //We need to add this lines and also add a file "javacpp.sbt" to ./project with addSbtPlugin("org.bytedeco" % "sbt-javacpp" % "1.13") inside it
  //  addSbtPlugin("org.bytedeco" % "sbt-javacpp" % "1.13"),
  //  javaCppPresetLibs ++= Seq("tesseract" -> "3.05.01"),
  //  libraryDependencies += "org.bytedeco.javacpp-presets" % "leptonica" % "1.74.4-1.4",
  //  libraryDependencies += "org.bytedeco.javacpp-presets" % "leptonica-platform" % "1.74.4-1.4"
)


lazy val root = project.in(file(".")).aggregate(reader, generator, converter).settings(basicSettings)

lazy val reader = project.in(file("reader")).settings(basicSettings)

lazy val generator = project.in(file("generator")).settings(basicSettings)

lazy val converter = project.in(file("converter")).dependsOn(reader).settings(basicSettings)