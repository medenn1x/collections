package net.arinoru.util.views;

import net.arinoru.util.LongCollection;

import java.util.Collection;

public class LongCollectionView extends AbstractLongCollectionView {
    private final Forwarder<Collection<?>, LongCollection> forwarder;

    protected LongCollectionView(Collection<?> delegate, ForwardingType forwardingType) {
        forwarder = new Forwarder<>(delegate, forwardingType,
                this instanceof UnmodifiableView);
    }

    @Override
    protected Forwarder<Collection<?>,LongCollection> forwarder() {
        return forwarder;
    }

    public static LongCollection unmodifiable(LongCollection collection) {
        return switch (collection) {
            case UnmodifiableView ignored -> collection;
            case java.io.Serializable ignored ->
                new SerializableUnmodifiable(collection, ForwardingType.PURE);
            default -> new Unmodifiable(collection, ForwardingType.PURE);
        };
    }

    public static class Serializable extends AbstractLongCollectionView
            implements java.io.Serializable {
        private final Forwarder<Collection<?>,LongCollection> forwarder;

        protected Serializable(Collection<?> delegate, ForwardingType forwardingType) {
            forwarder = new Forwarder<>(delegate, forwardingType,
                    this instanceof UnmodifiableView);
        }

        @Override
        protected Forwarder<Collection<?>,LongCollection> forwarder() {
            return forwarder;
        }
    }

    public static class Unmodifiable extends LongCollectionView
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
