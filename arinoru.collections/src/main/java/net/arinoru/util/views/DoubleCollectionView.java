package net.arinoru.util.views;

import net.arinoru.util.DoubleCollection;

import java.util.Collection;

public class DoubleCollectionView extends AbstractDoubleCollectionView {
    private final Forwarder<Collection<?>, DoubleCollection> forwarder;

    protected DoubleCollectionView(Collection<?> delegate, ForwardingType forwardingType) {
        forwarder = new Forwarder<>(delegate, forwardingType,
                this instanceof UnmodifiableView);
    }

    @Override
    protected Forwarder<Collection<?>,DoubleCollection> forwarder() {
        return forwarder;
    }

    public static DoubleCollection unmodifiable(DoubleCollection collection) {
        return switch (collection) {
            case UnmodifiableView ignored -> collection;
            case java.io.Serializable ignored ->
                new SerializableUnmodifiable(collection, ForwardingType.PURE);
            default -> new Unmodifiable(collection, ForwardingType.PURE);
        };
    }

    public static class Serializable extends AbstractDoubleCollectionView
            implements java.io.Serializable {
        private final Forwarder<Collection<?>,DoubleCollection> forwarder;

        protected Serializable(Collection<?> collection, ForwardingType forwardingType) {
            forwarder = new Forwarder<>(collection, forwardingType,
                    this instanceof UnmodifiableView);
        }

        @Override
        protected Forwarder<Collection<?>,DoubleCollection> forwarder() {
            return forwarder;
        }
    }

    public static class Unmodifiable extends DoubleCollectionView
            implements UnmodifiableView {
        protected Unmodifiable(Collection<?> collection, ForwardingType forwardingType) {
            super(collection, forwardingType);
        }
    }

    public static class SerializableUnmodifiable extends Serializable
            implements UnmodifiableView {
        protected SerializableUnmodifiable(Collection<?> collection, ForwardingType forwardingType) {
            super(collection, forwardingType);
        }
    }
}
