package com.sylvona.leona.data.dynamodb.converter;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.sylvona.leona.data.dynamodb.AttributeType;

import java.lang.reflect.Type;

public interface ConverterContext {
    <T, A extends AttributeType<?>> DatabaseItemConverter<T, A> getConverterForType(Type type);
    <T> T parse(ConverterContext context, Type targetType, AttributeValue attributeValue);
    Type targetType();
}
