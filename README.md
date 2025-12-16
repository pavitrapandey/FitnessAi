# FitU - AI-Powered Fitness Tracking Platform

A comprehensive microservices-based fitness application that leverages AI to provide personalized workout recommendations and activity analysis.

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
- Docker (optional, for containerized deployment)

## 🔧 Environment Setup

### 1. Database Setup

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

### 2. Kafka Setup
```bash
# Start Zookeeper
bin/zookeeper-server-start.sh config/zookeeper.properties

# Start Kafka Server
bin/kafka-server-start.sh config/server.properties

# Create topic
bin/kafka-topics.sh --create --topic activity-events --bootstrap-server localhost:9092
```

### 3. Environment Variables

Create environment variables for AI Service:
```bash
export GEMINI_API_URL=https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent
export GEMINI_API_KEY=your_gemini_api_key
```

## 🚀 Running the Application

### Option 1: Manual Startup (Recommended Order)

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

### Option 2: Using Maven (All services)
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

- **Eureka Dashboard**: http://localhost:8761
- **Config Server**: http://localhost:8888
- **Gateway Health**: http://localhost:8080/actuator/health

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

1. **Service Discovery Issues**
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