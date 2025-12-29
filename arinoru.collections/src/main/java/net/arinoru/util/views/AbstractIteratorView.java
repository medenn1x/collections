package net.arinoru.util.views;

import java.util.Iterator;
import java.util.PrimitiveIterator;
import java.util.function.Consumer;

public abstract class AbstractIteratorView<E> implements View, Iterator<E> {
    protected abstract Forwarder<Iterator<?>,Iterator<E>> forwarder();

    @Override
    public void forEachRemaining(Consumer<? super E> action) {
        forwarder().voidOp(delegate -> delegate.forEachRemaining(action),
                () -> Iterator.super.forEachRemaining(action));
    }

    @Override
    public boolean hasNext() {
        return forwarder().asDelegateType().hasNext();
    }

    @SuppressWarnings("unchecked")
    private E delegateNext() {
        // Since we cannot access default methods, we bake in some logic for
        // primitive iterators at SHALLOW level of forwarding.
        var delegate = forwarder().asDelegateType();
        return switch (delegate) {
            case PrimitiveIterator.OfDouble iterator ->
                    (E) Double.valueOf(iterator.nextDouble());
            case PrimitiveIterator.OfInt iterator ->
                    (E) Integer.valueOf(iterator.nextInt());
            case PrimitiveIterator.OfLong iterator ->
                    (E) Long.valueOf(iterator.nextLong());
            case PrimitiveIterator<?,?> ignored ->
                throw new InvalidForwardingContextException("Internal error");
            default -> (E) delegate.next();
        };
    }

    @Override
    public E next() {
        return forwarder().boxedOp(Iterator::next, this::delegateNext);
    }

    @Override
    public void remove() {
        checkModifiable();
        forwarder().asDelegateType().remove();
    }
}
