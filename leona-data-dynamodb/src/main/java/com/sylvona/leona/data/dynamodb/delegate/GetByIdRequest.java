package com.sylvona.leona.data.dynamodb.delegate;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.BatchGetItemRequest;
import com.amazonaws.services.dynamodbv2.model.KeysAndAttributes;
import com.sylvona.leona.core.commons.streams.LINQ;
import com.sylvona.leona.data.dynamodb.entity.EntityLayout;
import lombok.Getter;

import java.util.Map;

@Getter
public class GetByIdRequest<ID> extends BatchGetItemRequest implements DynamoRequest {
    private final String tableName;
    private final String primaryKeyName;

    public GetByIdRequest(String tableName, EntityLayout<?> entityLayout, Iterable<ID> ids) {
        this.tableName = tableName;
        this.primaryKeyName = entityLayout.getPrimaryKeyName();
        KeysAndAttributes keysAndAttributes = new KeysAndAttributes();

        Map<String, KeysAndAttributes> tableKeys = Map.of(tableName, keysAndAttributes);
        keysAndAttributes.setKeys(LINQ.stream(ids).map(entityLayout::getSignificantKeys).toList());
        setRequestItems(tableKeys);
    }

    @Override
    public AttributeValue getPrimaryKeyValue() {
        return null;
    }
}
