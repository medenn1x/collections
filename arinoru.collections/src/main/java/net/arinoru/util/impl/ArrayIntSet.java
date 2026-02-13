package net.arinoru.util.impl;

import net.arinoru.util.IntCollection;
import net.arinoru.util.IntSet;
import net.arinoru.util.PrimitiveCollections;
import net.arinoru.util.PrimitiveSet;

import java.util.Arrays;
import java.util.PrimitiveIterator;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.IntConsumer;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * <p>An internal implementation of an unmodifiable {@link IntSet} backed by an
 * array. This implementation is intended to be used for sets with two or more
 * elements.</p>
 */
public class ArrayIntSet extends UnmodifiablePrimitiveCollection<Integer,int[],
        IntConsumer,IntPredicate,PrimitiveIterator.OfInt,Spliterator.OfInt,IntStream,
        IntCollection> implements IntSet {
    private final int[] arr;

    private ArrayIntSet(int[] arr) {
        this.arr = arr;
    }

    private static int[] validate(int[] arr) {
        for (int i = 1; i < arr.length; i++)
            for (int j = 0; j < i; j++)
                if (arr[i] == arr[j])
                    throw new IllegalArgumentException("Duplicate element");
        return arr;
    }

    /**
     * <p>Returns an unmodifiable int set backed by the contents of the specified
     * array. Note that the returned set may or may not be an instance of
     * {@link ArrayIntSet}, but it will always implement
     * {@link net.arinoru.util.views.UnmodifiableView}.</p>
     * @implNote <p>When returning an actual {@link ArrayIntSet}, this method will
     * create a defensive copy of the specified array. If the specified array has only
     * one element, this method will return a {@link IntSingleton} instead. If the
     * specified array has no elements, this method will return an instance of
     * {@link EmptyIntSet}.</p>
     * @param a array containing elements to back the set
     * @return unmodifiable int set backed by the array's elements
     * @throws IllegalArgumentException if the specified array contains duplicate
     * elements
     * @throws NullPointerException if the specified array is null
     */
    public static IntSet fromArray(int[] a) {
        return switch (a.length) {
            case 0 -> EmptyIntSet.INSTANCE;
            case 1 -> new IntSingleton(a[0]);
            default -> new ArrayIntSet(validate(Arrays.copyOf(a, a.length)));
        };
    }

    /**
     * <p>Returns an unmodifiable int set backed by the contents of the specified
     * collection. Note that the returned set may or may not be an instance of
     * {@link ArrayIntSet}, but it will always implement
     * {@link net.arinoru.util.views.UnmodifiableView}.</p>
     * @implNote <p>When returning an actual {@link ArrayIntSet}, the set will
     * be backed by the elements currently present in the specified collection. For
     * unknown implementations, this involves a defensive copy of the array returned
     * by {@link IntCollection#toPrimitiveArray()}. Known implementations may
     * result in additional implementation-specific optimizations.</p>
     * @param collection collection containing elements to back the set
     * @return unmodifiable int set backed by the collection's elements
     * @throws IllegalArgumentException if the specified collection contains
     * duplicate elements
     * @throws NullPointerException if the specified collection is null
     */
    public static IntSet fromCollection(IntCollection collection) {
        // TODO: Validate collection class and avoid defensive copy
        //  for known safe implementations
        var a = collection.toPrimitiveArray();
        return switch (a.length) {
            case 0 -> EmptyIntSet.INSTANCE;
            case 1 -> new IntSingleton(a[0]);
            default -> new ArrayIntSet(validate(Arrays.copyOf(a, a.length)));
        };
    }

    /**
     * <p>Returns {@code true} if this set contains the specified element.
     * More formally, returns {@code true} if and only if this set contains an
     * element {@code e} such that {@code element == e}.</p>
     * @implNote <p>Iterates over the backing array and returns true if an
     * element matches {@code element}.</p>
     * @param element element whose presence in this set is to be tested
     * @return {@code true} if this set contains the specified element
     */
    @Override
    public boolean containsInt(int element) {
        for (int v : arr)
            if (v == element)
                return true;
        return false;
    }

    /**
     * <p>Compares the specified object with this set for equality. Returns
     * {@code true} if the specified object is also a set, the two sets have the
     * same size, and every member of the specified set is contained in this set
     * (or equivalently, every member of this set is contained in the specified
     * set). This definition ensures that the equals method works properly
     * across different implementations of the set interface.</p>
     * @implNote <p>This method relies on
     * {@link PrimitiveCollections#equals(PrimitiveSet, Object)}.</p>
     * @param o object to be compared for equality with this set
     * @return {@code true} if the specified object is equal to this set
     * @see Object#equals(Object)
     * @see Set#equals(Object)
     */
    @Override
    @SuppressWarnings("EqualsDoesntCheckParameterClass")
    public boolean equals(Object o) {
        return PrimitiveCollections.equals(this, o);
    }

    /**
     * <p>Returns the hash code value for this set. The hash code of a set is
     * defined to be the sum of the hash codes of the elements in the set. This
     * ensures that {@code s1.equals(s2)} implies that
     * {@code s1.hashCode()==s2.hashCode()} for any two sets {@code s1} and
     * {@code s2}, as required by the general contract of
     * {@link Object#equals(Object)}.</p>
     * @implNote <p>This method relies on
     * {@link PrimitiveCollections#hashCode(PrimitiveSet)}.</p>
     * @return the hash code value for this set
     * @see Object#equals(Object)
     * @see Set#hashCode()
     */
    @Override
    public int hashCode() {
        return PrimitiveCollections.hashCode(this);
    }

    /**
     * <p>Returns a primitive iterator over the elements in this collection.
     * The elements are returned in the order in which they appear in the backing
     * array.</p>
     * @implNote <p>This method relies on
     * {@link Spliterators#iterator(Spliterator.OfInt)}.</p>
     * @return a {@code PrimitiveIterator.OfInt} over the elements in this set
     * @see #spliterator()
     */
    @Override
    public PrimitiveIterator.OfInt iterator() {
        return Spliterators.iterator(spliterator());
    }

    /**
     * <p>Returns a sequential {@code IntStream} with this set as its source.</p>
     * @implNote <p>This method simply obtains a {@code IntStream} over the
     * backline array.</p>
     * @return a sequential {@code IntStream} over the elements in this set
     * @see IntStream#of(int...)
     */
    @Override
    public IntStream primitiveStream() {
        return IntStream.of(arr);
    }

    /**
     * <p>Returns the number of elements in this set (its cardinality).</p>
     * @implNote <p>This method returns the length of the backing array.</p>
     * @return the number of elements in this set (its cardinality)
     */
    @Override
    public int size() {
        return arr.length;
    }

    /**
     * <p>Creates a {@code Spliterator.OfInt} over the elements in this set.</p>
     * @implNote <p>This method relies on the
     * {@link Spliterators#spliterator(int[], int)} method. The returned
     * spliterator reports the following characteristics:</p>
     * <ul><li>{@link Spliterator#IMMUTABLE}</li>
     * <li>{@link Spliterator#ORDERED}</li>
     * <li>{@link Spliterator#DISTINCT}</li></ul>
     * @return a {@code Spliterator.OfInt} over the elements in this set
     */
    @Override
    public Spliterator.OfInt spliterator() {
        return Spliterators.spliterator(arr, Spliterator.IMMUTABLE |
                Spliterator.ORDERED | Spliterator.DISTINCT);
    }

    /**
     * <p>Returns a sequential {@code Stream} with this set as its source.</p>
     * @implNote <p>This method creates a {@code IntStream} over the backing
     * array, then converts it into a boxed stream.</p>
     * @return a sequential {@code Stream} over the element in this set
     */
    @Override
    public Stream<Integer> stream() {
        return IntStream.of(arr).boxed();
    }

    /**
     * <p>Returns an array containing all of the elements in this set.</p>
     * @implNote <p>This implementation obtains a boxed stream over the backing
     * array, and then calls the {@link Stream#toArray()} method.</p>
     * @return an array containing all the elements in this set
     */
    @Override
    public Object[] toArray() {
        return IntStream.of(arr).boxed().toArray();
    }

    /**
     * <p>Returns an array containing all of the elements in this set. The runtime
     * type of the returned array is that of the array generated by the specified
     * function. If the generator produces an array larger than the number of
     * elements in the set, the element in the array immediately following the end
     * of the set is set to {@code null}.</p>
     * <p>The elements in the returned array will be in the same order as they
     * appear in the backing array.</p>
     * @param generator function that generates an array
     * @return an array containing all of the elements in this set
     * @param <U> The component type of the array to contain the set
     * @throws ArrayStoreException if the runtime type of {@link Integer} is not
     * assignable to the runtime component type of the array returned by the
     * function
     * @throws NullPointerException if generator is null, or its the apply method
     * returns null.
     * @throws OutOfMemoryError if it is not possible to allocate a sufficiently
     * large array to contain this set (i.e. the size of the set exceeds the
     * maximum capacity of a Java array)
     */
    @Override
    public <U> U[] toArray(IntFunction<U[]> generator) {
        return toArray(generator.apply(arr.length));
    }

    /**
     * <p>Returns an array containing all of the elements in this set; the
     * runtime type of the returned array is that of the specified array. If
     * the set fits in the specified array, it is returned therein. Otherwise,
     * a new array is allocated with the runtime type of the specified array and
     * the size of this set.</p>
     * <p>If this set fits in the specified array with room to spare (i.e.
     * the array has more elements than this set), the element in the array
     * immediately following the end of the set is set to {@code null}.</p>
     * <p>The elements in the returned array will be in the same order as they
     * appear in the backing array.</p>
     * @param a the array into which the elements of this array are to be
     *          stored, if it is big enough; otherwise, a new array of the
     *          same runtime type is allocated for this purpose.
     * @return an array containing all of the elements in this set
     * @param <U> the component type of the array to contain the set
     * @throws ArrayStoreException if the runtime type of any element in this
     * set is not assignable to the runtime component type of the
     * specified array
     * @throws NullPointerException if the specified array is null
     * @throws OutOfMemoryError if it is not possible to allocate a sufficiently
     * large array to contain this set (i.e. if the size of the set
     * exceeds the maximum capacity of a Java array)
     */
    @Override
    @SuppressWarnings("unchecked")
    public <U> U[] toArray(U[] a) {
        if (a.length < arr.length)
            a = ArrayImpl.newArray(a, arr.length);
        for (int i = 0; i < arr.length; i++)
            a[i] = (U) Integer.valueOf(arr[i]);
        if (a.length > arr.length)
            a[arr.length] = null;
        return a;
    }

    /**
     * <p>Returns an array containing all of the elements in this set. The
     * elements in the returned array will have the same order as the backing
     * array.</p>
     * <p>The returned array will be "safe" in that no references to it are
     * maintained by this set. (In other words, this method must allocate a new
     * array even if this set is backed by an array). The caller is thus free
     * to modify the returned array.</p>
     * @implNote <p>This method simply returns a copy of the backing array.</p>
     * @return an array containing all of the elements in this set
     */
    @Override
    public int[] toPrimitiveArray() {
        return Arrays.copyOf(arr, arr.length);
    }
}
