package com.sylvona.leona.data.dynamodb.repository;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.sylvona.leona.data.dynamodb.delegate.DynamoRepositoryDelegateFactory;
import com.sylvona.leona.data.dynamodb.entity.EntityLayoutFactory;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.core.support.RepositoryFactoryBeanSupport;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

@Slf4j
class DynamoRepositoryFactoryBeanSupport<T extends DynamoRepository<S, ID>, S, ID> extends RepositoryFactoryBeanSupport<T, S, ID> {
    private final DynamoRepositoryDelegateFactory delegateFactory;
    private final EntityLayoutFactory entityLayoutFactory;
    private final AmazonDynamoDB amazonDynamoDB;
    private final Class<T> customImplementationClass;

    /**
     * Creates a new {@link RepositoryFactoryBeanSupport} for the given repository interface.
     *
     * @param repositoryInterface  must not be {@literal null}.
     * @param delegateFactory
     * @param entityLayoutFactory
     */
    public DynamoRepositoryFactoryBeanSupport(Class<T> repositoryInterface, DynamoRepositoryDelegateFactory delegateFactory, EntityLayoutFactory entityLayoutFactory, AmazonDynamoDB amazonDynamoDB) {
        super(repositoryInterface);
        this.delegateFactory = delegateFactory;
        this.entityLayoutFactory = entityLayoutFactory;
        this.amazonDynamoDB = amazonDynamoDB;
        this.customImplementationClass = repositoryInterface;
    }


    @Override
    protected @NotNull RepositoryFactorySupport createRepositoryFactory() {
        return new DynamoRepositoryFactorySupport<>(delegateFactory, entityLayoutFactory, amazonDynamoDB, customImplementationClass);
    }
}
