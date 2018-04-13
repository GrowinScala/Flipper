package generator.utils

/**
  * Abstract configuration class that supports the possibility of specifying certain styyling details in the generated PDF
  *
  * @param color         - The color of the text to be displayed. Ex:. "red"; "green"; "#000"
  * @param fontSize      - The size of the text to be displayed. Ex:. "10"; "20.2"
  * @param textAlignment - The alignment the displayed text should take. Ex:. "center"; "justify"
  * @param fontFamily    - The desired font to be used in the displayed text. Ex:. "Arial"; "Times New Roman"
  * @param fontWeight    - The desired font weight to be used in the displayed text. Ex:. "bold"
  */
abstract class Configuration(
                              color: String,
                              fontSize: String,
                              textAlignment: String,
                              fontFamily: String,
                              fontWeight: String
                            )

/*
  ---------------------------------- Configuration Subclasses ----------------------------------
*/

case class Config(
                   color: String = "",
                   fontSize: String = "",
                   textAlignment: String = "",
                   fontFamily: String = "",
                   fontWeight: String = ""
                 ) extends Configuration(color, fontSize, textAlignment, fontFamily, fontWeight)

/* The default styling options start here */

case class BigHeader() extends Configuration("", "30", "center", "", "bold")

case class SmallHeader() extends Configuration("", "15", "left", "", "bold")

case class JustifiedText() extends Configuration("", "", "justify", "", "")

case class BoldText() extends Configuration("", "", "", "", "bold")
