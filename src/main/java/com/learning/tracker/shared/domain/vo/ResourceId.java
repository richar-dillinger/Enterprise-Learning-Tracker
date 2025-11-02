package com.learning.tracker.shared.domain.vo;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * Value object representing a unique identifier for a Learning Resource.
 * <p>
 * This is an immutable value object using UUID for unique identification.
 */
public record ResourceId(UUID value) implements Serializable {

    public ResourceId {
        Objects.requireNonNull(value, "ResourceId cannot be null");
    }

    public static ResourceId generate() {
        return new ResourceId(UUID.randomUUID());
    }

    public static ResourceId of(UUID uuid) {
        return new ResourceId(uuid);
    }

    public static ResourceId of(String uuid) {
        Objects.requireNonNull(uuid, "UUID string cannot be null");
        try {
            return new ResourceId(UUID.fromString(uuid));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid UUID format for ResourceId: " + uuid, e);
        }
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
