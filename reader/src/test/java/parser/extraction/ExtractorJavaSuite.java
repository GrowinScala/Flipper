package parser.extraction;

import junit.framework.*;
import parser.utils.POSTagEnum;
import parser.utils.SpecificationJava;
import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ExtractorJavaSuite extends TestCase {
    protected ExtractorJava ej = new ExtractorJava();

    protected String strValid, strColor, strNull, strEmpty, emptyJson;
    protected HashMap keywords, mapNull, mapEmpty;
    protected File file;

    // assigning the values
    protected void setUp(){
        strNull = null;
        strEmpty = "";
        emptyJson = "{}";

        keywords = new HashMap<>();
        keywords.put("name", SpecificationJava.postTag(POSTagEnum.PROPERNOUN));
        keywords.put("age", SpecificationJava.postTag(POSTagEnum.NUMBER));

        mapNull = null;
        mapEmpty = new HashMap<>();

        file = new File("./reader/src/main/resources/test.pdf");
        strValid = ej.readPDF(file);
        strColor = "In this example text we will want to find a specific color." +
                   " Here are some of the options: blue , red, green, orange." +
                   " These are just some examples.";
    }

    /**
     * Tests that calling readPDF with null returns NullPointerException
     */
    public void testReadPDFNullFile(){
        try {
            ej.readPDF(null);
        } catch (Exception e) {
            assert (e instanceof NullPointerException);
        }
    }

    /**
     * Test that calling readPDF with invalid file returns NullPointerException
     */
    public void testReadPDFInvalidFile() {
        try{
            ej.readPDF(new File(strEmpty));
        } catch (Exception e) {
           assert (e instanceof NullPointerException);
        }
    }

    /**
     * Tests if the result of calling getAllMatchedValues is correct or not
     */
    public void testGetAllMatchedValues(){
        Map<String,List<String>> matchedValues = ej.getAllMatchedValues(strValid,keywords);
        String expValue = "Margarida Reis";
        if(matchedValues.isEmpty()) fail("getAllMatchedValues returned an empty List");
        else assert (matchedValues.get("name").get(0).equals(expValue));
    }

    /**
     * Tests that passing a list of possible values to getAllMatchedValues will return one of the possibilities passed (assuming it exists in the text)
     */
    public void testGetAllMatchedValuesOneOf(){
        ArrayList oneList = new ArrayList();
        oneList.add("blue");
        oneList.add("red");
        HashMap colorMap = new HashMap<>();
        colorMap.put("color", SpecificationJava.oneOf(oneList));
        String expValue = "blue";
        Map<String, List<String>> matchedValues = ej.getAllMatchedValues(strColor, colorMap);
        assert(matchedValues.get("color").get(0).equals(expValue));
    }

    /**
     * Tests that passing a list of possible values to getAllMatchedValues will return multiple of the possibilities passed (assuming it exists in the text)
     */
    public void testGetAllMatchedValuesMultiOf(){
        ArrayList multiList = new ArrayList();
        multiList.add("blue");
        multiList.add("red");
        HashMap colorMap = new HashMap<>();
        colorMap.put("color",SpecificationJava.multipleOf(multiList));
        ArrayList<String> exptValue = new ArrayList<>();
        exptValue.add("blue");
        exptValue.add("red");
        Map<String, List<String>> matchedValues = ej.getAllMatchedValues(strColor,colorMap);
        assert(matchedValues.get("color").equals(exptValue));
    }

    /**
     * Tests that passing a list of possible values that don't exist in the text will return an empty list in both OneOf and MultipleOf
     */
    public void testGetAllMatchedValuesNoOneMulti(){
        ArrayList poss = new ArrayList();
        poss.add("does no exist");
        HashMap colorMapOne = new HashMap<>();
        HashMap colorMapMul = new HashMap<>();
        colorMapOne.put("color",SpecificationJava.multipleOf(poss));
        colorMapMul.put("color",SpecificationJava.oneOf(poss));
        Map<String,List<String>> mVOne = ej.getAllMatchedValues(strColor,colorMapOne);
        Map<String,List<String>> mVMul = ej.getAllMatchedValues(strColor,colorMapMul);
        assert (mVMul.get("color").isEmpty() && mVOne.get("color").isEmpty());
    }

    /**
     * Tests that passing a OneOf object with an empty possibilities list will result in an IllegalArgumentException
     */
    public void testGetAllMatchedValuesOneEmpty(){
        ArrayList emptyPos = new ArrayList();
        HashMap colorMap = new HashMap<>();
        try{
            colorMap.put("color",SpecificationJava.oneOf(emptyPos));
            ej.getAllMatchedValues(strColor,colorMap);
        } catch (Exception e) {
            assert (e instanceof IllegalArgumentException);
        }
    }

    /**
     * Tests that passing a MultipleOf object with an empty possibilities list will result in an IllegalArgumentException
     */
    public void testGetAllMatchedValuesMultiEmpty(){
        ArrayList emptyPos = new ArrayList();
        HashMap colorMap = new HashMap<>();
        try{
            colorMap.put("color",SpecificationJava.multipleOf(emptyPos));
            ej.getAllMatchedValues(strColor,colorMap);
        } catch (Exception e) {
            assert (e instanceof IllegalArgumentException);
        }
    }

    /**
     * Tests that passing a null or empty text will return an empty list
     */
    public void testGetAllMatchedValuesInvalidText(){
        Map nullRes = ej.getAllMatchedValues(strNull, keywords);
        Map emptyRes = ej.getAllMatchedValues(strEmpty,keywords);
        assert (nullRes.isEmpty() && emptyRes.isEmpty());
    }

    /**
     * Tests if the result of calling getAllMatchedValues with a keyword that does not exist in the text is empty
     */
    public void testGetAllMatchedValuesInvalidKey(){
        HashMap mapInv = new HashMap<>();
        mapInv.put("color",SpecificationJava.postTag(POSTagEnum.NOUN));
        Map<String,List<String>> matchedValues = ej.getAllMatchedValues(strValid,mapInv);
        if(matchedValues.isEmpty()) fail("Matched value came back as an Empty List");
        else assert (matchedValues.get("color").isEmpty());
    }

    /**
     * Tests that passing a null keywords map will result in a NullPointerException
     */
    public void testGetAllMatchedValuesNullMap(){
        try {
            ej.getAllMatchedValues(strValid, mapNull);
        } catch (Exception e) {
            assert(e instanceof NullPointerException);
        }
    }

    /**
     * Tests that passing an empty keywords map will result in a IllegalArgumentException
     */
    public void testGetAllMatchedValuesEmptyMap(){
        try {
            ej.getAllMatchedValues(strValid, mapEmpty);
        } catch (Exception e) {
            assert(e instanceof IllegalArgumentException);
        }
    }

    /**
     * Tests if getSingleMatchedValue returns only one value for all keys
     */
    public void testGetSingleMatchedValue(){
        Boolean assertRes = true;
        Map<String,List<String>> matchedValue = ej.getSingleMatchedValue(strValid,keywords);
        for (Map.Entry<String,List<String>> entry : matchedValue.entrySet()){
            if(entry.getValue().size() != 1) assertRes = false;
        }
        assert (assertRes);
    }

    /**
     * Tests that passing an empty or null text string to getSingleMatchedValue will return an empty list
     */
    public void testGetSingleMatchedValueInvalidText(){
        Map nullRes = ej.getSingleMatchedValue(strNull,keywords);
        Map emptyRes = ej.getSingleMatchedValue(strEmpty,keywords);
        assert (nullRes.isEmpty() && emptyRes.isEmpty());
    }

    /**
     * Tests that passing a null keywords map to getSingleMatchedValue will result in a NullPointerException
     */
    public void testGetSingleMatchedValueNullMap(){
        try {
            ej.getSingleMatchedValue(strValid, mapNull);
        } catch (Exception e){
            assert (e instanceof NullPointerException);
        }
    }

    /**
     * Tests that passing an empty keywords map to getSingleMatchedValue will result in a IlllegalArgumentException
     */
    public void testGetSingleMatchedValueEmptyMap(){
        try{
            ej.getSingleMatchedValue(strValid,mapEmpty);
        } catch (Exception e) {
            assert (e instanceof IllegalArgumentException);
        }
    }

    /**
     * Tests if getSingleMatchedValue returns an empty value list if the given keyword does not exist in the text
     */
    public void testGetSingleMatchedValueInvalidKey(){
        HashMap mapInv = new HashMap<>();
        mapInv.put("color",SpecificationJava.postTag(POSTagEnum.NOUN));
        Map<String,List<String>> matchedValue = ej.getSingleMatchedValue(strValid,mapInv);
        if(matchedValue.isEmpty()) fail("Matched value came back as an Empty List");
        else assert (matchedValue.get("color").isEmpty());
    }

    /**
     * Tests that passing an empty or null text to getAllObjects will return an empty list
     */
    public void testGetAllObjectsInvalidText(){
        Map nullRes = ej.getAllMatchedValues(strNull,keywords);
        Map emptyRes = ej.getAllMatchedValues(strEmpty,keywords);
        assert (nullRes.isEmpty() && emptyRes.isEmpty());
    }

    /**
     * Tests that passing a null map to getAllObjects will result in a NullPointerException
     */
    public void testGetAllObjectsNullMap(){
        try{
            ej.getAllObjects(strValid,mapNull);
        } catch (Exception e) {
            assert (e instanceof NullPointerException);
        }
    }

    /**
     * Tests that passing an empty map to getAllObjects will result in a IllegalArgumentException
     */
    public void testGetAllObjectsEmptyMap(){
        try{
            ej.getAllObjects(strValid,mapEmpty);
        } catch (Exception e) {
            assert (e instanceof IllegalArgumentException);
        }
    }

    /**
     * Tests if getAllObjects returns a list with the correct information
     */
    public void testGetAllObjects(){
        HashMap validKey = new HashMap<>();
        validKey.put("name", SpecificationJava.postTag(POSTagEnum.PROPERNOUN));
        List<Map<String,List<String>>> expected = new ArrayList<>();
        List<String> auxList1 = new ArrayList<>();
        auxList1.add("Margarida Reis");
        List<String> auxList2 = new ArrayList<>();
        auxList2.add("Lucas");
        List<String> auxList3 = new ArrayList<>();
        auxList3.add("Albertina");
        List<String> auxList4 = new ArrayList<>();
        auxList4.add("Justino Alberto");
        HashMap auxMap1 = new HashMap<>();
        auxMap1.put("name", auxList1);
        HashMap auxMap2 = new HashMap<>();
        auxMap2.put("name", auxList2);
        HashMap auxMap3 = new HashMap<>();
        auxMap3.put("name", auxList3);
        HashMap auxMap4 = new HashMap<>();
        auxMap4.put("name", auxList4);
        expected.add(auxMap1);
        expected.add(auxMap2);
        expected.add(auxMap3);
        expected.add(auxMap4);
        List res = ej.getAllObjects(strValid,validKey);
        if(res.isEmpty()) fail("getAllObjects returned an enpty List");
        else assert (res.equals(expected));
    }

    /**
     * Tests that calling getJsonObjects with null or empty String returns empty Lists
     */
    public void testGetJsonObjectsInvalidText(){
        List nullRes = ej.getJSONObjects(strNull,keywords);
        List emptyRes = ej.getJSONObjects(strEmpty,keywords);
        assert (nullRes.isEmpty() && emptyRes.isEmpty());
    }

    /**
     * Tests that calling getJsonObjects with null map returns NullPointerException
     */
    public void testGetJsonObjectsNullMap(){
        try{
            ej.getJSONObjects(strValid,mapNull);
        } catch (Exception e){
            assert (e instanceof NullPointerException);
        }
    }

    /**
     * Tests that calling getJasonObjects with empty map returns IllegalArgumentsException
     */
    public void testGetJsonObjectsEmptyMap(){
        try{
            ej.getJSONObjects(strValid,mapEmpty);
        } catch (Exception e) {
            assert (e instanceof IllegalArgumentException);
        }
    }

    /**
     * Tests if the matched values with the given keywords generates the correct JSON strings for all the expected objects
     */
    public void testGetAllJsonObjects(){
        List expected = new ArrayList<>();
        expected.add("{\"name\" : \"Margarida Reis\", \"age\" : 25}");
        expected.add("{\"name\" : \"Lucas\", \"age\" : 21}");
        expected.add("{\"name\" : \"Albertina\", \"age\" : \"\"}");
        expected.add("{\"name\" : \"Justino Alberto\", \"age\" : \"\"}");
        List<String> objRes = ej.getJSONObjects(strValid,keywords);
        assert (objRes.equals(expected));
    }

    /**
     * Tests that calling getSingleJson with null or empty string returns "{}"
     */
    public void testGetSingleJsonInvalidText(){
        Boolean nullRes = ej.getSingleJSON(strNull,keywords).equals(emptyJson);
        Boolean emptyRes = ej.getSingleJSON(strEmpty,keywords).equals(emptyJson);
        assert (nullRes && emptyRes);
    }

    /**
     * Tests that calling getsingleJson with null map returns NullPointerException
     */
    public void testGetSingleJsonNullMap(){
        try{
            ej.getSingleJSON(strValid,mapNull);
        } catch (Exception e){
            assert (e instanceof NullPointerException);
        }
    }

    /**
     * Tests that calling makeJsonString with empty map returns IllegalArgumentException
     */
    public void testGetSingleJsonEmptyMap(){
        try{
            ej.getSingleJSON(strValid,mapEmpty);
        } catch (Exception e) {
            assert (e instanceof IllegalArgumentException);
        }
    }

    /**
     * Tests that calling makeJsonString with null map returns NullPointerException
     */
    public void testMakeJsonStringNullMap(){
        try {
            ej.makeJSONString(mapNull);
        } catch (Exception e){
            assert (e instanceof NullPointerException);
        }
    }

    /**
     * Tests that making a JSON String with an empty map will return an empty JSON object ("{}")
     */
    public void testMakeJsonStringEmptyMap(){
        String res = ej.makeJSONString(mapEmpty);
        assert (res.equals(emptyJson));
    }

    /**
     * Tests that calling getJsonFromForm with empty or null String returns "{}"
     */
    public void testGetJsonFromForm(){
        Boolean emptyRes = ej.getJSONFromForm(strEmpty).equals(emptyJson);
        Boolean nullRes = ej.getJSONFromForm(strNull).equals(emptyJson);
        assert (emptyRes && nullRes);
    }
}
