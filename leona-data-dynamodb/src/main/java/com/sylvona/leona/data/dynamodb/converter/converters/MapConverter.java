package com.sylvona.leona.data.dynamodb.converter.converters;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.sylvona.leona.core.commons.streams.LINQ;
import com.sylvona.leona.data.dynamodb.AttributeType;
import com.sylvona.leona.data.dynamodb.converter.ConverterRegistryView;
import com.sylvona.leona.data.dynamodb.converter.DatabaseItemConverter;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Converts a generic map to/from a DynamoDB Map
 * @param <TValue> the map type
 * @see AttributeType.Map
 */
public class MapConverter<TValue> extends DatabaseItemConverter<Map<String, TValue>, AttributeType.Map> {
    private final Type valueType;

    /**
     * Constructs converter with the generic map type.
     * @param valueType the generic type of map
     */
    public MapConverter(Type valueType) {
        this.valueType = valueType;
    }

    @Override
    public AttributeType.Map toDatabase(Map<String, TValue> map, ConverterRegistryView registry) {
        return AttributeType.map(LINQ.stream(map.entrySet()).toMap(Map.Entry::getKey, e -> toValue(e.getValue(), registry)));
    }

    @Override
    public Map<String, TValue> toJavaObject(AttributeType.Map map, ConverterRegistryView registry) {
        return LINQ.stream(map.getValue().entrySet()).toMap(Map.Entry::getKey, e -> registry.parse(registry, valueType, e.getValue()));
    }

    private AttributeValue toValue(TValue value, ConverterRegistryView context) {
        return context.getConverterForType(valueType).toDatabase(value, context).attributeValue();
    }
}
