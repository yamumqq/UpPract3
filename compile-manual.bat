@echo off
set JAVA_HOME=C:\Users\admin\.jdks\corretto-22.0.2
set PATH=%JAVA_HOME%\bin;%PATH%

echo Manual compilation...
echo.

REM Create directories
if not exist "target\classes" mkdir "target\classes"

REM Compile all Java files
echo Compiling Java files...
javac -parameters -cp "target/dependency/*" -d target/classes src/main/java/com/example/project2/*.java src/main/java/com/example/project2/controller/*.java src/main/java/com/example/project2/model/*.java src/main/java/com/example/project2/repository/*.java src/main/java/com/example/project2/service/*.java src/main/java/com/example/project2/config/*.java

if %ERRORLEVEL% EQU 0 (
    echo Compilation successful!
    echo.
    echo Starting application...
    java -cp "target/classes;target/dependency/*" com.example.project2.Project2Application
) else (
    echo Compilation failed!
    echo Please check the error messages above.
)

pause

