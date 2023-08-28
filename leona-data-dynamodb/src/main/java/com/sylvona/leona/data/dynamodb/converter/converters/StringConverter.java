package com.sylvona.leona.data.dynamodb.converter.converters;

import com.sylvona.leona.data.dynamodb.AttributeType;
import com.sylvona.leona.data.dynamodb.converter.ConverterRegistryView;
import com.sylvona.leona.data.dynamodb.converter.DatabaseItemConverter;

public class StringConverter extends DatabaseItemConverter<String, AttributeType.String> {
    @Override
    public AttributeType.String toDatabase(String s, ConverterRegistryView registry) {
        return AttributeType.string(s);
    }

    @Override
    public String toJavaObject(AttributeType.String string, ConverterRegistryView registry) {
        return string.getValue();
    }
}
