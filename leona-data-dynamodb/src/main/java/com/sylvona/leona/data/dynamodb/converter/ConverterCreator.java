package com.sylvona.leona.data.dynamodb.converter;

import com.sylvona.leona.core.commons.exceptions.NoSuchMethodRuntimeException;
import com.sylvona.leona.core.commons.exceptions.ReflectiveOperationRuntimeException;
import jakarta.annotation.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Utility class to help create {@link DatabaseItemConverter} from classes.
 */
public class ConverterCreator {
    /**
     * Creates a {@link DatabaseItemConverter} from a provided class and an optional generic type representing the target of a type argument.
     * @param converterClass the class to construct
     * @param genericType an optional type, usually the target of a generic type argument.
     * @return the constructor {@link DatabaseItemConverter}.
     * @throws NoSuchMethodRuntimeException if no eligible constructor on the {@code converterClass} was found.
     * @throws ReflectiveOperationRuntimeException if there was an error invoking the constructor.
     */
    public static DatabaseItemConverter<?, ?> createConverter(Class<? extends DatabaseItemConverter> converterClass, @Nullable Type genericType) {
        try {
            return createConverterInternal(converterClass, genericType);
        } catch (NoSuchMethodException e) {
            throw new NoSuchMethodRuntimeException("Could not find an empty constructor for converter class %s".formatted(converterClass), e);
        } catch (ReflectiveOperationException exception) {
            throw new ReflectiveOperationRuntimeException("Could not construct converter class %s".formatted(converterClass), exception);
        }
    }

    private static DatabaseItemConverter<?, ?> createConverterInternal(Class<? extends DatabaseItemConverter> converterClass, @Nullable Type genericType) throws ReflectiveOperationException {
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
