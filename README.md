# Enterprise Learning Tracker

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.org/projects/jdk/21/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.6-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Spring Modulith](https://img.shields.io/badge/Spring%20Modulith-1.4.3-blue.svg)](https://spring.io/projects/spring-modulith)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

> A modular monolith application for managing employee learning and development activities across enterprises.

## 📋 Table of Contents

- [About](#about)
- [Key Features](#key-features)
- [Prerequisites](#prerequisites)
- [Quick Start](#quick-start)
- [Building the Project](#building-the-project)
- [Development Setup](#development-setup)
- [Running the Application](#running-the-application)
- [Authentication & API Testing](#authentication--api-testing)
- [Project Structure](#project-structure)
- [Architecture](#architecture)
- [Development Workflow](#development-workflow)
- [Troubleshooting](#troubleshooting)
- [Additional Documentation](#additional-documentation)
- [Contributing](#contributing)

## About

Enterprise Learning Tracker (ELT) is a comprehensive platform designed to manage, track, and deliver learning resources for organizations. It aggregates various learning platforms, courses, and technological resources to provide a unified system for governing employee learning and development.

### Problem Statement

- **What it solves**: Centralized management and tracking of learning paths organized by schools
- **Target users**: Employees (as students or tutors) and managers
- **Pain points addressed**: Creation, update, assignment, enrollment, assessment, and tracking of learning paths

## Key Features

- 🏢 **Multi-School Management** - Support for multiple organizational schools/departments
- 📚 **Learning Path Creation** - Design and organize structured learning experiences
- 👥 **User Management** - Role-based access control (System Admin, School Admin, Tutor, Student)
- 🔐 **Keycloak Authentication** - Secure OAuth2/OpenID Connect integration
- 📊 **Student Progress Tracking** - Monitor learner achievements and progression
- 🎓 **Enrollment Management** - Assign and manage user enrollments
- 🏆 **Badge System** - Award achievements and recognition
- 🔄 **Event-Driven Architecture** - Loosely coupled module communication
- 🧩 **Modular Monolith** - Spring Modulith for maintainable, scalable architecture

## Prerequisites

Before you begin, ensure you have the following installed:

- **Java 21** - [Download OpenJDK 21](https://openjdk.org/projects/jdk/21/)
- **Docker & Docker Compose** - [Install Docker](https://docs.docker.com/get-docker/)
- **Git** - For cloning the repository
- **IDE** (optional but recommended):
  - IntelliJ IDEA (with Lombok plugin)
  - VS Code (with Java extensions)
  - Eclipse (with Lombok support)

### Verify Installation

```bash
# Check Java version
java -version  # Should show version 21

# Check Docker
docker --version
docker-compose --version

# Check Gradle (wrapper included in project)
./gradlew --version
```

## Quick Start

Get up and running in 3 steps:

```bash
# 1. Clone the repository
git clone <repository-url>
cd Enterprise-Learning-Tracker

# 2. Start Keycloak and PostgreSQL
./start-dev.sh

# 3. Run the application
./gradlew bootRun --args='--spring.profiles.active=dev'
```

Access the application at **http://localhost:8080**

## Building the Project

### Standard Build (with tests)

```bash
./gradlew build
```

### Build without Tests

```bash
./gradlew build -x test
```

### Clean Build

```bash
./gradlew clean build
```

### Generate Bootable JAR

```bash
./gradlew bootJar
```

The JAR will be created at: `build/libs/Enterprise-Learning-Tracker-0.0.1-SNAPSHOT.jar`

### Run Specific Tests

```bash
# Run all tests
./gradlew test

# Run a specific test class
./gradlew test --tests com.learning.tracker.YourTestClass

# Run a specific test method
./gradlew test --tests com.learning.tracker.YourTestClass.testMethodName
```

## Development Setup

### 1. Start Infrastructure Services

The application requires Keycloak for authentication and two PostgreSQL instances (one for the application, one for Keycloak).

#### Option A: Automated Script (Recommended)

```bash
./start-dev.sh
```

This script will:
- Start Application PostgreSQL on port 5434
- Start Keycloak PostgreSQL on port 5433
- Start Keycloak on port 8180
- Wait for services to be ready
- Display connection information and test user credentials

#### Option B: Manual Docker Compose

```bash
docker-compose up -d

# View logs
docker-compose logs -f

# Check status
docker-compose ps
```

### 2. Verify Services

- **Keycloak Admin Console**: http://localhost:8180
  - Username: `admin`
  - Password: `admin`
  - Realm: `enterprise-learning-tracker`

- **PostgreSQL Databases**:
  - Application Database:
    - Host: `localhost`
    - Port: `5434`
    - Database: `eltdb`
    - Username: `eltuser`
    - Password: `eltpass`
  - Keycloak Database:
    - Host: `localhost`
    - Port: `5433`
    - Database: `keycloak`
    - Username: `keycloak`
    - Password: `keycloak`

### 3. IDE Setup

#### IntelliJ IDEA

1. Enable Lombok annotation processing:
   - Settings → Build, Execution, Deployment → Compiler → Annotation Processors
   - Check "Enable annotation processing"

2. Install Lombok Plugin:
   - Settings → Plugins → Search "Lombok" → Install

#### VS Code

1. Install extensions:
   - Extension Pack for Java
   - Lombok Annotations Support for VS Code

#### Eclipse

1. Install Lombok:
   - Download lombok.jar
   - Run: `java -jar lombok.jar`
   - Select Eclipse installation directory

## Running the Application

### Using Gradle (Development)

```bash
# Run with dev profile
./gradlew bootRun --args='--spring.profiles.active=dev'

# Run with test profile
./gradlew bootRun --args='--spring.profiles.active=test'

# Run with prod profile
./gradlew bootRun --args='--spring.profiles.active=prod'
```

### Using JAR (Production-like)

```bash
# Build the JAR
./gradlew bootJar

# Run the JAR
java -jar build/libs/Enterprise-Learning-Tracker-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev
```

### Application Access Points

| Service | URL | Credentials |
|---------|-----|-------------|
| Application API | http://localhost:8080 | See test users below |
| App PostgreSQL | localhost:5434 | eltuser / eltpass |
| Keycloak PostgreSQL | localhost:5433 | keycloak / keycloak |
| Keycloak Admin | http://localhost:8180 | admin / admin |
| Actuator Health | http://localhost:8080/actuator/health | Public |
| Actuator Endpoints | http://localhost:8080/actuator | Public (dev only) |

## Authentication & API Testing

### Pre-configured Test Users

The application comes with 4 pre-configured users for testing:

| Email | Password | Role | Description |
|-------|----------|------|-------------|
| admin@elt.com | admin123 | SYSTEM_ADMIN | Full system access |
| school.admin@elt.com | school123 | SCHOOL_ADMIN | School management |
| tutor@elt.com | tutor123 | TUTOR | Content creation |
| student@elt.com | student123 | STUDENT | Learning access |

### API Endpoints

#### Authentication Endpoints (Public)

**Register a New User**
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "newuser@example.com",
    "firstName": "John",
    "lastName": "Doe",
    "password": "password123"
  }'
```

**Response:**
```json
{
  "userId": "550e8400-e29b-41d4-a716-446655440000",
  "message": "User registered successfully"
}
```

**Login (Get JWT Token)**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "student@elt.com",
    "password": "student123"
  }'
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

#### Protected Endpoints (Require JWT)

**Get All Users**
```bash
TOKEN="your-access-token-here"

curl http://localhost:8080/api/users \
  -H "Authorization: Bearer $TOKEN"
```

**Get User by ID**
```bash
curl http://localhost:8080/api/users/{userId} \
  -H "Authorization: Bearer $TOKEN"
```

**Get User by Email**
```bash
curl http://localhost:8080/api/users/by-email?email=student@elt.com \
  -H "Authorization: Bearer $TOKEN"
```

### Testing Workflow Example

```bash
# 1. Login to get token
TOKEN=$(curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"student@elt.com","password":"student123"}' \
  | jq -r '.accessToken')

# 2. Use token to access protected endpoint
curl http://localhost:8080/api/users \
  -H "Authorization: Bearer $TOKEN"
```

## Project Structure

```
Enterprise-Learning-Tracker/
├── src/main/java/com/learning/tracker/
│   ├── shared/                          # Shared Kernel
│   │   ├── domain/
│   │   │   ├── vo/                      # Value Objects (UserId, Email, etc.)
│   │   │   └── event/                   # Domain Events
│   │   └── infrastructure/
│   │       ├── exception/               # Common exceptions
│   │       ├── persistence/             # JPA converters
│   │       └── security/                # Security configuration
│   │
│   ├── usermanagement/                  # User Management Module
│   │   ├── domain/
│   │   │   ├── model/                   # User, SystemRole, SchoolRole
│   │   │   ├── repository/              # UserRepository (interface)
│   │   │   ├── port/out/                # Outbound ports (AuthenticationPort)
│   │   │   └── event/                   # UserRegisteredEvent, etc.
│   │   ├── application/
│   │   │   ├── usecase/                 # Use case interfaces
│   │   │   ├── service/                 # Service implementations
│   │   │   └── dto/                     # Data Transfer Objects
│   │   ├── infrastructure/
│   │   │   ├── persistence/             # JPA entities and repositories
│   │   │   ├── mapper/                  # Entity-Domain mappers
│   │   │   └── keycloak/                # Keycloak integration
│   │   └── api/                         # REST controllers
│   │
│   ├── schoolmanagement/                # School Management Module
│   ├── learningcontent/                 # Learning Content Module
│   ├── enrollment/                      # Enrollment Module
│   └── studentprogress/                 # Student Progress Module
│
├── src/main/resources/
│   ├── application.yml                  # Base configuration
│   ├── application-dev.yml              # Development profile
│   ├── application-test.yml             # Test profile
│   └── application-prod.yml             # Production profile
│
├── docker/
│   ├── keycloak/realms/                 # Keycloak realm configuration
│   └── postgres/init/                   # PostgreSQL init scripts
│
├── docker-compose.yml                   # Docker services definition
├── start-dev.sh                         # Development startup script
├── build.gradle                         # Gradle build configuration
├── KEYCLOAK_SETUP.md                    # Detailed Keycloak documentation
└── CLAUDE.md                            # AI assistant guidelines
```

### Module Organization

Each module follows **Hexagonal Architecture** (Ports & Adapters):

- **domain/** - Core business logic (entities, value objects, domain services)
- **application/** - Use cases and application services
- **infrastructure/** - Technical implementations (persistence, external services)
- **api/** - REST controllers (inbound adapters)

## Architecture

### Technology Stack

- **Java 21** - Modern Java with records, pattern matching, and improved performance
- **Spring Boot 3.5.6** - Application framework
- **Spring Modulith 1.4.3** - Modular monolith architecture with module boundaries
- **Spring Security** - Security framework with OAuth2 Resource Server
- **Keycloak 23.0** - Identity and Access Management
- **Spring Data JPA** - Data persistence
- **H2 Database** - In-memory database for development
- **PostgreSQL** - Production database (also used for Keycloak)
- **Lombok** - Boilerplate reduction
- **Gradle** - Build automation

### Infrastructure Architecture

The development environment uses separate PostgreSQL instances for better isolation:

```
┌──────────────────────────────────────────────────────────────┐
│                     Application                              │
│                  (Spring Boot)                               │
│                   Port: 8080                                 │
└───────────────────────┬──────────────────────────────────────┘
                        │
                        ↓
            ┌───────────────────────┐
            │  App PostgreSQL       │
            │  Port: 5434           │
            │  Database: eltdb      │
            │  User: eltuser        │
            └───────────────────────┘


┌──────────────────────────────────────────────────────────────┐
│                      Keycloak                                │
│               (Authentication Server)                        │
│                   Port: 8180                                 │
└───────────────────────┬──────────────────────────────────────┘
                        │
                        ↓
            ┌───────────────────────┐
            │ Keycloak PostgreSQL   │
            │  Port: 5433           │
            │  Database: keycloak   │
            │  User: keycloak       │
            └───────────────────────┘
```

**Key Infrastructure Components:**
- **Application PostgreSQL** (port 5434): Stores application data (users, schools, learning paths, enrollments, etc.)
- **Keycloak PostgreSQL** (port 5433): Stores Keycloak's internal data (realms, clients, sessions)
- **Keycloak Server** (port 8180): Handles authentication and authorization
- **Spring Boot Application** (port 8080): Main application server

### Architectural Patterns

#### 1. Spring Modulith (Modular Monolith)

The application is organized into independent modules:

```
┌─────────────────────────────────────────────────────────────┐
│                     User Management                          │
│                    (Core Module)                             │
└─────────────────┬───────────────────────────────────────────┘
                  │
                  │ depends on
                  ↓
┌─────────────────────────────────────────────────────────────┐
│                  School Management                           │
└─────────────┬───────────────────────────────────────────────┘
              │
              │ publishes events
              ↓
┌─────────────────────────────────────────────────────────────┐
│                  Learning Content                            │
│              (creates paths & activities)                    │
└─────────────┬───────────────────────────────────────────────┘
              │
              │ publishes events
              ↓
┌──────────────────────┐         ┌──────────────────────────┐
│    Enrollment        │←──────→│   Student Progress       │
│  (manages enrollments)│  events │ (tracks achievements)   │
└──────────────────────┘         └──────────────────────────┘
```

**Key Principles:**
- Modules communicate via domain events (publish/subscribe)
- Module boundaries are enforced at compile-time
- Each module can be deployed independently (ready for microservices)

#### 2. Domain-Driven Design (DDD)

- **Bounded Contexts**: Each module represents a bounded context
- **Aggregates**: Entities organized with clear boundaries (e.g., User, School)
- **Value Objects**: Immutable types for domain concepts (UserId, Email)
- **Domain Events**: UserRegisteredEvent, EnrollmentCompletedEvent, etc.
- **Repositories**: Abstract data access following DDD patterns

#### 3. Hexagonal Architecture (Ports & Adapters)

```
┌─────────────────────────────────────────────────────────┐
│                     API Layer                           │
│              (AuthenticationController)                 │
└───────────────────────┬─────────────────────────────────┘
                        │ inbound
┌───────────────────────▼─────────────────────────────────┐
│                 Application Layer                       │
│    (RegisterUserService, AuthenticateUserService)       │
└───────────────────────┬─────────────────────────────────┘
                        │ uses
┌───────────────────────▼─────────────────────────────────┐
│                  Domain Layer                           │
│     (User, AuthenticationPort interface)                │
└───────────────────────┬─────────────────────────────────┘
                        │ implemented by
┌───────────────────────▼─────────────────────────────────┐
│              Infrastructure Layer                       │
│    (KeycloakAuthenticationAdapter, UserRepositoryImpl)  │
└─────────────────────────────────────────────────────────┘
```

**Benefits:**
- Domain logic independent of frameworks
- Easy to test with mocks
- Swappable infrastructure (e.g., change Keycloak to Auth0)
- Clear separation of concerns

#### 4. Keycloak Integration

Authentication and authorization are handled by Keycloak:

- **OAuth2/OpenID Connect** - Industry-standard authentication
- **JWT Tokens** - Stateless authentication (5-minute access token, refresh token)
- **Role-Based Access Control** - System roles and school-specific roles
- **Dual Storage** - User data in app DB + credentials in Keycloak

## Development Workflow

### Running in Development Mode

Development mode includes:
- Auto-restart with Spring DevTools
- PostgreSQL database (persistent)
- Detailed logging
- All Actuator endpoints exposed
- CORS enabled for local frontend development

```bash
./gradlew bootRun --args='--spring.profiles.active=dev'
```

### Hot Reload

Spring DevTools provides automatic restart when files change:
- Modify Java files
- Save changes
- Application automatically restarts (usually < 5 seconds)

### Database Access

**PostgreSQL (Development)**

You can connect to the database using any PostgreSQL client (pgAdmin, DBeaver, psql, etc.):

```bash
# Using psql command line
psql -h localhost -p 5434 -U eltuser -d eltdb

# Connection details
Host: localhost
Port: 5434
Database: eltdb
Username: eltuser
Password: eltpass
```

### Logging

Logging levels are configured per profile in `application-{profile}.yml`:

```yaml
logging:
  level:
    root: INFO
    com.learning.tracker: DEBUG
    org.springframework.modulith: DEBUG
    org.hibernate.SQL: DEBUG
```

### Testing Modules

Spring Modulith provides module testing support:

```java
@ApplicationModuleTest
class UserManagementModuleTests {
    // Test module boundaries and dependencies
}
```

### Verifying Module Structure

```bash
# Run tests that verify module boundaries
./gradlew test --tests "*ModuleTest"
```

### Working with Events

Modules communicate through Spring Application Events:

```java
// Publishing an event
@DomainEvents
Collection<Object> domainEvents() {
    return this.events;
}

// Listening to an event
@ApplicationModuleListener
void on(UserRegisteredEvent event) {
    // React to event
}
```

## Troubleshooting

### Common Issues

#### 1. Port Already in Use

**Problem**: `Port 8080 is already in use`

**Solution**:
```bash
# Find process using port
lsof -i :8080  # macOS/Linux
netstat -ano | findstr :8080  # Windows

# Kill the process or change application port in application.yml
server:
  port: 8081
```

#### 2. Docker Services Not Starting

**Problem**: Keycloak or PostgreSQL won't start

**Solution**:
```bash
# Check Docker is running
docker info

# View logs
docker-compose logs keycloak
docker-compose logs postgres

# Restart services
docker-compose down
docker-compose up -d

# Remove volumes and restart (WARNING: deletes data)
docker-compose down -v
docker-compose up -d
```

#### 3. Lombok Not Working

**Problem**: Cannot resolve method/constructor (Lombok annotations not processing)

**Solution**:
- IntelliJ IDEA: Enable annotation processing in Settings
- Eclipse: Install lombok.jar
- Clean and rebuild: `./gradlew clean build`

#### 4. Build Fails

**Problem**: Compilation errors or dependency issues

**Solution**:
```bash
# Clean and rebuild
./gradlew clean build --refresh-dependencies

# Clear Gradle cache
rm -rf ~/.gradle/caches/

# Check Java version
java -version  # Must be Java 21
```

#### 5. Authentication Fails

**Problem**: Login returns 401 Unauthorized

**Solution**:
- Verify Keycloak is running: http://localhost:8180
- Check user exists in Keycloak Admin Console
- Verify application-dev.yml has correct Keycloak URL
- Check Keycloak realm name matches configuration

#### 6. PostgreSQL Connection Issues

**Problem**: Cannot connect to PostgreSQL

**Solution**:
- Ensure Docker services are running: `docker-compose ps`
- Check PostgreSQL is listening: `docker-compose logs elt-postgres`
- Verify connection details in `application-dev.yml`
- Test connection: `psql -h localhost -p 5434 -U eltuser -d eltdb`
- Restart services: `docker-compose restart elt-postgres`

### Getting Help

If you encounter issues not covered here:

1. Check logs: `docker-compose logs` and application console output
2. Review [KEYCLOAK_SETUP.md](KEYCLOAK_SETUP.md) for Keycloak-specific issues
3. Check [Spring Modulith Documentation](https://spring.io/projects/spring-modulith)
4. Review [Keycloak Documentation](https://www.keycloak.org/documentation)

## Additional Documentation

- **[KEYCLOAK_SETUP.md](KEYCLOAK_SETUP.md)** - Comprehensive Keycloak setup and configuration guide
- **[SETUP_SUMMARY.md](SETUP_SUMMARY.md)** - Summary of implemented components and features
- **[CLAUDE.md](CLAUDE.md)** - AI assistant guidelines and project architecture details

## Contributing

### Code Style

- Follow Java naming conventions
- Use meaningful variable and method names
- Add Javadoc for public APIs
- Keep methods focused (Single Responsibility Principle)
- Prefer composition over inheritance

### Module Boundaries

When adding new features:

1. **Identify the bounded context** - Which module owns this functionality?
2. **Use domain events** - Don't directly call other modules
3. **Keep modules independent** - Modules should work in isolation
4. **Test module boundaries** - Use `@ApplicationModuleTest`

### Pull Request Process

1. Create a feature branch from `main`
2. Make your changes following the architecture patterns
3. Add tests for new functionality
4. Ensure build passes: `./gradlew build`
5. Verify module boundaries: `./gradlew test`
6. Create pull request with clear description

### Testing Requirements

- **Unit Tests**: Domain logic must have unit tests
- **Integration Tests**: Infrastructure adapters need integration tests
- **Module Tests**: New modules must have boundary verification tests

```bash
# Run all tests
./gradlew test

# Run with coverage
./gradlew test jacocoTestReport
```

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## Support

For questions or support:
- Create an issue in the repository
- Check existing documentation
- Review Spring Modulith and Keycloak documentation

**Happy Coding! 🚀**
