package com.learning.tracker.shared.infrastructure.persistence;

import com.learning.tracker.shared.domain.vo.UserId;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.UUID;

/**
 * JPA converter for {@link UserId} value object.
 * <p>
 * Converts between UserId and UUID for database persistence.
 * Auto-applied to all UserId fields in entities.
 */
@Converter(autoApply = true)
public class UserIdConverter implements AttributeConverter<UserId, UUID> {

    @Override
    public UUID convertToDatabaseColumn(UserId attribute) {
        return attribute != null ? attribute.value() : null;
    }

    @Override
    public UserId convertToEntityAttribute(UUID dbData) {
        return dbData != null ? UserId.of(dbData) : null;
    }
}
