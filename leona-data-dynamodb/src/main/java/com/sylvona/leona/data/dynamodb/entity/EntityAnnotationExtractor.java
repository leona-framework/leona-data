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

/**
 * A utility class for extracting annotations and attributes from fields of an entity class.
 * This class is used to extract metadata related to annotations and attributes for DynamoDB entity fields.
 */
public class EntityAnnotationExtractor<T> {
    private final ConverterRegistry converterRegistry;
    private final BiConsumer<AnnotationAttribute, Field> resultConsumer;
    private final Class<T> entityClass;
    /**
     * The DynamoDB entity annotation associated with the entity class.
     */
    @Getter private final DynamoEntity dynamoEntity;
    /**
     * The primary key name for the DynamoDB entity.
     */
    @Getter private String primaryKeyName;
    /**
     * The entity field representing the primary key.
     */
    @Getter private EntityField<T> primaryKeyField;
    /**
     * The range key name for the DynamoDB entity.
     */
    @Getter private String rangeKeyName;
    /**
     * The entity field representing the range key.
     */
    @Getter private EntityField<T> rangeKeyField;

    /**
     * Creates an instance of {@code EntityAnnotationExtractor}.
     *
     * @param converterRegistry The converter registry for handling attribute conversions.
     * @param resultConsumer    The consumer for annotation attributes and fields.
     * @param entityClass       The class of the entity.
     * @throws EntityLayoutException If the entity class is not annotated with {@link DynamoEntity}.
     */
    public EntityAnnotationExtractor(ConverterRegistry converterRegistry, BiConsumer<AnnotationAttribute, Field> resultConsumer, Class<T> entityClass) {
        this.converterRegistry = converterRegistry;
        this.resultConsumer = resultConsumer;
        this.entityClass = entityClass;
        dynamoEntity = entityClass.getAnnotation(DynamoEntity.class);
        if (dynamoEntity == null) {
            throw new EntityLayoutException("DynamoDB entity must be annotated with @DynamoEntity. (Class=%s)".formatted(entityClass));
        }
    }


    /**
     * Extracts metadata from a field based on the specified annotation class.
     *
     * @param field           The field to extract metadata from.
     * @param annotationClass The class of the annotation to extract.
     * @return {@code true} if metadata was extracted, {@code false} otherwise.
     */
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
