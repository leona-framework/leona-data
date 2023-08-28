package com.sylvona.leona.data.dynamodb.repository;

import com.sylvona.leona.data.dynamodb.entity.EntityLayout;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.repository.core.EntityInformation;

/**
 * Default implementation of {@link EntityInformation} which lazily provides information to the internal DynamoDB delegate.
 * @param <T> the entity type
 * @param <ID> the primary key of the entity
 */
@Getter
@RequiredArgsConstructor
public class DynamoRepositoryEntityInformation<T, ID> implements EntityInformation<T, ID> {
    private final Class<T> javaType;
    private final EntityLayout<T> layout;
    @Setter @Accessors(chain = true)
    private Class<ID> idType;

    @Override
    public boolean isNew(@NotNull T entity) {
        return false;
    }

    @Override
    public ID getId(@NotNull T entity) {
        return null;
    }
}
