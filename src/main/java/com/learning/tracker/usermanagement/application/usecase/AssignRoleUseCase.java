package com.learning.tracker.usermanagement.application.usecase;

import com.learning.tracker.usermanagement.domain.model.SchoolRole;
import com.learning.tracker.usermanagement.domain.model.SystemRole;

import java.util.UUID;

/**
 * Use case for assigning roles to users.
 * <p>
 * This is an inbound port (interface) that defines the contract
 * for role assignment operations.
 */
public interface AssignRoleUseCase {

    /**
     * Assigns a system role to a user.
     *
     * @param command the command
     */
    void assignSystemRole(AssignSystemRoleCommand command);

    /**
     * Assigns a school role to a user.
     *
     * @param command the command
     */
    void assignSchoolRole(AssignSchoolRoleCommand command);

    /**
     * Removes a school role from a user.
     *
     * @param command the command
     */
    void removeSchoolRole(RemoveSchoolRoleCommand command);

    /**
     * Command for assigning a system role.
     *
     * @param userId     the user identifier
     * @param systemRole the system role to assign
     */
    record AssignSystemRoleCommand(
            UUID userId,
            SystemRole systemRole
    ) {
    }

    /**
     * Command for assigning a school role.
     *
     * @param userId     the user identifier
     * @param schoolId   the school identifier
     * @param schoolRole the school role to assign
     */
    record AssignSchoolRoleCommand(
            UUID userId,
            UUID schoolId,
            SchoolRole schoolRole
    ) {
    }

    /**
     * Command for removing a school role.
     *
     * @param userId   the user identifier
     * @param schoolId the school identifier
     */
    record RemoveSchoolRoleCommand(
            UUID userId,
            UUID schoolId
    ) {
    }
}
