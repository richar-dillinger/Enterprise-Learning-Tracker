package com.learning.tracker.schoolmanagement.domain.repository;

import com.learning.tracker.shared.domain.vo.SchoolId;
import com.learning.tracker.shared.domain.vo.UserId;
import com.learning.tracker.schoolmanagement.domain.model.School;
import com.learning.tracker.schoolmanagement.domain.model.SchoolStatus;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for School aggregate.
 * <p>
 * This is a domain-level interface (outbound port) that defines
 * data access operations for schools.
 */
public interface SchoolRepository {

    /**
     * Saves a school.
     *
     * @param school the school to save
     * @return the saved school
     */
    School save(School school);

    /**
     * Finds a school by its unique identifier.
     *
     * @param id the school id
     * @return the school, if found
     */
    Optional<School> findById(SchoolId id);

    /**
     * Finds a school by its name.
     *
     * @param name the school name
     * @return the school, if found
     */
    Optional<School> findByName(String name);

    /**
     * Checks if a school with the given name exists.
     *
     * @param name the name to check
     * @return true if a school with this name exists
     */
    boolean existsByName(String name);

    /**
     * Finds all schools with a specific status.
     *
     * @param status the school status
     * @return list of schools with the specified status
     */
    List<School> findByStatus(SchoolStatus status);

    /**
     * Finds all schools created by a specific user.
     *
     * @param userId the user identifier
     * @return list of schools created by the user
     */
    List<School> findByCreatedBy(UserId userId);

    /**
     * Finds all active schools.
     *
     * @return list of active schools
     */
    List<School> findAllActive();

    /**
     * Finds all schools.
     *
     * @return list of all schools
     */
    List<School> findAll();

    /**
     * Deletes a school by its identifier.
     *
     * @param id the school id
     */
    void deleteById(SchoolId id);
}
