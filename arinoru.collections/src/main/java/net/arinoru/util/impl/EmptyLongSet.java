package net.arinoru.util.impl;

import net.arinoru.util.LongSet;

import java.util.Set;

/**
 * <p>An internal representation of an unmodifiable {@link LongSet} containing no
 * elements.</p>
 */
public class EmptyLongSet extends EmptyLongCollection implements LongSet {
    /**
     * <p>A canonical instance of an {@code EmptyDoubleSet}. As this is completely
     * immutable and has no state, this instance can be reused and shared between
     * threads.</p>
     */
    public static EmptyLongSet INSTANCE = new EmptyLongSet();

    private EmptyLongSet() {}

    /**
     * <p>Compares the specified object with this set for equality. Returns
     * {@code true} if the specified object is also a set, the two sets have the
     * same size, and every member of the specified set is contained in this set
     * (or equivalently, every member of this set is contained in the specified
     * set). This definition ensures that the equals method works properly
     * across different implementations of the set interface.</p>
     * @implNote <p>Returns {@code true} if the specified object is an empty
     * set.</p>
     * @param o object to be compared for equality with this set
     * @return {@code true} if the specified object is equal to this set
     */
    @Override
    public boolean equals(Object o) {
        return o == this || (o instanceof Set<?> set && set.isEmpty());
    }

    /**
     * <p>Returns the hash code value for this set. The hash code of a set is
     * defined to be the sum of the hash codes of the elements in the set. This
     * ensures that {@code s1.equals(s2)} implies that
     * {@code s1.hashCode()==s2.hashCode()} for any two sets {@code s1} and
     * {@code s2}, as required by the general contract of
     * {@link Object#equals(Object)}.</p>
     * @implNote <p>This method always returns {@code 0}.</p>
     * @return the hash code value for this set
     */
    @Override
    public int hashCode() {
        return 0;
    }

    /**
     * <p>Returns an instance of {@code EmptyLongSet}.</p>
     * @return an empty long set
     */
    public static EmptyLongSet getInstance() {
        return INSTANCE;
    }
}
