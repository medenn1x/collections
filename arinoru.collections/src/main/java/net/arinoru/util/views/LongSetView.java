package net.arinoru.util.views;

import net.arinoru.util.LongSet;

import java.util.Set;

public class LongSetView extends AbstractLongSetView {
    private final Forwarder<Set<?>,LongSet> forwarder;

    protected LongSetView(Set<?> delegate, ForwardingType forwardingType) {
        forwarder = new Forwarder<>(delegate, forwardingType,
                this instanceof UnmodifiableView);
    }

    @Override
    protected Forwarder<Set<?>,LongSet> forwarder() {
        return forwarder;
    }

    public static LongSet unmodifiable(LongSet set) {
        return switch (set) {
            case UnmodifiableView ignored -> set;
            case java.io.Serializable ignored ->
                new SerializableUnmodifiable(set, ForwardingType.PURE);
            default -> new Unmodifiable(set, ForwardingType.PURE);
        };
    }

    public static class Serializable extends AbstractLongSetView
            implements java.io.Serializable {
        private final Forwarder<Set<?>,LongSet> forwarder;

        protected Serializable(Set<?> delegate, ForwardingType forwardingType) {
            forwarder = new Forwarder<>(delegate, forwardingType,
                    this instanceof UnmodifiableView);
        }

        @Override
        protected Forwarder<Set<?>,LongSet> forwarder() {
            return forwarder;
        }
    }

    public static class Unmodifiable extends LongSetView implements UnmodifiableView {
        protected Unmodifiable(Set<?> delegate, ForwardingType forwardingType) {
            super(delegate, forwardingType);
        }
    }

    public static class SerializableUnmodifiable extends Serializable
            implements UnmodifiableView {
        protected SerializableUnmodifiable(Set<?> delegate, ForwardingType forwardingType) {
            super(delegate, forwardingType);
        }
    }
}
