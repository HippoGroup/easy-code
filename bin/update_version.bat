@echo off
echo ------------------------------------------------
echo 升级Easy-Code版本，包括：
echo 1. 升级pom.xml中的版本号
echo ------------------------------------------------

echo 请输入新版本号
set /p versionNumber=

if "%versionNumber%"=="" (
echo "ERROR: 新版本号不存在，必须输入新版本号"
pause
exit
)

echo 替换所有模块pom.xml中的版本
cd ../
mvn versions:set -DnewVersion="%versionNumber%"

pause
