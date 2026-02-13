package net.arinoru.util.impl;

import java.util.Objects;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.LongConsumer;

/**
 * <p>An internal implementation of a {@link Spliterator.OfLong} over a single
 * double value.</p>
 * @implNote <p>This spliterator reports {@link #NONNULL}, {@link #SIZED},
 * {@link #SUBSIZED}, {@link #IMMUTABLE}, {@link #DISTINCT}, and
 * {@link #ORDERED}.</p>
 */
public class SingletonLongSpliterator extends SingletonPrimitiveSpliterator<Long,
        LongConsumer, Spliterator.OfLong> implements Spliterator.OfLong {
    private final long value;

    /**
     * <p>Create a spliterator over the specified long.</p>
     * @param value value returned by spliterator
     */
    SingletonLongSpliterator(long value) {
        this.value = value;
    }

    /**
     * <p>Performs the given action on the next element of the iteration, if any.
     * Returns {@code true} if there was such an element; otherwise, returns
     * {@code false}.</p>
     * @implNote <p>This method wraps the specified action and delegates to
     * {@link #tryAdvance(LongConsumer)}.</p>
     * @param action the action
     * @return true if an element was available to process, otherwise false
     * @throws NullPointerException if action is null
     */
    @Override
    public boolean tryAdvance(Consumer<? super Long> action) {
        return tryAdvance((LongConsumer) action::accept);
    }

    /**
     * <p>Performs the given action on the next element of the iteration, if any.
     * Returns {@code true} if there was such an element; otherwise, returns
     * {@code false}.</p>
     * @implNote <p>This method returns {@code false} if the iteration has been
     * exhausted; otherwise, it passes the sole element to the action's
     * {@code accept} method and returns {@code true}.</p>
     * @param action the action
     * @return true if an element was available to process, otherwise false
     * @throws NullPointerException if action is null
     */
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
