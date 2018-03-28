package generator

/**
  * Config case class implemented for the user to be able to specify certain styling details in the generated PDF
  *
  * @param textColor     - The color of the text to be displayed. Ex:. "red"; "green"; "#000"
  * @param fontSize      - The size of the text to be displayed. Ex:. 10; 20.2
  * @param textAlignment - The alignment the displayed text should take. Ex:. "center"; "justify"
  * @param fontFamily    - The desired font to be used in the displayed text. Ex:. "Arial"; "Times New Roman"
  * @param fontWeight    - The desired font weight to be used in the displayed text. Ex:. "bold"; "italic"
  */
private[generator] case class Config(
                                      var textColor: String = "",
                                      var fontSize: Double = -1.0,
                                      var textAlignment: String = "",
                                      var fontFamily: String = "",
                                      var fontWeight: String = ""
                                    )

//In case we want Java to act like it has default parameter values

//  this.setTextColor(textColor)
//  this.setFontSize(fontSize)
//  this.setTextAlignment(textAlignment)
//  this.setFontFamily(fontFamily)
//  this.setFontWeight(fontWeight)
//
//  def setTextColor(textColor: String): Unit = if (textColor == null) this.textColor = ""
//
//  def setFontSize(fontSize: Double): Unit = if (fontSize == null) this.fontSize = -1.0


