package com.sylvona.leona.data.dynamodb.converter.converters;

import com.sylvona.leona.data.dynamodb.AttributeType;
import com.sylvona.leona.data.dynamodb.converter.ConverterContext;
import com.sylvona.leona.data.dynamodb.converter.DatabaseItemConverter;

public class StringConverter extends DatabaseItemConverter<String, AttributeType.String> {
    @Override
    public AttributeType.String toDatabase(String s, ConverterContext context) {
        return AttributeType.string(s);
    }

    @Override
    public String toJavaObject(AttributeType.String string, ConverterContext context) {
        return string.getValue();
    }
}
