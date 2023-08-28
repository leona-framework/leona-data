package com.sylvona.leona.data.dynamodb;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.sylvona.leona.core.commons.streams.LINQ;
import com.sylvona.leona.data.dynamodb.converter.ConverterRegistryView;
import lombok.Getter;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Set;
import java.util.function.Function;

/**
 * The base class for all basic Dynamo DB data types.
 * @param <T> the java representation of the Dynamo DB type.
 * @see AttributeType.String
 * @see AttributeType.Number
 * @see AttributeType.Binary
 * @see AttributeType.Bool
 * @see AttributeType.Null
 */
public abstract sealed class AttributeType<T> permits AttributeType.String, AttributeType.Number, AttributeType.Binary, AttributeType.Bool, AttributeType.Null, AttributeType.Map, AttributeType.List, AttributeType.SetString, AttributeType.SetNumber, AttributeType.SetBinary {
    /**
     * The underlying java value for the Dynamo DB AttributeType.
     */
    protected T value;

    /**
     * Constructs the Dynamo DB {@link AttributeType} with the given value.
     * @param value the value of the {@code AttributeType}.
     */
    protected AttributeType(T value) {
        this.value = value;
    }

    /**
     * Converts a java string into a Dynamo DB {@link String} type.
     * @param s the java string to convert
     * @return a new DynamoDB String representing the input string.
     */
    public static String string(java.lang.String s) {
        return new String(s);
    }

    /**
     * Converts a java integer into a Dynamo DB {@link Number} type.
     * @param i the java integer to convert
     * @return a new Number representing the integer.
     */
    public static Number numeric(int i) {
        return new Number(java.lang.String.valueOf(i));
    }

    /**
     * Converts a java long into a Dynamo DB {@link Number} type.
     * @param l the java long to convert
     * @return a new Number representing the long.
     */
    public static Number numeric(long l) {
        return new Number(java.lang.String.valueOf(l));
    }

    /**
     * Converts a java float into a Dynamo DB {@link Number} type.
     * @param f the java float to convert
     * @return a new Number representing the float.
     */
    public static Number numeric(float f) {
        return new Number(java.lang.String.valueOf(f));
    }

    /**
     * Converts a java double into a Dynamo DB {@link Number} type.
     * @param d the java double to convert
     * @return a new Number representing the double.
     */
    public static Number numeric(double d) {
        return new Number(java.lang.String.valueOf(d));
    }

    /**
     * Converts a java BigInteger into a Dynamo DB {@link Number} type.
     * @param bigInteger the java BigInteger to convert
     * @return a new Number representing the BigInteger.
     */
    public static Number numeric(BigInteger bigInteger) {
        return new Number(bigInteger.toString());
    }

    /**
     * Converts a java byteBuffer into a Dynamo DB {@link Binary} type.
     * @param byteBuffer the java byteBuffer to convert
     * @return a new Binary representing the byteBuffer.
     */
    public static Binary binary(ByteBuffer byteBuffer) {
        return new Binary(byteBuffer);
    }

    /**
     * Converts a byte array into a Dynamo DB {@link Binary} type.
     * @param bytes the byte array to convert
     * @return a new Binary representing the byte array.
     */
    public static Binary binary(byte[] bytes) {
        return new Binary(ByteBuffer.wrap(bytes));
    }

    /**
     * Converts a java boolean into a Dynamo DB {@link Bool} type.
     * @param b the java boolean to convert
     * @return a new Bool representing the boolean.
     */
    public static Bool bool(boolean b) {
        return new Bool(b);
    }

    /**
     * Returns a Dynamo DB null type.
     * @return a new Dynamo DB null type.
     */
    public static Null empty() {
        return new Null();
    }

    /**
     * Converts a java map into a Dynamo DB {@link Map} type.
     * @param map the java map to convert
     * @return a new Map representing the map.
     */
    public static Map map(java.util.Map<java.lang.String, AttributeValue> map) {
        return new Map(map);
    }

    /**
     * Converts a java list of {@link AttributeValue} into a Dynamo DB {@link List} type.
     * @param list the list to convert
     * @return a new List representing the list of AttributeValues.
     */
    public static List list(java.util.List<AttributeValue> list) {
        return new List(list);
    }

    /**
     * Converts a collection of {@link AttributeValue} into a Dynamo DB {@link List} type.
     * @param values the values to convert
     * @return a new List representing the collection of AttributeValues.
     */
    public static List list(AttributeValue... values) {
        return new List(java.util.List.of(values));
    }

    /**
     * Converts a java list of objects into a Dynamo DB {@link List} type.
     * @param converterRegistryView the convert context
     * @param values the list of objects to convert
     * @return a new List with all converted objects.
     */
    public static List list(ConverterRegistryView converterRegistryView, java.util.List<Object> values) {
        return new List(LINQ.stream(values).map(v -> converterRegistryView.getConverterForType(v.getClass()).toDatabase(v, converterRegistryView).attributeValue()).toList());
    }

    /**
     * Converts a collection of objects into a Dynamo DB {@link List} type.
     * @param converterRegistryView the convert context
     * @param values the collections of objects to convert
     * @return a new List with all converted objects.
     */
    public static List list(ConverterRegistryView converterRegistryView, Object[] values) {
        return new List(LINQ.stream(values).map(v -> converterRegistryView.getConverterForType(v.getClass()).toDatabase(v, converterRegistryView).attributeValue()).toList());
    }

    /**
     * Converts a java String set into a Dynamo DB {@link SetString} type.
     * @param strings the java set to convert
     * @return a new SetString representing the set of strings.
     */
    public static SetString setString(Set<java.lang.String> strings) {
        return new SetString(strings);
    }

    /**
     * Converts a collections of String into a Dynamo DB {@link SetString} type.
     * @param strings the collections of Strings to convert
     * @return a new SetString representing the collection of strings.
     */
    public static SetString setString(java.lang.String... strings) {
        return new SetString(Set.of(strings));
    }

    /**
     * Converts a java Integer set into a Dynamo DB {@link SetNumber} type.
     * @param ints the java set to convert
     * @return a new SetNumber representing the set of integers.
     */
    public static SetNumber setInt(Set<Integer> ints) {
        return new SetNumber(LINQ.stream(ints).map(java.lang.String::valueOf).toSet());
    }

    /**
     * Converts a collections of Integers into a Dynamo DB {@link SetNumber} type.
     * @param ints the collections of Integers to convert
     * @return a new SetNumber representing the collection of integers.
     */
    public static SetNumber setInt(Integer... ints) {
        return new SetNumber(LINQ.stream(ints).map(java.lang.String::valueOf).toSet());
    }

    /**
     * Converts a java Long set into a Dynamo DB {@link SetNumber} type.
     * @param longs the java set to convert
     * @return a new SetNumber representing the set of longs.
     */
    public static SetNumber setLong(Set<Long> longs) {
        return new SetNumber(LINQ.stream(longs).map(java.lang.String::valueOf).toSet());
    }

    /**
     * Converts a collections of Longs into a Dynamo DB {@link SetNumber} type.
     * @param longs the collections of Longs to convert
     * @return a new SetNumber representing the collection of longs.
     */
    public static SetNumber setLong(Long... longs) {
        return new SetNumber(LINQ.stream(longs).map(java.lang.String::valueOf).toSet());
    }

    /**
     * Converts a java Float set into a Dynamo DB {@link SetNumber} type.
     * @param floats the java set to convert
     * @return a new SetNumber representing the set of floats.
     */
    public static SetNumber setFloat(Set<Float> floats) {
        return new SetNumber(LINQ.stream(floats).map(java.lang.String::valueOf).toSet());
    }

    /**
     * Converts a collections of Floats into a Dynamo DB {@link SetNumber} type.
     * @param floats the collections of Floats to convert
     * @return a new SetNumber representing the collection of floats.
     */
    public static SetNumber setFloat(Float... floats) {
        return new SetNumber(LINQ.stream(floats).map(java.lang.String::valueOf).toSet());
    }

    /**
     * Converts a java Double set into a Dynamo DB {@link SetNumber} type.
     * @param doubles the java set to convert
     * @return a new SetNumber representing the set of doubles.
     */
    public static SetNumber setDouble(Set<Double> doubles) {
        return new SetNumber(LINQ.stream(doubles).map(java.lang.String::valueOf).toSet());
    }

    /**
     * Converts a collections of Doubles into a Dynamo DB {@link SetNumber} type.
     * @param doubles collections of Doubles to convert
     * @return a new SetNumber representing the collection of doubles.
     */
    public static SetNumber setDouble(Double... doubles) {
        return new SetNumber(LINQ.stream(doubles).map(java.lang.String::valueOf).toSet());
    }

    /**
     * Converts a java BigInteger set into a Dynamo DB {@link SetNumber} type.
     * @param bigIntegers the java set to convert
     * @return a new SetNumber representing the set of big integers.
     */
    public static SetNumber setBigInteger(Set<BigInteger> bigIntegers) {
        return new SetNumber(LINQ.stream(bigIntegers).map(java.lang.String::valueOf).toSet());
    }

    /**
     * Converts a collections of BigIntegers into a Dynamo DB {@link SetNumber} type.
     * @param bigIntegers collections of BigIntegers to convert
     * @return a new SetNumber representing the collection of big integers.
     */
    public static SetNumber setBigInteger(BigInteger... bigIntegers) {
        return new SetNumber(LINQ.stream(bigIntegers).map(java.lang.String::valueOf).toSet());
    }

    /**
     * Converts a java ByteBuffer set into a Dynamo DB {@link SetBinary} type.
     * @param byteBuffers the java set to convert
     * @return a new SetBinary representing the set of byte buffers.
     */
    public static SetBinary setBinary(Set<ByteBuffer> byteBuffers) {
        return new SetBinary(byteBuffers);
    }

    /**
     * Converts a collections of ByteBuffers into a Dynamo DB {@link SetBinary} type.
     * @param buffers collections of ByteBuffers to convert
     * @return a new SetBinary representing the collection of byte buffers.
     */
    public static SetBinary setBinary(ByteBuffer... buffers) {
        return new SetBinary(Set.of(buffers));
    }

    /**
     * Converts a set of byte arrays into a Dynamo DB {@link SetBinary} type.
     * @param bytes the java byte array set to convert
     * @return a new SetBinary representing the set of byte arrays.
     */
    public static SetBinary setBytes(Set<byte[]> bytes) {
        return new SetBinary(LINQ.stream(bytes).map(ByteBuffer::wrap).toSet());
    }

    /**
     * Converts a collections of byte arrays into a Dynamo DB {@link SetBinary} type.
     * @param bytes collections of byte arrays to convert
     * @return a new SetBinary representing the collection of byte arrays.
     */
    public static SetBinary setBytes(byte[]... bytes) {
        return new SetBinary(LINQ.stream(bytes).map(ByteBuffer::wrap).toSet());
    }


    /**
     * Returns a {@link AttributeValue} representation of the given {@code AttributeType}.
     * @return the value of the {@code AttributeType} as an {@link AttributeValue}.
     */
    public abstract AttributeValue attributeValue();

    /**
     * Returns the value of the {@link AttributeType}.
     * @return the current value of the {@code AttributeType}.
     */
    public T getValue() {
        return value;
    }

    /**
     * Converts then returns the value of the {@link AttributeType}.
     * @param converter the function to apply to the value.
     * @return the converted value of the {@code AttributeType}.
     * @param <UT> the expected return type.
     */
    public <UT> UT getValue(Function<T, UT> converter) {
        return converter.apply(value);
    }

    /**
     * Represents a Dynamo DB String data type.
     */
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

    /**
     * Represents a Dynamo DB Number data type.
     */
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

        /**
         * Returns the inner value as a long.
         * @return the instance's value as a long.
         */
        public long toLong() {
            return getValue(Long::parseLong);
        }

        /**
         * Returns the inner value as an int.
         * @return the instance's value as an int.
         */
        public int toInt() {
            return getValue(Integer::parseInt);
        }

        /**
         * Returns the inner value as a float.
         * @return the instance's value as a float.
         */
        public float toFloat() {
            return getValue(Float::parseFloat);
        }

        /**
         * Returns the inner value as a double.
         * @return the instance's value as a double.
         */
        public double toDouble() {
            return getValue(Double::parseDouble);
        }

        /**
         * Returns the inner value as a {@link BigInteger}.
         * @return the instance's value as a {@code BigInteger}.
         */
        public BigInteger toBigInt() {
            return getValue(BigInteger::new);
        }
    }

    /**
     * Represents a Dynamo DB Binary data type.
     */
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

    /**
     * Represents a Dynamo DB Bool data type.
     */
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


    /**
     * Represents a Dynamo DB Null data type.
     */
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

    /**
     * Represents a Dynamo DB Map data type.
     */
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

    /**
     * Represents a Dynamo DB List data type.
     */
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

    /**
     * Represents a Dynamo DB String Set data type.
     */
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

    /**
     * Represents a Dynamo DB Number set data type.
     */
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

    /**
     * Represents a Dynamo DB Binary set data type.
     */
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
