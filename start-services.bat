@echo off
echo ========================================
echo Starting FitU Microservices
echo ========================================
echo.

REM Check if Maven is available
where mvn >nul 2>&1
if errorlevel 1 (
    echo ERROR: Maven is not installed or not in PATH
    echo Please install Maven and try again
    pause
    exit /b 1
)

echo Step 1: Starting Config Server (Port 8888)...
start "Config Server" cmd /k "cd configServer && mvn spring-boot:run"
echo Waiting 20 seconds for Config Server to start...
timeout /t 20 /nobreak >nul

echo.
echo Step 2: Starting Eureka Server (Port 8761)...
start "Eureka Server" cmd /k "cd eureka && mvn spring-boot:run"
echo Waiting 20 seconds for Eureka Server to start...
timeout /t 20 /nobreak >nul

echo.
echo Step 3: Starting User Service (Port 8083)...
start "User Service" cmd /k "cd userService && mvn spring-boot:run"
echo Waiting 15 seconds...
timeout /t 15 /nobreak >nul

echo.
echo Step 4: Starting Activity Service (Port 8081)...
start "Activity Service" cmd /k "cd activityService && mvn spring-boot:run"
echo Waiting 15 seconds...
timeout /t 15 /nobreak >nul

echo.
echo Step 5: Starting AI Service (Port 8082)...
echo NOTE: Make sure GEMINI_API_KEY and GEMINI_API_URL are set!
start "AI Service" cmd /k "cd aiService && mvn spring-boot:run"
echo Waiting 15 seconds...
timeout /t 15 /nobreak >nul

echo.
echo Step 6: Starting Gateway (Port 8080)...
start "Gateway" cmd /k "cd Gateway && mvn spring-boot:run"
echo Waiting 20 seconds for Gateway to start...
timeout /t 20 /nobreak >nul

echo.
echo ========================================
echo All services started!
echo ========================================
echo.
echo Service URLs:
echo - Config Server: http://localhost:8888
echo - Eureka Dashboard: http://localhost:8761
echo - User Service: http://localhost:8083
echo - Activity Service: http://localhost:8081
echo - AI Service: http://localhost:8082
echo - Gateway: http://localhost:8080
echo.
echo Check each window for startup logs and errors.
echo.
pause
