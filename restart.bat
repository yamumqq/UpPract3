@echo off
echo Stopping any running Java processes...
taskkill /f /im java.exe 2>nul

echo Waiting 3 seconds...
timeout /t 3 /nobreak >nul

echo Starting Spring Boot application...
set JAVA_HOME=C:\Users\admin\.jdks\corretto-22.0.2
set PATH=%JAVA_HOME%\bin;%PATH%

java -cp "target/classes;target/dependency/*" com.example.project2.Project2Application

pause
