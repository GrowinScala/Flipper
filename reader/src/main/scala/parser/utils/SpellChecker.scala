package parser.utils

import org.languagetool.JLanguageTool
import org.languagetool.language.{BritishEnglish, PortugalPortuguese}
import org.languagetool.rules.RuleMatch
import parser.utils.OpenNLP.detectLanguage
import scala.annotation.tailrec
import scala.collection.JavaConverters._

/**
  * Singleton object that handles all the operations regarding spell checking
  */
private[parser] object SpellChecker {


  /**
    * Method that checks the input text for possible errors and corrects them using LanguageTool's spellchecker
    *
    * @param text - The input text to be corrected
    * @return A String containing the corrected text with errors replaced by the spellchecker's suggestions
    */
  def correctText(text: String): String = {

    /**
      * Auxiliary method that iterates through all of the identified errors (rules) and builds a string with the spellchecker's suggestions
      *
      * @param rules        - The java.util.List of rules (errors found) to be iterated
      * @param originalText - The original text with the errors
      * @param lastPos      - The last known index of an identified error. This is used to jump over corrected characters when applying the suggestions
      * @param outputText   -The corrected string with the errors replaced by the spellchecker's suggestions
      * @return A String containing the corrected text with the errors replaced by the spellchecker's suggestions
      */
    @tailrec def iterateRules(rules: List[RuleMatch], originalText: String, lastPos: Int = 0, outputText: String = ""): String = {
      if (rules.isEmpty) //Stop condition
        if (lastPos < originalText.length)
        //If we haven't reached the end of the input text and the spellchecker finds no more errors
          outputText + originalText.substring(lastPos, originalText.length)
        else
          outputText
      else {
        lazy val currentRule = rules.head
        val textSegment =
          if (currentRule.getSuggestedReplacements.isEmpty)
          //In case the spellchecker found an error but has no suggestion for it, leave it be
            originalText.substring(lastPos, currentRule.getToPos)
          else
            originalText.substring(lastPos, currentRule.getFromPos) + currentRule.getSuggestedReplacements.get(0)

        iterateRules(rules.tail, originalText, currentRule.getToPos, outputText + textSegment)
      }
    }

    //Initialize LanguageTool with the correct language
    val languageTool = detectLanguage(text) match {
      case "por" => new JLanguageTool(new PortugalPortuguese())
      case _ => new JLanguageTool(new BritishEnglish())
    }

    val matches = languageTool.check(text).asScala.toList //.check returns a java.util.List, .asScala.toList transforms it to a Scala collection
    iterateRules(matches, text)
  }
}
