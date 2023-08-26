package com.sylvona.leona.data.dynamodb.delegate.filters;

import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.sylvona.leona.data.dynamodb.delegate.DynamoRequest;

@FunctionalInterface
public interface PreDynamoDBReadFilter extends PreDynamoDBFilter {
    void preReadExecution(GetItemRequest itemRequest);

    @Override
    default void preExecution(DynamoRequest dynamoRequest) {
        if (dynamoRequest instanceof GetItemRequest getItemRequest) preReadExecution(getItemRequest);
    }
}
