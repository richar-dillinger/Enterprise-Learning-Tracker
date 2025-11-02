package com.learning.tracker.schoolmanagement.domain.event;

import com.learning.tracker.shared.domain.event.DomainEvent;
import com.learning.tracker.shared.domain.vo.SchoolId;
import com.learning.tracker.schoolmanagement.domain.model.SchoolStatus;

import java.time.Instant;

/**
 * Domain event published when a school's status changes.
 * <p>
 * This event is important for other modules to react to school lifecycle changes.
 * For example, when a school is archived, enrollments should be closed.
 */
public record SchoolStatusChangedEvent(
        SchoolId schoolId,
        SchoolStatus previousStatus,
        SchoolStatus newStatus,
        Instant occurredOn
) implements DomainEvent {

    /**
     * Creates a new SchoolStatusChangedEvent.
     *
     * @param schoolId       the school identifier
     * @param previousStatus the previous status
     * @param newStatus      the new status
     */
    public SchoolStatusChangedEvent(SchoolId schoolId, SchoolStatus previousStatus,
                                    SchoolStatus newStatus) {
        this(schoolId, previousStatus, newStatus, Instant.now());
    }
}
