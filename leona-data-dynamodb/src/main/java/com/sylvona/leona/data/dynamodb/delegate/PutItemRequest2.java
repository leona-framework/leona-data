package com.sylvona.leona.data.dynamodb.delegate;

import com.amazonaws.services.dynamodbv2.model.*;
import com.sylvona.leona.data.dynamodb.AttributeKeyValue;

import java.util.Map;

class PutItemRequest2 extends PutItemRequest implements DynamoRequest {
    private final String primaryKeyName;
    private AttributeKeyValue attributeKeyValue;

    private PutItemRequest2() {
        primaryKeyName = null;
    }

    PutItemRequest2(String primaryKeyName) {
        this.primaryKeyName = primaryKeyName;
    }

    @Override
    public PutItemRequest2 withTableName(String tableName) {
        super.withTableName(tableName);
		return this;
    }

    @Override
    public PutItemRequest2 withItem(Map<String, AttributeValue> item) {
        super.withItem(item);
		return this;
    }

    @Override
    public PutItemRequest2 withExpected(Map<String, ExpectedAttributeValue> expected) {
        super.withExpected(expected);
		return this;
    }

    @Override
    public PutItemRequest2 withReturnValues(String returnValues) {
        super.withReturnValues(returnValues);
		return this;
    }

    @Override
    public PutItemRequest2 withReturnValues(ReturnValue returnValues) {
        super.withReturnValues(returnValues);
		return this;
    }

    @Override
    public PutItemRequest2 withReturnConsumedCapacity(String returnConsumedCapacity) {
        super.withReturnConsumedCapacity(returnConsumedCapacity);
		return this;
    }

    @Override
    public PutItemRequest2 withReturnConsumedCapacity(ReturnConsumedCapacity returnConsumedCapacity) {
        super.withReturnConsumedCapacity(returnConsumedCapacity);
		return this;
    }

    @Override
    public PutItemRequest2 withReturnItemCollectionMetrics(String returnItemCollectionMetrics) {
        super.withReturnItemCollectionMetrics(returnItemCollectionMetrics);
		return this;
    }

    @Override
    public PutItemRequest2 withReturnItemCollectionMetrics(ReturnItemCollectionMetrics returnItemCollectionMetrics) {
        super.withReturnItemCollectionMetrics(returnItemCollectionMetrics);
		return this;
    }

    @Override
    public PutItemRequest2 withConditionalOperator(String conditionalOperator) {
        super.withConditionalOperator(conditionalOperator);
		return this;
    }

    @Override
    public PutItemRequest2 withConditionalOperator(ConditionalOperator conditionalOperator) {
        super.withConditionalOperator(conditionalOperator);
		return this;
    }

    @Override
    public PutItemRequest2 withConditionExpression(String conditionExpression) {
        super.withConditionExpression(conditionExpression);
		return this;
    }

    @Override
    public PutItemRequest2 withExpressionAttributeNames(Map<String, String> expressionAttributeNames) {
        super.withExpressionAttributeNames(expressionAttributeNames);
		return this;
    }

    @Override
    public PutItemRequest2 withExpressionAttributeValues(Map<String, AttributeValue> expressionAttributeValues) {
        super.withExpressionAttributeValues(expressionAttributeValues);
		return this;
    }

    @Override
    public String getPrimaryKeyName() {
        if (attributeKeyValue == null) {
            attributeKeyValue = new AttributeKeyValue(primaryKeyName, getItem().get(primaryKeyName));
        }
        return attributeKeyValue.getKey();
    }

    @Override
    public AttributeValue getPrimaryKeyValue() {
        if (attributeKeyValue == null) {
            attributeKeyValue = new AttributeKeyValue(primaryKeyName, getItem().get(primaryKeyName));
        }
        return attributeKeyValue.getValue();
    }
}
