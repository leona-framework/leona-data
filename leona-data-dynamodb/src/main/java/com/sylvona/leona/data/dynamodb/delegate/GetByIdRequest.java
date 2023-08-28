package com.sylvona.leona.data.dynamodb.delegate;

import com.amazonaws.services.dynamodbv2.model.BatchGetItemRequest;
import com.amazonaws.services.dynamodbv2.model.KeysAndAttributes;
import com.sylvona.leona.core.commons.streams.LINQ;
import com.sylvona.leona.data.dynamodb.entity.EntityLayout;
import lombok.Getter;

import java.util.Map;

/**
 * A class representing a request to retrieve items by their IDs using the BatchGetItem operation in DynamoDB.
 *
 * @param <ID> The type of the entity's primary key.
 */
@Getter
public class GetByIdRequest<ID> extends BatchGetItemRequest implements DynamoRequest {
    private final String tableName;

    /**
     * Constructs a new GetByIdRequest for retrieving items by their IDs.
     *
     * @param tableName The name of the table to query.
     * @param entityLayout The layout of the entity associated with the table.
     * @param ids The IDs of the items to retrieve.
     */
    public GetByIdRequest(String tableName, EntityLayout<?> entityLayout, Iterable<ID> ids) {
        this.tableName = tableName;
        KeysAndAttributes keysAndAttributes = new KeysAndAttributes();

        Map<String, KeysAndAttributes> tableKeys = Map.of(tableName, keysAndAttributes);
        keysAndAttributes.setKeys(LINQ.stream(ids).map(entityLayout::getSignificantKeys).toList());
        setRequestItems(tableKeys);
    }

    /**
     * Retrieves the name of the table for which the request is made.
     *
     * @return The name of the table.
     */
    @Override
    public String getTableName() {
        return tableName;
    }
}

