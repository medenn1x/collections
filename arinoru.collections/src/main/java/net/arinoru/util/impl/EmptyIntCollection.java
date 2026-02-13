package net.arinoru.util.impl;

import net.arinoru.util.IntCollection;

import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.IntConsumer;
import java.util.function.IntPredicate;
import java.util.stream.IntStream;

/**
 * <p>An internal implementation of an unmodifiable {@link IntCollection}
 * containing no elements.</p>
 */
public class EmptyIntCollection extends EmptyPrimitiveCollection<Integer,int[],
        IntConsumer,IntPredicate,PrimitiveIterator.OfInt,Spliterator.OfInt,IntStream,
        IntCollection> implements IntCollection {
    /**
     * <p>A canonical instance of an {@code EmptyIntCollection}. As this is
     * completely immutable an has no state, this instance can be reused and
     * shared between threads.</p>
     */
    public static final EmptyIntCollection INSTANCE = new EmptyIntCollection();

    EmptyIntCollection() {}

    /**
     * <p>Returns {@code true} if this collection contains the specified element.</p>
     * @implNote <p>This method always returns {@code false}.</p>
     * @param element element whose presence in this collection is to be tested
     * @return {@code true} if this set contains the specified element
     */
    @Override
    public boolean containsInt(int element) {
        return false;
    }

    /**
     * <p>Returns a primitive iterator over the elements in this collection.</p>
     * @implNote <p>This method returns an instance of
     * {@link EmptyIntIterator}.</p>
     * @return a {@code PrimitiveIterator.OfInt} over the elements in this
     * collection
     */
    @Override
    public PrimitiveIterator.OfInt iterator() {
        return EmptyIntIterator.INSTANCE;
    }

    /**
     * <p>Returns a sequential {@code IntStream} with this collection as its
     * source.</p>
     * @implNote <p>This method returns an empty {@code IntStream}.</p>
     * @return a sequential {@code DoubleStream} over the elements in this
     * collection
     * @see IntStream#empty()
     */
    @Override
    public IntStream primitiveStream() {
        return IntStream.empty();
    }

    /**
     * <p>Creates a {@code Spliterator.OfInt} over the elements in this
     * collection.</p>
     * @implNote <p>This method returns an empty int spliterator.</p>
     * @return a {@code Spliterator.OfInt} over the elements in this set
     * @see Spliterators#emptyIntSpliterator()
     */
    @Override
    public Spliterator.OfInt spliterator() {
        return Spliterators.emptyIntSpliterator();
    }

    /**
     * <p>Returns an array containing the elements in this collection.</p>
     * @implNote <p>This method returns an empty array of doubles.</p>
     * @return an array containing the elements in this collection
     */
    @Override
    public int[] toPrimitiveArray() {
        return new int[0];
    }

    /**
     * <p>Returns an instance of {@code EmptyIntCollection}.</p>
     * @return an empty int collection
     */
    public static EmptyIntCollection getInstance() {
        return INSTANCE;
    }
}
