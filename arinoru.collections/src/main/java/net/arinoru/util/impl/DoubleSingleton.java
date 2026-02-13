package net.arinoru.util.impl;

import net.arinoru.util.DoubleCollection;
import net.arinoru.util.DoubleSet;

import java.util.Collection;
import java.util.PrimitiveIterator;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.DoublePredicate;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

/**
 * <p>An internal implementation of an unmodifiable {@link DoubleSet} containing a
 * single element.</p>
 */
public class DoubleSingleton extends PrimitiveSingleton<Double,double[],DoubleConsumer,
        DoublePredicate,PrimitiveIterator.OfDouble,Spliterator.OfDouble,DoubleStream,
        DoubleCollection> implements DoubleSet {
    private final double value;

    /**
     * <p>Create a set containing only the specified double value.</p>
     * @param value element contained within this set
     */
    public DoubleSingleton(double value) {
        this.value = value;
    }

    /**
     * <p>Returns {@code true} if this set contains the specified element. More
     * formally, returns {@code true} if and only if this set contains an element
     * {@code e} such that {@code Objects.equals(o, e)}.</p>
     * @implNote <p>This method returns {@code true} if {@code o} is a
     * {@link Double} whose value matches the value of the sole element in this
     * set.</p>
     * @param o element whose presence in this set is to be tested
     * @return {@code true} if this set contains the specified element
     */
    @Override
    public boolean contains(Object o) {
        return o instanceof Double d && value == d;
    }

    /**
     * <p>Returns {@code true} if this set contains all of the elements of the
     * specified collection. If the specified collection is also a set, this
     * method returns {@code true} if it is a <em>subset</em> of this set.</p>
     * @implNote <p>This method returns {@code true} if the size of the specified
     * collection is {@code 0}; if the specified collection has a size of {@code 1},
     * this method returns the result of calling the
     * {@link Collection#contains(Object)} method with the value of this set's
     * sole element (or {@link DoubleCollection#containsDouble(double)} if the
     * specified collection is a {@code DoubleCollection}). For any other
     * collection size, this method returns {@code false}.</p>
     * @param collection collection to be checked for containment in this set
     * @return {@code true} if this set contains all of the elements of the
     * specified collection
     * @throws ClassCastException if the specified collection's {@code contains}
     * method throws a ClassCastException when passed a {@link Double} value.
     * @throws NullPointerException if the specified collection is null
     */
    @Override
    public boolean containsAll(Collection<?> collection) {
        if (collection instanceof DoubleCollection doubles)
            return containsAll(doubles);
        return switch (collection.size()) {
            case 0 -> true;
            case 1 -> collection.contains(value);
            default -> false;
        };
    }

    /**
     * <p>Returns {@code true} if this set contains all of the elements of the
     * specified collection. If the specified collection is also a set, this
     * method returns {@code true} if it is a <em>subset</em> of this set.</p>
     * @implNote <p>This method returns {@code true} if the size of the specified
     * collection is {@code 0}; if the specified collection has a size of {@code 1},
     * this method returns the result of calling the
     * {@link DoubleCollection#containsDouble(double)} method with the value of
     * this set's sole element. For any other collection size, this method returns
     * {@code false}.</p>
     * @param collection collection to be checked for containment in this set
     * @return {@code true} if this set contains all of the elements of the
     * specified collection
     * @throws NullPointerException if the specified collection is null
     */
    @Override
    public boolean containsAll(DoubleCollection collection) {
        return switch (collection.size()) {
            case 0 -> true;
            case 1 -> collection.containsDouble(value);
            default -> false;
        };
    }

    /**
     * <p>Returns {@code true} if this set contains the specified element.
     * More formally, returns {@code true} if and only if this set contains an
     * element {@code e} such that {@code element == e}.</p>
     * @implNote <p>Returns true if the specified element is equal to the
     * sole element in this set.</p>
     * @param element element whose presence in this set is to be tested
     * @return {@code true} if this set contains the specified element
     */
    @Override
    public boolean containsDouble(double element) {
        return value == element;
    }

    /**
     * <p>Compares the specified object with this set for equality. Returns
     * {@code true} if the specified object is also a set, the two sets have the
     * same size, and every member of the specified set is contained in this set
     * (or equivalently, every member of this set is contained in the specified
     * set). This definition ensures that the equals method works properly
     * across different implementations of the set interface.</p>
     * @implNote <p>This method returns true if one of the following conditions
     * is met:</p>
     * <ul><li>{@code o} is this set.</li>
     * <li>{@code o} is a double set with size {@code 1} whose
     * {@link DoubleSet#containsDouble(double)} method returns true when passed
     * this set's sole element.</li>
     * <li>{@code o} is a set with size {@code 1} whose
     * {@link Set#contains(Object)} method returns true when passed this set's
     * sole element.</li></ul>
     * <p>Each condition preceding the last is meant to allow a less expensive
     * operation to determine equality.</p>
     * @param o object to be compared for equality with this set
     * @return {@code true} if the specified object is equal to this set
     */
   @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        return switch (o) {
            case DoubleSet set -> set.size() == 1 && set.containsDouble(value);
            case Set<?> set -> set.size() == 1 && set.contains(value);
            case null, default -> false;
        };
    }

    /**
     * <p>Performs the given action on this set's sole element. Exceptions
     * thrown by the action are relayed to the caller.</p>
     * @param action action to perform on the element of the set
     * @throws NullPointerException if the specified action is null
     */
    @Override
    public void forEach(Consumer<? super Double> action) {
        action.accept(value);
    }

    /**
     * <p>Performs the given action on this set's sole element. Exceptions
     * thrown by the action are relayed to the caller.</p>
     * @param action action to perform on the element of the set
     * @throws NullPointerException if the specified action is null
     */
    @Override
    public void forEach(DoubleConsumer action) {
        action.accept(value);
    }

    /**
     * <p>Returns the hash code value for this set. The hash code of a set is
     * defined to be the sum of the hash codes of the elements in the set. This
     * ensures that {@code s1.equals(s2)} implies that
     * {@code s1.hashCode()==s2.hashCode()} for any two sets {@code s1} and
     * {@code s2}, as required by the general contract of
     * {@link Object#equals(Object)}.</p>
     * @implNote <p>This method returns the result of calling
     * {@link Double#hashCode(double)} on this set's sole element.</p>
     * @return the hash code value for this set
     */
    @Override
    public int hashCode() {
        return Double.hashCode(value);
    }

    /**
     * <p>Returns a primitive iterator over the element in this set.</p>
     * @implNote <p>This method relies on
     * {@link Spliterators#iterator(Spliterator.OfDouble)}.</p>
     * @return a {@code PrimitiveIterator.OfDouble} over the elements in this set
     * @see #spliterator()
     */
    @Override
    public PrimitiveIterator.OfDouble iterator() {
        // TODO: Is it better to use DoubleStream.iterator instead?
        return Spliterators.iterator(spliterator());
    }

    /**
     * <p>Returns a sequential {@code DoubleStream} with this set as its
     * source.</p>
     * @implNote <p>This method simply obtains a {@code DoubleStream} over the
     * sole element.</p>
     * @return a sequential {@code DoubleStream} over the element in this set
     * @see DoubleStream#of(double)
     */
    @Override
    public DoubleStream primitiveStream() {
        return DoubleStream.of(value);
    }

    /**
     * <p>Creates a {@code Spliterator.OfDouble} over the element in this set.</p>
     * @implNote <p>This method returns a {@link SingletonDoubleSpliterator} with
     * the set's sole element.</p>
     * @return a {@code Spliterator.OfDouble} over the element in this set
     */
    @Override
    public Spliterator.OfDouble spliterator() {
        return new SingletonDoubleSpliterator(value);
    }

    /**
     * <p>Returns a sequential {@code Stream} with this set as its source.</p>
     * @implNote <p>this method creates a {@code Stream} over the set's sole
     * element.</p>
     * @return a sequential {@code Stream} over the element in this set
     * @see Stream#of(Object)
     */
    @Override
    public Stream<Double> stream() {
        return Stream.of(value);
    }

    /**
     * <p>Returns an array containing the element in this set.</p>
     * @implNote <p>This implementation creates a new single-element array
     * containing a boxed representation of this set's sole element.</p>
     * @return an array containing the element in this set
     */
    @Override
    public Object[] toArray() {
        return new Object[] { value };
    }

    /**
     * <p>Returns an array containing the element in this set; the
     * runtime type of the returned array is that of the specified array. If
     * the set fits in the specified array, it is returned therein. Otherwise,
     * a new array is allocated with the runtime type of the specified array and
     * the size of this set.</p>
     * <p>If this set fits in the specified array with room to spare (i.e.
     * the array has more elements than this set), the element in the array
     * immediately following the end of the set is set to {@code null}.</p>
     * @param a the array into which the elements of this array are to be
     *          stored, if it is big enough; otherwise, a new array of the
     *          same runtime type is allocated for this purpose.
     * @return an array containing the element in this set
     * @param <U> the component type of the array to contain the set
     * @throws ArrayStoreException if the runtime type of the element in this
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
        if (a.length < 1)
            a = ArrayImpl.newArray(a, 1);
        a[0] = (U) Double.valueOf(value);
        if (a.length > 1)
            a[1] = null;
        return a;
    }

    /**
     * <p>Returns an array containing the element in this set.</p>
     * <p>The returned array will be "safe" in that no references to it are
     * maintained by this set. The caller is thus free
     * to modify the returned array.</p>
     * @implNote <p>This implementation creates a new single-element array
     * containing this set's sole element.</p>
     * @return an array containing the element in this set
     */
    @Override
    public double[] toPrimitiveArray() {
        return new double[] { value };
    }
}
