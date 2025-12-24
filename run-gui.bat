@echo off
cd /d "%~dp0src"
javac -cp ".;lib\mysql-connector-j-9.1.0.jar;lib\javafx-sdk-23.0.1\lib\*" -d ..\bin models\*.java database\*.java utils\*.java gui\*.java 2>nul
java --module-path "lib\javafx-sdk-23.0.1\lib" --add-modules javafx.controls -cp "..\bin;lib\mysql-connector-j-9.1.0.jar" gui.BudgetTrackerApp
