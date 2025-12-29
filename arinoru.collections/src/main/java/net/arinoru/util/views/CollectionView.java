package net.arinoru.util.views;

import java.util.Collection;

public class CollectionView<E> extends AbstractCollectionView<E> {
    private final Forwarder<Collection<?>,Collection<E>> forwarder;

    protected CollectionView(Collection<?> delegate, ForwardingType forwardingType) {
        forwarder = new Forwarder<>(delegate, forwardingType,
                this instanceof UnmodifiableView);
    }

    @Override
    protected Forwarder<Collection<?>,Collection<E>> forwarder() {
        return forwarder;
    }

    public static <E> Collection<E> unmodifiable(Collection<E> collection) {
        return switch (collection) {
            case UnmodifiableView ignored -> collection;
            case java.io.Serializable ignored ->
                new SerializableUnmodifiable<>(collection, ForwardingType.PURE);
            default -> new Unmodifiable<>(collection, ForwardingType.PURE);
        };
    }

    public static class Serializable<E> extends AbstractCollectionView<E>
            implements java.io.Serializable {
        private final Forwarder<Collection<?>,Collection<E>> forwarder;

        protected Serializable(Collection<?> delegate, ForwardingType forwardingType) {
            forwarder = new Forwarder<>(delegate, forwardingType,
                    this instanceof UnmodifiableView);
        }

        @Override
        protected Forwarder<Collection<?>,Collection<E>> forwarder() {
            return forwarder;
        }
    }

    public static class Unmodifiable<E> extends CollectionView<E>
            implements UnmodifiableView {
        protected Unmodifiable(Collection<?> delegate, ForwardingType forwardingType) {
            super(delegate, forwardingType);
        }
    }

    public static class SerializableUnmodifiable<E> extends Serializable<E>
            implements UnmodifiableView {
        protected SerializableUnmodifiable(Collection<?> delegate, ForwardingType forwardingType) {
            super(delegate, forwardingType);
        }
    }
}
