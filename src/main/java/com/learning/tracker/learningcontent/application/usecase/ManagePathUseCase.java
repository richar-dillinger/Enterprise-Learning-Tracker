package com.learning.tracker.learningcontent.application.usecase;

import com.learning.tracker.learningcontent.domain.model.ActivityType;
import com.learning.tracker.learningcontent.domain.model.ResourceType;

import java.util.UUID;

public interface ManagePathUseCase {

    void updateInfo(UpdatePathInfoCommand command);

    void publish(PublishPathCommand command);

    void archive(ArchivePathCommand command);

    void addActivity(AddActivityCommand command);

    record UpdatePathInfoCommand(
            UUID pathId,
            String title,
            String description
    ) {
    }

    record PublishPathCommand(UUID pathId) {
    }

    record ArchivePathCommand(UUID pathId) {
    }

    record AddActivityCommand(
            UUID pathId,
            String title,
            String description,
            ActivityType type,
            Integer displayOrder,
            Integer estimatedMinutes
    ) {
    }
}
