package net.arinoru.util.views;

import java.util.Iterator;
import java.util.PrimitiveIterator;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

public abstract class AbstractIntIteratorView implements View, PrimitiveIterator.OfInt {
    protected abstract Forwarder<Iterator<?>,PrimitiveIterator.OfInt> forwarder();

    @Override
    public void forEachRemaining(Consumer<? super Integer> action) {
        forwarder().voidOp(delegate -> delegate.forEachRemaining(action),
                () -> OfInt.super.forEachRemaining(action));
    }

    @Override
    public void forEachRemaining(IntConsumer action) {
        forwarder().voidOp(delegate -> delegate.forEachRemaining(action),
                () -> OfInt.super.forEachRemaining(action));
    }

    @Override
    public boolean hasNext() {
        return forwarder().asDelegateType().hasNext();
    }

    @Override
    public Integer next() {
        return forwarder().boxedOp(OfInt::next, OfInt.super::next);
    }

    @Override
    public int nextInt() {
        return forwarder().intOp(OfInt::nextInt);
    }

    @Override
    public void remove() {
        checkModifiable();
        forwarder().asDelegateType().remove();
    }
}
