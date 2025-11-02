package com.learning.tracker.usermanagement.application.service;

import com.learning.tracker.shared.domain.vo.SchoolId;
import com.learning.tracker.shared.domain.vo.UserId;
import com.learning.tracker.shared.infrastructure.exception.EntityNotFoundException;
import com.learning.tracker.usermanagement.application.usecase.AssignRoleUseCase;
import com.learning.tracker.usermanagement.domain.event.UserRoleChangedEvent;
import com.learning.tracker.usermanagement.domain.model.User;
import com.learning.tracker.usermanagement.domain.repository.UserRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Application service for assigning roles to users.
 * <p>
 * This service implements the AssignRoleUseCase and handles
 * both system-level and school-level role assignments.
 */
@Service
@Transactional
public class AssignRoleService implements AssignRoleUseCase {

    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    public AssignRoleService(UserRepository userRepository,
                            ApplicationEventPublisher eventPublisher) {
        this.userRepository = userRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void assignSystemRole(AssignSystemRoleCommand command) {
        UserId userId = UserId.of(command.userId());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User", userId));

        user.assignSystemRole(command.systemRole());
        userRepository.save(user);

        // Publish event
        UserRoleChangedEvent event = UserRoleChangedEvent.systemRoleChanged(
                userId,
                command.systemRole()
        );
        eventPublisher.publishEvent(event);
    }

    @Override
    public void assignSchoolRole(AssignSchoolRoleCommand command) {
        UserId userId = UserId.of(command.userId());
        SchoolId schoolId = SchoolId.of(command.schoolId());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User", userId));

        user.assignSchoolRole(schoolId, command.schoolRole());
        userRepository.save(user);

        // Publish event
        UserRoleChangedEvent event = UserRoleChangedEvent.schoolRoleAssigned(
                userId,
                schoolId,
                command.schoolRole()
        );
        eventPublisher.publishEvent(event);
    }

    @Override
    public void removeSchoolRole(RemoveSchoolRoleCommand command) {
        UserId userId = UserId.of(command.userId());
        SchoolId schoolId = SchoolId.of(command.schoolId());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User", userId));

        boolean removed = user.removeSchoolRole(schoolId);
        if (removed) {
            userRepository.save(user);

            // Publish event
            UserRoleChangedEvent event = UserRoleChangedEvent.schoolRoleRemoved(
                    userId,
                    schoolId
            );
            eventPublisher.publishEvent(event);
        }
    }
}
