@echo off
REM Force clean compilation
cd /d "%~dp0"
if exist bin\gui rmdir /s /q bin\gui
cd src
javac -cp ".;lib\mysql-connector-j-9.1.0.jar;lib\javafx-sdk-23.0.1\lib\*" -d ..\bin models\*.java database\*.java utils\*.java gui\*.java
java --module-path "lib\javafx-sdk-23.0.1\lib" --add-modules javafx.controls -cp "..\bin;lib\mysql-connector-j-9.1.0.jar" gui.BudgetTrackerApp
