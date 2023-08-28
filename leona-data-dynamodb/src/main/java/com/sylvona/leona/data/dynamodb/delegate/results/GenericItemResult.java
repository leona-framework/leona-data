package com.sylvona.leona.data.dynamodb.delegate.results;

import com.amazonaws.AmazonWebServiceResult;
import com.amazonaws.ResponseMetadata;
import com.amazonaws.services.dynamodbv2.model.ConsumedCapacity;

/**
 * An interface representing the result of an operation on a generic item in an Amazon Web Services (AWS) service.
 * This interface provides methods to retrieve the AWS result metadata and information about consumed capacity.
 * <p>
 * Implementations of this interface encapsulate the outcome of interactions with AWS services, specifically focusing
 * on operations involving generic items, such as items in a database or a data store.
 */
public interface GenericItemResult {

    /**
     * Retrieves the AWS result metadata associated with the operation on the generic item.
     *
     * @return The AWS result metadata.
     */
    AmazonWebServiceResult<ResponseMetadata> getAwsResult();

    /**
     * Retrieves information about the consumed capacity resulting from the operation on the generic item.
     *
     * @return The consumed capacity information.
     */
    ConsumedCapacity getConsumedCapacity();
}

