package com.sylvona.leona.data.dynamodb.entity;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import jakarta.annotation.Nullable;

import java.util.Map;

/**
 * An interface that defines methods for managing the layout and attributes of a DynamoDB entity.
 *
 * @param <T> The type of the entity.
 */
public interface EntityLayout<T> {
    /**
     * Creates an entity object from the given attributes.
     *
     * @param attributes The attributes representing the entity.
     * @return The created entity object.
     */
    T createEntity(Map<String, AttributeValue> attributes);

    /**
     * Gets the attributes of the entity object.
     *
     * @param entity The entity object.
     * @return The attributes of the entity.
     */
    Map<String, AttributeValue> getAttributes(T entity);

    /**
     * Gets the name of the DynamoDB entity.
     *
     * @return The name of the entity.
     */
    String getEntityName();

    /**
     * Gets the primary key of the entity based on the provided ID.
     *
     * @param id The ID for which significant keys are generated.
     * @param <ID> The type of the ID.
     * @return The significant keys as attributes.
     */
    default <ID> Map<String, AttributeValue> getSignificantKeys(ID id) {
        return getSignificantKeys(id, null);
    }

    /**
     * Gets the significant keys (primary key, and optional range key) of the entity based on the provided IDs.
     *
     * @param id1 The first ID component.
     * @param id2 The second ID component (can be null).
     * @param <ID1> The type of the first ID component.
     * @param <ID2> The type of the second ID component.
     * @return The significant keys as attributes.
     */
    <ID1, ID2> Map<String, AttributeValue> getSignificantKeys(ID1 id1, @Nullable ID2 id2);

    /**
     * Gets the name of the primary key attribute for the entity.
     *
     * @return The name of the primary key attribute.
     */
    String getPrimaryKeyName();

    /**
     * Gets the name of the range key attribute for the entity, if applicable.
     *
     * @return The name of the range key attribute, or null if not applicable.
     */
    @Nullable String getRangeKeyName();
}
