package com.sylvona.leona.data.dynamodb.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface DynamoRepository<Type, HashKey> extends CrudRepository<Type, HashKey> {
}
