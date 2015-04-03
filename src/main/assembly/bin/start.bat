@echo off & setlocal enabledelayedexpansion

set LIB_JARS=""
cd ..\lib
for %%i in (*) do set LIB_JARS=!LIB_JARS!;..\lib\%%i
cd ..\bin


java -Xms64m -Xmx1024m -XX:MaxPermSize=64M -classpath ../;%LIB_JARS% org.tangerine.test.Server
goto end

:end
pause