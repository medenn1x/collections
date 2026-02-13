package net.arinoru.util.impl;

import net.arinoru.util.IntCollection;

import java.util.Objects;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

/**
 * <p>An internal implementation of a {@link Spliterator.OfInt} over an
 * {@link IntCollection}. This spliterator is implemented in terms of the
 * backing collection's iterator and is <em>late-binding</em>.</p>
 */
public class IntSpliterator extends PrimitiveCollectionSpliterator<Integer,IntConsumer,
        PrimitiveIterator.OfInt,Spliterator.OfInt,IntCollection>
        implements Spliterator.OfInt {
    /**
     * <p>Create a spliterator over the specified int collection. If the specified
     * characteristics do not include {@link #CONCURRENT}, this spliterator will
     * additionally report {@link #SIZED} and {@link #SUBSIZED}.</p>
     * @param collection backing collection
     * @param characteristics spliterator characteristics bitmask
     */
    public IntSpliterator(IntCollection collection, int characteristics) {
        super(collection, characteristics);
    }

    /**
     * <p>If this spliterator can be partitioned, returns a spliterator covering
     * elements that will, upon return from this method, not be covered by this
     * spliterator.</p>
     * @implNote <p>If the underlying iterator still has elements and we have not
     * exhausted the number of elements we expected to return, a new, array-backed
     * spliterator will be returned containing a "batch" of elements pulled from
     * the iterator. Each subsequent call to this method will increase the size of
     * the batch used for the next spliterator, up to a configured maximum batch
     * size. If there are no more elements, returns {@code null}.</p>
     * @return a spliterator covering some partition of the elements, or
     * {@code null} if this spliterator cannot be split
     */
    @Override
    public Spliterator.OfInt trySplit() {
        bindIterator();
        if (size > 1 && iterator.hasNext()) {
            int n = batchSize + BATCH_UNIT;
            if (n > size)
                n = size;
            if (n > MAX_BATCH)
                n = MAX_BATCH;
            int[] a = new int[n];
            int j = 0;
            do {
                a[j] = iterator.nextInt();
            } while (++j < n && iterator.hasNext());
            batchSize = j;
            size -= j;
            return Spliterators.spliterator(a, 0, j, characteristics());
        }
        return null;
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
     * @implNote <p>This method binds iteration, checks whether the iterator has
     * an element to return, and if so passes it to the action's {@code accept}
     * method.</p>
     * @param action the action
     * @return true if an element was available to process, otherwise false
     * @throws NullPointerException if action is null
     */
    @Override
    public boolean tryAdvance(IntConsumer action) {
        Objects.requireNonNull(action);
        bindIterator();
        if (iterator.hasNext()) {
            action.accept(iterator.nextInt());
            return true;
        }
        return false;
    }
}
