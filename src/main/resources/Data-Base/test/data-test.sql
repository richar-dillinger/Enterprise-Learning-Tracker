-- ============================================================================
-- Enterprise Learning Tracker - Test Dummy Data
-- ============================================================================
-- This file contains dummy data for test environment
-- IMPORTANT: User data must match Keycloak realm configuration
-- Smaller dataset optimized for automated testing
-- ============================================================================

-- ============================================================================
-- USERS (matching Keycloak realm users)
-- ============================================================================
-- Using the same user structure as development for consistency

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

-- Additional test user for multi-role scenarios
INSERT INTO users (id, email, first_name, last_name, system_role, attributes, active, created_at, updated_at)
VALUES
    ('550e8400-e29b-41d4-a716-446655440005',
     'test.user@elt.com',
     'Test',
     'User',
     'USER',
     '{"department": "QA", "locale": "en"}',
     true,
     CURRENT_TIMESTAMP - INTERVAL '10 days',
     CURRENT_TIMESTAMP - INTERVAL '1 hour');

-- Inactive user for testing user activation/deactivation
INSERT INTO users (id, email, first_name, last_name, system_role, attributes, active, created_at, updated_at)
VALUES
    ('550e8400-e29b-41d4-a716-446655440006',
     'inactive.user@elt.com',
     'Inactive',
     'User',
     'USER',
     '{"department": "Engineering", "locale": "en"}',
     false,
     CURRENT_TIMESTAMP - INTERVAL '100 days',
     CURRENT_TIMESTAMP - INTERVAL '50 days');

-- ============================================================================
-- SCHOOLS
-- ============================================================================
-- Test school in ACTIVE status
INSERT INTO schools (id, name, description, status, created_by, created_at, updated_at)
VALUES
    ('650e8400-e29b-41d4-a716-446655440001',
     'Test Academy',
     'Test school for automated testing and development.',
     'ACTIVE',
     '550e8400-e29b-41d4-a716-446655440002',
     CURRENT_TIMESTAMP - INTERVAL '60 days',
     CURRENT_TIMESTAMP - INTERVAL '30 days');

-- Test school in DRAFT status
INSERT INTO schools (id, name, description, status, created_by, created_at, updated_at)
VALUES
    ('650e8400-e29b-41d4-a716-446655440002',
     'Draft School',
     'School in draft status for testing school lifecycle.',
     'DRAFT',
     '550e8400-e29b-41d4-a716-446655440002',
     CURRENT_TIMESTAMP - INTERVAL '10 days',
     CURRENT_TIMESTAMP - INTERVAL '5 days');

-- Test school in SUSPENDED status
INSERT INTO schools (id, name, description, status, created_by, created_at, updated_at)
VALUES
    ('650e8400-e29b-41d4-a716-446655440003',
     'Suspended School',
     'School in suspended status for testing suspension features.',
     'SUSPENDED',
     '550e8400-e29b-41d4-a716-446655440001',
     CURRENT_TIMESTAMP - INTERVAL '50 days',
     CURRENT_TIMESTAMP - INTERVAL '2 days');

-- ============================================================================
-- USER SCHOOL ROLES
-- ============================================================================
-- Test Academy roles
INSERT INTO user_school_roles (user_id, school_id, school_role)
VALUES ('550e8400-e29b-41d4-a716-446655440002', '650e8400-e29b-41d4-a716-446655440001', 'MANAGER');

INSERT INTO user_school_roles (user_id, school_id, school_role)
VALUES ('550e8400-e29b-41d4-a716-446655440003', '650e8400-e29b-41d4-a716-446655440001', 'TUTOR');

INSERT INTO user_school_roles (user_id, school_id, school_role)
VALUES ('550e8400-e29b-41d4-a716-446655440004', '650e8400-e29b-41d4-a716-446655440001', 'STUDENT');

INSERT INTO user_school_roles (user_id, school_id, school_role)
VALUES ('550e8400-e29b-41d4-a716-446655440005', '650e8400-e29b-41d4-a716-446655440001', 'STUDENT');

-- Draft School roles
INSERT INTO user_school_roles (user_id, school_id, school_role)
VALUES ('550e8400-e29b-41d4-a716-446655440002', '650e8400-e29b-41d4-a716-446655440002', 'MANAGER');

INSERT INTO user_school_roles (user_id, school_id, school_role)
VALUES ('550e8400-e29b-41d4-a716-446655440003', '650e8400-e29b-41d4-a716-446655440002', 'TUTOR');

-- Suspended School roles
INSERT INTO user_school_roles (user_id, school_id, school_role)
VALUES ('550e8400-e29b-41d4-a716-446655440002', '650e8400-e29b-41d4-a716-446655440003', 'MANAGER');

-- ============================================================================
-- LEARNING PATHS
-- ============================================================================
-- Published learning path for enrollment testing
INSERT INTO learning_paths (id, school_id, title, description, status, created_by, created_at, updated_at, published_at)
VALUES
    ('750e8400-e29b-41d4-a716-446655440001',
     '650e8400-e29b-41d4-a716-446655440001',
     'Test Learning Path - Published',
     'Published learning path for testing enrollment and progress tracking.',
     'PUBLISHED',
     '550e8400-e29b-41d4-a716-446655440003',
     CURRENT_TIMESTAMP - INTERVAL '30 days',
     CURRENT_TIMESTAMP - INTERVAL '10 days',
     CURRENT_TIMESTAMP - INTERVAL '10 days');

-- Draft learning path for testing path creation and editing
INSERT INTO learning_paths (id, school_id, title, description, status, created_by, created_at, updated_at, published_at)
VALUES
    ('750e8400-e29b-41d4-a716-446655440002',
     '650e8400-e29b-41d4-a716-446655440001',
     'Test Learning Path - Draft',
     'Draft learning path for testing path lifecycle and publication.',
     'DRAFT',
     '550e8400-e29b-41d4-a716-446655440003',
     CURRENT_TIMESTAMP - INTERVAL '5 days',
     CURRENT_TIMESTAMP - INTERVAL '1 day',
     NULL);

-- Archived learning path for testing archive functionality
INSERT INTO learning_paths (id, school_id, title, description, status, created_by, created_at, updated_at, published_at)
VALUES
    ('750e8400-e29b-41d4-a716-446655440003',
     '650e8400-e29b-41d4-a716-446655440001',
     'Test Learning Path - Archived',
     'Archived learning path for testing archive and historical data.',
     'ARCHIVED',
     '550e8400-e29b-41d4-a716-446655440003',
     CURRENT_TIMESTAMP - INTERVAL '90 days',
     CURRENT_TIMESTAMP - INTERVAL '20 days',
     CURRENT_TIMESTAMP - INTERVAL '60 days');

-- Empty learning path for testing path with no activities
INSERT INTO learning_paths (id, school_id, title, description, status, created_by, created_at, updated_at, published_at)
VALUES
    ('750e8400-e29b-41d4-a716-446655440004',
     '650e8400-e29b-41d4-a716-446655440002',
     'Empty Learning Path',
     'Learning path with no activities for testing edge cases.',
     'DRAFT',
     '550e8400-e29b-41d4-a716-446655440003',
     CURRENT_TIMESTAMP - INTERVAL '2 days',
     CURRENT_TIMESTAMP - INTERVAL '1 day',
     NULL);

-- ============================================================================
-- LEARNING ACTIVITIES
-- ============================================================================
-- Activities for "Test Learning Path - Published"
INSERT INTO learning_activities (id, learning_path_id, title, description, type, display_order, estimated_minutes)
VALUES
    ('850e8400-e29b-41d4-a716-446655440001',
     '750e8400-e29b-41d4-a716-446655440001',
     'Introduction Module',
     'Introduction to the learning path and its objectives.',
     'READING',
     0,
     30);

INSERT INTO learning_activities (id, learning_path_id, title, description, type, display_order, estimated_minutes)
VALUES
    ('850e8400-e29b-41d4-a716-446655440002',
     '750e8400-e29b-41d4-a716-446655440001',
     'Core Concepts Video',
     'Video tutorial covering core concepts.',
     'VIDEO',
     1,
     45);

INSERT INTO learning_activities (id, learning_path_id, title, description, type, display_order, estimated_minutes)
VALUES
    ('850e8400-e29b-41d4-a716-446655440003',
     '750e8400-e29b-41d4-a716-446655440001',
     'Hands-on Exercise',
     'Practice exercise to apply learned concepts.',
     'EXERCISE',
     2,
     60);

INSERT INTO learning_activities (id, learning_path_id, title, description, type, display_order, estimated_minutes)
VALUES
    ('850e8400-e29b-41d4-a716-446655440004',
     '750e8400-e29b-41d4-a716-446655440001',
     'Knowledge Check Quiz',
     'Quiz to assess understanding of the material.',
     'QUIZ',
     3,
     20);

INSERT INTO learning_activities (id, learning_path_id, title, description, type, display_order, estimated_minutes)
VALUES
    ('850e8400-e29b-41d4-a716-446655440005',
     '750e8400-e29b-41d4-a716-446655440001',
     'Final Project',
     'Capstone project to demonstrate mastery.',
     'PROJECT',
     4,
     120);

-- Activities for "Test Learning Path - Draft"
INSERT INTO learning_activities (id, learning_path_id, title, description, type, display_order, estimated_minutes)
VALUES
    ('850e8400-e29b-41d4-a716-446655440006',
     '750e8400-e29b-41d4-a716-446655440002',
     'Draft Activity 1',
     'First activity in draft learning path.',
     'READING',
     0,
     25);

INSERT INTO learning_activities (id, learning_path_id, title, description, type, display_order, estimated_minutes)
VALUES
    ('850e8400-e29b-41d4-a716-446655440007',
     '750e8400-e29b-41d4-a716-446655440002',
     'Draft Activity 2',
     'Second activity in draft learning path.',
     'VIDEO',
     1,
     40);

-- Activities for "Test Learning Path - Archived"
INSERT INTO learning_activities (id, learning_path_id, title, description, type, display_order, estimated_minutes)
VALUES
    ('850e8400-e29b-41d4-a716-446655440008',
     '750e8400-e29b-41d4-a716-446655440003',
     'Archived Activity',
     'Activity in an archived learning path.',
     'READING',
     0,
     30);

-- ============================================================================
-- LEARNING RESOURCES
-- ============================================================================
-- Resources for "Introduction Module"
INSERT INTO learning_resources (id, activity_id, title, description, type, url, display_order)
VALUES
    ('950e8400-e29b-41d4-a716-446655440001',
     '850e8400-e29b-41d4-a716-446655440001',
     'Getting Started Guide',
     'Official getting started documentation.',
     'URL',
     'https://docs.example.com/getting-started',
     0);

INSERT INTO learning_resources (id, activity_id, title, description, type, url, display_order)
VALUES
    ('950e8400-e29b-41d4-a716-446655440002',
     '850e8400-e29b-41d4-a716-446655440001',
     'Introduction Article',
     'Comprehensive introduction article.',
     'TEXT',
     'https://blog.example.com/introduction',
     1);

-- Resources for "Core Concepts Video"
INSERT INTO learning_resources (id, activity_id, title, description, type, url, display_order)
VALUES
    ('950e8400-e29b-41d4-a716-446655440003',
     '850e8400-e29b-41d4-a716-446655440002',
     'Core Concepts Tutorial',
     'Video tutorial on core concepts.',
     'VIDEO',
     'https://youtube.com/watch?v=example',
     0);

INSERT INTO learning_resources (id, activity_id, title, description, type, url, display_order)
VALUES
    ('950e8400-e29b-41d4-a716-446655440004',
     '850e8400-e29b-41d4-a716-446655440002',
     'Supplementary Reading',
     'Additional reading material for core concepts.',
     'TEXT',
     'https://example.com/core-concepts',
     1);

-- Resources for "Hands-on Exercise"
INSERT INTO learning_resources (id, activity_id, title, description, type, url, display_order)
VALUES
    ('950e8400-e29b-41d4-a716-446655440005',
     '850e8400-e29b-41d4-a716-446655440003',
     'Exercise Starter Code',
     'GitHub repository with starter code for the exercise.',
     'CODE',
     'https://github.com/example/exercise-starter',
     0);

INSERT INTO learning_resources (id, activity_id, title, description, type, url, display_order)
VALUES
    ('950e8400-e29b-41d4-a716-446655440006',
     '850e8400-e29b-41d4-a716-446655440003',
     'Exercise Instructions',
     'Detailed step-by-step instructions.',
     'URL',
     'https://example.com/exercise-guide',
     1);

-- Resources for "Final Project"
INSERT INTO learning_resources (id, activity_id, title, description, type, url, display_order)
VALUES
    ('950e8400-e29b-41d4-a716-446655440007',
     '850e8400-e29b-41d4-a716-446655440005',
     'Project Requirements Document',
     'Complete project requirements and specifications.',
     'PDF',
     'https://docs.example.com/project-requirements',
     0);

INSERT INTO learning_resources (id, activity_id, title, description, type, url, display_order)
VALUES
    ('950e8400-e29b-41d4-a716-446655440008',
     '850e8400-e29b-41d4-a716-446655440005',
     'Project Template',
     'GitHub repository with project template.',
     'CODE',
     'https://github.com/example/project-template',
     1);

INSERT INTO learning_resources (id, activity_id, title, description, type, url, display_order)
VALUES
    ('950e8400-e29b-41d4-a716-446655440009',
     '850e8400-e29b-41d4-a716-446655440005',
     'Example Projects Gallery',
     'Collection of example completed projects.',
     'FILE',
     'https://example.com/project-gallery',
     2);

-- ============================================================================
-- End of Test Dummy Data
-- ============================================================================
