package com.learning.tracker.learningcontent.infrastructure.persistence;

import com.learning.tracker.shared.domain.vo.ResourceId;
import com.learning.tracker.learningcontent.domain.model.ResourceType;
import jakarta.persistence.*;

import java.util.Objects;

/**
 * JPA entity for Resource persistence.
 */
@Entity
@Table(name = "learning_resources")
public class ResourceEntity {

    @Id
    private ResourceId id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ResourceType type;

    @Column(nullable = false, length = 2000)
    private String url;

    @Column(nullable = false)
    private Integer displayOrder;

    protected ResourceEntity() {
    }

    public ResourceEntity(ResourceId id, String title, String description,
                          ResourceType type, String url, Integer displayOrder) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.type = type;
        this.url = url;
        this.displayOrder = displayOrder;
    }

    // Getters and setters
    public ResourceId getId() {
        return id;
    }

    public void setId(ResourceId id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ResourceType getType() {
        return type;
    }

    public void setType(ResourceType type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResourceEntity that = (ResourceEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
