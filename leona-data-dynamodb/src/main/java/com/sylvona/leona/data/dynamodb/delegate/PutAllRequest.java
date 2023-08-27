package com.sylvona.leona.data.dynamodb.delegate;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.BatchWriteItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutRequest;
import com.amazonaws.services.dynamodbv2.model.WriteRequest;
import com.sylvona.leona.data.dynamodb.entity.EntityLayout;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
public class PutAllRequest<T> extends BatchWriteItemRequest implements DynamoRequest {
    private final String tableName;
    private final String primaryKeyName;

    public PutAllRequest(String tableName, EntityLayout<T> entityLayout, Iterable<T> entities) {
        this.tableName = tableName;
        this.primaryKeyName = entityLayout.getPrimaryKeyName();

        List<WriteRequest> writeRequests = new ArrayList<>();
        Map<String, List<WriteRequest>> writeRequestMap = Map.of(tableName, writeRequests);

        for (T entity : entities) {
            writeRequests.add(new WriteRequest().withPutRequest(new PutRequest().withItem(entityLayout.getAttributes(entity))));
        }

        setRequestItems(writeRequestMap);
    }

    @Override
    public AttributeValue getPrimaryKeyValue() {
        return null;
    }
}
