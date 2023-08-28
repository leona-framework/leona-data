package com.sylvona.leona.data.dynamodb.annotations;

import com.sylvona.leona.data.dynamodb.converter.DatabaseItemConverter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a class as a DynamoDB entity, defining it as a member of a specific DynamoDB table. DynamoDB entities must declare
 * a singular field with as a {@link PartitionKey}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DynamoEntity {
    /**
     * The name of the Dynamo DB table.
     * @return the name of the table.
     */
    String value();

    /**
     * Defines all custom converters to use for this entity. Converters defined here will override any defaults of the same
     * target-type for this entity.
     * @return the custom converters to be used by this entity.
     */
    Class<? extends DatabaseItemConverter<?, ?>>[] converters() default {};
}
