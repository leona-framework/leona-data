package com.sylvona.leona.data.dynamodb.converter;

import com.sylvona.leona.core.commons.streams.LINQ;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A factory implementation of the {@link ConverterRegistryFactory} interface that provides a default implementation of
 * creating {@link ConverterRegistry} instances.
 * <p>
 * The {@code DefaultConverterRegistryFactory} class manages a map of {@code ConverterRegistry} instances associated with
 * specific classes. It allows for the creation of new {@code ConverterRegistry} instances, initializing them with default
 * converters and additional converters provided during construction.
 */
public class DefaultConverterRegistryFactory implements ConverterRegistryFactory {
    private final Map<Class<?>, ConverterRegistry> registryMap = new HashMap<>();
    private final List<DatabaseItemConverter<?, ?>> defaultConverters;

    /**
     * Constructs a {@code DefaultConverterRegistryFactory} instance with the specified additional converters.
     *
     * @param converters Additional converters to be included in the default converter registry.
     */
    public DefaultConverterRegistryFactory(List<DatabaseItemConverter<?, ?>> converters) {
        defaultConverters = LINQ.stream(ConverterRegistry.DEFAULT_CONVERTS).concat(converters).toList();
    }

    /**
     * Retrieves a {@link ConverterRegistry} instance associated with the given class. If no registry exists for the class,
     * a new instance is created and initialized with default and additional converters.
     *
     * @param cls The class for which to retrieve the converter registry.
     * @return The {@link ConverterRegistry} instance associated with the specified class.
     */
    @Override
    public ConverterRegistry getConverterRegistry(Class<?> cls) {
        return registryMap.computeIfAbsent(cls, ignored -> new InitializedConverterRegistry(defaultConverters));
    }
}
