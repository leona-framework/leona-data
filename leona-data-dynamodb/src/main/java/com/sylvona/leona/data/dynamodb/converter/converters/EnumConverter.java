package com.sylvona.leona.data.dynamodb.converter.converters;

import com.sylvona.leona.data.dynamodb.AttributeType;
import com.sylvona.leona.data.dynamodb.converter.ConverterContext;
import com.sylvona.leona.data.dynamodb.converter.DatabaseItemConverter;

public class EnumConverter<TEnum extends Enum<TEnum>> extends DatabaseItemConverter<TEnum, AttributeType.String> {
    private final Class<TEnum> enumClass;

    public EnumConverter(Class<?> enumType) {
        this.enumClass = (Class<TEnum>) enumType;
    }


    @Override
    public AttributeType.String toDatabase(TEnum tEnum, ConverterContext context) {
        return AttributeType.string(tEnum.toString());
    }

    @Override
    public TEnum toJavaObject(AttributeType.String string, ConverterContext context) {
        return Enum.valueOf(enumClass, string.getValue());
    }
}
