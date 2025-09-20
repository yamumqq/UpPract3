@echo off
set JAVA_HOME=C:\Users\admin\.jdks\corretto-22.0.2
set PATH=%JAVA_HOME%\bin;%PATH%

echo Compiling with -parameters flag...
javac -parameters -cp "target/dependency/*" -d target/classes src/main/java/com/example/project2/*.java src/main/java/com/example/project2/controller/*.java src/main/java/com/example/project2/model/*.java src/main/java/com/example/project2/repository/*.java src/main/java/com/example/project2/service/*.java src/main/java/com/example/project2/config/*.java

if %ERRORLEVEL% EQU 0 (
    echo Compilation completed successfully!
) else (
    echo Compilation failed!
)

pause
