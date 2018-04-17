# Flipper
### What is Flipper? ###

Flipper is an open-source PDF library written in Scala and that can be integrated in any Java/Scala environment 
developed by the good people at Growin. It has some really usefull features such as: 
* **Parsing a PDF document and returning a JSON object** - Flipper is able to parse the text in a PDF document,
 as well as recognize text in images inside the PDF document, and return a JSON object with the extracted information. 
 You simply specify the type of value you want to obtain for a given keyword (A noun, a verb, a number etc.), and 
 Flipper will do the rest!
 
 * **Convert JSON to PDF** - Flipper does not content itself with just parsing a PDF file, that's easy!
 Flipper also converts a given JSON object to a PDF document. You can also customize the outputted document
 with CSS.
 
 * **Convert PDF to other file types** - We also support the conversion from PDF to other popular
 formats: .png; .jpeg/jpg; .gif; .odt.
 
 Current version: 0.2
 
 ---
 
### Project structure ###

Flipper is divided into 3 different modules that can be used individually: **Reader**, **Generator** and **Converter**.


```
Flipper/
        ├── converter ; PDF to other file types module
        ├── generator ; JSON to PDF module
        ├── reader    ; PDF parser to JSON
        └── build.sbt ; Project config file
```

You can find the individual **README.md** files with examples and documentation here:
* [Reader](./reader/README.md)
* [Generator](./generator/README.md)
* [Converter](./converter/README.md)
 
 
---


### How do I get set up? ###

This part of the documentation will guide you on the simple process of setting up Flipper for yourself.

* [Configuration](#configuration)
* [Dependencies](#dependencies)
* [How to test Flipper](#how-to-test-flipper)
* [Deployment instructions](#deployment-instructions)
<br/>

### Configuration

Flipper is available on maven central, so to use it you simply need to add the lines bellow to your own project.

If you are using SBT:

```scala
libraryDependencies += "com.growin" %% "flipper" % "0.2"
```

Or Maven:

```xml
<dependency>
    <groupId>com.growin</groupId>
    <artifactId>flipper_2.12</artifactId>
    <version>0.2</version>
</dependency>
```

For the most current version can access the [maven repository](https://mvnrepository.com/artifact/com.growin/flipper_2.12). There you will also find other
ways of including Flipper into your project that aren't  SBT or Maven
<br/>

### Dependencies

Download the **eng.traineddata** and **por.traineddata** from [here](https://github.com/tesseract-ocr/tessdata) and
insert them in a directory named **tessdata** in the **root** of the project.

Flipper uses **_[Tess4j](http://tess4j.sourceforge.net/)_** (a tesseract for java wrapper) to extract
text from images (using an algorithm known as _optical character recognition_). In order to improve this algorithms
accuracy, we must provide Tess4j with a set of **training data**.

<br/>

### How to test Flipper

To be implemented

<br/>

### Deployment instructions

To be implemented

<br/>
 
---

### Who do I talk to? ###

Flipper is an Open-Source project developed at Growin in our offices in Lisbon.
 <br/> If you have any questions, you can contact:
 
 * Valter Fernandes  &nbsp; &nbsp;- vfernandes@growin.pt
 * Margarida Reis   &nbsp; &nbsp; &nbsp; - mreis@growin.pt
 * Lucas Fischer    &nbsp; &nbsp; &nbsp; &nbsp;&nbsp; - lfischer@growin.pt

Or visit our website: [www.growin.com](https://www.growin.com/)