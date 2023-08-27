package com.sylvona.leona.data.dynamodb.delegate;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.*;
import com.sylvona.leona.core.commons.Functions;
import com.sylvona.leona.data.dynamodb.delegate.filters.PostDynamoDBFilter;
import com.sylvona.leona.data.dynamodb.delegate.filters.PreDynamoDBFilter;
import com.sylvona.leona.data.dynamodb.delegate.results.DynamoGetAllResult;
import com.sylvona.leona.data.dynamodb.delegate.results.DynamoItemResult;
import com.sylvona.leona.data.dynamodb.delegate.results.DynamoPutAllResult;
import com.sylvona.leona.data.dynamodb.delegate.results.DynamoResult;
import com.sylvona.leona.data.dynamodb.entity.EntityLayout;
import jakarta.annotation.Nullable;

import java.time.Duration;
import java.util.List;
import java.util.Map;

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
        return get(primaryKeyValue, null);
    }

    public <ID2> DynamoResult<T> get(ID primaryKeyValue, @Nullable ID2 rangeKey) {
        GetItemRequest2 getItemRequest2 = new GetItemRequest2().withTableName(tableName).withKey(entityLayout.getSignificantKeys(primaryKeyValue, rangeKey));
        long startTime = doRequestFilters(getItemRequest2);

        GetItemResult getItemResult = amazonDynamoDB.getItem(getItemRequest2);
        Duration executionTime = Duration.ofNanos(System.nanoTime() - startTime);

        DynamoItemResult<T> dynamoResult = DynamoItemResult.getResult(executionTime, Functions.caching(entityLayout::createEntity), getItemResult);
        doResponseFilters(dynamoResult);

        return dynamoResult;
    }

    public DynamoResult<List<T>> getAll() {
        return doScanRequest(new ScanRequest2().withTableName(tableName));
    }

    public DynamoResult<List<T>> getAllById(Iterable<ID> ids) {
        if (entityLayout.getRangeKeyName() != null) {
            return doScanRequest(new ScanAllByIdRequest<>(tableName, entityLayout, ids));
        }

        GetByIdRequest<ID> request = new GetByIdRequest<>(tableName, entityLayout, ids);
        long startTime = doRequestFilters(request);

        BatchGetItemResult batchGetItemResult = amazonDynamoDB.batchGetItem(request);
        Duration executionTime = Duration.ofNanos(System.nanoTime() - startTime);

        DynamoGetAllResult<T> getAllResult = new DynamoGetAllResult<>(batchGetItemResult, executionTime, entityLayout::createEntity);
        doResponseFilters(getAllResult);

        return getAllResult;
    }

    public <ID2> DynamoResult<List<T>> getAllById(Map<ID, Iterable<ID2>> ids) {
        return doScanRequest(new ScanAllByIdRequest<>(tableName, entityLayout, ids));
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

    public DynamoResult<List<T>> putAll(Iterable<T> entities) {
        PutAllRequest<T> putAllRequest = new PutAllRequest<>(tableName, entityLayout, entities);
        long startTime = doRequestFilters(putAllRequest);

        BatchWriteItemResult batchWriteItemResult = amazonDynamoDB.batchWriteItem(putAllRequest);
        Duration executionTime = Duration.ofNanos(System.nanoTime() - startTime);

        DynamoPutAllResult<T> dynamoResult = new DynamoPutAllResult<>(batchWriteItemResult, executionTime);
        doResponseFilters(dynamoResult);

        return dynamoResult;
    }

    private DynamoResult<List<T>> doScanRequest(ScanRequest2 scanRequest) {
        long startTime = doRequestFilters(scanRequest);

        ScanResult result = amazonDynamoDB.scan(scanRequest);
        Duration executionTime = Duration.ofNanos(System.nanoTime() - startTime);

        DynamoGetAllResult<T> getAllResult = new DynamoGetAllResult<>(result, executionTime, entityLayout::createEntity);
        doResponseFilters(getAllResult);

        return getAllResult;
    }

    private long doRequestFilters(DynamoRequest request) {
        for (PreDynamoDBFilter preDynamoDBFilter : preExecutionFilters) {
            preDynamoDBFilter.preExecution(request);
        }
        return System.nanoTime();
    }

    private void doResponseFilters(DynamoResult<?> result) {
        for (PostDynamoDBFilter postDynamoDBFilter : postExecutionFilters) {
            postDynamoDBFilter.postExecution(result);
        }
    }
}
