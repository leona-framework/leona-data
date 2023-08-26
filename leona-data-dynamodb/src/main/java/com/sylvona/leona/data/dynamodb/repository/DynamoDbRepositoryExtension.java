package com.sylvona.leona.data.dynamodb.repository;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.data.repository.config.AnnotationRepositoryConfigurationSource;
import org.springframework.data.repository.config.RepositoryConfigurationExtensionSupport;

class DynamoDbRepositoryExtension extends RepositoryConfigurationExtensionSupport {
    @Override
    protected String getModulePrefix() {
        return "DYNAMO-DB";
    }

    @Override
    public String getRepositoryFactoryBeanClassName() {
        return DynamoRepositoryFactoryBeanSupport.class.getName();
    }

    @Override
    public void postProcess(BeanDefinitionBuilder builder, AnnotationRepositoryConfigurationSource config) {
        super.postProcess(builder, config);
    }
}
