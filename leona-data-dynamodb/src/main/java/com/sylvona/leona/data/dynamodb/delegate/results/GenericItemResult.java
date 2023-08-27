package com.sylvona.leona.data.dynamodb.delegate.results;

import com.amazonaws.AmazonWebServiceResult;
import com.amazonaws.ResponseMetadata;
import com.amazonaws.services.dynamodbv2.model.ConsumedCapacity;

public interface GenericItemResult {
    AmazonWebServiceResult<ResponseMetadata> getAwsResult();
    ConsumedCapacity getConsumedCapacity();
}
