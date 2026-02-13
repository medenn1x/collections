package net.arinoru.util.views;

import java.util.Set;

/**
 * <p>An internal implementation of a set view. In the normal course, views should
 * extend either this class or one of its nested classes and provide their own
 * functionality on top. Some nested classes may also be useful in their own right
 * for providing simple unmodifiable views.</p>
 * <p>While this class is not declared {@code abstract}, it is unlikely to be
 * useful without being extended. It is provided as an alternative to
 * {@link AbstractSetView} as most implementing classes will neither need nor want
 * to manage their own {@link Forwarder}.</p>
 * @param <E> The element type of the set
 */
public class SetView<E> extends AbstractSetView<E> {
    private final Forwarder<Set<?>,Set<E>> forwarder;

    /**
     * <p>Create a set view with the specified backing set and forwarding type.</p>
     * @param delegate the backing set whose elements are represented by the view
     * @param forwardingType the default forwarding behavior for set operations on
     *                       the view
     */
    protected SetView(Set<?> delegate, ForwardingType forwardingType) {
        forwarder = new Forwarder<>(delegate, forwardingType,
                this instanceof UnmodifiableView);
    }

    /**
     * <p>Returns the {@link Forwarder} managing access to the backing set.</p>
     * @return forwarder for the backing set
     */
    @Override
    protected Forwarder<Set<?>,Set<E>> forwarder() {
        return forwarder;
    }

    /**
     * <p>An internal implementation of a (possibly serializable) set view. If the
     * backing set is serializable, this view will also be serializable.</p>
     * <p>While this class is not declared {@code abstract}, it is unlikely to be
     * useful without being extended. It is provided as an alternative to
     * {@link AbstractSetView} as most implementing classes will neither need nor
     * want to manage their own {@link Forwarder}.</p>
     * @param <E> the element type of the set
     */
    public static class Serializable<E> extends AbstractSetView<E>
            implements java.io.Serializable {
        private final Forwarder<Set<?>,Set<E>> forwarder;

        /**
         * <p>Create a (possibly serializable) set view with the specified backing
         * set and forwarding type.</p>
         * @param delegate the backing set whose elements are represented by the
         *                 view. If this set is serializable, the view will also
         *                 be serializable.
         * @param forwardingType the default forwarding behavior for set
         *                      operations on the view
         */
        protected Serializable(Set<?> delegate, ForwardingType forwardingType) {
            forwarder = new Forwarder<>(delegate, forwardingType,
                    this instanceof UnmodifiableView);
        }

        /**
         * <p>Returns the {@link Forwarder} managing access to the backing set.</p>
         * @return forwarder for the backing set
         */
        @Override
        protected Forwarder<Set<?>,Set<E>> forwarder() {
            return forwarder;
        }
    }

    /**
     * <p>An internal implementation of an unmodifiable set view.</p>
     * @param <E> the element type of the set
     */
    public static class Unmodifiable<E> extends SetView<E> implements UnmodifiableView {
        /**
         * <p>Create an unmodifiable set view with the specified backing set and
         * forwarding type.</p>
         * @param delegate the backing set whose elements are represented by the
         *                 view.
         * @param forwardingType the default forwarding behavior for set
         *                       operations on the view
         */
        protected Unmodifiable(Set<?> delegate, ForwardingType forwardingType) {
            super(delegate, forwardingType);
        }
    }

    /**
     * <p>An internal implementation of an unmodifiable (possibly serializable)
     * set view. If the backing set is serializable, this view will also be
     * serializable.</p>
     * @param <E> the element type of the set
     */
    public static class SerializableUnmodifiable<E> extends Serializable<E>
            implements UnmodifiableView {
        /**
         * <p>Create an unmodifiable (possibly serializable) set view with the
         * specified backing set and forwarding type.</p>
         * @param delegate the backing set whose elements are represented by the
         *                 view. If this set is serializable, the view will also
         *                 be serializable.
         * @param forwardingType the default forwarding behavior for set
         *                      operations on the view
         */
        protected SerializableUnmodifiable(Set<?> delegate, ForwardingType forwardingType) {
            super(delegate, forwardingType);
        }
    }
}
