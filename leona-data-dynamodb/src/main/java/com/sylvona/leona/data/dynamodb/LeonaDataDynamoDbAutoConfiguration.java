package com.sylvona.leona.data.dynamodb;

import com.sylvona.leona.data.dynamodb.converter.ConverterRegistryFactory;
import com.sylvona.leona.data.dynamodb.converter.DatabaseItemConverter;
import com.sylvona.leona.data.dynamodb.converter.DefaultConverterRegistryFactory;
import com.sylvona.leona.data.dynamodb.delegate.DynamoRepositoryDelegateFactory;
import com.sylvona.leona.data.dynamodb.delegate.FilterAwareDynamoRepositoryDelegateFactory;
import com.sylvona.leona.data.dynamodb.delegate.filters.PostDynamoDBFilter;
import com.sylvona.leona.data.dynamodb.delegate.filters.PreDynamoDBFilter;
import com.sylvona.leona.data.dynamodb.layout.EntityLayoutFactory;
import com.sylvona.leona.data.dynamodb.layout.ValidatingLayoutFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import java.util.List;

@Slf4j
@AutoConfiguration
@RequiredArgsConstructor
public class LeonaDataDynamoDbAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean(DynamoRepositoryDelegateFactory.class)
    public DynamoRepositoryDelegateFactory delegateFactory(List<PreDynamoDBFilter> preDynamoFilters, List<PostDynamoDBFilter> postDynamoFilters) {
        return new FilterAwareDynamoRepositoryDelegateFactory(preDynamoFilters, postDynamoFilters);
    }

    @Bean
    @ConditionalOnMissingBean(ConverterRegistryFactory.class)
    public ConverterRegistryFactory registryFactory(List<DatabaseItemConverter<?, ?>> databaseItemConverters) {
        return new DefaultConverterRegistryFactory(databaseItemConverters);
    }

    @Bean
    @ConditionalOnMissingBean(EntityLayoutFactory.class)
    public EntityLayoutFactory entityLayoutFactory(ConverterRegistryFactory converterRegistryFactory) {
        return new ValidatingLayoutFactory(converterRegistryFactory);
    }
}
