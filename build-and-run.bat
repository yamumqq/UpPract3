@echo off
set JAVA_HOME=C:\Users\admin\.jdks\corretto-22.0.2
set PATH=%JAVA_HOME%\bin;%PATH%

echo Building and running Spring Boot application...
echo.

REM Stop any running Java processes
taskkill /f /im java.exe 2>nul

REM Clean and compile
echo Cleaning and compiling...
mvn clean compile -Dmaven.compiler.fork=true -Dmaven.compiler.executable="C:\Users\admin\.jdks\corretto-22.0.2\bin\javac.exe"

if %ERRORLEVEL% NEQ 0 (
    echo Compilation failed! Trying alternative approach...
    echo.
    
    REM Try to compile manually
    echo Compiling manually...
    if not exist "target\classes" mkdir "target\classes"
    
    javac -parameters -cp "target/dependency/*" -d target/classes src/main/java/com/example/project2/*.java src/main/java/com/example/project2/controller/*.java src/main/java/com/example/project2/model/*.java src/main/java/com/example/project2/repository/*.java src/main/java/com/example/project2/service/*.java src/main/java/com/example/project2/config/*.java
    
    if %ERRORLEVEL% NEQ 0 (
        echo Manual compilation also failed!
        echo Please use IntelliJ IDEA to run the application.
        pause
        exit /b 1
    )
)

echo.
echo Starting application...
java -cp "target/classes;target/dependency/*" com.example.project2.Project2Application

pause

