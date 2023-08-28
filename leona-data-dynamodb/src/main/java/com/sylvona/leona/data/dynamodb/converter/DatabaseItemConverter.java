package com.sylvona.leona.data.dynamodb.converter;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.sylvona.leona.data.dynamodb.AttributeType;
import com.sylvona.leona.data.dynamodb.converter.converters.ListConverter;
import jakarta.annotation.Nullable;
import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.core.GenericTypeResolver;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

/**
 * Abstract base class for converting between Java types and DynamoDB attribute types.
 * Subclasses of this converter are responsible for defining the conversion logic.
 * {@code DBType} should always be a subclass of {@link AttributeType}.
 * <p>
 * When extending this class, implementers can either define a no-args constructor for reflections-based initializations or,
 * for generic-implementations (ex: a {@code List<T>} converter) a single-arg constructor that accepts a generic class is acceptable.
 * Upon initialization, the generic class will represent the type argument of the overall generic class.
 * <br/>
 * <p>Usage example:
 * <pre>
 * {@code
 * public class CollectionConverter<TValue> extends DatabaseItemConverter<Collection<TValue>, AttributeType.List> {
 *     public CollectionConverter(Class<?> genericClass) {
 *         // code... usually assigning genericType to a variable
 *     }
 *     // converter methods ...
 * }
 * }
 * </pre>
 *
 * @param <JavaType> The Java type to convert.
 * @param <DBType> The DynamoDB {@link AttributeType} to convert to.
 * @see ListConverter
 */
@Getter
public abstract class DatabaseItemConverter<JavaType, DBType extends AttributeType<?>> {
    private final Class<DBType> databaseType;
    private final Class<JavaType> targetType;
    @Getter(AccessLevel.NONE)
    private final Constructor<DBType> typeConstructor;

    /**
     * Constructs a DatabaseItemConverter instance.
     * Resolves type arguments, initializes necessary variables, and sets up the type constructor.
     */
    public DatabaseItemConverter() {
        //noinspection unchecked
        Class<?>[] typeArguments = Objects.requireNonNull(GenericTypeResolver.resolveTypeArguments(this.getClass(), DatabaseItemConverter.class));
        this.targetType = (Class<JavaType>) typeArguments[0];
        this.databaseType = (Class<DBType>) typeArguments[1];

        try {
            this.typeConstructor = databaseType.getDeclaredConstructor(AttributeValue.class);
            this.typeConstructor.setAccessible(true);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Converts a Java object into a DynamoDB acceptable {@link AttributeType}.
     *
     * @param javaType The object to convert.
     * @param registry the registry of converters.
     * @return The converted object as an {@link AttributeType}.
     * @see AttributeType
     */
    public abstract DBType toDatabase(JavaType javaType, ConverterRegistryView registry);

    /**
     * Converts a DynamoDB {@link AttributeType} into its corresponding Java-object representation.
     *
     * @param dbType The {@link AttributeType} to convert.
     * @param registry the registry of converters.
     * @return The converted Java object.
     */
    public abstract JavaType toJavaObject(DBType dbType, ConverterRegistryView registry);

    /**
     * Specifies whether this converter allows for the conversion of null values. Defaults to false.
     * When enabled, null fields are transformed into a database representation.
     *
     * @return True if null conversion is supported, otherwise false.
     */
    public boolean canConvertNull() {
        return false;
    }

    /**
     * Generic version of {@link #toDatabase(Object, ConverterRegistryView)} that performs null checks and type checks
     * on the input before calling the actual conversion function.
     * This function is typically invoked internally.
     *
     * @param javaType The generic input to convert.
     * @param registry the registry of converters.
     * @return The converted object as an {@link AttributeType}.
     * @see AttributeType
     * @see #toDatabase(Object, ConverterRegistryView)
     */
    public final @Nullable DBType shortCircuitToDatabase(Object javaType, ConverterRegistryView registry) {
        if (javaType == null && !canConvertNull()) return null;
        if (javaType != null && !targetType.isInstance(javaType))
            throw new ClassCastException("Cannot cast %s to %s".formatted(javaType.getClass(), targetType));
        return toDatabase(targetType.cast(javaType), registry);
    }

    /**
     * Parses an {@link AttributeValue} into a Java object by first converting it to an {@link AttributeType},
     * and then calling {@link #toJavaObject(AttributeType, ConverterRegistryView)}.
     * This function is typically invoked internally.
     *
     * @param attributeValue The value to parse.
     * @param registry       the registry of converters.
     * @return The converted value as a Java object.
     * @see #toJavaObject(AttributeType, ConverterRegistryView)
     */
    public final JavaType parseAttributeValue(AttributeValue attributeValue, ConverterRegistryView registry) {
        try {
            DBType databaseType = typeConstructor.newInstance(attributeValue);
            return toJavaObject(databaseType, registry);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
