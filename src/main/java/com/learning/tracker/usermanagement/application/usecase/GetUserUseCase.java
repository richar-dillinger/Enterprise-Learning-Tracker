package com.learning.tracker.usermanagement.application.usecase;

import com.learning.tracker.usermanagement.application.dto.UserDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Use case for retrieving user information.
 * <p>
 * This is an inbound port (interface) for querying user data.
 */
public interface GetUserUseCase {

    /**
     * Finds a user by their identifier.
     *
     * @param userId the user identifier
     * @return the user, if found
     */
    Optional<UserDTO> findById(UUID userId);

    /**
     * Finds a user by their email.
     *
     * @param email the email address
     * @return the user, if found
     */
    Optional<UserDTO> findByEmail(String email);

    /**
     * Finds all users in a specific school.
     *
     * @param schoolId the school identifier
     * @return list of users in the school
     */
    List<UserDTO> findBySchool(UUID schoolId);

    /**
     * Finds all active users.
     *
     * @return list of all active users
     */
    List<UserDTO> findAllActive();
}
