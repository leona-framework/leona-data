package com.sylvona.leona.data.dynamodb.delegate.filters;

import com.sylvona.leona.data.dynamodb.delegate.results.DynamoItemResult;
import com.sylvona.leona.data.dynamodb.delegate.results.DynamoResult;
import com.sylvona.leona.data.dynamodb.delegate.DynamoResultType;

public interface PostDynamoDBWriteFilter extends PostDynamoDBFilter {
    void writePostExecution(DynamoItemResult<?> itemResult);

    @Override
    default void postExecution(DynamoResult<?> result) {
        if (result instanceof DynamoItemResult<?> itemResult && itemResult.resultType() == DynamoResultType.PUT) {
            writePostExecution(itemResult);
        }
    }
}
