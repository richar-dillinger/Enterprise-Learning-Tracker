-- ============================================================================
-- Enterprise Learning Tracker - Development Dummy Data
-- ============================================================================
-- This file contains dummy data for development environment
-- IMPORTANT: User data must match Keycloak realm configuration
-- ============================================================================

-- ============================================================================
-- USERS (matching Keycloak realm users)
-- ============================================================================
-- User IDs are predefined for easy reference and relationship setup
-- Emails, names, and roles must match the Keycloak configuration

-- Admin User (System Administrator)
INSERT INTO users (id, email, first_name, last_name, system_role, attributes, active, created_at, updated_at)
VALUES
    ('550e8400-e29b-41d4-a716-446655440001',
     'admin@elt.com',
     'System',
     'Administrator',
     'ADMIN',
     '{"department": "IT", "locale": "en"}',
     true,
     CURRENT_TIMESTAMP - INTERVAL '90 days',
     CURRENT_TIMESTAMP - INTERVAL '1 day');

-- School Admin User (Platform Manager)
INSERT INTO users (id, email, first_name, last_name, system_role, attributes, active, created_at, updated_at)
VALUES
    ('550e8400-e29b-41d4-a716-446655440002',
     'school.admin@elt.com',
     'School',
     'Admin',
     'PLATFORM_MANAGER',
     '{"department": "Education", "locale": "en"}',
     true,
     CURRENT_TIMESTAMP - INTERVAL '60 days',
     CURRENT_TIMESTAMP - INTERVAL '2 days');

-- Tutor User
INSERT INTO users (id, email, first_name, last_name, system_role, attributes, active, created_at, updated_at)
VALUES
    ('550e8400-e29b-41d4-a716-446655440003',
     'tutor@elt.com',
     'John',
     'Tutor',
     'USER',
     '{"department": "Engineering", "locale": "en", "expertise": "Java, Spring Boot"}',
     true,
     CURRENT_TIMESTAMP - INTERVAL '45 days',
     CURRENT_TIMESTAMP - INTERVAL '1 hour');

-- Student User
INSERT INTO users (id, email, first_name, last_name, system_role, attributes, active, created_at, updated_at)
VALUES
    ('550e8400-e29b-41d4-a716-446655440004',
     'student@elt.com',
     'Jane',
     'Student',
     'USER',
     '{"department": "Engineering", "locale": "en", "level": "Junior"}',
     true,
     CURRENT_TIMESTAMP - INTERVAL '30 days',
     CURRENT_TIMESTAMP - INTERVAL '3 hours');

-- Additional Users for realistic data
INSERT INTO users (id, email, first_name, last_name, system_role, attributes, active, created_at, updated_at)
VALUES
    ('550e8400-e29b-41d4-a716-446655440005',
     'maria.garcia@elt.com',
     'Maria',
     'Garcia',
     'USER',
     '{"department": "Engineering", "locale": "es", "expertise": "Python, Data Science"}',
     true,
     CURRENT_TIMESTAMP - INTERVAL '20 days',
     CURRENT_TIMESTAMP - INTERVAL '5 hours');

INSERT INTO users (id, email, first_name, last_name, system_role, attributes, active, created_at, updated_at)
VALUES
    ('550e8400-e29b-41d4-a716-446655440006',
     'robert.chen@elt.com',
     'Robert',
     'Chen',
     'USER',
     '{"department": "Engineering", "locale": "en", "level": "Senior"}',
     true,
     CURRENT_TIMESTAMP - INTERVAL '15 days',
     CURRENT_TIMESTAMP - INTERVAL '1 day');

INSERT INTO users (id, email, first_name, last_name, system_role, attributes, active, created_at, updated_at)
VALUES
    ('550e8400-e29b-41d4-a716-446655440007',
     'sarah.miller@elt.com',
     'Sarah',
     'Miller',
     'USER',
     '{"department": "Marketing", "locale": "en", "level": "Mid"}',
     true,
     CURRENT_TIMESTAMP - INTERVAL '10 days',
     CURRENT_TIMESTAMP - INTERVAL '2 hours');

-- ============================================================================
-- SCHOOLS
-- ============================================================================
INSERT INTO schools (id, name, description, status, created_by, created_at, updated_at)
VALUES
    ('650e8400-e29b-41d4-a716-446655440001',
     'Java Backend Academy',
     'Comprehensive learning paths for Java backend development, covering Spring Boot, microservices, and cloud-native applications.',
     'ACTIVE',
     '550e8400-e29b-41d4-a716-446655440002',
     CURRENT_TIMESTAMP - INTERVAL '60 days',
     CURRENT_TIMESTAMP - INTERVAL '30 days');

INSERT INTO schools (id, name, description, status, created_by, created_at, updated_at)
VALUES
    ('650e8400-e29b-41d4-a716-446655440002',
     'Frontend Development School',
     'Modern frontend development with React, TypeScript, and modern web technologies.',
     'ACTIVE',
     '550e8400-e29b-41d4-a716-446655440002',
     CURRENT_TIMESTAMP - INTERVAL '50 days',
     CURRENT_TIMESTAMP - INTERVAL '20 days');

INSERT INTO schools (id, name, description, status, created_by, created_at, updated_at)
VALUES
    ('650e8400-e29b-41d4-a716-446655440003',
     'Data Science Institute',
     'Data science, machine learning, and analytics training programs.',
     'ACTIVE',
     '550e8400-e29b-41d4-a716-446655440001',
     CURRENT_TIMESTAMP - INTERVAL '40 days',
     CURRENT_TIMESTAMP - INTERVAL '10 days');

INSERT INTO schools (id, name, description, status, created_by, created_at, updated_at)
VALUES
    ('650e8400-e29b-41d4-a716-446655440004',
     'DevOps & Cloud School',
     'DevOps practices, CI/CD, and cloud infrastructure management.',
     'DRAFT',
     '550e8400-e29b-41d4-a716-446655440002',
     CURRENT_TIMESTAMP - INTERVAL '10 days',
     CURRENT_TIMESTAMP - INTERVAL '2 days');

-- ============================================================================
-- USER SCHOOL ROLES
-- ============================================================================
-- Assign school-specific roles to users

-- Java Backend Academy roles
INSERT INTO user_school_roles (user_id, school_id, school_role)
VALUES ('550e8400-e29b-41d4-a716-446655440002', '650e8400-e29b-41d4-a716-446655440001', 'MANAGER');

INSERT INTO user_school_roles (user_id, school_id, school_role)
VALUES ('550e8400-e29b-41d4-a716-446655440003', '650e8400-e29b-41d4-a716-446655440001', 'TUTOR');

INSERT INTO user_school_roles (user_id, school_id, school_role)
VALUES ('550e8400-e29b-41d4-a716-446655440004', '650e8400-e29b-41d4-a716-446655440001', 'STUDENT');

INSERT INTO user_school_roles (user_id, school_id, school_role)
VALUES ('550e8400-e29b-41d4-a716-446655440006', '650e8400-e29b-41d4-a716-446655440001', 'STUDENT');

-- Frontend Development School roles
INSERT INTO user_school_roles (user_id, school_id, school_role)
VALUES ('550e8400-e29b-41d4-a716-446655440002', '650e8400-e29b-41d4-a716-446655440002', 'MANAGER');

INSERT INTO user_school_roles (user_id, school_id, school_role)
VALUES ('550e8400-e29b-41d4-a716-446655440003', '650e8400-e29b-41d4-a716-446655440002', 'TUTOR');

INSERT INTO user_school_roles (user_id, school_id, school_role)
VALUES ('550e8400-e29b-41d4-a716-446655440004', '650e8400-e29b-41d4-a716-446655440002', 'STUDENT');

INSERT INTO user_school_roles (user_id, school_id, school_role)
VALUES ('550e8400-e29b-41d4-a716-446655440007', '650e8400-e29b-41d4-a716-446655440002', 'STUDENT');

-- Data Science Institute roles
INSERT INTO user_school_roles (user_id, school_id, school_role)
VALUES ('550e8400-e29b-41d4-a716-446655440005', '650e8400-e29b-41d4-a716-446655440003', 'TUTOR');

INSERT INTO user_school_roles (user_id, school_id, school_role)
VALUES ('550e8400-e29b-41d4-a716-446655440004', '650e8400-e29b-41d4-a716-446655440003', 'STUDENT');

INSERT INTO user_school_roles (user_id, school_id, school_role)
VALUES ('550e8400-e29b-41d4-a716-446655440006', '650e8400-e29b-41d4-a716-446655440003', 'STUDENT');

-- DevOps & Cloud School roles
INSERT INTO user_school_roles (user_id, school_id, school_role)
VALUES ('550e8400-e29b-41d4-a716-446655440002', '650e8400-e29b-41d4-a716-446655440004', 'MANAGER');

-- ============================================================================
-- LEARNING PATHS
-- ============================================================================

-- Java Backend Academy Paths
INSERT INTO learning_paths (id, school_id, title, description, status, created_by, created_at, updated_at, published_at)
VALUES
    ('750e8400-e29b-41d4-a716-446655440001',
     '650e8400-e29b-41d4-a716-446655440001',
     'Spring Boot Fundamentals',
     'Master the fundamentals of Spring Boot framework, including dependency injection, REST APIs, and data access.',
     'PUBLISHED',
     '550e8400-e29b-41d4-a716-446655440003',
     CURRENT_TIMESTAMP - INTERVAL '45 days',
     CURRENT_TIMESTAMP - INTERVAL '20 days',
     CURRENT_TIMESTAMP - INTERVAL '20 days');

INSERT INTO learning_paths (id, school_id, title, description, status, created_by, created_at, updated_at, published_at)
VALUES
    ('750e8400-e29b-41d4-a716-446655440002',
     '650e8400-e29b-41d4-a716-446655440001',
     'Microservices Architecture',
     'Learn to design and implement microservices using Spring Boot, Docker, and Kubernetes.',
     'PUBLISHED',
     '550e8400-e29b-41d4-a716-446655440003',
     CURRENT_TIMESTAMP - INTERVAL '40 days',
     CURRENT_TIMESTAMP - INTERVAL '15 days',
     CURRENT_TIMESTAMP - INTERVAL '15 days');

INSERT INTO learning_paths (id, school_id, title, description, status, created_by, created_at, updated_at, published_at)
VALUES
    ('750e8400-e29b-41d4-a716-446655440003',
     '650e8400-e29b-41d4-a716-446655440001',
     'Advanced Spring Security',
     'Deep dive into Spring Security, OAuth2, and JWT authentication.',
     'DRAFT',
     '550e8400-e29b-41d4-a716-446655440003',
     CURRENT_TIMESTAMP - INTERVAL '10 days',
     CURRENT_TIMESTAMP - INTERVAL '1 day',
     NULL);

-- Frontend Development School Paths
INSERT INTO learning_paths (id, school_id, title, description, status, created_by, created_at, updated_at, published_at)
VALUES
    ('750e8400-e29b-41d4-a716-446655440004',
     '650e8400-e29b-41d4-a716-446655440002',
     'React Fundamentals',
     'Learn React from basics to advanced patterns including hooks, context, and state management.',
     'PUBLISHED',
     '550e8400-e29b-41d4-a716-446655440003',
     CURRENT_TIMESTAMP - INTERVAL '35 days',
     CURRENT_TIMESTAMP - INTERVAL '10 days',
     CURRENT_TIMESTAMP - INTERVAL '10 days');

INSERT INTO learning_paths (id, school_id, title, description, status, created_by, created_at, updated_at, published_at)
VALUES
    ('750e8400-e29b-41d4-a716-446655440005',
     '650e8400-e29b-41d4-a716-446655440002',
     'TypeScript Mastery',
     'Master TypeScript for building type-safe frontend applications.',
     'PUBLISHED',
     '550e8400-e29b-41d4-a716-446655440003',
     CURRENT_TIMESTAMP - INTERVAL '30 days',
     CURRENT_TIMESTAMP - INTERVAL '8 days',
     CURRENT_TIMESTAMP - INTERVAL '8 days');

-- Data Science Institute Paths
INSERT INTO learning_paths (id, school_id, title, description, status, created_by, created_at, updated_at, published_at)
VALUES
    ('750e8400-e29b-41d4-a716-446655440006',
     '650e8400-e29b-41d4-a716-446655440003',
     'Python for Data Science',
     'Complete guide to Python programming for data analysis, including NumPy, Pandas, and Matplotlib.',
     'PUBLISHED',
     '550e8400-e29b-41d4-a716-446655440005',
     CURRENT_TIMESTAMP - INTERVAL '25 days',
     CURRENT_TIMESTAMP - INTERVAL '5 days',
     CURRENT_TIMESTAMP - INTERVAL '5 days');

-- ============================================================================
-- LEARNING ACTIVITIES
-- ============================================================================

-- Spring Boot Fundamentals Activities
INSERT INTO learning_activities (id, learning_path_id, title, description, type, display_order, estimated_minutes)
VALUES
    ('850e8400-e29b-41d4-a716-446655440001',
     '750e8400-e29b-41d4-a716-446655440001',
     'Introduction to Spring Framework',
     'Overview of Spring Framework, its core concepts, and ecosystem.',
     'READING',
     0,
     45);

INSERT INTO learning_activities (id, learning_path_id, title, description, type, display_order, estimated_minutes)
VALUES
    ('850e8400-e29b-41d4-a716-446655440002',
     '750e8400-e29b-41d4-a716-446655440001',
     'Dependency Injection Deep Dive',
     'Understanding dependency injection and inversion of control in Spring.',
     'VIDEO',
     1,
     60);

INSERT INTO learning_activities (id, learning_path_id, title, description, type, display_order, estimated_minutes)
VALUES
    ('850e8400-e29b-41d4-a716-446655440003',
     '750e8400-e29b-41d4-a716-446655440001',
     'Build Your First REST API',
     'Hands-on exercise to create a RESTful web service using Spring Boot.',
     'EXERCISE',
     2,
     120);

INSERT INTO learning_activities (id, learning_path_id, title, description, type, display_order, estimated_minutes)
VALUES
    ('850e8400-e29b-41d4-a716-446655440004',
     '750e8400-e29b-41d4-a716-446655440001',
     'Spring Data JPA Introduction',
     'Learn to work with databases using Spring Data JPA.',
     'READING',
     3,
     50);

INSERT INTO learning_activities (id, learning_path_id, title, description, type, display_order, estimated_minutes)
VALUES
    ('850e8400-e29b-41d4-a716-446655440005',
     '750e8400-e29b-41d4-a716-446655440001',
     'Spring Boot Assessment Quiz',
     'Test your knowledge of Spring Boot fundamentals.',
     'QUIZ',
     4,
     30);

-- Microservices Architecture Activities
INSERT INTO learning_activities (id, learning_path_id, title, description, type, display_order, estimated_minutes)
VALUES
    ('850e8400-e29b-41d4-a716-446655440006',
     '750e8400-e29b-41d4-a716-446655440002',
     'Microservices Principles',
     'Understanding microservices architecture patterns and best practices.',
     'READING',
     0,
     60);

INSERT INTO learning_activities (id, learning_path_id, title, description, type, display_order, estimated_minutes)
VALUES
    ('850e8400-e29b-41d4-a716-446655440007',
     '750e8400-e29b-41d4-a716-446655440002',
     'Service Discovery with Eureka',
     'Implementing service discovery using Netflix Eureka.',
     'VIDEO',
     1,
     75);

INSERT INTO learning_activities (id, learning_path_id, title, description, type, display_order, estimated_minutes)
VALUES
    ('850e8400-e29b-41d4-a716-446655440008',
     '750e8400-e29b-41d4-a716-446655440002',
     'Building a Microservice Project',
     'Create a complete microservice application with multiple services.',
     'PROJECT',
     2,
     240);

-- React Fundamentals Activities
INSERT INTO learning_activities (id, learning_path_id, title, description, type, display_order, estimated_minutes)
VALUES
    ('850e8400-e29b-41d4-a716-446655440009',
     '750e8400-e29b-41d4-a716-446655440004',
     'React Components and Props',
     'Learn about React components, props, and component composition.',
     'READING',
     0,
     45);

INSERT INTO learning_activities (id, learning_path_id, title, description, type, display_order, estimated_minutes)
VALUES
    ('850e8400-e29b-41d4-a716-446655440010',
     '750e8400-e29b-41d4-a716-446655440004',
     'React Hooks Tutorial',
     'Master useState, useEffect, and custom hooks.',
     'VIDEO',
     1,
     90);

INSERT INTO learning_activities (id, learning_path_id, title, description, type, display_order, estimated_minutes)
VALUES
    ('850e8400-e29b-41d4-a716-446655440011',
     '750e8400-e29b-41d4-a716-446655440004',
     'Build a Todo App with React',
     'Hands-on project to build a fully functional todo application.',
     'PROJECT',
     2,
     180);

-- Python for Data Science Activities
INSERT INTO learning_activities (id, learning_path_id, title, description, type, display_order, estimated_minutes)
VALUES
    ('850e8400-e29b-41d4-a716-446655440012',
     '750e8400-e29b-41d4-a716-446655440006',
     'Python Basics for Data Science',
     'Essential Python programming concepts for data analysis.',
     'READING',
     0,
     60);

INSERT INTO learning_activities (id, learning_path_id, title, description, type, display_order, estimated_minutes)
VALUES
    ('850e8400-e29b-41d4-a716-446655440013',
     '750e8400-e29b-41d4-a716-446655440006',
     'NumPy and Pandas Workshop',
     'Working with NumPy arrays and Pandas DataFrames.',
     'VIDEO',
     1,
     120);

INSERT INTO learning_activities (id, learning_path_id, title, description, type, display_order, estimated_minutes)
VALUES
    ('850e8400-e29b-41d4-a716-446655440014',
     '750e8400-e29b-41d4-a716-446655440006',
     'Data Analysis Project',
     'Analyze a real-world dataset using Python and Pandas.',
     'PROJECT',
     2,
     200);

-- ============================================================================
-- LEARNING RESOURCES
-- ============================================================================

-- Resources for "Introduction to Spring Framework"
INSERT INTO learning_resources (id, activity_id, title, description, type, url, display_order)
VALUES
    ('950e8400-e29b-41d4-a716-446655440001',
     '850e8400-e29b-41d4-a716-446655440001',
     'Spring Framework Documentation',
     'Official Spring Framework reference documentation.',
     'URL',
     'https://docs.spring.io/spring-framework/reference/',
     0);

INSERT INTO learning_resources (id, activity_id, title, description, type, url, display_order)
VALUES
    ('950e8400-e29b-41d4-a716-446655440002',
     '850e8400-e29b-41d4-a716-446655440001',
     'Spring in Action (Book)',
     'Comprehensive guide to Spring Framework.',
     'PDF',
     'https://www.manning.com/books/spring-in-action-sixth-edition',
     1);

-- Resources for "Dependency Injection Deep Dive"
INSERT INTO learning_resources (id, activity_id, title, description, type, url, display_order)
VALUES
    ('950e8400-e29b-41d4-a716-446655440003',
     '850e8400-e29b-41d4-a716-446655440002',
     'Spring DI Tutorial Video',
     'Comprehensive video tutorial on Spring dependency injection.',
     'VIDEO',
     'https://www.youtube.com/watch?v=example-spring-di',
     0);

INSERT INTO learning_resources (id, activity_id, title, description, type, url, display_order)
VALUES
    ('950e8400-e29b-41d4-a716-446655440004',
     '850e8400-e29b-41d4-a716-446655440002',
     'Baeldung DI Guide',
     'Detailed guide on dependency injection patterns in Spring.',
     'URL',
     'https://www.baeldung.com/spring-dependency-injection',
     1);

-- Resources for "Build Your First REST API"
INSERT INTO learning_resources (id, activity_id, title, description, type, url, display_order)
VALUES
    ('950e8400-e29b-41d4-a716-446655440005',
     '850e8400-e29b-41d4-a716-446655440003',
     'Spring Boot REST Tutorial',
     'Step-by-step tutorial for building REST APIs with Spring Boot.',
     'URL',
     'https://spring.io/guides/tutorials/rest/',
     0);

INSERT INTO learning_resources (id, activity_id, title, description, type, url, display_order)
VALUES
    ('950e8400-e29b-41d4-a716-446655440006',
     '850e8400-e29b-41d4-a716-446655440003',
     'REST API Starter Project',
     'GitHub repository with REST API starter template.',
     'CODE',
     'https://github.com/spring-projects/spring-boot/tree/main/spring-boot-samples/spring-boot-sample-web',
     1);

-- Resources for "React Components and Props"
INSERT INTO learning_resources (id, activity_id, title, description, type, url, display_order)
VALUES
    ('950e8400-e29b-41d4-a716-446655440007',
     '850e8400-e29b-41d4-a716-446655440009',
     'React Official Documentation',
     'Official React documentation on components and props.',
     'URL',
     'https://react.dev/learn/passing-props-to-a-component',
     0);

INSERT INTO learning_resources (id, activity_id, title, description, type, url, display_order)
VALUES
    ('950e8400-e29b-41d4-a716-446655440008',
     '850e8400-e29b-41d4-a716-446655440009',
     'React Component Patterns',
     'Advanced component patterns in React.',
     'URL',
     'https://www.patterns.dev/react',
     1);

-- Resources for "Python Basics for Data Science"
INSERT INTO learning_resources (id, activity_id, title, description, type, url, display_order)
VALUES
    ('950e8400-e29b-41d4-a716-446655440009',
     '850e8400-e29b-41d4-a716-446655440012',
     'Python for Data Analysis',
     'Essential Python programming guide for data scientists.',
     'PDF',
     'https://www.oreilly.com/library/view/python-for-data/9781491957653/',
     0);

INSERT INTO learning_resources (id, activity_id, title, description, type, url, display_order)
VALUES
    ('950e8400-e29b-41d4-a716-446655440010',
     '850e8400-e29b-41d4-a716-446655440012',
     'Python Data Science Handbook',
     'Free online handbook for data science with Python.',
     'URL',
     'https://jakevdp.github.io/PythonDataScienceHandbook/',
     1);

-- ============================================================================
-- End of Development Dummy Data
-- ============================================================================
