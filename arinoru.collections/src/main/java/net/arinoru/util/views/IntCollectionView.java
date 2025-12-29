package net.arinoru.util.views;

import net.arinoru.util.IntCollection;

import java.util.Collection;

public class IntCollectionView extends AbstractIntCollectionView {
    private final Forwarder<Collection<?>,IntCollection> forwarder;

    protected IntCollectionView(Collection<?> delegate, ForwardingType forwardingType) {
        forwarder = new Forwarder<>(delegate, forwardingType,
                this instanceof UnmodifiableView);
    }

    @Override
    protected Forwarder<Collection<?>,IntCollection> forwarder() {
        return forwarder;
    }

    public static IntCollection unmodifiable(IntCollection collection) {
        return switch (collection) {
            case UnmodifiableView ignored -> collection;
            case java.io.Serializable ignored ->
                new SerializableUnmodifiable(collection, ForwardingType.PURE);
            default -> new Serializable(collection, ForwardingType.PURE);
        };
    }

    public static class Serializable extends AbstractIntCollectionView
            implements java.io.Serializable {
        private final Forwarder<Collection<?>,IntCollection> forwarder;

        protected Serializable(Collection<?> delegate, ForwardingType forwardingType) {
            forwarder = new Forwarder<>(delegate, forwardingType,
                    this instanceof UnmodifiableView);
        }

        @Override
        protected Forwarder<Collection<?>,IntCollection> forwarder() {
            return forwarder;
        }
    }

    public static class Unmodifiable extends IntCollectionView
            implements UnmodifiableView {
        protected Unmodifiable(Collection<?> delegate, ForwardingType forwardingType) {
            super(delegate, forwardingType);
        }
    }

    public static class SerializableUnmodifiable extends Serializable
            implements UnmodifiableView {
        protected SerializableUnmodifiable(Collection<?> delegate, ForwardingType forwardingType) {
            super(delegate, forwardingType);
        }
    }
}
