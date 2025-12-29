package net.arinoru.util.impl;

import net.arinoru.util.PrimitiveCollection;

import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.stream.BaseStream;

public abstract class UnmodifiablePrimitiveCollection<T,T_ARR,T_CONS,T_PRED,
        T_ITER extends PrimitiveIterator<T,T_CONS>,
        T_SPLITR extends Spliterator.OfPrimitive<T,T_CONS,T_SPLITR>,
        T_STR extends BaseStream<T,T_STR>,
        T_COLL extends PrimitiveCollection<T,T_ARR,T_CONS,T_PRED,T_ITER,T_SPLITR,T_STR,T_COLL>>
        extends UnmodifiableCollection<T>
        implements PrimitiveCollection<T,T_ARR,T_CONS,T_PRED,T_ITER,T_SPLITR,T_STR,T_COLL> {
    @Override
    public boolean addAll(T_COLL collection) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(T_COLL collection) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeIf(T_PRED filter) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(T_COLL collection) {
        throw new UnsupportedOperationException();
    }
}
