package com.sylvona.leona.data.dynamodb.annotations;

import com.sylvona.leona.data.dynamodb.converter.DatabaseItemConverter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DynamoEntity {
    String value();

    /**
     * Defines all custom converters to use for this entity. Converters defined here will override any defaults of the same
     * target-type for this entity.
     */
    Class<? extends DatabaseItemConverter<?, ?>>[] converters() default {};
}
