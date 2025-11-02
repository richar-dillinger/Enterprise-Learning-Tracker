package com.learning.tracker.schoolmanagement.infrastructure.mapper;

import com.learning.tracker.schoolmanagement.domain.model.School;
import com.learning.tracker.schoolmanagement.infrastructure.persistence.SchoolEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper for converting between School domain model and SchoolEntity.
 * <p>
 * Handles bidirectional mapping between domain and persistence layers.
 */
@Component
public class SchoolMapper {

    /**
     * Converts a School domain model to a SchoolEntity.
     *
     * @param school the domain model
     * @return the entity
     */
    public SchoolEntity toEntity(School school) {
        if (school == null) {
            return null;
        }

        return new SchoolEntity(
                school.getId(),
                school.getName(),
                school.getDescription(),
                school.getStatus(),
                school.getCreatedBy(),
                school.getCreatedAt(),
                school.getUpdatedAt()
        );
    }

    /**
     * Converts a SchoolEntity to a School domain model.
     *
     * @param entity the entity
     * @return the domain model
     */
    public School toDomain(SchoolEntity entity) {
        if (entity == null) {
            return null;
        }

        return School.reconstitute(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getStatus(),
                entity.getCreatedBy(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
