package com.sylvona.leona.data.dynamodb;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.sylvona.leona.core.utils.BeanFirstClassConstructor;
import com.sylvona.leona.data.dynamodb.client.AWSCredentialsProviderWithSpring;
import com.sylvona.leona.data.dynamodb.client.AmazonDynamoDBClientConfiguration;
import com.sylvona.leona.data.dynamodb.converter.ConverterRegistryFactory;
import com.sylvona.leona.data.dynamodb.converter.DatabaseItemConverter;
import com.sylvona.leona.data.dynamodb.converter.DefaultConverterRegistryFactory;
import com.sylvona.leona.data.dynamodb.delegate.DynamoRepositoryDelegateFactory;
import com.sylvona.leona.data.dynamodb.delegate.FilterAwareDynamoRepositoryDelegateFactory;
import com.sylvona.leona.data.dynamodb.delegate.filters.PostDynamoDBFilter;
import com.sylvona.leona.data.dynamodb.delegate.filters.PreDynamoDBFilter;
import com.sylvona.leona.data.dynamodb.entity.EntityLayoutFactory;
import com.sylvona.leona.data.dynamodb.entity.ValidatingLayoutFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.List;

@Slf4j
@AutoConfiguration
@RequiredArgsConstructor
@EnableConfigurationProperties(LeonaDataDynamoDBConfigurationSource.class)
public class LeonaDataDynamoDbAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean(AmazonDynamoDB.class)
    public AmazonDynamoDB autoConfiguredAmazonDynamoDBClient(BeanFirstClassConstructor beanFirstClassConstructor, LeonaDataDynamoDBConfigurationSource configurationSource) {
        AmazonDynamoDBClientConfiguration clientConfiguration = configurationSource.getAwsClient();
        AWSCredentialsProvider credentialsProvider = beanFirstClassConstructor.getOrCreate(clientConfiguration.getCredentialsProvider());

        if (credentialsProvider instanceof AWSCredentialsProviderWithSpring awsCredentialsProviderWithSpring)
            awsCredentialsProviderWithSpring.receiveAccessCredentials(clientConfiguration.getAccessKey(), clientConfiguration.getAccessSecret());

        return AmazonDynamoDBClientBuilder.standard()
                .withCredentials(credentialsProvider)
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(clientConfiguration.getEndpoint(), clientConfiguration.getSigningRegion()))
                .build();
    }

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
