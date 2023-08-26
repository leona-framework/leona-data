package com.sylvona.leona.data.dynamodb.converter;

import org.springframework.core.convert.ConversionException;

public class ConverterNotFoundException extends ConversionException {
    public ConverterNotFoundException(String message) {
        super(message);
    }
}
