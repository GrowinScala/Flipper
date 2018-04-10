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
             ├─────── java/generator/generate/
             |                                └── GeneratorJava.java    ; Java interface for this module's API
             └── scala/generator/
                                 ├── generate/
                                 |            ├── HTMLHandler.scala     ; Handles the HTML operations
                                 |            ├── FileHandler.scala     ; Handles the file inputs
                                 |            └── Generator.scala       ; Handles the generation of PDF documents from JSON/Maps
                                 └───── utils/
                                              ├── Config.scala          ; Configuration object for styling the converted PDF
                                              ├── Content.scala         ; Object that describes the content to be displayed (field name, field value, HTML Entity)
                                              └── HTMLEntity.scala  ; Classes that enumerate the possible HTML entities Flipper supports
   ``` 
   
---

### Main Features ### 

In this Module it is possible to generate a PDF with the information of a JSON String or a Map of Keywords and Content objects
describing what information should be displayed in the PDF file.

* **Parsing a JSON String and Returning a PDF** - Using **_iText_** and **_JSON4S_** we're able
to extract all the information given in the JSON String and put it in a PDF document. This is accomplished in
two different steps, the first is to convert the JSON into an HTML file, and the second is to convert the 
resulting HTML file into the desired PDF file.
 
---

### Main Methods / Examples ###

In this Module there are two main methods that can be called in four different ways each. In this section 
there will be some examples on how to call this methods.

### HTML Entities ###

For both methods you can supply them with information regarding what HTML entity they should represent, this way you have a better control on how the converted PDF should look like.The way you achive this is by using one of HTMLEntity sub-classes. The supported HTML entities so far are:

| HTMLEntity          | Actual HTML Entity  |
|:-------------------:|:-------------------:| 
| H1                  | h1                  |
| H2                  | h2                  |
| H3                  | h3                  |
| OrderedList         | ol                  |
| UnorderedList       | ul                  |
| Table               | table               |
| P                   | p                   |
| Text                | span                |
| Everything else     | span                |

### The `Content` and `Config` Classes ###

In both examples you will use 2 main classes for specifying what should be displayed in the PDF file, and how should that information should be displayed.

* #### `Content` class #### 

To specify what should be displayed we use the `Content` class. This class has the following fields:

| Field               | Meaning                                                  |
|:-------------------:|:--------------------------------------------------------:| 
| fieldName           | The name of the keyword to be displayed                  |
| fieldValue          | The actual value of the keyword to be displayed          |
| htmlEntity          | The HTML entity this keyword-value pair should represent |
| cssClass            | **Optional** In case you want to customize the styling of this keyword-value pair you can do so specifying a CSS class name for it and then create the styling for the CSS class using:  **`Config` class / CSS File / CSS String**             |


* #### `Config` class #### 

To specify customize the styling of the information displayed we can use the `Config` class (or plain old CSS). Here are the fields this class supports. Note: This class is to be used for basic styling or for those how don't feel to used to CSS, if you're a CSS master, go right ahead and create your CSS file/CSS String and suply the methods with it!

| Field               | Meaning                                            |
|:-------------------:|:---------------------------------------------------| 
| color               | The color to be used in that specific HTML entity  |
| fontSize            | The size of the font, in points (pt)               |
| textAlignment       | The text alignment to take effect                  |
| fontFamily          | The font to be used in the text elements           |
| fontWeight          | The font weight to be used in the text elements    |

---

### API Examples ###

* #### Generate a PDF from a Map ####

Generating a PDF file from a Map is done by calling the **`convertMapToPDF`** function, with the Map in the following 
format **`Map[String,Content]`**. This can be done with one of four ways, by simply passing the Map, by passing the Map and
a CSS file, by passing the Map and a CSS String, by passing the Map and a ConfigMap (**`Map[String, Config]`**). The ConfigMap is a
Map that contains some configurations for each CSS class defined in the **`Map[String, Content]`** object, these are text color, font size, text alignment,
font family and font weight. This function returns a Boolean saying if the conversion was successful and the output file shows in the **root folder** (**`./`**).

### Scala

```scala
    import generator.generate.Generator.convertMapToPDF
    import generator.utils._
    
    val contentMap = 
    Map(
        "name" -> Content("name", "John Doe", H1()),
        "phones" -> Content("phones", List(12345, 54321), UnorderedList())
        )
    val success = convertMapToPDF(contentMap)
```

```scala
    import generator.generate.Generator.convertMapToPDF
    import generator.utils._
    import java.io.File
    
    val contentMap = 
    Map(
        "name" -> Content("name", "John Doe", H1(), "bigHeader"),
        "phones" -> Content("phones", List(12345, 54321), UnorderedList(), "list")
        )
    val cssFile = new File("CSSFile.css")
    val success = convertMapToPDF(contentMap, cssFile)
```

```scala
    import generator.generate.Generator.convertMapToPDF
    import generator.utils._
   
    val contentMap = 
    Map(
        "name" -> Content("name", "John Doe", H1(), "bigHeader"),
        "phones" -> Content("phones", List(12345, 54321), UnorderedList(), "list")
        )
    val cssString = ".bigHeader { color: green; font-size: 30px; text-align: center; } .list{ color: red; font-size: 10px; }"
    val success = convertMapToPDF(contentMap, cssString)
```

```scala
    import generator.generate.Generator.convertMapToPDF
    import generator.utils._
    
    val contentMap = 
    Map(
        "name" -> Content("name", "John Doe", H1(), "bigHeader"),
        "phones" -> Content("phones", List(12345, 54321), UnorderedList(), "list")
        )
    val configMap = 
    Map(
        "bigHeader" -> Config("green", "30", "center"),
        "list" -> Config("red", "10")
    )
    val success = convertMapToPDF(contentMap,configMap)
```

### Java

```java
    import generator.generate.GeneratorJava;
    import generator.utils.*;
    import java.util.HashMap;
    import java.util.ArrayList;
    
    public class Example {
         public static void main(String[] args) {
             GeneratorJava gj = new GeneratorJava();

             ArrayList phoneNumbers = new ArrayList<>();
             phoneNumbers.add(12345);
             phoneNumbers.add(54321);

             HashMap<String, Content> contentMap = new HashMap<>();
             contentMap.put("name", new Content("name", "John Doe", new H1()))
             contentMap.put("phones", new Content("phones", phoneNumbers, new UnorderedList()))

             boolean success = gj.convertMapToPDF(contentMap);
         }
    }
```

```java
    import generator.generate.GeneratorJava;
    import generator.utils.*;
    import java.util.HashMap;
    import java.util.ArrayList;
    import java.io.File;

    public class Example {
         public static void main(String[] args) {
             GeneratorJava gj = new GeneratorJava();

             ArrayList phoneNumbers = new ArrayList<>();
             phoneNumbers.add(12345);
             phoneNumbers.add(54321);

             HashMap<String, Content> contentMap = new HashMap<>();
             contentMap.put("name", new Content("name", "John Doe", new H1(), "bigHeader"));
             contentMap.put("phones", new Content("phones", phoneNumbers, new UnorderedList(), "list"));

            File cssFile = new File("CSSFile.css");

             boolean success = gj.convertMapToPDF(contentMap, cssFile);
         }
    }
```

```java
    import generator.generate.GeneratorJava;
    import generator.utils.*;
    import java.util.HashMap;
    import java.util.ArrayList;
    
    public class Example {
         public static void main(String[] args) {
             GeneratorJava gj = new GeneratorJava();

             ArrayList phoneNumbers = new ArrayList<>();
             phoneNumbers.add(12345);
             phoneNumbers.add(54321);

             HashMap<String, Content> contentMap = new HashMap<>();
             contentMap.put("name", new Content("name", "John Doe", new H1(), "bigHeader"));
             contentMap.put("phones", new Content("phones", phoneNumbers, new UnorderedList(), "list"));

            String cssString = ".bigHeader { color: green; font-size: 30px; text-align: center; } .list{ color: red; font-size: 10px; }";

             boolean success = gj.convertMapToPDF(contentMap, cssString);
         }
    }
```

```java
    import generator.generate.GeneratorJava;
    import generator.utils.*;
    import java.util.HashMap;
    import java.util.ArrayList;
    
    public class Example {
         public static void main(String[] args) {
             GeneratorJava gj = new GeneratorJava();

             ArrayList phoneNumbers = new ArrayList<>();
             phoneNumbers.add(12345);
             phoneNumbers.add(54321);

             HashMap<String, Content> contentMap = new HashMap<>();
             contentMap.put("name", new Content("name", "John Doe", new H1(), "bigHeader"));
             contentMap.put("phones", new Content("phones", phoneNumbers, new UnorderedList(), "list"));

            HashMap<String, Config> configMap = new HashMap<>();
            configMap.put("bigHeader", new Config("green", "30", "center", "", "");
            configMap.put("list", new Config("red", "10", "", "", ""));

             boolean success = gj.convertMapToPDF(contentMap, configMap);
             /*
                Note all parameters in Config class in java's interface must be initialized.
                You achieve that by initializing all parameters as empty String (if you don't want to specify a value for them)
                
                If you only want to use a green color and leave all rest by default, you'd do:
                Config config = new Config("green", -1.0, "", "", "");
              */
         }
    }
```

* #### Generate a PDF from a JSON ####

To generate a PDF file from a JSON String you need to call the **`convertJSONtoPDF`** method. Similarly to the
previous method, this can be done by simply passing the JSON String, or by passing a JSON String and a CSS File/CSS String/Config JSON. This method returns a Boolean saying if the conversion was successful and the output file 
shows in the **root folder** (**`./`**).

### Scala
 
```scala
    import generator.generate.Generator.convertJSONtoPDF
    
    val contentJSON =
      """
        |{ 
        |   "name"  : {
        |             "fieldName" : "name",
        |             "fieldValue" : "John Doe",
        |             "htmlEntity" : "H1",
        |             "cssClass" : "bigHeader"
        |            },
        |  "phones" : {
        |             "fieldName" : "phones",
        |             "fieldValue" : [12345, 54321],
        |             "htmlEntity" : "UnorderedList",
        |             "cssClass" : "list"
        |           },
        |}
      """.stripMargin
    val success = convertJSONtoPDF(contentJSON)
```

```scala
    import generator.generate.Generator.convertJSONtoPDF
    import java.io.File
    
    val contentJSON =
      """
        |{ 
        |   "name"  : {
        |             "fieldName" : "name",
        |             "fieldValue" : "John Doe",
        |             "htmlEntity" : "H1",
        |             "cssClass" : "bigHeader"
        |            },
        |  "phones" : {
        |             "fieldName" : "phones",
        |             "fieldValue" : [12345, 54321],
        |             "htmlEntity" : "UnorderedList",
        |             "cssClass" : "list"
        |           },
        |}
      """.stripMargin
    val cssFile = new File("CSSFile.css")
    val success = convertJSONtoPDF(contentJSON, cssFile)
```

```scala
    import generator.generate.Generator.convertJSONtoPDFWithCSS
   
    val contentJSON =
      """
        |{ 
        |   "name"  : {
        |             "fieldName" : "name",
        |             "fieldValue" : "John Doe",
        |             "htmlEntity" : "H1",
        |             "cssClass" : "bigHeader"
        |            },
        |  "phones" : {
        |             "fieldName" : "phones",
        |             "fieldValue" : [12345, 54321],
        |             "htmlEntity" : "UnorderedList",
        |             "cssClass" : "list"
        |           },
        |}
      """.stripMargin
    val cssString = ".bigHeader { color: green; font-size: 30px; text-align: center; } .list{ color: red; font-size: 10px; }"
    val success = convertJSONtoPDF(contentJSON, cssString)
```

```scala
    import generator.generate.Generator.convertJSONtoPDF
   
    val contentJSON =
      """
        |{ 
        |   "name" : {
        |             "fieldName" : "name",
        |             "fieldValue" : "John Doe",
        |             "htmlEntity" : "H1",
        |             "cssClass" : "bigHeader"
        |            },
        |
        |  "phones" : {
        |             "fieldName" : "phones",
        |             "fieldValue" : [12345, 54321],
        |             "htmlEntity" : "UnorderedList",
        |             "cssClass" : "list"
        |           },
        |}
      """.stripMargin
    
    val configJSON =
      """
        |{ 
        |  "bigHeader" : {
        |                  "color" : "green",
        |                  "fontSize"  : "30",
        |                  "textAlignment" : "center",
        |                },
        |
        |  "list" : {
        |             "color" : "red",
        |             "fontSize"  : "10"
        |           },
        |
        |}
      """.stripMargin
    val success = convertJSONtoPDF(contentJSON, configJSON)
```

### Java

```java
    import generator.generate.GeneratorJava;
    
    public class Example {
         public static void main(String[] args) {
            GeneratorJava gj = new GeneratorJava();
            String contentJSON = ""+
            "{" +
            "  \"name\" : {" +
            "               \"fieldName\" : \"name\"," +
            "               \"fieldValue\" : \"John Doe\"," +
            "               \"htmlEntity\" : \"H1\"" +
            "             },"+
            "  \"phones\" : {" +
            "               \"fieldName\" : \"phones\"," +
            "               \"fieldValue\" : [12345, 54321]," +
            "               \"htmlEntity\" : \"UnorderedList\"" +
            "             }"+
            "}";

            boolean success = gj.convertJSONtoPDF(contentJSON);
         }
    }
```

```java
    import generator.generate.GeneratorJava;
    import java.io.File;
    
    public class Example {
         public static void main(String[] args) {
            GeneratorJava gj = new GeneratorJava();

            String contentJSON = ""+
            "{" +
            "  \"name\" : {" +
            "               \"fieldName\" : \"name\"," +
            "               \"fieldValue\" : \"John Doe\"," +
            "               \"htmlEntity\" : \"H1\"," +
            "               \"cssClass\" : \"bigHeader\"" +
            "             },"+
            "  \"phones\" : {" +
            "               \"fieldName\" : \"phones\"," +
            "               \"fieldValue\" : [12345, 54321]," +
            "               \"htmlEntity\" : \"UnorderedList\"," +
            "               \"cssClass\" : \"list\"" +
            "             }"+
            "}";
            File cssFile = new File("CSSFile.css");
            boolean success = gj.convertJSONtoPDF(contentJSON, cssFile);
         }
    }
```

```java
    import generator.generate.GeneratorJava;
    
    public class Example {
         public static void main(String[] args) {
            GeneratorJava gj = new GeneratorJava();

            String contentJSON = ""+
            "{" +
            "  \"name\" : {" +
            "               \"fieldName\" : \"name\"," +
            "               \"fieldValue\" : \"John Doe\"," +
            "               \"htmlEntity\" : \"H1\"," +
            "               \"cssClass\" : \"bigHeader\"" +
            "             },"+
            "  \"phones\" : {" +
            "               \"fieldName\" : \"phones\"," +
            "               \"fieldValue\" : [12345, 54321]," +
            "               \"htmlEntity\" : \"UnorderedList\"," +
            "               \"cssClass\" : \"list\"" +
            "             }"+
            "}";
             
            String cssString = ".bigHeader { color: green; font-size: 30px; text-align: center; } .list{ color: red; font-size: 10px; }"

            boolean success = gj.convertJSONtoPDF(contentJSON, cssString);
         }
    }
```

```java
    import generator.generate.GeneratorJava;
    import generator.utils.Config;
    
    public class Example {
         public static void main(String[] args) {
            GeneratorJava gj = new GeneratorJava();

            String contentJSON = ""+
            "{" +
            "  \"name\" : {" +
            "               \"fieldName\" : \"name\"," +
            "               \"fieldValue\" : \"John Doe\"," +
            "               \"htmlEntity\" : \"H1\"," +
            "               \"cssClass\" : \"bigHeader\"" +
            "             },"+
            "  \"phones\" : {" +
            "               \"fieldName\" : \"phones\"," +
            "               \"fieldValue\" : [12345, 54321]," +
            "               \"htmlEntity\" : \"UnorderedList\"," +
            "               \"cssClass\" : \"list\"" +
            "             }"+
            "}";

            String configJSON = ""+
            "{" +
            "  \"bigHeader\" : {" +
            "                    \"color\" : \"green\"," +
            "                    \"fontSize\" : \"30\"," +
            "                    \"textAlignment\" : \"center\"" +
            "             },"+
            "  \"list\" : {" +
            "                    \"color\" : \"red\"," +
            "                    \"fontSize\" : \"10\"" +
            "             }"+
            "}";
            boolean success = gj.convertJSONtoPDF(contentJSON, configJSON);
         }
    }
```
