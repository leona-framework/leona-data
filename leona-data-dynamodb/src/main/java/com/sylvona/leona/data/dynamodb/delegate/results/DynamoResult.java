package com.sylvona.leona.data.dynamodb.delegate.results;

import com.sylvona.leona.core.commons.containers.ExecutionView;

public interface DynamoResult<T> extends ExecutionView<T>, GenericItemResult {
}
