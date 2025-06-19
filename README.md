# Moodle->PDF converter
## Description
This is an application that converts a Moodle .mbz file into an interactive PDF file. 
## Usage
### Prerequisites
- Java version 21 (21.0.3 was used in development)
- Maven
### Installation
Run `mvn clean install` in the root folder to compile.
This application has 2 different versions: 
1. A console version, which takes in 2 arguments: 
   path to the backup file and path to the folder in which the resulting file should be saved
   (the second argument could be omitted to save to the same folder the backup file is in);
   Launched with `java -cp target/moodle_to_pdf-1.0.jar by.sentencija.ConsoleMain`;
2. A UI version; Launched with `java -cp target/moodle_to_pdf-1.0.jar by.sentencija.Main`.