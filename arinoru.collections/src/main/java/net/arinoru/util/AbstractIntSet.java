package net.arinoru.util;

import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.function.IntConsumer;
import java.util.function.IntPredicate;
import java.util.stream.IntStream;

/**
 * <p>This class provides a skeletal implementation of the
 * {@link IntSet} interface to minimize the effort required to
 * implement this interface.</p>
 * <p>This class provides no additional implementation details beyond those
 * implemented by {@link AbstractPrimitiveSet}, but by directly implementing
 * {@code IntSet}, it allows the possibility of creating
 * anonymous subclasses. It also may be more convenient to extend this than to
 * directly extend {@code AbstractPrimitiveSet} given the number of generic
 * type parameters required.</p>
 */
public abstract class AbstractIntSet extends AbstractPrimitiveSet<Integer,int[],
        IntConsumer,IntPredicate,PrimitiveIterator.OfInt,Spliterator.OfInt,
        IntStream,IntCollection> implements IntSet {
    /**
     * <p>Returns a primitive iterator over the elements in this set.
     * Implementations are required to override this to provide a source of
     * elements for the set. If the returned iterator supports the
     * {@code remove} operation, then this set will support element removal
     * via the {@code remove}, {@code removeAll}, {@code removeIf},
     * {@code removeIfInt}, {@code removeInt}, and {@code retainAll}
     * operations.</p>
     * @return a {@code PrimitiveIterator} over the elements in this set
     */
    @Override
    public abstract PrimitiveIterator.OfInt iterator();

    /**
     * <p>Returns the number of elements in this set (its cardinality).
     * Implementations are required to override this to provide the count of
     * the elements in the set.</p>
     * @return the number of elements in this set (its cardinality)
     */
    public abstract int size();
}
