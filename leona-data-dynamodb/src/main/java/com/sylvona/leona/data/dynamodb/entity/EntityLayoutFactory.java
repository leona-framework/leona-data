package com.sylvona.leona.data.dynamodb.entity;

public interface EntityLayoutFactory {
    <T> EntityLayout<T> getLayout(Class<T> cls);
}
