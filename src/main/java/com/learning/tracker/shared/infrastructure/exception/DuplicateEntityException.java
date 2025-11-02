package com.learning.tracker.shared.infrastructure.exception;

/**
 * Exception thrown when attempting to create an entity that already exists.
 * <p>
 * This exception indicates a uniqueness constraint violation, such as
 * attempting to register a user with an email that's already in use.
 */
public class DuplicateEntityException extends DomainException {

    private final String entityType;
    private final String fieldName;
    private final Object fieldValue;

    /**
     * Constructs a new duplicate entity exception.
     *
     * @param entityType the type of entity
     * @param fieldName  the name of the field that must be unique
     * @param fieldValue the value that already exists
     */
    public DuplicateEntityException(String entityType, String fieldName, Object fieldValue) {
        super(String.format("%s with %s '%s' already exists", entityType, fieldName, fieldValue));
        this.entityType = entityType;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    /**
     * Constructs a new duplicate entity exception with a custom message.
     *
     * @param message the detail message
     */
    public DuplicateEntityException(String message) {
        super(message);
        this.entityType = null;
        this.fieldName = null;
        this.fieldValue = null;
    }

    /**
     * Returns the type of entity.
     *
     * @return the entity type, or null if not specified
     */
    public String getEntityType() {
        return entityType;
    }

    /**
     * Returns the name of the field that must be unique.
     *
     * @return the field name, or null if not specified
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * Returns the value that already exists.
     *
     * @return the field value, or null if not specified
     */
    public Object getFieldValue() {
        return fieldValue;
    }
}
