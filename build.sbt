name := "Flipper"

val basicSettings = Seq(
  version := "0.1",
  scalaVersion := "2.12.4",
  libraryDependencies += "org.apache.pdfbox" % "pdfbox" % "2.0.7",
  libraryDependencies += "junit" % "junit" % "4.10" % Test,
  libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.0-SNAP10" % Test,
  libraryDependencies += "org.apache.opennlp" % "opennlp-tools" % "1.8.4"
)

lazy val root = project.in(file(".")).aggregate(reader, generator, converter).settings(basicSettings)

lazy val reader = project.in(file("reader")).settings(basicSettings)

lazy val generator = project.in(file("generator")).settings(basicSettings)

lazy val converter = project.in(file("converter")).settings(basicSettings)