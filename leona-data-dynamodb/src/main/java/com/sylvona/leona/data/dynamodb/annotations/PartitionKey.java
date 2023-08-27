package com.sylvona.leona.data.dynamodb.annotations;

import com.sylvona.leona.core.commons.annotations.Represents;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Represents(AnnotationAttribute.class)
public @interface PartitionKey {
    @AliasFor("name")
    @Represents.FieldMapping("attributeName")
    String value() default "";

    @AliasFor("value")
    String name() default "";

    Class<?> converter() default void.class;
}
