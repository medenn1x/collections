package net.arinoru.collections;

import net.arinoru.prerelease.PrereleaseContent;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

/**
 * <p>A base type for primitive specializations of {@code Set}. Specialized
 * subtypes are provided for {@code int}, {@code long} and {@code double}
 * values.</p>
 * <p>The specialized subtype default implementations for various
 * {@code Set} methods rely on boxing and unboxing primitive values to and
 * from instances of their corresponding wrapper class. Such boxing may offset any
 * advantages gained when using the primitive specializations. To avoid boxing and
 * unboxing, the corresponding primitive-based methods should be used. For example,
 * {@link OfInt#containsInt(int)} and {@link OfInt#removeInt(int)} should be used
 * in favor of {@link OfInt#contains(Object)} and {@link OfInt#remove(Object)}.</p>
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
 * @param <T_SPLITR> the type of primitive spliterator. The type must be a
 *                  primitive specialization of {@link Spliterator} for {@code T},
 *                  such as {@link Spliterator.OfInt} for {@code Integer}.
 * @param <T_STR> the type of primitive stream. The type must be a primitive
 *               specialization of {@link BaseStream} for {@code T}, such as
 *               {@link IntStream} for {@code Integer}.
 * @param <T_COLL> the type of the collection. This should be the specific
 *                specialization collection type for {@code T}, such as
 *                {@link PrimitiveCollection.OfInt} for {@code Integer}. More specific collection
 *                types like sets or lists will use the underlying collection
 *                interface type here for the sake of supporting generic
 *                operations on collections, such as creating a set copy of a
 *                collection, or confirming set membership of elements in a
 *                collection.
 * @see Set
 * @see PrimitiveCollection
 */
@PrereleaseContent
public interface PrimitiveSet<T,T_ARR,T_CONS,T_PRED,
        T_SPLITR extends Spliterator.OfPrimitive<T,T_CONS,T_SPLITR>,
        T_STR extends BaseStream<T,T_STR>,
        T_COLL extends PrimitiveCollection<T,T_ARR,T_CONS,T_PRED,T_SPLITR,T_STR,T_COLL>>
        extends PrimitiveCollection<T,T_ARR,T_CONS,T_PRED,T_SPLITR,T_STR,T_COLL>, Set<T> {
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
     * @param t element to be added to this set
     * @return {@code true} if this set did not already contain the specified element
     * @throws UnsupportedOperationException if the {@code add} operation is not
     * supported by this set
     * @throws ClassCastException if the class of the specified element prevents it
     * from being added to this set
     * @throws NullPointerException if the specified element is null
     * @throws IllegalArgumentException if some property of the specified element
     * prevents it from being added to this set
     */
    boolean add(T t);

    /**
     * <p>Adds all of the elements in the specified collection to this set if they're
     * not already present (optional operation). If the specified collection is also
     * a set, the {@code addAll} operation effectively modifies this set so that its
     * value is the <em>union</em> of the two sets. The behavior of this operation
     * is undefined if the specified collection is modified while the operation is in
     * progress.</p>
     * @param c collection containing elements to be added to this set
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
    boolean addAll(Collection<? extends T> c);

    /**
     * <p>Adds all of the elements in the specified collection to this set if they're
     * not already present (optional operation). If the specified collection is also
     * a set, the {@code addAll} operation effectively modifies this set so that its
     * value is the <em>union</em> of the two sets. The behavior of this operation
     * is undefined if the specified collection is modified while the operation is in
     * progress.</p>
     * @param c collection containing elements to be added to this set
     * @return {@code true} if this set changed as a result of the call
     * @throws UnsupportedOperationException if the {@code addAll} operation is not
     * supported by this set
     * @throws NullPointerException if the specified collection is null
     * @throws IllegalArgumentException if some property of an element of the
     * specified collection prevents it from being added to this set
     * @see #addAll(Collection)
     */
    boolean addAll(T_COLL c);

    /**
     * <p>Removes all of the elements from this set (optional operation). The set
     * will be empty after this call returns.</p>
     * @throws UnsupportedOperationException if the {@code clear} method is not
     * supported by this set
     */
    default void clear() {
        PrimitiveCollection.super.clear();
    }

    /**
     * <p>Returns {@code true} if this set contains the specified element. More
     * formally, returns {@code true} if and only if this set contains an element
     * {@code e} such that {@code Objects.equals(o, e)}.</p>
     * @param o element whose presence in this collection is to be tested
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
     * @param c collection to be checked for containment in this set
     * @return {@code true} if this set contains all of the elements of the
     * specified collection
     * @throws ClassCastException if the types of one or more elements in the
     * specified collection are incompatible with this set (optional)
     * @throws NullPointerException if the specified collection contains one or
     * more null elements (optional), or if the specified collection is null
     * @see #contains(Object)
     * @see #containsAll(PrimitiveCollection)
     */
    boolean containsAll(Collection<?> c);

    /**
     * <p>Returns {@code true} if this set contains all of the elements of the
     * specified collection. If the specified collection is also a set, this
     * method returns {@code true} if it is a <em>subset</em> of this set.</p>
     * @param c collection to be checked for containment in this set
     * @return {@code true} if this set contains all of the elements of the
     * specified collection
     * @throws NullPointerException if the specified collection is null
     * @see #containsAll(Collection)
     */
    boolean containsAll(T_COLL c);

    /**
     * <p>Returns an unmodifiable set containing the elements of the given
     * primitive collection. The given collection must not be null. If the given
     * collection is subsequently modified, the returned set will not reflect such
     * modifications.</p>
     * @implNote <p>If the given collection is an unmodifiable primitive set,
     * calling copyOf will generally not create a copy.</p>
     * @param coll a primitive collection from which elements are drawn, must be
     *             non-null
     * @return a primitive set containing the elements of the given collection
     * @throws NullPointerException if coll is null
     * @param <T> boxed type representing the collection's element type
     * @param <T_COLL> primitive collection type
     * @param <T_SET> primitive set type
     */
    @SuppressWarnings("unchecked")
    static <T,T_COLL extends PrimitiveCollection<T,?,?,?,?,?,T_COLL>,
            T_SET extends PrimitiveSet<T,?,?,?,?,?,T_COLL>> T_SET copyOf(
                    T_COLL coll) {
        return (T_SET) switch (coll) {
            case PrimitiveCollection.OfDouble c -> OfDouble.copyOf(c);
            case PrimitiveCollection.OfInt c -> OfInt.copyOf(c);
            case PrimitiveCollection.OfLong c -> OfLong.copyOf(c);
            default -> throw new ClassCastException("Internal error");
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
    PrimitiveIterator<T,T_CONS> iterator();

    /**
     * <p>Returns a possibly parallel primitive stream with this set as its source.
     * It is allowable for this method to return a sequential primitive stream.</p>
     * <p>This method should be overridden when the {@link #spliterator()}
     * method cannot return a spliterator that is {@code IMMUTABLE},
     * {@code CONCURRENT}, or <em>late-binding</em>. (See
     * {@link #spliterator()} for details.)</p>
     * @return a possibly parallel primitive stream over the elements
     * in this collection
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
     * @param c collection containing elements to be removed from this set
     * @return {@code true} if this set changed as a result of the call
     * @throws UnsupportedOperationException if the {@code removeAll} operation
     * is not supported by this set
     * @throws ClassCastException if the class of an element of this set is
     * incompatible with the specified collection (optional)
     * @throws NullPointerException if this set contains a null element (optional),
     * or the specified collection is null
     * @see #remove(Object)
     * @see #contains(Object)
     * @see #removeAll(PrimitiveCollection) removeAll(T_COLL)
     */
    boolean removeAll(Collection<?> c);

    /**
     * <p>Removes from this set all of its elements that are contained in the
     * specified collection (optional operation). If the specified collection is
     * also a set, this operation effectively modifies this set so that its
     * value is the <em>asymmetric set difference</em> of the two sets.</p>
     * @param c collection containing elements to be removed from this set
     * @return {@code true} if this set changed as a result of the call
     * @throws UnsupportedOperationException if the {@code removeAll} operation
     * is not supported by this set
     * @throws NullPointerException if the specified collection is null
     * @see #removeAll(Collection)
     */
    boolean removeAll(T_COLL c);

    /**
     * <p>Retains only the elements in this set that are contained in the
     * specified collection (optional operation). In other words, removes from this
     * set all of its elements that are not contained in the specified collection.
     * If the specified collection is also a set, this operation effectively
     * modifies this set so that it's value is the <em>intersection</em> of the two
     * sets.</p>
     * @param c collection containing elements to be retained in this set
     * @return {@code true} if this set changed as a result of the call
     * @throws UnsupportedOperationException if the {@code retainAll} operation is
     * not supported by this set
     * @throws ClassCastException if the class of an element of this set is
     * incompatible with the specified collection (optional)
     * @throws NullPointerException if this set contains a null element (optional),
     * or if the specified collection is null
     * @see #remove(Object)
     * @see #retainAll(PrimitiveCollection) retainAll(T_COLL)
     */
    boolean retainAll(Collection<?> c);

    /**
     * <p>Retains only the elements in this set that are contained in the
     * specified collection (optional operation). In other words, removes from this
     * set all of its elements that are not contained in the specified collection.
     * If the specified collection is also a set, this operation effectively modifies
     * this set s that its value is the <em>intersection</em> of the two sets.</p>
     * @param c collection containing elements to be retained in this set
     * @return {@code true} if this set changed as a result of the call
     * @throws UnsupportedOperationException if the {@code retainAll} operation is
     * not supported by this collection
     * @throws NullPointerException if the specified collection is null
     * @see #retainAll(Collection)
     */
    boolean retainAll(T_COLL c);

    /**
     * <p>Returns the number of elements in this set (its cardinality). If this set
     * contains more than {@code Integer.MAX_VALUE} elements, returns
     * {@code Integer.MAX_VALUE}.</p>
     * @return the number of elements in this set (its cardinality)
     */
    int size();

    /**
     * <p>Creates a {@code Spliterator.OfPrimitive} over the elements in this
     * set.</p>
     * <p>The {@code Spliterator.OfPrimitive} reports {@link Spliterator#DISTINCT}.
     * Implementations should document the reporting of additional
     * characteristic values.</p>
     * @return a {@code Spliterator.OfPrimitive} over the elements in this set
     */
    Spliterator.OfPrimitive<T,T_CONS,T_SPLITR> spliterator();

    /**
     * <p>Returns a sequential {@code Stream} with this set as its source.</p>
     * <p>This method should be overridden when the {@link #spliterator()} method
     * cannot return a spliterator that is {@code IMMUTABLE}, {@code CONCURRENT},
     * or <em>late-binding</em>. (See {@link #spliterator()}} for details.)</p>
     * @implSpec <p>The default implementation is inherited from
     * {@code PrimitiveCollection}.</p>
     * @return a sequential {@code Stream} over the element in this set
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
     * <p>If this collection makes any guarantees as to what order its elements
     * are returned by its iterator, this method must return the elements in the
     * same order.</p>
     * @implSpec <p>The default method implementation is inherited from
     * {@code PrimitiveCollection}.</p>
     * @param a the array into which the elements of this collection are to be
     *          stored, if it is big enough; otherwise, a new array of the
     *          same runtime type is allocated for this purpose.
     * @return an array containing all of the elements in this collection
     * @param <U> the component type of the array to contain the collection
     * @throws ArrayStoreException if the runtime type of any element in this
     * collection is not assignable to the runtime component type of the
     * specified array
     * @throws NullPointerException if the specified array is null
     * @throws OutOfMemoryError if it is not possible to allocate a sufficiently
     * large array to contain this collection (i.e. if the size of the collection
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

    /**
     * <p>A Set specialized for {@code double} values.</p>
     */
    @PrereleaseContent
    interface OfDouble extends PrimitiveSet<Double,double[],DoubleConsumer,
            DoublePredicate,Spliterator.OfDouble,DoubleStream,
            PrimitiveCollection.OfDouble>, PrimitiveCollection.OfDouble {
        /**
         * <p>Adds the specified element to this set if it is not already present
         * (optional operation). More formally, adds the specified element {@code t}
         * to this set if the set contains no element {@code e} such that
         * {@code Objects.equals(t, e)}. If this set already contains the element,
         * the call leaves the set unchanged and returns {@code false}. In
         * combination with the restriction on constructors, this ensures that
         * sets never contains duplicate elements.</p>
         * <p>The stipulation above does not apply that sets must accept all elements;
         * sets may refuse to add any particular element, and throw an exception, as
         * described in the specification for
         * {@link Collection#add(Object) Collection.add}. Individual set
         * implementations should clearly document any restrictions on the elements
         * that they may contain.</p>
         * <p>Because this method takes boxed values, reliance on it may eliminate any
         * performance advantage obtained by using a primitive collection; where
         * possible, users may wish to utilize the {@link #addDouble(double)} method
         * instead,</p>
         * @implSpec <p>The default implementation is inherited from
         * {@code PrimitiveCollection.OfDouble}.</p>
         * @param t element to be added to this set
         * @return {@code true} if this set did not already contain the specified
         * element
         * @throws UnsupportedOperationException if the {@code add} operation is not
         * supported by this set
         * @throws NullPointerException if the specified element is null
         * @throws IllegalArgumentException if some property of the specified element
         * prevents it from being added to this set
         * @see #addDouble(double)
         * @see PrimitiveCollection.OfDouble#add(Double)
         */
        default boolean add(Double t) {
            return PrimitiveCollection.OfDouble.super.add(t);
        }

        /**
         * <p>Adds all of the elements in the specified collection to this set if
         * they're not already present (optional operation). If the specified
         * collection is also a set, the {@code addAll} operation effectively
         * modifies this set so that its value is the <em>union</em> of the two
         * sets. The behavior of this operation is undefined if the specified
         * collection is modified while the operation is in progress.</p>
         * @implSpec <p>The default implementation is inherited from
         * {@code PrimitiveCollection.OfDouble}.</p>
         * @param c collection containing elements to be added to this set
         * @return {@code true} if this set changed as a result of the call
         * @throws UnsupportedOperationException if the {@code addAll} operation is
         * not supported by this set.
         * @throws ClassCastException if the class of an element of the specified
         * collection prevents it from being added to this set
         * @throws NullPointerException if the specified collection contains one or
         * more null elements, or if the specified collection is null
         * @throws IllegalArgumentException if some property of an element of the
         * specified collection prevents it from being added to this set
         * @see #addAll(PrimitiveCollection.OfDouble)
         * @see PrimitiveCollection.OfDouble#addAll(Collection)
         * @see #add(Double)
         */
        default boolean addAll(Collection<? extends Double> c) {
            return PrimitiveCollection.OfDouble.super.addAll(c);
        }

        /**
         * <p>Adds all of the elements in the specified collection to this set if
         * they're not already present (optional operation). If the specified
         * collection is also a set, the {@code addAll} operation effectively
         * modifies this set so that its value is the <em>union</em> of the two
         * sets. The behavior of this operation is undefined if the specified
         * collection is modified while the operation is in progress.</p>
         * @implSpec <p>The default implementation is inherited from
         * {@code PrimitiveCollection.OfDouble}.</p>
         * @param c collection containing elements to be added to this set
         * @return {@code true} if this set changed as a result of the call
         * @throws UnsupportedOperationException if the {@code addAll} operation is
         * not supported by this set.
         * @throws IllegalArgumentException if some property of an element of the
         * specified collection prevents it from being added to this set
         * @see #addAll(Collection)
         * @see PrimitiveCollection.OfDouble#addAll(PrimitiveCollection.OfDouble)
         * @see #addDouble(double)
         */
        default boolean addAll(PrimitiveCollection.OfDouble c) {
            return PrimitiveCollection.OfDouble.super.addAll(c);
        }

        /**
         * <p>Adds the specified element to this set if it is not already present
         * (optional operation). More formally, adds the specified element {@code d}
         * to this set if the set contains no element {@code e} such that
         * {@code d == e}. If this set already contains the element, the call leaves
         * the set unchanged and returns {@code false}. In combination with the
         * restriction on constructors, this ensures that sets never contains
         * duplicate elements.</p>
         * <p>The stipulation above does not apply that sets must accept all elements;
         * sets may refuse to add any particular element, and throw an exception, as
         * described in the specification for
         * {@link Collection#add(Object) Collection.add}. Individual set
         * implementations should clearly document any restrictions on the elements
         * that they may contain.</p>
         * @implSpec <p>The default implementation is inherited from
         * {@code PrimitiveCollection.OfDouble}.</p>
         * @param d element to be added to this set
         * @return {@code true} if this set did not already contain the specified
         * element
         * @throws UnsupportedOperationException if the {@code add} operation is not
         * supported by this set
         * @throws IllegalArgumentException if some property of the specified element
         * prevents it from being added to this set
         * @see #add(Double)
         * @see PrimitiveCollection.OfDouble#addDouble(double)
         */
        default boolean addDouble(double d) {
            return PrimitiveCollection.OfDouble.super.addDouble(d);
        }

        /**
         * <p>Returns {@code true} if this set contains the specified element.
         * More formally, returns {@code true} if and only if this set contains an
         * element {@code e} such that {@code Objects.equals(o, e)}.</p>
         * <p>Because this method takes boxed values, reliance on it may eliminate any
         * performance advantage obtained by using a primitive collection; where
         * possible, users may wish to utilize the {@link #containsDouble(double)}
         * method instead,</p>
         * @implSpec <p>The default implementation is inherited from
         * {@code PrimitiveCollection.OfDouble}.</p>
         * @param o element whose presence in this set is to be tested
         * @return {@code true} if this set contains the specified element
         * @throws ClassCastException if the type of the specified element is
         * incompatible with this set (optional)
         * @throws NullPointerException if the specified element is null (optional)
         */
        default boolean contains(Object o) {
            return PrimitiveCollection.OfDouble.super.contains(o);
        }

        /**
         * <p>Returns {@code true} if this set contains all of the elements of the
         * specified collection. If the specified collection is also a set, this
         * method returns {@code true} if it is a <em>subset</em> of this set.</p>
         * @implSpec <p>The default implementation is inherited from
         * {@code PrimitiveCollection.OfDouble}.</p>
         * @param c collection to be checked for containment in this set
         * @return {@code true} if this set contains all of the elements of the
         * specified collection
         * @throws ClassCastException if the types of one or more elements in the
         * specified collection are incompatible with this set (optional)
         * @throws NullPointerException if the specified collection contains one or
         * more null elements (optional), or if the specified collection is null
         * @see #containsAll(PrimitiveCollection.OfDouble)
         * @see PrimitiveCollection.OfDouble#containsAll(Collection)
         * @see #contains(Object)
         */
        default boolean containsAll(Collection<?> c) {
            return PrimitiveCollection.OfDouble.super.containsAll(c);
        }

        /**
         * <p>Returns {@code true} if this set contains all of the elements of the
         * specified collection. If the specified collection is also a set, this
         * method returns {@code true} if it is a <em>subset</em> of this set.</p>
         * @implSpec <p>The default implementation is inherited from
         * {@code PrimitiveCollection.OfDouble}.</p>
         * @param c collection to be checked for containment in this set
         * @return {@code true} if this set contains all of the elements of the
         * specified collection
         * @throws NullPointerException if the specified collection is null
         * @see #containsAll(Collection)
         * @see PrimitiveCollection.OfDouble#containsAll(PrimitiveCollection.OfDouble)
         * @see #containsDouble(double)
         */
        default boolean containsAll(PrimitiveCollection.OfDouble c) {
            return PrimitiveCollection.OfDouble.super.containsAll(c);
        }

        /**
         * <p>Returns {@code true} if this set contains the specified element.
         * More formally, returns {@code true} if and only if this set contains an
         * element {@code e} such that {@code d == e}.</p>
         * @implSpec <p>The default implementation is inherited from
         * {@code PrimitiveCollection.OfDouble}.</p>
         * @param d element whose presence in this set is to be tested
         * @return {@code true} if this set contains the specified element
         * @see #contains(Object)
         * @see PrimitiveCollection.OfDouble#containsDouble(double)
         */
        default boolean containsDouble(double d) {
            return PrimitiveCollection.OfDouble.super.containsDouble(d);
        }

        /**
         * <p>Returns an unmodifiable set containing the elements of the given
         * primitive collection. The given collection must not be null. If the
         * given collection contains duplicate elements, a single instance of
         * that element will be preserved. If the given collection is
         * subsequently modified, the returned set will not reflect such
         * modifications.</p>
         * @implNote <p>If the given collection is an unmodifiable set, calling
         * copyOf will generally not create a copy.</p>
         * @param coll a primitive collection from which elements are drawn,
         *             must be non-null
         * @return a {@code PrimitiveSet.OfDouble} containing the elements of
         * the given collection
         * @throws NullPointerException if coll is null
         */
        static PrimitiveSet.OfDouble copyOf(PrimitiveCollection.OfDouble coll) {
            return PrimitiveCollections.setOf(coll);
        }

        /**
         * <p>Returns a primitive iterator over the elements in this collection.
         * The elements are returned in no particular order (unless this set is an
         * instance of some class that provides a guarantee).</p>
         * @return a {@code PrimitiveIterator.OfDouble} over the elements in this set
         */
        PrimitiveIterator.OfDouble iterator();

        /**
         * <p>Returns an unmodifiable set containing zero elements.</p>
         * @return an empty {@code PrimitiveSet.OfDouble}.
         */
        static PrimitiveSet.OfDouble of() {
            return PrimitiveCollections.emptyDoubleSet();
        }

        /**
         * <p>Returns an unmodifiable set containing one element.</p>
         * @param e the single element
         * @return a {@code PrimitiveSet.OfDouble} containing the specified element
         */
        static PrimitiveSet.OfDouble of(double e) {
            return PrimitiveCollections.singleton(e);
        }

        /**
         * <p>Returns an unmodifiable set containing an arbitrary number of
         * elements.</p>
         * @apiNote <p>This method also accepts a single array as an argument. The
         * size of the resulting set will be equal to the length of the array.</p>
         * @param elements the elements to be contained in the set
         * @return a {@code PrimitiveSet.OfDouble} containing the specified elements
         * @throws IllegalArgumentException if there are any duplicate elements
         * @throws NullPointerException if the array is null
         */
        static PrimitiveSet.OfDouble of(double... elements) {
            return PrimitiveCollections.setOf(elements);
        }

        /**
         * <p>Returns a possibly parallel {@code DoubleStream} with this set as its
         * source. It is allowable for this method to return a sequential double
         * stream.</p>
         * <p>This method should be overridden when the {@link #spliterator()}
         * method cannot return a spliterator that is {@code IMMUTABLE},
         * {@code CONCURRENT} or <em>late-binding</em>. (See
         * {@link #spliterator()} for details.)</p>
         * @implSpec <p>The default implementation creates a parallel
         * {@code DoubleStream} from the set's {@code Spliterator.OfDouble}.</p>
         * @return a possibly parallel {@code DoubleStream} over the elements in
         * this collection
         */
        default DoubleStream parallelPrimitiveStream() {
            return StreamSupport.doubleStream(spliterator(), true);
        }

        /**
         * <p>Returns a sequential {@code DoubleStream} with this set as its
         * source.</p>
         * <p>This method should be overridden when the {@link #spliterator()}
         * method cannot return a spliterator that is {@code IMMUTABLE},
         * {@code CONCURRENT}, or <em>late-binding</em>. (See
         * {@link #spliterator()} for details.)</p>
         * @implSpec <p>The default implementation creates a sequential
         * {@code DoubleStream} from the set's {@code Spliterator.OfDouble}.</p>
         * @return a sequential {@code DoubleStream} over the elements in this set
         */
        default DoubleStream primitiveStream() {
            return StreamSupport.doubleStream(spliterator(), false);
        }

        /**
         * <p>Removes the specified element from this set if it is present (optional
         * operation). Move formally, removes an element {@code e} such that
         * {@code Objects.equals(o, e)}, if this set contains such an element.
         * Returns {@code true} if this set contained the element (or equivalently,
         * if this set changed as a result of the call). (This set will not contain
         * the element once the call returns.)</p>
         * <p>As this method requires boxing the element to be removed, reliance upon
         * it may eliminate any benefit from using a primitive set. Developers should
         * prefer using {@link #removeDouble(double)} where possible.</p>
         * @implSpec <p>The default implementation is inherited from
         * {@code PrimitiveCollection.OfDouble}.</p>
         * @param o element to be removed from this set, if present
         * @return {@code true} if this set contained the specified element
         * @throws ClassCastException if the type of the specified element is
         * incompatible with this set (optional)
         * @throws NullPointerException if the specified element is null (optional)
         * @throws UnsupportedOperationException if the {@code remove} operation
         * is not supported by this set
         * @see #removeDouble(double)
         * @see PrimitiveCollection.OfDouble#remove(Object)
         */
        default boolean remove(Object o) {
            return PrimitiveCollection.OfDouble.super.remove(o);
        }

        /**
         * <p>Removes from this set all of its elements that are contained in the
         * specified collection (optional operation). If the specified collection is
         * also a set, this operation effectively modifies this set so that its
         * value is the <em>asymmetric set difference</em> of the two sets.</p>
         * @implSpec <p>The default implementation is inherited from
         * {@code PrimitiveCollection.OfDouble}.</p>
         * @param c collection containing elements to be removed from this set
         * @return {@code true} if this set changed as a result of the call
         * @throws UnsupportedOperationException if the {@code removeAll} operation
         * is not supported by this set
         * @throws ClassCastException if the class of an element of this set is
         * incompatible with the specified collection (optional)
         * @throws NullPointerException if the specified collection is null
         */
        default boolean removeAll(Collection<?> c) {
            return PrimitiveCollection.OfDouble.super.removeAll(c);
        }

        /**
         * <p>Removes from this set all of its elements that are contained in the
         * specified collection (optional operation). If the specified collection is
         * also a set, this operation effectively modifies this set so that its
         * value is the <em>asymmetric set difference</em> of the two sets.</p>
         * @implSpec <p>The default implementation is inherited from
         * {@code PrimitiveCollection.OfDouble}.</p>
         * @param c collection containing elements to be removed from this set
         * @return {@code true} if this set changed as a result of the call
         * @throws UnsupportedOperationException if the {@code removeAll} operation
         * is not supported by this set
         * @throws NullPointerException if the specified collection is null
         */
        default boolean removeAll(PrimitiveCollection.OfDouble c) {
            return PrimitiveCollection.OfDouble.super.removeAll(c);
        }

        /**
         * <p>Removes the specified element from this set if it is present (optional
         * operation). Move formally, removes an element {@code e} such that
         * {@code d == e}, if this set contains such an element.
         * Returns {@code true} if this set contained the element (or equivalently,
         * if this set changed as a result of the call). (This set will not contain
         * the element once the call returns.)</p>
         * @implSpec <p>The default implementation is inherited from
         * {@code PrimitiveCollection.OfDouble}.</p>
         * @param d element to be removed from this set, if present
         * @return {@code true} if this set contained the specified element
         * @throws UnsupportedOperationException if the {@code remove} operation
         * is not supported by this set
         * @see #remove(Object)
         * @see PrimitiveCollection.OfDouble#removeDouble(double)
         */
        default boolean removeDouble(double d) {
            return PrimitiveCollection.OfDouble.super.removeDouble(d);
        }

        /**
         * <p>Retains only the elements in this set that are contained in the
         * specified collection (optional operation). In other words, removes from
         * this set all of its elements that are not contained in the specified
         * collection. If the specified collection is also a set, this operation
         * effectively modifies this set so that its value is the
         * <em>intersection</em> of the two sets.</p>
         * @implSpec <p>The default implementation is inherited from
         * {@code PrimitiveCollection.OfDouble}.</p>
         * @param c collection containing elements to be retained in this set
         * @return {@code true} if this set changes as a result of the call
         * @throws UnsupportedOperationException if the {@code retainAll}
         * operation is not supported by this set
         * @throws ClassCastException if the class of an element of this set is
         * incompatible with the specified collection (optional)
         * @throws NullPointerException if the specified collection is null
         * @see #retainAll(PrimitiveCollection.OfDouble)
         * @see PrimitiveCollection.OfDouble#retainAll(Collection)
         * @see #remove(Object)
         */
        default boolean retainAll(Collection<?> c) {
            return PrimitiveCollection.OfDouble.super.retainAll(c);
        }

        /**
         * <p>Retains only the elements in this set that are contained in the
         * specified collection (optional operation). In other words, removes from
         * this set all of its elements that are not contained in the specified
         * collection. If the specified collection is also a set, this operation
         * effectively modifies this set so that its value is the
         * <em>intersection</em> of the two sets.</p>
         * @implSpec <p>The default implementation is inherited from
         * {@code PrimitiveCollection.OfDouble}.</p>
         * @param c collection containing elements to be retained in this set
         * @return {@code true} if this set changes as a result of the call
         * @throws UnsupportedOperationException if the {@code retainAll}
         * operation is not supported by this set
         * @throws NullPointerException if the specified collection is null
         * @see #retainAll(Collection)
         * @see PrimitiveCollection.OfDouble#retainAll(PrimitiveCollection.OfDouble)
         * @see #removeDouble(double)
         */
        default boolean retainAll(PrimitiveCollection.OfDouble c) {
            return PrimitiveCollection.OfDouble.super.retainAll(c);
        }

        /**
         * <p>Creates a {@code Spliterator.OfDouble} over the elements in this
         * set.</p>
         * <p>The {@code Spliterator.OfDouble} reports {@link Spliterator#DISTINCT}.
         * Implementations should document the reporting of additional
         * characteristic values.</p>
         * @implSpec <p>The default implementation creates a <em>late-binding</em>
         * spliterator from the set's {@code PrimitiveIterator.OfDouble}. The
         * spliterator inherits the <em>fail-fast</em> properties of the set's
         * iterator.</p>
         * <p>The created {@code Spliterator.OfDouble} additionally reports
         * {@link Spliterator#SIZED}.</p>
         * @implNote <p>The created {@code Spliterator.OfDouble} additionally reports
         * {@link Spliterator#SUBSIZED}.</p>
         * @return a {@code Spliterator.OfDouble} over the elements in this set
         */
        default Spliterator.OfDouble spliterator() {
            return PrimitiveCollections.doubleSpliterator(this, Spliterator.DISTINCT);
        }

        /**
         * <p>Returns an array containing all of the elements in this set. If this set
         * makes any guarantees as to what order its elements are returned by its
         * iterator, this method must return the elements in the same order.</p>
         * <p>The returned array will be "safe" in that no references to it are
         * maintained by this set. (In other words, this method must allocate a new
         * array even if this set is backed by an array). The called is thus free
         * to modify the returned array.</p>
         * @implSpec <p>The default implementation is inherited from
         * {@code PrimitiveCollection.OfDouble}.</p>
         * @return an array containing all the elements in this set
         */
        default double[] toPrimitiveArray() {
            return PrimitiveCollection.OfDouble.super.toPrimitiveArray();
        }

        /**
         * <p>Returns a set view over the specified set. This view will not permit
         * any operations which modify the set.</p>
         * <p>The returned view is backed by the set, so any changes to the backing
         * set will be visible in the returned set.</p>
         * @param set set to create an unmodifiable view over
         * @return an unmodifiable view over the specified set
         */
        static PrimitiveSet.OfDouble unmodifiableSet(PrimitiveSet.OfDouble set) {
            return PrimitiveCollections.unmodifiableSet(set);
        }
    }

    /**
     * <p>A Set specialized for {@code int} values.</p>
     */
    @PrereleaseContent
    interface OfInt extends PrimitiveSet<Integer,int[],IntConsumer,
            IntPredicate,Spliterator.OfInt,IntStream,
            PrimitiveCollection.OfInt>, PrimitiveCollection.OfInt {
        /**
         * <p>Adds the specified element to this set if it is not already present
         * (optional operation). More formally, adds the specified element {@code t}
         * to this set if the set contains no element {@code e} such that
         * {@code Objects.equals(t, e)}. If this set already contains the element,
         * the call leaves the set unchanged and returns {@code false}. In
         * combination with the restriction on constructors, this ensures that
         * sets never contains duplicate elements.</p>
         * <p>The stipulation above does not apply that sets must accept all elements;
         * sets may refuse to add any particular element, and throw an exception, as
         * described in the specification for
         * {@link Collection#add(Object) Collection.add}. Individual set
         * implementations should clearly document any restrictions on the elements
         * that they may contain.</p>
         * <p>Because this method takes boxed values, reliance on it may eliminate any
         * performance advantage obtained by using a primitive collection; where
         * possible, users may wish to utilize the {@link #addInt(int)} method
         * instead,</p>
         * @implSpec <p>The default implementation is inherited from
         * {@code PrimitiveCollection.OfInt}.</p>
         * @param t element to be added to this set
         * @return {@code true} if this set did not already contain the specified
         * element
         * @throws UnsupportedOperationException if the {@code add} operation is not
         * supported by this set
         * @throws NullPointerException if the specified element is null
         * @throws IllegalArgumentException if some property of the specified element
         * prevents it from being added to this set
         * @see #addInt(int)
         * @see PrimitiveCollection.OfInt#add(Integer)
         */
        default boolean add(Integer t) {
            return PrimitiveCollection.OfInt.super.add(t);
        }

        /**
         * <p>Adds all of the elements in the specified collection to this set if
         * they're not already present (optional operation). If the specified
         * collection is also a set, the {@code addAll} operation effectively
         * modifies this set so that its value is the <em>union</em> of the two
         * sets. The behavior of this operation is undefined if the specified
         * collection is modified while the operation is in progress.</p>
         * @implSpec <p>The default implementation is inherited from
         * {@code PrimitiveCollection.OfInt}.</p>
         * @param c collection containing elements to be added to this set
         * @return {@code true} if this set changed as a result of the call
         * @throws UnsupportedOperationException if the {@code addAll} operation is
         * not supported by this set.
         * @throws ClassCastException if the class of an element of the specified
         * collection prevents it from being added to this set
         * @throws NullPointerException if the specified collection contains one or
         * more null elements, or if the specified collection is null
         * @throws IllegalArgumentException if some property of an element of the
         * specified collection prevents it from being added to this set
         * @see #addAll(PrimitiveCollection.OfInt)
         * @see PrimitiveCollection.OfInt#addAll(Collection)
         * @see #add(Integer)
         */
        default boolean addAll(Collection<? extends Integer> c) {
            return PrimitiveCollection.OfInt.super.addAll(c);
        }

        /**
         * <p>Adds all of the elements in the specified collection to this set if
         * they're not already present (optional operation). If the specified
         * collection is also a set, the {@code addAll} operation effectively
         * modifies this set so that its value is the <em>union</em> of the two
         * sets. The behavior of this operation is undefined if the specified
         * collection is modified while the operation is in progress.</p>
         * @implSpec <p>The default implementation is inherited from
         * {@code PrimitiveCollection.OfInt}.</p>
         * @param c collection containing elements to be added to this set
         * @return {@code true} if this set changed as a result of the call
         * @throws UnsupportedOperationException if the {@code addAll} operation is
         * not supported by this set.
         * @throws IllegalArgumentException if some property of an element of the
         * specified collection prevents it from being added to this set
         * @see #addAll(Collection)
         * @see PrimitiveCollection.OfInt#addAll(PrimitiveCollection.OfInt)
         * @see #addInt(int)
         */
        default boolean addAll(PrimitiveCollection.OfInt c) {
            return PrimitiveCollection.OfInt.super.addAll(c);
        }

        /**
         * <p>Adds the specified element to this set if it is not already present
         * (optional operation). More formally, adds the specified element {@code i}
         * to this set if the set contains no element {@code e} such that
         * {@code i == e}. If this set already contains the element, the call leaves
         * the set unchanged and returns {@code false}. In combination with the
         * restriction on constructors, this ensures that sets never contains
         * duplicate elements.</p>
         * <p>The stipulation above does not apply that sets must accept all elements;
         * sets may refuse to add any particular element, and throw an exception, as
         * described in the specification for
         * {@link Collection#add(Object) Collection.add}. Individual set
         * implementations should clearly document any restrictions on the elements
         * that they may contain.</p>
         * @implSpec <p>The default implementation is inherited from
         * {@code PrimitiveCollection.OfInt}.</p>
         * @param i element to be added to this set
         * @return {@code true} if this set did not already contain the specified
         * element
         * @throws UnsupportedOperationException if the {@code add} operation is not
         * supported by this set
         * @throws IllegalArgumentException if some property of the specified element
         * prevents it from being added to this set
         * @see #add(Integer)
         * @see PrimitiveCollection.OfInt#addInt(int)
         */
        default boolean addInt(int i) {
            return PrimitiveCollection.OfInt.super.addInt(i);
        }

        /**
         * <p>Returns {@code true} if this set contains the specified element.
         * More formally, returns {@code true} if and only if this set contains an
         * element {@code e} such that {@code Objects.equals(o, e)}.</p>
         * <p>Because this method takes boxed values, reliance on it may eliminate any
         * performance advantage obtained by using a primitive collection; where
         * possible, users may wish to utilize the {@link #containsInt(int)}
         * method instead,</p>
         * @implSpec <p>The default implementation is inherited from
         * {@code PrimitiveCollection.OfInt}.</p>
         * @param o element whose presence in this set is to be tested
         * @return {@code true} if this set contains the specified element
         * @throws ClassCastException if the type of the specified element is
         * incompatible with this set (optional)
         * @throws NullPointerException if the specified element is null (optional)
         */
        default boolean contains(Object o) {
            return PrimitiveCollection.OfInt.super.contains(o);
        }

        /**
         * <p>Returns {@code true} if this set contains all of the elements of the
         * specified collection. If the specified collection is also a set, this
         * method returns {@code true} if it is a <em>subset</em> of this set.</p>
         * @implSpec <p>The default implementation is inherited from
         * {@code PrimitiveCollection.OfInt}.</p>
         * @param c collection to be checked for containment in this set
         * @return {@code true} if this set contains all of the elements of the
         * specified collection
         * @throws ClassCastException if the types of one or more elements in the
         * specified collection are incompatible with this set (optional)
         * @throws NullPointerException if the specified collection contains one or
         * more null elements (optional), or if the specified collection is null
         * @see #containsAll(PrimitiveCollection.OfInt)
         * @see PrimitiveCollection.OfInt#containsAll(Collection)
         * @see #contains(Object)
         */
        default boolean containsAll(Collection<?> c) {
            return PrimitiveCollection.OfInt.super.containsAll(c);
        }

        /**
         * <p>Returns {@code true} if this set contains all of the elements of the
         * specified collection. If the specified collection is also a set, this
         * method returns {@code true} if it is a <em>subset</em> of this set.</p>
         * @implSpec <p>The default implementation is inherited from
         * {@code PrimitiveCollection.OfInt}.</p>
         * @param c collection to be checked for containment in this set
         * @return {@code true} if this set contains all of the elements of the
         * specified collection
         * @throws NullPointerException if the specified collection is null
         * @see #containsAll(Collection)
         * @see PrimitiveCollection.OfInt#containsAll(PrimitiveCollection.OfInt)
         * @see #containsInt(int)
         */
        default boolean containsAll(PrimitiveCollection.OfInt c) {
            return PrimitiveCollection.OfInt.super.containsAll(c);
        }

        /**
         * <p>Returns {@code true} if this set contains the specified element.
         * More formally, returns {@code true} if and only if this set contains an
         * element {@code e} such that {@code i == e}.</p>
         * @implSpec <p>The default implementation is inherited from
         * {@code PrimitiveCollection.OfInt}.</p>
         * @param i element whose presence in this set is to be tested
         * @return {@code true} if this set contains the specified element
         * @see #contains(Object)
         * @see PrimitiveCollection.OfInt#containsInt(int)
         */
        default boolean containsInt(int i) {
            return PrimitiveCollection.OfInt.super.containsInt(i);
        }

        /**
         * <p>Returns an unmodifiable set containing the elements of the given
         * primitive collection. The given collection must not be null. If the
         * given collection contains duplicate elements, a single instance of
         * that element will be preserved. If the given collection is
         * subsequently modified, the returned set will not reflect such
         * modifications.</p>
         * @implNote <p>If the given collection is an unmodifiable set, calling
         * copyOf will generally not create a copy.</p>
         * @param coll a primitive collection from which elements are drawn,
         *             must be non-null
         * @return a {@code PrimitiveSet.OfInt} containing the elements of
         * the given collection
         * @throws NullPointerException if coll is null
         */
        static PrimitiveSet.OfInt copyOf(PrimitiveCollection.OfInt coll) {
            return PrimitiveCollections.setOf(coll);
        }

        /**
         * <p>Returns a primitive iterator over the elements in this collection.
         * The elements are returned in no particular order (unless this set is an
         * instance of some class that provides a guarantee).</p>
         * @return a {@code PrimitiveIterator.OfInt} over the elements in this set
         */
        PrimitiveIterator.OfInt iterator();

        /**
         * <p>Returns an unmodifiable set containing zero elements.</p>
         * @return an empty {@code PrimitiveSet.OfInt}.
         */
        static PrimitiveSet.OfInt of() {
            return PrimitiveCollections.emptyIntSet();
        }

        /**
         * <p>Returns an unmodifiable set containing one element.</p>
         * @param e the single element
         * @return a {@code PrimitiveSet.OfInt} containing the specified element
         */
        static PrimitiveSet.OfInt of(int e) {
            return PrimitiveCollections.singleton(e);
        }

        /**
         * <p>Returns an unmodifiable set containing an arbitrary number of
         * elements.</p>
         * @apiNote <p>This method also accepts a single array as an argument. The
         * size of the resulting set will be equal to the length of the array.</p>
         * @param elements the elements to be contained in the set
         * @return a {@code PrimitiveSet.OfInt} containing the specified elements
         * @throws IllegalArgumentException if there are any duplicate elements
         * @throws NullPointerException if the array is null
         */
        static PrimitiveSet.OfInt of(int... elements) {
            return PrimitiveCollections.setOf(elements);
        }

        /**
         * <p>Returns a possibly parallel {@code IntStream} with this set as its
         * source. It is allowable for this method to return a sequential int
         * stream.</p>
         * <p>This method should be overridden when the {@link #spliterator()}
         * method cannot return a spliterator that is {@code IMMUTABLE},
         * {@code CONCURRENT} or <em>late-binding</em>. (See
         * {@link #spliterator()} for details.)</p>
         * @implSpec <p>The default implementation creates a parallel
         * {@code IntStream} from the set's {@code Spliterator.OfInt}.</p>
         * @return a possibly parallel {@code IntStream} over the elements in
         * this collection
         */
        default IntStream parallelPrimitiveStream() {
            return StreamSupport.intStream(spliterator(), true);
        }

        /**
         * <p>Returns a sequential {@code IntStream} with this set as its
         * source.</p>
         * <p>This method should be overridden when the {@link #spliterator()}
         * method cannot return a spliterator that is {@code IMMUTABLE},
         * {@code CONCURRENT}, or <em>late-binding</em>. (See
         * {@link #spliterator()} for details.)</p>
         * @implSpec <p>The default implementation creates a sequential
         * {@code IntStream} from the set's {@code Spliterator.OfInt}.</p>
         * @return a sequential {@code IntStream} over the elements in this set
         */
        default IntStream primitiveStream() {
            return StreamSupport.intStream(spliterator(), false);
        }

        /**
         * <p>Removes the specified element from this set if it is present (optional
         * operation). Move formally, removes an element {@code e} such that
         * {@code Objects.equals(o, e)}, if this set contains such an element.
         * Returns {@code true} if this set contained the element (or equivalently,
         * if this set changed as a result of the call). (This set will not contain
         * the element once the call returns.)</p>
         * <p>As this method requires boxing the element to be removed, reliance upon
         * it may eliminate any benefit from using a primitive set. Developers should
         * prefer using {@link #removeInt(int)} where possible.</p>
         * @implSpec <p>The default implementation is inherited from
         * {@code PrimitiveCollection.OfInt}.</p>
         * @param o element to be removed from this set, if present
         * @return {@code true} if this set contained the specified element
         * @throws ClassCastException if the type of the specified element is
         * incompatible with this set (optional)
         * @throws NullPointerException if the specified element is null (optional)
         * @throws UnsupportedOperationException if the {@code remove} operation
         * is not supported by this set
         * @see #removeInt(int)
         * @see PrimitiveCollection.OfInt#remove(Object)
         */
        default boolean remove(Object o) {
            return PrimitiveCollection.OfInt.super.remove(o);
        }

        /**
         * <p>Removes from this set all of its elements that are contained in the
         * specified collection (optional operation). If the specified collection is
         * also a set, this operation effectively modifies this set so that its
         * value is the <em>asymmetric set difference</em> of the two sets.</p>
         * @implSpec <p>The default implementation is inherited from
         * {@code PrimitiveCollection.OfInt}.</p>
         * @param c collection containing elements to be removed from this set
         * @return {@code true} if this set changed as a result of the call
         * @throws UnsupportedOperationException if the {@code removeAll} operation
         * is not supported by this set
         * @throws ClassCastException if the class of an element of this set is
         * incompatible with the specified collection (optional)
         * @throws NullPointerException if the specified collection is null
         */
        default boolean removeAll(Collection<?> c) {
            return PrimitiveCollection.OfInt.super.removeAll(c);
        }

        /**
         * <p>Removes from this set all of its elements that are contained in the
         * specified collection (optional operation). If the specified collection is
         * also a set, this operation effectively modifies this set so that its
         * value is the <em>asymmetric set difference</em> of the two sets.</p>
         * @implSpec <p>The default implementation is inherited from
         * {@code PrimitiveCollection.OfInt}.</p>
         * @param c collection containing elements to be removed from this set
         * @return {@code true} if this set changed as a result of the call
         * @throws UnsupportedOperationException if the {@code removeAll} operation
         * is not supported by this set
         * @throws NullPointerException if the specified collection is null
         */
        default boolean removeAll(PrimitiveCollection.OfInt c) {
            return PrimitiveCollection.OfInt.super.removeAll(c);
        }

        /**
         * <p>Removes the specified element from this set if it is present (optional
         * operation). Move formally, removes an element {@code e} such that
         * {@code i == e}, if this set contains such an element.
         * Returns {@code true} if this set contained the element (or equivalently,
         * if this set changed as a result of the call). (This set will not contain
         * the element once the call returns.)</p>
         * @implSpec <p>The default implementation is inherited from
         * {@code PrimitiveCollection.OfInt}.</p>
         * @param i element to be removed from this set, if present
         * @return {@code true} if this set contained the specified element
         * @throws UnsupportedOperationException if the {@code remove} operation
         * is not supported by this set
         * @see #remove(Object)
         * @see PrimitiveCollection.OfInt#removeInt(int)
         */
        default boolean removeInt(int i) {
            return PrimitiveCollection.OfInt.super.removeInt(i);
        }

        /**
         * <p>Retains only the elements in this set that are contained in the
         * specified collection (optional operation). In other words, removes from
         * this set all of its elements that are not contained in the specified
         * collection. If the specified collection is also a set, this operation
         * effectively modifies this set so that its value is the
         * <em>intersection</em> of the two sets.</p>
         * @implSpec <p>The default implementation is inherited from
         * {@code PrimitiveCollection.OfInt}.</p>
         * @param c collection containing elements to be retained in this set
         * @return {@code true} if this set changes as a result of the call
         * @throws UnsupportedOperationException if the {@code retainAll}
         * operation is not supported by this set
         * @throws ClassCastException if the class of an element of this set is
         * incompatible with the specified collection (optional)
         * @throws NullPointerException if the specified collection is null
         * @see #retainAll(PrimitiveCollection.OfInt)
         * @see PrimitiveCollection.OfInt#retainAll(Collection)
         * @see #remove(Object)
         */
        default boolean retainAll(Collection<?> c) {
            return PrimitiveCollection.OfInt.super.retainAll(c);
        }

        /**
         * <p>Retains only the elements in this set that are contained in the
         * specified collection (optional operation). In other words, removes from
         * this set all of its elements that are not contained in the specified
         * collection. If the specified collection is also a set, this operation
         * effectively modifies this set so that its value is the
         * <em>intersection</em> of the two sets.</p>
         * @implSpec <p>The default implementation is inherited from
         * {@code PrimitiveCollection.OfInt}.</p>
         * @param c collection containing elements to be retained in this set
         * @return {@code true} if this set changes as a result of the call
         * @throws UnsupportedOperationException if the {@code retainAll}
         * operation is not supported by this set
         * @throws NullPointerException if the specified collection is null
         * @see #retainAll(Collection)
         * @see PrimitiveCollection.OfInt#retainAll(PrimitiveCollection.OfInt)
         * @see #removeInt(int)
         */
        default boolean retainAll(PrimitiveCollection.OfInt c) {
            return PrimitiveCollection.OfInt.super.retainAll(c);
        }

        /**
         * <p>Creates a {@code Spliterator.OfInt} over the elements in this
         * set.</p>
         * <p>The {@code Spliterator.OfInt} reports {@link Spliterator#DISTINCT}.
         * Implementations should document the reporting of additional
         * characteristic values.</p>
         * @implSpec <p>The default implementation creates a <em>late-binding</em>
         * spliterator from the set's {@code PrimitiveIterator.OfInt}. The
         * spliterator inherits the <em>fail-fast</em> properties of the set's
         * iterator.</p>
         * <p>The created {@code Spliterator.OfInt} additionally reports
         * {@link Spliterator#SIZED}.</p>
         * @implNote <p>The created {@code Spliterator.OfInt} additionally reports
         * {@link Spliterator#SUBSIZED}.</p>
         * @return a {@code Spliterator.OfInt} over the elements in this set
         */
        default Spliterator.OfInt spliterator() {
            return PrimitiveCollections.intSpliterator(this, Spliterator.DISTINCT);
        }

        /**
         * <p>Returns an array containing all of the elements in this set. If this set
         * makes any guarantees as to what order its elements are returned by its
         * iterator, this method must return the elements in the same order.</p>
         * <p>The returned array will be "safe" in that no references to it are
         * maintained by this set. (In other words, this method must allocate a new
         * array even if this set is backed by an array). The called is thus free
         * to modify the returned array.</p>
         * @implSpec <p>The default implementation is inherited from
         * {@code PrimitiveCollection.OfInt}.</p>
         * @return an array containing all the elements in this set
         */
        default int[] toPrimitiveArray() {
            return PrimitiveCollection.OfInt.super.toPrimitiveArray();
        }

        /**
         * <p>Returns a set view over the specified set. This view will not permit
         * any operations which modify the set.</p>
         * <p>The returned view is backed by the set, so any changes to the backing
         * set will be visible in the returned set.</p>
         * @param set set to create an unmodifiable view over
         * @return an unmodifiable view over the specified set
         */
        static PrimitiveSet.OfInt unmodifiableSet(PrimitiveSet.OfInt set) {
            return PrimitiveCollections.unmodifiableSet(set);
        }
    }

    /**
     * <p>A Set specialized for {@code long} values.</p>
     */
    @PrereleaseContent
    interface OfLong extends PrimitiveSet<Long,long[],LongConsumer,
            LongPredicate,Spliterator.OfLong,LongStream,
            PrimitiveCollection.OfLong>, PrimitiveCollection.OfLong {
        /**
         * <p>Adds the specified element to this set if it is not already present
         * (optional operation). More formally, adds the specified element {@code t}
         * to this set if the set contains no element {@code e} such that
         * {@code Objects.equals(t, e)}. If this set already contains the element,
         * the call leaves the set unchanged and returns {@code false}. In
         * combination with the restriction on constructors, this ensures that
         * sets never contains duplicate elements.</p>
         * <p>The stipulation above does not apply that sets must accept all elements;
         * sets may refuse to add any particular element, and throw an exception, as
         * described in the specification for
         * {@link Collection#add(Object) Collection.add}. Individual set
         * implementations should clearly document any restrictions on the elements
         * that they may contain.</p>
         * <p>Because this method takes boxed values, reliance on it may eliminate any
         * performance advantage obtained by using a primitive collection; where
         * possible, users may wish to utilize the {@link #addLong(long)} method
         * instead,</p>
         * @implSpec <p>The default implementation is inherited from
         * {@code PrimitiveCollection.OfLong}.</p>
         * @param t element to be added to this set
         * @return {@code true} if this set did not already contain the specified
         * element
         * @throws UnsupportedOperationException if the {@code add} operation is not
         * supported by this set
         * @throws NullPointerException if the specified element is null
         * @throws IllegalArgumentException if some property of the specified element
         * prevents it from being added to this set
         * @see #addLong(long)
         * @see PrimitiveCollection.OfLong#add(Long)
         */
        default boolean add(Long t) {
            return PrimitiveCollection.OfLong.super.add(t);
        }

        /**
         * <p>Adds all of the elements in the specified collection to this set if
         * they're not already present (optional operation). If the specified
         * collection is also a set, the {@code addAll} operation effectively
         * modifies this set so that its value is the <em>union</em> of the two
         * sets. The behavior of this operation is undefined if the specified
         * collection is modified while the operation is in progress.</p>
         * @implSpec <p>The default implementation is inherited from
         * {@code PrimitiveCollection.OfLong}.</p>
         * @param c collection containing elements to be added to this set
         * @return {@code true} if this set changed as a result of the call
         * @throws UnsupportedOperationException if the {@code addAll} operation is
         * not supported by this set.
         * @throws ClassCastException if the class of an element of the specified
         * collection prevents it from being added to this set
         * @throws NullPointerException if the specified collection contains one or
         * more null elements, or if the specified collection is null
         * @throws IllegalArgumentException if some property of an element of the
         * specified collection prevents it from being added to this set
         * @see #addAll(PrimitiveCollection.OfLong)
         * @see PrimitiveCollection.OfLong#addAll(Collection)
         * @see #add(Long)
         */
        default boolean addAll(Collection<? extends Long> c) {
            return PrimitiveCollection.OfLong.super.addAll(c);
        }

        /**
         * <p>Adds all of the elements in the specified collection to this set if
         * they're not already present (optional operation). If the specified
         * collection is also a set, the {@code addAll} operation effectively
         * modifies this set so that its value is the <em>union</em> of the two
         * sets. The behavior of this operation is undefined if the specified
         * collection is modified while the operation is in progress.</p>
         * @implSpec <p>The default implementation is inherited from
         * {@code PrimitiveCollection.OfLong}.</p>
         * @param c collection containing elements to be added to this set
         * @return {@code true} if this set changed as a result of the call
         * @throws UnsupportedOperationException if the {@code addAll} operation is
         * not supported by this set.
         * @throws IllegalArgumentException if some property of an element of the
         * specified collection prevents it from being added to this set
         * @see #addAll(Collection)
         * @see PrimitiveCollection.OfLong#addAll(PrimitiveCollection.OfLong)
         * @see #addLong(long)
         */
        default boolean addAll(PrimitiveCollection.OfLong c) {
            return PrimitiveCollection.OfLong.super.addAll(c);
        }

        /**
         * <p>Adds the specified element to this set if it is not already present
         * (optional operation). More formally, adds the specified element {@code l}
         * to this set if the set contains no element {@code e} such that
         * {@code l == e}. If this set already contains the element, the call leaves
         * the set unchanged and returns {@code false}. In combination with the
         * restriction on constructors, this ensures that sets never contains
         * duplicate elements.</p>
         * <p>The stipulation above does not apply that sets must accept all elements;
         * sets may refuse to add any particular element, and throw an exception, as
         * described in the specification for
         * {@link Collection#add(Object) Collection.add}. Individual set
         * implementations should clearly document any restrictions on the elements
         * that they may contain.</p>
         * @implSpec <p>The default implementation is inherited from
         * {@code PrimitiveCollection.OfLong}.</p>
         * @param l element to be added to this set
         * @return {@code true} if this set did not already contain the specified
         * element
         * @throws UnsupportedOperationException if the {@code add} operation is not
         * supported by this set
         * @throws IllegalArgumentException if some property of the specified element
         * prevents it from being added to this set
         * @see #add(Long)
         * @see PrimitiveCollection.OfLong#addLong(long)
         */
        default boolean addLong(long l) {
            return PrimitiveCollection.OfLong.super.addLong(l);
        }

        /**
         * <p>Returns {@code true} if this set contains the specified element.
         * More formally, returns {@code true} if and only if this set contains an
         * element {@code e} such that {@code Objects.equals(o, e)}.</p>
         * <p>Because this method takes boxed values, reliance on it may eliminate any
         * performance advantage obtained by using a primitive collection; where
         * possible, users may wish to utilize the {@link #containsLong(long)}
         * method instead,</p>
         * @implSpec <p>The default implementation is inherited from
         * {@code PrimitiveCollection.OfLong}.</p>
         * @param o element whose presence in this set is to be tested
         * @return {@code true} if this set contains the specified element
         * @throws ClassCastException if the type of the specified element is
         * incompatible with this set (optional)
         * @throws NullPointerException if the specified element is null (optional)
         */
        default boolean contains(Object o) {
            return PrimitiveCollection.OfLong.super.contains(o);
        }

        /**
         * <p>Returns {@code true} if this set contains all of the elements of the
         * specified collection. If the specified collection is also a set, this
         * method returns {@code true} if it is a <em>subset</em> of this set.</p>
         * @implSpec <p>The default implementation is inherited from
         * {@code PrimitiveCollection.OfLong}.</p>
         * @param c collection to be checked for containment in this set
         * @return {@code true} if this set contains all of the elements of the
         * specified collection
         * @throws ClassCastException if the types of one or more elements in the
         * specified collection are incompatible with this set (optional)
         * @throws NullPointerException if the specified collection contains one or
         * more null elements (optional), or if the specified collection is null
         * @see #containsAll(PrimitiveCollection.OfLong)
         * @see PrimitiveCollection.OfLong#containsAll(Collection)
         * @see #contains(Object)
         */
        default boolean containsAll(Collection<?> c) {
            return PrimitiveCollection.OfLong.super.containsAll(c);
        }

        /**
         * <p>Returns {@code true} if this set contains all of the elements of the
         * specified collection. If the specified collection is also a set, this
         * method returns {@code true} if it is a <em>subset</em> of this set.</p>
         * @implSpec <p>The default implementation is inherited from
         * {@code PrimitiveCollection.OfLong}.</p>
         * @param c collection to be checked for containment in this set
         * @return {@code true} if this set contains all of the elements of the
         * specified collection
         * @throws NullPointerException if the specified collection is null
         * @see #containsAll(Collection)
         * @see PrimitiveCollection.OfLong#containsAll(PrimitiveCollection.OfLong)
         * @see #containsLong(long)
         */
        default boolean containsAll(PrimitiveCollection.OfLong c) {
            return PrimitiveCollection.OfLong.super.containsAll(c);
        }

        /**
         * <p>Returns {@code true} if this set contains the specified element.
         * More formally, returns {@code true} if and only if this set contains an
         * element {@code e} such that {@code l == e}.</p>
         * @implSpec <p>The default implementation is inherited from
         * {@code PrimitiveCollection.OfLong}.</p>
         * @param l element whose presence in this set is to be tested
         * @return {@code true} if this set contains the specified element
         * @see #contains(Object)
         * @see PrimitiveCollection.OfLong#containsLong(long)
         */
        default boolean containsLong(long l) {
            return PrimitiveCollection.OfLong.super.containsLong(l);
        }

        /**
         * <p>Returns an unmodifiable set containing the elements of the given
         * primitive collection. The given collection must not be null. If the
         * given collection contains duplicate elements, a single instance of
         * that element will be preserved. If the given collection is
         * subsequently modified, the returned set will not reflect such
         * modifications.</p>
         * @implNote <p>If the given collection is an unmodifiable set, calling
         * copyOf will generally not create a copy.</p>
         * @param coll a primitive collection from which elements are drawn,
         *             must be non-null
         * @return a {@code PrimitiveSet.OfLong} containing the elements of
         * the given collection
         * @throws NullPointerException if coll is null
         */
        static PrimitiveSet.OfLong copyOf(PrimitiveCollection.OfLong coll) {
            return PrimitiveCollections.setOf(coll);
        }

        /**
         * <p>Returns a primitive iterator over the elements in this collection.
         * The elements are returned in no particular order (unless this set is an
         * instance of some class that provides a guarantee).</p>
         * @return a {@code PrimitiveIterator.OfLong} over the elements in this set
         */
        PrimitiveIterator.OfLong iterator();

        /**
         * <p>Returns an unmodifiable set containing zero elements.</p>
         * @return an empty {@code PrimitiveSet.OfLong}.
         */
        static PrimitiveSet.OfLong of() {
            return PrimitiveCollections.emptyLongSet();
        }

        /**
         * <p>Returns an unmodifiable set containing one element.</p>
         * @param e the single element
         * @return a {@code PrimitiveSet.OfLong} containing the specified element
         */
        static PrimitiveSet.OfLong of(long e) {
            return PrimitiveCollections.singleton(e);
        }

        /**
         * <p>Returns an unmodifiable set containing an arbitrary number of
         * elements.</p>
         * @apiNote <p>This method also accepts a single array as an argument. The
         * size of the resulting set will be equal to the length of the array.</p>
         * @param elements the elements to be contained in the set
         * @return a {@code PrimitiveSet.OfLong} containing the specified elements
         * @throws IllegalArgumentException if there are any duplicate elements
         * @throws NullPointerException if the array is null
         */
        static PrimitiveSet.OfLong of(long... elements) {
            return PrimitiveCollections.setOf(elements);
        }

        /**
         * <p>Returns a possibly parallel {@code LongStream} with this set as its
         * source. It is allowable for this method to return a sequential long
         * stream.</p>
         * <p>This method should be overridden when the {@link #spliterator()}
         * method cannot return a spliterator that is {@code IMMUTABLE},
         * {@code CONCURRENT} or <em>late-binding</em>. (See
         * {@link #spliterator()} for details.)</p>
         * @implSpec <p>The default implementation creates a parallel
         * {@code LongStream} from the set's {@code Spliterator.OfLong}.</p>
         * @return a possibly parallel {@code LongStream} over the elements in
         * this collection
         */
        default LongStream parallelPrimitiveStream() {
            return StreamSupport.longStream(spliterator(), true);
        }

        /**
         * <p>Returns a sequential {@code LongStream} with this set as its
         * source.</p>
         * <p>This method should be overridden when the {@link #spliterator()}
         * method cannot return a spliterator that is {@code IMMUTABLE},
         * {@code CONCURRENT}, or <em>late-binding</em>. (See
         * {@link #spliterator()} for details.)</p>
         * @implSpec <p>The default implementation creates a sequential
         * {@code LongStream} from the set's {@code Spliterator.OfLong}.</p>
         * @return a sequential {@code LongStream} over the elements in this set
         */
        default LongStream primitiveStream() {
            return StreamSupport.longStream(spliterator(), false);
        }

        /**
         * <p>Removes the specified element from this set if it is present (optional
         * operation). Move formally, removes an element {@code e} such that
         * {@code Objects.equals(o, e)}, if this set contains such an element.
         * Returns {@code true} if this set contained the element (or equivalently,
         * if this set changed as a result of the call). (This set will not contain
         * the element once the call returns.)</p>
         * <p>As this method requires boxing the element to be removed, reliance upon
         * it may eliminate any benefit from using a primitive set. Users should
         * prefer using {@link #removeLong(long)} where possible.</p>
         * @implSpec <p>The default implementation is inherited from
         * {@code PrimitiveCollection.OfLong}.</p>
         * @param o element to be removed from this set, if present
         * @return {@code true} if this set contained the specified element
         * @throws ClassCastException if the type of the specified element is
         * incompatible with this set (optional)
         * @throws NullPointerException if the specified element is null (optional)
         * @throws UnsupportedOperationException if the {@code remove} operation
         * is not supported by this set
         * @see #removeLong(long)
         * @see PrimitiveCollection.OfLong#remove(Object)
         */
        default boolean remove(Object o) {
            return PrimitiveCollection.OfLong.super.remove(o);
        }

        /**
         * <p>Removes from this set all of its elements that are contained in the
         * specified collection (optional operation). If the specified collection is
         * also a set, this operation effectively modifies this set so that its
         * value is the <em>asymmetric set difference</em> of the two sets.</p>
         * @implSpec <p>The default implementation is inherited from
         * {@code PrimitiveCollection.OfLong}.</p>
         * @param c collection containing elements to be removed from this set
         * @return {@code true} if this set changed as a result of the call
         * @throws UnsupportedOperationException if the {@code removeAll} operation
         * is not supported by this set
         * @throws ClassCastException if the class of an element of this set is
         * incompatible with the specified collection (optional)
         * @throws NullPointerException if the specified collection is null
         */
        default boolean removeAll(Collection<?> c) {
            return PrimitiveCollection.OfLong.super.removeAll(c);
        }

        /**
         * <p>Removes from this set all of its elements that are contained in the
         * specified collection (optional operation). If the specified collection is
         * also a set, this operation effectively modifies this set so that its
         * value is the <em>asymmetric set difference</em> of the two sets.</p>
         * @implSpec <p>The default implementation is inherited from
         * {@code PrimitiveCollection.OfLong}.</p>
         * @param c collection containing elements to be removed from this set
         * @return {@code true} if this set changed as a result of the call
         * @throws UnsupportedOperationException if the {@code removeAll} operation
         * is not supported by this set
         * @throws NullPointerException if the specified collection is null
         */
        default boolean removeAll(PrimitiveCollection.OfLong c) {
            return PrimitiveCollection.OfLong.super.removeAll(c);
        }

        /**
         * <p>Removes the specified element from this set if it is present (optional
         * operation). Move formally, removes an element {@code e} such that
         * {@code l == e}, if this set contains such an element.
         * Returns {@code true} if this set contained the element (or equivalently,
         * if this set changed as a result of the call). (This set will not contain
         * the element once the call returns.)</p>
         * @implSpec <p>The default implementation is inherited from
         * {@code PrimitiveCollection.OfLong}.</p>
         * @param l element to be removed from this set, if present
         * @return {@code true} if this set contained the specified element
         * @throws UnsupportedOperationException if the {@code remove} operation
         * is not supported by this set
         * @see #remove(Object)
         * @see PrimitiveCollection.OfLong#removeLong(long)
         */
        default boolean removeLong(long l) {
            return PrimitiveCollection.OfLong.super.removeLong(l);
        }

        /**
         * <p>Retains only the elements in this set that are contained in the
         * specified collection (optional operation). In other words, removes from
         * this set all of its elements that are not contained in the specified
         * collection. If the specified collection is also a set, this operation
         * effectively modifies this set so that its value is the
         * <em>intersection</em> of the two sets.</p>
         * @implSpec <p>The default implementation is inherited from
         * {@code PrimitiveCollection.OfLong}.</p>
         * @param c collection containing elements to be retained in this set
         * @return {@code true} if this set changes as a result of the call
         * @throws UnsupportedOperationException if the {@code retainAll}
         * operation is not supported by this set
         * @throws ClassCastException if the class of an element of this set is
         * incompatible with the specified collection (optional)
         * @throws NullPointerException if the specified collection is null
         * @see #retainAll(PrimitiveCollection.OfLong)
         * @see PrimitiveCollection.OfLong#retainAll(Collection)
         * @see #remove(Object)
         */
        default boolean retainAll(Collection<?> c) {
            return PrimitiveCollection.OfLong.super.retainAll(c);
        }

        /**
         * <p>Retains only the elements in this set that are contained in the
         * specified collection (optional operation). In other words, removes from
         * this set all of its elements that are not contained in the specified
         * collection. If the specified collection is also a set, this operation
         * effectively modifies this set so that its value is the
         * <em>intersection</em> of the two sets.</p>
         * @implSpec <p>The default implementation is inherited from
         * {@code PrimitiveCollection.OfLong}.</p>
         * @param c collection containing elements to be retained in this set
         * @return {@code true} if this set changes as a result of the call
         * @throws UnsupportedOperationException if the {@code retainAll}
         * operation is not supported by this set
         * @throws NullPointerException if the specified collection is null
         * @see #retainAll(Collection)
         * @see PrimitiveCollection.OfLong#retainAll(PrimitiveCollection.OfLong)
         * @see #removeLong(long)
         */
        default boolean retainAll(PrimitiveCollection.OfLong c) {
            return PrimitiveCollection.OfLong.super.retainAll(c);
        }

        /**
         * <p>Creates a {@code Spliterator.OfLong} over the elements in this
         * set.</p>
         * <p>The {@code Spliterator.OfLong} reports {@link Spliterator#DISTINCT}.
         * Implementations should document the reporting of additional
         * characteristic values.</p>
         * @implSpec <p>The default implementation creates a <em>late-binding</em>
         * spliterator from the set's {@code PrimitiveIterator.OfLong}. The
         * spliterator inherits the <em>fail-fast</em> properties of the set's
         * iterator.</p>
         * <p>The created {@code Spliterator.OfLong} additionally reports
         * {@link Spliterator#SIZED}.</p>
         * @implNote <p>The created {@code Spliterator.OfLong} additionally reports
         * {@link Spliterator#SUBSIZED}.</p>
         * @return a {@code Spliterator.OfLong} over the elements in this set
         */
        default Spliterator.OfLong spliterator() {
            return PrimitiveCollections.longSpliterator(this, Spliterator.DISTINCT);
        }

        /**
         * <p>Returns an array containing all of the elements in this set. If this set
         * makes any guarantees as to what order its elements are returned by its
         * iterator, this method must return the elements in the same order.</p>
         * <p>The returned array will be "safe" in that no references to it are
         * maintained by this set. (In other words, this method must allocate a new
         * array even if this set is backed by an array). The called is thus free
         * to modify the returned array.</p>
         * @implSpec <p>The default implementation is inherited from
         * {@code PrimitiveCollection.OfLong}.</p>
         * @return an array containing all the elements in this set
         */
        default long[] toPrimitiveArray() {
            return PrimitiveCollection.OfLong.super.toPrimitiveArray();
        }

        /**
         * <p>Returns a set view over the specified set. This view will not permit
         * any operations which modify the set.</p>
         * <p>The returned view is backed by the set, so any changes to the backing
         * set will be visible in the returned set.</p>
         * @param set set to create an unmodifiable view over
         * @return an unmodifiable view over the specified set
         */
        static PrimitiveSet.OfLong unmodifiableSet(PrimitiveSet.OfLong set) {
            return PrimitiveCollections.unmodifiableSet(set);
        }
    }
}
