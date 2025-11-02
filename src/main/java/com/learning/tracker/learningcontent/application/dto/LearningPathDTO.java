package com.learning.tracker.learningcontent.application.dto;

import com.learning.tracker.learningcontent.domain.model.LearningPath;
import com.learning.tracker.learningcontent.domain.model.PathStatus;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public record LearningPathDTO(
        UUID id,
        UUID schoolId,
        String title,
        String description,
        PathStatus status,
        UUID createdBy,
        List<ActivityDTO> activities,
        int totalEstimatedMinutes,
        Instant createdAt,
        Instant updatedAt,
        Instant publishedAt
) {
    public static LearningPathDTO from(LearningPath path) {
        return new LearningPathDTO(
                path.getId().value(),
                path.getSchoolId().value(),
                path.getTitle(),
                path.getDescription(),
                path.getStatus(),
                path.getCreatedBy().value(),
                path.getActivities().stream()
                        .map(ActivityDTO::from)
                        .collect(Collectors.toList()),
                path.getTotalEstimatedMinutes(),
                path.getCreatedAt(),
                path.getUpdatedAt(),
                path.getPublishedAt()
        );
    }
}
