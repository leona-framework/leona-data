package com.sylvona.leona.data.dynamodb.repository;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.sylvona.leona.data.dynamodb.delegate.DynamoRepositoryDelegate;
import com.sylvona.leona.data.dynamodb.delegate.DynamoRepositoryDelegateFactory;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;

class DynamoRepositoryImpl<T, ID extends Serializable> implements DynamoRepository<T, ID> {
    private final DynamoRepositoryDelegate<ID, T> repositoryDelegate;

    public DynamoRepositoryImpl(DynamoRepositoryEntityInformation<T, ?> entityInformation, DynamoRepositoryDelegateFactory delegateFactory, AmazonDynamoDB dynamoDB) {
        repositoryDelegate = delegateFactory.getRepositoryDelegate(dynamoDB, entityInformation);
    }

    @Override
    public <S extends T> @NotNull S save(@NotNull S entity) {
        repositoryDelegate.put(entity);
        return entity;
    }

    @Override
    public <S extends T> @NotNull Iterable<S> saveAll(@NotNull Iterable<S> entities) {
        repositoryDelegate.putAll((Iterable<T>) entities);
        return entities;
    }

    @Override
    public @NotNull Optional<T> findById(@NotNull ID id) {
        return Optional.ofNullable(repositoryDelegate.get(id).result());
    }

    @Override
    public <RID> Optional<T> findById(ID id, RID rid) {
        return Optional.ofNullable(repositoryDelegate.get(id, rid).result());
    }

    @Override
    public boolean existsById(@NotNull ID id) {
        return findById(id).isPresent();
    }

    @Override
    public @NotNull List<T> findAll() {
        return repositoryDelegate.getAll().result();
    }

    @Override
    public <ID2> List<T> findAllById(Map<ID, Iterable<ID2>> hashAndRangeKeys) {
        return repositoryDelegate.getAllById(hashAndRangeKeys).result();
    }

    @Override
    public @NotNull List<T> findAllById(@NotNull Iterable<ID> ids) {
        return repositoryDelegate.getAllById(ids).result();
    }

    @Override
    public long count() {
        return findAll().size();
    }

    @Override
    public void deleteById(@NotNull ID id) {

    }

    @Override
    public void delete(@NotNull T entity) {

    }

    @Override
    public void deleteAllById(@NotNull Iterable<? extends ID> ids) {

    }

    @Override
    public void deleteAll(@NotNull Iterable<? extends T> entities) {

    }

    @Override
    public void deleteAll() {

    }
}
