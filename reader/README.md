# Reader

The present file documents the Reader module.
<br/>

### Table of contents ###

* [Module Structure](#module-structure)
* [Main Features](#main-features)
* [Main Methods / Examples](#main-methods-/-examples)
* [Dependencies](#dependencies)
 
  
---
 
### Module Structure ### 
 
  ```
  reader/
         ├── java/parser/extraction/
         |                          └── ExtractorJava.java     ; Java interface for the API
         └── scala/parser/
                         ├── extraction/
                         |              ├── Extractor.scala    ; Handles the PDF parsing and JSON generation
                         |              └── FileHandler.scala  ; Handles the file inputs
                         └── utils/                
                                   ├── ImageProcessing.scala   ; Handles processing the image and extract its text
                                   ├── OpenNLP.scala           ; Handles the NLP (natural language processing) functionalities
                                   ├── POSTag.scala            ; Enum for the possible Part-Of-Speech tags to be used in when extracting values for keywords
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

In this section we will show some examples of the features above as well as a short API documentation for this module
in order for you to find what you need.
<br/>

* #### Extracting text from the PDF doc and its images ####

To extract the text from a PDF document using Flipper simply pass the document path
to **`readPDF`** (found in **Extractor.scala**).

```scala
    import parser.extraction.Extractor._
    import java.io.File
    
    val filePath = new File("./path/to/pdf/document")
    val extractedText = readPDF(filePath)
```

You now have the have the extracted text, wrapped in an `Option[String]` to prevent `null`'s
in case the file does not exist.

* #### Parsing PDF and returning a List of JSON Objects #### 

The most straight-forward way to use this module's API is to call `getJSONObjects`. You need
to supply this method with the **text** you want to extract data from and a **List of keywords** for which
you want to obtain values.

<br/>

The keywords list is a pair of Keywords and a POSTag.Value, this is because Flipper is using a 
Natural Language Processor (**_Apache's OpenNLP_**) for improving the odds of finding a useful 
value for a given keyword. This POS tag simply tells Flipper what kinda of value you want to obtain
for a given keyword, the possible POSTags can be found bellow:


| POS Tag        | Meaning                |
|:--------------:|:----------------------:|
| ADJ            | Adjective              |
| PN             | Proper Noun            |
| N              | Noun                   |
| NPLR           | Plural Noun            |
| VB             | Verb - Base Form       |
| VBN            | Verb - Past Participle |
| VBG            | Verb - Gerund          |
| NUM            | Number                 |
| ADV            | Adverb                 |


You can now implement the following snippet:



```scala
    import parser.extraction.Extractor.{readPDF, getJSONObjects}
    import java.io.File
    import parser.utils.POSTag
    
    val file = new File("./path/to/pdf/document")
    val extractedText = readPDF(file)
    val keywords = List( ("name", POSTag.N), ("age", POSTag.NUM) )
    
    val jsonObjs : List[String] = getJSONObjects(extractedText, keywords)
    
    //jsonObjs -> List( "{ "name" : "John Doe" , "age" : 21 }" , "{ "name" : "Jane Doe" , "age" : 22 }" )
```

You can also send an optional flag to `getJSONObjects` specifying how you want the JSON to be outputed when a keyword has no value:

Possibilities:

* "empty" (Default) &nbsp; &nbsp;- `{ "name" : "John Doe", "age": "" }`
* "null" &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; - `{ "name" : "John Doe", "age": null }`
* "remove" &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; - `{ "name" : "John Doe" }`

This would return you a List of JSON objects in the form of Strings. Flipper also provides
a more in-depth API in case you want a List of keywords and the values found for them instead of a JSON object
which we will see next.

* #### Getting a List of keywords and all the values found for them ####

In case you want to obtain a List Keywords with all the values found for that keyword, Flipper provides you
with that possibility through **`getAllMatchedValues`**.

```scala
    import parser.extraction.Extractor.{readPDF, getAllMatchedValues}
    import java.io.File
    import parser.utils.POSTag
    
    val file = new File("./path/to/pdf/document")
    val extractedText = readPDF(file)
    val keywords = List( ("name", POSTag.N), ("age", POSTag.NUM) )
    
    val matchedValues = getAllMatchedValues(extractedText, keywords) 
    //matchedValues -> List( ("name", List("John Doe", "Jane Doe")), ("age", List("21", "22") )
```

* #### Getting just a single value for each keyword ####

This method works exactly like the one above but instead of returning every value found for a keyword, returns only one.

```scala
    import parser.extraction.Extractor.{readPDF, getSingleMatchedValue}
    import java.io.File
    import parser.utils.POSTag
    
    val file = new File("./path/to/pdf/document")
    val extractedText = readPDF(file)
    val keywords = List( ("name", POSTag.N), ("age", POSTag.NUM) )
    
    val matchedValues = getSingleMatchedValue(extractedText, keywords) 
    //matchedValues -> List( ("name", List("John Doe")), ("age", List("21") )
```

* #### Getting all possible pre-JSON objects for the values found ####

This method returns a List containing all the possible pre-JSOn objects for the values found for 
the given keywords

```scala
    import parser.extraction.Extractor.{readPDF, getAllObjects}
    import java.io.File
    import parser.utils.POSTag
    
    val filePath = new File("./path/to/pdf/document")
    val extractedText = readPDF(filePath)
    val keywords = List( ("name", POSTag.N), ("age", POSTag.NUM) )
    
    val matchedValues = getAllObjects(extractedText, keywords) 
    //matchedValues -> List( List(("name", List("John Doe")), ("age", List("21")), List( ("name", "Jane Doe"), ("age", List("22")) ))
```
 
---

### Dependencies ### 

To be implemented
 
