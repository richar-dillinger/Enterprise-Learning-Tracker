package com.learning.tracker.usermanagement.application.service;

import com.learning.tracker.shared.domain.vo.Email;
import com.learning.tracker.shared.infrastructure.exception.ValidationException;
import com.learning.tracker.usermanagement.application.usecase.AuthenticateUserUseCase;
import com.learning.tracker.usermanagement.domain.port.out.AuthenticationPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Application service for user authentication.
 * <p>
 * This service implements the AuthenticateUserUseCase and orchestrates
 * the authentication process with Keycloak.
 */
@Service
@Slf4j
public class AuthenticateUserService implements AuthenticateUserUseCase {

    private final AuthenticationPort authenticationPort;

    public AuthenticateUserService(AuthenticationPort authenticationPort) {
        this.authenticationPort = authenticationPort;
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticateCommand command) {
        // Validate command
        validateCommand(command);

        // Create email value object (will validate format)
        Email email = Email.of(command.email());

        // Authenticate with Keycloak
        AuthenticationPort.AuthenticationResult result = authenticationPort.authenticate(email, command.password());

        log.info("User authenticated successfully: {}", email.value());

        // Map to response
        return new AuthenticationResponse(
                result.accessToken(),
                result.refreshToken(),
                result.expiresIn(),
                result.userId().toString()
        );
    }

    private void validateCommand(AuthenticateCommand command) {
        if (command == null) {
            throw new ValidationException("Authentication command cannot be null");
        }
        if (command.email() == null || command.email().isBlank()) {
            throw new ValidationException("email", "Email is required");
        }
        if (command.password() == null || command.password().isBlank()) {
            throw new ValidationException("password", "Password is required");
        }
    }
}
