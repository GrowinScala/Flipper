package generator.utils

/**
  *This class is used to specify what should be displayed
  *
  * @param fieldName - The name of the keyword to be displayed
  * @param fieldValue - The actual value of the keyword to be displayed
  * @param fieldType - The type of text the keyword-value pair should represent (header, table...)
  * @param formattingID - the Id of a specific format from a Config class, a CSS file or a CSS String
  */
case class Content(fieldName: String = "", fieldValue: Any, fieldType: FieldType, formattingID: String = "")
//TODO maybe change the order of the params so that fieldName would go last or second to last