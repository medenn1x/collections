package net.arinoru.util.views;

import java.util.Set;

public class SetView<E> extends AbstractSetView<E> {
    private final Forwarder<Set<?>,Set<E>> forwarder;

    protected SetView(Set<?> delegate, ForwardingType forwardingType) {
        forwarder = new Forwarder<>(delegate, forwardingType,
                this instanceof UnmodifiableView);
    }

    @Override
    protected Forwarder<Set<?>,Set<E>> forwarder() {
        return forwarder;
    }

    public static <E> Set<E> unmodifiable(Set<E> set, ForwardingType forwardingType) {
        return switch (set) {
            case UnmodifiableView ignored -> set;
            case java.io.Serializable ignored ->
                new SerializableUnmodifiable<>(set, forwardingType);
            default -> new Unmodifiable<>(set, forwardingType);
        };
    }

    public static class Serializable<E> extends AbstractSetView<E>
            implements java.io.Serializable {
        private final Forwarder<Set<?>,Set<E>> forwarder;

        protected Serializable(Set<?> delegate, ForwardingType forwardingType) {
            forwarder = new Forwarder<>(delegate, forwardingType,
                    this instanceof UnmodifiableView);
        }

        @Override
        protected Forwarder<Set<?>,Set<E>> forwarder() {
            return forwarder;
        }
    }

    public static class Unmodifiable<E> extends SetView<E> implements UnmodifiableView {
        protected Unmodifiable(Set<?> delegate, ForwardingType forwardingType) {
            super(delegate, forwardingType);
        }
    }

    public static class SerializableUnmodifiable<E> extends Serializable<E>
            implements UnmodifiableView {
        protected SerializableUnmodifiable(Set<?> delegate, ForwardingType forwardingType) {
            super(delegate, forwardingType);
        }
    }
}
