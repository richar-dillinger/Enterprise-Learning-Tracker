package com.learning.tracker.shared.infrastructure.persistence;

import com.learning.tracker.shared.domain.vo.EnrollmentId;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.UUID;

/**
 * JPA converter for {@link EnrollmentId} value object.
 * <p>
 * Converts between EnrollmentId and UUID for database persistence.
 * Auto-applied to all EnrollmentId fields in entities.
 */
@Converter(autoApply = true)
public class EnrollmentIdConverter implements AttributeConverter<EnrollmentId, UUID> {

    @Override
    public UUID convertToDatabaseColumn(EnrollmentId attribute) {
        return attribute != null ? attribute.value() : null;
    }

    @Override
    public EnrollmentId convertToEntityAttribute(UUID dbData) {
        return dbData != null ? EnrollmentId.of(dbData) : null;
    }
}
