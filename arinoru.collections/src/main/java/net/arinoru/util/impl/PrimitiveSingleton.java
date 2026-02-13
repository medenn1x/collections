package net.arinoru.util.impl;

import net.arinoru.util.PrimitiveCollection;
import net.arinoru.util.PrimitiveSet;

import java.io.Serializable;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.function.IntFunction;
import java.util.stream.BaseStream;
import java.util.stream.Stream;

/**
 * <p>Abstract base class for constructing singleton sets of primitive types.</p>
 * @param <T> the boxed type of the element contained within this singleton. The
 *           type must be a wrapper type for a primitive type, such as
 *           {@code Integer} for the primitive {@code int} type.
 * @param <T_ARR> the type of arrays who have the singleton's primitive element
 *               type as their runtime component type.
 * @param <T_CONS> the type of primitive consumer. The type must be a primitive
 *                specialization of {@link java.util.function.Consumer} for
 *                {@code T}, such as {@link java.util.function.IntConsumer} for
 *                {@code Integer}.
 * @param <T_PRED> the type of primitive predicate. The type must be a primitive
 *                specialization of {@link java.util.function.Predicate} for
 *                {@code T}, such as {@link java.util.function.IntPredicate} for
 *                {@code Integer}.
 * @param <T_ITER> the type of primitive iterator. The type must be a primitive
 *                specialization of {@link java.util.Iterator} for {@code T},
 *                such as {@link PrimitiveIterator.OfInt} for {@code Integer}.
 * @param <T_SPLITR> the type of primitive spliterator. The type must be a
 *                  primitive specialization of {@link Spliterator} for
 *                  {@code T}, such as {@link Spliterator.OfInt} for
 *                  {@code Integer}.
 * @param <T_STR> the type of primitive stream. The type must be a primitive
 *               specialization of {@link BaseStream} for {@code T}, such as
 *               {@link java.util.stream.IntStream} for {@code Integer}.
 * @param <T_COLL> the primitive collection type underlying the singleton. This
 *                should be the specific specialization collection type for
 *                {@code T}, such as {@link net.arinoru.util.IntCollection} for
 *                {@code Integer}.
 */
public abstract class PrimitiveSingleton<T,T_ARR,T_CONS,T_PRED,
        T_ITER extends PrimitiveIterator<T,T_CONS>,
        T_SPLITR extends Spliterator.OfPrimitive<T,T_CONS,T_SPLITR>,
        T_STR extends BaseStream<T,T_STR>,
        T_COLL extends PrimitiveCollection<T,T_ARR,T_CONS,T_PRED,T_ITER,T_SPLITR,T_STR,T_COLL>>
        extends UnmodifiablePrimitiveCollection<T,T_ARR,T_CONS,T_PRED,T_ITER,T_SPLITR,T_STR,T_COLL>
        implements PrimitiveSet<T,T_ARR,T_CONS,T_PRED,T_ITER,T_SPLITR,T_STR,T_COLL>, Serializable {
    /**
     * <p>Returns {@code true} if this set contains no elements.</p>
     * @implNote <p>This method always returns {@code false}.</p>
     * @return {@code true} if this set contains no elements
     */
    @Override
    public boolean isEmpty() {
        return false;
    }

    /**
     * <p>Returns the number of elements in this set (its cardinality).</p>
     * @implNote <p>This method always returns {@code 1}.</p>
     * @return the number of elements in this set (its cardinality)
     */
    @Override
    public int size() {
        return 1;
    }

    /**
     * <p>Returns a possibly parallel primitive stream with this set as its source.
     * It is allowable for this method to return a sequential primitive stream.</p>
     * @implNote <p>This method simply delegates to {@link #primitiveStream()}.</p>
     * @return a possibly parallel primitive stream over the elements
     * in this collection
     */
    @Override
    public T_STR parallelPrimitiveStream() {
        return primitiveStream();
    }

    /**
     * <p>Returns a possibly parallel {@code Stream} with this set as its source.
     * It is allowable for this method to return a sequential stream.</p>
     * @implNote <p>This method simply delegates to {@link #stream()}.</p>
     * @return a possibly parallel {@code Stream} over the elements in this set
     */
    @Override
    public Stream<T> parallelStream() {
        return stream();
    }

    /**
     * <p>Returns an array containing the element in this set. The
     * returned array's runtime component type is {@code Object}.</p>
     * <p>The returned array will be "safe" in that no references to it are
     * maintained by this set. (In other words, this method must allocate a new
     * array even if this set is backed by an array). The caller is thus free to
     * modify the returned array.</p>
     * @implNote <p>This method invokes the specified function with a value of
     * {@code 1}, then passes the resulting array to {@link #toArray(Object[])}.</p>
     * @return an array containing the element in this set
     */
    @Override
    public <U> U[] toArray(IntFunction<U[]> generator) {
        return toArray(generator.apply(1));
    }
}
