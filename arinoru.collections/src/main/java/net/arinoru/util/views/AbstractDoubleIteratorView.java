package net.arinoru.util.views;

import java.util.Iterator;
import java.util.PrimitiveIterator;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;

public abstract class AbstractDoubleIteratorView
        implements View, PrimitiveIterator.OfDouble {
    protected abstract Forwarder<Iterator<?>,PrimitiveIterator.OfDouble> forwarder();

    @Override
    public void forEachRemaining(Consumer<? super Double> action) {
        forwarder().voidOp(delegate -> delegate.forEachRemaining(action),
                () -> OfDouble.super.forEachRemaining(action));
    }

    @Override
    public void forEachRemaining(DoubleConsumer action) {
        forwarder().voidOp(delegate -> delegate.forEachRemaining(action),
                () -> OfDouble.super.forEachRemaining(action));
    }

    @Override
    public boolean hasNext() {
        return forwarder().asDelegateType().hasNext();
    }

    @Override
    public Double next() {
        return forwarder().boxedOp(OfDouble::next, OfDouble.super::next);
    }

    @Override
    public double nextDouble() {
        return forwarder().doubleOp(OfDouble::nextDouble);
    }

    @Override
    public void remove() {
        checkModifiable();
        forwarder().asDelegateType().remove();
    }
}
