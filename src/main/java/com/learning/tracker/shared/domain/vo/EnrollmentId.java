package com.learning.tracker.shared.domain.vo;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * Value object representing a unique identifier for an Enrollment.
 * <p>
 * This is an immutable value object using UUID for unique identification.
 * Equality is based on the UUID value.
 */
public record EnrollmentId(UUID value) implements Serializable {

    /**
     * Compact constructor with validation.
     */
    public EnrollmentId {
        Objects.requireNonNull(value, "EnrollmentId cannot be null");
    }

    /**
     * Creates a new EnrollmentId with a random UUID.
     *
     * @return a new EnrollmentId
     */
    public static EnrollmentId generate() {
        return new EnrollmentId(UUID.randomUUID());
    }

    /**
     * Creates an EnrollmentId from a UUID.
     *
     * @param uuid the UUID
     * @return the EnrollmentId value object
     */
    public static EnrollmentId of(UUID uuid) {
        return new EnrollmentId(uuid);
    }

    /**
     * Creates an EnrollmentId from a string representation of a UUID.
     *
     * @param uuid the UUID string
     * @return the EnrollmentId value object
     * @throws IllegalArgumentException if the string is not a valid UUID
     */
    public static EnrollmentId of(String uuid) {
        Objects.requireNonNull(uuid, "UUID string cannot be null");
        try {
            return new EnrollmentId(UUID.fromString(uuid));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid UUID format for EnrollmentId: " + uuid, e);
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
