package net.arinoru.util.impl;

import java.util.Objects;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

/**
 * <p>An internal implementation of a {@link Spliterator.OfInt} over a single int
 * value.</p>
 * @implNote <p>This spliterator reports {@link #NONNULL}, {@link #SIZED},
 * {@link #SUBSIZED}, {@link #IMMUTABLE}, {@link #DISTINCT},
 * and {@link #ORDERED}.</p>
 */
public class SingletonIntSpliterator extends SingletonPrimitiveSpliterator<Integer,
        IntConsumer, Spliterator.OfInt> implements Spliterator.OfInt {
    private final int value;

    /**
     * <p>Create a spliterator over the specified int.</p>
     * @param value value returns by spliterator
     */
    SingletonIntSpliterator(int value) {
        this.value = value;
    }

    /**
     * <p>Performs the given action on the next element of the iteration, if any.
     * Returns {@code true} if there was such an element; otherwise, returns
     * {@code false}.</p>
     * @implNote <p>This method wraps the specified action and delegates to
     * {@link #tryAdvance(IntConsumer)}.</p>
     * @param action the action
     * @return true if an element was available to process, otherwise false
     * @throws NullPointerException if action is null
     */
    @Override
    public boolean tryAdvance(Consumer<? super Integer> action) {
        return tryAdvance((IntConsumer) action::accept);
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
    public boolean tryAdvance(IntConsumer action) {
        Objects.requireNonNull(action);
        if (exhausted)
            return false;
        exhausted = true;
        action.accept(value);
        return true;
    }
}
