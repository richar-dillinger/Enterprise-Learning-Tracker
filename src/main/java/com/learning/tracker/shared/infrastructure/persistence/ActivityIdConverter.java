package com.learning.tracker.shared.infrastructure.persistence;

import com.learning.tracker.shared.domain.vo.ActivityId;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.UUID;

/**
 * JPA converter for {@link ActivityId} value object.
 * <p>
 * Converts between ActivityId and UUID for database persistence.
 * Auto-applied to all ActivityId fields in entities.
 */
@Converter(autoApply = true)
public class ActivityIdConverter implements AttributeConverter<ActivityId, UUID> {

    @Override
    public UUID convertToDatabaseColumn(ActivityId attribute) {
        return attribute != null ? attribute.value() : null;
    }

    @Override
    public ActivityId convertToEntityAttribute(UUID dbData) {
        return dbData != null ? ActivityId.of(dbData) : null;
    }
}
