# Maven Template
Maven should be pre-installed on many unix variants like Ubuntu, Debian, Mac OS X. Installer for Windows can be found [here](http://maven.apache.org/download.cgi)
Simply copy this template folder for each new homework. Import the new (copied) folder as project in Eclipse or IntelliJ (see below) and start coding :D

## Command line (for teacher)
Compile and execute from command line with:
```
mvn -q  package exec:java
```

## IntelliJ
Import the project with:
 1. File -> Import Project
 2. Select **pom.xml** from the projects folder
 3. Create run configuration (if not detected automatically)
  1. Run -> Edit Configurations
  2. Click on the `+` button (left upper corner)
  3. Select maven
  4. Insert a name for this configuration (top - middle of the dialog)
  5. In _"Command Line:"_ insert `package exec:java`
  6. Running and debugging should now work

## Eclipse
 1. Check if maven plugin is already installed: Help -> Install new Software
 2. On the bottom right corner you will see _"What is already installed?"_ . Click on it and check if `m2e` is installed. If yes continue in step 4
 3. **If m2e is not installed:** 
  1. Close the _"What's Installed Window"_
  2. Continue in the previous _"Install Software"_ Window 
  3. For _work with_ Dropdown chose "All available sites"
  4. In _"Type Filter"_ insert "maven"
  5. Select _m2e_ and install this plugin
  6. restart eclipse
 4. File -> Import -> Maven
 5. Browse to the Project folder that contains the _pom.xml_ and click the Finish-Button
 6. Open Main.java in Eclipse and click on the Run Button. Running and debugging should work now

