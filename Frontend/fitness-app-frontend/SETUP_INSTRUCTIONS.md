# Fitness Tracker - Setup Instructions

## Current Status

✅ **Frontend**: Running on http://localhost:5173 with Tailwind CSS  
⚠️ **Backend API**: Running on port 8080 but returning 500 errors  
❌ **Keycloak Auth**: NOT running (should be on port 8181)

## The Problem

The 500 Internal Server Error you're seeing is because:
1. **Keycloak authentication server is not running** on port 8181
2. Without Keycloak, users cannot log in and get auth tokens
3. Without auth tokens, the backend API rejects requests

## How to Fix

### Step 1: Start Keycloak Server
```bash
# Navigate to your Keycloak directory and start it
# The exact command depends on your Keycloak setup
# Common commands:
./bin/kc.sh start-dev  # Linux/Mac
.\bin\kc.bat start-dev # Windows
```

Keycloak should start on: **http://localhost:8181**

### Step 2: Verify Backend is Running
Your backend should be running on: **http://localhost:8080**

If not, start your Spring Boot backend:
```bash
# Navigate to your backend directory
./mvnw spring-boot:run  # Linux/Mac
mvnw.cmd spring-boot:run # Windows
```

### Step 3: Test the Application

1. Open http://localhost:5173 in your browser
2. Click "Login to Get Started"
3. You should be redirected to Keycloak login
4. After login, you'll see the activity tracker interface

## Tailwind CSS Status

✅ Tailwind CSS v4 is properly installed and configured:
- Package: `tailwindcss@4.2.4` and `@tailwindcss/vite@4.2.4`
- Vite plugin configured in `vite.config.js`
- CSS import in `src/index.css`
- All components use Tailwind classes

If styles aren't showing:
1. Hard refresh your browser (Ctrl+Shift+R or Cmd+Shift+R)
2. Clear browser cache
3. Check browser console for any CSS loading errors

## Architecture

```
┌─────────────────┐
│   Frontend      │
│  localhost:5173 │ ← You are here
└────────┬────────┘
         │
         ├─→ Keycloak (Auth)
         │   localhost:8181  ❌ NOT RUNNING
         │
         └─→ Backend API
             localhost:8080   ⚠️ Running but failing
```

## Quick Checklist

- [ ] Keycloak is running on port 8181
- [ ] Backend API is running on port 8080
- [ ] Frontend dev server is running on port 5173
- [ ] Browser is open to http://localhost:5173
- [ ] No CORS errors in browser console

## Common Issues

### "WebSocket connection failed"
- This is normal if dev server isn't running
- Run `npm run dev` to start the frontend

### "500 Internal Server Error"
- Keycloak is not running
- Backend can't validate auth tokens
- Start Keycloak first

### "401 Unauthorized"
- You're not logged in
- Click the login button
- Make sure Keycloak is running

### Tailwind styles not showing
- Hard refresh browser (Ctrl+Shift+R)
- Check that `@import "tailwindcss";` is in src/index.css
- Verify Vite dev server restarted after Tailwind installation

## Need Help?

Check the browser console (F12) for detailed error messages.
