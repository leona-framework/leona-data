package com.sylvona.leona.data.dynamodb.layout;

public interface EntityLayoutFactory {
    <T> EntityLayout<T> getLayout(Class<T> cls);
}
