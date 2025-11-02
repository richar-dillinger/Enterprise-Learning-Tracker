package com.learning.tracker.usermanagement.application.service;

import com.learning.tracker.shared.domain.vo.Email;
import com.learning.tracker.shared.domain.vo.UserId;
import com.learning.tracker.shared.infrastructure.exception.DuplicateEntityException;
import com.learning.tracker.shared.infrastructure.exception.ValidationException;
import com.learning.tracker.usermanagement.application.usecase.RegisterUserUseCase;
import com.learning.tracker.usermanagement.domain.event.UserRegisteredEvent;
import com.learning.tracker.usermanagement.domain.model.User;
import com.learning.tracker.usermanagement.domain.repository.UserRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Application service for user registration.
 * <p>
 * This service implements the RegisterUserUseCase and orchestrates
 * the user registration process including validation and event publishing.
 */
@Service
@Transactional
public class RegisterUserService implements RegisterUserUseCase {

    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    public RegisterUserService(UserRepository userRepository,
                               ApplicationEventPublisher eventPublisher) {
        this.userRepository = userRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public UserId register(RegisterUserCommand command) {
        // Validate command
        validateCommand(command);

        // Create email value object (will validate format)
        Email email = Email.of(command.email());

        // Check if user already exists
        if (userRepository.existsByEmail(email)) {
            throw new DuplicateEntityException("User", "email", email.value());
        }

        // Create and save user
        User user = User.create(email, command.firstName(), command.lastName());
        User savedUser = userRepository.save(user);

        // Publish domain event
        UserRegisteredEvent event = new UserRegisteredEvent(
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getFirstName(),
                savedUser.getLastName(),
                savedUser.getSystemRole()
        );
        eventPublisher.publishEvent(event);

        return savedUser.getId();
    }

    private void validateCommand(RegisterUserCommand command) {
        if (command == null) {
            throw new ValidationException("Registration command cannot be null");
        }
        if (command.email() == null || command.email().isBlank()) {
            throw new ValidationException("email", "Email is required");
        }
        if (command.firstName() == null || command.firstName().isBlank()) {
            throw new ValidationException("firstName", "First name is required");
        }
        if (command.lastName() == null || command.lastName().isBlank()) {
            throw new ValidationException("lastName", "Last name is required");
        }
    }
}
