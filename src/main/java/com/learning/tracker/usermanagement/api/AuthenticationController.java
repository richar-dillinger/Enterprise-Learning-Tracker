package com.learning.tracker.usermanagement.api;

import com.learning.tracker.usermanagement.application.usecase.AuthenticateUserUseCase;
import com.learning.tracker.usermanagement.application.usecase.RegisterUserUseCase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for authentication-related operations.
 * <p>
 * This controller provides endpoints for user registration and authentication.
 */
@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthenticationController {

    private final RegisterUserUseCase registerUserUseCase;
    private final AuthenticateUserUseCase authenticateUserUseCase;

    public AuthenticationController(RegisterUserUseCase registerUserUseCase,
                                     AuthenticateUserUseCase authenticateUserUseCase) {
        this.registerUserUseCase = registerUserUseCase;
        this.authenticateUserUseCase = authenticateUserUseCase;
    }

    /**
     * Registers a new user.
     *
     * @param request the registration request
     * @return the created user's ID
     */
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest request) {
        log.info("Registration request received for email: {}", request.email());

        var command = new RegisterUserUseCase.RegisterUserCommand(
                request.email(),
                request.firstName(),
                request.lastName(),
                request.password()
        );

        var userId = registerUserUseCase.register(command);

        log.info("User registered successfully with ID: {}", userId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new RegisterResponse(userId.toString(), "User registered successfully"));
    }

    /**
     * Authenticates a user and returns access tokens.
     *
     * @param request the authentication request
     * @return the authentication response with tokens
     */
    @PostMapping("/login")
    public ResponseEntity<AuthenticateUserUseCase.AuthenticationResponse> login(@RequestBody LoginRequest request) {
        log.info("Login request received for email: {}", request.email());

        var command = new AuthenticateUserUseCase.AuthenticateCommand(
                request.email(),
                request.password()
        );

        var response = authenticateUserUseCase.authenticate(command);

        log.info("User authenticated successfully: {}", request.email());

        return ResponseEntity.ok(response);
    }

    // DTOs

    /**
     * Registration request.
     *
     * @param email     the user's email
     * @param firstName the first name
     * @param lastName  the last name
     * @param password  the password
     */
    public record RegisterRequest(
            String email,
            String firstName,
            String lastName,
            String password
    ) {
    }

    /**
     * Registration response.
     *
     * @param userId  the created user's ID
     * @param message success message
     */
    public record RegisterResponse(
            String userId,
            String message
    ) {
    }

    /**
     * Login request.
     *
     * @param email    the user's email
     * @param password the password
     */
    public record LoginRequest(
            String email,
            String password
    ) {
    }
}
