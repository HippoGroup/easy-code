@echo off
cd ../
mvn -T 1C clean source:jar install -Dmaven.test.skip=false -Dmaven.javadoc.skip=false
pause
