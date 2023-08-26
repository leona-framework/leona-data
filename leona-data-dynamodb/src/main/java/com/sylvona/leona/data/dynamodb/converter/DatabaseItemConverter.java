package com.sylvona.leona.data.dynamodb.converter;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.sylvona.leona.data.dynamodb.AttributeType;
import jakarta.annotation.Nullable;
import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.core.GenericTypeResolver;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

@Getter
public abstract class DatabaseItemConverter<JavaType, DBType extends AttributeType<?>> {
    private final Class<DBType> databaseType;
    private final Class<JavaType> targetType;
    @Getter(AccessLevel.NONE)
    private final Constructor<DBType> typeConstructor;

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

    public abstract DBType toDatabase(JavaType javaType, ConverterContext context);
    public abstract JavaType toJavaObject(DBType dbType, ConverterContext context);

    public boolean canConvertNull() {
        return false;
    }

    /**
     * Short circuits checking if allowing null values
     * @param javaType
     * @param context
     * @return
     */
    public final @Nullable DBType shortCircuitToDatabase(JavaType javaType, ConverterContext context) {
        if (javaType == null && !canConvertNull()) return null;
        return toDatabase(javaType, context);
    }

    public final JavaType parseAttributeValue(AttributeValue attributeValue, ConverterContext context) {
        try {
            DBType databaseType = typeConstructor.newInstance(attributeValue);
            return toJavaObject(databaseType, context);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
