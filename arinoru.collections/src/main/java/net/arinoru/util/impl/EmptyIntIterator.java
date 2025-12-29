package net.arinoru.util.impl;

import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;
import java.util.function.IntConsumer;

public class EmptyIntIterator extends EmptyPrimitiveIterator<Integer,IntConsumer>
        implements PrimitiveIterator.OfInt {
    public static EmptyIntIterator INSTANCE = new EmptyIntIterator();

    private EmptyIntIterator() {}

    @Override
    public int nextInt() {
        throw new NoSuchElementException();
    }

    public static EmptyIntIterator getInstance() {
        return INSTANCE;
    }
}
