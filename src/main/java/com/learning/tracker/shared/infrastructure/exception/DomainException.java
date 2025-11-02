package com.learning.tracker.shared.infrastructure.exception;

/**
 * Base exception class for all domain-related exceptions.
 * <p>
 * Domain exceptions represent violations of business rules or invariants
 * within the domain layer. These exceptions should be caught and handled
 * appropriately by the application layer.
 */
public abstract class DomainException extends RuntimeException {

    /**
     * Constructs a new domain exception with the specified detail message.
     *
     * @param message the detail message
     */
    protected DomainException(String message) {
        super(message);
    }

    /**
     * Constructs a new domain exception with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause   the cause
     */
    protected DomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
