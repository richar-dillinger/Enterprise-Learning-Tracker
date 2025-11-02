package com.learning.tracker.usermanagement.infrastructure.mapper;

import com.learning.tracker.usermanagement.domain.model.User;
import com.learning.tracker.usermanagement.infrastructure.persistence.UserEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper for converting between User domain model and UserEntity.
 * <p>
 * This class handles the bidirectional mapping between the domain layer
 * and the persistence layer.
 */
@Component
public class UserMapper {

    /**
     * Converts a User domain model to a UserEntity.
     *
     * @param user the domain model
     * @return the entity
     */
    public UserEntity toEntity(User user) {
        if (user == null) {
            return null;
        }

        return new UserEntity(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getSystemRole(),
                user.getSchoolRoles(),
                user.isActive(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    /**
     * Converts a UserEntity to a User domain model.
     *
     * @param entity the entity
     * @return the domain model
     */
    public User toDomain(UserEntity entity) {
        if (entity == null) {
            return null;
        }

        return User.reconstitute(
                entity.getId(),
                entity.getEmail(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getSystemRole(),
                entity.getSchoolRoles(),
                entity.isActive(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
