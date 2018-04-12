package generator.generate

import collection.JavaConverters._
import generator.generate.Generator.Keyword
import scalatags.Text.all._
import generator.utils._

/**
  * Singleton object that handles all the HTML manipulation functionalities
  */
private[generate] object HTMLHandler {

  /**
    * Method that upon receiving a String containing an HTML tag converts it to a HTMLTag
    *
    * @param htmlTag - The HTML tag to be converted to HTMLTag
    * @return A HTMLTag object representing the same HTML tag passed as an argument
    */
  def stringToHTMLTag(htmlTag: String): HTMLTag = {
    htmlTag.toLowerCase match {
      case "h1" => H1()
      case "h2" => H2()
      case "h3" => H3()
      case "orderedlist" => OrderedList()
      case "unorderedlist" => UnorderedList()
      case "table" => Table()
      case "p" => P()
      case _ => Text()
    }
  }

  /**
    * Method that calls createHTML, passing to it a Map object with information regarding the content to be displayed
    * in the HTML/PDF file, and a CSS String with CSS code specifying how that content should be displayed
    *
    * @param content   - An object containing all the keywords and their Content(a object containig fieldName, fieldValue and formattingType)
    * @param cssString - A String containing CSS code to be added to the HTML file
    * @return a String containing all the HTML code to be converted into an HTML file
    */
  def writeHTMLString(content: Map[Keyword, Content], cssString: String): String = {
    createHTML(content, cssString)
  }

  /**
    * Auxliary method used to create the CSS string from the users input information
    *
    * @param content    - a Map of keywords and Content objects that describe how that keyword should be represented
    * @param formatting - a Map of String (formattingID) to Config objects that specify how that particular formattingID should be displayed
    * @return the CSS String created from the input information
    */
  def createCssString(content: Map[Keyword, Content], formatting: Map[String, Config]): String = {
    val cssList = for ((_, value) <- content) yield {
      if (value.cssClass.nonEmpty) {
        formatting.get(value.cssClass) match {
          case Some(config) =>
            "." + value.cssClass + "{" +
              " color: " + config.color + ";" +
              " text-align: " + config.textAlignment + ";" +
              " font-weight: " + config.fontWeight + ";" +
              " font-family: " + config.fontFamily + ";" +
              s" font-size: ${if (config.fontSize.isEmpty) -1 else config.fontSize}pt;" +
              "} "
          case None => ""
        }
      } else ""
    }
    cssList.toList.mkString("")
  }

  /**
    * Method used to create the HTML string to be converted into a HTML file and then to a PDF file
    *
    * @param content   - An object containing all the keywords and their Content(a object containig fieldName, fieldValue and formattingType)
    * @param cssString - A String containing the CSS code to be added to the HTML file
    * @return a String containing all the HTML code to be converted into an HTML file
    */
  private def createHTML(content: Map[Keyword, Content], cssString: String): String = {
    //Iterate through all the keywords (and their values) and create the respective HTML tag for each of them
    val htmlBody = content.map { case (_, singleContent) => writeHTMLTag(singleContent) }
    val htmlString = html(head(), body(htmlBody.toList)).toString
    val (left, right) = htmlString.splitAt(12) //split html string at index 12, right in between the <head> tag
    s"$left <style> $cssString </style> $right" //creates the desired HTML code with a <style> tag containing the user-sent css
  }

  /**
    * Auxaliary method used to create the different supported HTML tags
    *
    * @param value - The object containing the information regarding the field name, field value and formatting type
    * @return - A scalatags HTML element wich will be either a "h1", a "ul", a "table", a "p", or a "span" in case there's
    *         no formatting a given value
    */
  private def writeHTMLTag(value: Content): scalatags.Text.TypedTag[String] = {
    value.htmlTag match {
      case _: H1 => h1(`class` := value.cssClass)(displayInfo(value.fieldName, value.fieldValue))

      case _: H2 => h2(`class` := value.cssClass)(displayInfo(value.fieldName, value.fieldValue))

      case _: H3 => h3(`class` := value.cssClass)(displayInfo(value.fieldName, value.fieldValue))

      case _: P => p(`class` := value.cssClass)(displayInfo(value.fieldName, value.fieldValue))

      case _: Text => span(`class` := value.cssClass)(displayInfo(value.fieldName, value.fieldValue))

      case _: OrderedList => createHtmlList(ordered = true, value)

      case _: UnorderedList => createHtmlList(ordered = false, value)

      case _: Table => createHtmlTable(value)
    }
  }

  /**
    * Method that creates a HTML table with the correct content value inside it.
    * The value.fieldValue passed can take any form but to Flipper supports 2 special cases:
    *
    * 1 - value.fieldValue comes in the form of Map[String, List[Any] ], in this case we assume that the key of the map
    * is the table's header and that the values are of the map are the values to be put under said header.
    *
    * 2 - value.fieldValue comes in the form of List[Any], in this case Flipper uses the value.fieldName (if it exists) as
    * the table's header and the content of the list as the values to be put under the header.
    *
    * In all other cases Flipper just encapsulates the value.fieldValue in a HTML table with one row and one data cell
    *
    * @param value - The content value to be placed inside the HTML table
    * @return a scalatags HTMl table containing the value passed as parameter
    */
  private def createHtmlTable(value: Content): scalatags.Text.TypedTag[String] = {
    def getMaxSize(map: Map[String, List[Any]]): Int = map.maxBy(_._2.size)._2.size

    def getTableBody(map: Map[String, List[Any]], counter: Int): List[scalatags.Text.TypedTag[String]] =
      (for (i <- 0 until counter) yield {

        val tdList = map.mapValues(valuesList =>
          //In case there's  no more values inside the current valuesList return an empty table data
          //If not return a table data with the information at the "i" index of the list
          if (i < valuesList.size) td(valuesList(i).toString) else td()
        ).toList.map(_._2)

        tr(tdList) //For each iteration return a table row containing the correct table data inside each of them
      }).toList //return a list of table rows representing the body of the HTML table


    value.fieldValue match {
      case map: Map[String, List[Any]] =>
        val headerRow = tr(th(map.keys.toList)) //create the row of headers
      val tableBody = getTableBody(map, getMaxSize(map)) //create a list of table rows representing the body of HTML table
        table(`class` := value.cssClass)(headerRow, tableBody)


      case javaMap: java.util.Map[String, java.util.List[Object]] =>
        val scalaMap = javaMapToScala(javaMap) //convert to a scala Map
      val headerRow = tr(th(scalaMap.keys.toList)) //create the row of headers
      val tableBody = getTableBody(scalaMap, getMaxSize(scalaMap)) //create a list of table rows representing the body of HTML table
        table(`class` := value.cssClass)(headerRow, tableBody)

      case list: List[Any] =>
        val header = if (value.fieldName.nonEmpty) value.fieldName else "N/A" //TODO maybe change this ?
      val tableData = list.map(elem => tr(td(elem.toString)))
        table(`class` := value.cssClass)(tr(th(header)), tableData)

      case javaList: java.util.List[Object] =>
        val scalaList = javaList.asScala.toList
        val header = if (value.fieldName.nonEmpty) value.fieldName else "N/A" //TODO maybe change this ?
      val tableData = scalaList.map(elem => tr(td(elem.toString)))
        table(`class` := value.cssClass)(tr(th(header)), tableData)

      case _ => table(`class` := value.cssClass)(tr(td(value.fieldValue.toString)))
    }
  }

  /**
    * Method that creates both HTML lists (ol and ul) with the correct informationg inside it
    *
    * @param ordered - Boolean value specifying if the list should be an ordered one (ol) or unordered (ul)
    * @param value   - The content value to be placed inside the HTML list
    * @return a scalatags HTML list tag containing the value passed as parameter
    */
  private def createHtmlList(ordered: Boolean, value: Content): scalatags.Text.TypedTag[String] = {
    value.fieldValue match {
      case javaList: java.util.List[Object] =>
        val listItems = for (i <- 0 until javaList.size()) yield {
          li(javaList.get(i).toString)
        }
        if (ordered) ol(`class` := value.cssClass)(listItems.toList) else ul(`class` := value.cssClass)(listItems.toList)

      case list: List[Any] =>
        val listItems = list.map(elem => li(elem.toString))
        if (ordered) ol(`class` := value.cssClass)(listItems) else ul(`class` := value.cssClass)(listItems)

      case _ => if (ordered) ol(`class` := value.cssClass)(li(value.fieldValue.toString)) else ul(`class` := value.cssClass)(li(value.fieldValue.toString))
    }
  }

  /**
    * Method that displays the information correctly. If fieldName is empty then display just the value, if not
    * then display the information as Key : Value
    *
    * @param fieldName  - The name of the field of a specific information (the key)
    * @param fieldValue - The value of the field (the value)
    * @return a String containing the correctly displayed information
    */
  private def displayInfo(fieldName: String, fieldValue: Any): String =
    if (fieldName.isEmpty) printValue(fieldValue) else fieldName + " : " + printValue(fieldValue) //TODO maybe remove printValue

  /**
    * Method that handles the output of the value passed in the Map[String, Any]
    *
    * @param value the value to be correctly displayed
    * @return a String with the correct representation of the value passed
    */
  private def printValue(value: Any): String = value match {
    case list: List[Any] => list.mkString("[", ",", "]")
    case _ => value.toString
  }

  /**
    * Method that converts a java Map of Strings to java Lists , into a scala Map[String, List[Any] ]
    *
    * @param javaMap - The java map to be converted
    * @return a scala Map[String, List[Any] ] converted from the java map passed as parameter
    */
  private def javaMapToScala(javaMap: java.util.Map[String, java.util.List[Object]]): Map[String, List[Any]] = {
    val mutableMap = javaMap.asScala
    val mutableWithLists = mutableMap.map { case (key, value) => (key, value.asScala.toList) }
    mutableWithLists.toMap
  }

}
