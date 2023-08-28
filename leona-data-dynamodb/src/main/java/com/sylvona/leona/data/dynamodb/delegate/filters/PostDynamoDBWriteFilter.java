package com.sylvona.leona.data.dynamodb.delegate.filters;

import com.sylvona.leona.data.dynamodb.delegate.results.DynamoItemResult;
import com.sylvona.leona.data.dynamodb.delegate.results.DynamoResult;
import com.sylvona.leona.data.dynamodb.delegate.DynamoResultType;

/**
 * Interface for defining post-execution filters specifically for DynamoDB write operations.
 * Implementing classes can define logic to be applied after the execution of write operations on DynamoDB.
 */
public interface PostDynamoDBWriteFilter extends PostDynamoDBFilter {

    /**
     * Method to be executed after a write operation on DynamoDB.
     * This method allows implementing classes to apply custom logic to the result of a write operation.
     *
     * @param itemResult The DynamoDB item result object representing the outcome of the write operation.
     */
    void writePostExecution(DynamoItemResult<?> itemResult);

    /**
     * Default implementation of the post-execution method for write operations.
     * This method is overridden to ensure that the custom logic is applied only to write operation results.
     *
     * @param result The DynamoDB result object representing the outcome of the operation.
     */
    @Override
    default void postExecution(DynamoResult<?> result) {
        if (result instanceof DynamoItemResult<?> itemResult && itemResult.resultType() == DynamoResultType.PUT) {
            writePostExecution(itemResult);
        }
    }
}

