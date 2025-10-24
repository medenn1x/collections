package net.arinoru.collections;

import net.arinoru.function.ObjIntFunction;
import net.arinoru.prerelease.PrereleaseContent;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.BooleanSupplier;
import java.util.function.IntConsumer;
import java.util.function.ObjIntConsumer;
import java.util.function.ToIntFunction;

// most of the content of this class is imported and adapted from OpenJDK 11
// java.util.AbstractCollection

@PrereleaseContent
public class PrimitiveCollections {
    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

    private PrimitiveCollections() {}

    /**
     * Returns an empty {@code PrimitiveCollection.OfDouble}. The returned class is
     * immutable and may safely be shared between threads.
     * @return an empty double collection
     */
    public static PrimitiveCollection.OfDouble emptyDoubleCollection() {
        return EmptyPrimitiveCollection.OfDouble.INSTANCE;
    }

    /**
     * Returns an empty {@code PrimitiveCollection.OfInt}. The returned class is
     * immutable and may safely be shared between threads.
     * @return an empty int collection
     */
    public static PrimitiveCollection.OfInt emptyIntCollection() {
        return EmptyPrimitiveCollection.OfInt.INSTANCE;
    }

    /**
     * Returns an empty {@code PrimitiveCollection.OfLong}. The returned class is
     * immutable and may safely be shared between threads.
     * @return an empty long collection
     */
    public static PrimitiveCollection.OfLong emptyLongCollection() {
        return EmptyPrimitiveCollection.OfLong.INSTANCE;
    }

    /**
     * <p>Returns a collection view over the specified collection. This view will
     * not permit any operations which modify the collection.</p>
     * <p>The returned view is backed by the collection, so any changes to the
     * backing collection will be visible in the returned collection.</p>
     * @param coll collection to create an unmodifiable view over
     * @return an unmodifiable view over the specified collection.
     */
    public static PrimitiveCollection.OfDouble unmodifiableCollection(PrimitiveCollection.OfDouble coll) {
        return ForwardingPrimitiveCollection.OfDouble.unmodifiable(coll);
    }

    /**
     * <p>Returns a collection view over the specified collection. This view will
     * not permit any operations which modify the collection.</p>
     * <p>The returned view is backed by the collection, so any changes to the
     * backing collection will be visible in the returned collection.</p>
     * @param coll collection to create an unmodifiable view over
     * @return an unmodifiable view over the specified collection.
     */
    public static PrimitiveCollection.OfInt unmodifiableCollection(PrimitiveCollection.OfInt coll) {
        return ForwardingPrimitiveCollection.OfInt.unmodifiable(coll);
    }

    /**
     * <p>Returns a collection view over the specified collection. This view will
     * not permit any operations which modify the collection.</p>
     * <p>The returned view is backed by the collection, so any changes to the
     * backing collection will be visible in the returned collection.</p>
     * @param coll collection to create an unmodifiable view over
     * @return an unmodifiable view over the specified collection.
     */
    public static PrimitiveCollection.OfLong unmodifiableCollection(PrimitiveCollection.OfLong coll) {
        return ForwardingPrimitiveCollection.OfLong.unmodifiable(coll);
    }

    private static int hugeCapacity(int minCapacity) {
        if (minCapacity < 0) // overflow
            throw new OutOfMemoryError("Required array size too large");
        return minCapacity > MAX_ARRAY_SIZE ?
                Integer.MAX_VALUE :
                MAX_ARRAY_SIZE;
    }

    private static <T> T finishToArray(
            T r,
            ToIntFunction<T> getLength,
            BooleanSupplier hasNext,
            ObjIntFunction<T,T> reallocate,
            ObjIntConsumer<T> assignNext
    ) {
        int i = getLength.applyAsInt(r);
        while (hasNext.getAsBoolean()) {
            int cap = getLength.applyAsInt(r);
            if (i == cap) {
                int newCap = cap + (cap >> 1) + 1;
                // overflow-conscious code
                if (newCap - MAX_ARRAY_SIZE > 0)
                    newCap = hugeCapacity(cap + 1);
                r = reallocate.apply(r, newCap);
            }
            assignNext.accept(r, i++);
        }
        // trim if overallocated
        return i == getLength.applyAsInt(r) ? r : reallocate.apply(r, i);
    }

    private static <T> T toArrayGeneric(
            T r,
            ToIntFunction<T> getLength,
            BooleanSupplier hasNext,
            ObjIntFunction<T, T> reallocate,
            ObjIntConsumer<T> assignNext
    ) {
        for (int i = 0; i < getLength.applyAsInt(r); i++) {
            if (!hasNext.getAsBoolean()) // fewer elements than expected
                return reallocate.apply(r, i);
            assignNext.accept(r, i);
        }
        return hasNext.getAsBoolean() ?
                finishToArray(r, getLength, hasNext, reallocate, assignNext) : r;
    }

    // This adaptation may seem to be a bit of overkill just as a backend for
    // toArray(T[]). The reason is that the original version of PrimitiveCollection
    // included a toPrimitiveArray(T_ARR) method with similar semantics. The
    // generic adaptation is preserved in case this functionality comes back in
    // some specialized case, but the final version may be rewritten to use less
    // indirection if this does not pan out.
    private static <T> T toArrayGeneric(
            T a,
            T r,
            ToIntFunction<T> getLength,
            BooleanSupplier hasNext,
            ObjIntFunction<T, T> reallocate,
            ObjIntConsumer<T> assignNext,
            ObjIntConsumer<T> nullTerminate,
            IntConsumer copyArray
    ) {
        for (int i = 0; i < getLength.applyAsInt(r); i++) {
            if (!hasNext.getAsBoolean()) { // fewer elements than expected
                if (a == r)
                    nullTerminate.accept(r, i);
                else if (getLength.applyAsInt(a) < i)
                    return reallocate.apply(r, i);
                else {
                    copyArray.accept(i);
                    if (getLength.applyAsInt(a) > i)
                        nullTerminate.accept(a, i);
                }
                return a;
            }
            assignNext.accept(r, i);
        }
        return hasNext.getAsBoolean() ?
                finishToArray(r, getLength, hasNext, reallocate, assignNext) : r;
    }

    static double[] toPrimitiveArray(PrimitiveCollection.OfDouble c) {
        var r = new double[c.size()];
        var iterator = c.iterator();
        return toArrayGeneric(r, arr -> arr.length, iterator::hasNext,
                Arrays::copyOf, (arr, idx) -> arr[idx] = iterator.nextDouble());
    }

    static int[] toPrimitiveArray(PrimitiveCollection.OfInt c) {
        var r = new int[c.size()];
        var iterator = c.iterator();
        return toArrayGeneric(r, arr -> arr.length, iterator::hasNext,
                Arrays::copyOf, (arr, idx) -> arr[idx] = iterator.nextInt());
    }

    static long[] toPrimitiveArray(PrimitiveCollection.OfLong c) {
        var r = new long[c.size()];
        var iterator = c.iterator();
        return toArrayGeneric(r, arr -> arr.length, iterator::hasNext,
                Arrays::copyOf, (arr, idx) -> arr[idx] = iterator.nextLong());
    }

    static Object[] toArray(Collection<?> c) {
        var r = new Object[c.size()];
        var iterator = c.iterator();
        return toArrayGeneric(r, arr -> arr.length, iterator::hasNext,
                Arrays::copyOf, (arr, idx) -> arr[idx] = iterator.next());
    }

    @SuppressWarnings("unchecked")
    static <U> U[] toArray(Collection<?> c, U[] a) {
        // Estimate size of array; be prepared to see more or fewer elements
        int size = c.size();
        U[] r = a.length >= size ? a :
                (U[]) java.lang.reflect.Array.newInstance(
                        a.getClass().getComponentType(), size);
        var iterator = c.iterator();
        return toArrayGeneric(a, r, arr -> arr.length, iterator::hasNext,
                Arrays::copyOf, (arr, idx) -> arr[idx] = (U) iterator.next(),
                (arr, idx) -> arr[idx] = null,
                idx -> System.arraycopy(r, 0, a, 0, idx));
    }
}
