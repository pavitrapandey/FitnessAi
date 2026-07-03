# FitU - AI-Powered Fitness Tracking Platform

A comprehensive microservices-based fitness application that leverages AI to provide personalized workout recommendations and activity analysis.

## 📚 Documentation

- **[README.md](README.md)** - Main documentation (you are here)
- **[QUICK-FIX.md](QUICK-FIX.md)** - Fix OAuth2 404 error in 5 minutes
- **[keycloak-setup.md](keycloak-setup.md)** - Complete Keycloak OAuth2 setup guide
- **[Frontend/fitness-app-frontend/SETUP_INSTRUCTIONS.md](Frontend/fitness-app-frontend/SETUP_INSTRUCTIONS.md)** - Frontend setup instructions
- **[Frontend/fitness-app-frontend/TAILWIND_UPGRADE.md](Frontend/fitness-app-frontend/TAILWIND_UPGRADE.md)** - Tailwind CSS upgrade notes

## 🏗️ Architecture Overview

FitU is built using Spring Boot microservices architecture with the following components:

```
Frontend (React SPA - Port: 5173)
    ↓ (HTTP + OAuth2 token)
Gateway (API Gateway - Port: 8080)
    ↓ (validates JWT via Keycloak)
    ├─ /api/user/** → User Service (Port: 8083)
    ├─ /api/activities/** → Activity Service (Port: 8081)
    └─ /api/recommendations/** → AI Service (Port: 8082)
    ↓
Infrastructure Services
    ├─ Eureka (Service Discovery - Port: 8761)
    ├─ Config Server (Centralized Config - Port: 8888)
    ├─ Keycloak (OAuth2 Server - Port: 8181)
    ├─ PostgreSQL (User DB - Port: 5432)
    ├─ MongoDB (Activity & AI DB - Port: 27017)
    └─ Kafka + Zookeeper (Event Streaming - Ports: 9092, 2181)
```

**Event Flow:**
- Activity Service publishes events to Kafka
- AI Service consumes events and generates recommendations via Google Gemini API

## 🚀 Services

| Service | Port | Purpose | Database | Key Features |
|---------|------|---------|----------|--------------|
| **Gateway** | 8080 | API Gateway & Router | None | Centralized entry point, routes requests to services, OAuth2 validation |
| **Config Server** | 8888 | Centralized Config Management | None | Native file-system based config, service-specific properties |
| **Eureka** | 8761 | Service Discovery & Registration | None | Service registry, health monitoring, load balancing support |
| **User Service** | 8083 | User Management | PostgreSQL | Registration, profile management, user validation |
| **Activity Service** | 8081 | Activity Tracking | MongoDB | Track workouts, log activities, publish events to Kafka |
| **AI Service** | 8082 | AI Recommendations | MongoDB | Consume activity events from Kafka, generate personalized recommendations via Gemini API |
| **Frontend** | 5173 | React SPA | None | Activity tracker UI, authentication, activity management |

### Frontend Details
- **Framework**: React 19.0 with Vite
- **Styling**: Tailwind CSS 4.2.4
- **State Management**: Redux Toolkit
- **Routing**: React Router 7.2
- **Authentication**: OAuth2 PKCE flow with Keycloak
- **HTTP Client**: Axios

## 🛠️ Technology Stack

**Backend:**
- Spring Boot 3.x & Spring Cloud 2025.1.0
- Spring Cloud Gateway (API routing)
- Netflix Eureka (service discovery)
- Spring Cloud Config (centralized configuration)
- Apache Kafka (async messaging)
- Spring Data JPA (PostgreSQL for User Service)
- Spring Data MongoDB (for Activity & AI Services)

**Frontend:**
- React 19.0
- Vite (bundler/build tool)
- Tailwind CSS 4.2.4 (styling)
- Redux Toolkit (state management)
- React Router 7.2 (routing)
- Axios (HTTP client)
- react-oauth2-code-pkce (OAuth2 authentication)

**Infrastructure:**
- Docker & Docker Compose
- Keycloak 23.0 (OAuth2/OpenID Connect server)
- PostgreSQL 15 (User Service DB)
- MongoDB 7.0 (Activity & AI Services DB)
- Kafka 2.8+ with Zookeeper (event streaming)

**AI Integration:**
- Google Gemini API (recommendations engine)

**Build & Development:**
- Maven (backend services)
- npm (frontend)
- Java 21
- Node.js 18+

## 📋 Prerequisites

Before running the application, ensure you have the following installed:

- Java 21 or higher
- Maven 3.6+
- Node.js 18+ (for frontend)
- Docker & Docker Compose (recommended for infrastructure)
- Git

**Optional (if not using Docker):**
- PostgreSQL 15+
- MongoDB 7.0+
- Apache Kafka 2.8+
- Keycloak 23.0

## 🔧 Environment Setup

### Quick Start with Docker (Recommended)

The easiest way to set up all infrastructure components is using Docker Compose:

```bash
# Start all infrastructure services (Keycloak, PostgreSQL, MongoDB, Kafka)
docker-compose up -d

# Check if all services are running
docker-compose ps

# View logs
docker-compose logs -f
```

This will start:
- **Keycloak** (OAuth2 Server) on port 8181
- **PostgreSQL** on port 5432
- **MongoDB** on port 27017
- **Kafka** on port 9092
- **Zookeeper** on port 2181

### Using Provided Scripts

Windows users can use the batch scripts:

```bash
# Start infrastructure
start-infrastructure.bat

# Start all services
start-services.bat

# Quick start (infrastructure + services)
quick-start.bat
```

Linux/Mac users:

```bash
# Start infrastructure
./start-infrastructure.sh
```

### Manual Setup (Alternative)

If you prefer to install services manually, follow these steps:

#### 1. Keycloak Setup (OAuth2 Authentication)

**Important**: Keycloak must be configured before running the application.

See detailed setup instructions in [keycloak-setup.md](keycloak-setup.md)

**Quick Setup**:
1. Start Keycloak: `docker-compose up -d keycloak`
2. Access admin console: http://localhost:8181 (admin/admin)
3. Create realm: `fitness-oauth2`
4. Create client: `oauth2-pkce-client`
5. Create test user with credentials

#### 2. Database Setup

##### PostgreSQL (User Service)
```sql
CREATE DATABASE "FitU-user";
CREATE USER postgres WITH PASSWORD 'your_password';
GRANT ALL PRIVILEGES ON DATABASE "FitU-user" TO postgres;
```

##### MongoDB (Activity & AI Services)
```bash
# Start MongoDB
mongod --dbpath /path/to/your/db

# Create databases (will be created automatically on first use)
# - aiActivityFitness (Activity Service)
# - aiServiceFitness (AI Service)
```

#### 3. Kafka Setup
```bash
# Start Zookeeper
bin/zookeeper-server-start.sh config/zookeeper.properties

# Start Kafka Server
bin/kafka-server-start.sh config/server.properties

# Create topic
bin/kafka-topics.sh --create --topic activity-events --bootstrap-server localhost:9092
```

#### 4. Environment Variables

Create environment variables for AI Service:
```bash
export GEMINI_API_URL=https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent
export GEMINI_API_KEY=your_gemini_api_key
```

## 🚀 Running the Application

### Prerequisites Check

Before starting the services, ensure:
- ✅ Keycloak is running and configured (http://localhost:8181)
- ✅ PostgreSQL is running (port 5432)
- ✅ MongoDB is running (port 27017)
- ✅ Kafka is running (port 9092)
- ✅ Environment variables are set (GEMINI_API_KEY, GEMINI_API_URL)

### Option 1: Using Scripts (Recommended)

```bash
# Windows
quick-start.bat

# Linux/Mac
./start-infrastructure.sh
# Then manually start services or use individual scripts
```

### Option 2: Manual Startup (Recommended Order)

1. **Start Config Server**
```bash
cd configServer
mvn spring-boot:run
```

2. **Start Eureka Server**
```bash
cd eureka
mvn spring-boot:run
```

3. **Start Core Services** (can be started in parallel)
```bash
# Terminal 1
cd userService
mvn spring-boot:run

# Terminal 2
cd activityService
mvn spring-boot:run

# Terminal 3
cd aiService
mvn spring-boot:run
```

4. **Start Gateway**
```bash
cd Gateway
mvn spring-boot:run
```

5. **Start Frontend**
```bash
cd Frontend/fitness-app-frontend
npm install
npm run dev
```

### Option 3: Using Maven (All services)
```bash
# Build all services
mvn clean install

# Run each service in separate terminals
mvn spring-boot:run -pl configServer
mvn spring-boot:run -pl eureka
mvn spring-boot:run -pl userService
mvn spring-boot:run -pl activityService
mvn spring-boot:run -pl aiService
mvn spring-boot:run -pl Gateway
```

The frontend will be available at http://localhost:5173

## 🔐 Authentication Flow

The application uses OAuth2 with PKCE (Proof Key for Code Exchange) flow:

1. User accesses frontend (http://localhost:5173)
2. Frontend redirects to Keycloak login (http://localhost:8181)
3. User authenticates with Keycloak
4. Keycloak redirects back with authorization code
5. Frontend exchanges code for access token
6. Access token is used for API requests through Gateway
7. Gateway validates JWT token and syncs user data

**Test Credentials** (after Keycloak setup):
- Username: `testuser`
- Password: `Test@123`

## 📡 API Endpoints

### Gateway Routes (Port: 8080)

All API requests go through the Gateway with Bearer token authentication.

#### User Service
- `POST /api/user/register` - User registration
- `GET /api/users/**` - User management endpoints

#### Activity Service
- `GET /api/activities/**` - Activity tracking endpoints
- `POST /api/activities` - Create new activity
- `GET /api/activities/{id}` - Get activity by ID
- `PUT /api/activities/{id}` - Update activity
- `DELETE /api/activities/{id}` - Delete activity

#### AI Service
- `GET /api/recommendations/user/{userId}` - Get user recommendations
- `GET /api/recommendations/activity/{activityId}` - Get activity-specific recommendations

### Direct Service Access (Development)

#### User Service (Port: 8083)
- User registration, authentication, and profile management

#### Activity Service (Port: 8081)
- Activity CRUD operations, workout tracking

#### AI Service (Port: 8082)
- AI-generated fitness recommendations and analysis

## 🔍 Monitoring & Health Checks

- **Frontend**: http://localhost:5173
- **Gateway Health**: http://localhost:8080/actuator/health
- **Keycloak Admin Console**: http://localhost:8181 (admin/admin)
- **Eureka Dashboard**: http://localhost:8761
- **Config Server**: http://localhost:8888

## 🏃‍♂️ Usage Flow

1. **User Registration**: Register through User Service API
2. **Authentication**: Login via Keycloak OAuth2
3. **Activity Logging**: Log workouts via Activity Service (publishes to Kafka)
4. **AI Analysis**: AI Service consumes events and generates personalized recommendations using Google Gemini API
5. **Insights**: Users can retrieve AI-powered insights and suggestions through the frontend

## 🔒 Security Considerations

- JWT tokens are validated by the Gateway using Keycloak's JWK Set
- Database credentials are managed through Spring Cloud Config
- API keys (Gemini) are stored as environment variables
- OAuth2 PKCE flow prevents authorization code interception
- CORS is configured for frontend domain
- Consider implementing rate limiting for API endpoints

## 🐛 Troubleshooting

### Common Issues

1. **OAuth2 404 Error - Keycloak Not Found**
   ```
   GET http://localhost:8181/realms/fitness-oauth2/protocol/openid-connect/auth 404
   ```
   **Solution**:
   - Ensure Keycloak is running: `docker ps | grep keycloak`
   - Check Keycloak logs: `docker logs fitu-keycloak`
   - Verify realm `fitness-oauth2` exists in Keycloak admin console
   - Follow [keycloak-setup.md](keycloak-setup.md) for complete setup

2. **CORS Errors from Frontend**
   **Solution**:
   - Add `http://localhost:5173` to Keycloak client's Web Origins
   - Restart Gateway service

3. **JWT Token Validation Failed**
   **Solution**:
   - Verify Gateway configuration has correct JWK Set URI
   - Ensure realm name matches: `fitness-oauth2`
   - Restart Config Server and Gateway

4. **Service Discovery Issues**
   - Ensure Eureka server is running before starting other services
   - Check network connectivity between services

5. **Database Connection Issues**
   - Verify database servers are running
   - Check connection strings and credentials

6. **Kafka Issues**
   - Ensure Kafka and Zookeeper are running
   - Verify topic creation and permissions

7. **AI Service Issues**
   - Verify Gemini API key is valid and set
   - Check API rate limits and quotas

8. **Port Already in Use**
   **Solution**:
   - Check what's using the port: `netstat -ano | findstr :8080`
   - Kill the process or change the port in configuration

### Quick Reset

If you need to start fresh:
```bash
# Stop all Docker services
docker-compose down -v

# Remove all data
docker volume prune -f

# Start fresh
docker-compose up -d

# Reconfigure Keycloak (follow keycloak-setup.md)
```

### Logs Location
- Application logs: `logs/` directory in each service
- Check console output for startup issues
- Docker logs: `docker-compose logs [service-name]`

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- Google Gemini API for AI-powered recommendations
- Spring Boot and Spring Cloud communities
- Apache Kafka for event streaming
- MongoDB and PostgreSQL for data persistence

---

**Note**: This is a development setup. For production deployment, consider using Docker containers, Kubernetes orchestration, and proper CI/CD pipelines.