package com.sylvona.leona.data.dynamodb.converter.converters;

import com.sylvona.leona.core.commons.streams.LINQ;
import com.sylvona.leona.data.dynamodb.AttributeType;
import com.sylvona.leona.data.dynamodb.converter.ConverterRegistryView;
import com.sylvona.leona.data.dynamodb.converter.DatabaseItemConverter;

import java.lang.reflect.Array;

/**
 * Converts a generic array to/from a DynamoDB list
 * @param <TValue> the array type
 */
public class ArrayConverter<TValue> extends DatabaseItemConverter<TValue[], AttributeType.List> {
    private final Class<TValue> valueType;

    /**
     * Constructs converter with the generic array type.
     * @param valueType the generic type of array
     */
    public ArrayConverter(Class<TValue> valueType) {
        this.valueType = valueType;
    }

    @Override
    public AttributeType.List toDatabase(TValue[] tValues, ConverterRegistryView registry) {
        return AttributeType.list(registry, tValues);
    }

    @Override
    public TValue[] toJavaObject(AttributeType.List list, ConverterRegistryView registry) {
        return LINQ.stream(list.getValue()).map(av -> registry.parse(registry, valueType, av)).toArray(this::arrayGenerator);
    }

    @SuppressWarnings("unchecked")
    private TValue[] arrayGenerator(int size) {
        return (TValue[]) Array.newInstance(valueType, size);
    }
}