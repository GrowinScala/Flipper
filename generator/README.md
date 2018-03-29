# Generator

The present file documents the Generator module.
<br/>

### Table of contents ###

* [Module Structure](#module-structure)
* [Main Features](#main-features)
* [Main Methods / Examples](#main-methods-/-examples)
* [Dependencies](#dependencies)
 
 
---
  
### Module Structure ### 
  
   ```
   Generator/
             ├── java/generator/
             |                  └── GeneratorJava.java ; Java interface for this module's API
             └──scala/generator/
                                ├── Config.scala       ; Configuration object for styling the converted PDF
                                ├── FileHandler.scala  ; Handles the file inputs
                                └── Generator.scala    ; Handles the generation of PDF documents from JSON
   ``` 
   
---

### Main Features ### 

In this Module it is possible to generate a PDF with the information of a JSON String.

* **Parsing a JSON String and Returning a PDF** - Using **_iText_** and **_JSON4S_** we're able
to extract all the information given in the JSON String and put it in a PDF document. This is accomplished in
two different steps, the first is to convert the JSON into an HTML file, and the second is to convert the 
resulting HTML file into the desired PDF file.
 
---

### Main Methods / Examples ###

In this Module there is one single main method that can be called in four different ways. In this section 
there will be some example on how to call this method.

* #### Generate a PDF from a JSON ####

To generate a PDF file from a JSON String you need to call the **`convertJSONtoPDF`** function. This function can
be called by simply passing the JSON String, by passing the JSON String and a CSS file, by passing the JSON 
String, and a CSS String, by passing the JSON String and a Generator.Config. The Generator.Config is a class implemented that 
contains some configurations for the PDF file, these are text color, font size, text alignment, font family
and font weight. This function returns a Boolean saying if the conversion was successful and the output file 
shows in **`./`**.

### Scala
 
```scala
    import generator.Generator.convertJSONtoPDF
    
    val json = """ {"name": "FirstName LastName", "age": 25 } """
    val success = convertJSONtoPDF(json)
```

```scala
    import generator.Generator.convertJSONtoPDF
    import java.io.File
    
    val json = """ {"name": "FirstName LastName", "age": 25 }"""
    val cssFile = new File("CSSFile.css")
    val success = convertJSONtoPDF(json, cssFile)
```

```scala
    import generator.Generator.convertJSONtoPDF
   
    val json = """ {"name": "FirstName LastName", "age": 25 }"""
    val cssString = "p { color: green; }"
    val success = convertJSONtoPDF(json, cssString)
```

```scala
    import generator.Generator.convertJSONtoPDF
    import generator.Config
    
    val json = """ {"name": "FirstName LastName", "age": 25 }"""
    val config = Config("green",12,"center","Arial","bold")
    val success = convertJSONtoPDF(json,config)
```

### Java

```java
    import generator.GeneratorJava;
    
    public class Example {
         public static void main(String[] args) {
             GeneratorJava gj = new GeneratorJava();
             String json = " {\"name\": \"FirstName LastName\", \"age\": 25}";
             boolean success = gj.convertJSONtoPDF(json);
         }
    }
```

```java
    import generator.GeneratorJava;
    import java.io.File;
    
    public class Example {
         public static void main(String[] args) {
             GeneratorJava gj = new GeneratorJava();
             String json = " {\"name\": \"FirstName LastName\", \"age\": 25}";
             File cssFile = new File("CSSFile.css");
             boolean success = gj.convertJSONtoPDF(json, cssFile);
         }
    }
```

```java
    import generator.GeneratorJava;
    
    public class Example {
         public static void main(String[] args) {
             GeneratorJava gj = new GeneratorJava();
             String json = " {\"name\": \"FirstName LastName\", \"age\": 25}";
             String cssString = "p { color: green; }";
             boolean success = gj.convertJSONtoPDF(json, cssString);
         }
    }
```

```java
    import generator.GeneratorJava;
    import generator.Config;
    
    public class Example {
         public static void main(String[] args) {
             GeneratorJava gj = new GeneratorJava();
             String json = " {\"name\": \"FirstName LastName\", \"age\": 25}";
             Config config = new Config("green", 12, "center", "Arial", "bold");
             /*
                Note all parameters in Config class in java's interface must be initialized.
                You achieve that by initializing all parameters as empty String (if you don't want to specify a value for them)
                except fontSize which you initialize it as -1.0 if you don't want to specify a value for it.
                
                If you only want to use a green color and leave all rest by default, you'd do:
                Config config = new Config("green", -1.0, "", "", "");
              */
             boolean success = gj.convertJSONtoPDF(json, cssString);
         }
    }
```

 
---

### Dependencies ### 

to be implemented 
 
