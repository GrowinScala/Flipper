package parser.utils

import java.io.{File, FileInputStream}

import opennlp.tools.langdetect.{LanguageDetectorME, LanguageDetectorModel}
import opennlp.tools.postag.{POSModel, POSTaggerME}
import opennlp.tools.tokenize.WhitespaceTokenizer

/**
  * Singleton Object that implements all the Open NLP functionalities
  */
private[parser] object OpenNLP {

  /**
    * Method that receives a text and tokenizes it by whitespaces
    * This method also tags every single word of the text with its POS equivalent
    *
    * @param text - Text to tokenize
    * @return a pair of two arrays of string, wheres ._1 equals all the words separated by whitespaces
    *         and ._2 equals all the corresponding POS tag for each word
    */
  def tagText(text: String): (Array[String], Array[String]) = {
    //we can support English, Portuguese, Danish, German, Swedish
    val filepath = detectLanguage(text) match {
      case "por" => getClass.getResource("/pt-pos-maxent.bin")
      case _ => getClass.getResource("/en-pos-maxent.bin")
    }
    //    val inputStream = new FileInputStream(filepath)
    val posModel = new POSModel(filepath)
    val wsTokenizer = WhitespaceTokenizer.INSTANCE

    val splittedWords = wsTokenizer.tokenize(text)

    val tagger = new POSTaggerME(posModel) //Instanciate the POSTagger
    val tags = tagger.tag(splittedWords) //Tag all the words in the text

    (splittedWords, tags.map(translatePOSTag))
  }

  /**
    * Method that will try to detect the language used in the text
    * using Open NLP Language Detector training it with langdetect-183.bin
    *
    * @param text - The text to find the language for
    * @return - The ISO 639-3 identifier of the language used in the text (eng, por ...)
    *         The full list of ISO identifiers can be found in https://en.wikipedia.org/wiki/List_of_ISO_639-3_codes
    */
  def detectLanguage(text: String): String = {
    val file = getClass.getResource("/langdetect-183.bin")
    //    val file = new File("./reader/resources/langdetect-183.bin")
    val trainedModel = new LanguageDetectorModel(file)
    val langDect = new LanguageDetectorME(trainedModel)
    langDect.predictLanguage(text).getLang
  }

  /**
    * Method that translates the received tag into one of the created standard POS tags
    *
    * @param tag - The POS tag to translate
    * @return a String containing the translated POS tag
    */
  def translatePOSTag(tag: String): String = {
    tag match {
      case "adj" | "JJ" => "ADJ" //Adjective
      case "prop" | "NNP" => "PN" //Proper noun
      case "n" | "NN" => "N" //noun
      case "NNS" => "NPLR" //plural noun
      case "v-inf" | "VB" => "VB" //verb base form
      case "v-ger" | "VBG" => "VBG" //verb gerund
      case "v-pcp" | "VBN" => "VBN" //verb past participle
      case "num" | "CD" => "NUM" //Number
      case "adv" | "RB" => "ADV" //Adverb
      case _ => tag
    }
  }
}
