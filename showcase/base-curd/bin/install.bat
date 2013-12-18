@echo off
echo [INFO] Install jar to local repository.

cd %~dp0
cd ..
call mvn clean source:jar install -Dmaven.test.skip=true
cd bin
pause