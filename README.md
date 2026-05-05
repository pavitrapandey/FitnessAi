# FitU - AI-Powered Fitness Tracking Platform

A comprehensive microservices-based fitness application that leverages AI to provide personalized workout recommendations and activity analysis.

## 📚 Documentation

- **[README.md](README.md)** - Main documentation (you are here)
- **[QUICK-FIX.md](QUICK-FIX.md)** - Fix OAuth2 404 error in 5 minutes
- **[keycloak-setup.md](keycloak-setup.md)** - Complete Keycloak OAuth2 setup guide
- **[TROUBLESHOOTING.md](TROUBLESHOOTING.md)** - Comprehensive troubleshooting guide

## 🏗️ Architecture Overview

FitU is built using Spring Boot microservices architecture with the following components:

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Gateway       │    │   Config Server │    │   Eureka Server │
│   Port: 8080    │    │   Port: 8888    │    │   Port: 8761    │
└─────────────────┘    └─────────────────┘    └─────────────────┘
         │                       │                       │
         └───────────────────────┼───────────────────────┘
                                 │
    ┌────────────────────────────┼────────────────────────────┐
    │                            │                            │
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│  User Service   │    │Activity Service │    │   AI Service    │
│  Port: 8083     │    │  Port: 8081     │    │  Port: 8082     │
│  PostgreSQL     │    │   MongoDB       │    │   MongoDB       │
└─────────────────┘    └─────────────────┘    └─────────────────┘
                                │                       │
                                └───────── Kafka ───────┘
```

## 🚀 Services

### 1. **Gateway Service** (Port: 8080)
- API Gateway for routing requests to appropriate microservices
- Load balancing and service discovery integration
- Centralized entry point for all client requests

### 2. **Config Server** (Port: 8888)
- Centralized configuration management
- Environment-specific configurations
- Native file system configuration storage

### 3. **Eureka Server** (Port: 8761)
- Service discovery and registration
- Health monitoring of microservices
- Load balancing support

### 4. **User Service** (Port: 8083)
- User registration and authentication
- Profile management
- PostgreSQL database integration

### 5. **Activity Service** (Port: 8081)
- Activity tracking and logging
- Workout session management
- MongoDB database integration
- Kafka event publishing

### 6. **AI Service** (Port: 8082)
- AI-powered fitness recommendations using Google Gemini API
- Activity analysis and insights
- Personalized workout suggestions
- MongoDB database integration
- Kafka event consumption

## 🛠️ Technology Stack

- **Framework**: Spring Boot 3.x, Spring Cloud
- **Service Discovery**: Netflix Eureka
- **API Gateway**: Spring Cloud Gateway
- **Configuration**: Spring Cloud Config
- **Messaging**: Apache Kafka
- **Databases**: 
  - PostgreSQL (User Service)
  - MongoDB (Activity & AI Services)
- **AI Integration**: Google Gemini API
- **Build Tool**: Maven
- **Java Version**: 21

## 📋 Prerequisites

Before running the application, ensure you have the following installed:

- Java 21 or higher
- Maven 3.6+
- PostgreSQL 12+
- MongoDB 4.4+
- Apache Kafka 2.8+
- Keycloak (OAuth2 Server)
- Docker & Docker Compose (recommended for infrastructure)
- Node.js 18+ (for frontend)

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

### Manual Setup (Alternative)

If you prefer to install services manually, follow these steps:

### 1. Keycloak Setup (OAuth2 Authentication)

**Important**: Keycloak must be configured before running the application.

See detailed setup instructions in [keycloak-setup.md](keycloak-setup.md)

**Quick Setup**:
1. Start Keycloak: `docker-compose up -d keycloak`
2. Access admin console: http://localhost:8181 (admin/admin)
3. Create realm: `fitness-oauth2`
4. Create client: `oauth2-pkce-client`
5. Create test user with credentials

### 2. Database Setup

#### PostgreSQL (User Service)
```sql
CREATE DATABASE "FitU-user";
CREATE USER postgres WITH PASSWORD 'your_password';
GRANT ALL PRIVILEGES ON DATABASE "FitU-user" TO postgres;
```

#### MongoDB (Activity & AI Services)
```bash
# Start MongoDB
mongod --dbpath /path/to/your/db

# Create databases (will be created automatically on first use)
# - aiActivityFitness (Activity Service)
# - aiServiceFitness (AI Service)
```

### 3. Kafka Setup
```bash
# Start Zookeeper
bin/zookeeper-server-start.sh config/zookeeper.properties

# Start Kafka Server
bin/kafka-server-start.sh config/server.properties

# Create topic
bin/kafka-topics.sh --create --topic activity-events --bootstrap-server localhost:9092
```

### 4. Environment Variables

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

### Option 1: Using Docker Compose (Recommended)

```bash
# Start all infrastructure
docker-compose up -d

# Verify all services are running
docker-compose ps

# Configure Keycloak (first time only)
# Follow instructions in keycloak-setup.md
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

### Running the Frontend

```bash
cd Frontend/fitness-app-frontend
npm install
npm run dev
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

#### User Service
- `GET /api/users/**` - User management endpoints

#### Activity Service  
- `GET /api/activities/**` - Activity tracking endpoints

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

- **Keycloak Admin Console**: http://localhost:8181 (admin/admin)
- **Eureka Dashboard**: http://localhost:8761
- **Config Server**: http://localhost:8888
- **Gateway Health**: http://localhost:8080/actuator/health
- **Frontend**: http://localhost:5173

## 🏃‍♂️ Usage Flow

1. **User Registration**: Register through User Service
2. **Activity Logging**: Log workouts via Activity Service
3. **AI Analysis**: Activity Service publishes events to Kafka
4. **Recommendations**: AI Service consumes events and generates personalized recommendations
5. **Insights**: Users can retrieve AI-powered insights and suggestions

## 🔒 Security Considerations

- Database credentials should be externalized using Spring Cloud Config encryption
- API keys should be stored as environment variables
- Consider implementing OAuth2/JWT for authentication
- Add rate limiting and request validation

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
   - Ensure realm name matches: `fitness-oauth2` (not `fitness-app`)
   - Restart Config Server and Gateway

4. **Service Discovery Issues**
   - Ensure Eureka server is running before starting other services
   - Check network connectivity between services

2. **Database Connection Issues**
   - Verify database servers are running
   - Check connection strings and credentials

3. **Kafka Issues**
   - Ensure Kafka and Zookeeper are running
   - Verify topic creation and permissions

4. **AI Service Issues**
   - Verify Gemini API key is valid and set
   - Check API rate limits and quotas

5. **Port Already in Use**
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