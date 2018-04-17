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
        keywords.put("phone", SpecificationJava.postTag(POSTagEnum.NUMBER, true));

        mapNull = null;
        mapEmpty = new HashMap<>();

        file = new File("./reader/src/main/resources/test.pdf");
        strValid = ej.readPDF(file);
        strColor = "In this example text we will want to find a specific color." +
                   " Here are some of the options: blue , red, green, orange." +
                   " These are just some examples.";
    }

    public void testReadPDFNullFile(){
        try {
            ej.readPDF(null);
        } catch (Exception e) {
            assert (e instanceof NullPointerException);
        }
    }

    public void testReadPDFInvalidFile() {
        try{
            ej.readPDF(new File(strEmpty));
        } catch (Exception e) {
           assert (e instanceof NullPointerException);
        }
    }

    public void testGetAllMatchedValues(){
        Map<String,List<String>> matchedValues = ej.getAllMatchedValues(strValid,keywords);
        String expValue = "Margarida Reis";
        if(matchedValues.isEmpty()) fail("getAllMatchedValues returned an empty List");
        else assert (matchedValues.get("name").get(0).equals(expValue));
    }

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

    public void testGetAllMatchedValuesOneEmpty(){
        ArrayList emptyPos = new ArrayList();

    }

    public void testGetAllMatchedValuesInvalidText(){
        Map nullRes = ej.getAllMatchedValues(strNull, keywords);
        Map emptyRes = ej.getAllMatchedValues(strEmpty,keywords);
        assert (nullRes.isEmpty() && emptyRes.isEmpty());
    }

    public void testGetAllMatchedValuesInvalidKey(){

    }

    public void testGetAllMatchedValuesNullMap(){
        try {
            ej.getAllMatchedValues(strValid, mapNull);
        } catch (Exception e) {
            assert(e instanceof NullPointerException);
        }
    }

    public void testGetAllMatchedValuesEmptyMap(){
        try {
            ej.getAllMatchedValues(strValid, mapEmpty);
        } catch (Exception e) {
            assert(e instanceof IllegalArgumentException);
        }
    }

    public void testGetSingleMatchedValueInvalidText(){
        Map nullRes = ej.getSingleMatchedValue(strNull,keywords);
        Map emptyRes = ej.getSingleMatchedValue(strEmpty,keywords);
        assert (nullRes.isEmpty() && emptyRes.isEmpty());
    }

    public void testGetSingleMatchedValueNullMap(){
        try {
            ej.getSingleMatchedValue(strValid, mapNull);
        } catch (Exception e){
            assert (e instanceof NullPointerException);
        }
    }

    public void testGetsingleMatchedValueEmptyMap(){
        try{
            ej.getSingleMatchedValue(strValid,mapEmpty);
        } catch (Exception e) {
            assert (e instanceof IllegalArgumentException);
        }
    }

    public void testGetAllObjectsInvalidText(){
        Map nullRes = ej.getAllMatchedValues(strNull,keywords);
        Map emptyRes = ej.getAllMatchedValues(strEmpty,keywords);
        assert (nullRes.isEmpty() && emptyRes.isEmpty());
    }

    public void testGetAllObjectsNullMap(){
        try{
            ej.getAllObjects(strValid,mapNull);
        } catch (Exception e) {
            assert (e instanceof NullPointerException);
        }
    }

    public void testGetAllObjectsEmptyMap(){
        try{
            ej.getAllObjects(strValid,mapEmpty);
        } catch (Exception e) {
            assert (e instanceof IllegalArgumentException);
        }
    }

    public void testGetJsonObjectsInvalidText(){
        List nullRes = ej.getJSONObjects(strNull,keywords);
        List emptyRes = ej.getJSONObjects(strEmpty,keywords);
        assert (nullRes.isEmpty() && emptyRes.isEmpty());
    }

    public void testGetJsonObjectsNullMap(){
        try{
            ej.getJSONObjects(strValid,mapNull);
        } catch (Exception e){
            assert (e instanceof NullPointerException);
        }
    }

    public void testGetJsonObjectsEmptyMap(){
        try{
            ej.getJSONObjects(strValid,mapEmpty);
        } catch (Exception e) {
            assert (e instanceof IllegalArgumentException);
        }
    }

    public void testGetSingleJsonInvalidText(){
        Boolean nullRes = ej.getSingleJSON(strNull,keywords).equals(emptyJson);
        Boolean emptyRes = ej.getSingleJSON(strEmpty,keywords).equals(emptyJson);
        assert (nullRes && emptyRes);
    }

    public void testGetSingleJsonNullMap(){
        try{
            ej.getSingleJSON(strValid,mapNull);
        } catch (Exception e){
            assert (e instanceof NullPointerException);
        }
    }

    public void testGetSingleJsonEmptyMap(){
        try{
            ej.getSingleJSON(strValid,mapEmpty);
        } catch (Exception e) {
            assert (e instanceof IllegalArgumentException);
        }
    }

    public void testMakeJsonStringNullMap(){
        try {
            ej.makeJSONString(mapNull);
        } catch (Exception e){
            assert (e instanceof NullPointerException);
        }
    }

    public void testMakeJsonStringEmptyMap(){
        try{
            ej.makeJSONString(mapEmpty);
        } catch (Exception e){
            assert (e instanceof IllegalArgumentException);
        }
    }

    public void testGetJsonFromForm(){
        Boolean emptyRes = ej.getJSONFromForm(strEmpty).equals(emptyJson);
        Boolean nullRes = ej.getJSONFromForm(strNull).equals(emptyJson);
        assert (emptyRes && nullRes);
    }
}
