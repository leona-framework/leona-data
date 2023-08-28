package com.sylvona.leona.data.dynamodb.delegate;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.sylvona.leona.data.dynamodb.repository.DynamoRepositoryEntityInformation;

/**
 * A factory interface for creating instances of {@link DynamoRepositoryDelegate}.
 */
public interface DynamoRepositoryDelegateFactory {
    /**
     * Creates a new instance of {@link DynamoRepositoryDelegate} for the specified entity type and DynamoDB client.
     *
     * @param dynamoDB The Amazon DynamoDB client used to interact with the DynamoDB database.
     * @param entityInformation Information about the entity type associated with the repository.
     * @param <ID> The type of the entity's primary key.
     * @param <T> The type of the entity.
     * @return A new {@link DynamoRepositoryDelegate} instance for the specified entity type and DynamoDB client.
     */
    <ID, T> DynamoRepositoryDelegate<ID, T> getRepositoryDelegate(AmazonDynamoDB dynamoDB, DynamoRepositoryEntityInformation<T, ?> entityInformation);
}
