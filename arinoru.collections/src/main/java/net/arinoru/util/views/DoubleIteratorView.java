package net.arinoru.util.views;

import java.util.Iterator;
import java.util.PrimitiveIterator;

public class DoubleIteratorView extends AbstractDoubleIteratorView {
    private final Forwarder<Iterator<?>, PrimitiveIterator.OfDouble> forwarder;

    protected DoubleIteratorView(Iterator<?> delegate, ForwardingType forwardingType) {
        forwarder = new Forwarder<>(delegate, forwardingType,
                this instanceof UnmodifiableView);
    }

    @Override
    protected Forwarder<Iterator<?>,PrimitiveIterator.OfDouble> forwarder() {
        return forwarder;
    }

    public static PrimitiveIterator.OfDouble unmodifiable(PrimitiveIterator.OfDouble iterator) {
        return switch (iterator) {
            case UnmodifiableView ignored -> iterator;
            case java.io.Serializable ignored ->
                new SerializableUnmodifiable(iterator, ForwardingType.PURE);
            default -> new Unmodifiable(iterator, ForwardingType.PURE);
        };
    }

    public static class Serializable extends AbstractDoubleIteratorView
            implements java.io.Serializable {
        private final Forwarder<Iterator<?>,PrimitiveIterator.OfDouble> forwarder;

        protected Serializable(Iterator<?> delegate, ForwardingType forwardingType) {
            forwarder = new Forwarder<>(delegate, forwardingType,
                    this instanceof UnmodifiableView);
        }

        @Override
        protected Forwarder<Iterator<?>,PrimitiveIterator.OfDouble> forwarder() {
            return forwarder;
        }
    }

    public static class Unmodifiable extends DoubleIteratorView
            implements UnmodifiableView {
        protected Unmodifiable(Iterator<?> delegate, ForwardingType forwardingType) {
            super(delegate, forwardingType);
        }
    }

    public static class SerializableUnmodifiable extends Serializable
            implements UnmodifiableView {
        protected SerializableUnmodifiable(Iterator<?> delegate, ForwardingType forwardingType) {
            super(delegate, forwardingType);
        }
    }
}
