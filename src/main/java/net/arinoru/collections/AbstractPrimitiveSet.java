package net.arinoru.collections;

import net.arinoru.prerelease.PrereleaseContent;

import java.util.PrimitiveIterator;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.*;
import java.util.stream.BaseStream;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

/**
 * <p>This class provides a skeletal implementation of the {@link PrimitiveSet}
 * interface to minimize the effort required to implement this interface. It is
 * intended as a rough equivalent to the {@link java.util.AbstractSet} class
 * from the Java Collections Framework.</p>
 * <p>The process of implementing a primitive set by extending this class (or one
 * of its nested subclasses) is almost identical to that of implementing a Set by
 * extending AbstractSet, except that the iterator must conform to the appropriate
 * {@link PrimitiveIterator} type.</p>
 * <p>Note that this class does not override any of the default method
 * implementations from {@code PrimitiveSet} or {@link PrimitiveCollection}. It
 * merely adds implementations for {@code equals} and {@code hashCode}.</p>
 * <p>Developers may prefer using the nested subclasses to avoid directly
 * interfacing with the various generic types required by the base
 * {@code PrimitiveSet} interface; however, these subclasses provide no additional
 * implementations; they merely additionally implement the corresponding
 * specialization of {@code PrimitiveSet}.</p>
 * @param <T> The boxed type of elements contained within this set
 * @param <T_ARR> The type of arrays who have the set's primitive element type as
 *               their runtime component type
 * @param <T_CONS> The type of primitive consumer
 * @param <T_PRED> The type of primitive predicate
 * @param <T_SPLITR> The type of primitive spliterator
 * @param <T_STR> The type of primitive stream
 * @param <T_COLL> The type of the underlying primitive collection specialization
 *                corresponding to this set's element type
 */
@PrereleaseContent
public abstract class AbstractPrimitiveSet<T,T_ARR,T_CONS,T_PRED,
        T_SPLITR extends Spliterator.OfPrimitive<T,T_CONS,T_SPLITR>,
        T_STR extends BaseStream<T,T_STR>,
        T_COLL extends PrimitiveCollection<T,T_ARR,T_CONS,T_PRED,T_SPLITR,T_STR,T_COLL>>
        implements PrimitiveSet<T,T_ARR,T_CONS,T_PRED,T_SPLITR,T_STR,T_COLL> {
    /**
     * <p>Compares the specified object with this set for equality, as per the
     * contract required by {@link Set#equals(Object)}.</p>
     * @implNote <p>This method simply delegates to the
     * {@link PrimitiveCollections#equals(PrimitiveSet, Object)} method.</p>
     * @param o object to be compared for equality with this set
     * @return {@code true} if the specified object is equal to this set
     * @see PrimitiveCollections#equals(PrimitiveSet, Object)
     * @see Object#equals(Object)
     * @see Set#equals(Object)
     * @see PrimitiveSet#equals(Object)
     */
    @Override
    @SuppressWarnings("EqualsDoesntCheckParameterClass")
    public boolean equals(Object o) {
        return PrimitiveCollections.equals(this, o);
    }

    /**
     * <p>Returns the hash code value for this set, as per the contract required by
     * {@link Set#hashCode()}</p>
     * @implNote <p>This method simply delegates to the
     * {@link PrimitiveCollections#hashCode(PrimitiveSet)} method.</p>
     * @return the hash code value for this set
     * @see PrimitiveCollections#hashCode(PrimitiveSet)
     * @see Object#hashCode()
     * @see Set#hashCode()
     * @see PrimitiveSet#hashCode()
     */
    @Override
    public int hashCode() {
        return PrimitiveCollections.hashCode(this);
    }

    /**
     * <p>Returns a primitive iterator over the elements in this set.
     * Implementations are required to override this to provide a source of
     * elements for the set.</p>
     * @return a {@code PrimitiveIterator} over the elements in this set
     */
    @Override
    public abstract PrimitiveIterator<T,T_CONS> iterator();

    /**
     * <p>Returns the number of elements in this set (its cardinality).
     * Implementations are required to override this to provide the count of
     * the elements in the set.</p>
     * @return the number of elements in this set (its cardinality)
     */
    @Override
    public abstract int size();

    /**
     * <p>This class provides a skeletal implementation of the
     * {@link PrimitiveSet.OfDouble} interface to minimize the effort required to
     * implement this interface.</p>
     * <p>This class provides no additional implementation details beyond those
     * implemented by {@link AbstractPrimitiveSet}, but by directly implementing
     * {@code PrimitiveSet.OfDouble}, it allows the possibility of creating
     * anonymous subclasses. It also may be more convenient to extend this than to
     * directly extend {@code AbstractPrimitiveSet} given the number of generic
     * type parameters required.</p>
     */
    @PrereleaseContent
    public static abstract class OfDouble extends AbstractPrimitiveSet<Double,
            double[],DoubleConsumer,DoublePredicate,Spliterator.OfDouble,DoubleStream,
            PrimitiveCollection.OfDouble> implements PrimitiveSet.OfDouble {
        /**
         * <p>Returns a primitive iterator over the elements in this set.
         * Implementations are required to override this to provide a source of
         * elements for the set. If the returned iterator supports the
         * {@code remove} operation, then this set will support element removal
         * via the {@code remove}, {@code removeAll}, {@code removeIf},
         * {@code removeIfDouble}, {@code removeDouble}, and {@code retainAll}
         * operations.</p>
         * @return a {@code PrimitiveIterator} over the elements in this set
         */
        @Override
        public abstract PrimitiveIterator.OfDouble iterator();
    }

    /**
     * <p>This class provides a skeletal implementation of the
     * {@link PrimitiveSet.OfInt} interface to minimize the effort required to
     * implement this interface.</p>
     * <p>This class provides no additional implementation details beyond those
     * implemented by {@link AbstractPrimitiveSet}, but by directly implementing
     * {@code PrimitiveSet.OfInt}, it allows the possibility of creating
     * anonymous subclasses. It also may be more convenient to extend this than to
     * directly extend {@code AbstractPrimitiveSet} given the number of generic
     * type parameters required.</p>
     */
    @PrereleaseContent
    public static abstract class OfInt extends AbstractPrimitiveSet<Integer,
            int[], IntConsumer, IntPredicate, Spliterator.OfInt, IntStream,
            PrimitiveCollection.OfInt> implements PrimitiveSet.OfInt {
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
    }

    /**
     * <p>This class provides a skeletal implementation of the
     * {@link PrimitiveSet.OfLong} interface to minimize the effort required to
     * implement this interface.</p>
     * <p>This class provides no additional implementation details beyond those
     * implemented by {@link AbstractPrimitiveSet}, but by directly implementing
     * {@code PrimitiveSet.OfLong}, it allows the possibility of creating
     * anonymous subclasses. It also may be more convenient to extend this than to
     * directly extend {@code AbstractPrimitiveSet} given the number of generic
     * type parameters required.</p>
     */
    @PrereleaseContent
    public static abstract class OfLong extends AbstractPrimitiveSet<Long,
            long[], LongConsumer, LongPredicate, Spliterator.OfLong, LongStream,
            PrimitiveCollection.OfLong> implements PrimitiveSet.OfLong {
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
    }
}
