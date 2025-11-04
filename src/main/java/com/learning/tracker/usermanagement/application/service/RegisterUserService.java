package com.learning.tracker.usermanagement.application.service;

import com.learning.tracker.shared.domain.vo.Email;
import com.learning.tracker.shared.domain.vo.UserId;
import com.learning.tracker.shared.infrastructure.exception.DuplicateEntityException;
import com.learning.tracker.shared.infrastructure.exception.ValidationException;
import com.learning.tracker.usermanagement.application.usecase.RegisterUserUseCase;
import com.learning.tracker.usermanagement.domain.event.UserRegisteredEvent;
import com.learning.tracker.usermanagement.domain.model.User;
import com.learning.tracker.usermanagement.domain.port.out.AuthenticationPort;
import com.learning.tracker.usermanagement.domain.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Application service for user registration.
 * <p>
 * This service implements the RegisterUserUseCase and orchestrates
 * the user registration process including validation, Keycloak integration,
 * and event publishing.
 */
@Service
@Transactional
@Slf4j
public class RegisterUserService implements RegisterUserUseCase {

    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final AuthenticationPort authenticationPort;

    public RegisterUserService(UserRepository userRepository,
                               ApplicationEventPublisher eventPublisher,
                               AuthenticationPort authenticationPort) {
        this.userRepository = userRepository;
        this.eventPublisher = eventPublisher;
        this.authenticationPort = authenticationPort;
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

        // Create and save user in our domain
        User user = User.create(email, command.firstName(), command.lastName());
        User savedUser = userRepository.save(user);

        // Create user in Keycloak authentication system
        try {
            String keycloakUserId = authenticationPort.createUser(
                    savedUser.getId(),
                    savedUser.getEmail(),
                    savedUser.getFirstName(),
                    savedUser.getLastName(),
                    command.password()
            );
            log.info("User created in Keycloak with ID: {}", keycloakUserId);
        } catch (Exception e) {
            log.error("Failed to create user in Keycloak, rolling back user creation", e);
            // Transaction will rollback automatically due to @Transactional
            throw e;
        }

        // Publish domain event
        UserRegisteredEvent event = new UserRegisteredEvent(
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getFirstName(),
                savedUser.getLastName(),
                savedUser.getSystemRole()
        );
        eventPublisher.publishEvent(event);

        log.info("User registered successfully: {}", savedUser.getId().value());
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
        if (command.password() == null || command.password().isBlank()) {
            throw new ValidationException("password", "Password is required");
        }
        if (command.password().length() < 8) {
            throw new ValidationException("password", "Password must be at least 8 characters");
        }
    }
}
