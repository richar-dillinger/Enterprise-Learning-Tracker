package com.learning.tracker.learningcontent.application.dto;

import com.learning.tracker.learningcontent.domain.model.Resource;
import com.learning.tracker.learningcontent.domain.model.ResourceType;

import java.util.UUID;

public record ResourceDTO(
        UUID id,
        String title,
        String description,
        ResourceType type,
        String url,
        int displayOrder
) {
    public static ResourceDTO from(Resource resource) {
        return new ResourceDTO(
                resource.getId().value(),
                resource.getTitle(),
                resource.getDescription(),
                resource.getType(),
                resource.getUrl(),
                resource.getDisplayOrder()
        );
    }
}
