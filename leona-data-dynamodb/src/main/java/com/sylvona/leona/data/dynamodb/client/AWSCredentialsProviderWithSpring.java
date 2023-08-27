package com.sylvona.leona.data.dynamodb.client;

import com.amazonaws.auth.AWSCredentialsProvider;

public interface AWSCredentialsProviderWithSpring extends AWSCredentialsProvider {
    void receiveAccessCredentials(String accessKey, String accessSecret);
}
