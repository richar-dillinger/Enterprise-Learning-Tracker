package com.learning.tracker.shared.infrastructure.persistence;

import com.learning.tracker.shared.domain.vo.ResourceId;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.UUID;

/**
 * JPA converter for {@link ResourceId} value object.
 */
@Converter(autoApply = true)
public class ResourceIdConverter implements AttributeConverter<ResourceId, UUID> {

    @Override
    public UUID convertToDatabaseColumn(ResourceId attribute) {
        return attribute != null ? attribute.value() : null;
    }

    @Override
    public ResourceId convertToEntityAttribute(UUID dbData) {
        return dbData != null ? ResourceId.of(dbData) : null;
    }
}
