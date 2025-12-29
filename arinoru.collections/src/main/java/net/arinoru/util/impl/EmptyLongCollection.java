package net.arinoru.util.impl;

import net.arinoru.util.LongCollection;

import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.LongConsumer;
import java.util.function.LongPredicate;
import java.util.stream.LongStream;

public class EmptyLongCollection extends EmptyPrimitiveCollection<Long,long[],
        LongConsumer,LongPredicate,PrimitiveIterator.OfLong,Spliterator.OfLong,
        LongStream,LongCollection> implements LongCollection {
    public static final EmptyLongCollection INSTANCE = new EmptyLongCollection();

    EmptyLongCollection() {}

    @Override
    public boolean containsAll(LongCollection collection) {
        return collection.isEmpty();
    }

    @Override
    public boolean containsLong(long element) {
        return false;
    }

    @Override
    public PrimitiveIterator.OfLong iterator() {
        return EmptyLongIterator.INSTANCE;
    }

    @Override
    public LongStream primitiveStream() {
        return LongStream.empty();
    }

    @Override
    public Spliterator.OfLong spliterator() {
        return Spliterators.emptyLongSpliterator();
    }

    @Override
    public long[] toPrimitiveArray() {
        return new long[0];
    }

    public static EmptyLongCollection getInstance() {
        return INSTANCE;
    }
}
