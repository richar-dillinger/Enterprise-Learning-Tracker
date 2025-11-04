package com.learning.tracker.usermanagement.application.usecase;

import com.learning.tracker.shared.domain.vo.UserId;

/**
 * Use case for registering a new user in the system.
 * <p>
 * This is an inbound port (interface) that defines the contract
 * for user registration. Implementations are provided by the application layer.
 */
public interface RegisterUserUseCase {

    /**
     * Registers a new user.
     *
     * @param command the registration command
     * @return the created user's identifier
     */
    UserId register(RegisterUserCommand command);

    /**
     * Command for registering a new user.
     *
     * @param email     the user's email
     * @param firstName the first name
     * @param lastName  the last name
     * @param password  the user's password
     */
    record RegisterUserCommand(
            String email,
            String firstName,
            String lastName,
            String password
    ) {
    }
}
