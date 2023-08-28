package com.sylvona.leona.data.dynamodb.delegate;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.sylvona.leona.data.dynamodb.delegate.filters.PostDynamoDBFilter;
import com.sylvona.leona.data.dynamodb.delegate.filters.PreDynamoDBFilter;
import com.sylvona.leona.data.dynamodb.repository.DynamoRepositoryEntityInformation;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * A factory class for creating instances of {@link DynamoRepositoryDelegate} with support for filters.
 */
@RequiredArgsConstructor
public class FilterAwareDynamoRepositoryDelegateFactory implements DynamoRepositoryDelegateFactory {
    private final List<PreDynamoDBFilter> preExecutionFilters;
    private final List<PostDynamoDBFilter> postExecutionFilters;

    /**
     * Creates a new {@link DynamoRepositoryDelegate} instance with the provided filters.
     *
     * @param dynamoDB The AmazonDynamoDB client.
     * @param entityInformation The entity information for the repository.
     * @param <ID> The type of the entity's primary key.
     * @param <T> The type of the entity.
     * @return A new {@link DynamoRepositoryDelegate} instance with support for filters.
     */
    @Override
    public <ID, T> DynamoRepositoryDelegate<ID, T> getRepositoryDelegate(AmazonDynamoDB dynamoDB, DynamoRepositoryEntityInformation<T, ?> entityInformation) {
        return new DynamoRepositoryDelegate<>(dynamoDB, entityInformation.getLayout(), preExecutionFilters, postExecutionFilters);
    }
}
