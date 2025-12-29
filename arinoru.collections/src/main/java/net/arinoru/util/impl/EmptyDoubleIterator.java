package net.arinoru.util.impl;

import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;
import java.util.function.DoubleConsumer;

public class EmptyDoubleIterator extends EmptyPrimitiveIterator<Double,DoubleConsumer>
        implements PrimitiveIterator.OfDouble {
    public static final EmptyDoubleIterator INSTANCE = new EmptyDoubleIterator();

    private EmptyDoubleIterator() {}

    @Override
    public double nextDouble() {
        throw new NoSuchElementException();
    }

    public static EmptyDoubleIterator getInstance() {
        return INSTANCE;
    }
}
