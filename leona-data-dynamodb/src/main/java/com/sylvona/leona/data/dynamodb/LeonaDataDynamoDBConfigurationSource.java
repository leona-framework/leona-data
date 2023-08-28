package com.sylvona.leona.data.dynamodb;

import com.sylvona.leona.data.dynamodb.client.AmazonDynamoDBClientConfiguration;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * Configuration properties class for Leona Data's DynamoDB settings.
 * This class is used to define configuration properties related to DynamoDB operations.
 */
@Data
@ConfigurationProperties("leona.data.dynamodb")
public class LeonaDataDynamoDBConfigurationSource {

    /**
     * Nested configuration property for Amazon DynamoDB client settings.
     * This property holds the configuration for the Amazon DynamoDB client, including credentials and endpoints.
     */
    @NestedConfigurationProperty
    private AmazonDynamoDBClientConfiguration awsClient = new AmazonDynamoDBClientConfiguration();
}
