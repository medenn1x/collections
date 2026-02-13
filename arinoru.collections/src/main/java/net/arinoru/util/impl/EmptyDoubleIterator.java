package net.arinoru.util.impl;

import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;
import java.util.function.DoubleConsumer;

/**
 * <p>An internal implementation of a {@link PrimitiveIterator.OfDouble} over an
 * empty collection.</p>
 */
public class EmptyDoubleIterator extends EmptyPrimitiveIterator<Double,DoubleConsumer>
        implements PrimitiveIterator.OfDouble {
    /**
     * <p>A canonical instance of an {@code EmptyDoubleIterator}. As this is
     * completely immutable and has no state, this instance can be reused and
     * shared between threads.</p>
     */
    public static final EmptyDoubleIterator INSTANCE = new EmptyDoubleIterator();

    private EmptyDoubleIterator() {}

    /**
     * <p>Returns the next element in the iteration.</p>
     * @implNote <p>This method always throws {@link NoSuchElementException}.</p>
     * @return next element of the iteration
     * @throws NoSuchElementException if there are no more elements
     */
    @Override
    public double nextDouble() {
        throw new NoSuchElementException();
    }

    /**
     * <p>Returns an instance of {@code EmptyDoubleIterator}.</p>
     * @return an empty double iterator
     */
    public static EmptyDoubleIterator getInstance() {
        return INSTANCE;
    }
}
