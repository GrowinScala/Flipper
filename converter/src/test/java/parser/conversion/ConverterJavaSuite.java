package parser.conversion;

import junit.framework.TestCase;
import org.junit.Test;

import java.io.File;

public class ConverterJavaSuite extends TestCase {
    protected File validFile = new File("./converter/src/main/resources/cv.pdf");
    protected ConverterJava cj = new ConverterJava();

    /**
     * Tests that sending a valid filePath and fileType to convertPDFtoIMG will return true
     * This means that the method successfully converted the pdf to the specified file type
     */
    @Test
    public void testValidConvertPDFToIMG() {
        assert (cj.convertPDFtoIMG(validFile, new PNG()));
    }

    /**
     * Tests that sending an invalid filePath convertPDFtoIMG will return false
     * This means that the method did not convert the pdf to the specified file type
     */
    @Test
    public void testInvalidConvertPDFtoIMG() {
        assert (!cj.convertPDFtoIMG(new File(""), new PNG()) && !cj.convertPDFtoIMG(null, new PNG()));
    }

    /**
     * Tests that sending a null FileType object to convertPDFtoIMG will result in a
     * IllegalArgumentException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testExceptionConvertPDFtoIMG() {
        try {
            cj.convertPDFtoIMG(validFile, null);
        } catch (Exception e) {
            assert (e instanceof IllegalArgumentException);
        }
    }

    /**
     * Tests that sending a valid filePath to convertPDFtoODF will return true
     * meaning it successfully converted the PDF to ODF
     */
    @Test
    public void testValidConvertPDFToODT() {
        assert (cj.convertPDFtoODT(validFile));
    }

    /**
     * Tests that sending an invalid filePath to convertPDFtoODF will return false
     * meaning it did not convert the PDF to ODF
     */
    @Test
    public void testInvalidConvertPDFtoODT() {
//        cj.convertPDFtoODT(null);
        assert (!cj.convertPDFtoODT(new File("")) && !cj.convertPDFtoODT(null));
    }
}
