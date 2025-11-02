package com.learning.tracker.learningcontent.application.dto;

import com.learning.tracker.learningcontent.domain.model.Activity;
import com.learning.tracker.learningcontent.domain.model.ActivityType;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public record ActivityDTO(
        UUID id,
        String title,
        String description,
        ActivityType type,
        int displayOrder,
        int estimatedMinutes,
        List<ResourceDTO> resources
) {
    public static ActivityDTO from(Activity activity) {
        return new ActivityDTO(
                activity.getId().value(),
                activity.getTitle(),
                activity.getDescription(),
                activity.getType(),
                activity.getDisplayOrder(),
                activity.getEstimatedMinutes(),
                activity.getResources().stream()
                        .map(ResourceDTO::from)
                        .collect(Collectors.toList())
        );
    }
}
