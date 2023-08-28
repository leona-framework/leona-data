package com.sylvona.leona.data.dynamodb.delegate.filters;

import com.sylvona.leona.data.dynamodb.delegate.results.DynamoResult;

/**
 * Interface for defining post-execution filters for DynamoDB operations.
 * Implementing classes can define logic to be applied after the execution of DynamoDB operations.
 */
public interface PostDynamoDBFilter {

    /**
     * Method to be executed after a DynamoDB operation.
     * This method allows implementing classes to apply custom logic to the result of a DynamoDB operation.
     *
     * @param result The DynamoDB result object representing the outcome of the operation.
     */
    void postExecution(DynamoResult<?> result);
}
