package com.learning.tracker.schoolmanagement.application.usecase;

import com.learning.tracker.shared.domain.vo.SchoolId;

import java.util.UUID;

/**
 * Use case for creating a new school.
 * <p>
 * This is an inbound port (interface) that defines the contract
 * for school creation.
 */
public interface CreateSchoolUseCase {

    /**
     * Creates a new school.
     *
     * @param command the creation command
     * @return the created school's identifier
     */
    SchoolId create(CreateSchoolCommand command);

    /**
     * Command for creating a new school.
     *
     * @param name        the school name
     * @param description the school description (optional)
     * @param createdBy   the user creating the school
     */
    record CreateSchoolCommand(
            String name,
            String description,
            UUID createdBy
    ) {
    }
}
