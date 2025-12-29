package net.arinoru.util.impl;

import net.arinoru.util.IntCollection;
import net.arinoru.util.IntSet;

import java.util.Collection;
import java.util.PrimitiveIterator;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.function.IntPredicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class IntSingleton extends PrimitiveSingleton<Integer,int[],IntConsumer,
        IntPredicate,PrimitiveIterator.OfInt,Spliterator.OfInt,IntStream,IntCollection>
        implements IntSet {
    private final int value;

    public IntSingleton(int value) {
        this.value = value;
    }

    @Override
    public boolean contains(Object o) {
        return o instanceof Integer i && value == i;
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        if (collection instanceof IntCollection integers)
            return containsAll(integers);
        return switch (collection.size()) {
            case 0 -> true;
            case 1 -> collection.contains(value);
            default -> false;
        };
    }

    @Override
    public boolean containsAll(IntCollection collection) {
        return switch (collection.size()) {
            case 0 -> true;
            case 1 -> collection.containsInt(value);
            default -> false;
        };
    }

    @Override
    public boolean containsInt(int element) {
        return value == element;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        return switch (o) {
            case IntSet set -> set.size() == 1 && set.containsInt(value);
            case Set<?> set -> set.size() == 1 && set.contains(value);
            case null, default -> false;
        };
    }

    @Override
    public void forEach(Consumer<? super Integer> action) {
        action.accept(value);
    }

    @Override
    public void forEach(IntConsumer action) {
        action.accept(value);
    }

    @Override
    public int hashCode() {
        return value;
    }

    @Override
    public PrimitiveIterator.OfInt iterator() {
        return Spliterators.iterator(spliterator());
    }

    @Override
    public IntStream primitiveStream() {
        return IntStream.of(value);
    }

    @Override
    public Spliterator.OfInt spliterator() {
        return new SingletonIntSpliterator(value);
    }

    @Override
    public Stream<Integer> stream() {
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
        a[0] = (U) Integer.valueOf(value);
        if (a.length > 1)
            a[1] = null;
        return a;
    }

    @Override
    public int[] toPrimitiveArray() {
        return new int[] { value };
    }
}
