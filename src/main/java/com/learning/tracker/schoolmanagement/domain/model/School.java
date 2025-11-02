package com.learning.tracker.schoolmanagement.domain.model;

import com.learning.tracker.shared.domain.vo.SchoolId;
import com.learning.tracker.shared.domain.vo.UserId;

import java.time.Instant;
import java.util.Objects;

/**
 * School aggregate root representing an organization or institution.
 * <p>
 * A school is a bounded context where learning paths are created and managed,
 * and where users are assigned roles (tutors, students, managers).
 * <p>
 * This is an aggregate root that maintains consistency of school data.
 */
public class School {

    private final SchoolId id;
    private String name;
    private String description;
    private SchoolStatus status;
    private final UserId createdBy;
    private final Instant createdAt;
    private Instant updatedAt;

    /**
     * Private constructor for reconstruction from persistence.
     */
    private School(SchoolId id, String name, String description, SchoolStatus status,
                   UserId createdBy, Instant createdAt, Instant updatedAt) {
        this.id = Objects.requireNonNull(id, "School id cannot be null");
        this.name = validateAndTrimName(name);
        this.description = description != null ? description.trim() : "";
        this.status = Objects.requireNonNull(status, "Status cannot be null");
        this.createdBy = Objects.requireNonNull(createdBy, "Created by cannot be null");
        this.createdAt = Objects.requireNonNull(createdAt, "Created at cannot be null");
        this.updatedAt = Objects.requireNonNull(updatedAt, "Updated at cannot be null");
    }

    /**
     * Creates a new School.
     * New schools are created in DRAFT status.
     *
     * @param name        the school name
     * @param description the description (optional)
     * @param createdBy   the user creating the school
     * @return a new School instance
     */
    public static School create(String name, String description, UserId createdBy) {
        Instant now = Instant.now();
        return new School(
                SchoolId.generate(),
                name,
                description,
                SchoolStatus.DRAFT,
                createdBy,
                now,
                now
        );
    }

    /**
     * Reconstructs a School from persistence.
     *
     * @param id          the school id
     * @param name        the name
     * @param description the description
     * @param status      the status
     * @param createdBy   the creator
     * @param createdAt   the creation timestamp
     * @param updatedAt   the last update timestamp
     * @return a School instance
     */
    public static School reconstitute(SchoolId id, String name, String description,
                                      SchoolStatus status, UserId createdBy,
                                      Instant createdAt, Instant updatedAt) {
        return new School(id, name, description, status, createdBy, createdAt, updatedAt);
    }

    /**
     * Updates the school's basic information.
     *
     * @param name        the new name
     * @param description the new description
     */
    public void updateInfo(String name, String description) {
        this.name = validateAndTrimName(name);
        this.description = description != null ? description.trim() : "";
        this.updatedAt = Instant.now();
    }

    /**
     * Activates the school, making it operational.
     *
     * @throws IllegalStateException if the school is archived
     */
    public void activate() {
        if (status == SchoolStatus.ARCHIVED) {
            throw new IllegalStateException("Cannot activate an archived school");
        }
        this.status = SchoolStatus.ACTIVE;
        this.updatedAt = Instant.now();
    }

    /**
     * Suspends the school temporarily.
     *
     * @throws IllegalStateException if the school is not active
     */
    public void suspend() {
        if (status != SchoolStatus.ACTIVE) {
            throw new IllegalStateException("Can only suspend active schools");
        }
        this.status = SchoolStatus.SUSPENDED;
        this.updatedAt = Instant.now();
    }

    /**
     * Resumes a suspended school.
     *
     * @throws IllegalStateException if the school is not suspended
     */
    public void resume() {
        if (status != SchoolStatus.SUSPENDED) {
            throw new IllegalStateException("Can only resume suspended schools");
        }
        this.status = SchoolStatus.ACTIVE;
        this.updatedAt = Instant.now();
    }

    /**
     * Archives the school permanently.
     * This is a final state - archived schools cannot be reactivated.
     */
    public void archive() {
        this.status = SchoolStatus.ARCHIVED;
        this.updatedAt = Instant.now();
    }

    /**
     * Checks if the school can accept new enrollments.
     *
     * @return true if enrollments are allowed
     */
    public boolean canAcceptEnrollments() {
        return status.allowsEnrollments();
    }

    /**
     * Checks if content can be created in this school.
     *
     * @return true if content creation is allowed
     */
    public boolean canCreateContent() {
        return status.allowsContentCreation();
    }

    // Getters
    public SchoolId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public SchoolStatus getStatus() {
        return status;
    }

    public UserId getCreatedBy() {
        return createdBy;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    // Helper methods
    private String validateAndTrimName(String name) {
        Objects.requireNonNull(name, "School name cannot be null");
        String trimmed = name.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException("School name cannot be blank");
        }
        if (trimmed.length() > 200) {
            throw new IllegalArgumentException("School name cannot exceed 200 characters");
        }
        return trimmed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        School school = (School) o;
        return Objects.equals(id, school.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "School{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", status=" + status +
                '}';
    }
}
