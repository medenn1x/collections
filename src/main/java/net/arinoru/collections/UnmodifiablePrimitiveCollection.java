package net.arinoru.collections;

import net.arinoru.prerelease.PrereleaseContent;

import java.util.Spliterator;
import java.util.stream.BaseStream;

@PrereleaseContent
abstract class UnmodifiablePrimitiveCollection<T,T_ARR,T_CONS,T_PRED,
        T_SPLITR extends Spliterator.OfPrimitive<T,T_CONS,T_SPLITR>,
        T_STR extends BaseStream<?,?>,
        T_COLL extends PrimitiveCollection<T,T_ARR,T_CONS,T_PRED,T_SPLITR,T_STR>>
        extends UnmodifiableCollection<T>
        implements PrimitiveCollection<T,T_ARR,T_CONS,T_PRED,T_SPLITR,T_STR> {
    public boolean addAll(T_COLL ignored) {
        throw new UnsupportedOperationException();
    }

    public boolean removeAll(T_COLL ignored) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeIf(T_PRED filter) {
        throw new UnsupportedOperationException();
    }

    public boolean retainAll(T_COLL ignored) {
        throw new UnsupportedOperationException();
    }
}
