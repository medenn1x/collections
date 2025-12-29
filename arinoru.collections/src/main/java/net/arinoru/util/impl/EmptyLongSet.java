package net.arinoru.util.impl;

import net.arinoru.util.LongSet;

import java.util.Set;

public class EmptyLongSet extends EmptyLongCollection implements LongSet {
    public static EmptyLongSet INSTANCE = new EmptyLongSet();

    private EmptyLongSet() {}

    @Override
    public boolean equals(Object o) {
        return o == this || (o instanceof Set<?> set && set.isEmpty());
    }

    @Override
    public int hashCode() {
        return 0;
    }

    public static EmptyLongSet getInstance() {
        return INSTANCE;
    }
}
