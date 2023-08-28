package com.sylvona.leona.data.dynamodb.converter.converters;

import com.sylvona.leona.data.dynamodb.AttributeType;
import com.sylvona.leona.data.dynamodb.converter.ConverterRegistryView;
import com.sylvona.leona.data.dynamodb.converter.DatabaseItemConverter;

import java.time.Instant;

/**
 * Converts an {@link Instant} to/from a DynamoDB NUmber
 */
public class InstantConverter extends DatabaseItemConverter<Instant, AttributeType.Number> {
    @Override
    public AttributeType.Number toDatabase(Instant instant, ConverterRegistryView registry) {
        return AttributeType.numeric(instant.getEpochSecond());
    }

    @Override
    public Instant toJavaObject(AttributeType.Number number, ConverterRegistryView registry) {
        return Instant.ofEpochSecond(number.toLong());
    }
}
