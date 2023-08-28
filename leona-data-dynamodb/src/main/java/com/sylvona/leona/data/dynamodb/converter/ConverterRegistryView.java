package com.sylvona.leona.data.dynamodb.converter;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.sylvona.leona.data.dynamodb.AttributeType;

import java.lang.reflect.Type;

/**
 * A view interface for accessing and using converters within a {@link ConverterRegistry}.
 * <p>
 * The {@code ConverterRegistryView} interface provides methods to retrieve converters associated with specific types and perform
 * parsing operations using those converters. It acts as a read-only interface to access converter-related functionality provided
 * by a {@link ConverterRegistry}.
 */
public interface ConverterRegistryView {

    /**
     * Retrieves a {@link DatabaseItemConverter} instance associated with the given type.
     *
     * @param type The type for which to retrieve the converter.
     * @param <T>  The target type of the converter.
     * @param <A>  The attribute type of the converter.
     * @return The {@link DatabaseItemConverter} instance associated with the specified type.
     */
    <T, A extends AttributeType<?>> DatabaseItemConverter<T, A> getConverterForType(Type type);

    /**
     * Parses an {@link AttributeValue} using the appropriate converter for the target type.
     *
     * @param context        The context from which to retrieve converters.
     * @param targetType    The target type to parse the attribute value into.
     * @param attributeValue The attribute value to parse.
     * @param <T>           The target type to parse into.
     * @return An instance of the parsed target type.
     */
    <T> T parse(ConverterRegistryView context, Type targetType, AttributeValue attributeValue);
}

