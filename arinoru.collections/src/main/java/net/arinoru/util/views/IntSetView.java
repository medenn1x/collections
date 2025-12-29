package net.arinoru.util.views;

import net.arinoru.util.IntSet;

import java.util.Set;

public class IntSetView extends AbstractIntSetView {
    private final Forwarder<Set<?>,IntSet> forwarder;

    protected IntSetView(Set<?> delegate, ForwardingType forwardingType) {
        forwarder = new Forwarder<>(delegate, forwardingType,
                this instanceof UnmodifiableView);
    }

    @Override
    protected Forwarder<Set<?>,IntSet> forwarder() {
        return forwarder;
    }

    public static IntSet unmodifiable(IntSet set) {
        return switch (set) {
            case UnmodifiableView ignored -> set;
            case java.io.Serializable ignored ->
                new SerializableUnmodifiable(set, ForwardingType.PURE);
            default -> new Unmodifiable(set, ForwardingType.PURE);
        };
    }

    public static class Serializable extends AbstractIntSetView
            implements java.io.Serializable {
        private final Forwarder<Set<?>,IntSet> forwarder;

        protected Serializable(Set<?> delegate, ForwardingType forwardingType) {
            forwarder = new Forwarder<>(delegate, forwardingType,
                    this instanceof UnmodifiableView);
        }

        @Override
        protected Forwarder<Set<?>,IntSet> forwarder() {
            return forwarder;
        }
    }

    public static class Unmodifiable extends IntSetView implements UnmodifiableView {
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
