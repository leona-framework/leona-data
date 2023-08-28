package com.sylvona.leona.data.dynamodb.annotations;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.sylvona.leona.core.commons.annotations.Represents;
import com.sylvona.leona.data.dynamodb.converter.DatabaseItemConverter;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a field in a {@link DynamoEntity} as the primary / partition key for its table. Every {@code DynamoEntity} must have a singular
 * {@code PartitionKey} attribute. {@link DynamoDBHashKey} is also supported but is not preferred.
 * <p>
 * This annotation is a specialized version of the {@link Attribute} annotation.
 *
 * @see RangeKey
 * @see Attribute
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Represents(AnnotationAttribute.class)
public @interface PartitionKey {
    /**
     * The name of the DynamoDB attribute, or the name of the field by default.
     * @return if provided the name of the DynamoDB attribute, otherwise the name of the field.
     */
    @AliasFor("name")
    @Represents.FieldMapping("attributeName")
    String value() default "";

    /**
     * The name of the DynamoDB attribute, or the name of the field by default.
     * @return if provided the name of the DynamoDB attribute, otherwise the name of the field.
     */
    @AliasFor("value")
    String name() default "";

    /**
     * A custom converter class to be used for conversion of this field. If not provided, uses default converters, otherwise
     * overrides any pre-existing converters for given field's type.
     * @return if provided, a custom converter ot be used for this field, otherwise the default converters will be used.
     */
    Class<? extends DatabaseItemConverter> converter() default DatabaseItemConverter.class;
}
