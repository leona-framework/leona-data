package com.sylvona.leona.data.dynamodb.delegate;

/**
 * An interface representing a DynamoDB request with the ability to retrieve the associated table name.
 */
public interface DynamoRequest {
    /**
     * Retrieves the name of the table associated with this DynamoDB request.
     *
     * @return The name of the DynamoDB table.
     */
    String getTableName();
}

