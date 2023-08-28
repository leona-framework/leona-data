package com.sylvona.leona.data.dynamodb.converter;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.sylvona.leona.data.dynamodb.AttributeType;
import com.sylvona.leona.data.dynamodb.converter.converters.BooleanConverter;
import com.sylvona.leona.data.dynamodb.converter.converters.InstantConverter;
import com.sylvona.leona.data.dynamodb.converter.converters.NumericConverter;
import com.sylvona.leona.data.dynamodb.converter.converters.StringConverter;

import java.lang.reflect.Type;
import java.util.List;

/**
 * A registry for managing and accessing converters used in the conversion of {@link AttributeType AttributeTypes} to Java types.
 * <p>
 * The {@code ConverterRegistry} interface provides methods for adding, registering, and retrieving converters that are used in the process
 * of converting attribute values to Java types, and vice versa. It allows customization of conversion behavior by allowing users to provide
 * their own converters or use default converters provided by the registry.
 * <p>
 * The interface extends {@link ConverterRegistryView}, providing read-only methods for querying the registered converters.
 * The methods in this interface allow users to add converters directly or indirectly through converter classes, as well as parse attribute
 * values to obtain their corresponding Java representations.
 */
public interface ConverterRegistry extends ConverterRegistryView {
    /**
     * A list of default converters provided by the registry.
     */
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
    /**
     * Adds a converter to the registry for the specified target type.
     *
     * @param type      The target type for the converter.
     * @param converter The converter instance to be added.
     */
    void addConverter(Type type, DatabaseItemConverter<?, ?> converter);

    /**
     * Adds a {@link DatabaseItemConverter} to the registry for its specified target type.
     *
     * @param converter The converter instance to be added.
     */
    default void addConverter(DatabaseItemConverter<?, ?> converter) {
        addConverter(converter.getTargetType(), converter);
    }

    /**
     * Adds a converter to the registry using the provided converter class.
     * An instance of the converter is created using the default constructor.
     *
     * @param converterClass The class of the converter to be added.
     */
    default void addConverter(Class<? extends DatabaseItemConverter<?, ?>> converterClass) {
        addConverter(ConverterCreator.createConverter(converterClass, null));
    }

    /**
     * Parses an attribute value to obtain its corresponding Java representation of the specified target type.
     *
     * @param targetType     The target type to which the attribute value should be parsed.
     * @param attributeValue The attribute value to be parsed.
     * @param <T>            The type of the target representation.
     * @return The Java representation of the parsed attribute value.
     */
    <T> T parse(Type targetType, AttributeValue attributeValue);
}
