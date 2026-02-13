package net.arinoru.util.views;

import net.arinoru.util.LongSet;

import java.util.Set;

/**
 * <p>An internal implementation of a long set view. In the normal course, views
 * should extend either this class or one of its nested classes and provide their
 * own functionality on top. Some nested classes may also be useful in their own
 * right for providing simple unmodifiable views.</p>
 * <p>While this class is not declared {@code abstract}, it is unlikely to be
 * useful without being extended. It is provided as an alternative to
 * {@link AbstractLongSetView} as most implementing classes will neither need nor
 * want to manage their own {@link Forwarder}.</p>
 */
public class LongSetView extends AbstractLongSetView {
    private final Forwarder<Set<?>,LongSet> forwarder;

    /**
     * <p>Create a long set view with the specified backing set and forwarding
     * type.</p>
     * @apiNote <p>For correct default behavior of set operations, either the
     * forwarding type must be {@link ForwardingType#MINIMAL}, or the backing set
     * must be a {@link LongSet}.</p>
     * @param delegate the backing set whose elements are represented by the view
     * @param forwardingType the default forwarding behavior for set operations on
     *                       the view
     */
    protected LongSetView(Set<?> delegate, ForwardingType forwardingType) {
        forwarder = new Forwarder<>(delegate, forwardingType,
                this instanceof UnmodifiableView);
    }

    /**
     * <p>Returns the {@link Forwarder} managing access to the backing set.</p>
     * @return forwarder for the backing set
     */
    @Override
    protected Forwarder<Set<?>,LongSet> forwarder() {
        return forwarder;
    }

    /**
     * <p>Return an unmodifiable view of the specified set. In general this method
     * will not wrap already unmodifiable views.</p>
     * @param set set to provide an unmodifiable view over
     * @return unmodifiable view
     */
    public static LongSet unmodifiable(LongSet set) {
        return ViewImpl.unmodifiable(set, Unmodifiable::new,
                SerializableUnmodifiable::new);
    }

    /**
     * <p>An internal implementation of a (possibly serializable) long set view.
     * If the backing set is serializable, this view will also be
     * serializable.</p>
     * <p>While this class is not declared {@code abstract}, it is unlikely to be
     * useful without being extended. It is provided as an alternative to
     * {@link AbstractLongSetView} as most implementing classes will neither need
     * nor want to manage their own {@link Forwarder}.</p>
     */
    public static class Serializable extends AbstractLongSetView
            implements java.io.Serializable {
        private final Forwarder<Set<?>,LongSet> forwarder;

        /**
         * <p>Create a (possibly serializable) long set view with the specified
         * backing set and forwarding type.</p>
         * @apiNote <p>For correct default behavior of set operations, either the
         * forwarding type must be {@link ForwardingType#MINIMAL}, or the backing
         * set must be a {@link LongSet}.</p>
         * @param delegate the backing set whose elements are represented by the
         *                view
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
        protected Forwarder<Set<?>,LongSet> forwarder() {
            return forwarder;
        }
    }

    /**
     * <p>An internal implementation of an unmodifiable long set view.</p>
     */
    public static class Unmodifiable extends LongSetView implements UnmodifiableView {
        /**
         * <p>Create an unmodifiable long set view with the specified backing set
         * and forwarding type.</p>
         * @apiNote <p>For correct default behavior of set operations, either the
         * forwarding type must be {@link ForwardingType#MINIMAL}, or the backing
         * set must be a {@link LongSet}.</p>
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
     * long set view. If the backing set is serializable, this view will also be
     * serializable.</p>
     */
    public static class SerializableUnmodifiable extends Serializable
            implements UnmodifiableView {
        /**
         * <p>Create an unmodifiable (possibly serializable) long set view with
         * the specified backing set and forwarding type.</p>
         * @apiNote <p>For correct default behavior of set operations, either the
         * forwarding type must be {@link ForwardingType#MINIMAL}, or the backing
         * set must be a {@link LongSet}.</p>
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
