# Pokemon Kanto Adventure

Welcome to the Pokemon Kanto Adventure! This guide will help you open and run the `Pokemon_Kanto_Adventure.jar` file.

## Requirements:

Before you start, make sure you have the following installed on your computer:

1. **Java Runtime Environment (JRE)**: Version 8 or later.
   - You can download it from the [official Java website](https://www.oracle.com/java/technologies/javase-downloads.html) based on your OS like Mac or Window.

## Steps to Run the JAR File:

1. **Download the JAR File**: Ensure you have the `Pokemon_Kanto_Adventure.jar` file saved on your computer. You can find it in the `out/artifacts/Pokemon_Kanto_Adventure_jar/` directory.

2. **Open a Command Prompt or Terminal**:
   - **Windows**: Press `Win + R`, type `cmd`, and press `Enter`.
   - **Mac/Linux**: Open the Terminal application.

3. **Navigate to the Directory**: Change the directory to where the JAR file is located. For example, if your JAR file is in `C:\Users\YourUsername\Downloads\`,
   you would use the following command by open your `terminal` or `Git Bash`:
   ```bash
   cd C:\Users\YourUsername\Downloads\
   ```
   It is recommend to open the terminal in the directory wheren the JAR file is located by right click at the blank and choose the option `Open in Terminal`.

4. **Run the JAR file**: Use the following command to run the JAR file:
   ```bash
   java -jar [JAR FILE NAME]
   ```
   For example, you can just copy the following command or copy the filepath of JAR file and make the directory in terminal is correct.
   ```bash
   java -jar Pokemon_Kanto_Adventure.jar
   ```
   If everything is set up correctly, the game should start running.

## Troubleshooting:

1. **Command Not Found**: If you get an error saying java: command not found, it means Java is not installed or not added to your system's PATH.
   Follow the installation instructions on the Java website and make sure to restart your Command Prompt or Terminal after installation.

2. **Permission Denied**: If you encounter a permission error, try running the Command Prompt or Terminal as an administrator (on Windows) or use sudo on Mac/Linux:
   ```bash
   sudo java -jar Pokemon_Kanto_Adventure.jar
   ```

3. **JAR File Not Found**: Ensure that you have navigated to the correct directory where the JAR file is located. Double-check the file path.

Wish you all have fun for it!!!
