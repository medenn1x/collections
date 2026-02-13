package net.arinoru.util.views;

import java.util.Collection;

/**
 * <p>An internal implementation of a collection view. In the normal course, views
 * should extend either this class or one of its nested classes and provide their
 * own functionality on top. Some nested classes may also be useful in their own
 * right for providing simple unmodifiable views.</p>
 * <p>While this class is not declared {@code abstract}, it is unlikely to be
 * useful without being extended. It is provided as an alternative to
 * {@link AbstractCollectionView} as most implementing classes will neither need
 * nor want to manage their own {@link Forwarder}.</p>
 * @param <E> The element type of the collection
 */
public class CollectionView<E> extends AbstractCollectionView<E> {
    private final Forwarder<Collection<?>,Collection<E>> forwarder;

    /**
     * <p>Create a collection view with the specified backing set and forwarding
     * type.</p>
     * @param delegate the backing collection whose elements are represented by
     *                 the view
     * @param forwardingType the default forward behavior for collection operations
     *                       on the view
     */
    protected CollectionView(Collection<?> delegate, ForwardingType forwardingType) {
        forwarder = new Forwarder<>(delegate, forwardingType,
                this instanceof UnmodifiableView);
    }

    /**
     * <p>Returns the {@link Forwarder} managing access to the backing
     * collection.</p>
     * @return forward for the backing collection
     */
    @Override
    protected Forwarder<Collection<?>,Collection<E>> forwarder() {
        return forwarder;
    }

    /**
     * <p>An internal implementation of a (possibly serializable) collection view.
     * If the backing collection is serializable, this view will also be
     * serializable.</p>
     * <p>While this class is not declared {@code abstract}, it is unlikely to be
     * useful without being extended. It is provided as an alternative to
     * {@link AbstractCollectionView} as most implementing classes will neither
     * need nor want to manage their own {@link Forwarder}.</p>
     * @param <E> the element type of the collection
     */
    public static class Serializable<E> extends AbstractCollectionView<E>
            implements java.io.Serializable {
        private final Forwarder<Collection<?>,Collection<E>> forwarder;

        /**
         * <p>Create a (possibly serializable) collection view with the specified
         * backing collection and forward type.</p>
         * @param delegate the backing collection whose elements are represented by
         *                 the view. If this collection is serializable, the view
         *                 will also be serializable.
         * @param forwardingType the default forward behavior for collection
         *                       operations on the view
         */
        protected Serializable(Collection<?> delegate, ForwardingType forwardingType) {
            forwarder = new Forwarder<>(delegate, forwardingType,
                    this instanceof UnmodifiableView);
        }

        /**
         * <p>Returns the {@link Forwarder} managing access to the backing
         * collection.</p>
         * @return forward for the backing collection
         */
        @Override
        protected Forwarder<Collection<?>,Collection<E>> forwarder() {
            return forwarder;
        }
    }

    /**
     * <p>An internal implementation of an unmodifiable collection view.</p>
     * @param <E> the element type of the collection
     */
    public static class Unmodifiable<E> extends CollectionView<E>
            implements UnmodifiableView {
        /**
         * <p>Create an unmodifiable collection view with the specified
         * backing collection and forwarding type.</p>
         * @param delegate the backing collection whose elements are
         *                 represented by the view
         * @param forwardingType the default forwarding behavior for collection
         *                       operations on the view
         */
        protected Unmodifiable(Collection<?> delegate, ForwardingType forwardingType) {
            super(delegate, forwardingType);
        }
    }

    /**
     * <p>An internal implementation of an unmodifiable (possible serializable)
     * collection view. If the backing collection is serializable, this view will
     * also be serializable.</p>
     * @param <E> the element type of the collection
     */
    public static class SerializableUnmodifiable<E> extends Serializable<E>
            implements UnmodifiableView {
        /**
         * <p>Create an unmodifiable (possibly serializable) collection view
         * with the specified backing collection and forwarding type.</p>
         * @param delegate the backing collection whose elements are represented
         *                  by the view. If this collection is serializable,
         *                 the view will also be serializable.
         * @param forwardingType the default forwarding behavior for collection
         *                       operations on the view
         */
        protected SerializableUnmodifiable(Collection<?> delegate, ForwardingType forwardingType) {
            super(delegate, forwardingType);
        }
    }
}
