package net.arinoru.util.views;

import net.arinoru.util.PrimitiveCollection;

import java.util.Collection;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.stream.BaseStream;
import java.util.stream.Stream;

public abstract class AbstractPrimitiveCollectionView<T,T_ARR,T_CONS,T_PRED,
        T_ITER extends PrimitiveIterator<T,T_CONS>,
        T_SPLITR extends Spliterator.OfPrimitive<T,T_CONS,T_SPLITR>,
        T_STR extends BaseStream<T,T_STR>,
        T_COLL extends PrimitiveCollection<T,T_ARR,T_CONS,T_PRED,T_ITER,T_SPLITR,T_STR,T_COLL>>
        implements View, PrimitiveCollection<T,T_ARR,T_CONS,T_PRED,T_ITER,T_SPLITR,T_STR,T_COLL> {
    protected abstract Forwarder<Collection<?>,T_COLL> forwarder();

    @Override
    public void forEach(Consumer<? super T> action) {
        forwarder().voidOp(delegate -> delegate.forEach(action),
                () -> PrimitiveCollection.super.forEach(action));
    }

    @Override
    public void forEach(T_CONS action) {
        forwarder().voidOp(delegate -> delegate.forEach(action),
                () -> PrimitiveCollection.super.forEach(action));
    }

    @Override
    public int hashCode() {
        return forwarder().intOp(Object::hashCode, super::hashCode);
    }

    @Override
    public boolean isEmpty() {
        return forwarder().predicateOp(PrimitiveCollection::isEmpty,
                PrimitiveCollection.super::isEmpty);
    }

    @Override
    public Stream<T> parallelStream() {
        return forwarder().boxedOp(PrimitiveCollection::parallelStream,
                PrimitiveCollection.super::parallelStream);
    }

    @Override
    public int size() {
        return forwarder().asDelegateType().size();
    }

    @Override
    public Stream<T> stream() {
        return forwarder().boxedOp(PrimitiveCollection::stream,
                PrimitiveCollection.super::stream);
    }

    @Override
    public Object[] toArray() {
        return forwarder().boxedOp(PrimitiveCollection::toArray,
                PrimitiveCollection.super::toArray);
    }

    @Override
    public <U> U[] toArray(IntFunction<U[]> generator) {
        return forwarder().boxedOp(delegate -> delegate.toArray(generator),
                () -> PrimitiveCollection.super.toArray(generator));
    }

    @Override
    public <U> U[] toArray(U[] a) {
        return forwarder().boxedOp(delegate -> delegate.toArray(a),
                () -> PrimitiveCollection.super.toArray(a));
    }
}
