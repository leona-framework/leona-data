package com.sylvona.leona.data.dynamodb.layout;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import java.util.Map;

public interface EntityLayout<T> {
    T createEntity(Map<String, AttributeValue> attributes);

    Map<String, AttributeValue> getAttributes(T entity);

    Map<String, AttributeValue> getPrimaryKey(T entity);
    <ID> AttributeValue getPrimaryKeyValue(ID id);

    String getEntityName();

    String getPrimaryKeyName();
}
