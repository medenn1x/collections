package net.arinoru.util.impl;

import net.arinoru.util.DoubleSet;

import java.util.Set;

public class EmptyDoubleSet extends EmptyDoubleCollection implements DoubleSet {
    public static final EmptyDoubleSet INSTANCE = new EmptyDoubleSet();

    private EmptyDoubleSet() {}

    @Override
    public boolean equals(Object o) {
        return o == this || (o instanceof Set<?> set && set.isEmpty());
    }

    @Override
    public int hashCode() {
        return 0;
    }

    public static EmptyDoubleSet getInstance() {
        return INSTANCE;
    }
}
