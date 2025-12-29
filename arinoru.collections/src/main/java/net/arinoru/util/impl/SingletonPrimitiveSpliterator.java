package net.arinoru.util.impl;

import java.util.Spliterator;

public abstract class SingletonPrimitiveSpliterator<T,T_CONS,
        T_SPLITR extends Spliterator.OfPrimitive<T,T_CONS,T_SPLITR>>
        implements Spliterator.OfPrimitive<T,T_CONS,T_SPLITR> {
    protected boolean exhausted;

    @Override
    public T_SPLITR trySplit() {
        return null;
    }

    @Override
    public abstract boolean tryAdvance(T_CONS action);

    @Override
    public void forEachRemaining(T_CONS action) {
        tryAdvance(action);
    }

    @Override
    public long estimateSize() {
        return exhausted ? 0L : 1L;
    }

    @Override
    public int characteristics() {
        return Spliterator.NONNULL | Spliterator.SIZED | Spliterator.SUBSIZED |
                Spliterator.IMMUTABLE | Spliterator.DISTINCT | Spliterator.ORDERED;
    }
}
