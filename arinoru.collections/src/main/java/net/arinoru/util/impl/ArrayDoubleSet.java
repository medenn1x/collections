package net.arinoru.util.impl;

import net.arinoru.util.DoubleCollection;
import net.arinoru.util.DoubleSet;
import net.arinoru.util.PrimitiveCollections;

import java.util.Arrays;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.DoubleConsumer;
import java.util.function.DoublePredicate;
import java.util.function.IntFunction;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

public class ArrayDoubleSet extends UnmodifiablePrimitiveCollection<Double,double[],
        DoubleConsumer,DoublePredicate,PrimitiveIterator.OfDouble,Spliterator.OfDouble,
        DoubleStream,DoubleCollection> implements DoubleSet {
    private final double[] arr;

    private ArrayDoubleSet(double[] arr) {
        this.arr = arr;
    }

    private static double[] validate(double[] arr) {
        for (int i = 1; i < arr.length; i++)
            for (int j = 0; j < i; j++)
                if (arr[i] == arr[j])
                    throw new IllegalArgumentException("Duplicate element");
        return arr;
    }

    public static DoubleSet fromArray(double[] a) {
        return switch (a.length) {
            case 0 -> EmptyDoubleSet.INSTANCE;
            case 1 -> new DoubleSingleton(a[0]);
            default -> {
                var arr = Arrays.copyOf(a, a.length);
                yield new ArrayDoubleSet(validate(arr));
            }
        };
    }

    public static DoubleSet fromCollection(DoubleCollection collection) {
        // TODO: Validate collection class and avoice defensive copy
        //  for known safe implementations
        var a = collection.toPrimitiveArray();
        return switch (a.length) {
            case 0 -> EmptyDoubleSet.INSTANCE;
            case 1 -> new DoubleSingleton(a[0]);
            default -> {
                var arr = Arrays.copyOf(a, a.length);
                yield new ArrayDoubleSet(validate(arr));
            }
        };
    }

    @Override
    public boolean containsDouble(double element) {
        for (double v : arr)
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
    public PrimitiveIterator.OfDouble iterator() {
        return Spliterators.iterator(spliterator());
    }

    @Override
    public DoubleStream primitiveStream() {
        return DoubleStream.of(arr);
    }

    @Override
    public int size() {
        return arr.length;
    }

    @Override
    public Spliterator.OfDouble spliterator() {
        return Spliterators.spliterator(arr, Spliterator.IMMUTABLE |
                Spliterator.ORDERED | Spliterator.DISTINCT);
    }

    @Override
    public Stream<Double> stream() {
        return DoubleStream.of(arr).boxed();
    }

    @Override
    public Object[] toArray() {
        return DoubleStream.of(arr).boxed().toArray();
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
            a[i] = (U) Double.valueOf(arr[i]);
        if (a.length > arr.length)
            a[arr.length] = null;
        return a;
    }

    @Override
    public double[] toPrimitiveArray() {
        return Arrays.copyOf(arr, arr.length);
    }
}
