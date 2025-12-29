package net.arinoru.util.impl;

import net.arinoru.util.IntCollection;

import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.IntConsumer;
import java.util.function.IntPredicate;
import java.util.stream.IntStream;

public class EmptyIntCollection extends EmptyPrimitiveCollection<Integer,int[],
        IntConsumer,IntPredicate,PrimitiveIterator.OfInt,Spliterator.OfInt,IntStream,
        IntCollection> implements IntCollection {
    public static final EmptyIntCollection INSTANCE = new EmptyIntCollection();

    EmptyIntCollection() {}

    @Override
    public boolean containsAll(IntCollection collection) {
        return collection.isEmpty();
    }

    @Override
    public boolean containsInt(int element) {
        return false;
    }

    @Override
    public PrimitiveIterator.OfInt iterator() {
        return EmptyIntIterator.INSTANCE;
    }

    @Override
    public IntStream primitiveStream() {
        return IntStream.empty();
    }

    @Override
    public Spliterator.OfInt spliterator() {
        return Spliterators.emptyIntSpliterator();
    }

    @Override
    public int[] toPrimitiveArray() {
        return new int[0];
    }

    public static EmptyIntCollection getInstance() {
        return INSTANCE;
    }
}
