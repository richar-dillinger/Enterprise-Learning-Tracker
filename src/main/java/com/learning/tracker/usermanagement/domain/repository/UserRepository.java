package com.learning.tracker.usermanagement.domain.repository;

import com.learning.tracker.shared.domain.vo.Email;
import com.learning.tracker.shared.domain.vo.SchoolId;
import com.learning.tracker.shared.domain.vo.UserId;
import com.learning.tracker.usermanagement.domain.model.SchoolRole;
import com.learning.tracker.usermanagement.domain.model.User;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for User aggregate.
 * <p>
 * This is a domain-level interface (outbound port) that defines
 * data access operations for users. The implementation is provided
 * by the infrastructure layer.
 */
public interface UserRepository {

    /**
     * Saves a user.
     *
     * @param user the user to save
     * @return the saved user
     */
    User save(User user);

    /**
     * Finds a user by their unique identifier.
     *
     * @param id the user id
     * @return the user, if found
     */
    Optional<User> findById(UserId id);

    /**
     * Finds a user by their email address.
     *
     * @param email the email
     * @return the user, if found
     */
    Optional<User> findByEmail(Email email);

    /**
     * Checks if a user with the given email exists.
     *
     * @param email the email to check
     * @return true if a user with this email exists
     */
    boolean existsByEmail(Email email);

    /**
     * Finds all users with a specific role in a school.
     *
     * @param schoolId the school identifier
     * @param role     the school role
     * @return list of users with the specified role in the school
     */
    List<User> findBySchoolRole(SchoolId schoolId, SchoolRole role);

    /**
     * Finds all active users in a school (any role).
     *
     * @param schoolId the school identifier
     * @return list of users with any role in the school
     */
    List<User> findBySchool(SchoolId schoolId);

    /**
     * Finds all active users.
     *
     * @return list of active users
     */
    List<User> findAllActive();

    /**
     * Deletes a user by their identifier.
     *
     * @param id the user id
     */
    void deleteById(UserId id);
}
