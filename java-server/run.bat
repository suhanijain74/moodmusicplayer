@echo off
setlocal
echo =================================================
echo MOODSYNC JAVA ENGINE (Web Version)
echo =================================================
echo.
echo [1/2] Compiling Engine...
if not exist bin mkdir bin
javac -d bin src\*.java
if %errorlevel% neq 0 (
    echo [ERROR] Compilation failed!
    pause
    exit /b %errorlevel%
)

echo [2/2] Launching Engine...
echo.
echo -------------------------------------------------
echo OPEN YOUR BROWSER TO: http://localhost:9090
echo -------------------------------------------------
echo.
java -cp bin MoodSyncServer
if %errorlevel% neq 0 (
    echo [ERROR] Server failed to start!
)
pause
