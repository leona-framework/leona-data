package com.sylvona.leona.data.dynamodb.layout;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.sylvona.leona.core.commons.containers.Tuple;
import com.sylvona.leona.data.dynamodb.AttributeKeyValue;
import com.sylvona.leona.data.dynamodb.AttributeType;
import com.sylvona.leona.data.dynamodb.converter.ConverterRegistry;
import com.sylvona.leona.data.dynamodb.annotations.Attribute;
import com.sylvona.leona.data.dynamodb.annotations.DynamoEntity;
import com.sylvona.leona.data.dynamodb.converter.converters.ArrayConverter;
import com.sylvona.leona.data.dynamodb.converter.converters.EnumConverter;
import com.sylvona.leona.data.dynamodb.converter.converters.ListConverter;
import com.sylvona.leona.data.dynamodb.converter.converters.MapConverter;
import jakarta.annotation.Nullable;
import jakarta.validation.*;
import lombok.Getter;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.*;
import java.util.*;

public class ValidatedEntityLayout<T> implements EntityLayout<T> {
    private final Map<String, EntityField> entityFields = new HashMap<>();
    private final Validator validator;
    private final ConverterRegistry converterRegistry;
    private final Class<T> entityClass;
    private final Constructor<T> emptyConstructor;
    private final Tuple<String, EntityField> primaryKeyField;
    @Getter
    private final String entityName;

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

        String primaryEntityKey = null;
        EntityField primaryEntityField = null;

        for (Field declaredField : cls.getDeclaredFields()) {
            String attributeName = null;

            DynamoDBHashKey dynamoDBHashKey = declaredField.getAnnotation(DynamoDBHashKey.class);
            if (dynamoDBHashKey != null) {
                if (primaryEntityField != null) throw new NotImplementedException();
                attributeName = primaryEntityKey = dynamoDBHashKey.attributeName();
                primaryEntityField = new EntityField(declaredField, converterRegistry);
            }

            DynamoDBRangeKey dynamoDBRangeKey = declaredField.getAnnotation(DynamoDBRangeKey.class);
            if (dynamoDBRangeKey != null) {
                if (primaryEntityField != null) throw new NotImplementedException();
                attributeName = primaryEntityKey = dynamoDBRangeKey.attributeName();
                primaryEntityField = new EntityField(declaredField, converterRegistry);
            }

            Attribute attribute = declaredField.getAnnotation(Attribute.class);
            if (attribute != null) attributeName = attribute.value();
            if (attributeName != null)
                addField(attributeName, declaredField);
        }

        if (primaryEntityField == null || primaryEntityKey == null) {
            throw new IllegalArgumentException("Entity class requires a primary key attribute.");
        }

        primaryKeyField = Tuple.of(primaryEntityKey, primaryEntityField);
        DynamoEntity dynamoEntity = cls.getAnnotation(DynamoEntity.class);
        entityName = dynamoEntity == null ? cls.getSimpleName() : dynamoEntity.value();
    }

    public T createEntity(@Nullable Map<String, AttributeValue> attributes) {
        T entity = instantiateEntity();
        if (attributes == null) return entity;

        for (Map.Entry<String, AttributeValue> entry : attributes.entrySet()) {
            EntityField entityField = entityFields.get(entry.getKey());
            if (entityField == null) throw new NotImplementedException();
            entityField.writeField(entity, entry.getValue());
        }

/*        Set<ConstraintViolation<T>> constraintViolations = validator.validate(entity);
        if (!constraintViolations.isEmpty())
            throw new ConstraintViolationException(constraintViolations);*/

        return entity;
    }

    public Map<String, AttributeValue> getAttributes(T entity) {
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(entity);
        if (!constraintViolations.isEmpty())
            throw new ConstraintViolationException(constraintViolations);

        Map<String, AttributeValue> attributeValueMap = new HashMap<>();
        for (Map.Entry<String, EntityField> entry : entityFields.entrySet()) {
            AttributeValue value = entry.getValue().readToAttributeValue(entity);
            if (value != null) attributeValueMap.put(entry.getKey(), value);
        }

        return attributeValueMap;
    }

    public Map<String, AttributeValue> getPrimaryKey(T entity) {
        return new AttributeKeyValue(primaryKeyField.item1(), Objects.requireNonNull(primaryKeyField.item2().readToAttributeValue(entity)));
    }

    @Override
    public <ID> AttributeValue getPrimaryKeyValue(ID id) {
        return converterRegistry.getConverterForType(primaryKeyField.item2().fieldTargetType).toDatabase(id, converterRegistry).attributeValue();
    }

    @Override
    public String getEntityName() {
        return entityName;
    }

    @Override
    public String getPrimaryKeyName() {
        return primaryKeyField.item1();
    }

    private T instantiateEntity() {
        try {
            return emptyConstructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private void addField(String annotationValueName, Field field) {
        addCustomConverters(field.getType(), field.getGenericType());
        entityFields.put(getFieldName(annotationValueName, field), new EntityField(field, converterRegistry));
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

    private String getFieldName(String annotationValue, Field field) {
        if (!StringUtils.isBlank(annotationValue)) return annotationValue;
        return field.getName();
    }


    public class EntityField {
        private final ConverterRegistry converterRegistry;
        private final Field field;
        private final Type fieldTargetType;


        private EntityField(Field field, ConverterRegistry converterRegistry) {
            this.converterRegistry = converterRegistry;
            this.field = field;
            this.field.setAccessible(true);
            this.fieldTargetType = field.getGenericType() instanceof ParameterizedType parameterizedType ? parameterizedType : field.getType();
        }

        public void writeField(T entity, AttributeValue attributeValue) {
            try {
                FieldUtils.writeField(field, entity, converterRegistry.parse(fieldTargetType, attributeValue));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        public @Nullable AttributeValue readToAttributeValue(T entity) {
            try {
                Object value = FieldUtils.readField(field, entity);
                AttributeType<?> attributeType = converterRegistry.getConverterForType(fieldTargetType).shortCircuitToDatabase(value, converterRegistry);
                return attributeType == null ? null : attributeType.attributeValue();
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
