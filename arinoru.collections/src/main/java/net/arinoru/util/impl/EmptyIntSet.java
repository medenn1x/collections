package net.arinoru.util.impl;

import net.arinoru.util.IntSet;

import java.util.Set;

public class EmptyIntSet extends EmptyIntCollection implements IntSet {
    public static final EmptyIntSet INSTANCE = new EmptyIntSet();

    private EmptyIntSet() {}

    @Override
    public boolean equals(Object o) {
        return o == this || (o instanceof Set<?> set && set.isEmpty());
    }

    @Override
    public int hashCode() {
        return 0;
    }

    public static EmptyIntSet getInstance() {
        return INSTANCE;
    }
}
