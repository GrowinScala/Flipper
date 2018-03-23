# Reader

The present file documents the Reader module.
<br/>

### Table of contents ###

* [Main Features](#main-features)
* [Dependencies](#dependencies)
* [Main Methods](#main-methods)
 
 
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
 in this particular case would return `{"name" : <name that was found>}`.
 
* **Extracting text from images** - Using **_Tess4J_** and **_Scrimage_** Flipper is also able
to extract text from images applying an OCR (optical character recognition) with great accuracy in
 order to maximize the possibility of extracting useful information
 
 We will dive deeper in how you can go by doing this yourself on your project in a second.
 
---

### Main Methods ###

oaijs

 
---

### Dependencies ### 

asdjaosd

 
