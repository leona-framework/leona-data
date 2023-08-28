package com.sylvona.leona.data.dynamodb.delegate.results;

import com.amazonaws.AmazonWebServiceResult;
import com.amazonaws.ResponseMetadata;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ConsumedCapacity;
import com.amazonaws.services.dynamodbv2.model.GetItemResult;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import com.sylvona.leona.data.dynamodb.delegate.DynamoResultType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.time.Duration;
import java.util.Map;
import java.util.function.Function;

/**
 * A class representing the result of a DynamoDB item operation, such as a "get" or "put" operation.
 * This class implements the {@link DynamoResult} interface, providing details about the operation's result,
 * execution time, and consumed capacity, as well as AWS result metadata.
 *
 * @param <T> The type of the item that was retrieved or put.
 */
@Getter @Accessors(fluent = true)
public class DynamoItemResult<T> implements DynamoResult<T> {
    @Getter(AccessLevel.NONE)
    private final Function<Map<String, AttributeValue>, T> resultResolver;
    private final Duration executionTime;
    private final DynamoResultType resultType;
    private final AmazonWebServiceResult<ResponseMetadata> awsResult;
    private final ConsumedCapacity consumedCapacity;
    private final Map<String, AttributeValue> attributes;
    private final Throwable error;

    /**
     * Constructs a DynamoItemResult object for a successful item operation.
     *
     * @param resultType     The type of DynamoDB result (e.g., "get" or "put").
     * @param executionTime  The duration representing the execution time.
     * @param resultResolver A function to resolve the retrieved or put item from attribute map.
     * @param awsResult      The AWS result metadata.
     * @param consumedCapacity The capacity consumed by the operation.
     * @param attributes     The attribute map representing the retrieved or put item.
     */
    public DynamoItemResult(
            DynamoResultType resultType,
            Duration executionTime,
            Function<Map<String, AttributeValue>, T> resultResolver,
            AmazonWebServiceResult<ResponseMetadata> awsResult,
            ConsumedCapacity consumedCapacity,
            Map<String, AttributeValue> attributes
    ) {
        this.resultType = resultType;
        this.executionTime = executionTime;
        this.resultResolver = resultResolver;
        this.awsResult = awsResult;
        this.consumedCapacity = consumedCapacity;
        this.attributes = attributes;
        this.error = null;
    }

    /**
     * Constructs a DynamoItemResult object for a failed item operation.
     *
     * @param resultType    The type of DynamoDB result (e.g., "get" or "put").
     * @param executionTime The duration representing the execution time.
     * @param throwable     The error that occurred during the operation.
     */
    public DynamoItemResult(
            DynamoResultType resultType,
            Duration executionTime,
            Throwable throwable
    ) {
        this.resultType = resultType;
        this.executionTime = executionTime;
        this.error = throwable;
        this.resultResolver = null;
        this.awsResult = null;
        this.consumedCapacity = null;
        this.attributes = null;
    }

    /**
     * Creates a DynamoItemResult object for a "get" operation result.
     *
     * @param executionTime  The duration representing the execution time.
     * @param resultResolver A function to resolve the retrieved item from attribute map.
     * @param itemResult     The result of a "get" operation.
     */
    public static <T> DynamoItemResult<T> getResult(Duration executionTime, Function<Map<String, AttributeValue>, T> resultResolver, GetItemResult itemResult) {
        return new DynamoItemResult<>(DynamoResultType.GET, executionTime, resultResolver, itemResult, itemResult.getConsumedCapacity(), itemResult.getItem());
    }

    /**
     * Creates a DynamoItemResult object for a "put" operation result.
     *
     * @param executionTime  The duration representing the execution time.
     * @param resultResolver A function to resolve the put item from attribute map.
     * @param itemResult     The result of a "put" operation.
     */
    public static <T> DynamoItemResult<T> putResult(Duration executionTime, Function<Map<String, AttributeValue>, T> resultResolver, PutItemResult itemResult) {
        return new DynamoItemResult<>(DynamoResultType.GET, executionTime, resultResolver, itemResult, itemResult.getConsumedCapacity(), itemResult.getAttributes());
    }

    @Override
    public T result() {
        return resultResolver.apply(attributes);
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
