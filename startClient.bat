@echo off

set production_path=%cd%\out\production\ClientServer

echo Production path: %production_path%

cd %production_path%
cls
java User
pause