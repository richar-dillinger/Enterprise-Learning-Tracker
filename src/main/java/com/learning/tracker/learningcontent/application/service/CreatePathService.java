package com.learning.tracker.learningcontent.application.service;

import com.learning.tracker.shared.domain.vo.PathId;
import com.learning.tracker.shared.domain.vo.SchoolId;
import com.learning.tracker.shared.domain.vo.UserId;
import com.learning.tracker.shared.infrastructure.exception.ValidationException;
import com.learning.tracker.learningcontent.application.usecase.CreatePathUseCase;
import com.learning.tracker.learningcontent.domain.event.PathCreatedEvent;
import com.learning.tracker.learningcontent.domain.model.LearningPath;
import com.learning.tracker.learningcontent.domain.repository.LearningPathRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CreatePathService implements CreatePathUseCase {

    private final LearningPathRepository repository;
    private final ApplicationEventPublisher eventPublisher;

    public CreatePathService(LearningPathRepository repository,
                            ApplicationEventPublisher eventPublisher) {
        this.repository = repository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public PathId create(CreatePathCommand command) {
        validateCommand(command);

        SchoolId schoolId = SchoolId.of(command.schoolId());
        UserId createdBy = UserId.of(command.createdBy());

        LearningPath path = LearningPath.create(
                schoolId,
                command.title(),
                command.description(),
                createdBy
        );

        LearningPath saved = repository.save(path);

        eventPublisher.publishEvent(new PathCreatedEvent(
                saved.getId(),
                saved.getSchoolId(),
                saved.getTitle(),
                saved.getCreatedBy()
        ));

        return saved.getId();
    }

    private void validateCommand(CreatePathCommand command) {
        if (command == null) {
            throw new ValidationException("Create path command cannot be null");
        }
        if (command.schoolId() == null) {
            throw new ValidationException("schoolId", "School ID is required");
        }
        if (command.title() == null || command.title().isBlank()) {
            throw new ValidationException("title", "Title is required");
        }
        if (command.createdBy() == null) {
            throw new ValidationException("createdBy", "Creator is required");
        }
    }
}
