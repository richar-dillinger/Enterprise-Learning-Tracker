package com.learning.tracker.shared.domain.vo;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * Value object representing a unique identifier for a Learning Path.
 * <p>
 * This is an immutable value object using UUID for unique identification.
 * Equality is based on the UUID value.
 */
public record PathId(UUID value) implements Serializable {

    /**
     * Compact constructor with validation.
     */
    public PathId {
        Objects.requireNonNull(value, "PathId cannot be null");
    }

    /**
     * Creates a new PathId with a random UUID.
     *
     * @return a new PathId
     */
    public static PathId generate() {
        return new PathId(UUID.randomUUID());
    }

    /**
     * Creates a PathId from a UUID.
     *
     * @param uuid the UUID
     * @return the PathId value object
     */
    public static PathId of(UUID uuid) {
        return new PathId(uuid);
    }

    /**
     * Creates a PathId from a string representation of a UUID.
     *
     * @param uuid the UUID string
     * @return the PathId value object
     * @throws IllegalArgumentException if the string is not a valid UUID
     */
    public static PathId of(String uuid) {
        Objects.requireNonNull(uuid, "UUID string cannot be null");
        try {
            return new PathId(UUID.fromString(uuid));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid UUID format for PathId: " + uuid, e);
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
