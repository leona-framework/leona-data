package com.sylvona.leona.data.dynamodb.delegate.filters;

import com.sylvona.leona.data.dynamodb.delegate.DynamoRequest;

/**
 * Interface for defining pre-execution filters for DynamoDB operations.
 * Implementing classes can define logic to be applied before the execution of DynamoDB operations.
 */
public interface PreDynamoDBFilter {

    /**
     * Method to be executed before a DynamoDB operation.
     * This method allows implementing classes to apply custom logic before the execution of the operation.
     *
     * @param dynamoRequest The DynamoDB request object representing the upcoming operation.
     */
    void preExecution(DynamoRequest dynamoRequest);
}

