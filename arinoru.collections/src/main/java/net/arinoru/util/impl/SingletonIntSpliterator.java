package net.arinoru.util.impl;

import java.util.Objects;
import java.util.Spliterator;
import java.util.function.IntConsumer;

public class SingletonIntSpliterator extends SingletonPrimitiveSpliterator<Integer,
        IntConsumer, Spliterator.OfInt> implements Spliterator.OfInt {
    private final int value;

    SingletonIntSpliterator(int value) {
        this.value = value;
    }

    @Override
    public boolean tryAdvance(IntConsumer action) {
        Objects.requireNonNull(action);
        if (exhausted)
            return false;
        exhausted = true;
        action.accept(value);
        return true;
    }
}
