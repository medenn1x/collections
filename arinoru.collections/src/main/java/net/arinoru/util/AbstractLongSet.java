package net.arinoru.util;

import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.function.LongConsumer;
import java.util.function.LongPredicate;
import java.util.stream.LongStream;

/**
 * <p>This class provides a skeletal implementation of the
 * {@link LongSet} interface to minimize the effort required to
 * implement this interface.</p>
 * <p>This class provides no additional implementation details beyond those
 * implemented by {@link AbstractPrimitiveSet}, but by directly implementing
 * {@code LongSet}, it allows the possibility of creating
 * anonymous subclasses. It also may be more convenient to extend this than to
 * directly extend {@code AbstractPrimitiveSet} given the number of generic
 * type parameters required.</p>
 */
public abstract class AbstractLongSet extends AbstractPrimitiveSet<Long,long[],
        LongConsumer,LongPredicate,PrimitiveIterator.OfLong,Spliterator.OfLong,
        LongStream,LongCollection> implements LongSet {
    /**
     * <p>Returns a primitive iterator over the elements in this set.
     * Implementations are required to override this to provide a source of
     * elements for the set. If the returned iterator supports the
     * {@code remove} operation, then this set will support element removal
     * via the {@code remove}, {@code removeAll}, {@code removeIf},
     * {@code removeIfLong}, {@code removeLong}, and {@code retainAll}
     * operations.</p>
     * @return a {@code PrimitiveIterator} over the elements in this set
     */
    @Override
    public abstract PrimitiveIterator.OfLong iterator();

    /**
     * <p>Returns the number of elements in this set (its cardinality).
     * Implementations are required to override this to provide the count of
     * the elements in the set.</p>
     * @return the number of elements in this set (its cardinality)
     */
    public abstract int size();
}
