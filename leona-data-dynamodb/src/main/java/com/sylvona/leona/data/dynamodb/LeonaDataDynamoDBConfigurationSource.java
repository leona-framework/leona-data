package com.sylvona.leona.data.dynamodb;

import com.sylvona.leona.data.dynamodb.client.AmazonDynamoDBClientConfiguration;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@Data
@ConfigurationProperties("leona.data.dynamodb")
public class LeonaDataDynamoDBConfigurationSource {
    @NestedConfigurationProperty
    private AmazonDynamoDBClientConfiguration awsClient = new AmazonDynamoDBClientConfiguration();
}
