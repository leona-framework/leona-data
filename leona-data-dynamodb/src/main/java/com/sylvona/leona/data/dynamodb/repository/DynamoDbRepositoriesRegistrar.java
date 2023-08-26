package com.sylvona.leona.data.dynamodb.repository;

import org.springframework.data.repository.config.RepositoryBeanDefinitionRegistrarSupport;
import org.springframework.data.repository.config.RepositoryConfigurationExtension;

import java.lang.annotation.Annotation;

class DynamoDbRepositoriesRegistrar extends RepositoryBeanDefinitionRegistrarSupport {

    @Override
    protected Class<? extends Annotation> getAnnotation() {
        return EnableDynamoDbRepositories.class;
    }

    @Override
    protected RepositoryConfigurationExtension getExtension() {
        return new DynamoDbRepositoryExtension();
    }
}
