@echo off
echo [INFO] archetype create from project

if not exist generated-sources (md generated-sources)

cd generated-sources

call mvn archetype:generate -DarchetypeCatalog=local -DarchetypeGroupId=org.exitsoft.showcase -DarchetypeArtifactId=exitsoft-basic-curd-archetype -DarchetypeVersion=1.3.0-SNAPSHOT

cd ..

pause