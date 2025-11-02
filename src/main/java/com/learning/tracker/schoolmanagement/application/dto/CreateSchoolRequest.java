package com.learning.tracker.schoolmanagement.application.dto;

import java.util.UUID;

/**
 * Request DTO for creating a new school.
 * <p>
 * Used by REST controllers to receive school creation data.
 */
public record CreateSchoolRequest(
        String name,
        String description,
        UUID createdBy
) {
}
