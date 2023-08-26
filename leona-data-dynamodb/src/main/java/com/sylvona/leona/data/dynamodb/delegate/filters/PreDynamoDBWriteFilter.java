package com.sylvona.leona.data.dynamodb.delegate.filters;

import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.sylvona.leona.data.dynamodb.delegate.DynamoRequest;

@FunctionalInterface
public interface PreDynamoDBWriteFilter extends PreDynamoDBFilter {
    void preWriteExecution(PutItemRequest putItemRequest);

    @Override
    default void preExecution(DynamoRequest dynamoRequest) {
        if (dynamoRequest instanceof PutItemRequest putItemRequest) preWriteExecution(putItemRequest);
    }
}
