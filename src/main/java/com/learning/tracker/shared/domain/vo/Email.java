package com.learning.tracker.shared.domain.vo;

import java.io.Serializable;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Value object representing an email address.
 * <p>
 * This is an immutable value object that ensures email addresses are valid.
 * Equality is based on the normalized email address (lowercase).
 */
public record Email(String value) implements Serializable {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,}$"
    );

    /**
     * Compact constructor with validation.
     */
    public Email {
        Objects.requireNonNull(value, "Email cannot be null");
        value = value.trim().toLowerCase();

        if (value.isBlank()) {
            throw new IllegalArgumentException("Email cannot be blank");
        }

        if (!EMAIL_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("Invalid email format: " + value);
        }

        if (value.length() > 254) { // RFC 5321
            throw new IllegalArgumentException("Email is too long (max 254 characters)");
        }
    }

    /**
     * Creates an Email from a string value.
     *
     * @param email the email string
     * @return the Email value object
     * @throws IllegalArgumentException if the email is invalid
     */
    public static Email of(String email) {
        return new Email(email);
    }

    /**
     * Returns the email address as a string.
     *
     * @return the email address
     */
    @Override
    public String toString() {
        return value;
    }
}
