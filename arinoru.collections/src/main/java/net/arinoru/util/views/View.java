package net.arinoru.util.views;

import net.arinoru.util.CollectionNotModifiableException;

/**
 * <p>A base interface type for views.</p>
 */
public interface View {
    /**
     * <p>This method should be called by operations that modify the underlying
     * collection to ensure that such an operation is legal for this view. Throws
     * an {@link UnsupportedOperationException} if this view is unmodifiable.</p>
     * @apiNote <p>Implementing classes should either rely on the default behavior
     * or ensure that the implementation throws an instance of
     * {@code UnsupportedOperationException} or a subclass thereof (ideally, a
     * {@link CollectionNotModifiableException} or subclass thereof) if the class
     * implements {@link UnmodifiableView}. Any class presenting an unmodifiable
     * view should implement {@link UnmodifiableView} to ensure consistent
     * operation.</p>
     * @implNote <p>The default implementation throws a
     * {@link CollectionNotModifiableException} if the implementing class is also
     * an instance of {@link UnmodifiableView}.</p>
     * @throws UnsupportedOperationException if this view is unmodifiable.
     */
    default void checkModifiable() {
        if (this instanceof UnmodifiableView)
            throw new CollectionNotModifiableException();
    }
}
