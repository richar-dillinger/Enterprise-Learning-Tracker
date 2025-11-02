package com.learning.tracker.schoolmanagement.application.dto;

/**
 * Request DTO for updating school information.
 * <p>
 * Used by REST controllers to receive school update data.
 */
public record UpdateSchoolRequest(
        String name,
        String description
) {
}
