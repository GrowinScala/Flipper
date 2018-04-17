package generator.generate;

import java.io.File;
import java.util.Map;

import generator.utils.*;
import scala.collection.JavaConverters;

public class GeneratorJava {

    /**
     * Method that receives a content map and converts it into a HTML file, and then into a PDF file.
     * This method overload implements the user decision to not send any additional CSS
     *
     * @param contentMap - The content map to be converted into a PDF document
     * @return a Boolean saying if the conversion from Map to PDF was successful or not
     */
    public Boolean convertMapToPDF(Map<String, Content> contentMap) {
        return Generator.convertMapToPDF(contentMapToScala(contentMap));
    }

    /**
     * Method that receives a content map and converts it into a HTML file, and then into a PDF file.
     * This method overload implements the user decision to send an additional CSS file to be included in the HTML file
     *
     * @param contentMap - The content map to be converted into a PDF document
     * @param cssFile    - The additional CSS file to be included in the HTML file
     * @return a Boolean saying if the conversion from Map to PDF was successful or not
     */
    public Boolean convertMapToPDF(Map<String, Content> contentMap, File cssFile) {
        return Generator.convertMapToPDF(contentMapToScala(contentMap), cssFile);
    }

    /**
     * Method that receives a content map and converts it into a HTML file, and then into a PDF file.
     * This method overload implements the user decision to send an additional String containing the desired CSS to be included
     *
     * @param contentMap - The content map to be converted into a PDF document
     * @param cssString  - The additional String containing the the CSS to be included in the HTML file
     * @return a Boolean saying if the conversion from Map to PDF was successful or not
     */
    public Boolean convertMapToPDF(Map<String, Content> contentMap, String cssString) {
        return Generator.convertMapToPDF(contentMapToScala(contentMap), cssString);
    }

    /**
     * Method that receives a content map and converts it into a HTML file, and then into a PDF file.
     * This method overload implements the user decision to send an additional config file specifying simple styling details to be implemented
     *
     * @param contentMap - The content map to be converted into a PDF document
     * @param config     - The config map specifying simple styling details to be implemented in the PDF conversion
     * @return a Boolean saying if the conversion from Map to PDF was successful or not
     */
    public Boolean convertMapToPDF(Map<String, Content> contentMap, Map<String, Configuration> config) {
        return Generator.convertMapToPDF(contentMapToScala(contentMap), configMapToScala(config));
    }

    /**
     * Method that receives a JSON string to be parsed and converted into a HTML file, and then into a PDF file.
     * This method overload implements the user decision to not send any additional CSS
     *
     * @param contentJSON - The Json string to be converted into a PDF document
     * @return a Boolean saying if the conversion from JSON to PDF was successful or not
     */
    public Boolean convertJSONtoPDF(String contentJSON) {
        return Generator.convertJSONtoPDF(contentJSON);
    }

    /**
     * Method that receives a JSON string to be parsed and converted into a HTML file, and then into a PDF file.
     * This method overload implements the user decision to send an additional CSS file to be included in the HTML file
     *
     * @param contentJSON - The Json string to be converted into a PDF document
     * @param cssFile     - The additional CSS file to be included in the HTML file
     * @return a Boolean saying if the conversion from JSON to PDF was successful or not
     */
    public Boolean convertJSONtoPDF(String contentJSON, File cssFile) {
        return Generator.convertJSONtoPDF(contentJSON, cssFile);
    }

    /**
     * Method that receives a JSON string to be parsed and converted into a HTML file, and then into a PDF file.
     * This method overload implements the user decision to send an additional String containing the desired CSS to be included
     *
     * @param contentJSON - The Json string to be converted into a PDF document
     * @param cssString   - The additional String containing the the CSS to be included in the HTML file
     * @return a Boolean saying if the conversion from JSON to PDF was successful or not
     */
    public Boolean convertJSONtoPDF(String contentJSON, String cssString) {
        return Generator.convertJSONtoPDF(contentJSON, cssString);
    }

    /**
     * Method that converts a Java Content Map to a scala immutable map using JavaConverters
     *
     * @param javaMap - Java content map to be converted
     * @return a scala.collection.immutable.Map converted from the input Java Map
     */
    private scala.collection.immutable.Map contentMapToScala(Map<String, Content> javaMap) {
        scala.collection.mutable.Map mutableMap = JavaConverters.mapAsScalaMapConverter(javaMap).asScala();

        //Convert scala mutable map to scala immutable map by concatenating it with an empty immutable HashMap
        return new scala.collection.immutable.HashMap<>().$plus$plus(mutableMap);
    }

    /**
     * Method that converts a Java Config Map to a scala immutable map using JavaConverters
     *
     * @param javaMap - Java config map to be converted
     * @return a scala.collection.immutable.Map converted from the input Java Map
     */
    private scala.collection.immutable.Map configMapToScala(Map<String, Configuration> javaMap) {
        scala.collection.mutable.Map mutableMap = JavaConverters.mapAsScalaMapConverter(javaMap).asScala();

        //Convert scala mutable map to scala immutable map by concatenating it with an empty immutable HashMap
        return new scala.collection.immutable.HashMap<>().$plus$plus(mutableMap);
    }
}
