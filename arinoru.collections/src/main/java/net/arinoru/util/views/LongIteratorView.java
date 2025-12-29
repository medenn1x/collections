package net.arinoru.util.views;

import java.util.Iterator;
import java.util.PrimitiveIterator;

public class LongIteratorView extends AbstractLongIteratorView {
    private final Forwarder<Iterator<?>, PrimitiveIterator.OfLong> forwarder;

    protected LongIteratorView(Iterator<?> delegate, ForwardingType forwardingType) {
        forwarder = new Forwarder<>(delegate, forwardingType,
                this instanceof UnmodifiableView);
    }

    @Override
    protected Forwarder<Iterator<?>,PrimitiveIterator.OfLong> forwarder() {
        return forwarder;
    }

    public static PrimitiveIterator.OfLong unmodifiable(PrimitiveIterator.OfLong iterator) {
        return switch (iterator) {
            case UnmodifiableView ignored -> iterator;
            case java.io.Serializable ignored ->
                new SerializableUnmodifiable(iterator, ForwardingType.PURE);
            default -> new Unmodifiable(iterator, ForwardingType.PURE);
        };
    }

    public static class Serializable extends AbstractLongIteratorView
            implements java.io.Serializable {
        private final Forwarder<Iterator<?>,PrimitiveIterator.OfLong> forwarder;

        protected Serializable(Iterator<?> delegate, ForwardingType forwardingType) {
            forwarder = new Forwarder<>(delegate, forwardingType,
                    this instanceof UnmodifiableView);
        }

        @Override
        protected Forwarder<Iterator<?>,PrimitiveIterator.OfLong> forwarder() {
            return forwarder;
        }
    }

    public static class Unmodifiable extends LongIteratorView implements UnmodifiableView {
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
