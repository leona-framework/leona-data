package com.sylvona.leona.data.dynamodb.entity;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import jakarta.annotation.Nullable;

import java.util.Map;

public interface EntityLayout<T> {
    T createEntity(Map<String, AttributeValue> attributes);

    Map<String, AttributeValue> getAttributes(T entity);
    String getEntityName();

    default <ID> Map<String, AttributeValue> getSignificantKeys(ID id) {
        return getSignificantKeys(id, null);
    }
    <ID1, ID2> Map<String, AttributeValue> getSignificantKeys(ID1 id1, @Nullable ID2 id2);

    String getPrimaryKeyName();

    @Nullable String getRangeKeyName();
}
