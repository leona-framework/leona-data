package com.sylvona.leona.data.dynamodb.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.sylvona.leona.core.commons.annotations.Representer;
import com.sylvona.leona.data.dynamodb.annotations.AnnotationAttribute;
import com.sylvona.leona.data.dynamodb.annotations.DynamoEntity;
import com.sylvona.leona.data.dynamodb.annotations.PartitionKey;
import com.sylvona.leona.data.dynamodb.annotations.RangeKey;
import com.sylvona.leona.data.dynamodb.converter.ConverterRegistry;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.function.BiConsumer;

public class EntityAnnotationExtractor<T> {
    private final ConverterRegistry converterRegistry;
    private final BiConsumer<AnnotationAttribute, Field> resultConsumer;
    private final Class<T> entityClass;
    @Getter private final DynamoEntity dynamoEntity;
    @Getter private String primaryKeyName;
    @Getter private EntityField<T> primaryKeyField;
    @Getter private String rangeKeyName;
    @Getter private EntityField<T> rangeKeyField;

    public EntityAnnotationExtractor(ConverterRegistry converterRegistry, BiConsumer<AnnotationAttribute, Field> resultConsumer, Class<T> entityClass) {
        this.converterRegistry = converterRegistry;
        this.resultConsumer = resultConsumer;
        this.entityClass = entityClass;
        dynamoEntity = entityClass.getAnnotation(DynamoEntity.class);
        if (dynamoEntity == null) {
            throw new EntityLayoutException("DynamoDB entity must be annotated with @DynamoEntity. (Class=%s)".formatted(entityClass));
        }
    }


    public boolean extract(Field field, Class<? extends Annotation> annotationClass) {
        Annotation annotation = field.getAnnotation(annotationClass);
        if (annotation == null) return false;
        AnnotationAttribute annotationAttribute = Representer.get(annotation, AnnotationAttribute.class);

        if (annotationClass.equals(DynamoDBHashKey.class)) setPrimaryKey(annotationAttribute, field);
        if (annotationClass.equals(PartitionKey.class)) setPrimaryKey(annotationAttribute, field);
        if (annotationClass.equals(DynamoDBRangeKey.class)) setRangeKey(annotationAttribute, field);
        if (annotationClass.equals(RangeKey.class)) setRangeKey(annotationAttribute, field);

        resultConsumer.accept(annotationAttribute, field);
        return true;
    }

    private void setPrimaryKey(AnnotationAttribute annotationAttribute, Field field) {
        if (primaryKeyField != null) {
            throw new EntityLayoutException("DynamoDB entity cannot have multiple primary keys. (Class=%s)".formatted(entityClass));
        }
        primaryKeyName = getAttributeName(annotationAttribute, field);
        primaryKeyField = new EntityField<>(field, converterRegistry, null);
    }

    private void setRangeKey(AnnotationAttribute annotationAttribute, Field field) {
        if (rangeKeyField != null) {
            throw new EntityLayoutException("DynamoDB entity cannot have multiple range keys. (Class=%s)".formatted(entityClass));
        }
        rangeKeyName = getAttributeName(annotationAttribute, field);
        rangeKeyField = new EntityField<>(field, converterRegistry, null);
    }

    private String getAttributeName(AnnotationAttribute annotationAttribute, Field field) {
        String attributeName = annotationAttribute.getAttributeName();
        if (!StringUtils.isBlank(attributeName)) return attributeName;
        return field.getName();
    }

}
