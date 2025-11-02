package com.learning.tracker.learningcontent.domain.event;

import com.learning.tracker.shared.domain.event.DomainEvent;
import com.learning.tracker.shared.domain.vo.PathId;
import com.learning.tracker.shared.domain.vo.SchoolId;
import com.learning.tracker.shared.domain.vo.UserId;

import java.time.Instant;

/**
 * Domain event published when a learning path is created.
 */
public record PathCreatedEvent(
        PathId pathId,
        SchoolId schoolId,
        String title,
        UserId createdBy,
        Instant occurredOn
) implements DomainEvent {

    public PathCreatedEvent(PathId pathId, SchoolId schoolId, String title, UserId createdBy) {
        this(pathId, schoolId, title, createdBy, Instant.now());
    }
}
