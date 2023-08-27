package com.sylvona.leona.data.dynamodb.entity;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.sylvona.leona.data.dynamodb.AttributeType;
import com.sylvona.leona.data.dynamodb.converter.ConverterRegistry;
import com.sylvona.leona.data.dynamodb.converter.DatabaseItemConverter;
import jakarta.annotation.Nullable;
import lombok.Getter;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

class EntityField<Entity> {
    @Getter private final Type fieldTargetType;
    private final ConverterRegistry converterRegistry;
    private final Field field;
    private final DatabaseItemConverter<?, ?> customConverter;

    EntityField(Field field, ConverterRegistry converterRegistry, @Nullable DatabaseItemConverter<?, ?> customConverter) {
        this.converterRegistry = converterRegistry;
        this.field = field;
        this.field.setAccessible(true);
        this.fieldTargetType = field.getGenericType() instanceof ParameterizedType parameterizedType ? parameterizedType : field.getType();
        this.customConverter = customConverter;
    }

    public void writeField(Entity entity, AttributeValue attributeValue) {
        try {
            FieldUtils.writeField(field, entity, converterRegistry.parse(fieldTargetType, attributeValue));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public @Nullable AttributeValue readToAttributeValue(Entity entity) {
        try {
            Object value = FieldUtils.readField(field, entity);

            DatabaseItemConverter<?, ?> converter = customConverter;
            if (converter == null) converter = converterRegistry.getConverterForType(fieldTargetType);
            AttributeType<?> attributeType = converter.shortCircuitToDatabase(value, converterRegistry);
            return attributeType == null ? null : attributeType.attributeValue();

        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
