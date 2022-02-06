@echo off
cd ../
mvn clean deploy -P release
pause
