import java.io.{File, FileInputStream}

import opennlp.tools.langdetect.{LanguageDetectorME, LanguageDetectorModel}
import opennlp.tools.postag.{POSModel, POSTaggerME}
import opennlp.tools.tokenize.WhitespaceTokenizer

/**
  * Singleton Object that implements all the Open NLP functionalities
  */
object OpenNLP {

  /**
    * Method that receives a text and tokenizes it by whitespaces
    * This method also tags every single word of the text with its POS equivalent
    *
    * @param text - Text to tokenize
    * @return a pair of two arrays of string, wheres ._1 equals all the words separated by whitespaces
    *         and ._2 equals all the corresponding POS tag for each word
    */
  def tagText(text: String): (Array[String], Array[String]) = {
    if (text == null) return (Array(), Array())

    //we can support English, Portuguese, Danish, German, Sami
    val filepath = detectLanguage(text) match {
      case "por" => "C:/Users/Lucas Fischer/Documents/Flipper/reader/resources/pt-pos-maxent.bin"
      case _ => "C:/Users/Lucas Fischer/Documents/Flipper/reader/resources/en-pos-maxent.bin"
    }
    //TODO Open NPL changes the POS tags according to the language used (the bin file above)
    val inputStream = new FileInputStream(filepath)
    val posModel = new POSModel(inputStream)
    val wsTokenizer = WhitespaceTokenizer.INSTANCE

    val splittedWords = wsTokenizer.tokenize(text)

    val tagger = new POSTaggerME(posModel) //Instanciate the POSTagger
    val tags = tagger.tag(splittedWords) //Tag all the words in the text

    (splittedWords, tags)
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
    val file = new File("/Users/Lucas Fischer/Documents/Flipper/reader/resources/langdetect-183.bin")
    val trainedModel = new LanguageDetectorModel(file)
    val langDect = new LanguageDetectorME(trainedModel)
    langDect.predictLanguage(text).getLang
  }


  def translatePOSTag(tag: String): String = {
    //TODO Open NLP returns different POS tags acording to the language used in the bin file
    /**
      * Should we try to translate a tag from one language to another ?
      * In some cases this will fail, for example a portuguese conjunção does not exist in english
      * Also there are about 70 POS tags for the english language
      */
    ???
  }
}
