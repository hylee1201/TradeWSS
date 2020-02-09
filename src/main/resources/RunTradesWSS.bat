@echo off
SET JPATH=C:\CVR\TradesWSS
SET THIS=%JPATH%\RunTradesWSS.bat
SET LPATH=C:\CVR\logs
SET LOGFILE_DATE=%DATE:~10,4%-%DATE:~4,2%-%DATE:~7,2%
SET LOGFILE=%LPATH%\TradesWSS%LOGFILE_DATE%.log

rem SET JPATH=C:\CVR_Standalones\VsTradeMMB
echo ======================================= >> %LOGFILE%
echo Starting File Upload at: >> %LOGFILE%
java -version >> %LOGFILE%
date /T >> %LOGFILE%
time /T >> %LOGFILE%
hostname >> %LOGFILE%

javaw -cp .;%JPATH%\*;%JPATH%;%JPATH%\lib\* -Dlog4j.configuration=file:\%JPATH%\log4j-TradesWSS.properties com.tdsecurities.cvr.batch.wss.WSSBatch %THIS% >> %LOGFILE%
SET errLev=%Errorlevel%
verify > nul

echo Exiting with code %errLev% >> %LOGFILE%
exit %errLev%