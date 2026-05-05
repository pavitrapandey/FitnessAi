@echo off
echo ========================================
echo Quick Start - Essential Services Only
echo ========================================
echo.
echo This will start:
echo - Config Server (8888)
echo - Eureka Server (8761)
echo - Activity Service (8081)
echo - Gateway (8080)
echo.

REM Check if Maven is available
where mvn >nul 2>&1
if errorlevel 1 (
    echo ERROR: Maven is not installed or not in PATH
    pause
    exit /b 1
)

echo Starting Config Server...
start "Config Server" cmd /k "cd configServer && mvn spring-boot:run"
timeout /t 20 /nobreak >nul

echo Starting Eureka Server...
start "Eureka Server" cmd /k "cd eureka && mvn spring-boot:run"
timeout /t 20 /nobreak >nul

echo Starting Activity Service...
start "Activity Service" cmd /k "cd activityService && mvn spring-boot:run"
timeout /t 15 /nobreak >nul

echo Starting Gateway...
start "Gateway" cmd /k "cd Gateway && mvn spring-boot:run"
timeout /t 20 /nobreak >nul

echo.
echo ========================================
echo Services started!
echo ========================================
echo.
echo Gateway: http://localhost:8080
echo Eureka: http://localhost:8761
echo.
echo Wait 1-2 minutes for all services to fully start.
echo Check the opened windows for any errors.
echo.
pause
