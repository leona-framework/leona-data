package com.sylvona.leona.data.dynamodb.converter.converters;

import com.sylvona.leona.data.dynamodb.AttributeType;
import com.sylvona.leona.data.dynamodb.converter.ConverterContext;
import com.sylvona.leona.data.dynamodb.converter.DatabaseItemConverter;

import java.time.Instant;

public class InstantConverter extends DatabaseItemConverter<Instant, AttributeType.Number> {
    @Override
    public AttributeType.Number toDatabase(Instant instant, ConverterContext context) {
        return AttributeType.numeric(instant.getEpochSecond());
    }

    @Override
    public Instant toJavaObject(AttributeType.Number number, ConverterContext context) {
        return Instant.ofEpochSecond(number.toLong());
    }
}
