
@echo off
echo [INFO] create project to eclipse

cd %~dp0
cd ..
call mvn clean eclipse:eclipse
cd bin
pause
