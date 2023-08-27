package com.sylvona.leona.data.dynamodb.delegate.filters;

import com.sylvona.leona.data.dynamodb.delegate.results.DynamoItemResult;
import com.sylvona.leona.data.dynamodb.delegate.results.DynamoResult;
import com.sylvona.leona.data.dynamodb.delegate.DynamoResultType;

public interface PostDynamoDBReadFilter extends PostDynamoDBFilter {
    void readPostExecution(DynamoItemResult<?> itemResult);

    @Override
    default void postExecution(DynamoResult<?> result) {
        if (result instanceof DynamoItemResult<?> itemResult && itemResult.resultType() == DynamoResultType.GET) {
            readPostExecution(itemResult);
        }
    }
}
