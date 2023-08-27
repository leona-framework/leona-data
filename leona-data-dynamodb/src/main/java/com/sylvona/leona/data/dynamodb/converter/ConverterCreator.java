package com.sylvona.leona.data.dynamodb.converter;

import jakarta.annotation.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ConverterCreator {
    public static DatabaseItemConverter<?, ?> createConverter(Class<?> converterClass, @Nullable Type genericType) {
        try {
            return createConverterInternal(converterClass, genericType);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Could not find an empty constructor for converter class %s".formatted(converterClass), e);
        } catch (ReflectiveOperationException exception) {
            throw new RuntimeException("Could not construct converter class %s".formatted(converterClass), exception);
        }
    }

    private static DatabaseItemConverter<?, ?> createConverterInternal(Class<?> converterClass, @Nullable Type genericType) throws ReflectiveOperationException {
        Constructor<?> emptyCtor = converterClass.getDeclaredConstructor();
        ReflectiveOperationException reflectiveOperationException;
        try {
            emptyCtor.setAccessible(true);
            return (DatabaseItemConverter<?, ?>) emptyCtor.newInstance();
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            reflectiveOperationException = e;
        }

        ParameterizedType pType = (genericType instanceof ParameterizedType parameterizedType) ? parameterizedType : null;
        if (pType == null) throw reflectiveOperationException;

        Constructor<?> genericTypeCtor = converterClass.getDeclaredConstructor(Class.class);
        Class<?> typeArgument = (Class<?>) pType.getActualTypeArguments()[0];
        return (DatabaseItemConverter<?, ?>) genericTypeCtor.newInstance(typeArgument);
    }
}
