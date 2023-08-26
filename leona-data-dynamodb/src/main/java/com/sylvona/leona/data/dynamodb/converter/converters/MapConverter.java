package com.sylvona.leona.data.dynamodb.converter.converters;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.sylvona.leona.core.commons.streams.LINQ;
import com.sylvona.leona.data.dynamodb.AttributeType;
import com.sylvona.leona.data.dynamodb.converter.ConverterContext;
import com.sylvona.leona.data.dynamodb.converter.DatabaseItemConverter;

import java.lang.reflect.Type;
import java.util.Map;

public class MapConverter<TValue> extends DatabaseItemConverter<Map<String, TValue>, AttributeType.Map> {
    private final Type valueType;

    public MapConverter(Type valueType) {
        this.valueType = valueType;
    }

    @Override
    public AttributeType.Map toDatabase(Map<String, TValue> map, ConverterContext context) {
        return AttributeType.map(LINQ.stream(map.entrySet()).toMap(Map.Entry::getKey, e -> toValue(e.getValue(), context)));
    }

    @Override
    public Map<String, TValue> toJavaObject(AttributeType.Map map, ConverterContext context) {
        return LINQ.stream(map.getValue().entrySet()).toMap(Map.Entry::getKey, e -> context.parse(context, valueType, e.getValue()));
    }

    private AttributeValue toValue(TValue value, ConverterContext context) {
        return context.getConverterForType(valueType).toDatabase(value, context).attributeValue();
    }
}
