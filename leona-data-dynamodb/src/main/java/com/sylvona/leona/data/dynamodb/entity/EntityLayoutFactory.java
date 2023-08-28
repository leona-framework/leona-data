package com.sylvona.leona.data.dynamodb.entity;

/**
 * An interface for creating entity layouts based on the provided entity class.
 */
public interface EntityLayoutFactory {
    /**
     * Gets an entity layout for the given entity class.
     *
     * @param cls The class of the entity.
     * @param <T> The type of the entity.
     * @return The entity layout for the specified entity class.
     */
    <T> EntityLayout<T> getLayout(Class<T> cls);
}