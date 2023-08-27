package com.sylvona.leona.data.dynamodb.repository;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@NoRepositoryBean
public interface DynamoRepository<T, ID> extends CrudRepository<T, ID> {
    <RID> Optional<T> findById(ID id, RID rid);

    <ID2> List<T> findAllById(Map<ID, Iterable<ID2>> hashAndRangeKeys);
    @Override
    @NotNull
    List<T> findAll();

    @Override
    @NotNull
    List<T> findAllById(@NotNull Iterable<ID> hashKeys);
}
