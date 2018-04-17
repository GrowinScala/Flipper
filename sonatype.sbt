sonatypeProfileName := "com.growin"

publishMavenStyle := true

licenses := Seq("MIT" -> url("http://www.opensource.org/licenses/mit-license.php"))

homepage := Some(url("https://github.com/GrowinScala/Flipper"))
scmInfo := Some(
  ScmInfo(
    url("https://github.com/GrowinScala/Flipper"),
    "scm:git@github.com:GrowinScala/Flipper.git"
  )
)
developers := List(
  Developer(id="megidio", name="Margarida Reis", email="mreis@growin.pt", url=url("https://github.com/megidio")),
  Developer(id="MrLucasFischer", name="Lucas Fischer", email="lfischer@growin.pt", url=url("https://github.com/MrLucasFischer")),
  Developer(id="valterfernandes", name="Valter Fernandes", email="vfernandes@growin.pt", url=url("https://github.com/valterfernandes"))
)
