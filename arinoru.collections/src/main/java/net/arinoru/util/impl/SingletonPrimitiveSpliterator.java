package net.arinoru.util.impl;

import java.util.Comparator;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * <p>Abstract base class for constructing spliterators over primitive singleton
 * collections.</p>
 * @param <T> the boxed type of the element contained within the backing collection.
 *           The type must be a wrapper type for a primitive type, such as
 *           {@link Integer} for the primitive {@code int} type.
 * @param <T_CONS> the type of primitive consumer. The type must be a primitive
 *                specialization of {@link java.util.function.Consumer} for
 *                {@code T}, such as {@link java.util.function.IntConsumer} for
 *                {@link Integer}.
 * @param <T_SPLITR> the type of primitive spliterator. The type must be a
 *                  primitive specialization of {@link Spliterator} for {@code T},
 *                  such as {@link Spliterator.OfInt} for {@link Integer}.
 */
public abstract class SingletonPrimitiveSpliterator<T,T_CONS,
        T_SPLITR extends Spliterator.OfPrimitive<T,T_CONS,T_SPLITR>>
        implements Spliterator.OfPrimitive<T,T_CONS,T_SPLITR> {
    /**
     * <p>A boolean field indicating whether the sole element has been returned by
     * the iteration.</p>
     */
    protected boolean exhausted;

    /**
     * <p>If this spliterator can be partitioned, returns a spliterator covering
     * elements that will, upon return from this method, not be covered by this
     * spliterator.</p>
     * @implNote <p>This method always returns {@code null}.</p>
     * @return a spliterator covering some partition of the elements, or
     * {@code null} if this spliterator cannot be split
     */
    @Override
    public T_SPLITR trySplit() {
        return null;
    }

    /**
     * <p>Performs the given action on the next element of the iteration, if any.
     * Returns {@code true} if there was such an element; otherwise, returns
     * {@code false}.</p>
     * @param action the action
     * @return true if an element was available to process, otherwise false
     * @throws NullPointerException if action is null
     */
    @Override
    public abstract boolean tryAdvance(Consumer<? super T> action);

    /**
     * <p>Performs the given action on the next element of the iteration, if any.
     * Returns {@code true} if there was such an element; otherwise, returns
     * {@code false}.</p>
     * @param action the action
     * @return true if an element was available to process, otherwise false
     * @throws NullPointerException if action is null
     */
    @Override
    public abstract boolean tryAdvance(T_CONS action);

    /**
     * <p>Performs the given action for each remaining element, sequentially in
     * the current thread, until all elements have been processed or the action
     * throws an exception.</p>
     * @implNote <p>This method delegates to {@link #tryAdvance(Consumer)}.</p>
     * @param action the action
     * @throws NullPointerException if the specified action is null
     */
    @Override
    public void forEachRemaining(Consumer<? super T> action) {
        tryAdvance(action);
    }

    /**
     * <p>Performs the given action for each remaining element, sequentially in
     * the current thread, until all elements have been processed or the action
     * throws an exception.</p>
     * @implNote <p>This method delegates to
     * {@link #tryAdvance(Object) tryAdvance(T_CONS)}.</p>
     * @param action the action
     * @throws NullPointerException if the specified action is null
     */
    @Override
    public void forEachRemaining(T_CONS action) {
        tryAdvance(action);
    }

    /**
     * <p>Returns the estimated size of the iteration.</p>
     * @implNote <p>This method always returns {@code 1L}.</p>
     * @return estimated size of the iteration
     */
    @Override
    public long estimateSize() {
        return 1L;
    }

    /**
     * <p>Returns a set of characteristics of this spliterator and its
     * elements.</p>
     * @implNote <p>This method reports {@link #NONNULL}, {@link #SIZED},
     * {@link #SUBSIZED}, {@link #IMMUTABLE}, {@link #DISTINCT} and
     * {@link #ORDERED}.</p>
     * @return a representation of characteristics
     */
    @Override
    public int characteristics() {
        return Spliterator.NONNULL | Spliterator.SIZED | Spliterator.SUBSIZED |
                Spliterator.IMMUTABLE | Spliterator.DISTINCT | Spliterator.ORDERED;
    }
}
