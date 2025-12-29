package net.arinoru.util.impl;

import java.util.Objects;
import java.util.Spliterator;
import java.util.function.DoubleConsumer;

public class SingletonDoubleSpliterator extends SingletonPrimitiveSpliterator<Double,
        DoubleConsumer, Spliterator.OfDouble> implements Spliterator.OfDouble {
    private final double value;

    SingletonDoubleSpliterator(double value) {
        this.value = value;
    }

    @Override
    public boolean tryAdvance(DoubleConsumer action) {
        Objects.requireNonNull(action);
        if (exhausted)
            return false;
        exhausted = true;
        action.accept(value);
        return true;
    }
}
