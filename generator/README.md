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
                                              ├── Content.scala         ; Object that describes the content to be displayed (field name, field value, HTML tag)
                                              └── FieldType.scala  ; Classes that enumerate the possible types Flipper supports
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

For both methods you can supply them with information regarding what field type they should take, this way you have a better control on how the converted PDF should look like.The way you achive this is by using one of FieldType's sub-classes. The supported HTML entities so far are:

| FieldType           | Actual HTML Tag     |
|:-------------------:|:-------------------:| 
| Header1             | h1                  |
| Header2             | h2                  |
| Header3             | h3                  |
| OrderedList         | ol                  |
| UnorderedList       | ul                  |
| Table               | table               |
| Paragraph           | p                   |
| Text                | span                |
| Code                | code                |
| Link                | a                   |

### The `Content` and `Config` Classes ###

In both examples you will use 2 main classes for specifying what should be displayed in the PDF file, and how should that information should be displayed.

* #### `Content` class #### 

To specify what should be displayed we use the `Content` class. This class has the following fields:

| Field               | Meaning                                                  |
|:-------------------:|:--------------------------------------------------------:| 
| fieldName           | The name of the keyword to be displayed                  |
| fieldValue          | The actual value of the keyword to be displayed          |
| fieldType           | The type of text the keyword-value pair should represent |
| formattingID        | **Optional** In case you want to customize the styling of this keyword-value pair you can do so specifying a formattingID for it and then create the styling for that formatting using either a:  **`Config` class / CSS File / CSS String**             |


* #### `Config` class #### 

To specify customize the styling of the information displayed we can use the `Config` class (or plain old CSS). Here are the fields this class supports. Note: This class is to be used for basic styling or for those how don't feel to used to CSS, if you're a CSS master, go right ahead and create your CSS file/CSS String and suply the methods with it!

| Field               | Meaning                                            |
|:-------------------:|:--------------------------------------------------:|
| color               | The color to be used in that specific HTML tag     |
| fontSize            | The size of the font, in points (pt)               |
| textAlignment       | The text alignment to take effect                  |
| fontFamily          | The font to be used in the text elements           |
| fontWeight          | The font weight to be used in the text elements    |


<br/>

There's also some predefined styling options supported by Flipper wich you can use by using the 
**`BigHeader`**, **`SmallHeader`**, **`BigHeader`**, **`JustifiedText`** and **`BoldText`**

---

### API Examples ###

* #### Generate a PDF from a Map ####

Generating a PDF file from a Map is done by calling the **`convertMapToPDF`** function, with the Map in the following 
format **`Map[String,Content]`**. This can be done with one of four ways, by simply passing the Map, by passing the Map and
a CSS file, by passing the Map and a CSS String, by passing the Map and a ConfigMap (**`Map[String, Configuration]`**). The ConfigMap is a
Map that contains some configurations for each `formattingID` defined in the **`Map[String, Content]`** object, these are text color, font size, text alignment,
font family and font weight. This function returns a Boolean saying if the conversion was successful and the output file shows in the **root folder** (**`./`**).

### Scala

```scala
    import generator.generate.Generator.convertMapToPDF
    import generator.utils._
    
    val contentMap = 
    Map(
        "name" -> Content("name", "John Doe", Header1()),
        "phones" -> Content("phones", List(12345, 54321), UnorderedList()),
        "webSite" -> Content("webSite", "Great place to work", Link("www.growin.pt"))
        )
    val success = convertMapToPDF(contentMap)
```

```scala
    import generator.generate.Generator.convertMapToPDF
    import generator.utils._
    import java.io.File
    
    val contentMap = 
    Map(
        "name" -> Content("name", "John Doe", Header1(), "bigHeader"),
        "phones" -> Content("phones", List(12345, 54321), UnorderedList(), "list"),
        "webSite" -> Content("webSite", "Great place to work", Link("www.growin.pt"))
        )
    val cssFile = new File("CSSFile.css")
    val success = convertMapToPDF(contentMap, cssFile)
```

```scala
    import generator.generate.Generator.convertMapToPDF
    import generator.utils._
   
    val contentMap = 
    Map(
        "name" -> Content("name", "John Doe", Header1(), "bigHeader"),
        "phones" -> Content("phones", List(12345, 54321), UnorderedList(), "list"),
        "webSite" -> Content("webSite", "Great place to work", Link("www.growin.pt"))
        )
    val cssString = ".bigHeader { color: green; font-size: 30px; text-align: center; } .list{ color: red; font-size: 10px; }"
    val success = convertMapToPDF(contentMap, cssString)
```

```scala
    import generator.generate.Generator.convertMapToPDF
    import generator.utils._
    
    val contentMap = 
    Map(
        "name" -> Content("name", "John Doe", Header1(), "bigHeader"),
        "phones" -> Content("phones", List(12345, 54321), UnorderedList(), "list"),
        "webSite" -> Content("webSite", "Great place to work", Link("www.growin.pt"), "link")
        )
    val configMap = 
    Map(
        "bigHeader" -> Config("green", "30", "center"),
        "list" -> Config("red", "10"),
        "link" -> JustifiedText() //Using Flipper's predefined styling options
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
             contentMap.put("name", new Content("name", "John Doe", new Header1(), ""));
             contentMap.put("phones", new Content("phones", phoneNumbers, new UnorderedList(), ""));
             contentMap.put("webSite", new Content("webSite", "Great place to work", new Link("www.growin.pt"), ""));

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
             contentMap.put("name", new Content("name", "John Doe", new Header1(), "bigHeader"));
             contentMap.put("phones", new Content("phones", phoneNumbers, new UnorderedList(), "list"));
             contentMap.put("webSite", new Content("webSite", "Great place to work", new Link("www.growin.pt"), ""));

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
             contentMap.put("name", new Content("name", "John Doe", new Header1(), "bigHeader"));
             contentMap.put("phones", new Content("phones", phoneNumbers, new UnorderedList(), "list"));
             contentMap.put("webSite", new Content("webSite", "Great place to work", new Link("www.growin.pt"), ""));

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
             contentMap.put("name", new Content("name", "John Doe", new Header1(), "bigHeader"));
             contentMap.put("phones", new Content("phones", phoneNumbers, new UnorderedList(), "list"));
             contentMap.put("webSite", new Content("webSite", "Great place to work", new Link("www.growin.pt"), "link"));

            HashMap<String, Configuration> configMap = new HashMap<>();
            configMap.put("bigHeader", new Config("green", "30", "center", "", "");
            configMap.put("list", new Config("red", "10", "", "", ""));
            configMap.put("link", new JustifiedText());

             boolean success = gj.convertMapToPDF(contentMap, configMap);
             /*
                Note: all parameters in Config class in java's interface must be initialized.
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
 
 * Not specifying any styling details

```scala
    import generator.generate.Generator.convertJSONtoPDF
    
    val contentJSON =
      """
        |{ 
        |   "name"  : {
        |             "fieldName" : "name",
        |             "fieldValue" : "John Doe",
        |             "fieldType" : "Header1"
        |            },
        |  "phones" : {
        |             "fieldName" : "phones",
        |             "fieldValue" : [12345, 54321],
        |             "fieldType" : "UnorderedList"
        |           },
        |  "webSite" : {
        |             "fieldName" : "webSite",
        |             "fieldValue" : "Greate place to work",
        |             "fieldType" : { 
        |                               "link" : "www.growin.pt"
        |                           }
        |           }
        |}
      """.stripMargin
    val success = convertJSONtoPDF(contentJSON)
```

* Specifying styling details with a CSS file

```scala
    import generator.generate.Generator.convertJSONtoPDF
    import java.io.File
    
    val contentJSON =
      """
        |{ 
        |   "name"  : {
        |             "fieldName" : "name",
        |             "fieldValue" : "John Doe",
        |             "fieldType" : "Header1",
        |             "formattingID" : "bigHeader"
        |            },
        |  "phones" : {
        |             "fieldName" : "phones",
        |             "fieldValue" : [12345, 54321],
        |             "fieldType" : "UnorderedList",
        |             "formattingID" : "list"
        |           },
        |  "webSite" : {
        |             "fieldName" : "webSite",
        |             "fieldValue" : "Greate place to work",
        |             "fieldType" : { 
        |                               "link" : "www.growin.pt"
        |                           },
        |             "formattingID" : "link"
        |           }
        |}
      """.stripMargin
    val cssFile = new File("CSSFile.css")
    val success = convertJSONtoPDF(contentJSON, cssFile)
```

* Specifying styling details with a CSS String

```scala
    import generator.generate.Generator.convertJSONtoPDFWithCSS
   
    val contentJSON =
      """
        |{ 
        |   "name"  : {
        |             "fieldName" : "name",
        |             "fieldValue" : "John Doe",
        |             "fieldType" : "Header1",
        |             "formattingID" : "bigHeader"
        |            },
        |  "phones" : {
        |             "fieldName" : "phones",
        |             "fieldValue" : [12345, 54321],
        |             "fieldType" : "UnorderedList",
        |             "formattingID" : "list"
        |           },
        |  "webSite" : {
        |             "fieldName" : "webSite",
        |             "fieldValue" : "Greate place to work",
        |             "fieldType" : { 
        |                               "link" : "www.growin.pt"
        |                           },
        |             "formattingID" : "link"
        |           }
        |}
      """.stripMargin
    val cssString = ".bigHeader { color: green; font-size: 30px; text-align: center; } .list{ color: red; font-size: 10px; }"
    val success = convertJSONtoPDF(contentJSON, cssString)
```
* Specifying styling details with a confinguration JSON

```scala
    import generator.generate.Generator.convertJSONtoPDF
   
    val contentJSON =
      """
        |{ 
        |   "name"  : {
        |             "fieldName" : "name",
        |             "fieldValue" : "John Doe",
        |             "fieldType" : "Header1",
        |             "formattingID" : "bigHeader"
        |            },
        |  "phones" : {
        |             "fieldName" : "phones",
        |             "fieldValue" : [12345, 54321],
        |             "fieldType" : "UnorderedList",
        |             "formattingID" : "list"
        |           },
        |  "webSite" : {
        |             "fieldName" : "webSite",
        |             "fieldValue" : "Greate place to work",
        |             "fieldType" : { 
        |                               "link" : "www.growin.pt"
        |                           },
        |             "formattingID" : "link"
        |           }
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
            "               \"fieldType\" : \"Header1\"" +
            "             },"+
            "  \"phones\" : {" +
            "               \"fieldName\" : \"phones\"," +
            "               \"fieldValue\" : [12345, 54321]," +
            "               \"fieldType\" : \"UnorderedList\"" +
            "             },"+
            "  \"webSite\" : {" +
            "               \"fieldName\" : \"webSite\"," +
            "               \"fieldValue\" : \"Great place to work\"," +
            "               \"fieldType\" : { "+
            "                                   \"link\": \"www.growin.pt\""+
            "                                }" +
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
            "               \"fieldType\" : \"Header1\"," +
            "               \"formattingID\" : \"bigHeader\"" +
            "             },"+
            "  \"phones\" : {" +
            "               \"fieldName\" : \"phones\"," +
            "               \"fieldValue\" : [12345, 54321]," +
            "               \"fieldType\" : \"UnorderedList\"," +
            "               \"formattingID\" : \"list\"" +
            "             },"+
            "  \"webSite\" : {" +
            "               \"fieldName\" : \"webSite\"," +
            "               \"fieldValue\" : \"Great place to work\"," +
            "               \"fieldType\" : { "+
            "                                   \"link\": \"www.growin.pt\""+
            "                                }," +
            "               \"formattingID\" : \"link\"" +
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
            "               \"fieldType\" : \"Header1\"," +
            "               \"formattingID\" : \"bigHeader\"" +
            "             },"+
            "  \"phones\" : {" +
            "               \"fieldName\" : \"phones\"," +
            "               \"fieldValue\" : [12345, 54321]," +
            "               \"fieldType\" : \"UnorderedList\"," +
            "               \"formattingID\" : \"list\"" +
            "             },"+
            "  \"webSite\" : {" +
            "               \"fieldName\" : \"webSite\"," +
            "               \"fieldValue\" : \"Great place to work\"," +
            "               \"fieldType\" : { "+
            "                                   \"link\": \"www.growin.pt\""+
            "                                }," +
            "               \"formattingID\" : \"link\"" +
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
            "               \"fieldType\" : \"Header1\"," +
            "               \"formattingID\" : \"bigHeader\"" +
            "             },"+
            "  \"phones\" : {" +
            "               \"fieldName\" : \"phones\"," +
            "               \"fieldValue\" : [12345, 54321]," +
            "               \"fieldType\" : \"UnorderedList\"," +
            "               \"formattingID\" : \"list\"" +
            "             },"+
            "  \"webSite\" : {" +
            "               \"fieldName\" : \"webSite\"," +
            "               \"fieldValue\" : \"Great place to work\"," +
            "               \"fieldType\" : { "+
            "                                   \"link\": \"www.growin.pt\""+
            "                                }," +
            "               \"formattingID\" : \"link\"" +
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
