# Generator

The present file documents the Generator module.
<br/>

### Table of contents ###

* [Module Structure](#module-structure)
* [Main Features](#main-features)
* [Main Methods / Examples](#main-methods-/-examples)
 
 
---
  
### Module Structure ### 
  
   ```
   generator/
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

In this Module there are two main methods that can be called in four different ways each. In this section 
there will be some example on how to call this method.

* #### Generate a PDF from a Map ####

Generating a PDF file from a Map is done by calling the **`convertMapToPDF`** function, with the Map in the following 
format **`Map[String,Any]`**. This can be done with one of four ways, by simply passing the Map, by passing the Map and
a CSS file, by passing the Map and a CSS String, by passing the Map and a Generator.Config. The Generator.Config is a
class implemented that contains some configurations for the PDF file, these are text color, font size, text alignment,
font family and font weight. This function returns a Boolean saying if the conversion was successful and the output file
shows in **`./`**.

### Scala

```scala
    import generator.Generator.convertMapToPDF
    
    val jsonMap = Map("name" -> "FirstName LastName", "age" -> 25)
    val success = convertMapToPDF(jsonMap)
```

```scala
    import generator.Generator.convertMapToPDF
    import java.io.File
    
    val jsonMap = Map("name" -> "FirstName LastName", "age" -> 25)
    val cssFile = new File("CSSFile.css")
    val success = convertMapToPDF(jsonMap, cssFile)
```

```scala
    import generator.Generator.convertMapToPDF
   
    val jsonMap = Map("name" -> "FirstName LastName", "age" -> 25)
    val cssString = "p { color: green; }"
    val success = convertMapToPDF(jsonMap, cssString)
```

```scala
    import generator.Generator.convertMapToPDF
    import generator.Config
    
    val jsonMap = Map("name" -> "FirstName LastName", "age" -> 25)
    val config = Config("green",12,"center","Arial","bold")
    val success = convertMapToPDF(jsonMap,config)
```

### Java

```java
    import generator.GeneratorJava;
    import java.util.ArrayList;
    import java.util.HashMap;
    import java.util.List;

    
    public class Example {
         public static void main(String[] args) {
             GeneratorJava gj = new GeneratorJava();
             HashMap<String,List<String>> hm = new HashMap<>();
             ArrayList<String> l1 = new ArrayList<>();
             ArrayList<String> l2 = new ArrayList<>();
             l1.add("FirstName LastName");
             hm.put("name",l1);
             l2.add("25");
             hm.put("age",l2);
             jsonMap.put("FistName LastName",lst);
             boolean success = gj.convertMapToPDF(jsonMap);
         }
    }
```

```java
    import generator.GeneratorJava;
    import java.util.ArrayList;
    import java.util.HashMap;
    import java.util.List;
    import java.io.File;
    
    public class Example {
         public static void main(String[] args) {
             GeneratorJava gj = new GeneratorJava();
             HashMap<String,List<String>> hm = new HashMap<>();
             ArrayList<String> l1 = new ArrayList<>();
             ArrayList<String> l2 = new ArrayList<>();
             l1.add("FirstName LastName");
             hm.put("name",l1);
             l2.add("25");
             hm.put("age",l2);
             File cssFile = new File("CSSFile.css");
             boolean success = gj.convertMapToPDF(jsonMap, cssFile);
         }
    }
```

```java
    import generator.GeneratorJava;
    import java.util.ArrayList;
    import java.util.HashMap;
    import java.util.List;
    
    public class Example {
         public static void main(String[] args) {
             GeneratorJava gj = new GeneratorJava();
             HashMap<String,List<String>> hm = new HashMap<>();
             ArrayList<String> l1 = new ArrayList<>();
             ArrayList<String> l2 = new ArrayList<>();
             l1.add("FirstName LastName");
             hm.put("name",l1);
             l2.add("25");
             hm.put("age",l2);
             String cssString = "p { color: green; }";
             boolean success = gj.convertMapToPDF(jsonMap, cssString);
         }
    }
```

```java
    import generator.GeneratorJava;
    import java.util.ArrayList;
    import java.util.HashMap;
    import java.util.List;
    import generator.Config;
    
    public class Example {
         public static void main(String[] args) {
             GeneratorJava gj = new GeneratorJava();
             HashMap<String,List<String>> hm = new HashMap<>();
             ArrayList<String> l1 = new ArrayList<>();
             ArrayList<String> l2 = new ArrayList<>();
             l1.add("FirstName LastName");
             hm.put("name",l1);
             l2.add("25");
             hm.put("age",l2);
             Config config = new Config("green", 12, "center", "Arial", "bold");
             /*
                Note all parameters in Config class in java's interface must be initialized.
                You achieve that by initializing all parameters as empty String (if you don't want to specify a value for them)
                except fontSize which you initialize it as -1.0 if you don't want to specify a value for it.
                
                If you only want to use a green color and leave all rest by default, you'd do:
                Config config = new Config("green", -1.0, "", "", "");
              */
             boolean success = gj.convertMapToPDF(jsonMap, cssString);
         }
    }
```

* #### Generate a PDF from a JSON ####

To generate a PDF file from a JSON String you need to call the **`convertJSONtoPDF`** function. Similarly to the 
previous function, this can be done by simply passing the JSON String, by passing the JSON String and a CSS file, 
by passing the JSON String and a CSS String, by passing the JSON String and a Generator.Config. The Generator.Config is
a class implemented that contains some configurations for the PDF file, these are text color, font size, text alignment,
font family and font weight. This function returns a Boolean saying if the conversion was successful and the output file 
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
