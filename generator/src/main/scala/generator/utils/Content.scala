package generator.utils

case class Content(fieldName: String = "", fieldValue: Any, htmlEntity: HTMLEntity, cssClass: String = "")
//TODO maybe change the order of the params so that fieldName would go last or second to last