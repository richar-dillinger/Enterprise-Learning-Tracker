package com.learning.tracker.shared.infrastructure.persistence;

import com.learning.tracker.shared.domain.vo.SchoolId;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.UUID;

/**
 * JPA converter for {@link SchoolId} value object.
 * <p>
 * Converts between SchoolId and UUID for database persistence.
 * Auto-applied to all SchoolId fields in entities.
 */
@Converter(autoApply = true)
public class SchoolIdConverter implements AttributeConverter<SchoolId, UUID> {

    @Override
    public UUID convertToDatabaseColumn(SchoolId attribute) {
        return attribute != null ? attribute.value() : null;
    }

    @Override
    public SchoolId convertToEntityAttribute(UUID dbData) {
        return dbData != null ? SchoolId.of(dbData) : null;
    }
}
