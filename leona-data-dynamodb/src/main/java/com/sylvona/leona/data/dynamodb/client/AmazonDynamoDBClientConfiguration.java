package com.sylvona.leona.data.dynamodb.client;

import com.amazonaws.auth.AWSCredentialsProvider;
import lombok.Data;

/**
 * Provides configuration for the client used to connect with AWS DyanmoDB
 */
@Data
public class AmazonDynamoDBClientConfiguration {
    /**
     * The class to be used for providing AWS credentials to the DynamoDB client
     */
    private Class<? extends AWSCredentialsProvider> credentialsProvider = DefaultSpringAWSCredentialsProvider.class;
    /**
     * An Amazon AWS access key
     */
    private String accessKey;
    /**
     * An Amazon AWS access secret
     */
    private String accessSecret;
    /**
     * The service endpoint of AWS DynamoDB with or without the protocol (e.g. <a href="https://dynamodb.us-east-2.amazonaws.com">...</a> or dynamodb.us-east-2.amazonaws.com)
     */
    private String endpoint;
    /**
     * The region to use for SigV4 signing of requests (e.g. us-east-2)
     */
    private String signingRegion;
}
