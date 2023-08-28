package com.sylvona.leona.data.dynamodb.converter.converters;

import com.sylvona.leona.core.commons.streams.LINQ;
import com.sylvona.leona.data.dynamodb.AttributeType;
import com.sylvona.leona.data.dynamodb.converter.ConverterRegistryView;
import com.sylvona.leona.data.dynamodb.converter.DatabaseItemConverter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Converts a generic list to/from a DynamoDB list
 * @param <TValue> the list type
 * @see AttributeType.List
 */
public class ListConverter<TValue> extends DatabaseItemConverter<List<TValue>, AttributeType.List> {
    private final Type valueType;

    /**
     * Constructs converter with the generic list type.
     * @param valueType the generic type of list
     */
    public ListConverter(Type valueType) {
        this.valueType = valueType;
    }

    @Override
    public AttributeType.List toDatabase(List<TValue> tValues, ConverterRegistryView registry) {
        return AttributeType.list(registry, (List<Object>) tValues);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<TValue> toJavaObject(AttributeType.List list, ConverterRegistryView registry) {
        return LINQ.stream(list.getValue()).map(av -> (TValue) registry.parse(registry, valueType, av)).collect(ArrayList::new);
    }
}