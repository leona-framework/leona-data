package com.sylvona.leona.data.dynamodb.annotations;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.sylvona.leona.core.commons.annotations.Represents;
import lombok.Getter;

import java.lang.annotation.Annotation;

@Getter
@Represents({DynamoDBHashKey.class, DynamoDBRangeKey.class})
public class AnnotationAttribute {
    private String attributeName;
    private Class<?> converter;
    @Represents.SourceTargetField
    private Annotation annotation;
}