package generator;

import java.io.File;
import java.util.List;
import java.util.Map;

public class GeneratorJava {

    /**
     * Method that receives a Map and converts it into a HTML file, and then into a PDF file.
     * This method overload implements the user decision to not send any additional CSS
     *
     * @param jsonMap - The Map to be converted into a PDF document
     * @return a Boolean saying if the conversion from Map to PDF was successful or not
     */
    public Boolean convertObjtoPDF(Map<String, List<String>> jsonMap){
        return Generator.convertObjtoPDF(Generator.javaToScalaMap(jsonMap));
    }

    /**
     * Method that receives a Map and converts it into a HTML file, and then into a PDF file.
     * This method overload implements the user decision to send an additional CSS file to be included in the HTML file
     *
     * @param jsonMap - The Map to be converted into a PDF document
     * @param cssFile - The additional CSS file to be included in the HTML file
     * @return a Boolean saying if the conversion from Map to PDF was successful or not
     */
    public Boolean convertObjtoPDF(Map<String,List<String>> jsonMap, File cssFile) {
        return Generator.convertObjtoPDF(Generator.javaToScalaMap(jsonMap), cssFile);
    }

    /**
     * Method that receives a Map and converts it into a HTML file, and then into a PDF file.
     * This method overload implements the user decision to send an additional String containing the desired CSS to be included
     *
     * @param jsonMap   - The Json to be converted into a PDF document
     * @param cssString - The additional String containing the the CSS to be included in the HTML file
     * @return a Boolean saying if the conversion from Map to PDF was successful or not
     */
    public Boolean convertJSONtoPDF(Map<String,List<String>> jsonMap, String cssString) {
        return Generator.convertObjtoPDF(Generator.javaToScalaMap(jsonMap), cssString);
    }

    /**
     * Method that receives a Map and converts it into a HTML file, and then into a PDF file.
     * This method overload implements the user decision to send an additional config file specifying simple styling details to be implemented
     *
     * @param jsonMap - The Map to be converted into a PDF document
     * @param config  - The config specifying simple styling details to be implemented in the PDF conversion
     * @return a Boolean saying if the conversion from Map to PDF was successful or not
     */
    public Boolean convertObjtoPDF(Map<String,List<String>> jsonMap, Config config) {
        return Generator.convertObjtoPDF(Generator.javaToScalaMap(jsonMap), config);
    }

    /**
     * Method that receives a JSON string to be parsed and converted into a HTML file, and then into a PDF file.
     * This method overload implements the user decision to not send any additional CSS
     *
     * @param json - The Json string to be converted into a PDF document
     * @return a Boolean saying if the conversion from JSON to PDF was successful or not
     */
    public Boolean convertJSONtoPDF(String json) {
        return Generator.convertJSONtoPDF(json);
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
        return Generator.convertJSONtoPDF(json, cssFile);
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
        return Generator.convertJSONtoPDF(json, cssString);
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
        return Generator.convertJSONtoPDF(json, config);
    }


}
