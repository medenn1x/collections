package net.arinoru.util.impl;

import net.arinoru.util.DoubleCollection;
import net.arinoru.util.DoubleSet;

import java.util.Collection;
import java.util.PrimitiveIterator;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.DoublePredicate;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

public class DoubleSingleton extends PrimitiveSingleton<Double,double[],DoubleConsumer,
        DoublePredicate,PrimitiveIterator.OfDouble,Spliterator.OfDouble,DoubleStream,
        DoubleCollection> implements DoubleSet {
    private final double value;

    public DoubleSingleton(double value) {
        this.value = value;
    }

    @Override
    public boolean contains(Object o) {
        return o instanceof Double d && value == d;
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        if (collection instanceof DoubleCollection doubles)
            return containsAll(doubles);
        return switch (collection.size()) {
            case 0 -> true;
            case 1 -> collection.contains(value);
            default -> false;
        };
    }

    @Override
    public boolean containsAll(DoubleCollection collection) {
        return switch (collection.size()) {
            case 0 -> true;
            case 1 -> collection.containsDouble(value);
            default -> false;
        };
    }

    @Override
    public boolean containsDouble(double d) {
        return value == d;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        return switch (o) {
            case DoubleSet set -> set.size() == 1 && set.containsDouble(value);
            case Set<?> set -> set.size() == 1 && set.contains(value);
            case null, default -> false;
        };
    }

    @Override
    public void forEach(Consumer<? super Double> action) {
        action.accept(value);
    }

    @Override
    public void forEach(DoubleConsumer action) {
        action.accept(value);
    }

    @Override
    public int hashCode() {
        return Double.hashCode(value);
    }

    @Override
    public PrimitiveIterator.OfDouble iterator() {
        return Spliterators.iterator(spliterator());
    }

    @Override
    public DoubleStream primitiveStream() {
        return DoubleStream.of(value);
    }

    @Override
    public Spliterator.OfDouble spliterator() {
        return new SingletonDoubleSpliterator(value);
    }

    @Override
    public Stream<Double> stream() {
        return Stream.of(value);
    }

    @Override
    public Object[] toArray() {
        return new Object[] { value };
    }

    @Override
    @SuppressWarnings("unchecked")
    public <U> U[] toArray(U[] a) {
        if (a.length < 1)
            a = ArrayImpl.newArray(a, 1);
        a[0] = (U) Double.valueOf(value);
        if (a.length > 1)
            a[1] = null;
        return a;
    }

    @Override
    public double[] toPrimitiveArray() {
        return new double[] { value };
    }
}
