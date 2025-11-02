package com.learning.tracker.learningcontent.domain.event;

import com.learning.tracker.shared.domain.event.DomainEvent;
import com.learning.tracker.shared.domain.vo.PathId;
import com.learning.tracker.shared.domain.vo.SchoolId;

import java.time.Instant;

/**
 * Domain event published when a learning path is published.
 * <p>
 * This signals that the path is now available for enrollment.
 */
public record PathPublishedEvent(
        PathId pathId,
        SchoolId schoolId,
        String title,
        Instant occurredOn
) implements DomainEvent {

    public PathPublishedEvent(PathId pathId, SchoolId schoolId, String title) {
        this(pathId, schoolId, title, Instant.now());
    }
}
