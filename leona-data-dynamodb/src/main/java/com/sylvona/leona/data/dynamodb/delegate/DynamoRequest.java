package com.sylvona.leona.data.dynamodb.delegate;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;

public interface DynamoRequest {
    String getTableName();
    String getPrimaryKeyName();
    AttributeValue getPrimaryKeyValue();
}
