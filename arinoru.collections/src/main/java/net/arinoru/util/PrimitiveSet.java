package net.arinoru.util;

import net.arinoru.annotation.Unstable;

import java.util.Collection;
import java.util.PrimitiveIterator;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.stream.BaseStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * <p>A base type for primitive specializations of {@code Set}. Specialized
 * subtypes are provided for {@code int}, {@code long} and {@code double}
 * values.</p>
 * <p>The specialized subtype default implementations for various
 * {@code Set} methods rely on boxing and unboxing primitive values to and
 * from instances of their corresponding wrapper class. Such boxing may offset any
 * advantages gained when using the primitive specializations. To avoid boxing and
 * unboxing, the corresponding primitive-based methods should be used. For example,
 * {@link IntSet#containsInt(int)} and {@link IntSet#removeInt(int)} should be used
 * in favor of {@link IntSet#contains(Object)} and {@link IntSet#remove(Object)}.</p>
 * @param <T> the boxed type of elements contained within this
 *           {@code PrimitiveSet}. The type must be a wrapper type for a
 *           primitive type, such as {@code Integer} for the primitive {@code int}
 *           type.
 * @param <T_ARR> the type of arrays who have the set's primitive element
 *              type as their runtime component type.
 * @param <T_CONS> the type of primitive consumer. The type must be a primitive
 *                specialization of {@link Consumer} for {@code T}, such as
 *                {@link IntConsumer} for {@code Integer}.
 * @param <T_PRED> the type of primitive predicate. The type must be a primitive
 *                specialization of {@link Predicate} for {@code T}, such as
 *                {@link IntPredicate} for {@code Integer}.
 * @param <T_ITER> the type of primitive iterator. The type must be a primitive
 *                specialization of {@link java.util.Iterator} for {@code T}, such
 *                as {@link PrimitiveIterator.OfInt} for {@code Integer}.
 * @param <T_SPLITR> the type of primitive spliterator. The type must be a
 *                  primitive specialization of {@link Spliterator} for {@code T},
 *                  such as {@link Spliterator.OfInt} for {@code Integer}.
 * @param <T_STR> the type of primitive stream. The type must be a primitive
 *               specialization of {@link BaseStream} for {@code T}, such as
 *               {@link IntStream} for {@code Integer}.
 * @param <T_COLL> the type of the collection. This should be the specific
 *                specialization collection type for {@code T}, such as
 *                {@link IntCollection} for {@code Integer}. More specific collection
 *                types like sets or lists will use the underlying collection
 *                interface type here for the sake of supporting generic
 *                operations on collections, such as creating a set copy of a
 *                collection, or confirming set membership of elements in a
 *                collection.
 * @see Set
 * @see PrimitiveCollection
 */
@Unstable
public interface PrimitiveSet<T,T_ARR,T_CONS,T_PRED,
        T_ITER extends PrimitiveIterator<T,T_CONS>,
        T_SPLITR extends Spliterator.OfPrimitive<T,T_CONS,T_SPLITR>,
        T_STR extends BaseStream<T,T_STR>,
        T_COLL extends PrimitiveCollection<T,T_ARR,T_CONS,T_PRED,T_ITER,T_SPLITR,T_STR,T_COLL>>
        extends PrimitiveCollection<T,T_ARR,T_CONS,T_PRED,T_ITER,T_SPLITR,T_STR,T_COLL>, Set<T> {
    /**
     * <p>Adds the specified element to this set if it is not already present (optional
     * operation). More formally, adds the specified element {@code e} to this set if
     * the set contains no element {@code e2} such that {@code Objects.equals(e, e2)}.
     * If this set already contains the element, the call leaves the set unchanged and
     * returns {@code false}. In combination with the restriction on constructors,
     * this ensures that sets never contain duplicate elements.</p>
     * <p>The stipulation above does not imply that sets must accept all elements; sets
     * may refuse to add any particular element and throw an exception, as described in
     * the specification for {@code PrimitiveCollection.add}. Individual set
     * implementations should clearly document any restrictions on the elements that
     * they may contain.</p>
     * @param element element to be added to this set
     * @return {@code true} if this set did not already contain the specified element
     * @throws UnsupportedOperationException if the {@code add} operation is not
     * supported by this set
     * @throws ClassCastException if the class of the specified element prevents it
     * from being added to this set
     * @throws NullPointerException if the specified element is null
     * @throws IllegalArgumentException if some property of the specified element
     * prevents it from being added to this set
     */
    boolean add(T element);

    /**
     * <p>Adds all of the elements in the specified collection to this set if they're
     * not already present (optional operation). If the specified collection is also
     * a set, the {@code addAll} operation effectively modifies this set so that its
     * value is the <em>union</em> of the two sets. The behavior of this operation
     * is undefined if the specified collection is modified while the operation is in
     * progress.</p>
     * @param collection collection containing elements to be added to this set
     * @return {@code true} if this set changed as a result of the call
     * @throws UnsupportedOperationException if the {@code addAll} operation is not
     * supported by this set
     * @throws ClassCastException if the class of an element of the specified
     * collection prevents it from being added to this set
     * @throws NullPointerException if the specified collection contains one or more
     * null elements, or if the specified collection is null
     * @throws IllegalArgumentException if some property of an element of the
     * specified collection prevents it from being added to this set
     * @see #add(Object) add(T)
     * @see #addAll(PrimitiveCollection) addAll(T_COLL)
     */
    boolean addAll(Collection<? extends T> collection);

    /**
     * <p>Adds all of the elements in the specified collection to this set if they're
     * not already present (optional operation). If the specified collection is also
     * a set, the {@code addAll} operation effectively modifies this set so that its
     * value is the <em>union</em> of the two sets. The behavior of this operation
     * is undefined if the specified collection is modified while the operation is in
     * progress.</p>
     * @param collection collection containing elements to be added to this set
     * @return {@code true} if this set changed as a result of the call
     * @throws UnsupportedOperationException if the {@code addAll} operation is not
     * supported by this set
     * @throws NullPointerException if the specified collection is null
     * @throws IllegalArgumentException if some property of an element of the
     * specified collection prevents it from being added to this set
     * @see #addAll(Collection)
     */
    boolean addAll(T_COLL collection);

    /**
     * <p>Removes all of the elements from this set (optional operation). The set
     * will be empty after this call returns.</p>
     * @throws UnsupportedOperationException if the {@code clear} method is not
     * supported by this set
     */
    void clear();

    /**
     * <p>Returns {@code true} if this set contains the specified element. More
     * formally, returns {@code true} if and only if this set contains an element
     * {@code e} such that {@code Objects.equals(o, e)}.</p>
     * @param o element whose presence in this set is to be tested
     * @return {@code true} if this set contains the specified element
     * @throws ClassCastException if the type of the specified element is
     * incompatible with this set (optional)
     * @throws NullPointerException if the specified element is null (optional)
     */
    boolean contains(Object o);

    /**
     * <p>Returns {@code true} if this set contains all of the elements of the
     * specified collection. If the specified collection is also a set, this
     * method returns {@code true} if it is a <em>subset</em> of this set.</p>
     * @param collection collection to be checked for containment in this set
     * @return {@code true} if this set contains all of the elements of the
     * specified collection
     * @throws ClassCastException if the types of one or more elements in the
     * specified collection are incompatible with this set (optional)
     * @throws NullPointerException if the specified collection contains one or
     * more null elements (optional), or if the specified collection is null
     * @see #contains(Object)
     * @see #containsAll(PrimitiveCollection)
     */
    boolean containsAll(Collection<?> collection);

    /**
     * <p>Returns {@code true} if this set contains all of the elements of the
     * specified collection. If the specified collection is also a set, this
     * method returns {@code true} if it is a <em>subset</em> of this set.</p>
     * @param collection collection to be checked for containment in this set
     * @return {@code true} if this set contains all of the elements of the
     * specified collection
     * @throws NullPointerException if the specified collection is null
     * @see #containsAll(Collection)
     */
    boolean containsAll(T_COLL collection);

    /**
     * <p>Returns an unmodifiable set containing the elements of the given
     * primitive collection. The given collection must not be null. If the given
     * collection is subsequently modified, the returned set will not reflect such
     * modifications.</p>
     * @implNote <p>If the given collection is an unmodifiable primitive set,
     * calling copyOf will generally not create a copy.</p>
     * @param collection a primitive collection from which elements are drawn, must
     *                  be non-null
     * @return a primitive set containing the elements of the given collection
     * @throws NullPointerException if coll is null
     * @throws ClassCastException if coll is not a known specialization type of
     * PrimitiveCollection
     * @param <T> boxed type representing the collection's element type
     * @param <T_COLL> primitive collection type
     * @param <T_SET> primitive set type
     */
    @SuppressWarnings("unchecked")
    static <T,T_COLL extends PrimitiveCollection<T,?,?,?,?,?,?,T_COLL>,
            T_SET extends PrimitiveSet<T,?,?,?,?,?,?,T_COLL>> T_SET copyOf(
                    T_COLL collection) {
        return (T_SET) switch (collection) {
            case DoubleCollection doubles -> DoubleSet.copyOf(doubles);
            case IntCollection integers -> IntSet.copyOf(integers);
            case LongCollection longs -> LongSet.copyOf(longs);
            default -> throw new ClassCastException("Unknown primitive collection type");
        };
    }

    /**
     * <p>Compares the specified object with this set for equality. Returns
     * {@code true} if the specified object is also a set, the two sets have the
     * same size, and every member of the specified set is contained in this set
     * (or equivalently, every member of this set is contained in the specified
     * set). This definition ensures that the equals method works properly
     * across different implementations of the set interface.</p>
     * @param o object to be compared for equality with this set
     * @return {@code true} if the specified object is equal to this set
     * @see Object#equals(Object)
     * @see Set#equals(Object)
     */
    boolean equals(Object o);

    /**
     * <p>Performs the given action for each element of the set until all elements
     * have been processed or the action throws an exception. Actions are performed
     * in the order of iteration, if that order is specified. Exceptions thrown by
     * the action are relayed to the caller.</p>
     * <p>The behavior of this method is unspecified if the action performs
     * side-effects that modify the underlying source of elements, unless an
     * overriding class has specified a concurrent modification policy.</p>
     * @implSpec <p>The default implementation is inherited from
     * {@code PrimitiveCollection}.</p>
     * @param action action to perform on each element of the set
     * @throws NullPointerException if the specified action is null
     * @see #forEach(Object) forEach(T_CONS)
     * @see PrimitiveCollection#forEach(Consumer)
     */
    default void forEach(Consumer<? super T> action) {
        PrimitiveCollection.super.forEach(action);
    }

    /**
     * <p>Performs the given action for each element of the set until all elements
     * have been processed or the action throws an exception. Actions are performed
     * in the order of iteration, if that order is specified. Exceptions thrown by
     * the action are relayed to the caller.</p>
     * <p>The behavior of this method is unspecified if the action performs
     * side-effects that modify the underlying source of elements, unless an
     * overriding class has specified a concurrent modification policy.</p>
     * @implSpec <p>The default implementation is inherited from
     * {@code PrimitiveCollection}.</p>
     * @param action action to perform on each element of the set
     * @throws NullPointerException if the specified action is null
     * @see #forEach(Consumer)
     * @see PrimitiveCollection#forEach(Object) PrimitiveCollection.forEach(T_CONS)
     */
    default void forEach(T_CONS action) {
        PrimitiveCollection.super.forEach(action);
    }

    /**
     * <p>Returns the hash code value for this set. The hash code of a set is
     * defined to be the sum of the hash codes of the elements in the set. This
     * ensures that {@code s1.equals(s2)} implies that
     * {@code s1.hashCode()==s2.hashCode()} for any two sets {@code s1} and
     * {@code s2}, as required by the general contract of
     * {@link Object#equals(Object)}.</p>
     * @return the hash code value for this set
     * @see Object#equals(Object)
     * @see #equals(Object)
     * @see Set#hashCode()
     */
    int hashCode();

    /**
     * <p>Returns {@code true} if this set contains no elements.</p>
     * @implSpec <p>The default implementation is inherited from
     * {@code PrimitiveCollection}.</p>
     * @return {@code true} if this set contains no elements
     */
    default boolean isEmpty() {
        return PrimitiveCollection.super.isEmpty();
    }

    /**
     * <p>Returns a primitive iterator over the elements in this set. The elements are
     * returned in no particular order (unless this set is an instance of some
     * class that provides a guarantee).</p>
     * @return a @{code PrimitiveIterator} over the elements in this set
     */
    T_ITER iterator();

    /**
     * <p>Returns a possibly parallel primitive stream with this set as its source.
     * It is allowable for this method to return a sequential primitive stream.</p>
     * <p>This method should be overridden when the {@link #spliterator()}
     * method cannot return a spliterator that is {@code IMMUTABLE},
     * {@code CONCURRENT}, or <em>late-binding</em>. (See
     * {@link #spliterator()} for details.)</p>
     * @return a possibly parallel primitive stream over the elements in this set
     */
    T_STR parallelPrimitiveStream();

    /**
     * <p>Returns a possibly parallel {@code Stream} with this set as its source.
     * It is allowable for this method to return a sequential stream.</p>
     * <p>This method should be overridden when the {@link #spliterator()} method
     * cannot returns a spliterator that is {@code IMMUTABLE}, {@code CONCURRENT},
     * or <em>late-binding</em>. (See {@link #spliterator()} for details.)</p>
     * @implSpec <p>The default implementation is inherited from
     * {@code PrimitiveCollection}.</p>
     * @return a possibly parallel {@code Stream} over the elements in this set
     */
    default Stream<T> parallelStream() {
        return PrimitiveCollection.super.parallelStream();
    }

    /**
     * <p>Returns a sequential primitive stream with this set as its source.</p>
     * <p>This method should be overridden when the {@link #spliterator()} method
     * cannot return a spliterator that is {@code IMMUTABLE}, {@code CONCURRENT},
     * or <em>late-binding</em>. (See {@link #spliterator()} for detains.)</p>
     * @return a sequential primitive stream over the elements in this set
     */
    T_STR primitiveStream();

    /**
     * <p>Removes the specified element from this set if it is present (optional
     * operation). More formally, removes an element {@code e} such that
     * {@code Objects.equals(o, e)}, if this set contains such an eleemnt.
     * Returns {@code true} if this set contained the element (or equivalently,
     * if this set changed as a result of the call). (This set will not
     * contain the element once the call returns.)</p>
     * @param o element to be removed from this set, if present
     * @return {@code true} if this set contained the specified element
     * @throws ClassCastException if the type of the specified element is
     * incompatible with this set (optional)
     * @throws NullPointerException if the specified element is null (optional)
     * @throws UnsupportedOperationException if the {@code remove} operation is not
     * supported by this set
     */
    boolean remove(Object o);

    /**
     * <p>Removes from this set all of its elements that are contained in the
     * specified collection (optional operation). If the specified collection is
     * also a set, this operation effectively modifies this set so that its
     * value is the <em>asymmetric set difference</em> of the two sets.</p>
     * @param collection collection containing elements to be removed from this set
     * @return {@code true} if this set changed as a result of the call
     * @throws UnsupportedOperationException if the {@code removeAll} operation
     * is not supported by this set
     * @throws ClassCastException if the class of an element of this set is
     * incompatible with the specified collection (optional)
     * @throws NullPointerException if the specified collection is null
     * @see #remove(Object)
     * @see #contains(Object)
     * @see #removeAll(PrimitiveCollection) removeAll(T_COLL)
     */
    boolean removeAll(Collection<?> collection);

    /**
     * <p>Removes from this set all of its elements that are contained in the
     * specified collection (optional operation). If the specified collection is
     * also a set, this operation effectively modifies this set so that its
     * value is the <em>asymmetric set difference</em> of the two sets.</p>
     * @param collection collection containing elements to be removed from this set
     * @return {@code true} if this set changed as a result of the call
     * @throws UnsupportedOperationException if the {@code removeAll} operation
     * is not supported by this set
     * @throws NullPointerException if the specified collection is null
     * @see #removeAll(Collection)
     */
    boolean removeAll(T_COLL collection);

    /**
     * <p>Retains only the elements in this set that are contained in the
     * specified collection (optional operation). In other words, removes from this
     * set all of its elements that are not contained in the specified collection.
     * If the specified collection is also a set, this operation effectively
     * modifies this set so that it's value is the <em>intersection</em> of the two
     * sets.</p>
     * @param collection collection containing elements to be retained in this set
     * @return {@code true} if this set changed as a result of the call
     * @throws UnsupportedOperationException if the {@code retainAll} operation is
     * not supported by this set
     * @throws ClassCastException if the class of an element of this set is
     * incompatible with the specified collection (optional)
     * @throws NullPointerException if the specified collection is null
     * @see #remove(Object)
     * @see #retainAll(PrimitiveCollection) retainAll(T_COLL)
     */
    boolean retainAll(Collection<?> collection);

    /**
     * <p>Retains only the elements in this set that are contained in the
     * specified collection (optional operation). In other words, removes from this
     * set all of its elements that are not contained in the specified collection.
     * If the specified collection is also a set, this operation effectively modifies
     * this set s that its value is the <em>intersection</em> of the two sets.</p>
     * @param collection collection containing elements to be retained in this set
     * @return {@code true} if this set changed as a result of the call
     * @throws UnsupportedOperationException if the {@code retainAll} operation is
     * not supported by this collection
     * @throws NullPointerException if the specified collection is null
     * @see #retainAll(Collection)
     */
    boolean retainAll(T_COLL collection);

    /**
     * <p>Returns the number of elements in this set (its cardinality). If this set
     * contains more than {@code Integer.MAX_VALUE} elements, returns
     * {@code Integer.MAX_VALUE}.</p>
     * @return the number of elements in this set (its cardinality)
     */
    int size();

    /**
     * <p>Creates a spliterator over the elements in this set.</p>
     * <p>The spliterator reports {@link Spliterator#DISTINCT}.
     * Implementations should document the reporting of additional
     * characteristic values.</p>
     * @return a spliterator over the elements in this set
     */
    T_SPLITR spliterator();

    /**
     * <p>Returns a sequential {@code Stream} with this set as its source.</p>
     * <p>This method should be overridden when the {@link #spliterator()} method
     * cannot return a spliterator that is {@code IMMUTABLE}, {@code CONCURRENT},
     * or <em>late-binding</em>. (See {@link #spliterator()}} for details.)</p>
     * @implSpec <p>The default implementation is inherited from
     * {@code PrimitiveCollection}.</p>
     * @return a sequential {@code Stream} over the elements in this set
     */
    default Stream<T> stream() {
        return PrimitiveCollection.super.stream();
    }

    /**
     * <p>Returns an array containing all of the elements in this set. If this set
     * makes any guarantees as to what order its elements are returned by its
     * iterator, this method must return the elements in the same order. The
     * returned array's runtime component type is {@code Object}.</p>
     * <p>The returned array will be "safe" in that no references to it are
     * maintained by this set. (In other words, this method must allocate a new
     * array even if this set is backed by an array). The caller is thus free to
     * modify the returned array.</p>
     * @implSpec <p>The default method implementation is inherited from
     * {@code PrimitiveCollection}.</p>
     * @return an array containing all the elements in this set
     */
    default Object[] toArray() {
        return PrimitiveCollection.super.toArray();
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
     * <p>If this set makes any guarantees as to what order its elements
     * are returned by its iterator, this method must return the elements in the
     * same order.</p>
     * @implSpec <p>The default method implementation is inherited from
     * {@code PrimitiveCollection}.</p>
     * @param a the array into which the elements of this set are to be
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
    default <U> U[] toArray(U[] a) {
        return PrimitiveCollection.super.toArray(a);
    }

    /**
     * <p>Returns an array containing all of the elements in this set. If this set
     * makes any guarantees as to what order it elements are returned by its
     * iterator, this method must return the elements in the same order.</p>
     * <p>The returned array will be "safe" in that no references to it are
     * maintained by this set. (In other words, this method must allocate a new
     * array even if this set is backed by an array). The caller is thus free
     * to modify the returned array.</p>
     * @return an array containing all of the elements in this set
     */
    T_ARR toPrimitiveArray();
}
