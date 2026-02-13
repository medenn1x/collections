package net.arinoru.util.impl;

import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;
import java.util.function.IntConsumer;

/**
 * <p>An internal implementation of a {@link PrimitiveIterator.OfInt} over an empty
 * collection.</p>
 */
public class EmptyIntIterator extends EmptyPrimitiveIterator<Integer,IntConsumer>
        implements PrimitiveIterator.OfInt {
    /**
     * <p>A canonical instance of an {@code EmptyIntIterator}. As this is completely
     * immutable and has no state, this instance can be reused and shared between
     * threads.</p>
     */
    public static EmptyIntIterator INSTANCE = new EmptyIntIterator();

    private EmptyIntIterator() {}

    /**
     * <p>Returns the next element in the iteration.</p>
     * @implNote <p>This method always throws {@link NoSuchElementException}.</p>
     * @return next element of the iteration
     * @throws NoSuchElementException if there are no more elements
     */
    @Override
    public int nextInt() {
        throw new NoSuchElementException();
    }

    /**
     * <p>Returns an instance of {@code EmptyIntIterator}.</p>
     * @return an empty int iterator
     */
    public static EmptyIntIterator getInstance() {
        return INSTANCE;
    }
}
