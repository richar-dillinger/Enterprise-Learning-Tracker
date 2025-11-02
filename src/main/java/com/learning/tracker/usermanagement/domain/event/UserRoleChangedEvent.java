package com.learning.tracker.usermanagement.domain.event;

import com.learning.tracker.shared.domain.event.DomainEvent;
import com.learning.tracker.shared.domain.vo.SchoolId;
import com.learning.tracker.shared.domain.vo.UserId;
import com.learning.tracker.usermanagement.domain.model.SchoolRole;
import com.learning.tracker.usermanagement.domain.model.SystemRole;

import java.time.Instant;
import java.util.Optional;

/**
 * Domain event published when a user's role is changed.
 * <p>
 * This event covers both system role changes and school role changes.
 * Other modules can listen to this event to update permissions or access controls.
 */
public record UserRoleChangedEvent(
        UserId userId,
        SystemRole systemRole,
        SchoolId schoolId,
        SchoolRole schoolRole,
        String changeType,
        Instant occurredOn
) implements DomainEvent {

    /**
     * Creates an event for a system role change.
     *
     * @param userId     the user identifier
     * @param systemRole the new system role
     * @return the event
     */
    public static UserRoleChangedEvent systemRoleChanged(UserId userId, SystemRole systemRole) {
        return new UserRoleChangedEvent(
                userId,
                systemRole,
                null,
                null,
                "SYSTEM_ROLE_CHANGED",
                Instant.now()
        );
    }

    /**
     * Creates an event for a school role assignment.
     *
     * @param userId     the user identifier
     * @param schoolId   the school identifier
     * @param schoolRole the assigned school role
     * @return the event
     */
    public static UserRoleChangedEvent schoolRoleAssigned(UserId userId, SchoolId schoolId,
                                                          SchoolRole schoolRole) {
        return new UserRoleChangedEvent(
                userId,
                null,
                schoolId,
                schoolRole,
                "SCHOOL_ROLE_ASSIGNED",
                Instant.now()
        );
    }

    /**
     * Creates an event for a school role removal.
     *
     * @param userId   the user identifier
     * @param schoolId the school identifier
     * @return the event
     */
    public static UserRoleChangedEvent schoolRoleRemoved(UserId userId, SchoolId schoolId) {
        return new UserRoleChangedEvent(
                userId,
                null,
                schoolId,
                null,
                "SCHOOL_ROLE_REMOVED",
                Instant.now()
        );
    }

    /**
     * Returns the school id if this is a school role change.
     *
     * @return the school id
     */
    public Optional<SchoolId> getSchoolId() {
        return Optional.ofNullable(schoolId);
    }

    /**
     * Returns the school role if this is a school role assignment.
     *
     * @return the school role
     */
    public Optional<SchoolRole> getSchoolRole() {
        return Optional.ofNullable(schoolRole);
    }

    /**
     * Checks if this is a system role change.
     *
     * @return true if this is a system role change
     */
    public boolean isSystemRoleChange() {
        return "SYSTEM_ROLE_CHANGED".equals(changeType);
    }

    /**
     * Checks if this is a school role assignment.
     *
     * @return true if this is a school role assignment
     */
    public boolean isSchoolRoleAssignment() {
        return "SCHOOL_ROLE_ASSIGNED".equals(changeType);
    }

    /**
     * Checks if this is a school role removal.
     *
     * @return true if this is a school role removal
     */
    public boolean isSchoolRoleRemoval() {
        return "SCHOOL_ROLE_REMOVED".equals(changeType);
    }
}
