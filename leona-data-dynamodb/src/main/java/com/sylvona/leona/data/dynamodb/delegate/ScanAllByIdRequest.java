package com.sylvona.leona.data.dynamodb.delegate;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.sylvona.leona.data.dynamodb.entity.EntityLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScanAllByIdRequest<ID> extends ScanRequest2 {
    private final List<String> filterExpressionBuilder = new ArrayList<>();
    private final Map<String, AttributeValue> expressionValues = new HashMap<>();
    private final String primaryKeyName;
    private final String rangeKeyName;

    private ScanAllByIdRequest(String tableName, EntityLayout<?> entityLayout) {
        setTableName(tableName);
        primaryKeyName = entityLayout.getPrimaryKeyName();
        rangeKeyName = entityLayout.getRangeKeyName();
    }

    public ScanAllByIdRequest(String tableName, EntityLayout<?> entityLayout, Iterable<ID> ids) {
        this(tableName, entityLayout);

        int i = 0;
        for (ID id : ids) {
            String pKey = ":primary" + i;
            filterExpressionBuilder.add("(" + primaryKeyName + " = " + pKey + ")");
            expressionValues.put(pKey, entityLayout.getSignificantKeys(id).get(primaryKeyName));
            i++;
        }

        setFilterExpression(String.join(" or ", filterExpressionBuilder));
        setExpressionAttributeValues(expressionValues);
    }

    public <ID2> ScanAllByIdRequest(String tableName, EntityLayout<?> entityLayout, Map<ID, Iterable<ID2>> ids) {
        this(tableName, entityLayout);

        int i = 0;
        for (Map.Entry<ID, Iterable<ID2>> idIterableEntry : ids.entrySet()) {
            ID id = idIterableEntry.getKey();
            String pKey = ":primary" + i;
            String pKeyExpression = primaryKeyName + " = " + pKey;
            expressionValues.put(pKey, entityLayout.getSignificantKeys(id).get(primaryKeyName));

            Iterable<?> iterableID = idIterableEntry.getValue();
            if (iterableID == null) {
                filterExpressionBuilder.add("(" + pKeyExpression + ")");
            } else {
                boolean set = false;
                for (Object id2 : iterableID) {
                    String sKey = ":secondary" + i++;
                    String totalExpression = "(" + pKeyExpression + " and " + rangeKeyName + " = " + sKey + ")";
                    filterExpressionBuilder.add(totalExpression);
                    expressionValues.put(sKey, entityLayout.getSignificantKeys(id, id2).get(rangeKeyName));
                    set = true;
                }
                if (!set) filterExpressionBuilder.add("(" + pKeyExpression + ")");
            }

            i++;
        }

        setFilterExpression(String.join(" or ", filterExpressionBuilder));
        setExpressionAttributeValues(expressionValues);
    }
}
