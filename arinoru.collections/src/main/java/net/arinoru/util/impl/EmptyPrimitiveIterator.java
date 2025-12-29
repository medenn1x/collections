package net.arinoru.util.impl;

import net.arinoru.util.views.UnmodifiableView;

import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;
import java.util.function.Consumer;

public abstract class EmptyPrimitiveIterator<T,T_CONS>
        implements PrimitiveIterator<T,T_CONS>, UnmodifiableView {
    @Override
    public void forEachRemaining(Consumer<? super T> action) {}

    @Override
    public void forEachRemaining(T_CONS action) {}

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public T next() {
        throw new NoSuchElementException();
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
