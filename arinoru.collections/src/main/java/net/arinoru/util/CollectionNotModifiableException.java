package net.arinoru.util;

import net.arinoru.annotation.Unstable;

@Unstable
public class CollectionNotModifiableException extends UnsupportedOperationException {
    public CollectionNotModifiableException() {}
    public CollectionNotModifiableException(String message) {
        super(message);
    }
}
