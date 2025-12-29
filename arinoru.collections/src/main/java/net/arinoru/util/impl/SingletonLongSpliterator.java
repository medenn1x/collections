package net.arinoru.util.impl;

import java.util.Objects;
import java.util.Spliterator;
import java.util.function.LongConsumer;

public class SingletonLongSpliterator extends SingletonPrimitiveSpliterator<Long,
        LongConsumer, Spliterator.OfLong> implements Spliterator.OfLong {
    private final long value;

    SingletonLongSpliterator(long value) {
        this.value = value;
    }

    @Override
    public boolean tryAdvance(LongConsumer action) {
        Objects.requireNonNull(action);
        if (exhausted)
            return false;
        exhausted = true;
        action.accept(value);
        return true;
    }
}
