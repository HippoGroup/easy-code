@echo off
echo ------------------------------------------------
echo ����Easy-Code�汾��������
echo 1. ����pom.xml�еİ汾��
echo ------------------------------------------------

echo �������°汾��
set /p versionNumber=

if "%versionNumber%"=="" (
echo "ERROR: �°汾�Ų����ڣ����������°汾��"
pause
exit
)

echo �滻����ģ��pom.xml�еİ汾
cd ../
mvn versions:set -DnewVersion="%versionNumber%"

pause
