@echo off
if not exist "lib" mkdir "lib"
bitsadmin /transfer dlaf /download /priority normal https://repo1.maven.org/maven2/com/formdev/flatlaf/3.2/flatlaf-3.2.jar "%~dp0lib\flatlaf-3.2.jar"
if exist "lib\flatlaf-3.2.jar" (
    echo SUCCESS
) else (
    echo FAILED
)
