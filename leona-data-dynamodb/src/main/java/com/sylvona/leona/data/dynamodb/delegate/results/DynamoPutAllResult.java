package com.sylvona.leona.data.dynamodb.delegate.results;

import com.amazonaws.AmazonWebServiceResult;
import com.amazonaws.ResponseMetadata;
import com.amazonaws.services.dynamodbv2.model.BatchWriteItemResult;
import com.amazonaws.services.dynamodbv2.model.ConsumedCapacity;
import com.sylvona.leona.data.dynamodb.delegate.DynamoResultType;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.time.Duration;
import java.util.List;

/**
 * A class representing the result of a batch write operation in DynamoDB.
 * This class implements the {@link DynamoResult} interface, providing details about the operation's result,
 * execution time, and consumed capacity, as well as AWS result metadata.
 *
 * @param <T> The type of items that were put as part of the batch write operation.
 */
@Getter @Accessors(fluent = true)
public class DynamoPutAllResult<T> implements DynamoResult<List<T>> {
    private final Duration executionTime;
    private final DynamoResultType resultType;
    private final AmazonWebServiceResult<ResponseMetadata> awsResult;
    private final ConsumedCapacity consumedCapacity;
    private final Throwable error;

    /**
     * Constructs a DynamoPutAllResult object for a successful batch write operation.
     *
     * @param batchWriteItemResult The result of a batch write operation.
     * @param executionTime        The duration representing the execution time.
     */
    public DynamoPutAllResult(BatchWriteItemResult batchWriteItemResult, Duration executionTime) {
        this.awsResult = batchWriteItemResult;
        this.resultType = DynamoResultType.GET;
        this.executionTime = executionTime;
        this.consumedCapacity = batchWriteItemResult.getConsumedCapacity().get(0);
        this.error = null;
    }

    @Override
    public List<T> result() {
        return null;
    }

    @Override
    public AmazonWebServiceResult<ResponseMetadata> getAwsResult() {
        return awsResult;
    }

    @Override
    public ConsumedCapacity getConsumedCapacity() {
        return consumedCapacity;
    }
}
