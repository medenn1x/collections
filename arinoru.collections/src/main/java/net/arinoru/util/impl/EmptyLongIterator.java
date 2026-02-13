package net.arinoru.util.impl;

import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;
import java.util.function.LongConsumer;

/**
 * <p>An internal implementation of a {@link PrimitiveIterator.OfLong} over an
 * empty collection.</p>
 */
public class EmptyLongIterator extends EmptyPrimitiveIterator<Long,LongConsumer>
        implements PrimitiveIterator.OfLong {
    /**
     * <p>A canonical instance of an {@code EmptyLongIterator}. As this is
     * completely immutable and has no state, this instance can be reused and
     * shared between threads.</p>
     */
    public static final EmptyLongIterator INSTANCE = new EmptyLongIterator();

    private EmptyLongIterator() {}

    /**
     * <p>Returns the next element in the iteration.</p>
     * @implNote <p>This method always throws {@link NoSuchElementException}.</p>
     * @return next element of the iteration
     * @throws NoSuchElementException if there are no more elements
     */
    @Override
    public long nextLong() {
        throw new NoSuchElementException();
    }

    /**
     * <p>Returns an instance of {@code EmptyLongIterator}.</p>
     * @return an empty long iterator
     */
    public static EmptyLongIterator getInstance() {
        return INSTANCE;
    }
}
