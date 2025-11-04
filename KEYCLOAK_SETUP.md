# Keycloak Setup for Enterprise Learning Tracker

This guide explains how to set up and use Keycloak for authentication in the Enterprise Learning Tracker application.

## Overview

Keycloak is used as the identity and access management solution for this application. It provides:
- User registration and authentication
- JWT-based token authentication
- Role-based access control (RBAC)
- OAuth2/OpenID Connect support

## Prerequisites

- Docker and Docker Compose installed
- Java 21
- Gradle

## Quick Start

### 1. Start Keycloak and PostgreSQL

From the project root directory, run:

```bash
docker-compose up -d
```

This will start:
- **Keycloak** on http://localhost:8180
- **PostgreSQL** on localhost:5432

Wait for the containers to fully start (about 30-60 seconds).

### 2. Verify Keycloak is Running

Open your browser and navigate to:
```
http://localhost:8180
```

You should see the Keycloak welcome page.

### 3. Access Keycloak Admin Console

1. Click on "Administration Console"
2. Login with:
   - **Username**: `admin`
   - **Password**: `admin`

### 4. Verify Realm Configuration

The `enterprise-learning-tracker` realm should already be configured automatically. To verify:

1. In the Keycloak Admin Console, select the `enterprise-learning-tracker` realm from the dropdown (top-left)
2. Navigate to **Clients** and verify that `elt-backend` and `elt-frontend` clients exist
3. Navigate to **Users** and verify that test users exist

### 5. Start the Application

```bash
./gradlew bootRun --args='--spring.profiles.active=dev'
```

The application will start on http://localhost:8080

## Realm Configuration

### Pre-configured Realm: `enterprise-learning-tracker`

The realm is automatically imported from `docker/keycloak/realms/elt-realm.json` and includes:

#### Roles
- `SYSTEM_ADMIN` - System Administrator with full access
- `SCHOOL_ADMIN` - School Administrator who can manage a school
- `TUTOR` - Tutor who can create and manage learning content
- `STUDENT` - Student who can enroll in learning paths

#### Pre-configured Test Users

| Email | Password | Role | Description |
|-------|----------|------|-------------|
| admin@elt.com | admin123 | SYSTEM_ADMIN | System administrator |
| school.admin@elt.com | school123 | SCHOOL_ADMIN | School administrator |
| tutor@elt.com | tutor123 | TUTOR | Content creator |
| student@elt.com | student123 | STUDENT | Student user |

#### Clients

1. **elt-backend** (Confidential Client)
   - Client ID: `elt-backend`
   - Client Secret: `elt-backend-secret-key-12345`
   - Used by the Spring Boot backend for authentication
   - Service Accounts Enabled: Yes
   - Direct Access Grants Enabled: Yes

2. **elt-frontend** (Public Client)
   - Client ID: `elt-frontend`
   - Used by frontend applications (React, Angular, etc.)
   - Public Client: Yes

## API Endpoints

### Register a New User

```bash
POST http://localhost:8080/api/auth/register
Content-Type: application/json

{
  "email": "newuser@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "password": "password123"
}
```

**Response:**
```json
{
  "userId": "550e8400-e29b-41d4-a716-446655440000",
  "message": "User registered successfully"
}
```

### Authenticate (Login)

```bash
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "email": "student@elt.com",
  "password": "student123"
}
```

**Response:**
```json
{
  "accessToken": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "expiresIn": 300,
  "userId": "550e8400-e29b-41d4-a716-446655440000"
}
```

### Access Protected Endpoints

Use the `accessToken` in the Authorization header:

```bash
GET http://localhost:8080/api/users/me
Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...
```

## Testing with cURL

### Register a new user:
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "firstName": "Test",
    "lastName": "User",
    "password": "password123"
  }'
```

### Login:
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "student@elt.com",
    "password": "student123"
  }'
```

### Use the token:
```bash
# Save the token from the login response
TOKEN="eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9..."

# Use it to access protected endpoints
curl http://localhost:8080/api/users/me \
  -H "Authorization: Bearer $TOKEN"
```

## Architecture

### Hexagonal Architecture Implementation

The Keycloak integration follows the Hexagonal Architecture pattern:

```
usermanagement/
├── domain/
│   └── port/
│       └── out/
│           └── AuthenticationPort.java      # Outbound port (interface)
├── application/
│   ├── usecase/
│   │   ├── RegisterUserUseCase.java        # Inbound port (interface)
│   │   └── AuthenticateUserUseCase.java    # Inbound port (interface)
│   └── service/
│       ├── RegisterUserService.java         # Use case implementation
│       └── AuthenticateUserService.java     # Use case implementation
├── infrastructure/
│   └── keycloak/
│       ├── KeycloakConfig.java              # Configuration
│       ├── KeycloakConfigProperties.java    # Properties binding
│       └── KeycloakAuthenticationAdapter.java # Adapter implementation
└── api/
    └── AuthenticationController.java        # REST API
```

### Flow

1. **User Registration**:
   - Client → AuthenticationController → RegisterUserService → UserRepository (save to DB)
   - RegisterUserService → KeycloakAuthenticationAdapter → Keycloak (create user)
   - Publishes UserRegisteredEvent

2. **User Authentication**:
   - Client → AuthenticationController → AuthenticateUserService
   - AuthenticateUserService → KeycloakAuthenticationAdapter → Keycloak (authenticate)
   - Returns JWT tokens

## Configuration

### Application Configuration (application-dev.yml)

```yaml
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: http://localhost:8180/realms/enterprise-learning-tracker
          jwk-set-uri: http://localhost:8180/realms/enterprise-learning-tracker/protocol/openid-connect/certs

keycloak:
  auth-server-url: http://localhost:8180
  realm: enterprise-learning-tracker
  resource: elt-backend
  credentials:
    secret: elt-backend-secret-key-12345
  admin:
    username: admin
    password: admin
    client-id: admin-cli
```

## Troubleshooting

### Keycloak not starting

1. Check Docker logs:
   ```bash
   docker-compose logs keycloak
   ```

2. Ensure PostgreSQL is healthy:
   ```bash
   docker-compose ps
   ```

3. Restart containers:
   ```bash
   docker-compose down
   docker-compose up -d
   ```

### Realm not imported

1. Check if the realm file exists:
   ```bash
   ls -la docker/keycloak/realms/
   ```

2. Manually import the realm:
   - Login to Keycloak Admin Console
   - Click on realm dropdown → Create Realm
   - Upload `docker/keycloak/realms/elt-realm.json`

### Authentication failing

1. Verify Keycloak is accessible:
   ```bash
   curl http://localhost:8180/realms/enterprise-learning-tracker
   ```

2. Check application logs for connection errors

3. Verify the client secret in `application-dev.yml` matches Keycloak configuration

### Port conflicts

If ports 8180 or 5432 are already in use, update `docker-compose.yml`:

```yaml
services:
  keycloak:
    ports:
      - "8181:8080"  # Change 8180 to 8181

  postgres:
    ports:
      - "5433:5432"  # Change 5432 to 5433
```

Then update the URLs in `application-dev.yml` accordingly.

## Stopping Keycloak

To stop all services:

```bash
docker-compose down
```

To stop and remove all data:

```bash
docker-compose down -v
```

## Production Considerations

For production deployment:

1. **Use strong passwords**: Change default admin password and client secrets
2. **Enable HTTPS**: Configure SSL/TLS certificates
3. **External database**: Use a managed PostgreSQL instance
4. **High availability**: Set up Keycloak clustering
5. **Backup**: Regularly backup the Keycloak database
6. **Update configuration**: Use environment-specific properties
7. **Enable email verification**: Configure SMTP for email verification
8. **Configure session timeouts**: Adjust token lifespans based on security requirements

## Additional Resources

- [Keycloak Documentation](https://www.keycloak.org/documentation)
- [Spring Security OAuth2 Resource Server](https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/index.html)
- [OpenID Connect Specification](https://openid.net/connect/)
