# Converter

The present file documents the Converter module.
<br/>

### Table of contents ###

* [Module Structure](#module-structure)
* [Main Features](#main-features)
* [Main Methods](#main-methods)
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
an HTML file using **_Apache's PDFBox_** and then reading each individual line of the HTML and 
parsing it to extract the format of the original file and inputing it into a new ODT using 
the **_ODF Toolkit Project_**.


 
---

### Main Methods ###

to be implemented

 
---

### Dependencies ### 

to be implemented 
 
