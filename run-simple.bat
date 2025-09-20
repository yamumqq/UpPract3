@echo off
set JAVA_HOME=C:\Users\admin\.jdks\corretto-22.0.2
set PATH=%JAVA_HOME%\bin;%PATH%

echo Starting Spring Boot application...
echo.

REM Stop any running Java processes
taskkill /f /im java.exe 2>nul

REM Run the application
java -cp "target/classes;target/dependency/*" com.example.project2.Project2Application

pause