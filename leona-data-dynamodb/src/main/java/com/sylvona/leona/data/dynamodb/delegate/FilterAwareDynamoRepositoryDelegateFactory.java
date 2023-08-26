package com.sylvona.leona.data.dynamodb.delegate;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.sylvona.leona.data.dynamodb.delegate.filters.PostDynamoDBFilter;
import com.sylvona.leona.data.dynamodb.delegate.filters.PreDynamoDBFilter;
import com.sylvona.leona.data.dynamodb.repository.DynamoRepositoryEntityInformation;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class FilterAwareDynamoRepositoryDelegateFactory implements DynamoRepositoryDelegateFactory {
    private final List<PreDynamoDBFilter> preExecutionFilters;
    private final List<PostDynamoDBFilter> postExecutionFilters;

    @Override
    public <ID, T> DynamoRepositoryDelegate<ID, T> getRepositoryDelegate(AmazonDynamoDB dynamoDB, DynamoRepositoryEntityInformation<T, ?> entityInformation) {
        return new DynamoRepositoryDelegate<>(dynamoDB, entityInformation.getLayout(), preExecutionFilters, postExecutionFilters);
    }
}
