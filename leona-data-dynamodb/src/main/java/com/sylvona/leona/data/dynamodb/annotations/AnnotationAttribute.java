package com.sylvona.leona.data.dynamodb.annotations;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.sylvona.leona.core.commons.annotations.Represents;
import com.sylvona.leona.data.dynamodb.converter.DatabaseItemConverter;
import lombok.Getter;

import java.lang.annotation.Annotation;

/**
 * A class that represents variations of the {@link Attribute} annotation used with DynamoDB
 */
@Getter
@Represents({DynamoDBHashKey.class, DynamoDBRangeKey.class})
public class AnnotationAttribute {
    /**
     * The name of the attribute in Dynamo DB
     */
    private String attributeName;
    /**
     * A custom converter to be used for parsing to/from DynamoDB
     */
    private Class<? extends DatabaseItemConverter> converter;

    /**
     * The annotation that correlates to this representation class
     */
    @Represents.SourceTargetField
    private Annotation annotation;
}