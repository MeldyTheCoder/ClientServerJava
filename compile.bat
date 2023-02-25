@echo off

set production_path=%cd%\out\production\ClientServer
set src_path=%cd%\src

echo Production path: %production_path%
echo Source path: %src_path%

javac -d %production_path% %src_path%\*.java
pause