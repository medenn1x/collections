package net.arinoru.util.impl;

import net.arinoru.util.LongCollection;

import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.LongConsumer;
import java.util.function.LongPredicate;
import java.util.stream.LongStream;

/**
 * <p>An internal implementation of an unmodifiable {@link LongCollection}
 * containing no elements.</p>
 */
public class EmptyLongCollection extends EmptyPrimitiveCollection<Long,long[],
        LongConsumer,LongPredicate,PrimitiveIterator.OfLong,Spliterator.OfLong,
        LongStream,LongCollection> implements LongCollection {
    /**
     * <p>A canonical instance of an {@code EmptyLongCollection}. As this is
     * completely immutable and has no state, this instance can be reused and
     * shared between threads.</p>
     */
    public static final EmptyLongCollection INSTANCE = new EmptyLongCollection();

    EmptyLongCollection() {}

    /**
     * <p>Returns {@code true} if this collection contains the specified element.</p>
     * @implNote <p>This method always returns {@code false}.</p>
     * @param element element whose presence in this collection is to be tested
     * @return {@code true} if this set contains the specified element
     */
    @Override
    public boolean containsLong(long element) {
        return false;
    }

    /**
     * <p>Returns a primitive iterator over the elements in this collection.</p>
     * @implNote <p>This method returns an instance of
     * {@link EmptyLongIterator}.</p>
     * @return a {@code PrimitiveIterator.OfLong} over the elements in this
     * collection
     */
    @Override
    public PrimitiveIterator.OfLong iterator() {
        return EmptyLongIterator.INSTANCE;
    }

    /**
     * <p>Returns a sequential {@code LongStream} with this collection as its
     * source.</p>
     * @implNote <p>This method returns an empty {@code LongStream}.</p>
     * @return a sequential {@code LongStream} over the elements in this
     * collection
     * @see LongStream#empty()
     */
    @Override
    public LongStream primitiveStream() {
        return LongStream.empty();
    }

    /**
     * <p>Creates a {@code Spliterator.OfLong} over the elements in this
     * collection.</p>
     * @implNote <p>This method returns an empty long spliterator.</p>
     * @return a {@code Spliterator.OfLong} over the elements in this set
     * @see Spliterators#emptyLongSpliterator()
     */
    @Override
    public Spliterator.OfLong spliterator() {
        return Spliterators.emptyLongSpliterator();
    }

    /**
     * <p>Returns an array containing the elements in this collection.</p>
     * @implNote <p>This method returns an empty array of longs.</p>
     * @return an array containing the elements in this collection
     */
    @Override
    public long[] toPrimitiveArray() {
        return new long[0];
    }

    /**
     * <p>Returns an instance of {@code EmptyLongCollection}.</p>
     * @return an empty long collection
     */
    public static EmptyLongCollection getInstance() {
        return INSTANCE;
    }
}
