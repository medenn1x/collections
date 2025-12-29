package net.arinoru.util.views;

import java.util.Iterator;

public class IteratorView<E> extends AbstractIteratorView<E> {
    private final Forwarder<Iterator<?>,Iterator<E>> forwarder;

    protected IteratorView(Iterator<?> delegate, ForwardingType forwardingType) {
        forwarder = new Forwarder<>(delegate, forwardingType,
                this instanceof UnmodifiableView);
    }

    @Override
    protected Forwarder<Iterator<?>,Iterator<E>> forwarder() {
        return forwarder;
    }

    public static <E> Iterator<E> unmodifiable(Iterator<E> iterator) {
        return switch (iterator) {
            case UnmodifiableView ignored -> iterator;
            case java.io.Serializable ignored ->
                new SerializableUnmodifiable<>(iterator, ForwardingType.PURE);
            default -> new Unmodifiable<>(iterator, ForwardingType.PURE);
        };
    }

    public static class Serializable<E> extends AbstractIteratorView<E>
            implements java.io.Serializable {
        private final Forwarder<Iterator<?>,Iterator<E>> forwarder;

        protected Serializable(Iterator<?> delegate, ForwardingType forwardingType) {
            forwarder = new Forwarder<>(delegate, forwardingType,
                    this instanceof UnmodifiableView);
        }

        @Override
        protected Forwarder<Iterator<?>,Iterator<E>> forwarder() {
            return forwarder;
        }
    }

    public static class Unmodifiable<E> extends IteratorView<E>
            implements UnmodifiableView {
        protected Unmodifiable(Iterator<?> delegate, ForwardingType forwardingType) {
            super(delegate, forwardingType);
        }
    }

    public static class SerializableUnmodifiable<E> extends Serializable<E>
            implements UnmodifiableView {
        protected SerializableUnmodifiable(Iterator<?> delegate, ForwardingType forwardingType) {
            super(delegate, forwardingType);
        }
    }
}
