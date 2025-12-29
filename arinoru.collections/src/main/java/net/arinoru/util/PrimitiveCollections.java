package net.arinoru.util;

import net.arinoru.annotation.Unstable;
import net.arinoru.function.ObjIntFunction;
import net.arinoru.util.impl.ArrayDoubleSet;
import net.arinoru.util.impl.ArrayIntSet;
import net.arinoru.util.impl.ArrayLongSet;
import net.arinoru.util.impl.DoubleSingleton;
import net.arinoru.util.impl.DoubleSpliterator;
import net.arinoru.util.impl.EmptyDoubleCollection;
import net.arinoru.util.impl.EmptyDoubleIterator;
import net.arinoru.util.impl.EmptyDoubleSet;
import net.arinoru.util.impl.EmptyIntCollection;
import net.arinoru.util.impl.EmptyIntIterator;
import net.arinoru.util.impl.EmptyIntSet;
import net.arinoru.util.impl.EmptyLongCollection;
import net.arinoru.util.impl.EmptyLongIterator;
import net.arinoru.util.impl.EmptyLongSet;
import net.arinoru.util.impl.IntSingleton;
import net.arinoru.util.impl.IntSpliterator;
import net.arinoru.util.impl.LongSingleton;
import net.arinoru.util.impl.LongSpliterator;
import net.arinoru.util.views.DoubleCollectionView;
import net.arinoru.util.views.DoubleSetView;
import net.arinoru.util.views.IntCollectionView;
import net.arinoru.util.views.IntSetView;
import net.arinoru.util.views.LongCollectionView;
import net.arinoru.util.views.LongSetView;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.PrimitiveIterator;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.BooleanSupplier;
import java.util.function.IntConsumer;
import java.util.function.ObjIntConsumer;
import java.util.function.ToIntFunction;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

// Some of the content of this class is imported and adapter from OpenJDK11
// java.util.AbstractCollection and java.util.Spliterators

/**
 * <p>This class consists exclusively of static methods that operate on or return
 * primitive collections. It's meant to serve as an analogue to the {@link Collections}
 * class for operations on primitive collections.</p>
 */
@Unstable
public class PrimitiveCollections {
    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

    private PrimitiveCollections() {}

    /**
     * <p>Creates a {@code Spliterator.OfDouble} using the given collection's
     * {@link DoubleCollection#iterator() iterator} as the source of
     * elements, and reporting its {@link DoubleCollection#size()} as its
     * initial size.</p>
     * <p>The spliterator is <em>late-binding</em>, inherits the
     * <em>fail-fast</em> properties of the collection's iterator, and implements
     * {@code trySplit} to permit limited parallelism.</p>
     * @param collection the collection whose elements the returned spliterator
     *                   returns
     * @param characteristics characteristics of this spliterator's source or
     *                        elements. The characteristics {@code SIZED} and
     *                        {@code SUBSIZED} are additionally reported unless
     *                        {@code CONCURRENT} is supplied.
     * @return a spliterator from an iterator
     * @throws NullPointerException if the given collection is {@code null}
     */
    public static Spliterator.OfDouble doubleSpliterator(DoubleCollection collection,
                                                         int characteristics) {
        return new DoubleSpliterator(collection, characteristics);
    }

    /**
     * <p>Returns an empty {@code DoubleCollection}. The returned class is immutable
     * and may safely be shared between threads.</p>
     * @return an empty double collection
     */
    public static DoubleCollection emptyDoubleCollection() {
        return EmptyDoubleCollection.INSTANCE;
    }

    /**
     * <p>Retyrns an empty {@code PrimitiveIterator.OfDouble}. The returned class is
     * immutable and may safely be shared between threads.</p>
     * @return an empty double iterator
     */
    public static PrimitiveIterator.OfDouble emptyDoubleIterator() {
        return EmptyDoubleIterator.INSTANCE;
    }

    /**
     * <p>Returns an empty {@code DoubleSet}. The returned class is immutable and may
     * safely be shared between threads.</p>
     * @return an empty double set
     */
    public static DoubleSet emptyDoubleSet() {
        return EmptyDoubleSet.INSTANCE;
    }

    /**
     * <p>Returns an empty {@code IntCollection}. The returned class is immutable and
     * may safely be shared between threads.</p>
     * @return an empty int collection
     */
    public static IntCollection emptyIntCollection() {
        return EmptyIntCollection.INSTANCE;
    }

    /**
     * <p>Returns an empty {@code PrimitiveIterator.OfInt}. The returned class is
     * immutable and may safely be shared between threads.</p>
     * @return an empty int iterator
     */
    public static PrimitiveIterator.OfInt emptyIntIterator() {
        return EmptyIntIterator.INSTANCE;
    }

    /**
     * <p>Returns an empty {@code IntSet}. The returned class is immutable and may
     * safely be shared between threads.</p>
     * @return an empty int set
     */
    public static IntSet emptyIntSet() {
        return EmptyIntSet.INSTANCE;
    }

    /**
     * <p>Returns an empty {@code LongCollection}. The returned class is immutable and
     * may safely be shared between threads.</p>
     * @return an empty long collection
     */
    public static LongCollection emptyLongCollection() {
        return EmptyLongCollection.INSTANCE;
    }

    /**
     * <p>Returns an empty {@code PrimitiveIterator.OfLong}. The returned class is
     * immutable and may safely be shared between threads.</p>
     * @return an empty long iterator
     */
    public static PrimitiveIterator.OfLong emptyLongIterator() {
        return EmptyLongIterator.INSTANCE;
    }

    /**
     * <p>Returns an empty {@code LongSet}. The returned class is immutable and may
     * safely be shared between threads.</p>
     * @return an empty long set
     */
    public static LongSet emptyLongSet() {
        return EmptyLongSet.INSTANCE;
    }

    /**
     * <p>Compares the specified object {@code o} with the specified primitive
     * set {@code set} for equality, without recourse to the set's own
     * {@code equals} method. This allows it to serve as a canonical reference
     * implementation for {@code equals}.</p>
     * <p>An object is considered to be equal to a primitive set if:</p>
     * <ol>
     *     <li>It is a {@link Set}.</li>
     *     <li>It has the same size as the primitive set.</li>
     *     <li>It contains all of the elements of this set.</li>
     * </ol>
     * <p>As a corollary, if the specified object is a non-empty primitive set
     * of a different specialization than the specified primitive set, it is not
     * equal to the specified primitive set. Empty sets are always considered
     * to be equal to each other.</p>
     * @implNote <p>If the specified object is also a {@code PrimitiveSet} of the
     * same specialization, this method will take advantage of the
     * {@link PrimitiveSet#containsAll(PrimitiveCollection) PrimitiveSet.containsAll(T_COLL)}
     * method; otherwise, to guard against possible set implementations that
     * do not allow testing whether they contain elements of certain types,
     * this method will iterate over each (boxed) element in the unknown set,
     * testing whether it is an instance of the specified set's boxed type, and
     * if so checking whether it is contained in the specified set, returning
     * {@code true} only if all of the elements pass these checks.</p>
     * @param set the set with which {@code o} is to be compared for equality
     * @param o the object to be compared for equality with {@code set}
     * @return {@code true} if the specified object is equal to the specified set
     * @throws NullPointerException if set is null
     * @see Object#equals(Object)
     * @see PrimitiveSet#equals(Object)
     */
    public static boolean equals(PrimitiveSet<?,?,?,?,?,?,?,?> set, Object o) {
        if (Objects.requireNonNull(set) == o)
            return true;
        if (o instanceof Set<?> other) {
            if (set.size() != other.size())
                return false;
            if (set.isEmpty())
                // Special case: empty sets are equal even if their type would
                // otherwise prevent them from being equal
                return true;
            return switch (other) {
                case DoubleSet otherDoubleSet ->
                    set instanceof DoubleSet doubleSet &&
                            doubleSet.containsAll(otherDoubleSet);
                case IntSet otherIntSet ->
                    set instanceof IntSet intSet &&
                            intSet.containsAll(otherIntSet);
                case LongSet otherLongSet ->
                    set instanceof LongSet longSet &&
                            longSet.containsAll(otherLongSet);
                default -> genericSetEquals(set, other);
            };
        }
        return false;
    }

    /**
     * <p>Returns a hash code value for the specified set, without recourse to
     * the set's own {@link #hashCode()} method. This allows it to serve as a
     * canonical reference implementation for {@code hashCode}.</p>
     * <p>The hash code of a set is defined to be the sum of the hash codes of
     * the elements of the set. This ensures that {@code s1.equals(s2)} implies
     * that {@code hashCode(s1)==hashCode(s2)} for any two sets {@code s1} and
     * {@code s2}, as required by the general contract of
     * {@link Object#hashCode()}.</p>
     * @implNote <p>This method relies upon the static {@code hashCode} method
     * of the boxed representation class for the set's element type (e.g.
     * {@code Long} for {@code PrimitiveSet.OfLong}) to generate canonical
     * hash code values for the elements of the set, with the exception of
     * the int specialization of primitive set, which simply operates on the
     * elements directly. It is equivalent to
     * {@code set.primitiveStream().mapToInt(T::hashCode).sum()}, where
     * {@code T} is the boxed element type of the set.</p>
     * @param set the set to calculate a hash code value for
     * @return the hash code value for the specified set
     * @throws NullPointerException if set is null
     * @throws ClassCastException if set is not a recognized specialization of
     * PrimitiveSet.
     */
    public static int hashCode(PrimitiveSet<?,?,?,?,?,?,?,?> set) {
        return switch (set.parallelPrimitiveStream()) {
            case DoubleStream stream -> stream.mapToInt(Double::hashCode).sum();
            case IntStream stream -> stream.sum();
            case LongStream stream -> stream.mapToInt(Long::hashCode).sum();
            default -> throw new ClassCastException("Internal error");
        };
    }

    /**
     * <p>Creates a {@code Spliterator.OfInt} using the given collection's
     * {@link IntCollection#iterator() iterator} as the source of
     * elements, and reporting its {@link IntCollection#size()} as its
     * initial size.</p>
     * <p>The spliterator is <em>late-binding</em>, inherits the
     * <em>fail-fast</em> properties of the collection's iterator, and implements
     * {@code trySplit} to permit limited parallelism.</p>
     * @param collection the collection whose elements the returned spliterator
     *                   returns
     * @param characteristics characteristics of this spliterator's source or
     *                        elements. The characteristics {@code SIZED} and
     *                        {@code SUBSIZED} are additionally reported unless
     *                        {@code CONCURRENT} is supplied.
     * @return a spliterator from an iterator
     * @throws NullPointerException if the given collection is {@code null}
     */
    public static Spliterator.OfInt intSpliterator(IntCollection collection,
                                                   int characteristics) {
        return new IntSpliterator(collection, characteristics);
    }

    /**
     * <p>Creates a {@code Spliterator.OfLong} using the given collection's
     * {@link LongCollection#iterator() iterator} as the source of
     * elements, and reporting its {@link LongCollection#size()} as its
     * initial size.</p>
     * <p>The spliterator is <em>late-binding</em>, inherits the
     * <em>fail-fast</em> properties of the collection's iterator, and implements
     * {@code trySplit} to permit limited parallelism.</p>
     * @param collection the collection whose elements the returned spliterator
     *                   returns
     * @param characteristics characteristics of this spliterator's source or
     *                        elements. The characteristics {@code SIZED} and
     *                        {@code SUBSIZED} are additionally reported unless
     *                        {@code CONCURRENT} is supplied.
     * @return a spliterator from an iterator
     * @throws NullPointerException if the given collection is {@code null}
     */
    public static Spliterator.OfLong longSpliterator(LongCollection collection,
                                                     int characteristics) {
        return new LongSpliterator(collection, characteristics);
    }

    /**
     * <p>Returns an unmodifiable set containing one element.</p>
     * @param element the single element
     * @return a {@code DoubleSet} containing the specified element
     */
    public static DoubleSet singleton(double element) {
        return new DoubleSingleton(element);
    }

    /**
     * <p>Returns an unmodifiable set containing one element.</p>
     * @param element the single element
     * @return a {@code IntSet} containing the specified element
     */
    public static IntSet singleton(int element) {
        return new IntSingleton(element);
    }

    /**
     * <p>Returns an unmodifiable set containing one element.</p>
     * @param element the single element
     * @return a {@code LongSet} containing the specified element
     */
    public static LongSet singleton(long element) {
        return new LongSingleton(element);
    }

    /**
     * <p>Returns an unmodifiable view over the specified collection. Query
     * operations on the returned collection "read through" to the specified
     * collections, and attempts to modify the returned collection, whether
     * directly or via its iterator, result in an
     * {@code UnsupportedOperationException}.</p>
     * <p>The returned collection does <em>not</em> pass the hashCode and
     * equals operations through to the backing collection, but relies on
     * {@code Object}'s {@code equals} and {@code hashCode} methods. This
     * is necessary to preserve the contracts of these operations in the
     * case that the backing collection is a set or list.</p>
     * @param collection collection to create an unmodifiable view over
     * @return an unmodifiable view over the specified collection.
     */
    public static DoubleCollection unmodifiableCollection(DoubleCollection collection) {
        return DoubleCollectionView.unmodifiable(collection);
    }

    /**
     * <p>Returns a collection view over the specified collection. This view will
     * not permit any operations which modify the collection.</p>
     * <p>The returned view is backed by the collection, so any changes to the
     * backing collection will be visible in the returned collection.</p>
     * @param collection collection to create an unmodifiable view over
     * @return an unmodifiable view over the specified collection.
     */
    public static IntCollection unmodifiableCollection(IntCollection collection) {
        return IntCollectionView.unmodifiable(collection);
    }

    /**
     * <p>Returns a collection view over the specified collection. This view will
     * not permit any operations which modify the collection.</p>
     * <p>The returned view is backed by the collection, so any changes to the
     * backing collection will be visible in the returned collection.</p>
     * @param collection collection to create an unmodifiable view over
     * @return an unmodifiable view over the specified collection.
     */
    public static LongCollection unmodifiableCollection(LongCollection collection) {
        return LongCollectionView.unmodifiable(collection);
    }

    /**
     * <p>Returns a set view over the specified set. This view will not permit
     * any operations which modify the set.</p>
     * <p>The returned view is backed by the set, so any changes to the backing
     * set will be visible in the returned set.</p>
     * @param set set to create an unmodifiable view over
     * @return an unmodifiable view over the specified set.
     */
    public static DoubleSet unmodifiableSet(DoubleSet set) {
        return DoubleSetView.unmodifiable(set);
    }

    /**
     * <p>Returns a set view over the specified set. This view will not permit
     * any operations which modify the set.</p>
     * <p>The returned view is backed by the set, so any changes to the backing
     * set will be visible in the returned set.</p>
     * @param set set to create an unmodifiable view over
     * @return an unmodifiable view over the specified set.
     */
    public static IntSet unmodifiableSet(IntSet set) {
        return IntSetView.unmodifiable(set);
    }

    /**
     * <p>Returns a set view over the specified set. This view will not permit
     * any operations which modify the set.</p>
     * <p>The returned view is backed by the set, so any changes to the backing
     * set will be visible in the returned set.</p>
     * @param set set to create an unmodifiable view over
     * @return an unmodifiable view over the specified set.
     */
    public static LongSet unmodifiableSet(LongSet set) {
        return LongSetView.unmodifiable(set);
    }

    static DoubleSet setOf(DoubleCollection collection) {
        return switch (collection) {
            case ArrayDoubleSet set -> set;
            case DoubleSingleton singleton -> singleton;
            case EmptyDoubleSet set -> set;
            case EmptyDoubleCollection ignored -> EmptyDoubleSet.INSTANCE;
            default -> ArrayDoubleSet.fromCollection(collection);
        };
    }

    static DoubleSet setOf(double[] a) {
        return ArrayDoubleSet.fromArray(a);
    }

    static IntSet setOf(IntCollection collection) {
        return switch (collection) {
            case ArrayIntSet set -> set;
            case IntSingleton singleton -> singleton;
            case EmptyIntSet set -> set;
            case EmptyIntCollection ignored -> EmptyIntSet.INSTANCE;
            default -> ArrayIntSet.fromCollection(collection);
        };
    }

    static IntSet setOf(int[] a) {
        return ArrayIntSet.fromArray(a);
    }

    static LongSet setOf(LongCollection collection) {
        return switch (collection) {
            case ArrayLongSet set -> set;
            case LongSingleton singleton -> singleton;
            case EmptyLongSet set -> set;
            case EmptyLongCollection ignored -> EmptyLongSet.INSTANCE;
            default -> ArrayLongSet.fromCollection(collection);
        };
    }

    static LongSet setOf(long[] a) {
        return ArrayLongSet.fromArray(a);
    }

    private static boolean genericSetEquals(PrimitiveSet<?,?,?,?,?,?,?,?> set, Set<?> other) {
        // A set is not guaranteed to allow checks of objects of improper type,
        // so we need to validate type here to avoid the risk of exceptions.
        var elementBoxedType = switch (set) {
            case DoubleSet ignored -> Double.class;
            case IntSet ignored -> Integer.class;
            case LongSet ignored -> Long.class;
            default -> throw new ClassCastException("Internal error");
        };
        return set.size() == (int) other.parallelStream()
                .filter(elementBoxedType::isInstance)
                .filter(set::contains)
                .count();
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
            ObjIntFunction<T,T> reallocate,
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
    // generic adaptation is preserved in case this functionality comes back in some
    // specialized case, but the final version may be rewritten to use less
    // indirection if this does not pan out.
    private static <T> T toArrayGeneric(
            T a,
            T r,
            ToIntFunction<T> getLength,
            BooleanSupplier hasNext,
            ObjIntFunction<T,T> reallocate,
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

    static double[] toPrimitiveArray(DoubleCollection collection) {
        var r = new double[collection.size()];
        var iterator = collection.iterator();
        return toArrayGeneric(r, arr -> arr.length, iterator::hasNext,
                Arrays::copyOf, (arr, idx) -> arr[idx] = iterator.nextDouble());
    }

    static int[] toPrimitiveArray(IntCollection collection) {
        var r = new int[collection.size()];
        var iterator = collection.iterator();
        return toArrayGeneric(r, arr -> arr.length, iterator::hasNext,
                Arrays::copyOf, (arr, idx) -> arr[idx] = iterator.nextInt());
    }

    static long[] toPrimitiveArray(LongCollection collection) {
        var r = new long[collection.size()];
        var iterator = collection.iterator();
        return toArrayGeneric(r, arr -> arr.length, iterator::hasNext,
                Arrays::copyOf, (arr, idx) -> arr[idx] = iterator.nextLong());
    }

    static Object[] toArray(Collection<?> collection) {
        var r = new Object[collection.size()];
        var iterator = collection.iterator();
        return toArrayGeneric(r, arr -> arr.length, iterator::hasNext,
                Arrays::copyOf, (arr, idx) -> arr[idx] = iterator.next());
    }

    @SuppressWarnings("unchecked")
    static <U> U[] toArray(Collection<?> collection, U[] a) {
        // Estimate size of array; be prepared to see more or fewer elements
        int size = collection.size();
        U[] r = a.length >= size ? a :
                (U[]) java.lang.reflect.Array.newInstance(
                        a.getClass().getComponentType(), size);
        var iterator = collection.iterator();
        return toArrayGeneric(a, r, arr -> arr.length, iterator::hasNext,
                Arrays::copyOf, (arr, idx) -> arr[idx] = (U) iterator.next(),
                (arr, idx) -> arr[idx] = null,
                idx -> System.arraycopy(r, 0, a, 0, idx));
    }
}
