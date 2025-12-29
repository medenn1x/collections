package net.arinoru.util.impl;

import net.arinoru.util.LongCollection;
import net.arinoru.util.LongSet;
import net.arinoru.util.PrimitiveCollections;

import java.util.Arrays;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.IntFunction;
import java.util.function.LongConsumer;
import java.util.function.LongPredicate;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class ArrayLongSet extends UnmodifiablePrimitiveCollection<Long,long[]
        ,LongConsumer,LongPredicate,PrimitiveIterator.OfLong,Spliterator.OfLong,
        LongStream,LongCollection> implements LongSet {
    private final long[] arr;

    private ArrayLongSet(long[] arr) {
        this.arr = arr;
    }

    private static long[] validate(long[] arr) {
        for (int i = 1; i < arr.length; i++)
            for (int j = 0; j < i; j++)
                if (arr[i] == arr[j])
                    throw new IllegalArgumentException("Duplicate element");
        return arr;
    }

    public static LongSet fromArray(long[] a) {
        return switch (a.length) {
            case 0 -> EmptyLongSet.INSTANCE;
            case 1 -> new LongSingleton(a[0]);
            default -> new ArrayLongSet(validate(Arrays.copyOf(a, a.length)));
        };
    }

    public static LongSet fromCollection(LongCollection collection) {
        // TODO: Validate collection class and avoid defensive copy
        //  for known safe implementations
        var a = collection.toPrimitiveArray();
        return switch (a.length) {
            case 0 -> EmptyLongSet.INSTANCE;
            case 1 -> new LongSingleton(a[0]);
            default -> new ArrayLongSet(validate(Arrays.copyOf(a, a.length)));
        };
    }

    @Override
    public boolean containsLong(long element) {
        for (long v : arr)
            if (v == element)
                return true;
        return false;
    }

    @Override
    @SuppressWarnings("EqualsDoesntCheckParameterClass")
    public boolean equals(Object o) {
        return PrimitiveCollections.equals(this, o);
    }

    @Override
    public int hashCode() {
        return PrimitiveCollections.hashCode(this);
    }

    @Override
    public PrimitiveIterator.OfLong iterator() {
        return Spliterators.iterator(spliterator());
    }

    @Override
    public LongStream primitiveStream() {
        return LongStream.of(arr);
    }

    @Override
    public int size() {
        return arr.length;
    }

    @Override
    public Spliterator.OfLong spliterator() {
        return Spliterators.spliterator(arr, Spliterator.IMMUTABLE |
                Spliterator.ORDERED | Spliterator.DISTINCT);
    }

    @Override
    public Stream<Long> stream() {
        return LongStream.of(arr).boxed();
    }

    @Override
    public Object[] toArray() {
        return LongStream.of(arr).boxed().toArray();
    }

    @Override
    public <U> U[] toArray(IntFunction<U[]> generator) {
        return toArray(generator.apply(arr.length));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <U> U[] toArray(U[] a) {
        if (a.length < arr.length)
            a = ArrayImpl.newArray(a, arr.length);
        for (int i = 0; i < arr.length; i++)
            a[i] = (U) Long.valueOf(arr[i]);
        if (a.length > arr.length)
            a[arr.length] = null;
        return a;
    }

    @Override
    public long[] toPrimitiveArray() {
        return Arrays.copyOf(arr, arr.length);
    }
}
