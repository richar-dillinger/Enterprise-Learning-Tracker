package com.learning.tracker.learningcontent.domain.model;

import com.learning.tracker.shared.domain.vo.ResourceId;

import java.util.Objects;

/**
 * Represents a learning resource attached to an activity.
 * <p>
 * Resources provide the actual learning materials (videos, documents, links, etc.).
 */
public class Resource {

    private final ResourceId id;
    private String title;
    private String description;
    private ResourceType type;
    private String url;
    private Integer displayOrder;

    private Resource(ResourceId id, String title, String description,
                     ResourceType type, String url, Integer displayOrder) {
        this.id = Objects.requireNonNull(id, "Resource id cannot be null");
        this.title = validateTitle(title);
        this.description = description != null ? description.trim() : "";
        this.type = Objects.requireNonNull(type, "Resource type cannot be null");
        this.url = validateUrl(url);
        this.displayOrder = displayOrder != null ? displayOrder : 0;
    }

    public static Resource create(String title, String description,
                                  ResourceType type, String url, Integer displayOrder) {
        return new Resource(
                ResourceId.generate(),
                title,
                description,
                type,
                url,
                displayOrder
        );
    }

    public static Resource reconstitute(ResourceId id, String title, String description,
                                        ResourceType type, String url, Integer displayOrder) {
        return new Resource(id, title, description, type, url, displayOrder);
    }

    public void update(String title, String description, String url, Integer displayOrder) {
        this.title = validateTitle(title);
        this.description = description != null ? description.trim() : "";
        this.url = validateUrl(url);
        this.displayOrder = displayOrder != null ? displayOrder : 0;
    }

    // Getters
    public ResourceId getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public ResourceType getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    private String validateTitle(String title) {
        Objects.requireNonNull(title, "Resource title cannot be null");
        String trimmed = title.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException("Resource title cannot be blank");
        }
        if (trimmed.length() > 200) {
            throw new IllegalArgumentException("Resource title cannot exceed 200 characters");
        }
        return trimmed;
    }

    private String validateUrl(String url) {
        Objects.requireNonNull(url, "Resource URL cannot be null");
        String trimmed = url.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException("Resource URL cannot be blank");
        }
        if (trimmed.length() > 2000) {
            throw new IllegalArgumentException("Resource URL cannot exceed 2000 characters");
        }
        return trimmed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resource resource = (Resource) o;
        return Objects.equals(id, resource.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
