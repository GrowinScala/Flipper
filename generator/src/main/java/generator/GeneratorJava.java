package generator;

import java.io.File;
import java.util.Map;

public class GeneratorJava {

    /**
     * Method that receives a JSON string to be parsed and converted into a HTML file, and then into a PDF file.
     * This method overload implements the user decision to not send any additional CSS
     *
     * @param json - The Json string to be converted into a PDF document
     * @return a Boolean saying if the conversion from JSON to PDF was successful or not
     */
    public Boolean convertJSONtoPDF(Map<String,String> json) {
//        return Generator.convertJSONtoPDF(json);
        return true;
    }

    /**
     * Method that receives a JSON string to be parsed and converted into a HTML file, and then into a PDF file.
     * This method overload implements the user decision to send an additional CSS file to be included in the HTML file
     *
     * @param json    - The Json string to be converted into a PDF document
     * @param cssFile - The additional CSS file to be included in the HTML file
     * @return a Boolean saying if the conversion from JSON to PDF was successful or not
     */
    public Boolean convertJSONtoPDF(String json, File cssFile) {
//        return Generator.convertJSONtoPDF(json, cssFile);
        return true;
    }

    /**
     * Method that receives a JSON string to be parsed and converted into a HTML file, and then into a PDF file.
     * This method overload implements the user decision to send an additional String containing the desired CSS to be included
     *
     * @param json      - The Json string to be converted into a PDF document
     * @param cssString - The additional String containing the the CSS to be included in the HTML file
     * @return a Boolean saying if the conversion from JSON to PDF was successful or not
     */
    public Boolean convertJSONtoPDF(String json, String cssString) {
//        return Generator.convertJSONtoPDF(json, cssString);
        return true;
    }

    /**
     * Method that receives a JSON string to be parsed and converted into a HTML file, and then into a PDF file.
     * This method overload implements the user decision to send an additional config file specifying simple styling details to be implemented
     *
     * @param json   - The Json string to be converted into a PDF document
     * @param config - The config specifying simple styling details to be implemented in the PDF conversion
     * @return a Boolean saying if the conversion from JSON to PDF was successful or not
     */
    public Boolean convertJSONtoPDF(String json, Config config) {
//        return Generator.convertJSONtoPDF(json, config);
        return true;
    }


}
