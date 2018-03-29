# Converter

The present file documents the Converter module.
<br/>

### Table of contents ###

* [Module Structure](#module-structure)
* [Main Features](#main-features)
* [Main Methods / Examples](#main-methods-/-examples)
* [Dependencies](#dependencies)
 
 
---
### Module Structure ### 
  
   ```
   converter/
             ├── java/parser/conversion/
             |                          └── ConverterJava.java  ; Java interface for this module's API
             └── scala/parser/conversion/
                                         ├── FileType.scala     ; Enum for the supported file type conversions
                                         └── Converter.scala    ; Handles the conversion of PDF into other file types
   ```
---

### Main Features ### 

The main objective of this module is to convert a PDF file to either an image file type like 
JPG, PNG and GIF or an Apache Open Document file, ODT. This module is divided in two separate parts:

* **Convert PDF into an image type** - Using **_Apache's PDFBox_** to get the PDF file we then use 
Java's **_ImageIO_** we are able to transform each individual page of the document into an image type. 

* **Convert PDF into an Open Document** - This feature is accomplished by first transforming the PDF into
an HTML file using **_Apache's PDFBox_** and converting the resulting HTML into a new ODT using 
the **_ODF Toolkit Project_**. The reason we first convert to HTML is to maintain the format of the 
original PDF as much as possible.

  
---

### Main Methods / Examples ###

This section contains some examples of the features described above as well as a brief API documentation 
for the module in order for you to find what you need.
<br/>

* #### Converting a PDF into an image type ####

To convert the PDF document using Flipper you have to pass the document path and the type of file into which 
you want to convert the original to **`convertPDFtoIMG`** (found in **Converter.scala** or **ConverterJava.java**), the file type is 
chosen from an ENUM which contains the following: png, jpg, jpeg and gif. The output image files will appear 
in **`./target/PDFtoIMG`**, and the function returns a Boolean saying if the conversion was successful.

### Scala

```scala
    import java.io.File
    import parser.conversion.Converter._
    import parser.conversion.FileType
    
    val file = new File("./path/to/pdf/document")
    val success = convertPDFtoIMG(file, FileType.jpg)
```

### Java

```java
    import parser.conversion.ConverterJava;
    import java.io.File;
    import parser.conversion.FileType;
    
    public class Example {
         public static void main(String[] args) {
            ConverterJava cj = new ConverterJava();
            File file = new File("./path/to/pdf/document");
            boolean success = cj.convertPDFtoIMG(file, FileType.jpg());
         }
    }
```

 
 * #### Converting a PDF into an Open Document file ####
 
 To convert the PDF document to an ODT document you only have to pass the path of the file you want to convert 
 to **`convertPDFtoODT`** (found in **Converter.scala** or **ConverterJava.java**). the output will appear in **`./`**, and the functin
 returns a Boolean saying if the conversion was successful. 
 
 ### Scala
 
 ```scala
     import java.io.File
     import parser.conversion.Converter._
     
     val file = new File("./path/to/pdf/document")
     val success = convertPDFtoODT(file)
 ```
 
 ### Java
 
 ```java
     import parser.conversion.ConverterJava;
     import java.io.File;
     import parser.conversion.FileType;
     
     public class Example {
          public static void main(String[] args) {
             ConverterJava cj = new ConverterJava();
             File file = new File("./path/to/pdf/document");
             boolean success = cj.convertPDFtoODT(file);
          }
     }
 ```
 
---

### Dependencies ### 

to be implemented 
 
