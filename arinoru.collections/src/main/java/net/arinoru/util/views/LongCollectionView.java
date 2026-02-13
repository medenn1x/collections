package net.arinoru.util.views;

import net.arinoru.util.LongCollection;

import java.util.Collection;

/**
 * <p>An internal implementation of a long collection view. In the normal course,
 * views should extend either this class or one of its nested classes and provide
 * their own functionality on top. Some nested classes may also be useful in their
 * own right for providing simple unmodifiable views.</p>
 * <p>While this class is not declared {@code abstract}, it is unlikely to be
 * useful without being extended. It is provided as an alternative to
 * {@link AbstractLongCollectionView} as most implementing classes will neither
 * need nor want to manage their own {@link Forwarder}.</p>
 */
public class LongCollectionView extends AbstractLongCollectionView {
    private final Forwarder<Collection<?>, LongCollection> forwarder;

    /**
     * <p>Create a long collection view with the specified backing collection and
     * forward type.</p>
     * @apiNote <p>For correct default behavior of collection operations, either
     * the forwarding type must be {@link ForwardingType#MINIMAL}, or the backing
     * collection must be a {@link LongCollection}.</p>
     * @param delegate the backing collection whose elements are represented by
     *                 the view
     * @param forwardingType the default forwarding behavior for collection
     *                       operations on the view
     */
    protected LongCollectionView(Collection<?> delegate, ForwardingType forwardingType) {
        forwarder = new Forwarder<>(delegate, forwardingType,
                this instanceof UnmodifiableView);
    }

    /**
     * <p>Returns the {@link Forwarder} managing access to the backing
     * collection.</p>
     * @return forwarder for the backing collection
     */
    @Override
    protected Forwarder<Collection<?>,LongCollection> forwarder() {
        return forwarder;
    }

    /**
     * <p>Return an unmodifiable view of the specified collection. In general this
     * method will not wrap already unmodifiable views.</p>
     * @param collection collection to provide an unmodifiable view over
     * @return unmodifiable view
     */
    public static LongCollection unmodifiable(LongCollection collection) {
        return ViewImpl.unmodifiable(collection, Unmodifiable::new,
                SerializableUnmodifiable::new);
    }

    /**
     * <p>An internal implementation of a (possibly serializable) long collection
     * view. If the backing collection is serializable, this view will also be
     * serializable.</p>
     * <p>While this class is not declared {@code abstract}, it is unlikely to be
     * useful without being extended. It is provided as an alternative to
     * {@link AbstractLongCollectionView} as most implementing classes will
     * neither need nor want to manage their own {@link Forwarder}.</p>
     */
    public static class Serializable extends AbstractLongCollectionView
            implements java.io.Serializable {
        private final Forwarder<Collection<?>,LongCollection> forwarder;

        /**
         * <p>Create a (possibly serializable) long collection view with the
         * specified backing collection and forwarding type.</p>
         * @apiNote <p>For correct default behavior of collection operations,
         * either the forwarding type must be {@link ForwardingType#MINIMAL}, or
         * the backing collection must be a {@link LongCollection}.</p>
         * @param delegate the backing collection whose elements are represented
         *                 by the view. If this collection is serializable, the
         *                 view will also be serializable.
         * @param forwardingType the default forwarding behavior for collection
         *                      operations on the view
         */
        protected Serializable(Collection<?> delegate, ForwardingType forwardingType) {
            forwarder = new Forwarder<>(delegate, forwardingType,
                    this instanceof UnmodifiableView);
        }

        /**
         * <p>Returns the {@link Forwarder} managing access to the backing
         * collection.</p>
         * @return forwarder for the backing collection
         */
        @Override
        protected Forwarder<Collection<?>,LongCollection> forwarder() {
            return forwarder;
        }
    }

    /**
     * <p>An internal implementation of an unmodifiable long collection view.</p>
     */
    public static class Unmodifiable extends LongCollectionView
            implements UnmodifiableView {
        /**
         * <p>Create an unmodifiable long collection view with the specified
         * backing collection and forwarding type.</p>
         * @apiNote <p>For correct default behavior of collection operations,
         * either the forwarding type must be {@link ForwardingType#MINIMAL}, or
         * the backing collection must be a {@link LongCollection}.</p>
         * @param delegate the backing collection whose elements are represented
         *                 by the view
         * @param forwardingType the default forwarding behavior for collection
         *                       operations on the view
         */
        protected Unmodifiable(Collection<?> delegate, ForwardingType forwardingType) {
            super(delegate, forwardingType);
        }
    }

    /**
     * <p>An internal implementation of an unmodifiable (possibly serializable)
     * long collection view. If the backing collection is serializable, this view
     * will also be serializable.</p>
     */
    public static class SerializableUnmodifiable extends Serializable
            implements UnmodifiableView {
        /**
         * <p>Create an unmodifiable (possibly serializable) long collection view
         * with the specified backing collection and forwarding type.</p>
         * @apiNote <p>For correct default behavior of collection operations,
         * either the forwarding type must be {@link ForwardingType#MINIMAL}, or
         * the backing collection must be a {@link LongCollection}.</p>
         * @param delegate the backing collection whose elements are represented
         *                 by the view. If this collection is serializable, the
         *                 view will also be serializable.
         * @param forwardingType the default forwarding behavior for collection
         *                      operations on the view
         */
        protected SerializableUnmodifiable(Collection<?> delegate, ForwardingType forwardingType) {
            super(delegate, forwardingType);
        }
    }
}
