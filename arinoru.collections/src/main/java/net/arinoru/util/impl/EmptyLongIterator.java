package net.arinoru.util.impl;

import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;
import java.util.function.LongConsumer;

public class EmptyLongIterator extends EmptyPrimitiveIterator<Long,LongConsumer>
        implements PrimitiveIterator.OfLong {
    public static final EmptyLongIterator INSTANCE = new EmptyLongIterator();

    private EmptyLongIterator() {}

    @Override
    public long nextLong() {
        throw new NoSuchElementException();
    }

    public static EmptyLongIterator getInstance() {
        return INSTANCE;
    }
}
