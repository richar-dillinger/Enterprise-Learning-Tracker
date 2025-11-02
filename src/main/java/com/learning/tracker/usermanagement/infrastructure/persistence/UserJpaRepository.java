package com.learning.tracker.usermanagement.infrastructure.persistence;

import com.learning.tracker.shared.domain.vo.Email;
import com.learning.tracker.shared.domain.vo.UserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA repository for UserEntity.
 * <p>
 * This interface provides database access methods for user entities.
 */
@Repository
public interface UserJpaRepository extends JpaRepository<UserEntity, UserId> {

    /**
     * Finds a user by email.
     *
     * @param email the email
     * @return the user entity, if found
     */
    Optional<UserEntity> findByEmail(Email email);

    /**
     * Checks if a user with the given email exists.
     *
     * @param email the email
     * @return true if exists
     */
    boolean existsByEmail(Email email);

    /**
     * Finds all active users.
     *
     * @return list of active user entities
     */
    List<UserEntity> findByActiveTrue();

    /**
     * Finds users by school id (users who have any role in the school).
     *
     * @param schoolId the school identifier
     * @return list of user entities
     */
    @Query("SELECT u FROM UserEntity u JOIN u.schoolRoles sr WHERE KEY(sr) = :schoolId")
    List<UserEntity> findBySchool(@Param("schoolId") UUID schoolId);
}
