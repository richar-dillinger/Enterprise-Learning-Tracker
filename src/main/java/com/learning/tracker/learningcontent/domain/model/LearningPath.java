package com.learning.tracker.learningcontent.domain.model;

import com.learning.tracker.shared.domain.vo.PathId;
import com.learning.tracker.shared.domain.vo.SchoolId;
import com.learning.tracker.shared.domain.vo.UserId;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Learning Path aggregate root.
 * <p>
 * A learning path is a structured sequence of activities that students follow
 * to learn a specific topic or skill. Paths belong to schools and are created by tutors.
 */
public class LearningPath {

    private final PathId id;
    private final SchoolId schoolId;
    private String title;
    private String description;
    private PathStatus status;
    private final UserId createdBy;
    private final List<Activity> activities;
    private final Instant createdAt;
    private Instant updatedAt;
    private Instant publishedAt;

    private LearningPath(PathId id, SchoolId schoolId, String title, String description,
                         PathStatus status, UserId createdBy, List<Activity> activities,
                         Instant createdAt, Instant updatedAt, Instant publishedAt) {
        this.id = Objects.requireNonNull(id, "Path id cannot be null");
        this.schoolId = Objects.requireNonNull(schoolId, "School id cannot be null");
        this.title = validateTitle(title);
        this.description = description != null ? description.trim() : "";
        this.status = Objects.requireNonNull(status, "Status cannot be null");
        this.createdBy = Objects.requireNonNull(createdBy, "Created by cannot be null");
        this.activities = new ArrayList<>(activities != null ? activities : new ArrayList<>());
        this.createdAt = Objects.requireNonNull(createdAt, "Created at cannot be null");
        this.updatedAt = Objects.requireNonNull(updatedAt, "Updated at cannot be null");
        this.publishedAt = publishedAt;
    }

    public static LearningPath create(SchoolId schoolId, String title,
                                      String description, UserId createdBy) {
        Instant now = Instant.now();
        return new LearningPath(
                PathId.generate(),
                schoolId,
                title,
                description,
                PathStatus.DRAFT,
                createdBy,
                new ArrayList<>(),
                now,
                now,
                null
        );
    }

    public static LearningPath reconstitute(PathId id, SchoolId schoolId, String title,
                                            String description, PathStatus status,
                                            UserId createdBy, List<Activity> activities,
                                            Instant createdAt, Instant updatedAt, Instant publishedAt) {
        return new LearningPath(id, schoolId, title, description, status, createdBy,
                activities, createdAt, updatedAt, publishedAt);
    }

    public void updateInfo(String title, String description) {
        if (!status.allowsEditing()) {
            throw new IllegalStateException("Cannot edit path in " + status + " status");
        }
        this.title = validateTitle(title);
        this.description = description != null ? description.trim() : "";
        this.updatedAt = Instant.now();
    }

    public void addActivity(Activity activity) {
        Objects.requireNonNull(activity, "Activity cannot be null");
        if (!status.allowsEditing()) {
            throw new IllegalStateException("Cannot add activities to path in " + status + " status");
        }
        this.activities.add(activity);
        this.updatedAt = Instant.now();
    }

    public void removeActivity(Activity activity) {
        if (!status.allowsEditing()) {
            throw new IllegalStateException("Cannot remove activities from path in " + status + " status");
        }
        this.activities.remove(activity);
        this.updatedAt = Instant.now();
    }

    public void submitForReview() {
        if (status != PathStatus.DRAFT) {
            throw new IllegalStateException("Can only submit draft paths for review");
        }
        if (activities.isEmpty()) {
            throw new IllegalStateException("Cannot submit path with no activities");
        }
        this.status = PathStatus.REVIEW;
        this.updatedAt = Instant.now();
    }

    public void publish() {
        if (status != PathStatus.REVIEW && status != PathStatus.DRAFT) {
            throw new IllegalStateException("Can only publish paths in draft or review status");
        }
        if (activities.isEmpty()) {
            throw new IllegalStateException("Cannot publish path with no activities");
        }
        this.status = PathStatus.PUBLISHED;
        this.publishedAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public void archive() {
        if (status == PathStatus.ARCHIVED) {
            throw new IllegalStateException("Path is already archived");
        }
        this.status = PathStatus.ARCHIVED;
        this.updatedAt = Instant.now();
    }

    public void returnToDraft() {
        if (status != PathStatus.REVIEW) {
            throw new IllegalStateException("Can only return review paths to draft");
        }
        this.status = PathStatus.DRAFT;
        this.updatedAt = Instant.now();
    }

    public int getTotalEstimatedMinutes() {
        return activities.stream()
                .mapToInt(Activity::getEstimatedMinutes)
                .sum();
    }

    // Getters
    public PathId getId() {
        return id;
    }

    public SchoolId getSchoolId() {
        return schoolId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public PathStatus getStatus() {
        return status;
    }

    public UserId getCreatedBy() {
        return createdBy;
    }

    public List<Activity> getActivities() {
        return Collections.unmodifiableList(activities);
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getPublishedAt() {
        return publishedAt;
    }

    private String validateTitle(String title) {
        Objects.requireNonNull(title, "Path title cannot be null");
        String trimmed = title.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException("Path title cannot be blank");
        }
        if (trimmed.length() > 200) {
            throw new IllegalArgumentException("Path title cannot exceed 200 characters");
        }
        return trimmed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LearningPath that = (LearningPath) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "LearningPath{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", status=" + status +
                ", activities=" + activities.size() +
                '}';
    }
}
