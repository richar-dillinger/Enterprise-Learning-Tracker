package com.learning.tracker.shared.domain.vo;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * Value object representing a unique identifier for a School.
 * <p>
 * This is an immutable value object using UUID for unique identification.
 * Equality is based on the UUID value.
 */
public record SchoolId(UUID value) implements Serializable {

    /**
     * Compact constructor with validation.
     */
    public SchoolId {
        Objects.requireNonNull(value, "SchoolId cannot be null");
    }

    /**
     * Creates a new SchoolId with a random UUID.
     *
     * @return a new SchoolId
     */
    public static SchoolId generate() {
        return new SchoolId(UUID.randomUUID());
    }

    /**
     * Creates a SchoolId from a UUID.
     *
     * @param uuid the UUID
     * @return the SchoolId value object
     */
    public static SchoolId of(UUID uuid) {
        return new SchoolId(uuid);
    }

    /**
     * Creates a SchoolId from a string representation of a UUID.
     *
     * @param uuid the UUID string
     * @return the SchoolId value object
     * @throws IllegalArgumentException if the string is not a valid UUID
     */
    public static SchoolId of(String uuid) {
        Objects.requireNonNull(uuid, "UUID string cannot be null");
        try {
            return new SchoolId(UUID.fromString(uuid));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid UUID format for SchoolId: " + uuid, e);
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
