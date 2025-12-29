package net.arinoru.util.impl;

import net.arinoru.util.PrimitiveCollection;
import net.arinoru.util.PrimitiveSet;

import java.io.Serializable;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.function.IntFunction;
import java.util.stream.BaseStream;
import java.util.stream.Stream;

public abstract class PrimitiveSingleton<T,T_ARR,T_CONS,T_PRED,
        T_ITER extends PrimitiveIterator<T,T_CONS>,
        T_SPLITR extends Spliterator.OfPrimitive<T,T_CONS,T_SPLITR>,
        T_STR extends BaseStream<T,T_STR>,
        T_COLL extends PrimitiveCollection<T,T_ARR,T_CONS,T_PRED,T_ITER,T_SPLITR,T_STR,T_COLL>>
        extends UnmodifiablePrimitiveCollection<T,T_ARR,T_CONS,T_PRED,T_ITER,T_SPLITR,T_STR,T_COLL>
        implements PrimitiveSet<T,T_ARR,T_CONS,T_PRED,T_ITER,T_SPLITR,T_STR,T_COLL>, Serializable {
    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public T_STR parallelPrimitiveStream() {
        return primitiveStream();
    }

    @Override
    public Stream<T> parallelStream() {
        return stream();
    }

    @Override
    public <U> U[] toArray(IntFunction<U[]> generator) {
        return toArray(generator.apply(1));
    }
}
