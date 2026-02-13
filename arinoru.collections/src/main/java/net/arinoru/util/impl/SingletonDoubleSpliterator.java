package net.arinoru.util.impl;

import java.util.Objects;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;

/**
 * <p>An internal implementation of a {@link Spliterator.OfDouble} over a single
 * double value.</p>
 * @implNote <p>This spliterator reports {@link #NONNULL}, {@link #SIZED},
 * {@link #SUBSIZED}, {@link #IMMUTABLE}, {@link #DISTINCT}, and
 * {@link #ORDERED}.</p>
 */
public class SingletonDoubleSpliterator extends SingletonPrimitiveSpliterator<Double,
        DoubleConsumer, Spliterator.OfDouble> implements Spliterator.OfDouble {
    private final double value;

    /**
     * <p>Create a spliterator over the specified double.</p>
     * @param value value returned by spliterator
     */
    SingletonDoubleSpliterator(double value) {
        this.value = value;
    }

    /**
     * <p>Performs the given action on the next element of the iteration, if any.
     * Returns {@code true} if there was such an element; otherwise, returns
     * {@code false}.</p>
     * @implNote <p>This method wraps the specified action and delegates to
     * {@link #tryAdvance(DoubleConsumer)}.</p>
     * @param action the action
     * @return true if an element was available to process, otherwise false
     * @throws NullPointerException if action is null
     */
    @Override
    public boolean tryAdvance(Consumer<? super Double> action) {
        return tryAdvance((DoubleConsumer) action::accept);
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
    public boolean tryAdvance(DoubleConsumer action) {
        Objects.requireNonNull(action);
        if (exhausted)
            return false;
        exhausted = true;
        action.accept(value);
        return true;
    }
}
