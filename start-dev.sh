#!/bin/bash

# Enterprise Learning Tracker Development Startup Script

echo "======================================"
echo "Enterprise Learning Tracker - Dev Setup"
echo "======================================"
echo ""

# Check if Docker is running
if ! docker info > /dev/null 2>&1; then
    echo "❌ Error: Docker is not running. Please start Docker first."
    exit 1
fi

echo "✓ Docker is running"
echo ""

# Start Docker Compose services
echo "Starting Keycloak and PostgreSQL..."
docker-compose up -d

echo ""
echo "Waiting for services to be ready..."
echo "(This may take 30-60 seconds)"

# Wait for Keycloak to be ready
MAX_RETRIES=30
RETRY_COUNT=0
until curl -f http://localhost:8180 > /dev/null 2>&1; do
    RETRY_COUNT=$((RETRY_COUNT+1))
    if [ $RETRY_COUNT -ge $MAX_RETRIES ]; then
        echo "❌ Error: Keycloak did not start within the expected time"
        echo "Check logs with: docker-compose logs keycloak"
        exit 1
    fi
    echo -n "."
    sleep 2
done

echo ""
echo "✓ Keycloak is ready at http://localhost:8180"
echo "✓ PostgreSQL is ready"
echo ""

# Display service information
echo "======================================"
echo "Services Status:"
echo "======================================"
docker-compose ps
echo ""

echo "======================================"
echo "Keycloak Admin Console"
echo "======================================"
echo "URL:      http://localhost:8180"
echo "Username: admin"
echo "Password: admin"
echo "Realm:    enterprise-learning-tracker"
echo ""

echo "======================================"
echo "Pre-configured Test Users"
echo "======================================"
echo "1. System Admin"
echo "   Email:    admin@elt.com"
echo "   Password: admin123"
echo ""
echo "2. School Admin"
echo "   Email:    school.admin@elt.com"
echo "   Password: school123"
echo ""
echo "3. Tutor"
echo "   Email:    tutor@elt.com"
echo "   Password: tutor123"
echo ""
echo "4. Student"
echo "   Email:    student@elt.com"
echo "   Password: student123"
echo ""

echo "======================================"
echo "Next Steps"
echo "======================================"
echo "1. Start the application:"
echo "   ./gradlew bootRun --args='--spring.profiles.active=dev'"
echo ""
echo "2. Access the application at http://localhost:8080"
echo ""
echo "3. Test authentication:"
echo "   curl -X POST http://localhost:8080/api/auth/login \\"
echo "     -H 'Content-Type: application/json' \\"
echo "     -d '{\"email\":\"student@elt.com\",\"password\":\"student123\"}'"
echo ""
echo "4. View detailed setup instructions:"
echo "   cat KEYCLOAK_SETUP.md"
echo ""

echo "======================================"
echo "To stop services, run:"
echo "  docker-compose down"
echo "======================================"
