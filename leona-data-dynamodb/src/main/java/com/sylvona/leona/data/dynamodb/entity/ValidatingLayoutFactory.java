package com.sylvona.leona.data.dynamodb.entity;

import com.sylvona.leona.data.dynamodb.converter.ConverterRegistryFactory;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class ValidatingLayoutFactory implements EntityLayoutFactory {
    private final Map<Class<?>, EntityLayout<?>> entityLayoutCache = new HashMap<>();
    private final ConverterRegistryFactory converterRegistryFactory;

    @Override
    public <T> EntityLayout<T> getLayout(Class<T> cls) {
        @SuppressWarnings("unchecked")
        EntityLayout<T> entityLayout = (EntityLayout<T>) entityLayoutCache.get(cls);
        if (entityLayout != null) return entityLayout;
        entityLayout = new ValidatedEntityLayout<>(cls, converterRegistryFactory.getConverterRegistry(cls));
        entityLayoutCache.put(cls, entityLayout);
        return entityLayout;
    }
}
