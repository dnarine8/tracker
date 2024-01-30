echo off

REM  **************** program arguments *****************
set LOGGING_CONFIG=logging.forensics.properties
set MAIN_CLASS=com.cobra.forensics.app.Main
set CLASSPATH=forensics-1.0.jar
set VMARGS=-Djava.util.logging.config.file=%LOGGING_CONFIG%

REM **************************** main function *******************************
IF "%JAVA_HOME%"=="" (
  ECHO JAVA_HOME is NOT defined
  ECHO Please set JAVA_HOME to the home directory of java 11 or above and retry
  goto :EOF
)

set JAVA="%JAVA_HOME%\bin\java"

echo log config=%LOGGING_CONFIG%
echo main class=%MAIN_CLASS%
echo classpath=%CLASSPATH%
echo vmargs=%VMARGS%
echo java=%JAVA%

%JAVA% %VMARGS% -cp %CLASSPATH% %MAIN_CLASS% inv scripts $1

:EOF
