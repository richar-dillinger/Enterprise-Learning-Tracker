package com.learning.tracker.schoolmanagement.domain.event;

import com.learning.tracker.shared.domain.event.DomainEvent;
import com.learning.tracker.shared.domain.vo.SchoolId;
import com.learning.tracker.shared.domain.vo.UserId;

import java.time.Instant;

/**
 * Domain event published when a new school is created.
 * <p>
 * Other modules can listen to this event to perform actions such as:
 * <ul>
 *   <li>Initializing school-specific configurations</li>
 *   <li>Setting up default learning paths</li>
 *   <li>Notifying administrators</li>
 * </ul>
 */
public record SchoolCreatedEvent(
        SchoolId schoolId,
        String name,
        UserId createdBy,
        Instant occurredOn
) implements DomainEvent {

    /**
     * Creates a new SchoolCreatedEvent.
     *
     * @param schoolId  the new school's identifier
     * @param name      the school name
     * @param createdBy the user who created the school
     */
    public SchoolCreatedEvent(SchoolId schoolId, String name, UserId createdBy) {
        this(schoolId, name, createdBy, Instant.now());
    }
}
