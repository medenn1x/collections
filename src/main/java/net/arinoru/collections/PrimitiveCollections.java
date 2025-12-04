package net.arinoru.collections;

import net.arinoru.function.ObjIntFunction;
import net.arinoru.prerelease.PrereleaseContent;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

// Some of the content of this class is imported and adapted from OpenJDK 11
// java.util.AbstractCollection and java.util.Spliterators

/**
 * <p>This class consists exclusively of static methods that operate on or
 * return primitive collections. It's meant to serve as an analogue to the
 * {@link Collections} class for operations on primitive collections.</p>
 */
@PrereleaseContent
public class PrimitiveCollections {
    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

    private PrimitiveCollections() {}

    static PrimitiveSet.OfDouble setOf(PrimitiveCollection.OfDouble collection) {
        return switch (collection) {
            case ArrayDoubleSet set -> set;
            case DoubleSingleton singleton -> singleton;
            case EmptyDoubleSet set -> set;
            case EmptyDoubleCollection ignored -> EmptyDoubleSet.INSTANCE;
            default -> ArrayDoubleSet.fromCollection(collection);
        };
    }

    static PrimitiveSet.OfDouble setOf(double[] a) {
        return ArrayDoubleSet.fromArray(a);
    }

    static PrimitiveSet.OfInt setOf(PrimitiveCollection.OfInt collection) {
        return switch (collection) {
            case ArrayIntSet set -> set;
            case EmptyIntSet set -> set;
            case EmptyIntCollection ignored -> EmptyIntSet.INSTANCE;
            case IntSingleton singleton -> singleton;
            default -> ArrayIntSet.fromCollection(collection);
        };
    }

    static PrimitiveSet.OfInt setOf(int[] a) {
        return ArrayIntSet.fromArray(a);
    }

    static PrimitiveSet.OfLong setOf(PrimitiveCollection.OfLong collection) {
        return switch (collection) {
            case ArrayLongSet set -> set;
            case EmptyLongSet set -> set;
            case EmptyLongCollection ignored -> EmptyLongSet.INSTANCE;
            case LongSingleton singleton -> singleton;
            default -> ArrayLongSet.fromCollection(collection);
        };
    }

    static PrimitiveSet.OfLong setOf(long[] a) {
        return ArrayLongSet.fromArray(a);
    }

    /**
     * <p>Creates a {@code Spliterator.OfDouble} using the given collection's
     * {@link PrimitiveCollection.OfDouble#iterator() iterator} as the source of
     * elements, and reporting its {@link PrimitiveCollection#size()} as its
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
    public static Spliterator.OfDouble doubleSpliterator(
            PrimitiveCollection.OfDouble collection, int characteristics) {
        return new DoubleSpliterator(collection, characteristics);
    }

    /**
     * Returns an empty {@code PrimitiveCollection.OfDouble}. The returned class is
     * immutable and may safely be shared between threads.
     * @return an empty double collection
     */
    public static PrimitiveCollection.OfDouble emptyDoubleCollection() {
        return EmptyDoubleCollection.INSTANCE;
    }

    /**
     * <p>Returns an empty {@code PrimitiveIterator.OfDouble}. The returned class
     * is immutable and may safely be shared between threads.</p>
     * @return an empty double iterator
     */
    public static PrimitiveIterator.OfDouble emptyDoubleIterator() {
        return EmptyDoubleIterator.INSTANCE;
    }

    /**
     * <p>Returns an empty {@code PrimitiveSet.OfDouble}. The returned class is
     * immutable and may safely be shared between threads.</p>
     * @return an empty double set
     */
    public static PrimitiveSet.OfDouble emptyDoubleSet() {
        return EmptyDoubleSet.INSTANCE;
    }

    /**
     * Returns an empty {@code PrimitiveCollection.OfInt}. The returned class is
     * immutable and may safely be shared between threads.
     * @return an empty int collection
     */
    public static PrimitiveCollection.OfInt emptyIntCollection() {
        return EmptyIntCollection.INSTANCE;
    }

    /**
     * <p>Returns an empty {@code PrimitiveIterator.OfInt}. The returned class
     * is immutable and may safely be shared between threads.</p>
     * @return an empty int iterator
     */
    public static PrimitiveIterator.OfInt emptyIntIterator() {
        return EmptyIntIterator.INSTANCE;
    }

    /**
     * <p>Returns an empty {@code PrimitiveSet.OfInt}. The returned class is
     * immutable and may safely be shared between threads.</p>
     * @return an empty int set
     */
    public static PrimitiveSet.OfInt emptyIntSet() {
        return EmptyIntSet.INSTANCE;
    }

    /**
     * Returns an empty {@code PrimitiveCollection.OfLong}. The returned class is
     * immutable and may safely be shared between threads.
     * @return an empty long collection
     */
    public static PrimitiveCollection.OfLong emptyLongCollection() {
        return EmptyLongCollection.INSTANCE;
    }

    /**
     * <p>Returns an empty {@code PrimitiveIterator.OfLong}. The returned class
     * is immutable and may safely be shared between threads.</p>
     * @return an empty long iterator
     */
    public static PrimitiveIterator.OfLong emptyLongIterator() {
        return EmptyLongIterator.INSTANCE;
    }

    /**
     * <p>Returns an empty {@code PrimitiveSet.OfLong}. The returned class is
     * immutable and may safely be shared between threads.</p>
     * @return an empty long set
     */
    public static PrimitiveSet.OfLong emptyLongSet() {
        return EmptyLongSet.INSTANCE;
    }

    /**
     * <p>Creates a {@code Spliterator.OfInt} using the given collection's
     * {@link PrimitiveCollection.OfInt#iterator() iterator} as the source of
     * elements, and reporting its {@link PrimitiveCollection#size()} as its
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
    public static Spliterator.OfInt intSpliterator(
            PrimitiveCollection.OfInt collection, int characteristics) {
        return new IntSpliterator(collection, characteristics);
    }

    /**
     * <p>Creates a {@code Spliterator.OfLong} using the given collection's
     * {@link PrimitiveCollection.OfLong#iterator() iterator} as the source of
     * elements, and reporting its {@link PrimitiveCollection#size()} as its
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
    public static Spliterator.OfLong longSpliterator(
            PrimitiveCollection.OfLong collection, int characteristics) {
        return new LongSpliterator(collection, characteristics);
    }

    /**
     * <p>Returns an unmodifiable set containing one element.</p>
     * @param d the single element
     * @return a {@code PrimitiveSet.OfDouble} containing the specified element
     */
    public static PrimitiveSet.OfDouble singleton(double d) {
        return new DoubleSingleton(d);
    }

    /**
     * <p>Returns an unmodifiable set containing one element.</p>
     * @param i the single element
     * @return a {@code PrimitiveSet.OfInt} containing the specified element
     */
    public static PrimitiveSet.OfInt singleton(int i) {
        return new IntSingleton(i);
    }

    /**
     * <p>Returns an unmodifiable set containing one element.</p>
     * @param l the single element
     * @return a {@code PrimitiveSet.OfLong} containing the specified element
     */
    public static PrimitiveSet.OfLong singleton(long l) {
        return new LongSingleton(l);
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
     * @param coll collection to create an unmodifiable view over
     * @return an unmodifiable view over the specified collection.
     */
    public static PrimitiveCollection.OfDouble unmodifiableCollection(PrimitiveCollection.OfDouble coll) {
        return Views.unmodifiableDoubleCollectionView(coll);
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
        return Views.unmodifiableIntCollectionView(coll);
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
        return Views.unmodifiableLongCollectionView(coll);
    }

    /**
     * <p>Returns a set view over the specified set. This view will not permit
     * any operations which modify the set.</p>
     * <p>The returned view is backed by the set, so any changes to the backing
     * set will be visible in the returned set.</p>
     * @param set set to create an unmodifiable view over
     * @return an unmodifiable view over the specified set.
     */
    public static PrimitiveSet.OfDouble unmodifiableSet(PrimitiveSet.OfDouble set) {
        return Views.unmodifiableDoubleSetView(set);
    }

    /**
     * <p>Returns a set view over the specified set. This view will not permit
     * any operations which modify the set.</p>
     * <p>The returned view is backed by the set, so any changes to the backing
     * set will be visible in the returned set.</p>
     * @param set set to create an unmodifiable view over
     * @return an unmodifiable view over the specified set.
     */
    public static PrimitiveSet.OfInt unmodifiableSet(PrimitiveSet.OfInt set) {
        return Views.unmodifiableIntSetView(set);
    }

    /**
     * <p>Returns a set view over the specified set. This view will not permit
     * any operations which modify the set.</p>
     * <p>The returned view is backed by the set, so any changes to the backing
     * set will be visible in the returned set.</p>
     * @param set set to create an unmodifiable view over
     * @return an unmodifiable view over the specified set.
     */
    public static PrimitiveSet.OfLong unmodifiableSet(PrimitiveSet.OfLong set) {
        return Views.unmodifiableLongSetView(set);
    }

    private static boolean genericSetEquals(PrimitiveSet<?,?,?,?,?,?,?> set, Set<?> other) {
        // A set is not guaranted to allow checks of objects of improper type,
        // so we need to validate type here to avoid the risk of exceptions.
        var elementBoxedType = switch (set) {
            case PrimitiveSet.OfDouble ignored -> Double.class;
            case PrimitiveSet.OfInt ignored -> Integer.class;
            case PrimitiveSet.OfLong ignored -> Long.class;
            default -> throw new ClassCastException("Internal error");
        };
        return set.size() == (int) other.stream()
                .filter(elementBoxedType::isInstance)
                .filter(set::contains)
                .count();
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
     * @see Object#equals(Object)
     * @see PrimitiveSet#equals(Object)
     */
    public static boolean equals(PrimitiveSet<?,?,?,?,?,?,?> set, Object o) {
        if (set == o)
            return true;
        if (o instanceof Set<?> set2) {
            if (set.size() != set2.size())
                return false;
            if (set.isEmpty())
                // Special case: empty sets are equal even if their type would
                // otherwise prevent them from being equal
                return true;
            return switch (set2) {
                case PrimitiveSet.OfDouble doubleSet2 ->
                    set instanceof PrimitiveSet.OfDouble doubleSet &&
                            doubleSet.containsAll(doubleSet2);
                case PrimitiveSet.OfInt intSet2 ->
                    set instanceof PrimitiveSet.OfInt intSet &&
                            intSet.containsAll(intSet2);
                case PrimitiveSet.OfLong longSet2 ->
                    set instanceof PrimitiveSet.OfLong longSet &&
                            longSet.containsAll(longSet2);
                default -> genericSetEquals(set, set2);
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
     */
    public static int hashCode(PrimitiveSet<?,?,?,?,?,?,?> set) {
        return switch (set.primitiveStream()) {
            case DoubleStream stream -> stream.mapToInt(Double::hashCode).sum();
            case IntStream stream -> stream.sum();
            case LongStream stream -> stream.mapToInt(Long::hashCode).sum();
            default -> throw new ClassCastException("Internal error");
        };
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

    @PrereleaseContent
    static abstract class EmptyPrimitiveIterator<T,T_CONS>
            implements PrimitiveIterator<T,T_CONS>, UnmodifiableView {
        @Override
        public void forEachRemaining(Consumer<? super T> action) {}

        @Override
        public void forEachRemaining(T_CONS action) {}

        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public T next() {
            throw new NoSuchElementException();
        }

        @Override
        public void remove() {
            throw new IllegalStateException();
        }
    }

    @PrereleaseContent
    static final class EmptyDoubleIterator
            extends EmptyPrimitiveIterator<Double,DoubleConsumer>
            implements PrimitiveIterator.OfDouble {
        static final EmptyDoubleIterator INSTANCE = new EmptyDoubleIterator();

        private EmptyDoubleIterator() {}

        @Override
        public double nextDouble() {
            throw new NoSuchElementException();
        }
    }

    @PrereleaseContent
    static final class EmptyIntIterator
            extends EmptyPrimitiveIterator<Integer,IntConsumer>
            implements PrimitiveIterator.OfInt {
        static final EmptyIntIterator INSTANCE = new EmptyIntIterator();

        private EmptyIntIterator() {}

        @Override
        public int nextInt() {
            throw new NoSuchElementException();
        }
    }

    @PrereleaseContent
    static final class EmptyLongIterator
            extends EmptyPrimitiveIterator<Long,LongConsumer>
            implements PrimitiveIterator.OfLong {
        static final EmptyLongIterator INSTANCE = new EmptyLongIterator();

        private EmptyLongIterator() {}

        @Override
        public long nextLong() {
            throw new NoSuchElementException();
        }
    }

    @PrereleaseContent
    static abstract class PrimitiveCollectionSpliterator<T,T_CONS,
            T_SPLITR extends Spliterator.OfPrimitive<T,T_CONS,T_SPLITR>>
            implements Spliterator.OfPrimitive<T,T_CONS,T_SPLITR> {
        static final int BATCH_UNIT = 1 << 10;  // batch array size increment
        static final int MAX_BATCH = 1 << 25;  // max batch array size
        private final int characteristics;
        int size;
        int batchSize;

        PrimitiveCollectionSpliterator(int characteristics) {
            this.characteristics = (characteristics & Spliterator.CONCURRENT) == 0 ?
                    characteristics | Spliterator.SIZED | Spliterator.SUBSIZED :
                    characteristics;
        }

        abstract void bindIterator();

        @Override
        public long estimateSize() {
            bindIterator();
            return size;
        }

        @Override
        @SuppressWarnings("MagicConstant")
        public int characteristics() {
            return characteristics;
        }

        @Override
        public Comparator<? super T> getComparator() {
            if (hasCharacteristics(SORTED))
                return null;
            throw new IllegalStateException();
        }

        @Override
        public abstract T_SPLITR trySplit();
    }

    @PrereleaseContent
    static class DoubleSpliterator extends PrimitiveCollectionSpliterator<Double,
            DoubleConsumer, Spliterator.OfDouble> implements Spliterator.OfDouble {
        private final PrimitiveCollection.OfDouble doubleCollection;
        private PrimitiveIterator.OfDouble doubleIterator;

        DoubleSpliterator(
                PrimitiveCollection.OfDouble doubleCollection,
                int characteristics
        ) {
            super(characteristics);
            this.doubleCollection = doubleCollection;
        }

        @Override
        void bindIterator() {
            if (doubleIterator == null) {
                doubleIterator = doubleCollection.iterator();
                size = doubleCollection.size();
            }
        }

        @Override
        public Spliterator.OfDouble trySplit() {
            bindIterator();
            var iterator = doubleIterator;
            if (size > 1 && iterator.hasNext()) {
                int n = batchSize + BATCH_UNIT;
                if (n > size)
                    n = size;
                if (n > MAX_BATCH)
                    n = MAX_BATCH;
                double[] a = new double[n];
                int j = 0;
                do {
                    a[j] = iterator.nextDouble();
                } while (++j < n && iterator.hasNext());
                batchSize = j;
                size -= j;
                return Spliterators.spliterator(a, 0, j, characteristics());
            }
            return null;
        }

        @Override
        public void forEachRemaining(DoubleConsumer action) {
            Objects.requireNonNull(action);
            bindIterator();
            doubleIterator.forEachRemaining(action);
        }

        @Override
        public boolean tryAdvance(DoubleConsumer action) {
            Objects.requireNonNull(action);
            bindIterator();
            if (doubleIterator.hasNext()) {
                action.accept(doubleIterator.nextDouble());
                return true;
            }
            return false;
        }
    }

    @PrereleaseContent
    static class IntSpliterator extends PrimitiveCollectionSpliterator<Integer,
            IntConsumer, Spliterator.OfInt> implements Spliterator.OfInt {
        private final PrimitiveCollection.OfInt intCollection;
        private PrimitiveIterator.OfInt intIterator;

        IntSpliterator(
                PrimitiveCollection.OfInt intCollection,
                int characteristics
        ) {
            super(characteristics);
            this.intCollection = intCollection;
        }

        @Override
        void bindIterator() {
            if (intIterator == null) {
                intIterator = intCollection.iterator();
                size = intCollection.size();
            }
        }

        @Override
        public Spliterator.OfInt trySplit() {
            bindIterator();
            if (size > 1 && intIterator.hasNext()) {
                int n = batchSize + BATCH_UNIT;
                if (n > size)
                    n = size;
                if (n > MAX_BATCH)
                    n = MAX_BATCH;
                int[] a = new int[n];
                int j = 0;
                do {
                    a[j] = intIterator.nextInt();
                } while (++j < n && intIterator.hasNext());
                batchSize = j;
                size -= j;
                return Spliterators.spliterator(a, 0, j, characteristics());
            }
            return null;
        }

        @Override
        public void forEachRemaining(IntConsumer action) {
            Objects.requireNonNull(action);
            bindIterator();
            intIterator.forEachRemaining(action);
        }

        @Override
        public boolean tryAdvance(IntConsumer action) {
            Objects.requireNonNull(action);
            bindIterator();
            if (intIterator.hasNext()) {
                action.accept(intIterator.nextInt());
                return true;
            }
            return false;
        }
    }

    @PrereleaseContent
    static class LongSpliterator extends PrimitiveCollectionSpliterator<Long,
            LongConsumer, Spliterator.OfLong> implements Spliterator.OfLong {
        private final PrimitiveCollection.OfLong longCollection;
        private PrimitiveIterator.OfLong longIterator;

        LongSpliterator(
                PrimitiveCollection.OfLong longCollection,
                int characteristics
        ) {
            super(characteristics);
            this.longCollection = longCollection;
        }

        @Override
        void bindIterator() {
            if (longIterator == null) {
                longIterator = longCollection.iterator();
                size = longCollection.size();
            }
        }

        @Override
        public Spliterator.OfLong trySplit() {
            bindIterator();
            if (size > 1 && longIterator.hasNext()) {
                int n = batchSize + BATCH_UNIT;
                if (n > size)
                    n = size;
                if (n > MAX_BATCH)
                    n = MAX_BATCH;
                long[] a = new long[n];
                int j = 0;
                do {
                    a[j] = longIterator.nextLong();
                } while (++j < n && longIterator.hasNext());
                batchSize = j;
                size -= j;
                return Spliterators.spliterator(a, 0, j, characteristics());
            }
            return null;
        }

        @Override
        public void forEachRemaining(LongConsumer action) {
            Objects.requireNonNull(action);
            bindIterator();
            longIterator.forEachRemaining(action);
        }

        @Override
        public boolean tryAdvance(LongConsumer action) {
            Objects.requireNonNull(action);
            bindIterator();
            if (longIterator.hasNext()) {
                action.accept(longIterator.nextLong());
                return true;
            }
            return false;
        }
    }

    @PrereleaseContent
    static abstract class SingletonPrimitiveSpliterator<T,T_CONS,
            T_SPLITR extends Spliterator.OfPrimitive<T,T_CONS,T_SPLITR>>
            implements Spliterator.OfPrimitive<T,T_CONS,T_SPLITR> {
        boolean exhausted;

        @Override
        public T_SPLITR trySplit() {
            return null;
        }

        @Override
        public abstract boolean tryAdvance(T_CONS action);

        @Override
        public void forEachRemaining(T_CONS action) {
            tryAdvance(action);
        }

        @Override
        public long estimateSize() {
            return exhausted ? 0L : 1L;
        }

        @Override
        public int characteristics() {
            return Spliterator.NONNULL | Spliterator.SIZED | Spliterator.SUBSIZED |
                    Spliterator.IMMUTABLE | Spliterator.DISTINCT | Spliterator.ORDERED;
        }
    }

    @PrereleaseContent
    static class SingletonDoubleSpliterator
            extends SingletonPrimitiveSpliterator<Double, DoubleConsumer, Spliterator.OfDouble>
            implements Spliterator.OfDouble {
        private final double value;

        SingletonDoubleSpliterator(double value) {
            this.value = value;
        }

        @Override
        public boolean tryAdvance(DoubleConsumer action) {
            Objects.requireNonNull(action);
            if (exhausted)
                return false;
            exhausted = true;
            action.accept(value);
            return true;
        }
    }

    @PrereleaseContent
    static class SingletonIntSpliterator
            extends SingletonPrimitiveSpliterator<Integer, IntConsumer, Spliterator.OfInt>
            implements Spliterator.OfInt {
        private final int value;

        SingletonIntSpliterator(int value) {
            this.value = value;
        }

        @Override
        public boolean tryAdvance(IntConsumer action) {
            Objects.requireNonNull(action);
            if (exhausted)
                return false;
            exhausted = true;
            action.accept(value);
            return true;
        }
    }

    @PrereleaseContent
    static class SingletonLongSpliterator
            extends SingletonPrimitiveSpliterator<Long, LongConsumer, Spliterator.OfLong>
            implements Spliterator.OfLong {
        private final long value;

        SingletonLongSpliterator(long value) {
            this.value = value;
        }

        @Override
        public boolean tryAdvance(LongConsumer action) {
            Objects.requireNonNull(action);
            if (exhausted)
                return false;
            exhausted = true;
            action.accept(value);
            return true;
        }
    }

    @PrereleaseContent
    static abstract class UnmodifiableCollection<E>
            implements Collection<E>, UnmodifiableView {
        @Override
        public boolean add(E e) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean addAll(Collection<? extends E> c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void clear() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean remove(Object o) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean removeIf(Predicate<? super E> filter) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            throw new UnsupportedOperationException();
        }
    }

    @PrereleaseContent
    static abstract class UnmodifiablePrimitiveCollection<T,T_ARR,T_CONS,T_PRED,
            T_SPLITR extends Spliterator.OfPrimitive<T,T_CONS,T_SPLITR>,
            T_STR extends BaseStream<T,T_STR>,
            T_COLL extends PrimitiveCollection<T,T_ARR,T_CONS,T_PRED,T_SPLITR,T_STR,T_COLL>>
            extends UnmodifiableCollection<T>
            implements PrimitiveCollection<T,T_ARR,T_CONS,T_PRED,T_SPLITR,T_STR,T_COLL> {
        @Override
        public boolean addAll(T_COLL c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean removeAll(T_COLL c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean removeIf(T_PRED filter) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean retainAll(T_COLL c) {
            throw new UnsupportedOperationException();
        }
    }

    static abstract class EmptyPrimitiveCollection<T,T_ARR,T_CONS,T_PRED,
            T_SPLITR extends Spliterator.OfPrimitive<T,T_CONS,T_SPLITR>,
            T_STR extends BaseStream<T,T_STR>,
            T_COLL extends PrimitiveCollection<T,T_ARR,T_CONS,T_PRED,T_SPLITR,T_STR,T_COLL>>
            extends UnmodifiablePrimitiveCollection<T,T_ARR,T_CONS,T_PRED,T_SPLITR,T_STR,T_COLL> {
        @Override
        public boolean contains(Object o) {
            return false;
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            return c.isEmpty();
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public abstract PrimitiveIterator<T,T_CONS> iterator();

        @Override
        public T_STR parallelPrimitiveStream() {
            return primitiveStream();
        }

        @Override
        public Stream<T> parallelStream() {
            return Stream.empty();
        }

        @Override
        public abstract T_STR primitiveStream();

        @Override
        public int size() {
            return 0;
        }

        @Override
        public abstract Spliterator.OfPrimitive<T,T_CONS,T_SPLITR> spliterator();

        @Override
        public Stream<T> stream() {
            return Stream.empty();
        }

        @Override
        public Object[] toArray() {
            return new Object[0];
        }

        @Override
        public <U> U[] toArray(U[] a) {
            if (a.length > 0)
                a[0] = null;
            return a;
        }

        @Override
        public abstract T_ARR toPrimitiveArray();
    }

    @PrereleaseContent
    static class EmptyDoubleCollection extends EmptyPrimitiveCollection<Double,double[],
            DoubleConsumer,DoublePredicate,Spliterator.OfDouble, DoubleStream,
            PrimitiveCollection.OfDouble> implements PrimitiveCollection.OfDouble {
        static final EmptyDoubleCollection INSTANCE = new EmptyDoubleCollection();

        private EmptyDoubleCollection() {}

        @Override
        public boolean containsAll(PrimitiveCollection.OfDouble c) {
            return c.isEmpty();
        }

        @Override
        public boolean containsDouble(double d) {
            return false;
        }

        @Override
        public PrimitiveIterator.OfDouble iterator() {
            return EmptyDoubleIterator.INSTANCE;
        }

        @Override
        public DoubleStream primitiveStream() {
            return DoubleStream.empty();
        }

        @Override
        public Spliterator.OfDouble spliterator() {
            return Spliterators.emptyDoubleSpliterator();
        }

        @Override
        public double[] toPrimitiveArray() {
            return new double[0];
        }
    }

    @PrereleaseContent
    static class EmptyDoubleSet extends EmptyDoubleCollection
            implements PrimitiveSet.OfDouble {
        static final EmptyDoubleSet INSTANCE = new EmptyDoubleSet();

        private EmptyDoubleSet() {}

        @Override
        public boolean equals(Object o) {
            return o == this || (o instanceof Set<?> set && set.isEmpty());
        }

        @Override
        public int hashCode() {
            return 0;
        }
    }

    @PrereleaseContent
    static class EmptyIntCollection extends EmptyPrimitiveCollection<Integer,int[],
            IntConsumer, IntPredicate, Spliterator.OfInt, IntStream,
            PrimitiveCollection.OfInt> implements PrimitiveCollection.OfInt {
        static final EmptyIntCollection INSTANCE = new EmptyIntCollection();

        private EmptyIntCollection() {}

        @Override
        public boolean containsAll(PrimitiveCollection.OfInt collection) {
            return collection.isEmpty();
        }

        @Override
        public boolean containsInt(int i) {
            return false;
        }

        @Override
        public PrimitiveIterator.OfInt iterator() {
            return EmptyIntIterator.INSTANCE;
        }

        @Override
        public IntStream primitiveStream() {
            return IntStream.empty();
        }

        @Override
        public Spliterator.OfInt spliterator() {
            return Spliterators.emptyIntSpliterator();
        }

        @Override
        public int[] toPrimitiveArray() {
            return new int[0];
        }
    }

    @PrereleaseContent
    static class EmptyIntSet extends EmptyIntCollection
            implements PrimitiveSet.OfInt {
        static final EmptyIntSet INSTANCE = new EmptyIntSet();

        private EmptyIntSet() {}

        @Override
        public boolean equals(Object o) {
            return o == this || (o instanceof Set<?> set && set.isEmpty());
        }

        @Override
        public int hashCode() {
            return 0;
        }
    }

    @PrereleaseContent
    static class EmptyLongCollection extends EmptyPrimitiveCollection<Long,long[],
            LongConsumer, LongPredicate, Spliterator.OfLong, LongStream,
            PrimitiveCollection.OfLong> implements PrimitiveCollection.OfLong {
        static final EmptyLongCollection INSTANCE = new EmptyLongCollection();

        private EmptyLongCollection() {}

        @Override
        public boolean containsAll(PrimitiveCollection.OfLong collection) {
            return collection.isEmpty();
        }

        @Override
        public boolean containsLong(long l) {
            return false;
        }

        @Override
        public PrimitiveIterator.OfLong iterator() {
            return EmptyLongIterator.INSTANCE;
        }

        @Override
        public LongStream primitiveStream() {
            return LongStream.empty();
        }

        @Override
        public Spliterator.OfLong spliterator() {
            return Spliterators.emptyLongSpliterator();
        }

        @Override
        public long[] toPrimitiveArray() {
            return new long[0];
        }
    }

    @PrereleaseContent
    static class EmptyLongSet extends EmptyLongCollection
            implements PrimitiveSet.OfLong {
        static final EmptyLongSet INSTANCE = new EmptyLongSet();

        private EmptyLongSet() {}

        @Override
        public boolean equals(Object o) {
            return o == this || (o instanceof Set<?> set && set.isEmpty());
        }

        @Override
        public int hashCode() {
            return 0;
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> T[] newArray(T[] a, int length) {
        return (T[]) java.lang.reflect.Array.newInstance(
                a.getClass().getComponentType(), length);
    }

    @PrereleaseContent
    static abstract class PrimitiveSingleton<T,T_ARR,T_CONS,T_PRED,
            T_SPLITR extends Spliterator.OfPrimitive<T,T_CONS,T_SPLITR>,
            T_STR extends BaseStream<T,T_STR>,
            T_COLL extends PrimitiveCollection<T,T_ARR,T_CONS,T_PRED,T_SPLITR,T_STR,T_COLL>>
            extends UnmodifiablePrimitiveCollection<T,T_ARR,T_CONS,T_PRED,T_SPLITR,T_STR,T_COLL>
            implements PrimitiveSet<T,T_ARR,T_CONS,T_PRED,T_SPLITR,T_STR,T_COLL> {
        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public int size() {
            return 1;
        }

        @Override
        public T_STR parallelPrimitiveStream() {
            return primitiveStream();
        }

        @Override
        public Stream<T> parallelStream() {
            return stream();
        }

        @Override
        public <U> U[] toArray(IntFunction<U[]> generator) {
            return toArray(generator.apply(1));
        }
    }

    static class DoubleSingleton extends PrimitiveSingleton<Double, double[],
            DoubleConsumer, DoublePredicate, Spliterator.OfDouble, DoubleStream,
            PrimitiveCollection.OfDouble> implements PrimitiveSet.OfDouble {
        private final double value;

        DoubleSingleton(double value) {
            this.value = value;
        }

        @Override
        public boolean contains(Object o) {
            return o instanceof Double d && value == d;
        }

        @Override
        public boolean containsAll(Collection<?> collection) {
            if (collection instanceof PrimitiveCollection.OfDouble ofDouble)
                return containsAll(ofDouble);
            return switch (collection.size()) {
                case 0 -> true;
                case 1 -> collection.contains(value);
                default -> false;
            };
        }

        @Override
        public boolean containsAll(PrimitiveCollection.OfDouble collection) {
            return switch (collection.size()) {
                case 0 -> true;
                case 1 -> collection.containsDouble(value);
                default -> false;
            };
        }

        @Override
        public boolean containsDouble(double d) {
            return value == d;
        }

        @Override
        public boolean equals(Object o) {
            if (o == this)
                return true;
            return switch (o) {
                case PrimitiveSet.OfDouble set -> set.size() == 1 &&
                        set.containsDouble(value);
                case Set<?> set -> set.size() == 1 && set.contains(value);
                case null, default -> false;
            };
        }

        @Override
        public void forEach(Consumer<? super Double> action) {
            action.accept(value);
        }

        @Override
        public void forEach(DoubleConsumer action) {
            action.accept(value);
        }

        @Override
        public int hashCode() {
            return Double.hashCode(value);
        }

        @Override
        public PrimitiveIterator.OfDouble iterator() {
            return Spliterators.iterator(spliterator());
        }

        @Override
        public DoubleStream primitiveStream() {
            return DoubleStream.of(value);
        }

        @Override
        public Spliterator.OfDouble spliterator() {
            return new SingletonDoubleSpliterator(value);
        }

        @Override
        public Stream<Double> stream() {
            return Stream.of(value);
        }

        @Override
        public Object[] toArray() {
            return new Object[] { value };
        }

        @Override
        @SuppressWarnings("unchecked")
        public <U> U[] toArray(U[] a) {
            if (a.length < 1)
                a = newArray(a, 1);
            a[0] = (U) Double.valueOf(value);
            if (a.length > 1)
                a[1] = null;
            return a;
        }

        @Override
        public double[] toPrimitiveArray() {
            return new double[] { value };
        }
    }

    @PrereleaseContent
    static class IntSingleton extends PrimitiveSingleton<Integer, int[],
            IntConsumer, IntPredicate, Spliterator.OfInt, IntStream,
            PrimitiveCollection.OfInt> implements PrimitiveSet.OfInt {
        private final int value;

        IntSingleton(int value) {
            this.value = value;
        }

        @Override
        public boolean contains(Object o) {
            return o instanceof Integer i && value == i;
        }

        @Override
        public boolean containsAll(Collection<?> collection) {
            if (collection instanceof PrimitiveCollection.OfInt ofInt)
                return containsAll(ofInt);
            return switch (collection.size()) {
                case 0 -> true;
                case 1 -> collection.contains(value);
                default -> false;
            };
        }

        @Override
        public boolean containsAll(PrimitiveCollection.OfInt collection) {
            return switch (collection.size()) {
                case 0 -> true;
                case 1 -> collection.containsInt(value);
                default -> false;
            };
        }

        @Override
        public boolean containsInt(int i) {
            return value == i;
        }

        @Override
        public boolean equals(Object o) {
            if (o == this)
                return true;
            return switch (o) {
                case PrimitiveSet.OfInt set -> set.size() == 1 &&
                        set.containsInt(value);
                case Set<?> set -> set.size() == 1 && set.contains(value);
                case null, default -> false;
            };
        }

        @Override
        public void forEach(Consumer<? super Integer> action) {
            if (action instanceof IntConsumer dc)
                dc.accept(value);
            else
                action.accept(value);
        }

        @Override
        public void forEach(IntConsumer action) {
            action.accept(value);
        }

        @Override
        public int hashCode() {
            return Integer.hashCode(value);
        }

        @Override
        public PrimitiveIterator.OfInt iterator() {
            return Spliterators.iterator(spliterator());
        }

        @Override
        public IntStream primitiveStream() {
            return IntStream.of(value);
        }

        @Override
        public Spliterator.OfInt spliterator() {
            return new SingletonIntSpliterator(value);
        }

        @Override
        public Stream<Integer> stream() {
            return Stream.of(value);
        }

        @Override
        public Object[] toArray() {
            return new Object[] { value };
        }

        @Override
        @SuppressWarnings("unchecked")
        public <U> U[] toArray(U[] a) {
            if (a.length < 1)
                a = newArray(a, 1);
            a[0] = (U) Integer.valueOf(value);
            if (a.length > 1)
                a[1] = null;
            return a;
        }

        @Override
        public int[] toPrimitiveArray() {
            return new int[] { value };
        }
    }

    static class LongSingleton extends PrimitiveSingleton<Long, long[],
            LongConsumer, LongPredicate, Spliterator.OfLong, LongStream,
            PrimitiveCollection.OfLong> implements PrimitiveSet.OfLong {
        private final long value;

        LongSingleton(long value) {
            this.value = value;
        }

        @Override
        public boolean contains(Object o) {
            return o instanceof Long l && value == l;
        }

        @Override
        public boolean containsAll(Collection<?> collection) {
            if (collection instanceof PrimitiveCollection.OfLong ofLong)
                return containsAll(ofLong);
            return switch (collection.size()) {
                case 0 -> true;
                case 1 -> collection.contains(value);
                default -> false;
            };
        }

        @Override
        public boolean containsAll(PrimitiveCollection.OfLong collection) {
            return switch (collection.size()) {
                case 0 -> true;
                case 1 -> collection.containsLong(value);
                default -> false;
            };
        }

        @Override
        public boolean containsLong(long l) {
            return value == l;
        }

        @Override
        public boolean equals(Object o) {
            if (o == this)
                return true;
            return switch (o) {
                case PrimitiveSet.OfLong set -> set.size() == 1 &&
                        set.containsLong(value);
                case Set<?> set -> set.size() == 1 && set.contains(value);
                case null, default -> false;
            };
        }

        @Override
        public void forEach(Consumer<? super Long> action) {
            if (action instanceof LongConsumer lc)
                lc.accept(value);
            else
                action.accept(value);
        }

        @Override
        public void forEach(LongConsumer action) {
            action.accept(value);
        }

        @Override
        public int hashCode() {
            return Long.hashCode(value);
        }

        @Override
        public PrimitiveIterator.OfLong iterator() {
            return Spliterators.iterator(spliterator());
        }

        @Override
        public LongStream primitiveStream() {
            return LongStream.of(value);
        }

        @Override
        public Spliterator.OfLong spliterator() {
            return new SingletonLongSpliterator(value);
        }

        @Override
        public Stream<Long> stream() {
            return Stream.of(value);
        }

        @Override
        public Object[] toArray() {
            return new Object[] { value };
        }

        @Override
        @SuppressWarnings("unchecked")
        public <U> U[] toArray(U[] a) {
            if (a.length < 1)
                a = newArray(a, 1);
            a[0] = (U) Long.valueOf(value);
            if (a.length > 1)
                a[1] = null;
            return a;
        }

        @Override
        public long[] toPrimitiveArray() {
            return new long[] { value };
        }
    }

    @PrereleaseContent
    static abstract class ArrayPrimitiveSet<T,T_ARR,T_CONS,T_PRED,
            T_SPLITR extends Spliterator.OfPrimitive<T,T_CONS,T_SPLITR>,
            T_STR extends BaseStream<T,T_STR>,
            T_COLL extends PrimitiveCollection<T,T_ARR,T_CONS,T_PRED,T_SPLITR,T_STR,T_COLL>>
            extends UnmodifiablePrimitiveCollection<T,T_ARR,T_CONS,T_PRED,T_SPLITR,T_STR,T_COLL>
            implements PrimitiveSet<T,T_ARR,T_CONS,T_PRED,T_SPLITR,T_STR,T_COLL> {
        private final int size;

        ArrayPrimitiveSet(int size) {
            this.size = size;
        }

        @Override
        public int size() {
            return size;
        }

        @Override
        public <U> U[] toArray(IntFunction<U[]> generator) {
            return toArray(generator.apply(size));
        }
    }

    @PrereleaseContent
    static class ArrayDoubleSet extends ArrayPrimitiveSet<Double, double[],
            DoubleConsumer, DoublePredicate, Spliterator.OfDouble, DoubleStream,
            PrimitiveCollection.OfDouble> implements PrimitiveSet.OfDouble {
        private final double[] arr;

        private ArrayDoubleSet(double[] arr) {
            super(arr.length);
            this.arr = arr;
        }

        private static double[] validate(double[] arr) {
            for (int i = 1; i < arr.length; i++)
                for (int j = 0; j < i; j++)
                    if (arr[i] == arr[j])
                        throw new IllegalArgumentException("Duplicate element");
            return arr;
        }

        static PrimitiveSet.OfDouble fromArray(double[] a) {
            return switch (a.length) {
                case 0 -> EmptyDoubleSet.INSTANCE;
                case 1 -> new DoubleSingleton(a[0]);
                default -> {
                    var arr = Arrays.copyOf(a, a.length);
                    yield new ArrayDoubleSet(validate(arr));
                }
            };
        }

        static PrimitiveSet.OfDouble fromCollection(
                PrimitiveCollection.OfDouble collection) {
            // TODO: Validate collection class and avoid defensive copy
            //  for known safe implementations
            var a = collection.toPrimitiveArray();
            return switch (a.length) {
                case 0 -> EmptyDoubleSet.INSTANCE;
                case 1 -> new DoubleSingleton(a[0]);
                default -> {
                    var arr = Arrays.copyOf(a, a.length);
                    yield new ArrayDoubleSet(validate(arr));
                }
            };
        }

        @Override
        public boolean containsDouble(double d) {
            for (double v : arr)
                if (v == d)
                    return true;
            return false;
        }

        @Override
        @SuppressWarnings("EqualsDoesntCheckParameterClass")
        public boolean equals(Object o) {
            return PrimitiveCollections.equals(this, o);
        }

        @Override
        public int hashCode() {
            return PrimitiveCollections.hashCode(this);
        }

        @Override
        public PrimitiveIterator.OfDouble iterator() {
            return Spliterators.iterator(spliterator());
        }

        @Override
        public DoubleStream primitiveStream() {
            return DoubleStream.of(arr);
        }

        @Override
        public Spliterator.OfDouble spliterator() {
            return Spliterators.spliterator(arr,
                    Spliterator.IMMUTABLE |
                    Spliterator.ORDERED |
                    Spliterator.DISTINCT);
        }

        @Override
        public Stream<Double> stream() {
            return DoubleStream.of(arr).boxed();
        }

        @Override
        public Object[] toArray() {
            return DoubleStream.of(arr).boxed().toArray();
        }

        @Override
        @SuppressWarnings("unchecked")
        public <U> U[] toArray(U[] a) {
            if (a.length < arr.length)
                a = newArray(a, arr.length);
            for (int i = 0; i < arr.length; i++)
                a[i] = (U) Double.valueOf(arr[i]);
            if (a.length > arr.length)
                a[arr.length] = null;
            return a;
        }

        @Override
        public double[] toPrimitiveArray() {
            return Arrays.copyOf(arr, arr.length);
        }
    }

    @PrereleaseContent
    static class ArrayIntSet extends ArrayPrimitiveSet<Integer, int[],
            IntConsumer, IntPredicate, Spliterator.OfInt, IntStream,
            PrimitiveCollection.OfInt> implements PrimitiveSet.OfInt {
        private final int[] arr;

        private ArrayIntSet(int[] arr) {
            super(arr.length);
            this.arr = arr;
        }

        private static int[] validate(int[] arr) {
            for (int i = 1; i < arr.length; i++)
                for (int j = 0; j < i; j++)
                    if (arr[i] == arr[j])
                        throw new IllegalArgumentException("Duplicate element");
            return arr;
        }

        static PrimitiveSet.OfInt fromArray(int[] a) {
            return switch (a.length) {
                case 0 -> EmptyIntSet.INSTANCE;
                case 1 -> new IntSingleton(a[0]);
                default -> new ArrayIntSet(validate(Arrays.copyOf(a, a.length)));
            };
        }

        static PrimitiveSet.OfInt fromCollection(PrimitiveCollection.OfInt collection) {
            // TODO: Validate collection class and avoid defensive copy
            // for known safe implementations
            var a = collection.toPrimitiveArray();
            return switch (a.length) {
                case 0 -> EmptyIntSet.INSTANCE;
                case 1 -> new IntSingleton(a[0]);
                default -> {
                    var arr = Arrays.copyOf(a, a.length);
                    yield new ArrayIntSet(validate(arr));
                }
            };
        }

        @Override
        public boolean containsInt(int i) {
            for (int v : arr)
                if (v == i)
                    return true;
            return false;
        }

        @Override
        @SuppressWarnings("EqualsDoesntCheckParameterClass")
        public boolean equals(Object o) {
            return PrimitiveCollections.equals(this, o);
        }

        @Override
        public int hashCode() {
            return PrimitiveCollections.hashCode(this);
        }

        @Override
        public PrimitiveIterator.OfInt iterator() {
            return Spliterators.iterator(spliterator());
        }

        @Override
        public IntStream primitiveStream() {
            return IntStream.of(arr);
        }

        @Override
        public Spliterator.OfInt spliterator() {
            return Spliterators.spliterator(arr,
                    Spliterator.IMMUTABLE |
                    Spliterator.ORDERED |
                    Spliterator.DISTINCT);
        }

        @Override
        public Stream<Integer> stream() {
            return IntStream.of(arr).boxed();
        }

        @Override
        public Object[] toArray() {
            return IntStream.of(arr).boxed().toArray();
        }

        @Override
        @SuppressWarnings("unchecked")
        public <U> U[] toArray(U[] a) {
            if (a.length < arr.length)
                a = newArray(a, arr.length);
            for (int i = 0; i < arr.length; i++)
                a[i] = (U) Integer.valueOf(arr[i]);
            if (a.length > arr.length)
                a[arr.length] = null;
            return a;
        }

        @Override
        public int[] toPrimitiveArray() {
            return Arrays.copyOf(arr, arr.length);
        }
    }

    @PrereleaseContent
    static class ArrayLongSet extends ArrayPrimitiveSet<Long, long[],
            LongConsumer, LongPredicate, Spliterator.OfLong, LongStream,
            PrimitiveCollection.OfLong> implements PrimitiveSet.OfLong {
        private final long[] arr;

        private ArrayLongSet(long[] arr) {
            super(arr.length);
            this.arr = arr;
        }

        private static long[] validate(long[] arr) {
            for (int i = 1; i < arr.length; i++)
                for (int j = 0; j < i; j++)
                    if (arr[i] == arr[j])
                        throw new IllegalArgumentException("Duplicate element");
            return arr;
        }

        static PrimitiveSet.OfLong fromArray(long[] a) {
            return switch (a.length) {
                case 0 -> EmptyLongSet.INSTANCE;
                case 1 -> new LongSingleton(a[0]);
                default -> new ArrayLongSet(validate(Arrays.copyOf(a, a.length)));
            };
        }

        static PrimitiveSet.OfLong fromCollection(PrimitiveCollection.OfLong collection) {
            // TODO: Validate collection class and avoid defensive copy
            // for known safe implementations
            var a = collection.toPrimitiveArray();
            return switch (a.length) {
                case 0 -> EmptyLongSet.INSTANCE;
                case 1 -> new LongSingleton(a[0]);
                default -> {
                    var arr = Arrays.copyOf(a, a.length);
                    yield new ArrayLongSet(validate(arr));
                }
            };
        }

        @Override
        public boolean containsLong(long l) {
            for (long v : arr)
                if (v == l)
                    return true;
            return false;
        }

        @Override
        @SuppressWarnings("EqualsDoesntCheckParameterClass")
        public boolean equals(Object o) {
            return PrimitiveCollections.equals(this, o);
        }

        @Override
        public int hashCode() {
            return PrimitiveCollections.hashCode(this);
        }

        @Override
        public PrimitiveIterator.OfLong iterator() {
            return Spliterators.iterator(spliterator());
        }

        @Override
        public LongStream primitiveStream() {
            return LongStream.of(arr);
        }

        @Override
        public Spliterator.OfLong spliterator() {
            return Spliterators.spliterator(arr,
                    Spliterator.IMMUTABLE |
                    Spliterator.ORDERED |
                    Spliterator.DISTINCT);
        }

        @Override
        public Stream<Long> stream() {
            return LongStream.of(arr).boxed();
        }

        @Override
        public Object[] toArray() {
            return LongStream.of(arr).boxed().toArray();
        }

        @Override
        @SuppressWarnings("unchecked")
        public <U> U[] toArray(U[] a) {
            if (a.length < arr.length)
                a = newArray(a, arr.length);
            for (int i = 0; i < arr.length; i++)
                a[i] = (U) Long.valueOf(arr[i]);
            if (a.length > arr.length)
                a[arr.length] = null;
            return a;
        }

        @Override
        public long[] toPrimitiveArray() {
            return Arrays.copyOf(arr, arr.length);
        }
    }
}
