package com.learning.tracker.usermanagement.application.dto;

/**
 * Request DTO for creating a new user.
 * <p>
 * Used by REST controllers to receive user creation data.
 */
public record CreateUserRequest(
        String email,
        String firstName,
        String lastName,
        String password
) {
}
