package net.arinoru.util.views;

import net.arinoru.util.CollectionNotModifiableException;

public interface View {
    default void checkModifiable() {
        if (this instanceof UnmodifiableView)
            throw new CollectionNotModifiableException();
    }
}
