package com.learning.tracker.schoolmanagement.domain.model;

/**
 * Represents the lifecycle status of a School.
 * <p>
 * Schools progress through different states from creation to potential archival.
 */
public enum SchoolStatus {

    /**
     * School is being set up and configured.
     * Not yet available for enrollment.
     */
    DRAFT("Draft", "School is being configured"),

    /**
     * School is active and accepting enrollments.
     */
    ACTIVE("Active", "School is operational"),

    /**
     * School is temporarily suspended.
     * Existing enrollments remain but new enrollments are not allowed.
     */
    SUSPENDED("Suspended", "School is temporarily suspended"),

    /**
     * School is archived and no longer operational.
     * Historical data is preserved but no operations are allowed.
     */
    ARCHIVED("Archived", "School is archived");

    private final String displayName;
    private final String description;

    SchoolStatus(String displayName, String description) {
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
     * Returns the description of this status.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Checks if this status allows new enrollments.
     *
     * @return true if enrollments are allowed
     */
    public boolean allowsEnrollments() {
        return this == ACTIVE;
    }

    /**
     * Checks if this status allows content creation.
     *
     * @return true if content creation is allowed
     */
    public boolean allowsContentCreation() {
        return this == DRAFT || this == ACTIVE;
    }

    /**
     * Checks if the school is operational.
     *
     * @return true if operational
     */
    public boolean isOperational() {
        return this == ACTIVE;
    }
}
