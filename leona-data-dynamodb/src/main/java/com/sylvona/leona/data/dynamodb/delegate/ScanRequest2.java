package com.sylvona.leona.data.dynamodb.delegate;

import com.amazonaws.services.dynamodbv2.model.*;

import java.util.Collection;
import java.util.Map;

public class ScanRequest2 extends ScanRequest implements DynamoRequest {
    @Override
    public String getPrimaryKeyName() {
        return null;
    }

    @Override
    public AttributeValue getPrimaryKeyValue() {
        return null;
    }

    @Override
    public ScanRequest2 withTableName(String tableName) {
        super.withTableName(tableName);
		return this;
    }

    @Override
    public ScanRequest2 withIndexName(String indexName) {
        super.withIndexName(indexName);
		return this;
    }

    @Override
    public ScanRequest2 withAttributesToGet(String... attributesToGet) {
        super.withAttributesToGet(attributesToGet);
		return this;
    }

    @Override
    public ScanRequest2 withAttributesToGet(Collection<String> attributesToGet) {
        super.withAttributesToGet(attributesToGet);
		return this;
    }

    @Override
    public ScanRequest2 withLimit(Integer limit) {
        super.withLimit(limit);
		return this;
    }

    @Override
    public ScanRequest2 withSelect(String select) {
        super.withSelect(select);
		return this;
    }

    @Override
    public ScanRequest2 withSelect(Select select) {
        super.withSelect(select);
		return this;
    }

    @Override
    public ScanRequest2 withScanFilter(Map<String, Condition> scanFilter) {
        super.withScanFilter(scanFilter);
		return this;
    }

    @Override
    public ScanRequest2 withConditionalOperator(String conditionalOperator) {
        super.withConditionalOperator(conditionalOperator);
		return this;
    }

    @Override
    public ScanRequest2 withConditionalOperator(ConditionalOperator conditionalOperator) {
        super.withConditionalOperator(conditionalOperator);
		return this;
    }

    @Override
    public ScanRequest2 withExclusiveStartKey(Map<String, AttributeValue> exclusiveStartKey) {
        super.withExclusiveStartKey(exclusiveStartKey);
		return this;
    }

    @Override
    public ScanRequest2 withReturnConsumedCapacity(String returnConsumedCapacity) {
        super.withReturnConsumedCapacity(returnConsumedCapacity);
		return this;
    }

    @Override
    public ScanRequest2 withReturnConsumedCapacity(ReturnConsumedCapacity returnConsumedCapacity) {
        super.withReturnConsumedCapacity(returnConsumedCapacity);
		return this;
    }

    @Override
    public ScanRequest2 withTotalSegments(Integer totalSegments) {
        super.withTotalSegments(totalSegments);
		return this;
    }

    @Override
    public ScanRequest2 withSegment(Integer segment) {
        super.withSegment(segment);
		return this;
    }

    @Override
    public ScanRequest2 withProjectionExpression(String projectionExpression) {
        super.withProjectionExpression(projectionExpression);
		return this;
    }

    @Override
    public ScanRequest2 withFilterExpression(String filterExpression) {
        super.withFilterExpression(filterExpression);
		return this;
    }

    @Override
    public ScanRequest2 withExpressionAttributeNames(Map<String, String> expressionAttributeNames) {
        super.withExpressionAttributeNames(expressionAttributeNames);
		return this;
    }

    @Override
    public ScanRequest2 withExpressionAttributeValues(Map<String, AttributeValue> expressionAttributeValues) {
        super.withExpressionAttributeValues(expressionAttributeValues);
		return this;
    }

    @Override
    public ScanRequest2 withConsistentRead(Boolean consistentRead) {
        super.withConsistentRead(consistentRead);
		return this;
    }

    @Override
    public ScanRequest2 withExclusiveStartKey(Map.Entry<String, AttributeValue> hashKey, Map.Entry<String, AttributeValue> rangeKey) throws IllegalArgumentException {
        super.withExclusiveStartKey(hashKey, rangeKey);
		return this;
    }
}
