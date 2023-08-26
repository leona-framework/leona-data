package com.sylvona.leona.data.dynamodb.converter.converters;

import com.sylvona.leona.data.dynamodb.AttributeType;
import com.sylvona.leona.data.dynamodb.converter.ConverterContext;
import com.sylvona.leona.data.dynamodb.converter.DatabaseItemConverter;

import java.math.BigInteger;

public abstract class NumericConverter<NumberType> extends DatabaseItemConverter<NumberType, AttributeType.Number> {
    public static class IntegerConverter extends NumericConverter<Integer> {

        @Override
        public AttributeType.Number toDatabase(Integer integer, ConverterContext context) {
            return AttributeType.numeric(integer);
        }

        @Override
        public Integer toJavaObject(AttributeType.Number number, ConverterContext context) {
            return number.toInt();
        }
    }

    public static class LongConverter extends NumericConverter<Long> {

        @Override
        public AttributeType.Number toDatabase(Long aLong, ConverterContext context) {
            return AttributeType.numeric(aLong);
        }

        @Override
        public Long toJavaObject(AttributeType.Number number, ConverterContext context) {
            return number.toLong();
        }
    }

    public static class FloatConverter extends NumericConverter<Float> {

        @Override
        public AttributeType.Number toDatabase(Float aFloat, ConverterContext context) {
            return AttributeType.numeric(aFloat);
        }

        @Override
        public Float toJavaObject(AttributeType.Number number, ConverterContext context) {
            return number.toFloat();
        }
    }

    public static class DoubleConverter extends NumericConverter<Double> {

        @Override
        public AttributeType.Number toDatabase(Double aDouble, ConverterContext context) {
            return AttributeType.numeric(aDouble);
        }

        @Override
        public Double toJavaObject(AttributeType.Number number, ConverterContext context) {
            return number.toDouble();
        }
    }

    public static class BigIntegerConverter extends NumericConverter<BigInteger> {

        @Override
        public AttributeType.Number toDatabase(BigInteger bigInteger, ConverterContext context) {
            return AttributeType.numeric(bigInteger);
        }

        @Override
        public BigInteger toJavaObject(AttributeType.Number number, ConverterContext context) {
            return number.toBigInt();
        }
    }
}
