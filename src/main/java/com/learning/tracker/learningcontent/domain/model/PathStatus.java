package com.learning.tracker.learningcontent.domain.model;

/**
 * Represents the publication status of a Learning Path.
 * <p>
 * Learning paths progress through different states from creation to publication.
 */
public enum PathStatus {

    /**
     * Path is being created and configured.
     * Not visible to students.
     */
    DRAFT("Draft", "Path is being created"),

    /**
     * Path is under review before publication.
     */
    REVIEW("Review", "Path is being reviewed"),

    /**
     * Path is published and available for enrollment.
     */
    PUBLISHED("Published", "Path is available for enrollment"),

    /**
     * Path is archived and no longer available for new enrollments.
     */
    ARCHIVED("Archived", "Path is archived");

    private final String displayName;
    private final String description;

    PathStatus(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Checks if this status allows enrollments.
     *
     * @return true if enrollments are allowed
     */
    public boolean allowsEnrollments() {
        return this == PUBLISHED;
    }

    /**
     * Checks if this status allows editing.
     *
     * @return true if editing is allowed
     */
    public boolean allowsEditing() {
        return this == DRAFT || this == REVIEW;
    }
}
