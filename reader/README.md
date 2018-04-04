# Reader

The present file documents the Reader module.
<br/>

### Table of contents ###

* [Module Structure](#module-structure)
* [Main Features](#main-features)
* [Main Methods / Examples](#main-methods-/-examples)
 
  
---
 
### Module Structure ### 
 
  ```
  reader/
         ├── java/parser/extraction/
         |                          └── ExtractorJava.java     ; Java interface for this module's API
         └── scala/parser/
                         ├── extraction/
                         |              ├── Extractor.scala    ; Handles the PDF parsing and JSON generation
                         |              └── FileHandler.scala  ; Handles the file inputs
                         └── utils/                
                                   ├── ImageProcessing.scala   ; Handles processing the image and extract its text
                                   ├── OpenNLP.scala           ; Handles the NLP (natural language processing) functionalities
                                   ├── Specification.scala     ; Classes that help specify the keywords sent when extracting information
                                   └── SpellChecker.scala      ; Handles the spellchecking operations to improve the OCR's accuracy
  ```
  
---

### Main Features ### 

This module of Flipper is dedicated entirely to parsing a PDF document and returning 
a JSON object with useful information extracted from the document. In order to achieve this goal
this module implements features such as:

* **Extracting the text content of a PDF document** - Using **_Apache's PDF Box_** we're able
to extract all of the text information inside the PDF doc.

* **Searching values for keywords in a text** - In order to extract the correct information
to be returned in the JSON object the user has to send a List containing keywords that he/she
wants to find values for. **Example** if we were to pass in a list containing only a **"Name"** 
keyword Flipper would do it's best to find a value (in this case a name) for that keyword, and
 in this particular case would return `{"name" : <name that was found> }`.
 
* **Extracting text from images** - Using **_Tess4J_** and **_Scrimage_** Flipper is also able
to extract text from images applying an OCR (optical character recognition) with great accuracy in
 order to maximize the possibility of extracting useful information
 
 We will dive deeper in how you can go by doing this yourself on your project in a second.
 
---

### Main Methods / Examples ###

In this section we will show some examples (both in **Scala** and **Java**) of the features above as well as a short API documentation for this module
in order for you to find what you need.
<br/>

* #### Extracting text from the PDF doc and its images ####

To extract the text from a PDF document using Flipper simply pass the document path
to **`readPDF`** (found in **Extractor.scala** or **ExtractorJava.java**).

### Scala

```scala
    import parser.extraction.Extractor._
    import java.io.File
    
    val file = new File("./path/to/pdf/document")
    val extractedText = readPDF(file)
```

### Java

```java
    import parser.extraction.ExtractorJava;
    import java.io.File;
    
    public class Example {
         public static void main(String[] args) {
            ExtractorJava ex = new ExtractorJava();
            File file = new File("./path/to/pdf/document");
            String extractedText = ex.readPDF(file);
         }
    }
```

You now have the have the extracted text, wrapped in an `Option[String]` (or just a plain `String` if you're using Java's interface) to prevent `null`'s
in case the file does not exist.

* #### Parsing PDF and returning a List of JSON Objects #### 

The most straight-forward way to use this module's API is to call `getJSONObjects`. You need
to supply this method with the **text** you want to extract data from and a **Map of keywords** for which
you want to obtain values.

<br/>

The keywords Map is a map of Keywords and a Specification, the specification has three different cases,these are
MultipleOf, in which the user inputs a list of options and the return is a list of the options found in the text,
OneOf, like the previous case the user inputs a list of choices, but the return is a single option from which to
choose, and PostTag, this is for the Natural Language Processor (**_Apache's OpenNLP_**) Fliepper uses, in order to
improve the odds of finding a useful value for a given keyword. This POS tag simply tells Flipper what kind of 
value you want to obtain for a given keyword, the possible POSTags can be found bellow:


| Possible POSTags    |
|:-------------------:|
| Adjective           |
| ProperNoun          |
| Noun                |
| PluralNoun          |
| Verb                |
| VerbPastParticiple  |
| VerbGerund          |
| Number              |
| Adverb              |


You can now implement the following snippet:


### Scala

```scala
    import parser.extraction.Extractor.{readPDF, getJSONObjects}
    import java.io.File
    import parser.utils.{ProperNoun, Number}
    
    val file = new File("./path/to/pdf/document")
    val extractedText = readPDF(file)
    val keywords = Map("name"-> ProperNoun(), "age" -> Number())
    
    val jsonObjs : List[String] = getJSONObjects(extractedText, keywords)
    
    //jsonObjs -> List(
    //                "{ "name" : "John Doe" , "age" : 21 }",
    //                "{ "name" : "Jane Doe" , "age" : 22 }"
    //                )
```

### Java

```java
    import parser.extraction.ExtractorJava;
    import java.io.File;
    import parser.utils.Number;
    import parser.utils.ProperNoun;
    import java.util.HashMap;
    import java.util.List;
    
    public class Example {
         public static void main(String[] args) {
            ExtractorJava ex = new ExtractorJava();
            File file = new File("./path/to/pdf/document");
            String extractedText = ex.readPDF(file);
            
            HashMap keywords = new HashMap<>();
            keywords.put("name", new ProperNoun());
            keywords.put("age", new Number());
            
            List jsonOBjs = ex.getJSONObjects(text, keywords);
            
            //jsonOBjs -> List(
            //                 "{ "name" : "John Doe" , "age" : 21 }",
            //                 "{ "name" : "Jane Doe" , "age" : 22 }"
            //                )
         }
    }
```

You can also send an optional flag to `getJSONObjects` specifying how you want the JSON to be outputed when a keyword has no value:

Possibilities:

* "empty" (Default) &nbsp; &nbsp;- `{ "name" : "John Doe", "age": "" }`
* "null" &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; - `{ "name" : "John Doe", "age": null }`
* "remove" &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; - `{ "name" : "John Doe" }`

This would return you a List of JSON objects in the form of Strings. Flipper also provides
a more in-depth API in case you want a Map of keywords and the values found for them instead of a JSON object
which we will see next.

* #### Specifying values to find in the text

In some cases you might want to specify certain possible values to be found in the text, if you want to find only one of 
those possible values or multiple of them, you can achive that by using the **`OneOf`** / **`MultipleOf`** classes 
(in **Java** you use the **`Choice`** factory class that has methods for creating these scala classes, but we'll get to that in a second).
You only have to pass an additional List containing the **possible** values you want to find, like so: 

### Scala

```scala
    import parser.extraction.Extractor.{readPDF, getJSONObjects}
    import java.io.File
    import parser.utils.{ProperNoun, Number, OneOf, MultipleOf}
    
    val file = new File("./path/to/pdf/document")
    val extractedText = readPDF(file)
    val oneOfList = List("single","married","divorced")
    val multiList = List("java","scala","c","php","sql")
    val keywords = Map("civil status"-> OneOf(oneOfList), "skills" -> MultipleOf(multiList))
    
    val jsonObjs : List[String] = getJSONObjects(extractedText, keywords)
    
    //jsonObjs -> List(
    //                "{ "civil status" : "married" , "skills" : [java, c] }",
    //                "{ "civil status" : "single" , "skills" : [scala, php, sql] }"
    //                )
```

### Java

```java
    import parser.extraction.ExtractorJava;
    import java.io.File;
    import parser.utils.Choices;
    import java.util.HashMap;
    import java.util.List;
    import java.util.ArrayList;
    
    public class Example {
         public static void main(String[] args) {
            ExtractorJava ex = new ExtractorJava();
            File file = new File("./path/to/pdf/document");
            String extractedText = ex.readPDF(file);
            
            ArrayList oneOfList = new ArrayList();
            ArrayList multiList = new ArrayList();
            
            oneOfList.add("single");
            oneOfList.add("married");
            oneOfList.add("divorced");
            
            multiList.add("java");
            multiList.add("scala");
            multiList.add("c");
            multiList.add("php");
            multiList.add("sql");
                        
            
            HashMap keywords = new HashMap<>();
            keywords.put("name", Choices.oneOf(oneOfList));
            keywords.put("age", Choices.multipleOf(multiList));
            
            List jsonOBjs = ex.getJSONObjects(text, keywords);
            
            //jsonObjs -> List(
            //                "{ "civil status" : "married" , "skills" : [java, c] }",
            //                "{ "civil status" : "single" , "skills" : [scala, php, sql] }"
            //                )
         }
    }
```

* #### Getting a Map of keywords and all the values found for them ####

In case you want to obtain a Map Keywords with all the values found for that keyword, Flipper provides you
with that possibility through **`getAllMatchedValues`**.

### Scala

```scala
    import parser.extraction.Extractor.{readPDF, getAllMatchedValues}
    import java.io.File
    import parser.utils.{ProperNoun, Number, OneOf, MultipleOf}
    
    val file = new File("./path/to/pdf/document")
    val extractedText = readPDF(file)
    val oneofLst = List("single","married","divorced")
    val multiLst = List("java","scala","c","php","sql")
    val keywords = Map("name"-> ProperNoun(), "age" -> Number(), "marital status" -> OneOf(oneofLst), "skills" -> MultipleOf(multiLst))
    
    val matchedValues = getAllMatchedValues(extractedText, keywords) 
    //matchedValues -> Map(
    //                      "name" -> List("John Doe", "Jane Doe"),
    //                      "age" -> List("21", "22")
    //                    )
```

### Java

```java
    import parser.extraction.ExtractorJava;
    import java.io.File;
    import parser.utils.Number;
    import parser.utils.ProperNoun;
    import java.util.HashMap;
    import java.util.Map;
    
    public class Example {
         public static void main(String[] args) {
            ExtractorJava ex = new ExtractorJava();
            File file = new File("./path/to/pdf/document");
            String extractedText = ex.readPDF(file);
            
            HashMap keywords = new HashMap<>();
            keywords.put("name", new ProperNoun());
            keywords.put("age", new Number());
            
            Map matchedValues = ex.getAllMatchedValues(extractedText, keywords);
            
            //matchedValues -> Map(
            //                      "name" -> List("John Doe", "Jane Doe"),
            //                      "age" -> List("21", "22")
            //                    )
         }
    }
```

* #### Getting just a single value for each keyword ####

This method works exactly like the one above but instead of returning every value found for a keyword, returns only one.

### Scala

```scala
    import parser.extraction.Extractor.{readPDF, getSingleMatchedValue}
    import java.io.File
    import parser.utils.{ProperNoun, Number, OneOf, MultipleOf}
    
    val file = new File("./path/to/pdf/document")
    val extractedText = readPDF(file)
    val oneofLst = List("single","married","divorced")
    val multiLst = List("java","scala","c","php","sql")
    val keywords = Map("name"-> ProperNoun(), "age" -> Number(), "marital status" -> OneOf(oneofLst), "skills" -> MultipleOf(multiLst))
    
    val matchedValues = getSingleMatchedValue(extractedText, keywords) 
    //matchedValues -> Map(
    //                      "name" -> List("John Doe"),
    //                      "age" -> List("21")
    //                     )
```

### Java

```java
    import parser.extraction.ExtractorJava;
    import java.io.File;
    import parser.utils.Number;
    import parser.utils.ProperNoun;
    import java.util.HashMap;
    import java.util.Map;
    
    public class Example {
         public static void main(String[] args) {
            ExtractorJava ex = new ExtractorJava();
            File file = new File("./path/to/pdf/document");
            String extractedText = ex.readPDF(file);
            
            HashMap keywords = new HashMap<>();
            keywords.put("name", new ProperNoun());
            keywords.put("age", new Number());
            
            Map matchedValues = ex.getSingleMatchedValue(extractedText, keywords);
            
            //matchedValues -> Map(
            //                      "name" -> List("John Doe"),
            //                      "age" -> List("21")
            //                     )
         }
    }
```

* #### Getting all possible pre-JSON objects for the values found ####

This method returns a List containing all the possible pre-JSON objects for the values found for 
the given keywords.

### Scala

```scala
    import parser.extraction.Extractor.{readPDF, getAllObjects}
    import java.io.File
    import parser.utils.{ProperNoun, Number, OneOf, MultipleOf}
    
    val filePath = new File("./path/to/pdf/document")
    val extractedText = readPDF(filePath)
    val oneofLst = List("single","married","divorced")
    val multiLst = List("java","scala","c","php","sql")
    val keywords = Map("name"-> ProperNoun(), "age" -> Number(), "marital status" -> OneOf(oneofLst), "skills" -> MultipleOf(multiLst))
    
    val matchedValues = getAllObjects(extractedText, keywords) 
    //matchedValues -> List(
    //                      Map("name" -> List("John Doe"), "age" -> List("21")),
    //                      Map("name" -> List("Jane Doe"), "age" -> List("22"))
    //                     )
```

### Java

```java
    import parser.extraction.ExtractorJava;
    import java.io.File;
    import parser.utils.Number;
    import parser.utils.ProperNoun;
    import java.util.HashMap;
    import java.util.List;
    
    public class Example {
         public static void main(String[] args) {
            ExtractorJava ex = new ExtractorJava();
            File file = new File("./path/to/pdf/document");
            String extractedText = ex.readPDF(file);
            
            HashMap keywords = new HashMap<>();
            keywords.put("name", new ProperNoun());
            keywords.put("age", new Number());
            
            List matchedValues = ex.getAllObjects(extractedText, keywords);
            
            //matchedValues -> List(
            //                      Map("name" -> List("John Doe"), "age" -> List("21")),
            //                      Map("name" -> List("Jane Doe"), "age" -> List("22"))
            //                     )
         }
    }
```
 
