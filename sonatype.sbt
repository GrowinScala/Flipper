sonatypeProfileName := "com.growin"

// To sync with Maven central, you need to supply the following information:
  publishMavenStyle := true

// License of your choice
licenses := Seq("APL2" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt"))

homepage := Some(url("https://github.com/GrowinScala/Flipper"))
scmInfo := Some(
  ScmInfo(
    url("https://github.com/GrowinScala/Flipper"),
    "scm:mreis@growin.pt:GrowinScala/Flipper.git"
  )
)
developers := List(
  Developer(id="megidio", name="Margarida Reis", email="mreis@growin.pt", url=url("https://github.com/megidio")),
  Developer(id="MrLucasFischer", name="Lucas Fischer", email="lfischer@growin.pt", url=url("https://github.com/MrLucasFischer"))
)
