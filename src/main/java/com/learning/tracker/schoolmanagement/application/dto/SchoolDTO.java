package com.learning.tracker.schoolmanagement.application.dto;

import com.learning.tracker.schoolmanagement.domain.model.School;
import com.learning.tracker.schoolmanagement.domain.model.SchoolStatus;

import java.time.Instant;
import java.util.UUID;

/**
 * Data Transfer Object for School.
 * <p>
 * Used for transferring school data across application boundaries.
 */
public record SchoolDTO(
        UUID id,
        String name,
        String description,
        SchoolStatus status,
        UUID createdBy,
        Instant createdAt,
        Instant updatedAt
) {

    /**
     * Creates a SchoolDTO from a School domain model.
     *
     * @param school the school domain model
     * @return the DTO
     */
    public static SchoolDTO from(School school) {
        return new SchoolDTO(
                school.getId().value(),
                school.getName(),
                school.getDescription(),
                school.getStatus(),
                school.getCreatedBy().value(),
                school.getCreatedAt(),
                school.getUpdatedAt()
        );
    }
}
