@echo off
echo [INFO] create project to idea

cd %~dp0
cd ..
call mvn clean idea:idea
cd bin
pause