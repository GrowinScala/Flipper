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

    protected void setUp() {
        ArrayList ages = new ArrayList();
        ages.add(20);
        ages.add(30);
        content.put("name", new Content("name", "John Doe", new Header1(), "bigHeader"));
        content.put("age", new Content("age", ages, new OrderedList(), "small"));
    }

    @Test
    public void testCorrectlyCallConvertMapToPDF() {
        String emptyCSS = "";
        HashMap<String, Configuration> emptyConfig = new HashMap<>();
        HashMap<String, Configuration> configMap = new HashMap<>();
        configMap.put("bigHeader", new Config("blue", "20", "center", "corbel", "bold"));
        configMap.put("small", new SmallHeader());

        boolean noConfig = gj.convertMapToPDF(content);
        boolean convertWithCssFile = gj.convertMapToPDF(content, cssFile);
        boolean convertWithConfigMap = gj.convertMapToPDF(content, configMap);
        boolean convertWithEmptyConf = gj.convertMapToPDF(content, emptyConfig);
        boolean convertWithEmptyCSS = gj.convertMapToPDF(content, emptyCSS);

        assert (noConfig && convertWithCssFile && convertWithConfigMap && convertWithEmptyConf && convertWithEmptyCSS);

    }
}
