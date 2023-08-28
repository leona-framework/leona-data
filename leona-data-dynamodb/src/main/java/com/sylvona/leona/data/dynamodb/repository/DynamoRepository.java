package com.sylvona.leona.data.dynamodb.repository;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * A repository interface for performing CRUD operations on entities in Amazon DynamoDB.
 * This interface extends the {@link CrudRepository} interface and adds specific methods for DynamoDB operations.
 *
 * @param <T>  The type of entity to be managed.
 * @param <ID> The type of the primary key (hash key) of the entity.
 */
@NoRepositoryBean
public interface DynamoRepository<T, ID> extends CrudRepository<T, ID> {
    /**
     * Retrieves an entity by its primary key (hash key) and range key.
     *
     * @param id  The primary key (hash key) of the entity.
     * @param rid The range key of the entity.
     * @param <RID> The type of the range key.
     * @return An {@link Optional} containing the entity if found, or empty if not found.
     */
    <RID> Optional<T> findById(ID id, RID rid);

    /**
     * Retrieves entities based on a map of hash keys and their corresponding range keys.
     *
     * @param hashAndRangeKeys A map of hash keys to ranges keys for which entities need to be retrieved.
     * @param <ID2> The type of the range key.
     * @return A list of entities matching the provided hash and range key pairs.
     */
    <ID2> List<T> findAllById(Map<ID, Iterable<ID2>> hashAndRangeKeys);
    @Override
    @NotNull
    List<T> findAll();

    @Override
    @NotNull
    List<T> findAllById(@NotNull Iterable<ID> hashKeys);
}
