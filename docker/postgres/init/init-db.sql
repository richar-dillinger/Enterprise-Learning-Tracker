-- This script initializes the PostgreSQL database for Keycloak
-- The keycloak database is created automatically by the POSTGRES_DB environment variable
-- This script can be used for additional database setup if needed

-- Create additional schemas or configurations here if needed
-- For now, Keycloak will auto-create its own tables

-- Log that initialization is complete
DO $$
BEGIN
    RAISE NOTICE 'Database initialization complete for Keycloak';
END $$;
