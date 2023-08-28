package com.sylvona.leona.data.dynamodb.delegate.results;

import com.sylvona.leona.core.commons.containers.ExecutionView;

/**
 * An interface representing the result of an operation on a DynamoDB item, extending both the {@link ExecutionView}
 * interface for execution-related information and the {@link GenericItemResult} interface for AWS result metadata and
 * consumed capacity information.
 *
 * @param <T> The type of the successful result of the operation.
 */
public interface DynamoResult<T> extends ExecutionView<T>, GenericItemResult {
}
