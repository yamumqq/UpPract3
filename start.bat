@echo off
set JAVA_HOME=C:\Users\admin\.jdks\corretto-22.0.2
set PATH=%JAVA_HOME%\bin;%PATH%

echo Starting Spring Boot application...
echo.

java -cp "target/classes;target/dependency/*" com.example.project2.Project2Application

echo.
echo Application stopped.
pause

