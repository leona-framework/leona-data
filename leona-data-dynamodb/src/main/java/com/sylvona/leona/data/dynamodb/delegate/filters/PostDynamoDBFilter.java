package com.sylvona.leona.data.dynamodb.delegate.filters;

import com.sylvona.leona.data.dynamodb.delegate.results.DynamoResult;

public interface PostDynamoDBFilter {
    void postExecution(DynamoResult<?> result);
}
