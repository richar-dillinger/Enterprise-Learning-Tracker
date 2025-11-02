package com.learning.tracker.learningcontent.infrastructure.mapper;

import com.learning.tracker.learningcontent.domain.model.Activity;
import com.learning.tracker.learningcontent.domain.model.LearningPath;
import com.learning.tracker.learningcontent.domain.model.Resource;
import com.learning.tracker.learningcontent.infrastructure.persistence.ActivityEntity;
import com.learning.tracker.learningcontent.infrastructure.persistence.LearningPathEntity;
import com.learning.tracker.learningcontent.infrastructure.persistence.ResourceEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class LearningPathMapper {

    public LearningPathEntity toEntity(LearningPath path) {
        if (path == null) {
            return null;
        }

        List<ActivityEntity> activityEntities = path.getActivities().stream()
                .map(this::toActivityEntity)
                .collect(Collectors.toList());

        return new LearningPathEntity(
                path.getId(),
                path.getSchoolId(),
                path.getTitle(),
                path.getDescription(),
                path.getStatus(),
                path.getCreatedBy(),
                activityEntities,
                path.getCreatedAt(),
                path.getUpdatedAt(),
                path.getPublishedAt()
        );
    }

    public LearningPath toDomain(LearningPathEntity entity) {
        if (entity == null) {
            return null;
        }

        List<Activity> activities = entity.getActivities().stream()
                .map(this::toActivity)
                .collect(Collectors.toList());

        return LearningPath.reconstitute(
                entity.getId(),
                entity.getSchoolId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getStatus(),
                entity.getCreatedBy(),
                activities,
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getPublishedAt()
        );
    }

    private ActivityEntity toActivityEntity(Activity activity) {
        List<ResourceEntity> resourceEntities = activity.getResources().stream()
                .map(this::toResourceEntity)
                .collect(Collectors.toList());

        return new ActivityEntity(
                activity.getId(),
                activity.getTitle(),
                activity.getDescription(),
                activity.getType(),
                activity.getDisplayOrder(),
                activity.getEstimatedMinutes(),
                resourceEntities
        );
    }

    private Activity toActivity(ActivityEntity entity) {
        List<Resource> resources = entity.getResources().stream()
                .map(this::toResource)
                .collect(Collectors.toList());

        return Activity.reconstitute(
                entity.getId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getType(),
                entity.getDisplayOrder(),
                entity.getEstimatedMinutes(),
                resources
        );
    }

    private ResourceEntity toResourceEntity(Resource resource) {
        return new ResourceEntity(
                resource.getId(),
                resource.getTitle(),
                resource.getDescription(),
                resource.getType(),
                resource.getUrl(),
                resource.getDisplayOrder()
        );
    }

    private Resource toResource(ResourceEntity entity) {
        return Resource.reconstitute(
                entity.getId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getType(),
                entity.getUrl(),
                entity.getDisplayOrder()
        );
    }
}
