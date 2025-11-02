package com.learning.tracker.usermanagement.domain.model;

/**
 * System-wide roles that define global permissions.
 * <p>
 * These roles apply across all schools and define the highest level
 * of authorization in the system.
 */
public enum SystemRole {

    /**
     * System administrator with full access to all functionality.
     */
    ADMIN("System Administrator", "Full system access and configuration"),

    /**
     * Platform manager who can manage schools and users.
     */
    PLATFORM_MANAGER("Platform Manager", "Manage schools and platform-level settings"),

    /**
     * Regular user with basic access.
     */
    USER("User", "Basic user access");

    private final String displayName;
    private final String description;

    SystemRole(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    /**
     * Returns the human-readable display name.
     *
     * @return the display name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Returns the description of this role.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Checks if this role has administrative privileges.
     *
     * @return true if this is an admin role
     */
    public boolean isAdmin() {
        return this == ADMIN;
    }

    /**
     * Checks if this role can manage platform-level resources.
     *
     * @return true if this role can manage platform resources
     */
    public boolean canManagePlatform() {
        return this == ADMIN || this == PLATFORM_MANAGER;
    }
}
