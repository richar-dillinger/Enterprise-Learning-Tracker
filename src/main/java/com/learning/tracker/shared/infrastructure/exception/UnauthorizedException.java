package com.learning.tracker.shared.infrastructure.exception;

/**
 * Exception thrown when authentication is required but not provided.
 * <p>
 * This exception indicates that the user needs to authenticate (log in)
 * to access the requested resource or perform the requested operation.
 * <p>
 * HTTP Status Code: 401 Unauthorized
 */
public class UnauthorizedException extends DomainException {

    /**
     * Constructs a new unauthorized exception with a default message.
     */
    public UnauthorizedException() {
        super("Authentication is required to access this resource");
    }

    /**
     * Constructs a new unauthorized exception with a custom message.
     *
     * @param message the detail message
     */
    public UnauthorizedException(String message) {
        super(message);
    }

    /**
     * Constructs a new unauthorized exception with a message and cause.
     *
     * @param message the detail message
     * @param cause   the underlying cause
     */
    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }
}
