@echo off
echo Downloading FlatLaf...
if not exist "lib" mkdir "lib"
powershell -Command "Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/com/formdev/flatlaf/3.2/flatlaf-3.2.jar' -OutFile 'lib/flatlaf-3.2.jar'"
if exist "lib\flatlaf-3.2.jar" (
    echo [OK] FlatLaf downloaded successfully!
) else (
    echo [ERROR] Download failed.
)
