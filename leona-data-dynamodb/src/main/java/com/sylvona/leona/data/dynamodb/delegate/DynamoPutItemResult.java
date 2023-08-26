package com.sylvona.leona.data.dynamodb.delegate;

import com.amazonaws.AmazonWebServiceResult;
import com.amazonaws.ResponseMetadata;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ConsumedCapacity;
import com.amazonaws.services.dynamodbv2.model.ItemCollectionMetrics;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class DynamoPutItemResult implements GenericItemResult {
    private final PutItemResult putItemResult;

    @Override
    public AmazonWebServiceResult<ResponseMetadata> getAwsResult() {
        return putItemResult;
    }

    @Override
    public ConsumedCapacity getConsumedCapacity() {
        return putItemResult.getConsumedCapacity();
    }

    @Override
    public Map<String, AttributeValue> getAttributes() {
        return putItemResult.getAttributes();
    }

    public ItemCollectionMetrics getItemCollectionMetrics() {
        return putItemResult.getItemCollectionMetrics();
    }
}
