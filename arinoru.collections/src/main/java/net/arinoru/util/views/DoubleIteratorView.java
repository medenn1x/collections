package net.arinoru.util.views;

import java.util.Iterator;
import java.util.PrimitiveIterator;

/**
 * <p>An internal implementation of a double iterator view. In the normal course,
 * views should extend either this class or one of its nested classes and provide
 * their own functionality on top. Some nested classes may also be useful in their
 * own right for providing simple unmodifiable views.</p>
 * <p>While this class is not declared {@code abstract}, it is unlikely to be
 * useful without being extended. It is provided as an alternative to
 * {@link AbstractDoubleIteratorView} as most implementing classes will neither need
 * nor want to manage their own {@link Forwarder}.</p>
 */
public class DoubleIteratorView extends AbstractDoubleIteratorView {
    private final Forwarder<Iterator<?>, PrimitiveIterator.OfDouble> forwarder;

    /**
     * <p>Create a double iterator view with the specified backing iterator and
     * forwarding type.</p>
     * @apiNote <p>For correct default behavior of iterator operations,
     * either the forwarding type must be {@link ForwardingType#MINIMAL}, or
     * the backing iterator must be a
     * {@link java.util.PrimitiveIterator.OfDouble}.</p>
     * @param delegate the backing iterator whose elements are represented by the
     *                 view
     * @param forwardingType the default forwarding behavior for iterator
     *                       operations on the view
     */
    protected DoubleIteratorView(Iterator<?> delegate, ForwardingType forwardingType) {
        forwarder = new Forwarder<>(delegate, forwardingType,
                this instanceof UnmodifiableView);
    }

    /**
     * <p>Returns the {@link Forwarder} managing access to the backing iterator.</p>
     * @return forwarder for the backing iterator
     */
    @Override
    protected Forwarder<Iterator<?>,PrimitiveIterator.OfDouble> forwarder() {
        return forwarder;
    }

    /**
     * <p>Return an unmodifiable view of the specified iterator. In general this
     * method will not wrap already unmodifiable views.</p>
     * @param iterator iterator to provide an unmodifiable view over
     * @return unmodifiable view
     */
    public static PrimitiveIterator.OfDouble unmodifiable(
            PrimitiveIterator.OfDouble iterator) {
        return ViewImpl.unmodifiable(iterator, Unmodifiable::new,
                SerializableUnmodifiable::new);
    }

    /**
     * <p>An internal implementation of a (possibly serializable) double iterator
     * view. If the backing iterator is serializable, this view will also be
     * serializable.</p>
     * <p>While this class is not declared {@code abstract}, it is unlikely to be
     * useful without being extended. It is provided as an alternative to
     * {@link AbstractLongIteratorView} as most implementing classes will neither
     * need no want to manage their own {@link Forwarder}.</p>
     */
    public static class Serializable extends AbstractDoubleIteratorView
            implements java.io.Serializable {
        private final Forwarder<Iterator<?>,PrimitiveIterator.OfDouble> forwarder;

        /**
         * <p>Create a (possibly serializable) double iterator view with the specified
         * backing iterator and forwarding type.</p>
         * @apiNote <p>For correct default behavior of iterator operations,
         * either the forwarding type must be {@link ForwardingType#MINIMAL}, or
         * the backing iterator must be a
         * {@link java.util.PrimitiveIterator.OfDouble}.</p>
         * @param delegate the backing iterator whose elements are represented by
         *                 the view. If this iterator is serializable, the view
         *                 will also be serializable
         * @param forwardingType the default forwarding behavior for iterator
         *                       operations on the view
         */
        protected Serializable(Iterator<?> delegate, ForwardingType forwardingType) {
            forwarder = new Forwarder<>(delegate, forwardingType,
                    this instanceof UnmodifiableView);
        }

        /**
         * <p>Returns the {@link Forwarder} managing access to the backing
         * iterator.</p>
         * @return forwarder for the backing iterator
         */
        @Override
        protected Forwarder<Iterator<?>,PrimitiveIterator.OfDouble> forwarder() {
            return forwarder;
        }
    }

    /**
     * <p>An internal implementation of an unmodifiable double iterator view.</p>
     */
    public static class Unmodifiable extends DoubleIteratorView
            implements UnmodifiableView {
        /**
         * <p>Create an unmodifiable double iterator view with the specified backing
         * iterator and forwarding type.</p>
         * @apiNote <p>For correct default behavior of iterator operations,
         * either the forwarding type must be {@link ForwardingType#MINIMAL}, or
         * the backing iterator must be a
         * {@link java.util.PrimitiveIterator.OfDouble}.</p>
         * @param delegate the backing iterator whose elements are represented by
         *                 the view.
         * @param forwardingType the default forwarding behavior for iterator
         *                       operations on the view
         */
        protected Unmodifiable(Iterator<?> delegate, ForwardingType forwardingType) {
            super(delegate, forwardingType);
        }
    }

    /**
     * <p>An internal implementation of an unmodifiable (possibly serializable)
     * double iterator view. If the backing iterator is serializable, this view will
     * also be serializable.</p>
     */
    public static class SerializableUnmodifiable extends Serializable
            implements UnmodifiableView {
        /**
         * <p>Create an unmodifiable (possibly serializable) double iterator view
         * with the specified backing iterator and forwarding type.</p>
         * @apiNote <p>For correct default behavior of iterator operations,
         * either the forwarding type must be {@link ForwardingType#MINIMAL}, or
         * the backing iterator must be a
         * {@link java.util.PrimitiveIterator.OfDouble}.</p>
         * @param delegate the backing iterator whose elements are represented by
         *                 the view. If this iterator is serializable, the view
         *                 will also be serializable.
         * @param forwardingType the default forwarding behavior for iterator
         *                       operations on the view
         */
        protected SerializableUnmodifiable(Iterator<?> delegate, ForwardingType forwardingType) {
            super(delegate, forwardingType);
        }
    }
}
