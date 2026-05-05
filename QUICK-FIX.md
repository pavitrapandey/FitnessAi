# Quick Fix for OAuth2 404 Error

## Your Error
```
GET http://localhost:8181/realms/fitness-oauth2/protocol/openid-connect/auth?client_id=oauth2-pkce-client&response_type=code&redirect_uri=http://localhost:5173&scope=openid 404 (Not Found)
```

## Root Cause
Keycloak OAuth2 server is not running or not configured.

## Quick Solution (5 minutes)

### Step 1: Start Keycloak
```bash
# Windows
start-infrastructure.bat

# Linux/Mac
./start-infrastructure.sh
```

Or manually:
```bash
docker-compose up -d keycloak
```

### Step 2: Wait for Keycloak to Start
```bash
# Watch logs until you see "Keycloak started"
docker logs -f fitu-keycloak
```

Wait for this message (30-60 seconds):
```
Listening on: http://0.0.0.0:8181
```

Press `Ctrl+C` to exit logs.

### Step 3: Configure Keycloak (First Time Only)

1. **Open Keycloak Admin Console**
   - URL: http://localhost:8181
   - Username: `admin`
   - Password: `admin`

2. **Create Realm**
   - Click dropdown (top-left, shows "master")
   - Click "Create Realm"
   - Name: `fitness-oauth2`
   - Click "Create"

3. **Create Client**
   - Go to "Clients" → "Create client"
   - Client ID: `oauth2-pkce-client`
   - Click "Next"
   - Client authentication: **OFF**
   - Standard flow: **ON**
   - Direct access grants: **ON**
   - Click "Next"
   - Valid redirect URIs: `http://localhost:5173/*`
   - Web origins: `http://localhost:5173`
   - Click "Save"

4. **Enable PKCE**
   - In client settings, scroll to "Advanced Settings"
   - Proof Key for Code Exchange Code Challenge Method: `S256`
   - Click "Save"

5. **Create Test User**
   - Go to "Users" → "Add user"
   - Username: `testuser`
   - Email: `testuser@fitu.com`
   - First name: `Test`
   - Last name: `User`
   - Email verified: **ON**
   - Click "Create"
   - Go to "Credentials" tab
   - Click "Set password"
   - Password: `Test@123`
   - Temporary: **OFF**
   - Click "Save"

### Step 4: Fix Gateway Configuration

The Gateway has wrong realm name. I've already fixed this in the code, but you need to restart:

```bash
# Restart Config Server
cd configServer
mvn spring-boot:run

# In another terminal, restart Gateway
cd Gateway
mvn spring-boot:run
```

### Step 5: Test

1. Open frontend: http://localhost:5173
2. Click login
3. Should redirect to Keycloak
4. Login with `testuser` / `Test@123`
5. Should redirect back to frontend with token

## Verification

Test the auth endpoint manually:
```bash
curl http://localhost:8181/realms/fitness-oauth2/protocol/openid-connect/auth?client_id=oauth2-pkce-client&response_type=code&redirect_uri=http://localhost:5173&scope=openid
```

Should return HTML login page (not 404).

## Still Not Working?

### Check Keycloak is Running
```bash
docker ps | grep keycloak
```

Should show:
```
fitu-keycloak   Up X minutes   0.0.0.0:8181->8181/tcp
```

### Check Keycloak Logs
```bash
docker logs fitu-keycloak --tail 50
```

Look for errors.

### Verify Realm Exists
1. Open http://localhost:8181
2. Login with admin/admin
3. Check realm dropdown shows "fitness-oauth2"

### Complete Reset
```bash
docker-compose down -v
docker-compose up -d keycloak
# Wait 60 seconds
# Repeat Step 3 above
```

## Need More Help?

See detailed guides:
- [keycloak-setup.md](keycloak-setup.md) - Complete Keycloak setup
- [TROUBLESHOOTING.md](TROUBLESHOOTING.md) - All common issues
- [README.md](README.md) - Full documentation
