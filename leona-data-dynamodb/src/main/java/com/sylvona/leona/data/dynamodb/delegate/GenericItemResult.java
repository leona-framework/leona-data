package com.sylvona.leona.data.dynamodb.delegate;

import com.amazonaws.AmazonWebServiceResult;
import com.amazonaws.ResponseMetadata;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ConsumedCapacity;

import java.util.Map;

public interface GenericItemResult {
    AmazonWebServiceResult<ResponseMetadata> getAwsResult();
    ConsumedCapacity getConsumedCapacity();
    Map<String, AttributeValue> getAttributes();
}
