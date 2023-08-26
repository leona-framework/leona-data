package com.sylvona.leona.data.dynamodb.delegate;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.GetItemResult;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import com.sylvona.leona.core.commons.Functions;
import com.sylvona.leona.data.dynamodb.AttributeKeyValue;
import com.sylvona.leona.data.dynamodb.layout.EntityLayout;
import com.sylvona.leona.data.dynamodb.delegate.filters.PostDynamoDBFilter;
import com.sylvona.leona.data.dynamodb.delegate.filters.PreDynamoDBFilter;

import java.time.Duration;
import java.util.List;

public class DynamoRepositoryDelegate<ID, T> {
    private final AmazonDynamoDB amazonDynamoDB;
    private final EntityLayout<T> entityLayout;

    private final String tableName;
    private final String primaryKeyAttribute;

    private final List<PreDynamoDBFilter> preExecutionFilters;
    private final List<PostDynamoDBFilter> postExecutionFilters;

    public DynamoRepositoryDelegate(AmazonDynamoDB amazonDynamoDB, EntityLayout<T> entityLayout, List<PreDynamoDBFilter> preExecutionFilters, List<PostDynamoDBFilter> postExecutionFilters) {
        this.amazonDynamoDB = amazonDynamoDB;
        this.entityLayout = entityLayout;

        tableName = entityLayout.getEntityName();
        primaryKeyAttribute = entityLayout.getPrimaryKeyName();
        this.preExecutionFilters = preExecutionFilters;
        this.postExecutionFilters = postExecutionFilters;
    }

    public DynamoResult<T> get(ID primaryKeyValue) {
        GetItemRequest2 getItemRequest2 = new GetItemRequest2().withTableName(tableName).withKey(new AttributeKeyValue(primaryKeyAttribute, entityLayout.getPrimaryKeyValue(primaryKeyValue)));
        long startTime = doRequestFilters(getItemRequest2);

        GetItemResult getItemResult = amazonDynamoDB.getItem(getItemRequest2);
        Duration executionTime = Duration.ofNanos(System.nanoTime() - startTime);

        DynamoItemResult<T> dynamoResult = DynamoItemResult.getResult(executionTime, Functions.caching(entityLayout::createEntity), getItemResult);
        doResponseFilters(dynamoResult);

        return dynamoResult;
    }

    public DynamoResult<T> put(T entity) {
        PutItemRequest2 putItemRequest2 = new PutItemRequest2(primaryKeyAttribute).withTableName(tableName).withItem(entityLayout.getAttributes(entity));
        long startTime = doRequestFilters(putItemRequest2);

        PutItemResult putItemResult = amazonDynamoDB.putItem(putItemRequest2);
        Duration executionTime = Duration.ofNanos(System.nanoTime() - startTime);

        DynamoItemResult<T> dynamoResult = DynamoItemResult.putResult(executionTime, Functions.caching(entityLayout::createEntity), putItemResult);
        doResponseFilters(dynamoResult);

        return dynamoResult;
    }


    private long doRequestFilters(DynamoRequest request) {
        for (PreDynamoDBFilter preDynamoDBFilter : preExecutionFilters) {
            preDynamoDBFilter.preExecution(request);
        }
        return System.nanoTime();
    }

    private void doResponseFilters(DynamoResult<T> result) {
        for (PostDynamoDBFilter postDynamoDBFilter : postExecutionFilters) {
            postDynamoDBFilter.postExecution(result);
        }
    }
}
