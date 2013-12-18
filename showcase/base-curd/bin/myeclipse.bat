
@echo off
echo [INFO] create project to myeclipse

cd %~dp0
cd ..
call mvn clean eclipse:myeclipse
cd bin
pause
