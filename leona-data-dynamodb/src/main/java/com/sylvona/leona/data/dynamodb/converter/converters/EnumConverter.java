package com.sylvona.leona.data.dynamodb.converter.converters;

import com.sylvona.leona.data.dynamodb.AttributeType;
import com.sylvona.leona.data.dynamodb.converter.ConverterRegistryView;
import com.sylvona.leona.data.dynamodb.converter.DatabaseItemConverter;

/**
 * Converts a generic enum to/from a DynamoDB String
 * @param <TEnum> the enum type
 */
public class EnumConverter<TEnum extends Enum<TEnum>> extends DatabaseItemConverter<TEnum, AttributeType.String> {
    private final Class<TEnum> enumClass;

    /**
     * Constructs converter with the enum type.
     * @param enumType the enum type.
     */
    public EnumConverter(Class<?> enumType) {
        this.enumClass = (Class<TEnum>) enumType;
    }


    @Override
    public AttributeType.String toDatabase(TEnum tEnum, ConverterRegistryView registry) {
        return AttributeType.string(tEnum.toString());
    }

    @Override
    public TEnum toJavaObject(AttributeType.String string, ConverterRegistryView registry) {
        return Enum.valueOf(enumClass, string.getValue());
    }
}
