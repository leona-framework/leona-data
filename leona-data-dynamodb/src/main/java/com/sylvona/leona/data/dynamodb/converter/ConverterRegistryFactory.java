package com.sylvona.leona.data.dynamodb.converter;

/**
 * A factory interface for creating instances of {@link ConverterRegistry}.
 * <p>
 * The {@code ConverterRegistryFactory} interface defines a method for obtaining an instance of {@link ConverterRegistry} tailored
 * for a specific class. This factory provides a way to create and manage multiple instances of converter registries, each customized
 * for handling conversions related to a particular class or context.
 */
public interface ConverterRegistryFactory {

    /**
     * Retrieves a customized {@link ConverterRegistry} instance suitable for the provided class.
     *
     * @param cls The class for which the converter registry is intended.
     * @return An instance of {@link ConverterRegistry} customized for the specified class.
     */
    ConverterRegistry getConverterRegistry(Class<?> cls);
}

