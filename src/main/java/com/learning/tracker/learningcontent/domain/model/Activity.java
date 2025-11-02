package com.learning.tracker.learningcontent.domain.model;

import com.learning.tracker.shared.domain.vo.ActivityId;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents a learning activity within a learning path.
 * <p>
 * Activities are the individual learning units that students complete.
 * Each activity can have multiple resources attached to it.
 */
public class Activity {

    private final ActivityId id;
    private String title;
    private String description;
    private ActivityType type;
    private Integer displayOrder;
    private Integer estimatedMinutes;
    private final List<Resource> resources;

    private Activity(ActivityId id, String title, String description, ActivityType type,
                     Integer displayOrder, Integer estimatedMinutes, List<Resource> resources) {
        this.id = Objects.requireNonNull(id, "Activity id cannot be null");
        this.title = validateTitle(title);
        this.description = description != null ? description.trim() : "";
        this.type = Objects.requireNonNull(type, "Activity type cannot be null");
        this.displayOrder = displayOrder != null ? displayOrder : 0;
        this.estimatedMinutes = estimatedMinutes != null && estimatedMinutes > 0 ? estimatedMinutes : 30;
        this.resources = new ArrayList<>(resources != null ? resources : new ArrayList<>());
    }

    public static Activity create(String title, String description, ActivityType type,
                                  Integer displayOrder, Integer estimatedMinutes) {
        return new Activity(
                ActivityId.generate(),
                title,
                description,
                type,
                displayOrder,
                estimatedMinutes,
                new ArrayList<>()
        );
    }

    public static Activity reconstitute(ActivityId id, String title, String description,
                                        ActivityType type, Integer displayOrder,
                                        Integer estimatedMinutes, List<Resource> resources) {
        return new Activity(id, title, description, type, displayOrder, estimatedMinutes, resources);
    }

    public void update(String title, String description, Integer displayOrder, Integer estimatedMinutes) {
        this.title = validateTitle(title);
        this.description = description != null ? description.trim() : "";
        this.displayOrder = displayOrder != null ? displayOrder : 0;
        this.estimatedMinutes = estimatedMinutes != null && estimatedMinutes > 0 ? estimatedMinutes : 30;
    }

    public void addResource(Resource resource) {
        Objects.requireNonNull(resource, "Resource cannot be null");
        this.resources.add(resource);
    }

    public void removeResource(Resource resource) {
        this.resources.remove(resource);
    }

    // Getters
    public ActivityId getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public ActivityType getType() {
        return type;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public Integer getEstimatedMinutes() {
        return estimatedMinutes;
    }

    public List<Resource> getResources() {
        return Collections.unmodifiableList(resources);
    }

    private String validateTitle(String title) {
        Objects.requireNonNull(title, "Activity title cannot be null");
        String trimmed = title.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException("Activity title cannot be blank");
        }
        if (trimmed.length() > 200) {
            throw new IllegalArgumentException("Activity title cannot exceed 200 characters");
        }
        return trimmed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Activity activity = (Activity) o;
        return Objects.equals(id, activity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
