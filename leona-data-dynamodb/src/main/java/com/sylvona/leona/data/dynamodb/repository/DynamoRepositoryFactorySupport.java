package com.sylvona.leona.data.dynamodb.repository;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.sylvona.leona.data.dynamodb.delegate.DynamoRepositoryDelegateFactory;
import com.sylvona.leona.data.dynamodb.entity.EntityLayoutFactory;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

class DynamoRepositoryFactorySupport<T extends DynamoRepository<S, ID>, S, ID> extends RepositoryFactorySupport {
    private final DynamoRepositoryDelegateFactory delegateFactory;
    private final EntityLayoutFactory entityLayoutFactory;
    private final AmazonDynamoDB amazonDynamoDB;

    public DynamoRepositoryFactorySupport(DynamoRepositoryDelegateFactory delegateFactory, EntityLayoutFactory entityLayoutFactory, AmazonDynamoDB amazonDynamoDB, Class<T> customImplementationClass) {
        this.delegateFactory = delegateFactory;
        this.entityLayoutFactory = entityLayoutFactory;
        this.amazonDynamoDB = amazonDynamoDB;
    }

    @Override
    public <T2, ID2> @NotNull DynamoRepositoryEntityInformation<T2, ID2> getEntityInformation(@NotNull Class<T2> domainClass) {
        return new DynamoRepositoryEntityInformation<>(domainClass, entityLayoutFactory.getLayout(domainClass));
    }

    @Override
    protected @NotNull Object getTargetRepository(@NotNull RepositoryInformation information) {
        return new DynamoRepositoryImpl<>(getEntityInformation(information.getDomainType()), delegateFactory, amazonDynamoDB);
    }

    @Override
    protected @NotNull Class<?> getRepositoryBaseClass(@NotNull RepositoryMetadata metadata) {
        return DynamoRepository.class;
    }
}
