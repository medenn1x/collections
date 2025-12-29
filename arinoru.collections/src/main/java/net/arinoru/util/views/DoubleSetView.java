package net.arinoru.util.views;

import net.arinoru.util.DoubleSet;

import java.util.Set;

public class DoubleSetView extends AbstractDoubleSetView {
    private final Forwarder<Set<?>,DoubleSet> forwarder;

    protected DoubleSetView(Set<?> delegate, ForwardingType forwardingType) {
        forwarder = new Forwarder<>(delegate, forwardingType,
                this instanceof UnmodifiableView);
    }

    @Override
    protected Forwarder<Set<?>,DoubleSet> forwarder() {
        return forwarder;
    }

    public static DoubleSet unmodifiable(DoubleSet set) {
        return switch (set) {
            case UnmodifiableView ignored -> set;
            case java.io.Serializable ignored ->
                new SerializableUnmodifiable(set, ForwardingType.PURE);
            default -> new Unmodifiable(set, ForwardingType.PURE);
        };
    }

    public static class Serializable extends AbstractDoubleSetView
            implements java.io.Serializable {
        private final Forwarder<Set<?>,DoubleSet> forwarder;

        protected Serializable(Set<?> delegate, ForwardingType forwardingType) {
            forwarder = new Forwarder<>(delegate, forwardingType,
                    this instanceof UnmodifiableView);
        }

        @Override
        protected Forwarder<Set<?>,DoubleSet> forwarder() {
            return forwarder;
        }
    }

    public static class Unmodifiable extends DoubleSetView implements UnmodifiableView {
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
