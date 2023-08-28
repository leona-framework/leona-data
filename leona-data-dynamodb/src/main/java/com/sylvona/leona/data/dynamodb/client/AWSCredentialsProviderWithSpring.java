package com.sylvona.leona.data.dynamodb.client;

import com.amazonaws.auth.AWSCredentialsProvider;

/**
 * An enhanced implementation of {@link AWSCredentialsProvider} that provides credential properties directly from Spring's configuration management system.
 */
public interface AWSCredentialsProviderWithSpring extends AWSCredentialsProvider {
    /**
     * Automatically invoked when Spring initializes an object extending this class. Provides credentials from the configuration directly
     * into the provider.
     * @param accessKey the AWS access-key retrieved from configuration.
     * @param accessSecret the AWS access-secret retrieved from configuration.
     */
    void receiveAccessCredentials(String accessKey, String accessSecret);
}
