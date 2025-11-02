package com.learning.tracker.shared.infrastructure.exception;

import java.util.UUID;

/**
 * Exception thrown when an entity cannot be found.
 * <p>
 * This exception indicates that a requested entity does not exist in the system.
 * It's typically thrown by repositories or domain services when querying for entities.
 */
public class EntityNotFoundException extends DomainException {

    private final String entityType;
    private final Object entityId;

    /**
     * Constructs a new entity not found exception.
     *
     * @param entityType the type of entity that was not found
     * @param entityId   the identifier of the entity
     */
    public EntityNotFoundException(String entityType, Object entityId) {
        super(String.format("%s with id '%s' not found", entityType, entityId));
        this.entityType = entityType;
        this.entityId = entityId;
    }

    /**
     * Constructs a new entity not found exception with a UUID identifier.
     *
     * @param entityType the type of entity that was not found
     * @param entityId   the UUID identifier of the entity
     */
    public EntityNotFoundException(String entityType, UUID entityId) {
        this(entityType, (Object) entityId);
    }

    /**
     * Constructs a new entity not found exception with a custom message.
     *
     * @param message the detail message
     */
    public EntityNotFoundException(String message) {
        super(message);
        this.entityType = null;
        this.entityId = null;
    }

    /**
     * Returns the type of entity that was not found.
     *
     * @return the entity type, or null if not specified
     */
    public String getEntityType() {
        return entityType;
    }

    /**
     * Returns the identifier of the entity that was not found.
     *
     * @return the entity id, or null if not specified
     */
    public Object getEntityId() {
        return entityId;
    }
}
