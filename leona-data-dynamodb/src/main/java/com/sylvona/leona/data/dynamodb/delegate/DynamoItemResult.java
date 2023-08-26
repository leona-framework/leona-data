package com.sylvona.leona.data.dynamodb.delegate;

import com.amazonaws.AmazonWebServiceResult;
import com.amazonaws.ResponseMetadata;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ConsumedCapacity;
import com.amazonaws.services.dynamodbv2.model.GetItemResult;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.time.Duration;
import java.util.Map;
import java.util.function.Function;

@Getter
@Accessors(fluent = true)
public class DynamoItemResult<T> implements DynamoResult<T> {
    private final Duration executionTime;
    private final Function<Map<String, AttributeValue>, T> resultResolver;
    private final DynamoResultType resultType;
    private final AmazonWebServiceResult<ResponseMetadata> awsResult;
    private final ConsumedCapacity consumedCapacity;
    private final Map<String, AttributeValue> attributes;
    private final Throwable error;

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

    public static <T> DynamoItemResult<T> getResult(Duration executionTime, Function<Map<String, AttributeValue>, T> resultResolver, GetItemResult itemResult) {
        return new DynamoItemResult<>(DynamoResultType.GET, executionTime, resultResolver, itemResult, itemResult.getConsumedCapacity(), itemResult.getItem());
    }

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

    @Override
    public Map<String, AttributeValue> getAttributes() {
        return attributes;
    }
}
