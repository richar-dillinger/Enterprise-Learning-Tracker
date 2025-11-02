package com.learning.tracker.shared.infrastructure.exception;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Exception thrown when validation of data fails.
 * <p>
 * This exception can contain multiple validation errors, making it suitable
 * for scenarios where multiple fields fail validation simultaneously.
 */
public class ValidationException extends DomainException {

    private final List<ValidationError> errors;

    /**
     * Constructs a new validation exception with a single error message.
     *
     * @param message the validation error message
     */
    public ValidationException(String message) {
        super(message);
        this.errors = new ArrayList<>();
        this.errors.add(new ValidationError(null, message));
    }

    /**
     * Constructs a new validation exception with a field-specific error.
     *
     * @param fieldName the name of the field that failed validation
     * @param message   the validation error message
     */
    public ValidationException(String fieldName, String message) {
        super(String.format("Validation failed for field '%s': %s", fieldName, message));
        this.errors = new ArrayList<>();
        this.errors.add(new ValidationError(fieldName, message));
    }

    /**
     * Constructs a new validation exception with multiple validation errors.
     *
     * @param errors the list of validation errors
     */
    public ValidationException(List<ValidationError> errors) {
        super(buildMessage(errors));
        this.errors = new ArrayList<>(errors);
    }

    /**
     * Constructs a new validation exception from a map of field errors.
     *
     * @param fieldErrors map of field names to error messages
     */
    public ValidationException(Map<String, String> fieldErrors) {
        this(fieldErrors.entrySet().stream()
                .map(entry -> new ValidationError(entry.getKey(), entry.getValue()))
                .toList());
    }

    /**
     * Returns the list of validation errors.
     *
     * @return unmodifiable list of validation errors
     */
    public List<ValidationError> getErrors() {
        return Collections.unmodifiableList(errors);
    }

    /**
     * Returns true if there are multiple validation errors.
     *
     * @return true if multiple errors exist
     */
    public boolean hasMultipleErrors() {
        return errors.size() > 1;
    }

    private static String buildMessage(List<ValidationError> errors) {
        if (errors.isEmpty()) {
            return "Validation failed";
        }
        if (errors.size() == 1) {
            ValidationError error = errors.get(0);
            return error.fieldName() != null
                    ? String.format("Validation failed for field '%s': %s", error.fieldName(), error.message())
                    : error.message();
        }
        return String.format("Validation failed with %d errors", errors.size());
    }

    /**
     * Represents a single validation error.
     *
     * @param fieldName the name of the field (null for general errors)
     * @param message   the error message
     */
    public record ValidationError(String fieldName, String message) {
        public ValidationError {
            if (message == null || message.isBlank()) {
                throw new IllegalArgumentException("Validation error message cannot be blank");
            }
        }
    }
}
