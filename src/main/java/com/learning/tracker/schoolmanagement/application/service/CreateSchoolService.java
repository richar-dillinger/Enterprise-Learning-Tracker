package com.learning.tracker.schoolmanagement.application.service;

import com.learning.tracker.shared.domain.vo.SchoolId;
import com.learning.tracker.shared.domain.vo.UserId;
import com.learning.tracker.shared.infrastructure.exception.DuplicateEntityException;
import com.learning.tracker.shared.infrastructure.exception.ValidationException;
import com.learning.tracker.schoolmanagement.application.usecase.CreateSchoolUseCase;
import com.learning.tracker.schoolmanagement.domain.event.SchoolCreatedEvent;
import com.learning.tracker.schoolmanagement.domain.model.School;
import com.learning.tracker.schoolmanagement.domain.repository.SchoolRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Application service for school creation.
 * <p>
 * Implements the CreateSchoolUseCase and orchestrates school creation
 * including validation and event publishing.
 */
@Service
@Transactional
public class CreateSchoolService implements CreateSchoolUseCase {

    private final SchoolRepository schoolRepository;
    private final ApplicationEventPublisher eventPublisher;

    public CreateSchoolService(SchoolRepository schoolRepository,
                              ApplicationEventPublisher eventPublisher) {
        this.schoolRepository = schoolRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public SchoolId create(CreateSchoolCommand command) {
        // Validate command
        validateCommand(command);

        // Check if school name already exists
        if (schoolRepository.existsByName(command.name())) {
            throw new DuplicateEntityException("School", "name", command.name());
        }

        // Create and save school
        UserId createdBy = UserId.of(command.createdBy());
        School school = School.create(command.name(), command.description(), createdBy);
        School savedSchool = schoolRepository.save(school);

        // Publish domain event
        SchoolCreatedEvent event = new SchoolCreatedEvent(
                savedSchool.getId(),
                savedSchool.getName(),
                savedSchool.getCreatedBy()
        );
        eventPublisher.publishEvent(event);

        return savedSchool.getId();
    }

    private void validateCommand(CreateSchoolCommand command) {
        if (command == null) {
            throw new ValidationException("Create school command cannot be null");
        }
        if (command.name() == null || command.name().isBlank()) {
            throw new ValidationException("name", "School name is required");
        }
        if (command.createdBy() == null) {
            throw new ValidationException("createdBy", "Creator is required");
        }
    }
}
