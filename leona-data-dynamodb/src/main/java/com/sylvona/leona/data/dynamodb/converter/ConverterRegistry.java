package com.sylvona.leona.data.dynamodb.converter;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.sylvona.leona.data.dynamodb.converter.converters.BooleanConverter;
import com.sylvona.leona.data.dynamodb.converter.converters.InstantConverter;
import com.sylvona.leona.data.dynamodb.converter.converters.NumericConverter;
import com.sylvona.leona.data.dynamodb.converter.converters.StringConverter;

import java.lang.reflect.Type;
import java.util.List;

public interface ConverterRegistry extends ConverterContext {
    List<DatabaseItemConverter<?, ?>> DEFAULT_CONVERTS = List.of(
            new StringConverter(),
            new BooleanConverter(),
            new InstantConverter(),
            new NumericConverter.DoubleConverter(),
            new NumericConverter.BigIntegerConverter(),
            new NumericConverter.FloatConverter(),
            new NumericConverter.IntegerConverter(),
            new NumericConverter.LongConverter()
    );

    void addConverter(Type type, DatabaseItemConverter<?, ?> converter);

    default void addConverter(DatabaseItemConverter<?, ?> converter) {
        addConverter(converter.getTargetType(), converter);
    }

    default void addConverter(Class<? extends DatabaseItemConverter<?, ?>> converterClass) {
        addConverter(ConverterCreator.createConverter(converterClass, null));
    }

    <T> T parse(Type targetType, AttributeValue attributeValue);
}
