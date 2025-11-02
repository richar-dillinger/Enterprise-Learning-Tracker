package com.learning.tracker.usermanagement.application.dto;

import com.learning.tracker.usermanagement.domain.model.SchoolRole;
import com.learning.tracker.usermanagement.domain.model.SystemRole;
import com.learning.tracker.usermanagement.domain.model.User;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Data Transfer Object for User.
 * <p>
 * Used for transferring user data across application boundaries
 * (e.g., to REST controllers, external services).
 */
public record UserDTO(
        UUID id,
        String email,
        String firstName,
        String lastName,
        String fullName,
        SystemRole systemRole,
        Map<UUID, SchoolRole> schoolRoles,
        boolean active,
        Instant createdAt,
        Instant updatedAt
) {

    /**
     * Creates a UserDTO from a User domain model.
     *
     * @param user the user domain model
     * @return the DTO
     */
    public static UserDTO from(User user) {
        return new UserDTO(
                user.getId().value(),
                user.getEmail().value(),
                user.getFirstName(),
                user.getLastName(),
                user.getFullName(),
                user.getSystemRole(),
                user.getSchoolRoles().entrySet().stream()
                        .collect(Collectors.toMap(
                                entry -> entry.getKey().value(),
                                Map.Entry::getValue
                        )),
                user.isActive(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
