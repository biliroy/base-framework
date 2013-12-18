
@echo off
echo [INFO] reset database data

cd %~dp0
cd ..
call mvn antrun:run
cd bin
pause
