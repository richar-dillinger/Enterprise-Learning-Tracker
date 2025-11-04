# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Enterprise Learning Tracker is a Spring Boot application built with Spring Modulith architecture for tracking employee learning and development activities.

## 1. Business Context
In the market there are many platforms, courses, and different technological learning resources related to computer science,
this application is an aggregator of these resources and manage them to provide organizations with a management system to govern the learning of the employees of an organization.

### Problem Statement
- What business problem does this system solve?: Manage, track and make available learning paths organized by schools.   
- Who are the target users?: Employees as Student or tutors and managers.
- What pain points does it address?: Creation, Update assignment,enrollment, assessment, and Tracking of learning paths.

### Domain Overview
- ELT (Enterprise Learning Tracker)  
- Key business concepts and terminology
- Business rules and constraints

**Tech Stack:**
- Java 21
- Spring Boot 3.5.6
- Spring Modulith 1.4.3 (modular monolith architecture)
- Gradle build system
- PostgreSQL (for both application data and Keycloak)
- Keycloak 23.0 (authentication & authorization)
- Lombok for boilerplate reduction

## Infrastructure Architecture

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

## Module Architecture
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

---

## 📁 Recommended Project Structure
```
academy-platform/
├── src/main/java/com/academy/
│   ├── shared/                           # Shared Kernel
│   │   ├── domain/
│   │   │   ├── vo/                       # Value Objects
│   │   │   │   ├── UserId.java
│   │   │   │   ├── SchoolId.java
│   │   │   │   ├── PathId.java
│   │   │   │   └── Email.java
│   │   │   └── event/                    # Domain Events
│   │   │       ├── DomainEvent.java
│   │   │       └── EventPublisher.java
│   │   └── infrastructure/
│   │       ├── exception/
│   │       └── utils/
│   │
│   ├── usermanagement/                   # Module 1
│   │   ├── domain/
│   │   │   ├── model/
│   │   │   │   ├── User.java
│   │   │   │   ├── SystemRole.java
│   │   │   │   └── SchoolRole.java
│   │   │   ├── repository/
│   │   │   │   └── UserRepository.java
│   │   │   ├── service/
│   │   │   │   └── UserDomainService.java
│   │   │   └── event/
│   │   │       ├── UserRegisteredEvent.java
│   │   │       └── UserRoleChangedEvent.java
│   │   ├── application/
│   │   │   ├── usecase/
│   │   │   │   ├── RegisterUserUseCase.java
│   │   │   │   └── AssignRoleUseCase.java
│   │   │   └── dto/
│   │   │       ├── UserDTO.java
│   │   │       └── CreateUserRequest.java
│   │   ├── infrastructure/
│   │   │   ├── persistence/
│   │   │   │   ├── UserEntity.java
│   │   │   │   └── UserJpaRepository.java
│   │   │   ├── mapper/
│   │   │   │   └── UserMapper.java
│   │   │   └── keycloak/
│   │   │       └── KeycloakIntegration.java
│   │   └── api/
│   │       └── UserController.java
│   │
│   ├── schoolmanagement/                 # Module 2
│   │   ├── domain/
│   │   │   ├── model/
│   │   │   │   └── School.java
│   │   │   ├── repository/
│   │   │   │   └── SchoolRepository.java
│   │   │   └── event/
│   │   │       └── SchoolCreatedEvent.java
│   │   ├── application/
│   │   │   └── usecase/
│   │   │       └── CreateSchoolUseCase.java
│   │   ├── infrastructure/
│   │   │   └── persistence/
│   │   │       ├── SchoolEntity.java
│   │   │       └── SchoolJpaRepository.java
│   │   └── api/
│   │       └── SchoolController.java
│   │
│   ├── learningcontent/                  # Module 3
│   │   ├── domain/
│   │   │   ├── model/
│   │   │   │   ├── LearningPath.java
│   │   │   │   ├── Activity.java
│   │   │   │   └── Resource.java
│   │   │   ├── repository/
│   │   │   │   └── LearningPathRepository.java
│   │   │   └── event/
│   │   │       ├── PathPublishedEvent.java
│   │   │       └── ActivityCreatedEvent.java
│   │   ├── application/
│   │   │   └── usecase/
│   │   │       ├── CreateLearningPathUseCase.java
│   │   │       └── PublishPathUseCase.java
│   │   ├── infrastructure/
│   │   │   └── persistence/
│   │   │       ├── LearningPathEntity.java
│   │   │       └── ActivityEntity.java
│   │   └── api/
│   │       └── LearningPathController.java
│   │
│   ├── enrollment/                       # Module 4
│   │   ├── domain/
│   │   │   ├── model/
│   │   │   │   └── Enrollment.java
│   │   │   ├── repository/
│   │   │   │   └── EnrollmentRepository.java
│   │   │   └── event/
│   │   │       ├── UserEnrolledEvent.java
│   │   │       └── EnrollmentCompletedEvent.java
│   │   ├── application/
│   │   │   └── usecase/
│   │   │       ├── EnrollUserUseCase.java
│   │   │       └── AssignUserToPathUseCase.java
│   │   ├── infrastructure/
│   │   │   └── persistence/
│   │   │       └── EnrollmentEntity.java
│   │   └── api/
│   │       └── EnrollmentController.java
│   │
│   └── studentprogress/                  # Module 5
│       ├── domain/
│       │   ├── model/
│       │   │   ├── Profile.java
│       │   │   └── Badge.java
│       │   ├── repository/
│       │   │   └── ProfileRepository.java
│       │   └── event/
│       │       └── BadgeAwardedEvent.java
│       ├── application/
│       │   ├── usecase/
│       │   │   ├── UpdateProgressUseCase.java
│       │   │   └── AwardBadgeUseCase.java
│       │   └── eventlistener/
│       │       └── EnrollmentEventListener.java
│       ├── infrastructure/
│       │   └── persistence/
│       │       └── ProfileEntity.java
│       └── api/
│           └── ProgressController.java
│
└── resources/
└── application.yml
```
### Architecture style
This project follows a **Modular Monolith** architecture using **Spring Modulith**, implementing **Domain-Driven Design (DDD)** principles and structured as **Hexagonal Architecture** (Ports & Adapters).

### Core Architectural Patterns

#### 1. Spring Modulith
- Each business capability is encapsulated in an independent module
- Modules communicate through well-defined interfaces and events
- Internal module structure is hidden from other modules
- Spring Modulith validates module boundaries at compile-time and runtime

#### 2. Domain-Driven Design (DDD)
- **Bounded Contexts**: Each module represents a bounded context
- **Ubiquitous Language**: Domain terms are consistently used in code and documentation
- **Aggregates**: Domain entities are organized into aggregates with clear boundaries
- **Domain Events**: Modules communicate through domain events for loose coupling
- **Value Objects**: Immutable objects representing domain concepts
- **Repositories**: Abstract data access following DDD repository pattern

#### 3. Hexagonal Architecture (Ports & Adapters)
Each module is structured in three layers:
```
module-name/
├── domain/                 # Core business logic (hexagon center)
│   ├── model/             # Entities, Value Objects, Aggregates
│   ├── service/           # Domain Services
│   ├── event/             # Domain Events
│   └── port/              # Ports (interfaces)
│       ├── in/            # Inbound ports (use cases)
│       └── out/           # Outbound ports (repositories, external services)
├── application/            # Application/Use Case layer
│   └── service/           # Application Services implementing inbound ports
└── infrastructure/         # Adapters (hexagon edges)
    ├── adapter/
    │   ├── in/            # Inbound adapters (REST controllers, event listeners)
    │   └── out/           # Outbound adapters (JPA repositories, external clients)
    └── config/            # Module-specific configuration
```

## Module Structure Guidelines

### Domain Layer (Core)
- **No dependencies** on infrastructure or framework code
- Contains pure business logic and domain rules
- Defines ports (interfaces) for external interactions
- Uses Java records for Value Objects when appropriate
- Domain events for inter-module communication
```java
// Example: Domain model
public class Order {
    private OrderId id;
    private CustomerId customerId;
    private Money totalAmount;
    private OrderStatus status;
    
    public void confirm() {
        // Business logic
        this.status = OrderStatus.CONFIRMED;
        registerEvent(new OrderConfirmedEvent(this.id));
    }
}

// Example: Outbound port
public interface OrderRepository {
    Order save(Order order);
    Optional findById(OrderId id);
}
```

### Application Layer
- Orchestrates domain objects to fulfill use cases
- Implements inbound ports (use case interfaces)
- Manages transactions
- Publishes domain events
```java
@Service
@Transactional
public class OrderApplicationService implements ConfirmOrderUseCase {
    private final OrderRepository orderRepository;
    
    @Override
    public void confirm(OrderId orderId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new OrderNotFoundException(orderId));
        order.confirm();
        orderRepository.save(order);
    }
}
```

### Infrastructure Layer
- **Inbound Adapters**: REST controllers, event listeners, scheduled tasks
- **Outbound Adapters**: JPA repositories, external API clients, message publishers
- Depends on domain and application layers
- Maps between domain models and external representations (DTOs)
```java
// Inbound adapter
@RestController
@RequestMapping("/api/orders")
class OrderController {
    private final ConfirmOrderUseCase confirmOrderUseCase;
    
    @PostMapping("/{id}/confirm")
    public ResponseEntity confirmOrder(@PathVariable String id) {
        confirmOrderUseCase.confirm(new OrderId(id));
        return ResponseEntity.ok().build();
    }
}

// Outbound adapter
@Repository
class JpaOrderRepository implements OrderRepository {
    private final OrderJpaRepository jpaRepository;
    private final OrderMapper mapper;
    
    @Override
    public Order save(Order order) {
        OrderEntity entity = mapper.toEntity(order);
        OrderEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }
}
```

## Module Communication

### 1. Direct Dependency (Within Bounded Context)
```java
// Only through inbound ports (use case interfaces)
@Service
public class ShippingService {
    private final ConfirmOrderUseCase confirmOrderUseCase; // Port, not implementation
}
```

### 2. Event-Driven Communication (Between Bounded Contexts)
```java
// Publishing module
@DomainEvents
Collection domainEvents() {
    return this.events;
}

// Subscribing module
@ApplicationModuleListener
void on(OrderConfirmedEvent event) {
    // React to event from another module
}
```

## Key Principles

1. **Dependency Rule**: Dependencies point inward (Infrastructure → Application → Domain)
2. **Module Independence**: Modules should be deployable as microservices with minimal changes
3. **Explicit APIs**: Module public APIs are clearly defined in the root package
4. **Event-First**: Prefer events over direct calls between modules
5. **Package by Feature**: Organize by business capability, not technical layers
6. **Testability**: Domain logic is easily testable without infrastructure

## Testing Strategy

- **Domain Layer**: Pure unit tests, no Spring context
- **Application Layer**: Integration tests with test doubles for ports
- **Infrastructure Layer**: Integration tests with real adapters
- **Module Tests**: Spring Modulith's `@ApplicationModuleTest` for module boundary validation

## Technology Alignment

- **Spring Boot 3.x**: Application framework
- **Spring Data JPA**: Persistence adapter implementation
- **Spring Modulith**: Module boundary enforcement and event handling
- **PostgreSQL**: Application database (development & production)
- **PostgreSQL**: Keycloak persistence database (separate instance)
- **Keycloak 23.0**: Authentication/Authorization server
- **Docker & Docker Compose**: Container orchestration for development environment

This project uses **Spring Modulith**, a modular monolith architecture pattern. Key architectural principles:

- **Module Structure**: Code is organized into domain modules under `com.learning.tracker.*`. Each module should be a self-contained bounded context with minimal coupling to other modules.

- **Module Communication**: Modules communicate via Spring Application Events (publish/subscribe pattern) rather than direct method calls. This ensures loose coupling and module independence.

- **Module Verification**: Spring Modulith provides runtime verification of module boundaries and dependencies. Tests can verify module structure and detect violations.

- **Observability**: Spring Modulith Actuator and Observability modules are included for monitoring module interactions and application events.

- **Database**: JPA is used for persistence. Each module can maintain its own entities, but cross-module database access should go through event-driven integration.

## Package Structure

The base package is `com.learning.tracker`. When adding new modules:
- Create a new package under the base (e.g., `com.learning.tracker.courses`, `com.learning.tracker.employees`)
- Keep module internals private; only expose necessary APIs through public classes
- Use `package-info.java` to document module responsibilities and boundaries

## Configuration

- **Application properties**: `src/main/resources/application.yml` and profile-specific configurations
- **Main application class**: `src/main/java/com/learning/tracker/TrackerApplication.java`
- **Database**: PostgreSQL for both application data and Keycloak persistence (separate instances)
- **Profiles**:
  - `dev`: Development with PostgreSQL, detailed logging, all actuator endpoints
  - `test`: Testing profile
  - `prod`: Production profile

## Development

- **Infrastructure**: Start services with `./start-dev.sh` (starts both PostgreSQL instances and Keycloak)
- **Database Access**:
  - Application DB: `psql -h localhost -p 5434 -U eltuser -d eltdb`
  - Keycloak DB: `psql -h localhost -p 5433 -U keycloak -d keycloak`
- **Lombok**: Use Lombok annotations to reduce boilerplate. Ensure your IDE has Lombok annotation processing enabled.
- **DevTools**: Spring Boot DevTools is included for automatic application restart during development.
- **Actuator**: Spring Boot Actuator endpoints available for application monitoring and health checks.


## Build Commands

**Build the project:**
```bash
./gradlew build
```

**Run tests:**
```bash
./gradlew test
```

**Run a single test class:**
```bash
./gradlew test --tests com.learning.tracker.YourTestClass
```

**Run a single test method:**
```bash
./gradlew test --tests com.learning.tracker.YourTestClass.testMethodName
```

**Run the application:**
```bash
./gradlew bootRun
```

**Clean build artifacts:**
```bash
./gradlew clean
```

**Generate bootable JAR:**
```bash
./gradlew bootJar
```