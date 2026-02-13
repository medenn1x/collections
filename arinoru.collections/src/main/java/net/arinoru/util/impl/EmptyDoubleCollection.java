package net.arinoru.util.impl;

import net.arinoru.util.DoubleCollection;

import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.DoubleConsumer;
import java.util.function.DoublePredicate;
import java.util.stream.DoubleStream;

/**
 * <p>An internal implementation of an unmodifiable {@link DoubleCollection}
 * containing no elements.</p>
 */
public class EmptyDoubleCollection extends EmptyPrimitiveCollection<Double,double[],
        DoubleConsumer,DoublePredicate,PrimitiveIterator.OfDouble,Spliterator.OfDouble,
        DoubleStream,DoubleCollection> implements DoubleCollection {
    /**
     * <p>A canonical instance of an {@code EmptyDoubleCollection}. As this is
     * completely immutable and has no state, this instance can be reused and
     * shared between threads.</p>
     */
    public static final EmptyDoubleCollection INSTANCE = new EmptyDoubleCollection();

    EmptyDoubleCollection() {}

    /**
     * <p>Returns {@code true} if this collection contains the specified element.</p>
     * @implNote <p>This method always returns {@code false}.</p>
     * @param element element whose presence in this collection is to be tested
     * @return {@code true} if this set contains the specified element
     */
    @Override
    public boolean containsDouble(double element) {
        return false;
    }

    /**
     * <p>Returns a primitive iterator over the elements in this collection.</p>
     * @implNote <p>This method returns an instance of
     * {@link EmptyDoubleIterator}.</p>
     * @return a {@code PrimitiveIterator.OfDouble} over the elements in this
     * collection
     */
    @Override
    public PrimitiveIterator.OfDouble iterator() {
        return EmptyDoubleIterator.INSTANCE;
    }

    /**
     * <p>Returns a sequential {@code DoubleStream} with this collection as its
     * source.</p>
     * @implNote <p>This method returns an empty {@code DoubleStream}.</p>
     * @return a sequential {@code DoubleStream} over the elements in this
     * collection
     * @see DoubleStream#empty()
     */
    @Override
    public DoubleStream primitiveStream() {
        return DoubleStream.empty();
    }

    /**
     * <p>Creates a {@code Spliterator.OfDouble} over the elements in this
     * collection.</p>
     * @implNote <p>This method returns an empty double spliterator.</p>
     * @return a {@code Spliterator.OfDouble} over the elements in this set
     * @see Spliterators#emptyDoubleSpliterator()
     */
    @Override
    public Spliterator.OfDouble spliterator() {
        return Spliterators.emptyDoubleSpliterator();
    }

    /**
     * <p>Returns an array containing the elements in this collection.</p>
     * @implNote <p>This method returns an empty array of doubles.</p>
     * @return an array containing the elements in this collection
     */
    @Override
    public double[] toPrimitiveArray() {
        return new double[0];
    }

    /**
     * <p>Returns an instance of {@code EmptyDoubleCollection}.</p>
     * @return an empty double collection
     */
    public static EmptyDoubleCollection getInstance() {
        return INSTANCE;
    }
}
