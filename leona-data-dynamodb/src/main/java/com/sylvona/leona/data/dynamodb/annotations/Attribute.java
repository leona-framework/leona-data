package com.sylvona.leona.data.dynamodb.annotations;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDB;
import com.sylvona.leona.core.commons.annotations.Represents;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@DynamoDB
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Represents(AnnotationAttribute.class)
public @interface Attribute {
    @AliasFor("name")
    @Represents.FieldMapping("attributeName")
    String value() default "";

    @AliasFor("value")
    String name() default "";

    Class<?> converter() default void.class;
}
