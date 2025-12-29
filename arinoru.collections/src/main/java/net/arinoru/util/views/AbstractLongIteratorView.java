package net.arinoru.util.views;

import java.util.Iterator;
import java.util.PrimitiveIterator;
import java.util.function.Consumer;
import java.util.function.LongConsumer;

public abstract class AbstractLongIteratorView
        implements View, PrimitiveIterator.OfLong {
    protected abstract Forwarder<Iterator<?>,PrimitiveIterator.OfLong> forwarder();

    @Override
    public void forEachRemaining(Consumer<? super Long> action) {
        forwarder().voidOp(delegate -> delegate.forEachRemaining(action),
                () -> OfLong.super.forEachRemaining(action));
    }

    @Override
    public void forEachRemaining(LongConsumer action) {
        forwarder().voidOp(delegate -> delegate.forEachRemaining(action),
                () -> OfLong.super.forEachRemaining(action));
    }

    @Override
    public boolean hasNext() {
        return forwarder().asDelegateType().hasNext();
    }

    @Override
    public Long next() {
        return forwarder().boxedOp(OfLong::next, OfLong.super::next);
    }

    @Override
    public long nextLong() {
        return forwarder().longOp(OfLong::nextLong);
    }

    @Override
    public void remove() {
        checkModifiable();
        forwarder().asDelegateType().remove();
    }
}
