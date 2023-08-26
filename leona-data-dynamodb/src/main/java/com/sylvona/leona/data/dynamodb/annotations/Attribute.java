package com.sylvona.leona.data.dynamodb.annotations;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDB;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@DynamoDB
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Attribute {

    @AliasFor("name")
    String value() default "";

    @AliasFor("value")
    String name() default "";
}
