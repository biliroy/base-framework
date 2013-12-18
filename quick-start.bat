@echo off

set base_path=%cd%

echo [INFO] install jar to local m2 repository.

call mvn clean source:jar install -Dmaven.test.skip=true

echo [INFO] create basic curd project archetype.

cd %base_path%\showcase\basic-curd
call mvn archetype:create-from-project

echo [INFO] install basic curd archetype to local m2 repository

cd %base_path%\showcase\basic-curd\target\generated-sources\archetype
call mvn clean install -Dmaven.test.skip=true

cd %base_path%\showcase\basic-curd
rd /S /Q target

echo [INFO] init h2 data

call mvn antrun:run

echo [INFO] start basic curd app

start "basic curd" mvn clean jetty:run

echo [INFO] Please wait a moment. When you see "[INFO] Started Jetty Server", you can visit: http://localhost:8080/exitsoft-basic-curd/ to view the demo

cd %base_path%

pause