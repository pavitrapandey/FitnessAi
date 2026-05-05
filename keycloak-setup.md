# Keycloak OAuth2 Setup Guide for FitU

This guide will help you set up Keycloak for OAuth2 authentication in the FitU application.

## Quick Start with Docker

### 1. Start Keycloak
```bash
docker-compose up -d keycloak keycloak-db
```

Wait for Keycloak to start (about 30-60 seconds). Check logs:
```bash
docker logs -f fitu-keycloak
```

### 2. Access Keycloak Admin Console
- URL: http://localhost:8181
- Username: `admin`
- Password: `admin`

## Keycloak Configuration

### Step 1: Create Realm

1. Click on the dropdown in the top-left corner (currently showing "master")
2. Click **"Create Realm"**
3. Enter realm name: `fitness-oauth2`
4. Click **"Create"**

### Step 2: Create Client

1. In the `fitness-oauth2` realm, go to **Clients** → **Create client**
2. Configure the client:
   - **Client type**: OpenID Connect
   - **Client ID**: `oauth2-pkce-client`
   - Click **Next**

3. **Capability config**:
   - ✅ Client authentication: **OFF** (public client)
   - ✅ Authorization: **OFF**
   - ✅ Standard flow: **ON**
   - ✅ Direct access grants: **ON**
   - ✅ Implicit flow: **OFF**
   - Click **Next**

4. **Login settings**:
   - **Valid redirect URIs**: 
     - `http://localhost:5173/*`
     - `http://localhost:5173`
   - **Valid post logout redirect URIs**: 
     - `http://localhost:5173/*`
   - **Web origins**: 
     - `http://localhost:5173`
     - `+` (to allow CORS from redirect URIs)
   - Click **Save**

### Step 3: Configure Client Settings

1. Go to **Clients** → `oauth2-pkce-client` → **Settings** tab
2. Scroll down to **Advanced Settings**:
   - **Proof Key for Code Exchange Code Challenge Method**: `S256`
   - Click **Save**

### Step 4: Create Test User

1. Go to **Users** → **Add user**
2. Fill in the details:
   - **Username**: `testuser`
   - **Email**: `testuser@fitu.com`
   - **First name**: `Test`
   - **Last name**: `User`
   - **Email verified**: **ON**
   - Click **Create**

3. Set password:
   - Go to **Credentials** tab
   - Click **Set password**
   - **Password**: `Test@123`
   - **Temporary**: **OFF**
   - Click **Save**

### Step 5: Configure Client Scopes (Optional but Recommended)

1. Go to **Client scopes** → **Create client scope**
2. Create the following scopes if not present:
   - `profile`
   - `email`
   - `offline_access`

3. Go back to **Clients** → `oauth2-pkce-client` → **Client scopes** tab
4. Add the scopes to **Assigned default client scopes**

## Verify Configuration

### Test Endpoints

1. **Authorization Endpoint**:
   ```
   http://localhost:8181/realms/fitness-oauth2/protocol/openid-connect/auth
   ```

2. **Token Endpoint**:
   ```
   http://localhost:8181/realms/fitness-oauth2/protocol/openid-connect/token
   ```

3. **JWK Set URI** (for Gateway):
   ```
   http://localhost:8181/realms/fitness-oauth2/protocol/openid-connect/certs
   ```

### Test Authentication Flow

You can test the authentication using curl:

```bash
# Get authorization code (open in browser)
http://localhost:8181/realms/fitness-oauth2/protocol/openid-connect/auth?client_id=oauth2-pkce-client&response_type=code&redirect_uri=http://localhost:5173&scope=openid%20profile%20email
```

## Configuration Issue Fix

### Update Gateway Configuration

The gateway configuration has a mismatch. Update the file:

**File**: `configServer/src/main/resources/config/gateway-service.properties`

Change:
```properties
spring.security.oauth2.resourceserver.jwt.jwk-set-uri=http://localhost:8181/realms/fitness-app/protocol/openid-connect/certs
```

To:
```properties
spring.security.oauth2.resourceserver.jwt.jwk-set-uri=http://localhost:8181/realms/fitness-oauth2/protocol/openid-connect/certs
```

## Troubleshooting

### Issue: 404 Not Found on Auth Endpoint

**Cause**: Keycloak is not running or realm doesn't exist

**Solution**:
1. Check if Keycloak is running: `docker ps | grep keycloak`
2. Check Keycloak logs: `docker logs fitu-keycloak`
3. Verify realm name is `fitness-oauth2` (not `fitness-app`)
4. Wait for Keycloak to fully start (check logs for "Started Keycloak")

### Issue: CORS Errors

**Cause**: Web origins not configured properly

**Solution**:
1. Go to Keycloak Admin Console
2. **Clients** → `oauth2-pkce-client` → **Settings**
3. Add `http://localhost:5173` to **Web origins**
4. Add `+` to allow all redirect URIs as origins

### Issue: Invalid Redirect URI

**Cause**: Redirect URI not whitelisted

**Solution**:
1. Go to **Clients** → `oauth2-pkce-client` → **Settings**
2. Add exact redirect URI: `http://localhost:5173/*`
3. Click **Save**

### Issue: Token Validation Failed in Gateway

**Cause**: JWK Set URI mismatch

**Solution**:
1. Verify the realm name in gateway configuration matches Keycloak
2. Restart Gateway service after configuration change
3. Check Gateway logs for JWT validation errors

## Production Considerations

For production deployment:

1. **Use HTTPS**: Configure SSL/TLS certificates
2. **Strong Passwords**: Change default admin credentials
3. **External Database**: Use production-grade PostgreSQL
4. **Backup**: Regular backups of Keycloak database
5. **High Availability**: Run multiple Keycloak instances
6. **Monitoring**: Set up health checks and monitoring
7. **Rate Limiting**: Configure rate limiting for auth endpoints

## Additional Resources

- [Keycloak Documentation](https://www.keycloak.org/documentation)
- [OAuth 2.0 PKCE Flow](https://oauth.net/2/pkce/)
- [OpenID Connect](https://openid.net/connect/)

## Quick Commands

```bash
# Start all infrastructure
docker-compose up -d

# Stop all services
docker-compose down

# View Keycloak logs
docker logs -f fitu-keycloak

# Restart Keycloak
docker-compose restart keycloak

# Remove all data (clean start)
docker-compose down -v
```
