package com.sylvona.leona.data.dynamodb.converter;

import org.springframework.core.convert.ConversionException;

/**
 * Exception thrown when a suitable {@link DatabaseItemConverter} is not found for a conversion operation.
 * <p>
 * This exception is raised when a conversion operation is attempted, but no appropriate converter is available to perform the conversion.
 * It indicates that the requested conversion cannot be executed because there is no registered converter that supports the given source and target types.
 */
public class ConverterNotFoundException extends ConversionException {

    /**
     * Constructs a new {@code ConverterNotFoundException} instance with the specified error message.
     *
     * @param message The error message describing the exception.
     */
    public ConverterNotFoundException(String message) {
        super(message);
    }
}