package com.sylvona.leona.data.dynamodb.client;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;

class DefaultSpringAWSCredentialsProvider implements AWSCredentialsProviderWithSpring {
    private AWSCredentials awsCredentials;

    @Override
    public AWSCredentials getCredentials() {
        return awsCredentials;
    }

    @Override
    public void refresh() {}

    @Override
    public void receiveAccessCredentials(String accessKey, String accessSecret) {
        awsCredentials = new BasicAWSCredentials(accessKey, accessSecret);
    }
}
