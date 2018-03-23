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
  Reader/
         ├── Extractor.scala       ; Handles the PDF parsing and JSON generation
         ├── ImageProcessing.scala ; Handles processing the image and extract its text
         ├── OpenNLP.scala         ; Handles the NLP (natural language processing) functionalities
         ├── POSTag.scala          ; Enum for the possible Part-Of-Speech tags to be used in when extracting values for keywords
         └── SpellChecker.scala    ; Handles the spellchecking operations to improve the OCR's accuracy
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
    import Extractor.readPDF
    
    val filePath = "./path/to/pdf/document"
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
    import Extractor.{readPDF, getJSONObjects}
    
    val filePath = "./path/to/pdf/document"
    val extractedText = readPDF(filePath)
    val keywords = List( ("name", POSTag.N), ("age", POSTag.NUM) )
    
    val jsonObjs : List[String] = getJSONObjects(extractedText, keywords)
```

This would return you a List of JSON objects in the form of Strings. Flipper also provides
a more in-depth API in case you want a List of keywords and the values found for them instead of a JSON object
which we will see next.
 
---

### Dependencies ### 

asdjaosd

 
