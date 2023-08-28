package com.sylvona.leona.data.dynamodb.converter.converters;

import com.sylvona.leona.data.dynamodb.AttributeType;
import com.sylvona.leona.data.dynamodb.converter.ConverterRegistryView;
import com.sylvona.leona.data.dynamodb.converter.DatabaseItemConverter;

/**
 * Converts a boolean to/from a DynamoDB {@link AttributeType.Bool Bool}.
 */
public class BooleanConverter extends DatabaseItemConverter<Boolean, AttributeType.Bool> {
    @Override
    public AttributeType.Bool toDatabase(Boolean aBoolean, ConverterRegistryView registry) {
        return AttributeType.bool(aBoolean);
    }

    @Override
    public Boolean toJavaObject(AttributeType.Bool bool, ConverterRegistryView registry) {
        return bool.getValue();
    }
}
