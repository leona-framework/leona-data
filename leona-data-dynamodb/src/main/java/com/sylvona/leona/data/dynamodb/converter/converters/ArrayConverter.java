package com.sylvona.leona.data.dynamodb.converter.converters;

import com.sylvona.leona.core.commons.streams.LINQ;
import com.sylvona.leona.data.dynamodb.AttributeType;
import com.sylvona.leona.data.dynamodb.converter.ConverterContext;
import com.sylvona.leona.data.dynamodb.converter.DatabaseItemConverter;

import java.lang.reflect.Array;

public class ArrayConverter<TValue> extends DatabaseItemConverter<TValue[], AttributeType.List> {
    private final Class<TValue> valueType;

    public ArrayConverter(Class<TValue> valueType) {
        this.valueType = valueType;
    }

    @Override
    public AttributeType.List toDatabase(TValue[] tValues, ConverterContext context) {
        return AttributeType.list(context, tValues);
    }

    @Override
    public TValue[] toJavaObject(AttributeType.List list, ConverterContext context) {
        return LINQ.stream(list.getValue()).map(av -> context.parse(context, valueType, av)).toArray(this::arrayGenerator);
    }

    @SuppressWarnings("unchecked")
    private TValue[] arrayGenerator(int size) {
        return (TValue[]) Array.newInstance(valueType, size);
    }
}