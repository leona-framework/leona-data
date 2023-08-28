package com.sylvona.leona.data.dynamodb.converter;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.sylvona.leona.core.commons.streams.LINQ;
import com.sylvona.leona.data.dynamodb.AttributeType;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

class InitializedConverterRegistry implements ConverterRegistry {
    private final Map<Type, DatabaseItemConverter<?, ?>> itemConverters;
    private Type targetType;

    public InitializedConverterRegistry(List<DatabaseItemConverter<?, ?>> converters) {
        itemConverters = LINQ.stream(converters).toMap(DatabaseItemConverter::getTargetType, c -> c);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T, A extends AttributeType<?>> DatabaseItemConverter<T, A> getConverterForType(Type type) {
        return (DatabaseItemConverter<T, A>) itemConverters.get(type);
    }

    @Override
    public void addConverter(Type type, DatabaseItemConverter<?, ?> converter) {
        itemConverters.put(type, converter);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T parse(ConverterRegistryView context, Type targetType, AttributeValue attributeValue) {
        this.targetType = targetType;
        return (T) context.getConverterForType(targetType).parseAttributeValue(attributeValue, context);
    }

    @Override
    public <T> T parse(Type targetType, AttributeValue attributeValue) {
        return parse(this, targetType, attributeValue);
    }
}
