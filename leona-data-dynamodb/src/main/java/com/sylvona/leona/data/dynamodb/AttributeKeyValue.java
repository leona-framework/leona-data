package com.sylvona.leona.data.dynamodb;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.transform.MapEntry;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Getter
public class AttributeKeyValue implements Map<String, AttributeValue> {
    @NotNull
    private String key;
    @NotNull private AttributeValue value;

    public AttributeKeyValue(@NotNull String key, @NotNull AttributeValue value) {
        this.key = key;
        this.value = value;
    }

    AttributeKeyValue(@NotNull String primaryKeyName, Map<String, AttributeValue> attributeValueMap) {
        this.key = primaryKeyName;
        this.value = attributeValueMap.get(primaryKeyName);
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean containsKey(Object key) {
        return this.key.equals(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return this.value.equals(value);
    }

    @Override
    public AttributeValue get(Object key) {
        return value;
    }

    @Override
    public AttributeValue put(String key, AttributeValue value) {
        AttributeValue oldValue = this.value;
        this.key = key;
        this.value = value;
        return oldValue;
    }

    @Override
    public AttributeValue remove(Object key) {
        return this.value;
    }

    @Override
    public void putAll(Map<? extends String, ? extends AttributeValue> m) {
        this.value = m.get(this.key);
    }

    @Override
    public void clear() {}

    @Override
    public @NotNull Set<String> keySet() {
        return Set.of(this.key);
    }

    @Override
    public @NotNull Collection<AttributeValue> values() {
        return List.of(this.value);
    }

    @Override
    public @NotNull Set<Entry<String, AttributeValue>> entrySet() {
        MapEntry<String, AttributeValue> entry = new MapEntry<>();
        entry.setKey(key);
        entry.setValue(value);
        return Set.of(entry);
    }
}