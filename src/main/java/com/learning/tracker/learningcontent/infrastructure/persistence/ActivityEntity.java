package com.learning.tracker.learningcontent.infrastructure.persistence;

import com.learning.tracker.shared.domain.vo.ActivityId;
import com.learning.tracker.learningcontent.domain.model.ActivityType;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "learning_activities")
public class ActivityEntity {

    @Id
    private ActivityId id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(length = 2000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ActivityType type;

    @Column(nullable = false)
    private Integer displayOrder;

    @Column(nullable = false)
    private Integer estimatedMinutes;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "activity_id", nullable = false)
    @OrderBy("displayOrder ASC")
    private List<ResourceEntity> resources = new ArrayList<>();

    protected ActivityEntity() {
    }

    public ActivityEntity(ActivityId id, String title, String description,
                          ActivityType type, Integer displayOrder,
                          Integer estimatedMinutes, List<ResourceEntity> resources) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.type = type;
        this.displayOrder = displayOrder;
        this.estimatedMinutes = estimatedMinutes;
        this.resources = resources != null ? new ArrayList<>(resources) : new ArrayList<>();
    }

    // Getters and setters
    public ActivityId getId() {
        return id;
    }

    public void setId(ActivityId id) {
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

    public ActivityType getType() {
        return type;
    }

    public void setType(ActivityType type) {
        this.type = type;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public Integer getEstimatedMinutes() {
        return estimatedMinutes;
    }

    public void setEstimatedMinutes(Integer estimatedMinutes) {
        this.estimatedMinutes = estimatedMinutes;
    }

    public List<ResourceEntity> getResources() {
        return resources;
    }

    public void setResources(List<ResourceEntity> resources) {
        this.resources = resources;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActivityEntity that = (ActivityEntity) o;
        return java.util.Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(id);
    }
}
