package com.learning.tracker.usermanagement.application.usecase;

/**
 * Use case for authenticating a user.
 * <p>
 * This is an inbound port (interface) that defines the contract
 * for user authentication. Implementations are provided by the application layer.
 */
public interface AuthenticateUserUseCase {

    /**
     * Authenticates a user with email and password.
     *
     * @param command the authentication command
     * @return the authentication result
     */
    AuthenticationResponse authenticate(AuthenticateCommand command);

    /**
     * Command for authenticating a user.
     *
     * @param email    the user's email
     * @param password the user's password
     */
    record AuthenticateCommand(
            String email,
            String password
    ) {
    }

    /**
     * Response containing authentication tokens and user information.
     *
     * @param accessToken  the JWT access token
     * @param refreshToken the refresh token
     * @param expiresIn    token expiration time in seconds
     * @param userId       the authenticated user's ID
     */
    record AuthenticationResponse(
            String accessToken,
            String refreshToken,
            long expiresIn,
            String userId
    ) {
    }
}
