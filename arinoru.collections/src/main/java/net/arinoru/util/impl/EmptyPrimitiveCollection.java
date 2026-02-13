package net.arinoru.util.impl;

import net.arinoru.util.PrimitiveCollection;

import java.util.Collection;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.stream.BaseStream;
import java.util.stream.Stream;

/**
 * <p>Abstract base class for implementing immutable primitive collections which have
 * no elements. This class provides implementations of all methods of
 * {@link PrimitiveCollection} that do not depend on the type specialization of the
 * collection.</p>
 * @param <T> primitive collection specialization boxed type
 * @param <T_ARR> primitive collection specialization array type
 * @param <T_CONS> primitive collection specialization consumer type
 * @param <T_PRED> primitive collection specialization predicate type
 * @param <T_ITER> primitive collection specialization iterator type
 * @param <T_SPLITR> primitive collection specialization spliterator type
 * @param <T_STR> primitive collection specialization stream type
 * @param <T_COLL> primitive collection specialization type
 */
public abstract class EmptyPrimitiveCollection<T,T_ARR,T_CONS,T_PRED,
        T_ITER extends PrimitiveIterator<T,T_CONS>,
        T_SPLITR extends Spliterator.OfPrimitive<T,T_CONS,T_SPLITR>,
        T_STR extends BaseStream<T,T_STR>,
        T_COLL extends PrimitiveCollection<T,T_ARR,T_CONS,T_PRED,T_ITER,T_SPLITR,T_STR,T_COLL>>
        extends UnmodifiablePrimitiveCollection<T,T_ARR,T_CONS,T_PRED,T_ITER,T_SPLITR,T_STR,T_COLL> {
    /**
     * <p>Returns {@code true} if this collection contains the specified element. More
     * formally, returns {@code true} if and only if this collection contains at least
     * one element {@code e} such that {@code Objects.equals(o, e)}.</p>
     * @implNote <p>This method always returns {@code false}.</p>
     * @param o element whose presence in this collection is to be tested
     * @return {@code true} if this collection contains the specified element
     */
    @Override
    public boolean contains(Object o) {
        return false;
    }

    /**
     * <p>Returns {@code true} if this collection contains all of the elements in
     * the specified collection.</p>
     * @implNote <p>This method returns {@code true} if the specified collection is
     * empty; otherwise, it returns {@code false}.</p>
     * @param collection collection to be checked for containment in this collection
     * @return {@code true} if this collection contains all elements in the
     * specified collection
     * @throws NullPointerException if the specified collection is null.
     */
    @Override
    public boolean containsAll(Collection<?> collection) {
        return collection.isEmpty();
    }

    /**
     * <p>Returns {@code true} if this collection contains all of the elements in
     * the specified collection.</p>
     * @implNote <p>This method returns {@code true} if the specified collection is
     * empty; otherwise, it returns {@code false}.</p>
     * @param collection collection to be checked for containment in this collection
     * @return {@code true} if this collection contains all elements in the
     * specified collection
     * @throws NullPointerException if the specified collection is null.
     */
    @Override
    public boolean containsAll(T_COLL collection) {
        return collection.isEmpty();
    }

    /**
     * <p>Compares the specified object with this collection for equality.</p>
     * @implNote <p>This method delegates to the underlying
     * {@link Object#equals(Object)}</p>
     * @param o object to be compared for equality with this collection
     * @return {@code true} if the specified object is equal to this collection
     */
    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    /**
     * <p>Performs the given action for each element of the collection
     * until all element have been processed or the action throws an
     * exception.</p>
     * @implNote <p>This method performs no operation.</p>
     * @param action action to perform on each element of the collection
     */
    @Override
    public void forEach(Consumer<? super T> action) { }

    /**
     * <p>Performs the given action for each element of the collection
     * until all element have been processed or the action throws an
     * exception.</p>
     * @implNote <p>This method performs no operation.</p>
     * @param action action to perform on each element of the collection
     */
    @Override
    public void forEach(T_CONS action) { }

    /**
     * <p>Returns the hash code value for this collection.</p>
     * @implNote <p>This method delegates to {@link Object#hashCode()}.</p>
     * @return the hash code value for this collection.
     */
    @Override
    public int hashCode() {
        return super.hashCode();
    }

    /**
     * <p>Returns {@code true} if this collection contains no elements.</p>
     * @implNote <p>This method always returns {@code true}.</p>
     * @return {@code true} if this collection contains no elements
     */
    @Override
    public boolean isEmpty() {
        return true;
    }

    /**
     * Returns a primitive iterator over the elements in this collection. There
     * are no guarantees concerning the order in which the elements are returned
     * (unless this collection is an instance of some class that provides a
     * guarantee).
     * @return a {@code PrimitiveIterator} over the elements in this collection
     */
    @Override
    public abstract T_ITER iterator();

    /**
     * <p>Returns a possibly parallel primitive stream with this
     * collection as its source. It is allowable for this method to return a
     * sequential primitive stream.</p>
     * @implNote <p>This method delegates to {@link #primitiveStream()}.</p>
     * @return a possibly parallel primitive stream over the elements
     * in this collection
     */
    @Override
    public T_STR parallelPrimitiveStream() {
        return primitiveStream();
    }

    /**
     * <p>Returns a possibly parallel {@code Stream} with this collection as its
     * source. It is allowable for this method to return a sequential stream.</p>
     * @implNote <p>This method returns an empty {@code Stream}.</p>
     * @return a possibly parallel {@code Stream} over the elements in this
     * collection
     */
    @Override
    public Stream<T> parallelStream() {
        return Stream.empty();
    }

    /**
     * <p>Returns a sequential primitive stream with this collection as
     * its source.</p>
     * @implSpec <p>Extending classes should override this method to return an
     * empty stream of the appropriate type.</p>
     * @return a sequential primitive stream over the elements in this
     * collection
     */
    @Override
    public abstract T_STR primitiveStream();

    /**
     * <p>Returns the number of elements in this collection.</p>
     * @implNote <p>This method always returns {@code 0}.</p>
     * @return the number of elements in this collection
     */
    @Override
    public int size() {
        return 0;
    }

    /**
     * <p>Creates a {@code Spliterator} over the elements in this collection.</p>
     * @implSpec <p>Extending classes should override this method to return an
     * empty primitive spliterator. Such a spliterator should report
     * {@link Spliterator#SIZED} and {@link Spliterator#SUBSIZED}.</p>
     * @return a {@code Spliterator} over the elements in this collection
     */
    @Override
    public abstract T_SPLITR spliterator();

    /**
     * <p>Returns a sequential {@code Stream} with this collection as its
     * source.</p>
     * @implNote <p>This method returns an empty {@code Stream}.</p>
     * @return a sequential {@code Stream} over the elements in this collection
     */
    @Override
    public Stream<T> stream() {
        return Stream.empty();
    }

    /**
     * <p>Returns an array containing all of the elements in this collection.
     * The returned array's runtime component type is {@code Object}.</p>
     * @implNote <p>This method returns an empty array of {@code Object}s.</p>
     * @return an array, whose runtime component type is {@code Object},
     * containing all of the elements in this collection
     */
    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    /**
     * <p>Returns an array containing all of the elements in this collection; the
     * runtime type of the returned array is that of the array returned by the
     * specified function.</p>
     * @implNote <p>This method returns the result of calling
     * {@code generator.apply(0)}.</p>
     * @param generator function that generates an array
     * @return an array containing all of the elements of this collection
     * @param <U> the component type of the array to contain the collection
     * @throws NullPointerException if generator is null
     */
    @Override
    public <U> U[] toArray(IntFunction<U[]> generator) {
        return generator.apply(0);
    }

    /**
     * <p>Returns an array containing all of the elements in this collection; the
     * runtime type of the returned array is that of the specified array.</p>
     * @implNote <p>This method always returns the specified array. If the
     * specified array has a length greater than zero, its first element is set
     * to {@code null}.</p>
     * @param a the array into which the elements of this collection are to be
     *          stored, if it is big enough; otherwise, a new array of the
     *          same runtime type is allocated for this purpose.
     * @return an array containing all of the elements in this collection
     * @param <U> the component type of the array to contain the collection
     * @throws NullPointerException if the specified array is null
     */
    @Override
    public <U> U[] toArray(U[] a) {
        if (a.length > 0)
            a[0] = null;
        return a;
    }

    /**
     * <p>Returns an array containing all of the elements in this collection.</p>
     * @implSpec <p>Extending classes should override this method to return an
     * empty array of the appropriate type.</p>
     * @return an array containing all of the elements in this collection
     */
    @Override
    public abstract T_ARR toPrimitiveArray();
}
