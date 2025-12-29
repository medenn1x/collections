package net.arinoru.util;

import net.arinoru.annotation.Unstable;

import java.util.Collection;
import java.util.PrimitiveIterator;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.BaseStream;
import java.util.stream.Stream;

@Unstable
public interface PrimitiveSet<T,T_ARR,T_CONS,T_PRED,
        T_ITER extends PrimitiveIterator<T,T_CONS>,
        T_SPLITR extends Spliterator.OfPrimitive<T,T_CONS,T_SPLITR>,
        T_STR extends BaseStream<T,T_STR>,
        T_COLL extends PrimitiveCollection<T,T_ARR,T_CONS,T_PRED,T_ITER,T_SPLITR,T_STR,T_COLL>>
        extends PrimitiveCollection<T,T_ARR,T_CONS,T_PRED,T_ITER,T_SPLITR,T_STR,T_COLL>, Set<T> {
    boolean add(T element);
    boolean addAll(Collection<? extends T> collection);
    boolean addAll(T_COLL collection);
    void clear();
    boolean contains(Object o);
    boolean containsAll(Collection<?> collection);
    boolean containsAll(T_COLL collection);
    @SuppressWarnings("unchecked")
    static <T,T_COLL extends PrimitiveCollection<T,?,?,?,?,?,?,T_COLL>,
            T_SET extends PrimitiveSet<T,?,?,?,?,?,?,T_COLL>> T_SET copyOf(
                    T_COLL collection) {
        return (T_SET) switch (collection) {
            case DoubleCollection doubles -> DoubleSet.copyOf(doubles);
            case IntCollection integers -> IntSet.copyOf(integers);
            case LongCollection longs -> LongSet.copyOf(longs);
            default -> throw new ClassCastException("Unknown primitive collection type");
        };
    }
    boolean equals(Object o);
    default void forEach(Consumer<? super T> action) {
        PrimitiveCollection.super.forEach(action);
    }
    default void forEach(T_CONS action) {
        PrimitiveCollection.super.forEach(action);
    }
    int hashCode();
    default boolean isEmpty() {
        return PrimitiveCollection.super.isEmpty();
    }
    T_ITER iterator();
    T_STR parallelPrimitiveStream();
    default Stream<T> parallelStream() {
        return PrimitiveCollection.super.parallelStream();
    }
    T_STR primitiveStream();
    boolean remove(Object o);
    boolean removeAll(Collection<?> collection);
    boolean removeAll(T_COLL collection);
    boolean retainAll(Collection<?> collection);
    boolean retainAll(T_COLL collection);
    int size();
    T_SPLITR spliterator();
    default Stream<T> stream() {
        return PrimitiveCollection.super.stream();
    }
    default Object[] toArray() {
        return PrimitiveCollection.super.toArray();
    }
    default <U> U[] toArray(U[] a) {
        return PrimitiveCollection.super.toArray(a);
    }
    T_ARR toPrimitiveArray();
}
