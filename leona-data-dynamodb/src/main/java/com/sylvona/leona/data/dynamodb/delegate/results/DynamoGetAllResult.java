package com.sylvona.leona.data.dynamodb.delegate.results;

import com.amazonaws.AmazonWebServiceResult;
import com.amazonaws.ResponseMetadata;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.BatchGetItemResult;
import com.amazonaws.services.dynamodbv2.model.ConsumedCapacity;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.sylvona.leona.core.commons.streams.LINQ;
import com.sylvona.leona.data.dynamodb.delegate.DynamoResultType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Getter @Accessors(fluent = true)
public class DynamoGetAllResult<T> implements DynamoResult<List<T>> {
    @Getter(AccessLevel.NONE)
    private final Function<Map<String, AttributeValue>, T> resultResolver;
    private final Duration executionTime;
    private final DynamoResultType resultType;
    private final AmazonWebServiceResult<ResponseMetadata> awsResult;
    private final ConsumedCapacity consumedCapacity;
    private final List<Map<String, AttributeValue>> attributes;
    private final Throwable error;

    @Getter(AccessLevel.NONE)
    private List<T> result;

    public DynamoGetAllResult(ScanResult scanResult, Duration executionTime, Function<Map<String, AttributeValue>, T> resultResolver) {
        this.awsResult = scanResult;
        this.resultType = DynamoResultType.GET;
        this.executionTime = executionTime;
        this.resultResolver = resultResolver;
        this.consumedCapacity = scanResult.getConsumedCapacity();
        this.attributes = scanResult.getItems();
        this.error = null;
    }

    public DynamoGetAllResult(BatchGetItemResult batchGetItemResult, Duration executionTime, Function<Map<String, AttributeValue>, T> resultResolver) {
        this.awsResult = batchGetItemResult;
        this.resultType = DynamoResultType.GET;
        this.executionTime = executionTime;
        this.resultResolver = resultResolver;
        this.consumedCapacity = batchGetItemResult.getConsumedCapacity().get(0);
        this.attributes = LINQ.stream(batchGetItemResult.getResponses().values()).first();
        this.error = null;
    }

    public DynamoGetAllResult(Throwable error, Duration executionTime) {
        this.resultType = DynamoResultType.GET;
        this.executionTime = executionTime;
        this.error = error;
        this.awsResult = null;
        this.resultResolver = null;
        this.consumedCapacity = null;
        this.attributes = null;
    }

    @Override
    public List<T> result() {
        if (result != null) return result;
        return result = LINQ.stream(attributes).map(resultResolver).toList();
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
