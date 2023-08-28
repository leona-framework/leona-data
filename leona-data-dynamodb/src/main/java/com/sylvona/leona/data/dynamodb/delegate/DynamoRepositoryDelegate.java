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

/**
 * A delegate class that provides convenient methods to interact with Amazon DynamoDB for performing CRUD operations on entities.
 * This class encapsulates common functionality related to retrieving, putting, and scanning DynamoDB entities.
 *
 * @param <ID> The type of the primary key (hash key) of the entity.
 * @param <T>  The type of entity being managed.
 */
public class DynamoRepositoryDelegate<ID, T> {
    private final AmazonDynamoDB amazonDynamoDB;
    private final EntityLayout<T> entityLayout;

    private final String tableName;
    private final String primaryKeyAttribute;

    private final List<PreDynamoDBFilter> preExecutionFilters;
    private final List<PostDynamoDBFilter> postExecutionFilters;

    /**
     * Creates a new instance of {@code DynamoRepositoryDelegate} with the provided parameters.
     *
     * @param amazonDynamoDB      The Amazon DynamoDB client used for interactions.
     * @param entityLayout        The layout of the entity that specifies attributes and keys.
     * @param preExecutionFilters A list of pre-execution filters to apply before DynamoDB operations.
     * @param postExecutionFilters A list of post-execution filters to apply after DynamoDB operations.
     */
    public DynamoRepositoryDelegate(AmazonDynamoDB amazonDynamoDB, EntityLayout<T> entityLayout, List<PreDynamoDBFilter> preExecutionFilters, List<PostDynamoDBFilter> postExecutionFilters) {
        this.amazonDynamoDB = amazonDynamoDB;
        this.entityLayout = entityLayout;

        tableName = entityLayout.getEntityName();
        primaryKeyAttribute = entityLayout.getPrimaryKeyName();
        this.preExecutionFilters = preExecutionFilters;
        this.postExecutionFilters = postExecutionFilters;
    }

    /**
     * Retrieves an entity from the DynamoDB table using its primary key (hash key).
     *
     * @param primaryKeyValue The primary key (hash key) of the entity to retrieve.
     * @return A {@link DynamoResult} representing the retrieval operation.
     */
    public DynamoResult<T> get(ID primaryKeyValue) {
        return get(primaryKeyValue, null);
    }

    /**
     * Retrieves an entity by its primary key (hash key) and optionally range key.
     *
     * @param primaryKeyValue The primary key (hash key) of the entity.
     * @param rangeKey        The range key of the entity (if applicable).
     * @param <ID2> The type of the range key.
     * @return A {@link DynamoResult} representing the retrieval operation.
     */
    public <ID2> DynamoResult<T> get(ID primaryKeyValue, @Nullable ID2 rangeKey) {
        GetItemRequest2 getItemRequest2 = new GetItemRequest2().withTableName(tableName).withKey(entityLayout.getSignificantKeys(primaryKeyValue, rangeKey));
        long startTime = doRequestFilters(getItemRequest2);

        GetItemResult getItemResult = amazonDynamoDB.getItem(getItemRequest2);
        Duration executionTime = Duration.ofNanos(System.nanoTime() - startTime);

        DynamoItemResult<T> dynamoResult = DynamoItemResult.getResult(executionTime, Functions.caching(entityLayout::createEntity), getItemResult);
        doResponseFilters(dynamoResult);

        return dynamoResult;
    }

    /**
     * Retrieves all entities from the DynamoDB table.
     *
     * @return A {@link DynamoResult} representing the scan operation.
     */
    public DynamoResult<List<T>> getAll() {
        return doScanRequest(new ScanRequest2().withTableName(tableName));
    }

    /**
     * Retrieves multiple entities by their primary keys and optionally range keys.
     *
     * @param ids The list of primary keys for which entities need to be retrieved.
     * @return A {@link DynamoResult} representing the batch get operation.
     */
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

    /**
     * Retrieves multiple entities using a map of primary keys and their corresponding range keys.
     *
     * @param ids The map of primary keys and range keys for which entities need to be retrieved.
     * @param <ID2> The type of the range key.
     * @return A {@link DynamoResult} representing the batch get operation.
     */
    public <ID2> DynamoResult<List<T>> getAllById(Map<ID, Iterable<ID2>> ids) {
        return doScanRequest(new ScanAllByIdRequest<>(tableName, entityLayout, ids));
    }

    /**
     * Puts an entity into the DynamoDB table.
     *
     * @param entity The entity to be put.
     * @return A {@link DynamoResult} representing the put operation.
     */
    public DynamoResult<T> put(T entity) {
        PutItemRequest2 putItemRequest2 = new PutItemRequest2(primaryKeyAttribute).withTableName(tableName).withItem(entityLayout.getAttributes(entity));
        long startTime = doRequestFilters(putItemRequest2);

        PutItemResult putItemResult = amazonDynamoDB.putItem(putItemRequest2);
        Duration executionTime = Duration.ofNanos(System.nanoTime() - startTime);

        DynamoItemResult<T> dynamoResult = DynamoItemResult.putResult(executionTime, Functions.caching(entityLayout::createEntity), putItemResult);
        doResponseFilters(dynamoResult);

        return dynamoResult;
    }

    /**
     * Puts multiple entities into the DynamoDB table.
     *
     * @param entities The list of entities to be put.
     * @return A {@link DynamoResult} representing the batch write operation.
     */
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
