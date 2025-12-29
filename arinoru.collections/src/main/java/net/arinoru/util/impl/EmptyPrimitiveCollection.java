package net.arinoru.util.impl;

import net.arinoru.util.PrimitiveCollection;

import java.util.Collection;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.stream.BaseStream;
import java.util.stream.Stream;

public abstract class EmptyPrimitiveCollection<T,T_ARR,T_CONS,T_PRED,
        T_ITER extends PrimitiveIterator<T,T_CONS>,
        T_SPLITR extends Spliterator.OfPrimitive<T,T_CONS,T_SPLITR>,
        T_STR extends BaseStream<T,T_STR>,
        T_COLL extends PrimitiveCollection<T,T_ARR,T_CONS,T_PRED,T_ITER,T_SPLITR,T_STR,T_COLL>>
        extends UnmodifiablePrimitiveCollection<T,T_ARR,T_CONS,T_PRED,T_ITER,T_SPLITR,T_STR,T_COLL> {
    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return collection.isEmpty();
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public abstract T_ITER iterator();

    @Override
    public T_STR parallelPrimitiveStream() {
        return primitiveStream();
    }

    @Override
    public Stream<T> parallelStream() {
        return Stream.empty();
    }

    @Override
    public abstract T_STR primitiveStream();

    @Override
    public int size() {
        return 0;
    }

    @Override
    public abstract T_SPLITR spliterator();

    @Override
    public Stream<T> stream() {
        return Stream.empty();
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <U> U[] toArray(U[] a) {
        if (a.length > 0)
            a[0] = null;
        return a;
    }

    @Override
    public abstract T_ARR toPrimitiveArray();
}
