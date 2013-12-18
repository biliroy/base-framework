@echo off
echo [INFO] create a project.

cd %~dp0
cd ..
call mvn eclipse:eclipse
cd bin
pause