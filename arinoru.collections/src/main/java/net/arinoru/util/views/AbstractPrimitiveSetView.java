package net.arinoru.util.views;

import net.arinoru.util.PrimitiveCollection;
import net.arinoru.util.PrimitiveCollections;
import net.arinoru.util.PrimitiveSet;

import java.util.PrimitiveIterator;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.stream.BaseStream;
import java.util.stream.Stream;

public abstract class AbstractPrimitiveSetView<T,T_ARR,T_CONS,T_PRED,
        T_ITER extends PrimitiveIterator<T,T_CONS>,
        T_SPLITR extends Spliterator.OfPrimitive<T,T_CONS,T_SPLITR>,
        T_STR extends BaseStream<T,T_STR>,
        T_COLL extends PrimitiveCollection<T,T_ARR,T_CONS,T_PRED,T_ITER,T_SPLITR,T_STR,T_COLL>,
        T_SET extends PrimitiveSet<T,T_ARR,T_CONS,T_PRED,T_ITER,T_SPLITR,T_STR,T_COLL>>
        implements View, PrimitiveSet<T,T_ARR,T_CONS,T_PRED,T_ITER,T_SPLITR,T_STR,T_COLL> {
    protected abstract Forwarder<Set<?>,T_SET> forwarder();

    @Override
    @SuppressWarnings("EqualsDoesntCheckParameterClass")
    public boolean equals(Object o) {
        return forwarder().predicateOp(delegate -> delegate.equals(o),
                () -> PrimitiveCollections.equals(this, o));
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        forwarder().voidOp(delegate -> delegate.forEach(action),
                () -> PrimitiveSet.super.forEach(action));
    }

    @Override
    public void forEach(T_CONS action) {
        forwarder().voidOp(delegate -> delegate.forEach(action),
                () -> PrimitiveSet.super.forEach(action));
    }

    @Override
    public int hashCode() {
        return forwarder().intOp(PrimitiveSet::hashCode,
                () -> PrimitiveCollections.hashCode(this));
    }

    @Override
    public boolean isEmpty() {
        return forwarder().predicateOp(PrimitiveSet::isEmpty,
                PrimitiveSet.super::isEmpty);
    }

    @Override
    public Stream<T> parallelStream() {
        return forwarder().boxedOp(PrimitiveSet::parallelStream,
                PrimitiveSet.super::parallelStream);
    }

    @Override
    public int size() {
        return forwarder().asDelegateType().size();
    }

    @Override
    public Stream<T> stream() {
        return forwarder().boxedOp(PrimitiveSet::stream, PrimitiveSet.super::stream);
    }

    @Override
    public Object[] toArray() {
        return forwarder().boxedOp(PrimitiveSet::toArray, PrimitiveSet.super::toArray);
    }

    @Override
    public <U> U[] toArray(IntFunction<U[]> generator) {
        return forwarder().boxedOp(delegate -> delegate.toArray(generator),
                () -> PrimitiveSet.super.toArray(generator));
    }

    @Override
    public <U> U[] toArray(U[] a) {
        return forwarder().boxedOp(delegate -> delegate.toArray(a),
                () -> PrimitiveSet.super.toArray(a));
    }
}
