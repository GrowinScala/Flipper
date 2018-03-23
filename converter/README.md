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
   Converter/
             └── Converter.scala ; PDF to other file types module
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

to be implemented

* #### Converting a PDF into an image type ####

To convert the PDF document using Flipper you have to pass the document path and the type of file you want to
convert the original into to **`convertPDFtoIMG`** (found in **Converter.scala**), the file type is chosen 
from an ENUM which contains the following: png, jpg, jpeg and gif. The output image file of this function
will go into the PDFtoIMG folder in the target folder of the project. The function returns a Boolean saying 
if the conversion was successful.

```scala
    import Converter.convertPDFtoIMG
    
    val filePath = "./path/to/pdf/document"
    val fileType = FileType.jpg
    val success = convertPDFtoIMG(filePath, fileType)
```
 
---

### Dependencies ### 

to be implemented 
 
