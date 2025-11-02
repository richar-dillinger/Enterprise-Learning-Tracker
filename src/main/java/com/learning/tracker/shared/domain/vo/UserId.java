package com.learning.tracker.shared.domain.vo;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * Value object representing a unique identifier for a User.
 * <p>
 * This is an immutable value object using UUID for unique identification.
 * Equality is based on the UUID value.
 */
public record UserId(UUID value) implements Serializable {

    /**
     * Compact constructor with validation.
     */
    public UserId {
        Objects.requireNonNull(value, "UserId cannot be null");
    }

    /**
     * Creates a new UserId with a random UUID.
     *
     * @return a new UserId
     */
    public static UserId generate() {
        return new UserId(UUID.randomUUID());
    }

    /**
     * Creates a UserId from a UUID.
     *
     * @param uuid the UUID
     * @return the UserId value object
     */
    public static UserId of(UUID uuid) {
        return new UserId(uuid);
    }

    /**
     * Creates a UserId from a string representation of a UUID.
     *
     * @param uuid the UUID string
     * @return the UserId value object
     * @throws IllegalArgumentException if the string is not a valid UUID
     */
    public static UserId of(String uuid) {
        Objects.requireNonNull(uuid, "UUID string cannot be null");
        try {
            return new UserId(UUID.fromString(uuid));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid UUID format for UserId: " + uuid, e);
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
