package net.arinoru.util.impl;

import net.arinoru.util.LongCollection;
import net.arinoru.util.LongSet;

import java.util.Collection;
import java.util.PrimitiveIterator;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.function.LongConsumer;
import java.util.function.LongPredicate;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class LongSingleton extends PrimitiveSingleton<Long,long[],LongConsumer,
        LongPredicate,PrimitiveIterator.OfLong,Spliterator.OfLong,LongStream,
        LongCollection> implements LongSet {
    private final long value;

    public LongSingleton(long value) {
        this.value = value;
    }

    @Override
    public boolean contains(Object o) {
        return o instanceof Long l && value == l;
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        if (collection instanceof LongCollection longs)
            return containsAll(longs);
        return switch (collection.size()) {
            case 0 -> true;
            case 1 -> collection.contains(value);
            default -> false;
        };
    }

    @Override
    public boolean containsAll(LongCollection collection) {
        return switch (collection.size()) {
            case 0 -> true;
            case 1 -> collection.containsLong(value);
            default -> false;
        };
    }

    @Override
    public boolean containsLong(long element) {
        return value == element;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        return switch (o) {
            case LongSet set -> set.size() == 1 && set.containsLong(value);
            case Set<?> set -> set.size() == 1 && set.contains(value);
            case null, default -> false;
        };
    }

    @Override
    public void forEach(Consumer<? super Long> action) {
        action.accept(value);
    }

    @Override
    public void forEach(LongConsumer action) {
        action.accept(value);
    }

    @Override
    public int hashCode() {
        return Long.hashCode(value);
    }

    @Override
    public PrimitiveIterator.OfLong iterator() {
        return Spliterators.iterator(spliterator());
    }

    @Override
    public LongStream primitiveStream() {
        return LongStream.of(value);
    }

    @Override
    public Spliterator.OfLong spliterator() {
        return new SingletonLongSpliterator(value);
    }

    @Override
    public Stream<Long> stream() {
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
        a[0] = (U) Long.valueOf(value);
        if (a.length > 1)
            a[1] = null;
        return a;
    }

    @Override
    public long[] toPrimitiveArray() {
        return new long[] { value };
    }
}
