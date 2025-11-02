package com.learning.tracker.shared.infrastructure.exception;

/**
 * Exception thrown when a user is authenticated but lacks permission for an operation.
 * <p>
 * This exception indicates that the user is authenticated (logged in) but does not
 * have the necessary permissions or roles to perform the requested operation or
 * access the requested resource.
 * <p>
 * Examples:
 * <ul>
 *   <li>Student attempting to delete a learning path</li>
 *   <li>User trying to access another school's data</li>
 *   <li>Tutor attempting to modify system-level settings</li>
 * </ul>
 * <p>
 * HTTP Status Code: 403 Forbidden
 */
public class ForbiddenException extends DomainException {

    private final String resource;
    private final String action;

    /**
     * Constructs a new forbidden exception with a default message.
     */
    public ForbiddenException() {
        super("You do not have permission to perform this operation");
        this.resource = null;
        this.action = null;
    }

    /**
     * Constructs a new forbidden exception with a custom message.
     *
     * @param message the detail message
     */
    public ForbiddenException(String message) {
        super(message);
        this.resource = null;
        this.action = null;
    }

    /**
     * Constructs a new forbidden exception with resource and action details.
     *
     * @param resource the resource being accessed
     * @param action   the action being attempted
     */
    public ForbiddenException(String resource, String action) {
        super(String.format("You do not have permission to %s %s", action, resource));
        this.resource = resource;
        this.action = action;
    }

    /**
     * Constructs a new forbidden exception with a message and cause.
     *
     * @param message the detail message
     * @param cause   the underlying cause
     */
    public ForbiddenException(String message, Throwable cause) {
        super(message, cause);
        this.resource = null;
        this.action = null;
    }

    /**
     * Returns the resource that was being accessed.
     *
     * @return the resource, or null if not specified
     */
    public String getResource() {
        return resource;
    }

    /**
     * Returns the action that was being attempted.
     *
     * @return the action, or null if not specified
     */
    public String getAction() {
        return action;
    }
}
