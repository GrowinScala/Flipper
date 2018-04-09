package parser.conversion;

import java.io.File;

public class ConverterJava {

    /**
     * Method that converts a pdf file into a png image
     *
     * @param file     - The PDF file to be converted
     * @param fileType - FileType with one of the possible file types of the Enum FileType to be converted into
     * @return - A Boolean saying if the conversion was successful
     * @throws IllegalArgumentException if the specified fileType did not equal the supported file types
     */
    public Boolean convertPDFtoIMG(File file, FileType fileType) throws IllegalArgumentException {
        if (fileType == null)
            throw new IllegalArgumentException("File type must be one of png, jpg, gif or jpeg");
        return Converter.convertPDFtoIMG(file, fileType);
    }

    /**
     * Method that creates a odf file with the information taken from a pdf (Note: does not maintain full formatting)
     *
     * @param file - The PDF file to be converted
     * @return - A Boolean saying if the conversion was successful
     */
    public Boolean convertPDFtoODT(File file) {
        return Converter.convertPDFtoODT(file);
    }
}
