package com.sylvona.leona.data.dynamodb.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.sylvona.leona.core.commons.streams.LINQ;
import com.sylvona.leona.data.dynamodb.annotations.*;
import com.sylvona.leona.data.dynamodb.converter.ConverterCreator;
import com.sylvona.leona.data.dynamodb.converter.ConverterRegistry;
import com.sylvona.leona.data.dynamodb.converter.DatabaseItemConverter;
import com.sylvona.leona.data.dynamodb.converter.converters.ArrayConverter;
import com.sylvona.leona.data.dynamodb.converter.converters.EnumConverter;
import com.sylvona.leona.data.dynamodb.converter.converters.ListConverter;
import com.sylvona.leona.data.dynamodb.converter.converters.MapConverter;
import jakarta.annotation.Nullable;
import jakarta.validation.*;
import lombok.Getter;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.*;
import java.util.*;

public class ValidatedEntityLayout<T> implements EntityLayout<T> {
    private final Map<String, EntityField<T>> entityFields = new HashMap<>();
    private final Validator validator;
    private final ConverterRegistry converterRegistry;
    private final Class<T> entityClass;
    private final Constructor<T> emptyConstructor;
    private final EntityAnnotationExtractor<T> annotationExtractor;
    @Getter private final String entityName;

    public ValidatedEntityLayout(Class<T> cls, ConverterRegistry converterRegistry) {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
        validatorFactory.close();

        this.entityClass = cls;
        this.converterRegistry = converterRegistry;

        try {
            this.emptyConstructor = cls.getDeclaredConstructor();
            this.emptyConstructor.setAccessible(true);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Could not find eligible empty constructor for class.", e);
        }

        annotationExtractor = new EntityAnnotationExtractor<>(converterRegistry, this::addField, entityClass);

        for (Field declaredField : cls.getDeclaredFields()) {
            if (annotationExtractor.extract(declaredField, DynamoDBHashKey.class)) continue;
            if (annotationExtractor.extract(declaredField, DynamoDBRangeKey.class)) continue;
            if (annotationExtractor.extract(declaredField, PartitionKey.class)) continue;
            if (annotationExtractor.extract(declaredField, RangeKey.class)) continue;
            annotationExtractor.extract(declaredField, Attribute.class);
        }

        DynamoEntity dynamoEntity = annotationExtractor.getDynamoEntity();
        entityName = dynamoEntity.value();
        LINQ.stream(dynamoEntity.converters()).forEach(converterRegistry::addConverter);
    }

    public T createEntity(@Nullable Map<String, AttributeValue> attributes) {
        T entity = instantiateEntity();
        if (attributes == null) return null;

        for (Map.Entry<String, AttributeValue> entry : attributes.entrySet()) {
            EntityField<T> entityField = entityFields.get(entry.getKey());
            if (entityField == null) throw new NotImplementedException();
            entityField.writeField(entity, entry.getValue());
        }

        return entity;
    }

    public Map<String, AttributeValue> getAttributes(T entity) {
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(entity);
        if (!constraintViolations.isEmpty())
            throw new ConstraintViolationException(constraintViolations);

        Map<String, AttributeValue> attributeValueMap = new HashMap<>();
        for (Map.Entry<String, EntityField<T>> entry : entityFields.entrySet()) {
            AttributeValue value = entry.getValue().readToAttributeValue(entity);
            if (value != null) attributeValueMap.put(entry.getKey(), value);
        }

        return attributeValueMap;
    }

    @Override
    public <ID1, ID2> Map<String, AttributeValue> getSignificantKeys(ID1 id1, @Nullable ID2 id2) {
        Map<String, AttributeValue> keyMap = new HashMap<>();
        EntityField<T> entityField = annotationExtractor.getPrimaryKeyField();
        AttributeValue primaryKeyValue = converterRegistry.getConverterForType(entityField.getFieldTargetType()).toDatabase(id1, converterRegistry).attributeValue();
        keyMap.put(annotationExtractor.getPrimaryKeyName(), primaryKeyValue);

        if (id2 == null) return keyMap;
        entityField = annotationExtractor.getRangeKeyField();
        if (entityField == null)
            throw new EntityLayoutException("Entity class %s does not contain a range key.".formatted(entityClass));

        AttributeValue rangeKeyValue = converterRegistry.getConverterForType(entityField.getFieldTargetType()).toDatabase(id2, converterRegistry).attributeValue();
        keyMap.put(annotationExtractor.getRangeKeyName(), rangeKeyValue);
        return keyMap;
    }

    @Override
    public String getPrimaryKeyName() {
        return annotationExtractor.getPrimaryKeyName();
    }

    @Nullable
    @Override
    public String getRangeKeyName() {
        return annotationExtractor.getRangeKeyName();
    }

    private T instantiateEntity() {
        try {
            return emptyConstructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private void addField(AnnotationAttribute annotationAttribute, Field field) {
        addCustomConverters(field.getType(), field.getGenericType());

        Class<? extends DatabaseItemConverter> customConverterClass = annotationAttribute.getConverter();
        DatabaseItemConverter<?, ?> customConverter = null;
        if (customConverterClass != null && !customConverterClass.equals(DatabaseItemConverter.class)) {
            Type fieldGenericType = field.getGenericType();
            Type baseFieldType = fieldGenericType instanceof ParameterizedType parameterizedType ? parameterizedType : field.getType();
            customConverter = ConverterCreator.createConverter(customConverterClass, baseFieldType);
        }

        entityFields.put(getFieldName(annotationAttribute, field), new EntityField<T>(field, converterRegistry, customConverter));
    }


    private void addCustomConverters(Class<?> cls, @Nullable Type targetType) {
        ParameterizedType pType = (targetType instanceof ParameterizedType parameterizedType) ? parameterizedType : null;

        if (cls.isArray()) {
            Class<?> componentType = cls.componentType();
            converterRegistry.addConverter(cls, new ArrayConverter<>(componentType));
            addCustomConverters(componentType, null);
        }

        if (cls.isEnum()) {
            converterRegistry.addConverter(cls, new EnumConverter<>(cls));
        }

        // List type
        if (cls.isAssignableFrom(List.class)) {
            Class<?> argumentType = (Class<?>)Objects.requireNonNull(pType).getActualTypeArguments()[0];
            converterRegistry.addConverter(pType, new ListConverter<>(argumentType));
            addCustomConverters(argumentType, null);
        }

        // Map type
        if (cls.isAssignableFrom(Map.class)) {
            Class<?> argumentType = (Class<?>)Objects.requireNonNull(pType).getActualTypeArguments()[1];
            converterRegistry.addConverter(pType, new MapConverter<>(argumentType));
            addCustomConverters(argumentType, null);
        }
    }

    private String getFieldName(AnnotationAttribute annotationAttribute, Field field) {
        String attributeName = annotationAttribute.getAttributeName();
        if (!StringUtils.isBlank(attributeName)) return attributeName;
        return field.getName();
    }
}
