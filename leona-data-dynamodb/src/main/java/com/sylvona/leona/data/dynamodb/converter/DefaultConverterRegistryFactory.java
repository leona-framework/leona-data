package com.sylvona.leona.data.dynamodb.converter;

import com.sylvona.leona.core.commons.streams.LINQ;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultConverterRegistryFactory implements ConverterRegistryFactory {
    private final Map<Class<?>, ConverterRegistry> registryMap = new HashMap<>();
    private final List<DatabaseItemConverter<?, ?>> defaultConverters;

    public DefaultConverterRegistryFactory(List<DatabaseItemConverter<?, ?>> converters) {
        defaultConverters = LINQ.stream(ConverterRegistry.DEFAULT_CONVERTS).concat(converters).toList();
    }

    @Override
    public ConverterRegistry getConverterRegistry(Class<?> cls) {
        return registryMap.computeIfAbsent(cls, ignored -> new InitializedConverterRegistry(defaultConverters));
    }
}
