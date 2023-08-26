package com.sylvona.leona.data.dynamodb.converter.converters;

import com.sylvona.leona.core.commons.streams.LINQ;
import com.sylvona.leona.data.dynamodb.AttributeType;
import com.sylvona.leona.data.dynamodb.converter.ConverterContext;
import com.sylvona.leona.data.dynamodb.converter.DatabaseItemConverter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ListConverter<TValue> extends DatabaseItemConverter<List<TValue>, AttributeType.List> {
    private final Type valueType;

    public ListConverter(Type valueType) {
        this.valueType = valueType;
    }

    @Override
    public AttributeType.List toDatabase(List<TValue> tValues, ConverterContext context) {
        return AttributeType.list(context, (List<Object>) tValues);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<TValue> toJavaObject(AttributeType.List list, ConverterContext context) {
        return LINQ.stream(list.getValue()).map(av -> (TValue)context.parse(context, valueType, av)).collect(ArrayList::new);
    }
}