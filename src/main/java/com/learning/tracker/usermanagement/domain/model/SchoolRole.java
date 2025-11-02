package com.learning.tracker.usermanagement.domain.model;

/**
 * School-specific roles that define permissions within a school context.
 * <p>
 * These roles are assigned to users for specific schools and determine
 * what actions they can perform within that school.
 */
public enum SchoolRole {

    /**
     * School administrator with full control over the school.
     */
    SCHOOL_ADMIN("School Administrator", "Full access to school management and content"),

    /**
     * Manager who can oversee learning paths and enrollments.
     */
    MANAGER("Manager", "Manage learning paths, enrollments, and monitor progress"),

    /**
     * Tutor who creates content and mentors students.
     */
    TUTOR("Tutor", "Create learning content and mentor students"),

    /**
     * Student enrolled in learning paths.
     */
    STUDENT("Student", "Access and complete assigned learning paths");

    private final String displayName;
    private final String description;

    SchoolRole(String displayName, String description) {
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
     * Checks if this role has administrative privileges within a school.
     *
     * @return true if this is a school admin role
     */
    public boolean isSchoolAdmin() {
        return this == SCHOOL_ADMIN;
    }

    /**
     * Checks if this role can manage content within a school.
     *
     * @return true if this role can manage content
     */
    public boolean canManageContent() {
        return this == SCHOOL_ADMIN || this == MANAGER || this == TUTOR;
    }

    /**
     * Checks if this role can create learning paths.
     *
     * @return true if this role can create paths
     */
    public boolean canCreatePaths() {
        return this == SCHOOL_ADMIN || this == TUTOR;
    }

    /**
     * Checks if this role can manage enrollments.
     *
     * @return true if this role can manage enrollments
     */
    public boolean canManageEnrollments() {
        return this == SCHOOL_ADMIN || this == MANAGER;
    }

    /**
     * Checks if this is a student role.
     *
     * @return true if this is a student role
     */
    public boolean isStudent() {
        return this == STUDENT;
    }
}
