package generator.generate;

import generator.utils.*;
import junit.framework.TestCase;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class GeneratorJavaSuite extends TestCase {
    protected GeneratorJava gj = new GeneratorJava();
    protected HashMap content = new HashMap();
    protected File cssFile = new File("test.css");
    protected String cssString =
            ".bigHeader{" +
                    "color : blue;" +
                    "font-size : 20pt;" +
                    "text-align : center;" +
                    "font-family : corbel;" +
                    "font-weight : bold;" +
                    "} " +
                    ".small{ color : red; font-size : 10pt;}";

    protected String contentJSON =
            "{ " +
                    "\"name\" : {" +
                    "             \"fieldName\" : \"name\"," +
                    "             \"fieldValue\" : \"something\"," +
                    "             \"fieldType\" : \"Header1\"," +
                    "             \"formattingID\" : \"bigHeader\"" +
                    "           }" +
                    "}";

    protected String configJSON =
            "{ " +
                    "\"asd\" : {" +
                    "             \"textColor\" : \"red\"," +
                    "             \"fontSize\" : \"20\"," +
                    "             \"textAlignment\" : \"center\"," +
                    "             \"fontFamily\" : \"corbel\"," +
                    "             \"fontWeight\" : \"bold\"" +
                    "           }" +
                    "}";

    protected void setUp() {
        ArrayList ages = new ArrayList();
        ages.add(20);
        ages.add(30);
        content.put("name", new Content("name", "John Doe", new Header1(), "bigHeader"));
        content.put("age", new Content("age", ages, new OrderedList(), "small"));
    }

    /**
     * Tests that passing a correct ContentMap and a correct CSS file/CSS String/ConfigMap
     * to convertMapToPDF will return true saying the conversion was successful
     */
    @Test
    public void testCorrectlyCallConvertMapToPDF() {
        String emptyCSS = "";
        String nullCSS = null;
        HashMap<String, Configuration> emptyConfig = new HashMap<>();
        HashMap<String, Configuration> configMap = new HashMap<>();
        configMap.put("bigHeader", new Config("blue", "20", "center", "corbel", "bold"));
        configMap.put("small", new SmallHeader());

        boolean noConfig = gj.convertMapToPDF(content);
        boolean convertWithCssFile = gj.convertMapToPDF(content, cssFile);
        boolean convertWithConfigMap = gj.convertMapToPDF(content, configMap);
        boolean convertWithEmptyConf = gj.convertMapToPDF(content, emptyConfig);
        boolean convertWithEmptyCSS = gj.convertMapToPDF(content, emptyCSS);
        boolean convertWithNullCSS = gj.convertMapToPDF(content, nullCSS);

        assert (noConfig && convertWithCssFile && convertWithConfigMap && convertWithEmptyConf && convertWithEmptyCSS && convertWithNullCSS);
    }

    /**
     * Tests that calling convertMapToPDF with an incorrect ContentMap/CSS File will return false saying the conversion was not successful
     */
    @Test
    public void testIncorrectlyCallconvertMapToPDF() {
        File incorrectFile = new File("non-existing-file");
        File nullFile = null;
        HashMap<String, Content> emptyContent = new HashMap<>();
        assert (!gj.convertMapToPDF(content, incorrectFile) && !gj.convertMapToPDF(emptyContent, cssFile) && !gj.convertMapToPDF(content, nullFile));
    }

    /**
     * Test that calling convertMapToPDF with a null ContentMap will result in a
     * NullPointerException
     */
    @Test(expected = NullPointerException.class)
    public void testCallConvertMapToPDFNull_1() {
        try {
            gj.convertMapToPDF(null, cssFile);
        } catch (Exception e) {
            assert (e instanceof NullPointerException);
        }
    }

    /**
     * Test that calling convertMapToPDF with a null ContentMap will result in a
     * NullPointerException
     */
    @Test(expected = NullPointerException.class)
    public void testCallConvertMapToPDFNull_2() {
        HashMap nullMap = null;
        try {
            gj.convertMapToPDF(content, nullMap);
        } catch (Exception e) {
            assert (e instanceof NullPointerException);
        }
    }

    /**
     * Tests that calling converJSONtoPDF (and convertJSONtoPDFWithCSS) with correct input parameters will return true
     * saying the conversion was successful
     */
    @Test
    public void testCorrectlyCallConvertJSONtoPDF() {
        boolean noConfig = gj.convertJSONtoPDF(contentJSON);
        boolean convertWithConf = gj.convertJSONtoPDF(contentJSON, configJSON);
        boolean convertWithCSSStr = gj.convertJSONtoPDFWithCSS(contentJSON, cssString);
        boolean convertWithEmptyCSS = gj.convertJSONtoPDFWithCSS(contentJSON, "");
        boolean convertWithNullCSS = gj.convertJSONtoPDFWithCSS(contentJSON, null);
        boolean convertWithCSSFile = gj.convertJSONtoPDF(contentJSON, cssFile);

        assert (noConfig && convertWithConf && convertWithCSSStr && convertWithEmptyCSS && convertWithCSSFile && convertWithNullCSS);
    }

    /**
     * Tests that calling convertJSONtoPDF with an invalid fieldType (unsupported FieldType tag)
     * will result in a IllegalArgumentException.
     */
    @Test
    public void testCallingJSONToPDFInvalid_1() {
        String contentJSON =
                "{ \"name\" : {" +
                        "       \"fieldName\" : \"name\"," +
                        "       \"fieldValue\" : \"something\"," +
                        "       \"fieldType\" : \"Header20\"," +
                        "       \"formattingID\" : \"bigHeader\"" +
                        "     }" +
                        "}";
        try {
            gj.convertJSONtoPDF(contentJSON);
        } catch (IllegalArgumentException e) {
            assert (e.getMessage().equals("The value specified for fieldType is not supported"));
        }
    }

    /**
     * Tests that calling convertJSONtoPDF with an invalid fieldType (not passing a link attribute when passing a fieldType object)
     * will result in a IllegalArgumentException.
     */
    @Test
    public void testCallingJSONToPDFInvalid_2() {
        String contentJSON =
                "{ \"name\" : {" +
                        "       \"fieldName\" : \"name\"," +
                        "       \"fieldValue\" : \"something\"," +
                        "       \"fieldType\" : { \"unsupportedAttr\" : \"someValue\" }," +
                        "       \"formattingID\" : \"bigHeader\"" +
                        "     }" +
                        "}";
        try {
            gj.convertJSONtoPDF(contentJSON);
        } catch (IllegalArgumentException e) {
            assert (e.getMessage().equals("You must supply fieldType object with a type attribute"));
        }
    }

    /**
     * Tests that calling converJSONtoPDF with a null contentJSON will result in a NullPointerException
     */
    @Test(expected = NullPointerException.class)
    public void testCallConvertJSONtoPDFNull_1() {
        try {
            gj.convertJSONtoPDF(null);
        } catch (Exception e) {
            assert (e instanceof NullPointerException);
        }
    }

    /**
     * Tests that calling converJSONtoPDF with a null contentJSON will result in a NullPointerException
     */
    @Test(expected = NullPointerException.class)
    public void testCallConvertJSONtoPDFNull_2() {
        String nullConfigStr = null;
        try {
            gj.convertJSONtoPDF(contentJSON, nullConfigStr);
        } catch (Exception e) {
            assert (e instanceof NullPointerException);
        }
    }

    /**
     * Tests that incorrectly calling convertJSONtoPDF (empty content, empty config, wrong css file) will return false
     * saying the conversion was not successful
     */
    @Test
    public void testIncorrectlyCallConvertJSONtoPDF() {
        String nullConfigStr = null;
        File nullFile = null;

        boolean emptyContent = gj.convertJSONtoPDF("", configJSON);
        boolean emptyConfig = gj.convertJSONtoPDF(contentJSON, "");
        boolean invalidFile = gj.convertJSONtoPDF(contentJSON, new File("invalid-file"));
        boolean nullfile = gj.convertJSONtoPDF(contentJSON, nullFile);


        assert (!emptyContent && !emptyConfig && !invalidFile && !nullfile);
    }


}
