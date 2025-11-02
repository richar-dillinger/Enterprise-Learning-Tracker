package com.learning.tracker.schoolmanagement.application.usecase;

import com.learning.tracker.schoolmanagement.application.dto.SchoolDTO;
import com.learning.tracker.schoolmanagement.domain.model.SchoolStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Use case for retrieving school information.
 * <p>
 * This is an inbound port (interface) for querying school data.
 */
public interface GetSchoolUseCase {

    /**
     * Finds a school by its identifier.
     *
     * @param schoolId the school identifier
     * @return the school, if found
     */
    Optional<SchoolDTO> findById(UUID schoolId);

    /**
     * Finds a school by its name.
     *
     * @param name the school name
     * @return the school, if found
     */
    Optional<SchoolDTO> findByName(String name);

    /**
     * Finds all schools with a specific status.
     *
     * @param status the school status
     * @return list of schools
     */
    List<SchoolDTO> findByStatus(SchoolStatus status);

    /**
     * Finds all active schools.
     *
     * @return list of active schools
     */
    List<SchoolDTO> findAllActive();

    /**
     * Finds all schools created by a specific user.
     *
     * @param userId the user identifier
     * @return list of schools
     */
    List<SchoolDTO> findByCreatedBy(UUID userId);

    /**
     * Finds all schools.
     *
     * @return list of all schools
     */
    List<SchoolDTO> findAll();
}
