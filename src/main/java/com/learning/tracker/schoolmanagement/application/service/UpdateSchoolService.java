package com.learning.tracker.schoolmanagement.application.service;

import com.learning.tracker.shared.domain.vo.SchoolId;
import com.learning.tracker.shared.infrastructure.exception.EntityNotFoundException;
import com.learning.tracker.shared.infrastructure.exception.ValidationException;
import com.learning.tracker.schoolmanagement.application.usecase.UpdateSchoolUseCase;
import com.learning.tracker.schoolmanagement.domain.event.SchoolStatusChangedEvent;
import com.learning.tracker.schoolmanagement.domain.model.School;
import com.learning.tracker.schoolmanagement.domain.model.SchoolStatus;
import com.learning.tracker.schoolmanagement.domain.repository.SchoolRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Application service for updating schools.
 * <p>
 * Implements the UpdateSchoolUseCase and handles school updates.
 */
@Service
@Transactional
public class UpdateSchoolService implements UpdateSchoolUseCase {

    private final SchoolRepository schoolRepository;
    private final ApplicationEventPublisher eventPublisher;

    public UpdateSchoolService(SchoolRepository schoolRepository,
                              ApplicationEventPublisher eventPublisher) {
        this.schoolRepository = schoolRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void updateInfo(UpdateSchoolInfoCommand command) {
        validateUpdateCommand(command);

        SchoolId schoolId = SchoolId.of(command.schoolId());
        School school = schoolRepository.findById(schoolId)
                .orElseThrow(() -> new EntityNotFoundException("School", schoolId));

        school.updateInfo(command.name(), command.description());
        schoolRepository.save(school);
    }

    @Override
    public void changeStatus(ChangeSchoolStatusCommand command) {
        validateStatusCommand(command);

        SchoolId schoolId = SchoolId.of(command.schoolId());
        School school = schoolRepository.findById(schoolId)
                .orElseThrow(() -> new EntityNotFoundException("School", schoolId));

        SchoolStatus previousStatus = school.getStatus();
        SchoolStatus newStatus = command.newStatus();

        // Apply status change based on target status
        switch (newStatus) {
            case ACTIVE -> school.activate();
            case SUSPENDED -> school.suspend();
            case ARCHIVED -> school.archive();
            case DRAFT -> throw new ValidationException("Cannot revert school to DRAFT status");
        }

        schoolRepository.save(school);

        // Publish event if status actually changed
        if (previousStatus != newStatus) {
            SchoolStatusChangedEvent event = new SchoolStatusChangedEvent(
                    schoolId,
                    previousStatus,
                    newStatus
            );
            eventPublisher.publishEvent(event);
        }
    }

    private void validateUpdateCommand(UpdateSchoolInfoCommand command) {
        if (command == null) {
            throw new ValidationException("Update command cannot be null");
        }
        if (command.schoolId() == null) {
            throw new ValidationException("schoolId", "School ID is required");
        }
        if (command.name() == null || command.name().isBlank()) {
            throw new ValidationException("name", "School name is required");
        }
    }

    private void validateStatusCommand(ChangeSchoolStatusCommand command) {
        if (command == null) {
            throw new ValidationException("Status change command cannot be null");
        }
        if (command.schoolId() == null) {
            throw new ValidationException("schoolId", "School ID is required");
        }
        if (command.newStatus() == null) {
            throw new ValidationException("newStatus", "New status is required");
        }
    }
}
