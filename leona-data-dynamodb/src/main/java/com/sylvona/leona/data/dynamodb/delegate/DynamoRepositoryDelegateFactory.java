package com.sylvona.leona.data.dynamodb.delegate;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.sylvona.leona.data.dynamodb.repository.DynamoRepositoryEntityInformation;

public interface DynamoRepositoryDelegateFactory {
    <ID, T> DynamoRepositoryDelegate<ID, T> getRepositoryDelegate(AmazonDynamoDB dynamoDB, DynamoRepositoryEntityInformation<T, ?> entityInformation);
}
