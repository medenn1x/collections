package net.arinoru.util;

import net.arinoru.annotation.Unstable;

/**
 * <p>Exception thrown due to an attempt to modify an unmodifiable view. This is a
 * specialization of {@link UnsupportedOperationException} intended to support more
 * specificity in examining and reporting collection behavior.</p>
 */
@Unstable
public class CollectionNotModifiableException extends UnsupportedOperationException {
    /**
     * Create a new CollectionNotModifiableException with no message.
     */
    public CollectionNotModifiableException() {}

    /**
     * Create a new CollectionNotModifiableException with the specified message
     * @param message exception message
     */
    public CollectionNotModifiableException(String message) {
        super(message);
    }
}
