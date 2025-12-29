package net.arinoru.util.impl;

import net.arinoru.util.IntCollection;
import net.arinoru.util.IntSet;
import net.arinoru.util.PrimitiveCollections;

import java.util.Arrays;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.IntConsumer;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ArrayIntSet extends UnmodifiablePrimitiveCollection<Integer,int[],
        IntConsumer,IntPredicate,PrimitiveIterator.OfInt,Spliterator.OfInt,IntStream,
        IntCollection> implements IntSet {
    private final int[] arr;

    private ArrayIntSet(int[] arr) {
        this.arr = arr;
    }

    private static int[] validate(int[] arr) {
        for (int i = 1; i < arr.length; i++)
            for (int j = 0; j < i; j++)
                if (arr[i] == arr[j])
                    throw new IllegalArgumentException("Duplicate element");
        return arr;
    }

    public static IntSet fromArray(int[] a) {
        return switch (a.length) {
            case 0 -> EmptyIntSet.INSTANCE;
            case 1 -> new IntSingleton(a[0]);
            default -> new ArrayIntSet(validate(Arrays.copyOf(a, a.length)));
        };
    }

    public static IntSet fromCollection(IntCollection collection) {
        // TODO: Validate collection class and avoid defensive copy
        //  for known safe implementations
        var a = collection.toPrimitiveArray();
        return switch (a.length) {
            case 0 -> EmptyIntSet.INSTANCE;
            case 1 -> new IntSingleton(a[0]);
            default -> new ArrayIntSet(validate(Arrays.copyOf(a, a.length)));
        };
    }

    @Override
    public boolean containsInt(int element) {
        for (int v : arr)
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
    public PrimitiveIterator.OfInt iterator() {
        return Spliterators.iterator(spliterator());
    }

    @Override
    public IntStream primitiveStream() {
        return IntStream.of(arr);
    }

    @Override
    public int size() {
        return arr.length;
    }

    @Override
    public Spliterator.OfInt spliterator() {
        return Spliterators.spliterator(arr, Spliterator.IMMUTABLE |
                Spliterator.ORDERED | Spliterator.DISTINCT);
    }

    @Override
    public Stream<Integer> stream() {
        return IntStream.of(arr).boxed();
    }

    @Override
    public Object[] toArray() {
        return IntStream.of(arr).boxed().toArray();
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
            a[i] = (U) Integer.valueOf(arr[i]);
        if (a.length > arr.length)
            a[arr.length] = null;
        return a;
    }

    @Override
    public int[] toPrimitiveArray() {
        return Arrays.copyOf(arr, arr.length);
    }
}
