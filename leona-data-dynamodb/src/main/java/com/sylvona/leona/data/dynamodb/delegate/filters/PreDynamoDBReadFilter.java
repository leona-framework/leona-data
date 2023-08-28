package com.sylvona.leona.data.dynamodb.delegate.filters;

import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.sylvona.leona.data.dynamodb.delegate.DynamoRequest;

/**
 * Functional interface for defining pre-execution filters specifically for DynamoDB read operations.
 * Implementing classes can define logic to be applied before the execution of read operations.
 */
@FunctionalInterface
public interface PreDynamoDBReadFilter extends PreDynamoDBFilter {

    /**
     * Method to be executed before a DynamoDB read operation.
     * This method allows implementing classes to apply custom logic before the execution of read operations.
     *
     * @param itemRequest The DynamoDB read request object representing the upcoming operation.
     */
    void preReadExecution(GetItemRequest itemRequest);

    /**
     * Default implementation of the pre-execution method, filtering based on the operation type.
     *
     * @param dynamoRequest The DynamoDB request object representing the upcoming operation.
     */
    @Override
    default void preExecution(DynamoRequest dynamoRequest) {
        if (dynamoRequest instanceof GetItemRequest getItemRequest) {
            preReadExecution(getItemRequest);
        }
    }
}
