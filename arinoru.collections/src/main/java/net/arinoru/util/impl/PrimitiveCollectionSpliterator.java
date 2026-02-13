package net.arinoru.util.impl;

// Some of the content of this class may be imported and adapted from OpenJDK 11
// java.util.Spliterators

import net.arinoru.util.PrimitiveCollection;

import java.util.Comparator;
import java.util.Objects;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * <p>Abstract base class for constructing spliterators over primitive
 * collections that operate based on their provided iterator. It is not
 * appropriate for collections that rely on the collection spliterator to back
 * their iterator.</p>
 * @param <T> the boxed type of the elements contained within the backing
 *          collection. The type must be a wrapper type for a primitive type,
 *           such as {@code Integer} for the primitive {@code int} type.
 * @param <T_CONS> the type of primitive consumer. The type must be a primitive
 *                specialization of {@link Consumer} for {@code T}, such as
 *                {@link java.util.function.IntConsumer} for {@code Integer}.
 * @param <T_ITER> the type of primitive iterator. The type must be a primitive
 *                specialization of {@link java.util.Iterator} for {@code T},
 *                such as {@link PrimitiveIterator.OfInt} for {@code Integer}.
 * @param <T_SPLITR> the type of primitive spliterator. The type must be a
 *                 primitive specialization of {@link Spliterator} for
 *                  {@code T}, such as {@link Spliterator.OfInt} for
 *                  {@code Integer}.
 * @param <T_COLL> the primitive collection type of the backing collection.
 *                This should be the specific specialization collection type
 *                for {@code T}, such as {@link net.arinoru.util.IntCollection}
 *                for {@code Integer}.
 */
public abstract class PrimitiveCollectionSpliterator<T,T_CONS,
        T_ITER extends PrimitiveIterator<T,T_CONS>,
        T_SPLITR extends Spliterator.OfPrimitive<T,T_CONS,T_SPLITR>,
        T_COLL extends PrimitiveCollection<T,?,T_CONS,?,T_ITER,T_SPLITR,?,T_COLL>>
        implements Spliterator.OfPrimitive<T,T_CONS,T_SPLITR> {
    /**
     * <p>Size increment for batching. Each time {@link #trySplit()} is called on
     * the spliterator at the tail of the collection, the number of elements
     * handled by the returned spliterator increases by this amount, until
     * either the end of the collection or the maximum batch size is reached.</p>
     */
    protected static final int BATCH_UNIT = 1 << 10;

    /**
     * <p>Maximum batch size. When {@link #trySplit()} is called on the
     * spliterator at the tail of the collection, this is the maximum number of
     * elements retained by the spliterator after the call.</p>
     */
    protected static final int MAX_BATCH = 1 << 25;  // max batch array size

    private final int characteristics;

    /**
     * <p>The backing collection.</p>
     */
    protected final T_COLL collection;

    /**
     * <p>An iterator over the backing collection. This will be {@code null} until
     * the iteration is <em>bound</em> by invoking a method that inspects the
     * iteration state (i.e. {@link #estimateSize()}, {@link #trySplit()},
     * {@link #tryAdvance(Consumer)}, {@link #tryAdvance(Object) tryAdvance(T_CONS)},
     * {@link #forEachRemaining(Consumer)}, or
     * {@link #forEachRemaining(Object) forEachRemaining(T_CONS)}).</p>
     */
    protected T_ITER iterator;

    /**
     * <p>The estimated number of elements this spliterator will return.
     * This will be {@code 0} until the iteration is <em>bound</em> by invoking a
     * method that inspects the iteration state (i.e. {@link #estimateSize()},
     * {@link #trySplit()}, {@link #tryAdvance(Consumer)},
     * {@link #tryAdvance(Object) tryAdvance(T_CONS)},
     * {@link #forEachRemaining(Consumer)}, or
     * {@link #forEachRemaining(Object) forEachRemaining(T_CONS)}).</p>
     * <p>The size reported by the backing collection is used for this when
     * iteration is first bound. When {@link #trySplit()} is called, this is
     * reduced by the number of elements delegated to the returned spliterator,
     * if any.</p>
     */
    protected int size;

    /**
     * <p>The current batch size for this spliterator. Each time {@link #trySplit()}
     * is called, this size will increase, until either the limit of the remaining
     * number of elements or {@link #MAX_BATCH} is reached. This value represents
     * the size of the spliterator returned by {@link #trySplit()}.</p>
     */
    protected int batchSize;

    /**
     * <p>Construct a new primitive collection spliterator. If the specified
     * characteristics do not include {@link #CONCURRENT}, this spliterator will
     * additionally report {@link #SIZED} and {@link #SUBSIZED}.</p>
     * @param collection backing collection
     * @param characteristics spliterator characteristics bitmask
     */
    protected PrimitiveCollectionSpliterator(T_COLL collection, int characteristics) {
        this.collection = collection;
        this.characteristics = (characteristics & Spliterator.CONCURRENT) == 0 ?
                characteristics | Spliterator.SIZED | Spliterator.SUBSIZED :
                characteristics;
    }

    /**
     * <p>Binds the iteration. This operation is idempotent: it may be called any
     * number of times and will only have an effect the first time it is
     * called.</p>
     * @implSpec <p>To maintain the invariant cited above, extending classes
     * should not directly modify the {@link #iterator} field, except when
     * implementing an override of this method. If overriding this method,
     * implementations should perform no visible operation if {@link #iterator}
     * is not {@code null}.</p>
     */
    protected void bindIterator() {
        if (iterator == null) {
            iterator = collection.iterator();
            size = collection.size();
        }
    }

    /**
     * <p>Returns the estimated size of the iteration. Before any calls to
     * {@link #trySplit()} this is equal to the reported size of the collection
     * when the iteration was <em>bound</em>. After a call to {@link #trySplit()},
     * this is the estimated number of elements this spliterator is still
     * responsible for returning (including any elements already returned).</p>
     * @return estimated size of the iteration
     */
    @Override
    public long estimateSize() {
        bindIterator();
        return size;
    }

    /**
     * <p>Returns a set of characteristics of this spliterator and its
     * elements.</p>
     * @return a representation of characteristics
     */
    @Override
    @SuppressWarnings("MagicConstant")
    public int characteristics() {
        return characteristics;
    }

    /**
     * <p>If this spliterator's source is {@link #SORTED} by a {@link Comparator},
     * returns that comparator. If this spliterator's source is {@link #SORTED}
     * by natural order, returns {@code null}. If this spliterator's source is not
     * {@link #SORTED}, throws {@link IllegalStateException}.</p>
     * @implNote <p>This method returns {@code null} if the reported
     * characteristics includes {@link #SORTED}, otherwise it throws an
     * exception.</p>
     * @return comparator by which elements are sorted, or null if they are sorted
     * by natural order
     * @throws IllegalStateException if elements are not sorted
     */
    @Override
    public Comparator<? super T> getComparator() {
        if (hasCharacteristics(SORTED))
            return null;
        throw new IllegalStateException();
    }

    /**
     * <p>If this spliterator can be partitioned, returns a spliterator covering
     * elements that will, upon return from this method, not be covered by this
     * spliterator.</p>
     * <p>If this spliterator is {@link #ORDERED}, the returned spliterator must
     * cover a strict prefix of the elements.</p>
     * @implSpec <p>Implementing classes should override this method to call
     * {@link #bindIterator()} before attempting to partition.</p>
     * @return a spliterator covering some partition of the elements, or
     * {@code null} if this spliterator cannot be split
     */
    @Override
    public abstract T_SPLITR trySplit();

    /**
     * <p>Performs the given action for each remaining element, sequentially in
     * the current thread, until all elements have been processed or the action
     * throws an exception.</p>
     * @implNote <p>This method binds the iteration, then calls the underlying
     * iterator's
     * {@link PrimitiveIterator#forEachRemaining(Consumer) forEachRemaining(Consumer)}
     * method with the specified action.</p>
     * @param action the action
     * @throws NullPointerException if the specified action is null
     */
    @Override
    public void forEachRemaining(Consumer<? super T> action) {
        Objects.requireNonNull(action);
        bindIterator();
        iterator.forEachRemaining(action);
    }

    /**
     * <p>Performs the given action for each remaining element, sequentially in
     * the current thread, until all elements have been processed or the action
     * throws an exception.</p>
     * @implNote <p>This method binds the iteration, then calls the underlying
     * iterator's
     * {@link PrimitiveIterator#forEachRemaining(Object) forEachRemaining(T_CONS)}
     * method with the specified action.</p>
     * @param action the action
     * @throws NullPointerException if the specified action is null
     */
    @Override
    public void forEachRemaining(T_CONS action) {
        Objects.requireNonNull(action);
        bindIterator();
        iterator.forEachRemaining(action);
    }
}
