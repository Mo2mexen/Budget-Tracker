@echo off
cd /d "%~dp0src"
javac -cp ".;lib\mysql-connector-j-9.1.0.jar" Main.java database\*.java models\*.java
java -cp ".;lib\mysql-connector-j-9.1.0.jar" Main
