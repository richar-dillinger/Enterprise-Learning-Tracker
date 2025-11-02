package com.learning.tracker.shared.infrastructure.persistence;

import com.learning.tracker.shared.domain.vo.PathId;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.UUID;

/**
 * JPA converter for {@link PathId} value object.
 * <p>
 * Converts between PathId and UUID for database persistence.
 * Auto-applied to all PathId fields in entities.
 */
@Converter(autoApply = true)
public class PathIdConverter implements AttributeConverter<PathId, UUID> {

    @Override
    public UUID convertToDatabaseColumn(PathId attribute) {
        return attribute != null ? attribute.value() : null;
    }

    @Override
    public PathId convertToEntityAttribute(UUID dbData) {
        return dbData != null ? PathId.of(dbData) : null;
    }
}
