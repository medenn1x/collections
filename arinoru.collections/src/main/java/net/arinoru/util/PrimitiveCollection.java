package net.arinoru.util;

import net.arinoru.annotation.Unstable;

import java.util.Collection;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.BaseStream;
import java.util.stream.Stream;

@Unstable
public interface PrimitiveCollection<T,T_ARR,T_CONS,T_PRED,
        T_ITER extends PrimitiveIterator<T,T_CONS>,
        T_SPLITR extends Spliterator.OfPrimitive<T,T_CONS,T_SPLITR>,
        T_STR extends BaseStream<T,T_STR>,
        T_COLL extends PrimitiveCollection<T,T_ARR,T_CONS,T_PRED,T_ITER,T_SPLITR,T_STR,T_COLL>>
        extends Collection<T> {
    boolean add(T element);
    boolean addAll(Collection<? extends T> collection);
    boolean addAll(T_COLL collection);
    void clear();
    boolean contains(Object o);
    boolean containsAll(Collection<?> collection);
    boolean containsAll(T_COLL collection);
    boolean equals(Object o);
    default void forEach(Consumer<? super T> action) {
        spliterator().forEachRemaining(action);
    }
    default void forEach(T_CONS action) {
        spliterator().forEachRemaining(action);
    }
    int hashCode();
    default boolean isEmpty() {
        return size() == 0;
    }
    T_ITER iterator();
    default Stream<T> parallelStream() {
        return Collection.super.parallelStream();
    }
    T_STR parallelPrimitiveStream();
    T_STR primitiveStream();
    boolean remove(Object o);
    boolean removeAll(Collection<?> collection);
    boolean removeAll(T_COLL collection);
    boolean removeIf(T_PRED filter);
    boolean removeIf(Predicate<? super T> filter);
    boolean retainAll(Collection<?> collection);
    boolean retainAll(T_COLL collection);
    int size();
    T_SPLITR spliterator();
    default Stream<T> stream() {
        return Collection.super.stream();
    }
    default Object[] toArray() {
        return PrimitiveCollections.toArray(this);
    }
    default <U> U[] toArray(U[] a) {
        return PrimitiveCollections.toArray(this, a);
    }
    T_ARR toPrimitiveArray();
}
