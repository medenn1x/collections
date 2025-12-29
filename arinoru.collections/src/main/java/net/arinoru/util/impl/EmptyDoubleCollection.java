package net.arinoru.util.impl;

import net.arinoru.util.DoubleCollection;

import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.DoubleConsumer;
import java.util.function.DoublePredicate;
import java.util.stream.DoubleStream;

public class EmptyDoubleCollection extends EmptyPrimitiveCollection<Double,double[],
        DoubleConsumer,DoublePredicate,PrimitiveIterator.OfDouble,Spliterator.OfDouble,
        DoubleStream,DoubleCollection> implements DoubleCollection {
    public static final EmptyDoubleCollection INSTANCE = new EmptyDoubleCollection();

    EmptyDoubleCollection() {}

    @Override
    public boolean containsAll(DoubleCollection collection) {
        return collection.isEmpty();
    }

    @Override
    public boolean containsDouble(double element) {
        return false;
    }

    @Override
    public PrimitiveIterator.OfDouble iterator() {
        return EmptyDoubleIterator.INSTANCE;
    }

    @Override
    public DoubleStream primitiveStream() {
        return DoubleStream.empty();
    }

    @Override
    public Spliterator.OfDouble spliterator() {
        return Spliterators.emptyDoubleSpliterator();
    }

    @Override
    public double[] toPrimitiveArray() {
        return new double[0];
    }

    public static EmptyDoubleCollection getInstance() {
        return INSTANCE;
    }
}
