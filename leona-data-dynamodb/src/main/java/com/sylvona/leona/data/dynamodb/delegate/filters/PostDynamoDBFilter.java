package com.sylvona.leona.data.dynamodb.delegate.filters;

import com.sylvona.leona.data.dynamodb.delegate.DynamoResult;

public interface PostDynamoDBFilter {
    void postExecution(DynamoResult<?> result);
}
