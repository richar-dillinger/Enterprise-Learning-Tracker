-- ============================================================================
-- Enterprise Learning Tracker - Database Schema
-- ============================================================================
-- This schema is based on JPA entities with Spring Modulith architecture
-- Database: PostgreSQL 14+
-- Generated from: Persistence Entities (DDL-auto equivalent)
-- ============================================================================

-- Enable UUID extension for PostgreSQL
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- ============================================================================
-- Drop existing tables (in reverse dependency order)
-- ============================================================================
DROP TABLE IF EXISTS user_school_roles CASCADE;
DROP TABLE IF EXISTS learning_resources CASCADE;
DROP TABLE IF EXISTS learning_activities CASCADE;
DROP TABLE IF EXISTS learning_paths CASCADE;
DROP TABLE IF EXISTS schools CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS event_publication CASCADE;

-- ============================================================================
-- Table: users
-- Purpose: Store user information with custom attributes
-- Module: usermanagement
-- ============================================================================
CREATE TABLE users (
    id UUID PRIMARY KEY,
    email VARCHAR(254) NOT NULL UNIQUE,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    system_role VARCHAR(50) NOT NULL,
    attributes JSONB DEFAULT '{}'::JSONB,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT chk_system_role CHECK (system_role IN ('ADMIN', 'PLATFORM_MANAGER', 'USER'))
);

-- Index for email lookups
CREATE INDEX idx_users_email ON users(email);

-- Index for active users
CREATE INDEX idx_users_active ON users(active);

-- Index for JSONB attributes (GIN index for efficient JSON queries)
CREATE INDEX idx_users_attributes ON users USING GIN (attributes);

-- Comments
COMMENT ON TABLE users IS 'User aggregate root - stores user information and system-wide roles';
COMMENT ON COLUMN users.id IS 'User unique identifier (UserId value object)';
COMMENT ON COLUMN users.email IS 'User email address (Email value object)';
COMMENT ON COLUMN users.system_role IS 'System-wide role (ADMIN, PLATFORM_MANAGER, USER)';
COMMENT ON COLUMN users.attributes IS 'Custom user attributes stored as JSONB for flexible schema extension';
COMMENT ON COLUMN users.active IS 'User active status flag';

-- ============================================================================
-- Table: user_school_roles
-- Purpose: Store school-specific roles for users (ElementCollection)
-- Module: usermanagement
-- ============================================================================
CREATE TABLE user_school_roles (
    user_id UUID NOT NULL,
    school_id UUID NOT NULL,
    school_role VARCHAR(50) NOT NULL,

    PRIMARY KEY (user_id, school_id),
    CONSTRAINT fk_user_school_roles_user FOREIGN KEY (user_id)
        REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT chk_school_role CHECK (school_role IN ('MANAGER', 'TUTOR', 'STUDENT'))
);

-- Index for school lookups
CREATE INDEX idx_user_school_roles_school ON user_school_roles(school_id);

-- Comments
COMMENT ON TABLE user_school_roles IS 'Map of school-specific roles assigned to users';
COMMENT ON COLUMN user_school_roles.user_id IS 'Reference to user';
COMMENT ON COLUMN user_school_roles.school_id IS 'Reference to school (SchoolId value object)';
COMMENT ON COLUMN user_school_roles.school_role IS 'School-specific role (MANAGER, TUTOR, STUDENT)';

-- ============================================================================
-- Table: schools
-- Purpose: Store school/organization information
-- Module: schoolmanagement
-- ============================================================================
CREATE TABLE schools (
    id UUID PRIMARY KEY,
    name VARCHAR(200) NOT NULL UNIQUE,
    description VARCHAR(1000),
    status VARCHAR(20) NOT NULL,
    created_by UUID NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_schools_created_by FOREIGN KEY (created_by)
        REFERENCES users(id),
    CONSTRAINT chk_school_status CHECK (status IN ('DRAFT', 'ACTIVE', 'SUSPENDED', 'ARCHIVED'))
);

-- Index for name lookups
CREATE INDEX idx_schools_name ON schools(name);

-- Index for status filtering
CREATE INDEX idx_schools_status ON schools(status);

-- Index for creator lookups
CREATE INDEX idx_schools_created_by ON schools(created_by);

-- Comments
COMMENT ON TABLE schools IS 'School aggregate root - represents organizations or institutions';
COMMENT ON COLUMN schools.id IS 'School unique identifier (SchoolId value object)';
COMMENT ON COLUMN schools.name IS 'School name (must be unique)';
COMMENT ON COLUMN schools.status IS 'School status (DRAFT, ACTIVE, SUSPENDED, ARCHIVED)';
COMMENT ON COLUMN schools.created_by IS 'User who created this school';

-- ============================================================================
-- Table: learning_paths
-- Purpose: Store learning paths within schools
-- Module: learningcontent
-- ============================================================================
CREATE TABLE learning_paths (
    id UUID PRIMARY KEY,
    school_id UUID NOT NULL,
    title VARCHAR(200) NOT NULL,
    description VARCHAR(2000),
    status VARCHAR(20) NOT NULL,
    created_by UUID NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    published_at TIMESTAMP,

    CONSTRAINT fk_learning_paths_school FOREIGN KEY (school_id)
        REFERENCES schools(id) ON DELETE CASCADE,
    CONSTRAINT fk_learning_paths_created_by FOREIGN KEY (created_by)
        REFERENCES users(id),
    CONSTRAINT chk_path_status CHECK (status IN ('DRAFT', 'PUBLISHED', 'ARCHIVED'))
);

-- Index for school lookups
CREATE INDEX idx_learning_paths_school ON learning_paths(school_id);

-- Index for status filtering
CREATE INDEX idx_learning_paths_status ON learning_paths(status);

-- Index for creator lookups
CREATE INDEX idx_learning_paths_created_by ON learning_paths(created_by);

-- Index for published paths
CREATE INDEX idx_learning_paths_published ON learning_paths(published_at)
    WHERE published_at IS NOT NULL;

-- Comments
COMMENT ON TABLE learning_paths IS 'Learning path aggregate - collection of learning activities';
COMMENT ON COLUMN learning_paths.id IS 'Learning path unique identifier (PathId value object)';
COMMENT ON COLUMN learning_paths.school_id IS 'School that owns this learning path';
COMMENT ON COLUMN learning_paths.status IS 'Path status (DRAFT, PUBLISHED, ARCHIVED)';
COMMENT ON COLUMN learning_paths.published_at IS 'Timestamp when path was published';

-- ============================================================================
-- Table: learning_activities
-- Purpose: Store learning activities within paths
-- Module: learningcontent
-- ============================================================================
CREATE TABLE learning_activities (
    id UUID PRIMARY KEY,
    learning_path_id UUID NOT NULL,
    title VARCHAR(200) NOT NULL,
    description VARCHAR(2000),
    type VARCHAR(20) NOT NULL,
    display_order INTEGER NOT NULL,
    estimated_minutes INTEGER NOT NULL,

    CONSTRAINT fk_learning_activities_path FOREIGN KEY (learning_path_id)
        REFERENCES learning_paths(id) ON DELETE CASCADE,
    CONSTRAINT chk_activity_type CHECK (type IN ('READING', 'VIDEO', 'EXERCISE', 'QUIZ', 'PROJECT', 'DISCUSSION')),
    CONSTRAINT chk_display_order CHECK (display_order >= 0),
    CONSTRAINT chk_estimated_minutes CHECK (estimated_minutes >= 0)
);

-- Index for path lookups
CREATE INDEX idx_learning_activities_path ON learning_activities(learning_path_id);

-- Index for ordering within a path
CREATE INDEX idx_learning_activities_order ON learning_activities(learning_path_id, display_order);

-- Comments
COMMENT ON TABLE learning_activities IS 'Learning activities within a learning path';
COMMENT ON COLUMN learning_activities.id IS 'Activity unique identifier (ActivityId value object)';
COMMENT ON COLUMN learning_activities.learning_path_id IS 'Parent learning path';
COMMENT ON COLUMN learning_activities.type IS 'Activity type (READING, VIDEO, EXERCISE, QUIZ, PROJECT, DISCUSSION)';
COMMENT ON COLUMN learning_activities.display_order IS 'Order of activity within the path (0-based)';
COMMENT ON COLUMN learning_activities.estimated_minutes IS 'Estimated time to complete in minutes';

-- ============================================================================
-- Table: learning_resources
-- Purpose: Store resources attached to activities
-- Module: learningcontent
-- ============================================================================
CREATE TABLE learning_resources (
    id UUID PRIMARY KEY,
    activity_id UUID NOT NULL,
    title VARCHAR(200) NOT NULL,
    description VARCHAR(1000),
    type VARCHAR(20) NOT NULL,
    url VARCHAR(2000) NOT NULL,
    display_order INTEGER NOT NULL,

    CONSTRAINT fk_learning_resources_activity FOREIGN KEY (activity_id)
        REFERENCES learning_activities(id) ON DELETE CASCADE,
    CONSTRAINT chk_resource_type CHECK (type IN ('ARTICLE', 'VIDEO', 'DOCUMENTATION', 'TUTORIAL', 'REPOSITORY', 'TOOL', 'OTHER')),
    CONSTRAINT chk_resource_display_order CHECK (display_order >= 0)
);

-- Index for activity lookups
CREATE INDEX idx_learning_resources_activity ON learning_resources(activity_id);

-- Index for ordering within an activity
CREATE INDEX idx_learning_resources_order ON learning_resources(activity_id, display_order);

-- Comments
COMMENT ON TABLE learning_resources IS 'Resources attached to learning activities';
COMMENT ON COLUMN learning_resources.id IS 'Resource unique identifier (ResourceId value object)';
COMMENT ON COLUMN learning_resources.activity_id IS 'Parent activity';
COMMENT ON COLUMN learning_resources.type IS 'Resource type (ARTICLE, VIDEO, DOCUMENTATION, TUTORIAL, REPOSITORY, TOOL, OTHER)';
COMMENT ON COLUMN learning_resources.url IS 'URL to the resource';
COMMENT ON COLUMN learning_resources.display_order IS 'Order of resource within the activity';

-- ============================================================================
-- Table: event_publication
-- Purpose: Spring Modulith event publication tracking
-- Module: Spring Modulith (framework)
-- ============================================================================
CREATE TABLE event_publication (
    id UUID PRIMARY KEY,
    listener_id VARCHAR(512) NOT NULL,
    event_type VARCHAR(512) NOT NULL,
    serialized_event TEXT NOT NULL,
    publication_date TIMESTAMP NOT NULL,
    completion_date TIMESTAMP,

    CONSTRAINT idx_event_publication_by_completion_date
        CHECK (completion_date IS NULL OR completion_date >= publication_date)
);

-- Index for finding incomplete publications
CREATE INDEX idx_event_publication_incomplete
    ON event_publication(completion_date)
    WHERE completion_date IS NULL;

-- Index for publication date ordering
CREATE INDEX idx_event_publication_date ON event_publication(publication_date);

-- Comments
COMMENT ON TABLE event_publication IS 'Spring Modulith event publication log for reliable event processing';
COMMENT ON COLUMN event_publication.listener_id IS 'Identifier of the event listener';
COMMENT ON COLUMN event_publication.event_type IS 'Fully qualified class name of the event';
COMMENT ON COLUMN event_publication.serialized_event IS 'JSON serialized event data';
COMMENT ON COLUMN event_publication.publication_date IS 'When the event was published';
COMMENT ON COLUMN event_publication.completion_date IS 'When the event was successfully processed (NULL if pending)';

-- ============================================================================
-- End of Schema
-- ============================================================================
