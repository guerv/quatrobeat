# Cloning and Running QuatroBeat
*These instructions are for...*
- **Windows** machines
- Running on the **VSCode** IDE

**NOTE:** `quatrobeat` has another branch (`vscode_config`) with my VSCode config files for Java and JavaFX. However, these are configured to my JDK and JavaFX SDK versions and absolute paths. You can clone that branch and use this guide to know where to change these paths. 

--- 

## 1. Install Prerequisites
1. **Install JDK from [Oracle](https://www.oracle.com/java/technologies/downloads/)**
- Recommended to download version 17 or later

2. **Set the `JAVA_HOME` environment variable**
- Find JDK installation path (ex. `C:\Program Files\Java\jdk-21`)
- Under your Environment Variables, create a New System Variable
  - Variable Name: `JAVA_HOME`
  - Value: *Your JDK installation path*
 
3. **Update `PATH` variable** - *Can update the User Variables OR the System Variables*
- Under your Environment Variables, find the `PATH` variable and Edit it
- Add new entry: `%JAVA_HOME%\bin`

4. **Verify `JAVA_HOME` is set through the terminal**
```bash
echo $JAVA_HOME
java -version
```
- Will show JDK path and version info respectively


5. **Install JavaFX SDK from [Gluon](https://gluonhq.com/products/javafx/)**
- Recommended to download version 17 or later
- Extract the ZIP
  - *Recommended:* Extract the ZIP under the same directory where JDK is installed
    - ex. If JDK is installed `C:\Program Files\Java\jdk-21`, extract under `C:\Program Files\Java`


6. **Install VSCode Extensions**
- Extension Pack for Java
- JavaFX Support

## 2. Clone QuatroBeat's Repo 
- Navigate to the directory you want to store the project then `git clone`

## 3. Open the Project in VSCode

## 4. Configure JavaFX in VSCode 
1. **Create a `.vscode` directory**
2. **Under this directory, create a `launch.json` file**
```json
{
    "version": "0.2.0",
    "configurations": [
        {
            "type": "java",
            "name": "Launch QuatroBeat",
            "request": "launch",
            "mainClass": "application.Main",
            "projectName": "QuatroBeat",
            "vmArgs": "--module-path \"/YOUR/ACTUAL/PATH/TO/javafx-sdk-21/lib\" --add-modules javafx.controls,javafx.fxml,javafx.media,javafx.graphics"
        }
    ]
}
```
- Change `/YOUR/ACTUAL/PATH/TO/javafx-sdk-21` to your path to your absolute path to your JavaFX SDK directory, with its appropriate version
  - ex. `C:/Program Files/Java/javafx-sdk-21.0.7/lib` for JavaFX SDK version 21

## 5. Run QuatroBeat
1. **Open Run and Debug view in VSCode (Ctrl+Shift+D)**
2. **Select "Launch QuatroBeat" configuration**
3. **Run by pressing F5 or clicking the green play button**

## 6. (Optional) Fixing JavaFX Project Classpath Errors
The project should run, but you will likely notice that the project in VSCode will show many errors. To fix this, do the following. 

1. **Under `.vscode`, create/update `settings.json` with your installed JDK paths**
```json
{
    "java.configuration.updateBuildConfiguration": "automatic",
    "java.project.sourcePaths": ["src"],
    "java.project.outputPath": "bin",
    "java.project.referencedLibraries": [
        "lib/**/*.jar",
        "PATH_TO_YOUR_JAVAFX_SDK/lib/*.jar"
    ],
    "java.jdt.ls.java.home": "PATH_TO_YOUR_JDK"
}
```
- Replace `PATH_TO_YOUR_JAVAFX_SDK` with your JavaFX SDK absolute path 
- Replace `PATH_TO_YOUR_JDK` with your JDK absolute path 

2. **Update the `.classpath` file under the project**
This will override the previous Eclipse `.classpath` file
```xml
<?xml version="1.0" encoding="UTF-8"?>
<classpath>
    <classpathentry kind="src" path="src"/>
    <classpathentry kind="con" path="org.eclipse.jdt.launching.JRE_CONTAINER">
        <attributes>
            <attribute name="module" value="true"/>
        </attributes>
    </classpathentry>
    <classpathentry kind="lib" path="PATH_TO_YOUR_JAVAFX_SDK/lib/javafx.base.jar"/>
    <classpathentry kind="lib" path="PATH_TO_YOUR_JAVAFX_SDK/lib/javafx.controls.jar"/>
    <classpathentry kind="lib" path="PATH_TO_YOUR_JAVAFX_SDK/lib/javafx.fxml.jar"/>
    <classpathentry kind="lib" path="PATH_TO_YOUR_JAVAFX_SDK/lib/javafx.graphics.jar"/>
    <classpathentry kind="lib" path="PATH_TO_YOUR_JAVAFX_SDK/lib/javafx.media.jar"/>
    <classpathentry kind="lib" path="PATH_TO_YOUR_JAVAFX_SDK/lib/javafx.swing.jar"/>
    <classpathentry kind="lib" path="PATH_TO_YOUR_JAVAFX_SDK/lib/javafx.web.jar"/>
    <classpathentry kind="output" path="bin"/>
</classpath>
```
- Replace `PATH_TO_YOUR_JAVAFX_SDK` with your JavaFX SDK absolute path 

3. **Reload VSCode with new Java Config**
- Under the Command Palette (Ctrl+Shift+P), select _Java: Clean Java Language Server Workspace_
- Click the "Reload and Delete" prompt
