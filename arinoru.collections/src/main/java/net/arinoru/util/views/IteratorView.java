package net.arinoru.util.views;

import java.util.Iterator;

/**
 * <p>An internal implementation of an iterator view. In the normal course, views
 * should extend either this class or one of its nested classes and provide their
 * own functionality on top. Some nested classes may also be useful in their own
 * right for providing simple unmodifiable views.</p>
 * <p>While this class is not declared {@code abstract}, it is unlikely to be
 * useful without being extended. It is provided as an alternative to
 * {@link AbstractIteratorView} as most implementing classes will neither need
 * nor want to manage their own {@link Forwarder}.</p>
 * @param <E> The element type of the iterator
 */
public class IteratorView<E> extends AbstractIteratorView<E> {
    private final Forwarder<Iterator<?>,Iterator<E>> forwarder;

    /**
     * <p>Create an iterator view with the specified backing iterator and
     * forwarding type.</p>
     * @param delegate the backing iterator whose elements are represented by the
     *                 view
     * @param forwardingType the default forwarding behavior for iterator
     *                      operations on the view
     */
    protected IteratorView(Iterator<?> delegate, ForwardingType forwardingType) {
        forwarder = new Forwarder<>(delegate, forwardingType,
                this instanceof UnmodifiableView);
    }

    /**
     * <p>Returns the {@link Forwarder} managing access to the backing iterator.</p>
     * @return forwarder for the backing iterator
     */
    @Override
    protected Forwarder<Iterator<?>,Iterator<E>> forwarder() {
        return forwarder;
    }

    /**
     * <p>Return an unmodifiable view of the specified iterator. In general this
     * method will not wrap already unmodifiable views.</p>
     * @param iterator iterator to provide an unmodifiable view over
     * @return unmodifiable view
     * @param <E> element type of the iterator
     */
    static <E> Iterator<E> unmodifiable(Iterator<E> iterator) {
        return ViewImpl.unmodifiable(iterator, Unmodifiable::new,
                SerializableUnmodifiable::new);
    }

    /**
     * <p>An internal implementation of a (possibly serializable) iterator view.
     * If the backing iterator is serializable, this view will also be
     * serializable.</p>
     * <p>While this class is not declared {@code abstract}, it is unlikely to be
     * useful without being extended. It is provided as an alternative to
     * {@link AbstractIteratorView} as most implementing classes will neither need
     * nor want to manage their own {@link Forwarder}.</p>
     */
    public static class Serializable<E> extends AbstractIteratorView<E>
            implements java.io.Serializable {
        private final Forwarder<Iterator<?>,Iterator<E>> forwarder;

        /**
         * <p>Create a (possibly serializable) iterator view with the specified
         * backing iterator and forwarding type.</p>
         * @param delegate the backing iterator whose elements are represented by
         *                 the view. If this iterator is serializable, the view
         *                 will also be serializable.
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
        protected Forwarder<Iterator<?>,Iterator<E>> forwarder() {
            return forwarder;
        }
    }

    /**
     * <p>An internal implementation of an unmodifiable iterator view.</p>
     * @param <E> the element type of the iterator
     */
    public static class Unmodifiable<E> extends IteratorView<E>
            implements UnmodifiableView {
        /**
         * <p>Create an unmodifiable iterator view with the specified backing
         * iterator and forwarding type.</p>
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
     * iterator view. If the backing iterator is serializable, this view will also
     * be serializable.</p>
     * @param <E> the element type of the iterator
     */
    public static class SerializableUnmodifiable<E> extends Serializable<E>
            implements UnmodifiableView {
        /**
         * <p>Create an unmodifiable (possibly serializable) iterator view with
         * the specified backing iterator and forwarding type.</p>
         * @param delegate the backing iterator whose elements are represented by
         *                 the view. If this iterator is serializable, the view
         *                 will also be serializable.
         * @param forwardingType the default forwarding behavior for iterator
         *                      operations on the view
         */
        protected SerializableUnmodifiable(Iterator<?> delegate, ForwardingType forwardingType) {
            super(delegate, forwardingType);
        }
    }
}
