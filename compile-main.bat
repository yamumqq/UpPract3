@echo off
set JAVA_HOME=C:\Program Files\Java\jdk-17
set PATH=%JAVA_HOME%\bin;%PATH%

echo Compiling main application class...
javac -cp "target/dependency/*" -d target/classes src/main/java/com/example/project2/Project2Application.java

if %ERRORLEVEL% EQU 0 (
    echo Compilation successful!
    echo Starting application...
    java -cp "target/classes;target/dependency/*" com.example.project2.Project2Application
) else (
    echo Compilation failed!
    pause
)
