package com.sylvona.leona.data.dynamodb.converter;

public interface ConverterRegistryFactory {
    ConverterRegistry getConverterRegistry(Class<?> cls);
}
