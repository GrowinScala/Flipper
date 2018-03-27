package parser.extraction;

import java.util.HashMap;

import scala.Option;
import parser.utils.POSTag;
import scala.Some;

import java.io.File;
import java.util.List;
import java.util.Map;

public class ExtractorJava {

    /**
     * Method that given a file path (maybe change to a real file) will load that PDF file and read the text from it
     *
     * @param file - File to be loaded and parsed
     * @return An Option wrapping a String containing all the text found in the document. Returns None in case of Exception
     */
    public Option<String> readPDF(File file) {
        return readPDF(file, true);
    }

    /**
     * Method that given a file path (maybe change to a real file) will load that PDF file and read the text from it
     *
     * @param file - File to be loaded and parsed
     * @return An Option wrapping a String containing all the text found in the document. Returns None in case of Exception
     */
    public Option<String> readPDF(File file, Boolean readImages) {
        return Extractor.readPDF(file, readImages);
    }


    public String readPDFAux(File file, Boolean readImages) {
        Option<String> readResult = Extractor.readPDF(file, readImages);
        if (readResult.isDefined())
            return readResult.get();
        else
            throw new NullPointerException(); //For example
    }


    public List<Map<String, List<String>>> getAllMatchedValues(String text,
                                                               List<Map<String, POSTag>> keywords) {
        return getAllMatchedValues(text, keywords, new HashMap<>());
    }

    public List<Map<String, List<String>>> getAllMatchedValues(String text,
                                                               List<Map<String, POSTag>> keywords,
                                                               Map<String, String> clientRegEx) {

        Option<String> textOpt = text != null ? Some.apply(text) : Option.apply(null);


        return null;
    }
//    public getAllMatchedValues(Option<String> text, List<Tuple2<String, POSTag>> keywords )
}