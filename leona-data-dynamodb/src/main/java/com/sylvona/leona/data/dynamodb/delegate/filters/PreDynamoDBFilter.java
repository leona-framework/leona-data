package com.sylvona.leona.data.dynamodb.delegate.filters;

import com.sylvona.leona.data.dynamodb.delegate.DynamoRequest;

public interface PreDynamoDBFilter {
    void preExecution(DynamoRequest dynamoRequest);
}
