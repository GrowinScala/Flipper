//package generator;
//
//import scala.collection.JavaConverters;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class GeneratorJava {
//
//    /**
//     * Method that receives a Map and converts it into a HTML file, and then into a PDF file.
//     * This method overload implements the user decision to not send any additional CSS
//     *
//     * @param jsonMap - The Map to be converted into a PDF document
//     * @return a Boolean saying if the conversion from Map to PDF was successful or not
//     */
//    public Boolean convertMapToPDF(Map<String, List<String>> jsonMap) {
//        return Generator.convertMapToPDF(javaMapToScala(jsonMap));
//    }
//
//    /**
//     * Method that receives a Map and converts it into a HTML file, and then into a PDF file.
//     * This method overload implements the user decision to send an additional CSS file to be included in the HTML file
//     *
//     * @param jsonMap - The Map to be converted into a PDF document
//     * @param cssFile - The additional CSS file to be included in the HTML file
//     * @return a Boolean saying if the conversion from Map to PDF was successful or not
//     */
//    public Boolean convertMapToPDF(Map<String, List<String>> jsonMap, File cssFile) {
//        return Generator.convertMapToPDF(javaMapToScala(jsonMap), cssFile);
//    }
//
//    /**
//     * Method that receives a Map and converts it into a HTML file, and then into a PDF file.
//     * This method overload implements the user decision to send an additional String containing the desired CSS to be included
//     *
//     * @param jsonMap   - The Json to be converted into a PDF document
//     * @param cssString - The additional String containing the the CSS to be included in the HTML file
//     * @return a Boolean saying if the conversion from Map to PDF was successful or not
//     */
//    public Boolean convertMapToPDF(Map<String, List<String>> jsonMap, String cssString) {
//        return Generator.convertMapToPDF(javaMapToScala(jsonMap), cssString);
//    }
//
//    /**
//     * Method that receives a Map and converts it into a HTML file, and then into a PDF file.
//     * This method overload implements the user decision to send an additional config file specifying simple styling details to be implemented
//     *
//     * @param jsonMap - The Map to be converted into a PDF document
//     * @param config  - The config specifying simple styling details to be implemented in the PDF conversion
//     * @return a Boolean saying if the conversion from Map to PDF was successful or not
//     */
//    public Boolean convertMapToPDF(Map<String, List<String>> jsonMap, Config config) {
//        return Generator.convertMapToPDF(javaMapToScala(jsonMap), config);
//    }
//
//    /**
//     * Method that receives a JSON string to be parsed and converted into a HTML file, and then into a PDF file.
//     * This method overload implements the user decision to not send any additional CSS
//     *
//     * @param json - The Json string to be converted into a PDF document
//     * @return a Boolean saying if the conversion from JSON to PDF was successful or not
//     */
//    public Boolean convertJSONtoPDF(String json) {
//        return Generator.convertJSONtoPDF(json);
//    }
//
//    /**
//     * Method that receives a JSON string to be parsed and converted into a HTML file, and then into a PDF file.
//     * This method overload implements the user decision to send an additional CSS file to be included in the HTML file
//     *
//     * @param json    - The Json string to be converted into a PDF document
//     * @param cssFile - The additional CSS file to be included in the HTML file
//     * @return a Boolean saying if the conversion from JSON to PDF was successful or not
//     */
//    public Boolean convertJSONtoPDF(String json, File cssFile) {
//        return Generator.convertJSONtoPDF(json, cssFile);
//    }
//
//    /**
//     * Method that receives a JSON string to be parsed and converted into a HTML file, and then into a PDF file.
//     * This method overload implements the user decision to send an additional String containing the desired CSS to be included
//     *
//     * @param json      - The Json string to be converted into a PDF document
//     * @param cssString - The additional String containing the the CSS to be included in the HTML file
//     * @return a Boolean saying if the conversion from JSON to PDF was successful or not
//     */
//    public Boolean convertJSONtoPDF(String json, String cssString) {
//        return Generator.convertJSONtoPDF(json, cssString);
//    }
//
//    /**
//     * Method that receives a JSON string to be parsed and converted into a HTML file, and then into a PDF file.
//     * This method overload implements the user decision to send an additional config file specifying simple styling details to be implemented
//     *
//     * @param json   - The Json string to be converted into a PDF document
//     * @param config - The config specifying simple styling details to be implemented in the PDF conversion
//     * @return a Boolean saying if the conversion from JSON to PDF was successful or not
//     */
//    public Boolean convertJSONtoPDF(String json, Config config) {
//        return Generator.convertJSONtoPDF(json, config);
//    }
//
//    /**
//     * Method that converts a Java Map to a scala immutable map using JavaConverters
//     *
//     * @param javaMap - Java method to be converted
//     * @return a scala.collection.immutable.Map converted from the input Java Map
//     */
//    private scala.collection.immutable.Map javaMapToScala(Map<String, List<String>> javaMap) {
//        HashMap<String, scala.collection.immutable.List<String>> preScalaMap = new HashMap();
//        for (Map.Entry<String, List<String>> entry : javaMap.entrySet()) {
//
//            //Convert Java List<String> to a scala mutable Buffer
//            scala.collection.mutable.Buffer<String> scalaBuffer = JavaConverters.asScalaBuffer(entry.getValue());
//
//            //Convert scala mutable Buffer to a scala immutable List
//            scala.collection.immutable.List<String> scalaList = scalaBuffer.toList();
//            preScalaMap.put(entry.getKey(), scalaList);
//        }
//        scala.collection.mutable.Map mutableMap = JavaConverters.mapAsScalaMapConverter(preScalaMap).asScala();
//
//        //Convert scala mutable map to scala immutable map by concatenating it with an empty immutable HashMap
//        return new scala.collection.immutable.HashMap<>().$plus$plus(mutableMap);
//    }
//
//
//}
