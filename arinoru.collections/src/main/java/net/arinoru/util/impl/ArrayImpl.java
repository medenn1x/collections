package net.arinoru.util.impl;

import java.lang.reflect.Array;

class ArrayImpl {
    private ArrayImpl() {}

    @SuppressWarnings("unchecked")
    static <T> T[] newArray(T[] a, int length) {
        return (T[]) Array.newInstance(a.getClass().getComponentType(), length);
    }
}
