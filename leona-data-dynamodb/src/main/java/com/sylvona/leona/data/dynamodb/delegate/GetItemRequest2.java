package com.sylvona.leona.data.dynamodb.delegate;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.dynamodbv2.model.ReturnConsumedCapacity;
import com.sylvona.leona.data.dynamodb.AttributeKeyValue;
import lombok.AccessLevel;
import lombok.Setter;

import java.util.Collection;
import java.util.Map;

class GetItemRequest2 extends GetItemRequest implements DynamoRequest {
    private AttributeKeyValue attributeKeyValue;
    @Setter(AccessLevel.PACKAGE)
    private String primaryKeyName;

    @Override
    public GetItemRequest2 withTableName(String tableName) {
        super.withTableName(tableName);
		return this;
    }

    @Override
    public GetItemRequest2 withKey(Map<String, AttributeValue> key) {
        super.withKey(key);
        if (key instanceof AttributeKeyValue akv)
            this.attributeKeyValue = akv;
		return this;
    }

    public GetItemRequest2 withKey(String key, String value) {
        super.withKey(attributeKeyValue = new AttributeKeyValue(key, new AttributeValue(value)));
        return this;
    }

    @Override
    public GetItemRequest2 withAttributesToGet(String... attributesToGet) {
        super.withAttributesToGet(attributesToGet);
		return this;
    }

    @Override
    public GetItemRequest2 withAttributesToGet(Collection<String> attributesToGet) {
        super.withAttributesToGet(attributesToGet);
		return this;
    }

    @Override
    public GetItemRequest2 withConsistentRead(Boolean consistentRead) {
        super.withConsistentRead(consistentRead);
		return this;
    }

    @Override
    public GetItemRequest2 withReturnConsumedCapacity(String returnConsumedCapacity) {
        super.withReturnConsumedCapacity(returnConsumedCapacity);
		return this;
    }

    @Override
    public GetItemRequest2 withReturnConsumedCapacity(ReturnConsumedCapacity returnConsumedCapacity) {
        super.withReturnConsumedCapacity(returnConsumedCapacity);
		return this;
    }

    @Override
    public GetItemRequest2 withProjectionExpression(String projectionExpression) {
        super.withProjectionExpression(projectionExpression);
		return this;
    }

    @Override
    public GetItemRequest2 withExpressionAttributeNames(Map<String, String> expressionAttributeNames) {
        super.withExpressionAttributeNames(expressionAttributeNames);
		return this;
    }

    @Override
    public GetItemRequest2 withKey(Map.Entry<String, AttributeValue> hashKey, Map.Entry<String, AttributeValue> rangeKey) throws IllegalArgumentException {
        super.withKey(hashKey, rangeKey);
		return this;
    }

    @Override
    public String getPrimaryKeyName() {
        if (attributeKeyValue == null) {
            attributeKeyValue = new AttributeKeyValue(primaryKeyName, getKey().get(primaryKeyName));
        }
        return attributeKeyValue.getKey();
    }

    @Override
    public AttributeValue getPrimaryKeyValue() {
        if (attributeKeyValue == null) {
            attributeKeyValue = new AttributeKeyValue(primaryKeyName, getKey().get(primaryKeyName));
        }
        return attributeKeyValue.getValue();
    }
}
