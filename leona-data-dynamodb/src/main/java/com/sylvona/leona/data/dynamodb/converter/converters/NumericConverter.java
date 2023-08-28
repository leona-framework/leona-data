package com.sylvona.leona.data.dynamodb.converter.converters;

import com.sylvona.leona.data.dynamodb.AttributeType;
import com.sylvona.leona.data.dynamodb.converter.ConverterRegistryView;
import com.sylvona.leona.data.dynamodb.converter.DatabaseItemConverter;

import java.math.BigInteger;

/**
 * Abstract class that serves as the basis for converting java objects to/from Dynamo DB {@link AttributeType.Number Numbers}.
 * @param <NumberType>
 */
public abstract class NumericConverter<NumberType> extends DatabaseItemConverter<NumberType, AttributeType.Number> {
    /**
     * Converts an integer to/from a DynamoDB {@link AttributeType.Number}.
     */
    public static class IntegerConverter extends NumericConverter<Integer> {

        @Override
        public AttributeType.Number toDatabase(Integer integer, ConverterRegistryView registry) {
            return AttributeType.numeric(integer);
        }

        @Override
        public Integer toJavaObject(AttributeType.Number number, ConverterRegistryView registry) {
            return number.toInt();
        }
    }

    /**
     * Converts a long to/from a DynamoDB {@link AttributeType.Number}.
     */
    public static class LongConverter extends NumericConverter<Long> {

        @Override
        public AttributeType.Number toDatabase(Long aLong, ConverterRegistryView registry) {
            return AttributeType.numeric(aLong);
        }

        @Override
        public Long toJavaObject(AttributeType.Number number, ConverterRegistryView registry) {
            return number.toLong();
        }
    }

    /**
     * Converts a float to/from a DynamoDB {@link AttributeType.Number}.
     */
    public static class FloatConverter extends NumericConverter<Float> {

        @Override
        public AttributeType.Number toDatabase(Float aFloat, ConverterRegistryView registry) {
            return AttributeType.numeric(aFloat);
        }

        @Override
        public Float toJavaObject(AttributeType.Number number, ConverterRegistryView registry) {
            return number.toFloat();
        }
    }

    /**
     * Converts a double to/from a DynamoDB {@link AttributeType.Number}.
     */
    public static class DoubleConverter extends NumericConverter<Double> {

        @Override
        public AttributeType.Number toDatabase(Double aDouble, ConverterRegistryView registry) {
            return AttributeType.numeric(aDouble);
        }

        @Override
        public Double toJavaObject(AttributeType.Number number, ConverterRegistryView registry) {
            return number.toDouble();
        }
    }

    /**
     * Converts a {@link BigInteger} to/from a DynamoDB {@link AttributeType.Number}.
     */
    public static class BigIntegerConverter extends NumericConverter<BigInteger> {

        @Override
        public AttributeType.Number toDatabase(BigInteger bigInteger, ConverterRegistryView registry) {
            return AttributeType.numeric(bigInteger);
        }

        @Override
        public BigInteger toJavaObject(AttributeType.Number number, ConverterRegistryView registry) {
            return number.toBigInt();
        }
    }
}
