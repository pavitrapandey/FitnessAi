#!/bin/bash

echo "🚀 Starting FitU Infrastructure Services..."
echo ""

# Check if Docker is running
if ! docker info > /dev/null 2>&1; then
    echo "❌ Docker is not running. Please start Docker and try again."
    exit 1
fi

echo "✅ Docker is running"
echo ""

# Start infrastructure services
echo "📦 Starting infrastructure services (Keycloak, PostgreSQL, MongoDB, Kafka)..."
docker-compose up -d

echo ""
echo "⏳ Waiting for services to be ready..."
sleep 10

# Check service status
echo ""
echo "📊 Service Status:"
docker-compose ps

echo ""
echo "✅ Infrastructure services started successfully!"
echo ""
echo "📝 Next Steps:"
echo "1. Configure Keycloak (first time only):"
echo "   - Open http://localhost:8181"
echo "   - Login with admin/admin"
echo "   - Follow instructions in keycloak-setup.md"
echo ""
echo "2. Set environment variables:"
echo "   export GEMINI_API_URL=https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent"
echo "   export GEMINI_API_KEY=your_api_key_here"
echo ""
echo "3. Start microservices in order:"
echo "   - Config Server (port 8888)"
echo "   - Eureka Server (port 8761)"
echo "   - User Service (port 8083)"
echo "   - Activity Service (port 8081)"
echo "   - AI Service (port 8082)"
echo "   - Gateway (port 8080)"
echo ""
echo "4. Start frontend:"
echo "   cd Frontend/fitness-app-frontend"
echo "   npm install && npm run dev"
echo ""
echo "🔗 Service URLs:"
echo "   - Keycloak: http://localhost:8181"
echo "   - Eureka: http://localhost:8761"
echo "   - Gateway: http://localhost:8080"
echo "   - Frontend: http://localhost:5173"
echo ""
