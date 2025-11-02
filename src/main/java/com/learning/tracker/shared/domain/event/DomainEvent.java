package com.learning.tracker.shared.domain.event;

import java.time.Instant;

/**
 * Base interface for all domain events in the system.
 * <p>
 * Domain events represent something that has happened in the domain
 * and are used for inter-module communication via Spring Modulith's event system.
 * <p>
 * All domain events should be immutable and should use records when possible.
 */
public interface DomainEvent {

    /**
     * Returns the timestamp when this event occurred.
     *
     * @return the event occurrence timestamp
     */
    Instant occurredOn();

    /**
     * Returns a descriptive name for this event type.
     * Default implementation returns the simple class name.
     *
     * @return the event type name
     */
    default String eventType() {
        return this.getClass().getSimpleName();
    }
}
