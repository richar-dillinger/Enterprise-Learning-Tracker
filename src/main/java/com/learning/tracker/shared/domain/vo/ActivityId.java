package com.learning.tracker.shared.domain.vo;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * Value object representing a unique identifier for a Learning Activity.
 * <p>
 * This is an immutable value object using UUID for unique identification.
 * Equality is based on the UUID value.
 */
public record ActivityId(UUID value) implements Serializable {

    /**
     * Compact constructor with validation.
     */
    public ActivityId {
        Objects.requireNonNull(value, "ActivityId cannot be null");
    }

    /**
     * Creates a new ActivityId with a random UUID.
     *
     * @return a new ActivityId
     */
    public static ActivityId generate() {
        return new ActivityId(UUID.randomUUID());
    }

    /**
     * Creates an ActivityId from a UUID.
     *
     * @param uuid the UUID
     * @return the ActivityId value object
     */
    public static ActivityId of(UUID uuid) {
        return new ActivityId(uuid);
    }

    /**
     * Creates an ActivityId from a string representation of a UUID.
     *
     * @param uuid the UUID string
     * @return the ActivityId value object
     * @throws IllegalArgumentException if the string is not a valid UUID
     */
    public static ActivityId of(String uuid) {
        Objects.requireNonNull(uuid, "UUID string cannot be null");
        try {
            return new ActivityId(UUID.fromString(uuid));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid UUID format for ActivityId: " + uuid, e);
        }
    }

    /**
     * Returns the string representation of the UUID.
     *
     * @return the UUID as a string
     */
    @Override
    public String toString() {
        return value.toString();
    }
}
