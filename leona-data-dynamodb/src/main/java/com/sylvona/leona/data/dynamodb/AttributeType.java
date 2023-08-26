package com.sylvona.leona.data.dynamodb;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.sylvona.leona.core.commons.streams.LINQ;
import com.sylvona.leona.data.dynamodb.converter.ConverterContext;
import lombok.Getter;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Set;
import java.util.function.Function;

public abstract sealed class AttributeType<T> permits AttributeType.String, AttributeType.Number, AttributeType.Binary, AttributeType.Bool, AttributeType.Null, AttributeType.Map, AttributeType.List, AttributeType.SetString, AttributeType.SetNumber, AttributeType.SetBinary {
    protected T value;

    protected AttributeType(T value) {
        this.value = value;
    }

    public static String string(java.lang.String s) {
        return new String(s);
    }

    public static Number numeric(int i) {
        return new Number(java.lang.String.valueOf(i));
    }

    public static Number numeric(long l) {
        return new Number(java.lang.String.valueOf(l));
    }

    public static Number numeric(float f) {
        return new Number(java.lang.String.valueOf(f));
    }

    public static Number numeric(double d) {
        return new Number(java.lang.String.valueOf(d));
    }

    public static Number numeric(BigInteger bigInteger) {
        return new Number(bigInteger.toString());
    }

    public static Binary binary(ByteBuffer byteBuffer) {
        return new Binary(byteBuffer);
    }

    public static Binary binary(byte[] bytes) {
        return new Binary(ByteBuffer.wrap(bytes));
    }

    public static Bool bool(boolean b) {
        return new Bool(b);
    }

    public static Null empty() {
        return new Null();
    }

    public static Map map(java.util.Map<java.lang.String, AttributeValue> map) {
        return new Map(map);
    }

    public static List list(java.util.List<AttributeValue> list) {
        return new List(list);
    }

    public static List list(AttributeValue... values) {
        return new List(java.util.List.of(values));
    }

    public static List list(ConverterContext converterContext, java.util.List<Object> values) {
        return new List(LINQ.stream(values).map(v -> converterContext.getConverterForType(v.getClass()).toDatabase(v, converterContext).attributeValue()).toList());
    }

    public static List list(ConverterContext converterContext, Object[] values) {
        return new List(LINQ.stream(values).map(v -> converterContext.getConverterForType(v.getClass()).toDatabase(v, converterContext).attributeValue()).toList());
    }

    public static SetString setString(Set<java.lang.String> strings) {
        return new SetString(strings);
    }

    public static SetString setString(java.lang.String... strings) {
        return new SetString(Set.of(strings));
    }

    public static SetNumber setInt(Set<Integer> ints) {
        return new SetNumber(LINQ.stream(ints).map(java.lang.String::valueOf).toSet());
    }

    public static SetNumber setInt(Integer... ints) {
        return new SetNumber(LINQ.stream(ints).map(java.lang.String::valueOf).toSet());
    }

    public static SetNumber setLong(Set<Long> longs) {
        return new SetNumber(LINQ.stream(longs).map(java.lang.String::valueOf).toSet());
    }

    public static SetNumber setLong(Long... longs) {
        return new SetNumber(LINQ.stream(longs).map(java.lang.String::valueOf).toSet());
    }

    public static SetNumber setFloat(Set<Float> floats) {
        return new SetNumber(LINQ.stream(floats).map(java.lang.String::valueOf).toSet());
    }

    public static SetNumber setFloat(Float... floats) {
        return new SetNumber(LINQ.stream(floats).map(java.lang.String::valueOf).toSet());
    }

    public static SetNumber setDouble(Set<Double> doubles) {
        return new SetNumber(LINQ.stream(doubles).map(java.lang.String::valueOf).toSet());
    }

    public static SetNumber setDouble(Double... doubles) {
        return new SetNumber(LINQ.stream(doubles).map(java.lang.String::valueOf).toSet());
    }

    public static SetNumber setBigInteger(Set<BigInteger> bigIntegers) {
        return new SetNumber(LINQ.stream(bigIntegers).map(java.lang.String::valueOf).toSet());
    }

    public static SetNumber setBigInteger(BigInteger... bigIntegers) {
        return new SetNumber(LINQ.stream(bigIntegers).map(java.lang.String::valueOf).toSet());
    }

    public static SetBinary setBinary(Set<ByteBuffer> byteBuffers) {
        return new SetBinary(byteBuffers);
    }

    public static SetBinary setBinary(ByteBuffer... buffers) {
        return new SetBinary(Set.of(buffers));
    }

    public static SetBinary setBytes(Set<byte[]> bytes) {
        return new SetBinary(LINQ.stream(bytes).map(ByteBuffer::wrap).toSet());
    }

    public static SetBinary setBytes(byte[]... bytes) {
        return new SetBinary(LINQ.stream(bytes).map(ByteBuffer::wrap).toSet());
    }

    public abstract AttributeValue attributeValue();
    
    public T getValue() {
        return value;
    }

    public <UT> UT getValue(Function<T, UT> converter) {
        return converter.apply(value);
    }

    @Getter
    public static final class String extends AttributeType<java.lang.String> {
        private String(java.lang.String value) {
            super(value);
        }

        String(AttributeValue attributeValue) {
            super(attributeValue.getS());
        }

        @Override
        public AttributeValue attributeValue() {
            return new AttributeValue(value);
        }
    }

    @Getter
    public static final class Number extends AttributeType<java.lang.String> {
        private Number(java.lang.String value) {
            super(value);
        }

        Number(AttributeValue attributeValue) {
            super(attributeValue.getN());
        }

        @Override
        public AttributeValue attributeValue() {
            return new AttributeValue().withN(value);
        }

        public long toLong() {
            return getValue(Long::parseLong);
        }

        public int toInt() {
            return getValue(Integer::parseInt);
        }

        public float toFloat() {
            return getValue(Float::parseFloat);
        }

        public double toDouble() {
            return getValue(Double::parseDouble);
        }

        public BigInteger toBigInt() {
            return getValue(BigInteger::new);
        }
    }

    @Getter
    public static final class Binary extends AttributeType<ByteBuffer> {
        private Binary(ByteBuffer value) {
            super(value);
        }

        Binary(AttributeValue attributeValue) {
            super(attributeValue.getB());
        }

        @Override
        public AttributeValue attributeValue() {
            return new AttributeValue().withB(value);
        }
    }

    @Getter
    public static final class Bool extends AttributeType<Boolean> {
        private Bool(Boolean value) {
            super(value);
        }

        Bool(AttributeValue attributeValue) {
            super(attributeValue.getBOOL());
        }

        @Override
        public AttributeValue attributeValue() {
            return new AttributeValue().withBOOL(value);
        }
    }

    public static final class Null extends AttributeType<java.lang.String> {
        Null() {
            super(null);
        }

        Null(AttributeValue attributeValue) {
            super(null);
        }

        @Override
        public AttributeValue attributeValue() {
            return new AttributeValue().withNULL(true);
        }

        @Override
        public java.lang.String getValue() {
            return null;
        }
    }

    @Getter
    public static final class Map extends AttributeType<java.util.Map<java.lang.String, AttributeValue>> {
        private Map(java.util.Map<java.lang.String, AttributeValue> value) {
            super(value);
        }

        Map(AttributeValue attributeValue) {
            super(attributeValue.getM());
        }

        @Override
        public AttributeValue attributeValue() {
            return new AttributeValue().withM(value);
        }
    }

    @Getter
    public static final class List extends AttributeType<java.util.List<AttributeValue>> {
        private List(java.util.List<AttributeValue> value) {
            super(value);
        }

        List(AttributeValue attributeValue) {
            super(attributeValue.getL());
        }

        @Override
        public AttributeValue attributeValue() {
            return new AttributeValue().withL(value);
        }
    }

    @Getter
    public static final class SetString extends AttributeType<Set<java.lang.String>> {
        private SetString(Set<java.lang.String> value) {
            super(value);
        }

        SetString(AttributeValue attributeValue) {
            super(LINQ.stream(attributeValue.getSS()).toSet());
        }

        @Override
        public AttributeValue attributeValue() {
            return new AttributeValue().withSS(value);
        }
    }

    @Getter
    public static final class SetNumber extends AttributeType<Set<java.lang.String>> {
        private SetNumber(Set<java.lang.String> value) {
            super(value);
        }

        SetNumber(AttributeValue attributeValue) {
            super(LINQ.stream(attributeValue.getNS()).toSet());
        }

        @Override
        public AttributeValue attributeValue() {
            return new AttributeValue().withNS(value);
        }
    }

    @Getter
    public static final class SetBinary extends AttributeType<Set<ByteBuffer>> {
        private SetBinary(Set<ByteBuffer> value) {
            super(value);
        }

        SetBinary(AttributeValue attributeValue) {
            super(LINQ.stream(attributeValue.getBS()).toSet());
        }

        @Override
        public AttributeValue attributeValue() {
            return new AttributeValue().withBS(value);
        }
    }

}
