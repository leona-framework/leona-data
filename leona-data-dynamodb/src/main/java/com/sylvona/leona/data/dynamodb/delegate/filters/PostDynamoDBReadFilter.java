package com.sylvona.leona.data.dynamodb.delegate.filters;

import com.sylvona.leona.data.dynamodb.delegate.results.DynamoItemResult;
import com.sylvona.leona.data.dynamodb.delegate.results.DynamoResult;
import com.sylvona.leona.data.dynamodb.delegate.DynamoResultType;

/**
 * Interface for defining post-execution filters specifically for DynamoDB read operations.
 * Implementing classes can define logic to be applied after the execution of read operations on DynamoDB.
 */
public interface PostDynamoDBReadFilter extends PostDynamoDBFilter {

    /**
     * Method to be executed after a read operation on DynamoDB.
     * This method allows implementing classes to apply custom logic to the result of a read operation.
     *
     * @param itemResult The DynamoDB item result object representing the outcome of the read operation.
     */
    void readPostExecution(DynamoItemResult<?> itemResult);

    /**
     * Default implementation of the post-execution method for read operations.
     * This method is overridden to ensure that the custom logic is applied only to read operation results.
     *
     * @param result The DynamoDB result object representing the outcome of the operation.
     */
    @Override
    default void postExecution(DynamoResult<?> result) {
        if (result instanceof DynamoItemResult<?> itemResult && itemResult.resultType() == DynamoResultType.GET) {
            readPostExecution(itemResult);
        }
    }
}
