package com.sylvona.leona.data.dynamodb.converter.converters;

import com.sylvona.leona.data.dynamodb.AttributeType;
import com.sylvona.leona.data.dynamodb.converter.ConverterContext;
import com.sylvona.leona.data.dynamodb.converter.DatabaseItemConverter;

public class BooleanConverter extends DatabaseItemConverter<Boolean, AttributeType.Bool> {
    @Override
    public AttributeType.Bool toDatabase(Boolean aBoolean, ConverterContext context) {
        return AttributeType.bool(aBoolean);
    }

    @Override
    public Boolean toJavaObject(AttributeType.Bool bool, ConverterContext context) {
        return bool.getValue();
    }
}
