package net.arinoru.collections;

import net.arinoru.prerelease.PrereleaseContent;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

/**
 * <p>A base type for primitive specializations of {@code Collection}. Specialized
 * subtypes are provided for {@code int}, {@code long} and {@code double}
 * values.</p>
 * <p>The specialized subtype default implementations for various
 * {@code Collection} methods rely on boxing and unboxing primitive values to and
 * from instances of their corresponding wrapper class. Such boxing may offset any
 * advantages gained when using the primitive specializations. To avoid boxing and
 * unboxing, the corresponding primitive-based methods should be used. For example,
 * {@link OfInt#containsInt(int)} and {@link OfInt#removeInt(int)} should be used
 * in favor of {@link OfInt#contains(Object)} and {@link OfInt#remove(Object)}.</p>
 * @param <T> the boxed type of elements contained within this
 *           {@code PrimitiveCollection}. The type must be a wrapper type for a
 *           primitive type, such as {@code Integer} for the primitive {@code int}
 *           type.
 * @param <T_ARR> the type of arrays who have the collection's primitive element
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
 */
@PrereleaseContent
public interface PrimitiveCollection<T,T_ARR,T_CONS,T_PRED,
        T_SPLITR extends Spliterator.OfPrimitive<T,T_CONS,T_SPLITR>,
        T_STR extends BaseStream<?,?>>
        extends Collection<T> {
    /**
     * <p>Ensures that this collection contains the specified element (optional
     * operation). Returns {@code true} if this collection changed as a result of
     * the call. (Returns {@code false} if this collection does not permit
     * duplicates and already contains the specified element.)</p>
     * <p>Primitive collections are limited to only containing elements that
     * correspond to a primitive type; as such, any primitive collection will
     * refuse to add {@code null} elements.</p>
     * <p>If a collection refuses to add a particular element for any reason
     * other than that it already contains the element, it <em>must</em> throw
     * an exception (rather than returning {@code false}). This preserves the
     * invariant that a collection always contains the specified element after
     * this call returns.</p>
     * @param t element whose presence in this collection is to be ensured
     * @return {@code true} if this collection changed as a result of the call
     * @throws UnsupportedOperationException if the {@code add} operation is not
     * supported by this collection
     * @throws ClassCastException if the class of the specified element prevents
     * it from being added to this collection
     * @throws NullPointerException if the specified element is null
     * @throws IllegalArgumentException if some property of this element prevents
     * it from being added to this collection
     * @throws IllegalStateException if the element cannot be added at this time
     * due to insertion restrictions
     */
    boolean add(T t);

    /**
     * <p>Adds all of the elements in the specified collection to this collection
     * (optional operation). The behavior of this operation is undefined if the
     * specified collection is modified while the operation is in progress. (This
     * implies that the behavior of this call is undefined if the specified
     * collection is this collection, and this collection is nonempty.) If the
     * specified collection has a defined encounter order, processing of its
     * elements generally occurs in that order.</p>
     * <p>Optionally, an implementation may return {@code false} when the
     * specified collection contains no elements not already present in this
     * collection, even if the operation would otherwise be unsupported; however,
     * if the specified collection contains any elements not present in this
     * collection that cannot be added to this collection, an exception must be
     * thrown.</p>
     * @implSpec <p>The default implementation iterates over all elements of the
     * specified collection, calling {@code add} with each element, and returns
     * {@code false} only if each invocation of the operation returns
     * {@code false}.</p>
     * @param c collection containing elements to be added to this collection
     * @return {@code true} if this collection changed as a result of the call
     * @throws UnsupportedOperationException if the {@code addAll} operation is
     * not supported by this collection
     * @throws ClassCastException if the class of an element of the specified
     * collection prevents it from being added to this collection
     * @throws NullPointerException if the specified collection contains a null
     * element, or the specified collection is null
     * @throws IllegalArgumentException if some property of an element of the
     * specified collection prevents it from being added to this collection
     * @throws IllegalStateException if not all the elements can be added at
     * this time due to insertion restrictions
     * @see #add(Object)
     */
    default boolean addAll(Collection<? extends T> c) {
        var changed = false;
        for (T t : c)
            changed |= add(t);
        return changed;
    }

    /**
     * <p>Removes all of the elements from this collection (optional operation).
     * The collection will be empty after this method returns.</p>
     * @implSpec <p>The default implementation obtains an iterator over the
     * collection and calls its {@code remove} method after each element is
     * returned.</p>
     * @throws UnsupportedOperationException if the {@code clear} operation is
     * not supported by this collection
     */
    default void clear() {
        var iterator = iterator();
        while (iterator.hasNext()) {
            iterator.next();
            iterator.remove();
        }
    }

    /**
     * <p>Returns {@code true} if this collection contains the specified element. More
     * formally, returns {@code true} if and only if this collection contains at least
     * one element {@code e} such that {@code Objects.equals(o, e)}.</p>
     * @param o element whose presence in this collection is to be tested
     * @return {@code true} if this collection contains the specified element
     * @throws ClassCastException if the type of the specified element is
     * incompatible with this collection (optional)
     * @throws NullPointerException if the specified element is null (optional)
     */
    boolean contains(Object o);

    /**
     * <p>Returns {@code true} if this collection contains all of the elements in
     * the specified collection.</p>
     * <p>The default implementation is equivalent to
     * {@code c.stream().allMatch(this::contains)}.</p>
     * @param c collection to be checked for containment in this collection
     * @return {@code true} if this collection contains all elements in the
     * specified collection
     * @throws ClassCastException if the types of one or more elements in the
     * specified collection are incompatible with this collection (optional)
     * @throws NullPointerException if the specified collection contains one or
     * more null elements (optional) or if the specified collection is null.
     * @see #contains(Object)
     */
    default boolean containsAll(Collection<?> c) {
        return c.stream().allMatch(this::contains);
    }

    /**
     * <p>Compares the specified object with this collection for equality.</p>
     * <p>While the {@code PrimitiveCollection} interface adds no stipulations
     * to the general contract for the {@code Object.equals}, programmers who
     * implement a primitive collection interface "directly" (in other words,
     * create a class that is a {@code PrimitiveCollection} but not a
     * {@code Set} or {@code List}) must exercise care if
     * they choose to override the {@code Object.equals}. It is not necessary
     * to do so, and the simplest course of action is to rely on
     * {@code Object}'s implementation, but the implementor may wish to
     * implement a "value comparison" in place of the default "reference
     * comparison." (The {@code List} and {@code Set} interfaces mandate such
     * value comparisons.)</p>
     * <p>The general contract for the {@code Object.equals} method states that
     * equals must be symmetric (in other words, {@code a.equals(b)} if and
     * only if {@code b.equals(a)}). The contracts for {@code List.equals} and
     * {@code Set.equals} state that lists are only equal to other lists, and
     * sets to other sets. Thus, a custom {@code equals} method for a
     * collection that implements neither the {@code List} nor {@code Set}
     * interface must returns {@code false} when this collection is compared
     * to any list or set. (By the same logic, it is not possible to write a
     * class that correctly implements both the {@code Set} and {@code List}
     * interfaces.)</p>
     * @param o object to be compared for equality with this collection
     * @return {@code true} if the specified object is equal to this collection
     * @see Object#equals(Object)
     * @see java.util.Set#equals(Object)
     * @see java.util.List#equals(Object)
     * @see Collection#equals(Object)
     */
    boolean equals(Object o);

    /**
     * <p>Returns the hash code value for this collection. While the
     * {@code PrimitiveCollection} interface adds no stipulations to the
     * general contract for the {@code Object.hashCode} method, programmers
     * should take note that any class that overrides the {@code Object.equals}
     * method must also override the {@code Object.hashCode} method in order to
     * satisfy the general contract for the {@code Object.hashCode} method. In
     * particular, {@code c1.equals(c2)} implies that
     * {@code c1.hashCode()==c2.hashCode()}.</p>
     * @return the hash code value for this collection
     * @see Object#hashCode()
     * @see Object#equals(Object)
     */
    int hashCode();

    /**
     * <p>Returns {@code true} if this collection contains no elements.</p>
     * @implSpec <p>The default implementation returns {@code true} if
     * {@link #size()} returns {@code 0}.</p>
     * @return {@code true} if this collection contains no elements
     */
    default boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Returns a primitive iterator over the elements in this collection. There
     * are no guarantees concerning the order in which the elements are returned
     * (unless this collection is an instance of some class that provides a
     * guarantee).
     * @return a {@code PrimitiveIterator} over the elements in this collection
     */
    PrimitiveIterator<T,T_CONS> iterator();

    /**
     * <p>Returns a possibly parallel {@code Stream} with this collection as its
     * source. It is allowable for this method to return a sequential stream.</p>
     * <p>This method should be overridden when the {@link #spliterator()} method
     * cannot returns a spliterator that is {@code IMMUTABLE}, {@code CONCURRENT},
     * or <em>late-binding</em>. (See {@link #spliterator()} for details.)</p>
     * @implSpec <p>The default implementation is inherited from
     * {@code Collection}.</p>
     * @return a possibly parallel {@code Stream} over the elements in this
     * collection
     */
    default Stream<T> parallelStream() {
        return Collection.super.parallelStream();
    }

    /**
     * <p>Returns a possibly parallel primitive stream with this
     * collection as its source. It is allowable for this method to return a
     * sequential primitive stream.</p>
     * <p>This method should be overridden when the {@link #spliterator()}
     * method cannot return a spliterator that is {@code IMMUTABLE},
     * {@code CONCURRENT}, or <em>late-binding</em>. (See
     * {@link #spliterator()} for details.)</p>
     * @return a possibly parallel primitive stream over the elements
     * in this collection
     */
    T_STR parallelPrimitiveStream();

    /**
     * <p>Returns a sequential primitive stream with this collection as
     * its source.</p>
     * <p>This method should be overridden when the {@link #spliterator()} method
     * cannot return a spliterator that is {@code IMMUTABLE}, {@code CONCURRENT},
     * or <em>late-binding</em>. (See {@link #spliterator()} for detains.)</p>
     * @return a sequential primitive stream over the elements in this
     * collection
     */
    T_STR primitiveStream();

    /**
     * <p>Removes a single instance of the specified element from this collection,
     * if it is present (optional operation). More formally, removes an element
     * {@code e} such that {@code Objects.equals(o, e)}, if this collection
     * contains one or more such elements. Returns {@code true} if this collection
     * contained the specified element (or equivalently, if this collection changed
     * as a result of the call).</p>
     * <p>Implementations may optionally return {@code false} when the specified
     * element is not present in the collection, even if the operation would
     * otherwise be unsupported.</p>
     * @param o element to be removed from this collection, if present
     * @return {@code true} if an element was removed as a result of this call
     * @throws ClassCastException if the type of the specified element is
     * incompatible with this collection (optional)
     * @throws NullPointerException if the specified element is null (optional)
     * @throws UnsupportedOperationException if the {@code remove} operation is
     * not supported by this collection
     */
    boolean remove(Object o);

    /**
     * <p>Removes all of this collection's elements that are also contained in the
     * specified collection (optional operation). After this call returns, this
     * collection will contain no elements in common with the specified
     * collection.</p>
     * <p>Implementations may optionally return {@code false} when this collection
     * contains no elements in common with the specified collection, even if the
     * operation would otherwise be unsupported; however, if any element present in
     * the specified collection cannot be removed from this collection, an
     * exception <em>must</em> be thrown to preserve the invariant that the
     * collections will contain no elements in common after the call.</p>
     * @param c collection containing elements to be removed from this collection
     * @return {@code true} if this collection changed as a result of the call
     * @throws UnsupportedOperationException if the {@code removeAll} operation
     * is not supported by this collection
     * @throws ClassCastException if the types of one or more elements in this
     * collection are incompatible with the specified collection (optional)
     * @throws NullPointerException if this collection contains one or more
     * null elements (optional) or if the specified collection is null
     */
    boolean removeAll(Collection<?> c);

    /**
     * <p>Removes all of the elements of this collection that satisfy the given
     * predicate (optional operation). Errors or runtime exceptions thrown
     * during iteration or my the predicate are relayed to the caller.</p>
     * @param filter a predicate which returns {@code true} for elements to be
     *               removed
     * @return {@code true} if any elements were removed
     * @throws NullPointerException if the specified filter is null
     * @throws UnsupportedOperationException if the {@code removeIf} operation
     * is not supported by this collection
     * @see #removeIf(Predicate)
     */
    boolean removeIf(T_PRED filter);

    /**
     * <p>Removes all of the elements of this collection that satisfy the given
     * predicate (optional operation). Errors or runtime exceptions thrown
     * during iteration or by the predicate are relayed to the caller.</p>
     * @implSpec <p>The default implementation is inherited from
     * {@link Collection}.</p>
     * @param filter a predicate which returns {@code true} for elements to be
     *               removed
     * @return {@code true} if any elements were removed
     * @throws NullPointerException if the specified filter is null
     * @throws UnsupportedOperationException if the {@code removeIf} operation
     * is not supported by this collection
     * @see Collection#removeIf(Predicate)
     */
    default boolean removeIf(Predicate<? super T> filter) {
        return Collection.super.removeIf(filter);
    }

    /**
     * <p>Retains only the elements in this collection that are contained in the
     * specified collection (optional operation). In other words, removes from
     * this collection all of its elements that are not contained in the
     * specified collection.</p>
     * @implSpec <p>The default implementation is equivalent to
     * {@code removeIf(t -> !c.contains(t))}.</p>
     * @param c collection containing elements to be retained in this collection
     * @return {@code true} if this collection changed as a result of the call
     * @throws UnsupportedOperationException if the {@code retainAll} operation is
     * not supported by this collection
     * @throws ClassCastException if the types of one or more elements in this
     * collection are incompatible with the specified collection (optional)
     * @throws NullPointerException if the specified collection is null
     * @see #contains(Object)
     */
    default boolean retainAll(Collection<?> c) {
        return removeIf(t -> !c.contains(t));
    }

    /**
     * Returns the number of elements in this collection. If this collection
     * contains more than {@link Integer#MAX_VALUE} elements, returns
     * {@code Integer.MAX_VALUE}.
     * @return the number of elements in this collection
     */
    int size();

    /**
     * <p>Creates a {@code Spliterator} over the elements in this collection.
     * Implementations should document characteristic values reported by the
     * spliterator. Such characteristic values are not required to be reported
     * if the spliterator reports {@link Spliterator#SIZED} and this collection
     * contains no elements.</p>
     * <p>In order to preserve expected laziness behavior for the
     * {@link #stream()} and {@link #parallelStream} methods, spliterators
     * should either have the characteristic of {@code IMMUTABLE} or
     * {@code CONCURRENT}, or be late-binding. If none of these is practical,
     * the implementing class should describe the spliterator's
     * documented policy of binding and structural interference, and should
     * override the {@link #stream()} and {@link #parallelStream()} methods to
     * create streams using a {@code Supplier} of the spliterator, as in:</p>
     * <code>Stream&lt;E&gt; s = StreamSupport.stream(() -> spliterator(), spliteratorCharacteristics)</code>
     * <p>These requirements ensure that streams produced by the {@link #stream()}
     * and {@link #parallelStream()} methods will reflect the contents of the
     * collection as of initiation of the terminal stream operation.</p>
     * @return a {@code Spliterator} over the elements in this collection
     */
    Spliterator.OfPrimitive<T,T_CONS,T_SPLITR> spliterator();

    /**
     * <p>Returns a sequential {@code Stream} with this collection as its
     * source.</p>
     * <p>This method should be overridden when the {@link #spliterator()}
     * method cannot return a spliterator that is {@code IMMUTABLE},
     * {@code CONCURRENT}, or <em>late-binding</em>. (See
     * {@link #spliterator()} for details.)</p>
     * @implSpec <p>The default implementation is inherited from
     * {@code Collection}.</p>
     * @return a sequential {@code Stream} over the elements in this collection
     */
    default Stream<T> stream() {
        return Collection.super.stream();
    }

    /**
     * <p>Returns an array containing all of the elements in this collection.
     * If this collection makes any guarantees as to what order its elements
     * are returned by its iterator, this method must return the elements in
     * the same order. The returned array's runtime component type is
     * {@code Object}.</p>
     * <p>The returned array will be "safe" in that no references to it are
     * maintained by this collection. (In other words, this method must
     * allocate a new array even if this collection is backed by an array).
     * The caller is thus free to modify the returned array.</p>
     * @implSpec <p>The default method implementation is based on the
     * implementation in {@link java.util.AbstractCollection}; it allocates
     * an initial array using the value returned by {@link #size()}, then
     * iterates over the collection to fill it, reallocating as necessary if
     * the iterator does not return the expected number of elements (i.e. due
     * to the collection being modified between the time of allocation and
     * the time of iteration).</p>
     * @return an array, whose runtime component type is {@code Object},
     * containing all of the elements in this collection
     */
    default Object[] toArray() {
        return PrimitiveCollections.toArray(this);
    }

    /**
     * <p>Returns an array containing all of the elements in this collection; the
     * runtime type of the returned array is that of the specified array. If
     * the collection fits the specified array, it is returned therein. Otherwise,
     * a new array is allocated with the runtime type of the specified array and
     * the size of this collection.</p>
     * <p>If this collection fits in the specified array with room to spare (i.e.
     * the array has more elements than this collection), the element in the array
     * immediately following the end of the collection is set to {@code null}.
     * (This is useful in determining the length of this collection <em>only</em>
     * if the caller knows that this collection does not contain any {@code null}
     * elements.)</p>
     * <p>If this collection makes any guarantees as to what order its elements
     * are returned by its iterator, this method must return the elements in the
     * same order.</p>
     * @implSpec <p>The default method implementation is based on the implementation
     * in {@link java.util.AbstractCollection}; it makes an initial determination
     * based on the value returned by {@link #size()} as to whether it must
     * allocate a new array or reuse the specified array, and if needed uses
     * reflection to allocate a new array with a component type matching the
     * specified array. It then populates the selected target array by iterating
     * over the elements in this collection, reallocating as necessary if the
     * iterator does not return the expected number of elements (i.e. due to the
     * collection being modified between the time of allocation and the time of
     * iteration). If the existing array is being reused, and its length is
     * greater than the number of elements returned by the iterator, a {@code null}
     * element is added. The default implementation is overflow-conscious and
     * will throw an {@link OutOfMemoryError} if the size of the collection
     * exceeds the maximum possible array size.</p>
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
        return PrimitiveCollections.toArray(this, a);
    }

    /**
     * <p>Returns an array containing all of the elements in this collection. If
     * this collection makes any guarantees as to what order its elements are
     * returned by its iterator, this method must return the elements in the
     * same order.</p>
     * <p>The returned array will be "safe" in that no references to it are
     * maintained by this collection. (In other words, this method must allocate
     * a new array even if this collection is backed by an array). The caller is
     * thus free to modify the returned array.</p>
     * @return an array containing all of the elements in this collection
     */
    T_ARR toPrimitiveArray();

    /**
     * <p>A Collection specialized for {@code double} values.</p>
     */
    @PrereleaseContent
    interface OfDouble extends PrimitiveCollection<Double,double[],DoubleConsumer,
            DoublePredicate,Spliterator.OfDouble,DoubleStream> {
        /**
         * <p>Ensures that this collection contains the specified element (optional
         * operation). Returns {@code true} if this collection changed as a result of
         * the call. (Returns {@code false} if this collection does not permit
         * duplicates and already contains the specified element.)</p>
         * <p>If a collection refuses to add a particular element for any reason
         * other than that it already contains the element, it <em>must</em> throw
         * an exception (rather than returning {@code false}). This preserves the
         * invariant that a collection always contains the specified element after
         * this call returns.</p>
         * @implSpec <p>The default implementation is equivalent to
         * {@code addDouble(t.doubleValue())}.</p>
         * @param t element whose presence in this collection is to be ensured
         * @return {@code true} if this collection changed as a result of the call
         * @throws UnsupportedOperationException if the {@code add} operation is not
         * supported by this collection
         * @throws NullPointerException if the specified element is null
         * @throws IllegalArgumentException if some property of this element prevents
         * it from being added to this collection
         * @throws IllegalStateException if the element cannot be added at this time
         * due to insertion restrictions
         */
        default boolean add(Double t) {
            return addDouble(t);
        }

        /**
         * <p>Adds all of the elements in the specified collection to this collection
         * (optional operation). The behavior of this operation is undefined if the
         * specified collection is modified while the operation is in progress. (This
         * implies that the behavior of this call is undefined if the specified
         * collection is this collection, and this collection is nonempty.) If the
         * specified collection has a defined encounter order, processing of its
         * elements generally occurs in that order.</p>
         * <p>Optionally, an implementation may return {@code false} when the
         * specified collection contains no elements not already present in this
         * collection, even if the operation would otherwise be unsupported; however,
         * if the specified collection contains any elements not present in this
         * collection that cannot be added to this collection, an exception must be
         * thrown.</p>
         * @implSpec <p>The default implementation will check whether the runtime type
         * of the collection is an instance of {@code PrimitiveCollection.OfDouble},
         * and if so will pass it to {@link #addAll(OfDouble)}. Otherwise, it
         * iterates over all elements of the specified
         * collection, calling {@code add} with each element, and returns
         * {@code false} only if each invocation of the operation returns
         * {@code false}.</p>
         * @param c collection containing elements to be added to this collection
         * @return {@code true} if this collection changed as a result of the call
         * @throws UnsupportedOperationException if the {@code addAll} operation is
         * not supported by this collection
         * @throws ClassCastException if the class of an element of the specified
         * collection prevents it from being added to this collection
         * @throws NullPointerException if the specified collection contains a null
         * element, or the specified collection is null
         * @throws IllegalArgumentException if some property of an element of the
         * specified collection prevents it from being added to this collection
         * @throws IllegalStateException if not all the elements can be added at
         * this time due to insertion restrictions
         * @see #add(Object)
         * @see #addAll(OfDouble)
         */
        default boolean addAll(Collection<? extends Double> c) {
            if (c instanceof OfDouble ofDouble)
                return addAll(ofDouble);
            return PrimitiveCollection.super.addAll(c);
        }

        /**
         * <p>Adds all of the elements in the specified collection to this collection
         * (optional operation). The behavior of this operation is undefined if the
         * specified collection is modified while the operation is in progress. (This
         * implies that the behavior of this call is undefined if the specified
         * collection is this collection, and this collection is nonempty.) If the
         * specified collection has a defined encounter order, processing of its
         * elements generally occurs in that order.</p>
         * <p>Optionally, an implementation may return {@code false} when the
         * specified collection contains no elements not already present in this
         * collection, even if the operation would otherwise be unsupported; however,
         * if the specified collection contains any elements not present in this
         * collection that cannot be added to this collection, an exception must be
         * thrown.</p>
         * @implSpec <p>The default implementation iterates over all elements of the
         * specified collection, calling {@code addDouble} with each element, and
         * returns {@code true} if any invocation of the operation returns
         * {@code true}.</p>
         * @param c collection containing elements to be added to this collection
         * @return {@code true} if this collection changed as a result of the call
         * @throws UnsupportedOperationException if the {@code addAll} operation is
         * not supported by this collection
         * @throws NullPointerException if the specified collection is null
         * @throws IllegalArgumentException if some property of an element of the
         * specified collection prevents it from being added to this collection
         * @throws IllegalStateException if not all the elements can be added at
         * this time due to insertion restrictions
         * @see #addAll(Collection)
         * @see #addDouble(double)
         */
        default boolean addAll(OfDouble c) {
            var changed = false;
            var iterator = c.iterator();
            while (iterator.hasNext())
                changed |= addDouble(iterator.nextDouble());
            return changed;
        }

        /**
         * <p>Ensures that this collection contains the specified element (optional
         * operation). Returns {@code true} if this collection changed as a result of
         * the call. (Returns {@code false} if this collection does not permit
         * duplicates and already contains the specified element.)</p>
         * <p>If a collection refuses to add a particular element for any reason
         * other than that it already contains the element, it <em>must</em> throw
         * an exception (rather than returning {@code false}). This preserves the
         * invariant that a collection always contains the specified element after
         * this call returns.</p>
         * @implSpec <p>The default implementation always throws
         * {@code UnsupportedOperationException}.</p>
         * @param d element whose presence in this collection is to be ensured
         * @return {@code true} if this collection changed as a result of the call
         * @throws UnsupportedOperationException if the {@code add} operation is not
         * supported by this collection
         * @throws IllegalArgumentException if some property of this element prevents
         * it from being added to this collection
         * @throws IllegalStateException if the element cannot be added at this time
         * due to insertion restrictions
         * @see #add(Object)
         */
        default boolean addDouble(double d) {
            throw new UnsupportedOperationException();
        }

        /**
         * <p>Returns {@code true} if this collection contains the specified element. More
         * formally, returns {@code true} if and only if this collection contains at least
         * one element {@code e} such that {@code Objects.equals(o, e)}.</p>
         * @implSpec <p>The default implementation checks whether the runtime type of the
         * specified element is {@code Double}, and if so passes it to
         * {@link #containsDouble(double)}; otherwise, it returns {@code false}.</p>
         * @param o element whose presence in this collection is to be tested
         * @return {@code true} if this collection contains the specified element
         * @throws ClassCastException if the type of the specified element is
         * incompatible with this collection (optional)
         * @throws NullPointerException if the specified element is null (optional)
         */
        default boolean contains(Object o) {
            return o instanceof Double d && containsDouble(d);
        }

        /**
         * <p>Returns {@code true} if this collection contains all of the elements in
         * the specified collection.</p>
         * @implSpec <p>The default implementation checks whether the runtime type of the
         * specified collection is an instance of {@link OfDouble}, and if so
         * passes it to {@link #containsAll(OfDouble)}. Otherwise, it is equivalent to
         * {@code c.stream().allMatch(this::contains)}.</p>
         * @param c collection to be checked for containment in this collection
         * @return {@code true} if this collection contains all elements in the
         * specified collection
         * @throws ClassCastException if the types of one or more elements in the
         * specified collection are incompatible with this collection (optional)
         * @throws NullPointerException if the specified collection contains one or
         * more null elements (optional) or if the specified collection is null.
         * @see #contains(Object)
         */
        default boolean containsAll(Collection<?> c) {
            if (c instanceof OfDouble ofDouble)
                return containsAll(ofDouble);
            return c.stream().allMatch(this::contains);
        }

        /**
         * <p>Returns {@code true} if this collection contains all of the elements in
         * the specified collection.</p>
         * @implSpec <p>The default implementation is equivalent to
         * {@code c.primitiveStream().allMatch(this::containsDouble)}.</p>
         * @param c collection to be checked for containment in this collection
         * @return {@code true} if this collection contains all elements in the
         * specified collection
         * @throws NullPointerException if the specified collection is null.
         * @see #containsAll(Collection)
         * @see #containsDouble(double)
         */
        default boolean containsAll(OfDouble c) {
            return c.primitiveStream().allMatch(this::containsDouble);
        }

        /**
         * <p>Returns {@code true} if this collection contains the specified
         * element. More formally, returns {@code true} if and only if
         * this collection contains at least one element {@code e} such that
         * {@code d == e}.</p>
         * @implSpec <p>The default implementation is equivalent to
         * {@code primitiveStream().anyMatch(e -> e == d)}</p>
         * @param d element whose presence in this collection is to be tested
         * @return {@code true} if this collection contains the specified
         * element
         * @see #contains(Object)
         * @see OfInt#containsInt(int)
         * @see OfLong#containsLong(long)
         */
        default boolean containsDouble(double d) {
            return primitiveStream().anyMatch(e -> e == d);
        }

        /**
         * Returns an empty {@code PrimitiveCollection.OfDouble}. The returned class is
         * immutable and may safely be shared between threads.
         * @return an empty double collection
         */
        static OfDouble empty() {
            return EmptyPrimitiveCollection.OfDouble.INSTANCE;
        }

        /**
         * Returns a primitive iterator over the elements in this collection. There
         * are no guarantees concerning the order in which the elements are returned
         * (unless this collection is an instance of some class that provides a
         * guarantee).
         * @return a {@code PrimitiveIterator.OfDouble} over the elements in this collection
         */
        PrimitiveIterator.OfDouble iterator();

        /**
         * <p>Returns a possibly parallel {@code DoubleStream} with this
         * collection as its source. It is allowable for this method to return a
         * sequential double stream.</p>
         * <p>This method should be overridden when the {@link #spliterator()}
         * method cannot return a spliterator that is {@code IMMUTABLE},
         * {@code CONCURRENT}, or <em>late-binding</em>. (See
         * {@link #spliterator()} for details.)</p>
         * @implSpec <p>The default implementation creates a parallel
         * {@code DoubleStream} from the collection's
         * {@code Spliterator.OfDouble}.</p>
         * @return a possibly parallel {@code DoubleStream} over the elements
         * in this collection
         */
        default DoubleStream parallelPrimitiveStream() {
            return StreamSupport.doubleStream(spliterator(), true);
        }

        /**
         * <p>Returns a sequential {@code DoubleStream} with this
         * collection as its source.</p>
         * <p>This method should be overridden when the {@link #spliterator()} method
         * cannot return a spliterator that is {@code IMMUTABLE}, {@code CONCURRENT},
         * or <em>late-binding</em>. (See {@link #spliterator()} for detains.)</p>
         * @implSpec <p>The default implementation creates a sequential
         * {@code DoubleStream} from the collection's
         * {@code Spliterator.OfDouble}.</p>
         * @return a sequential {@code DoubleStream} over the elements in
         * this collection
         */
        default DoubleStream primitiveStream() {
            return StreamSupport.doubleStream(spliterator(), false);
        }

        /**
         * <p>Removes a single instance of the specified element from this collection,
         * if it is present (optional operation). More formally, removes an element
         * {@code e} such that {@code Objects.equals(o, e)}, if this collection
         * contains one or more such elements. Returns {@code true} if this collection
         * contained the specified element (or equivalently, if this collection changed
         * as a result of the call).</p>
         * <p>Implementations may optionally return {@code false} when the specified
         * element is not present in the collection, even if the operation would
         * otherwise be unsupported.</p>
         * @implSpec <p>The default implementation checks whether the specified element
         * is a {@code Double}, and if so passes it to {@link #removeDouble(double)};
         * otherwise it returns {@code false}.</p>
         * @param o element to be removed from this collection, if present
         * @return {@code true} if an element was removed as a result of this call
         * @throws ClassCastException if the type of the specified element is
         * incompatible with this collection (optional)
         * @throws NullPointerException if the specified element is null (optional)
         * @throws UnsupportedOperationException if the {@code remove} operation is
         * not supported by this collection
         */
        default boolean remove(Object o) {
            return o instanceof Double d && removeDouble(d);
        }

        /**
         * <p>Removes all of this collection's elements that are also contained in the
         * specified collection (optional operation). After this call returns, this
         * collection will contain no elements in common with the specified
         * collection.</p>
         * <p>Implementations may optionally return {@code false} when this collection
         * contains no elements in common with the specified collection, even if the
         * operation would otherwise be unsupported; however, if any element present in
         * the specified collection cannot be removed from this collection, an
         * exception <em>must</em> be thrown to preserve the invariant that the
         * collections will contain no elements in common after the call.</p>
         * @implSpec <p>The default implementation checks the runtime type of the specified
         * collection to determine whether it is an instance of {@link OfDouble},
         * and if so passes it to {@link #removeAll(OfDouble)}; otherwise it is
         * equivalent to {@code removeIf(c::contains)}.</p>
         * @param c collection containing elements to be removed from this collection
         * @return {@code true} if this collection changed as a result of the call
         * @throws UnsupportedOperationException if the {@code removeAll} operation
         * is not supported by this collection
         * @throws ClassCastException if the types of one or more elements in this
         * collection are incompatible with the specified collection (optional)
         * @throws NullPointerException if this collection contains one or more
         * null elements (optional) or if the specified collection is null
         */
        default boolean removeAll(Collection<?> c) {
            if (c instanceof OfDouble ofDouble)
                return removeAll(ofDouble);
            return removeIf((DoublePredicate) c::contains);
        }

        /**
         * <p>Removes all of this collection's elements that are also contained in the
         * specified collection (optional operation). After this call returns, this
         * collection will contain no elements in common with the specified
         * collection.</p>
         * <p>Implementations may optionally return {@code false} when this collection
         * contains no elements in common with the specified collection, even if the
         * operation would otherwise be unsupported; however, if any element present in
         * the specified collection cannot be removed from this collection, an
         * exception <em>must</em> be thrown to preserve the invariant that the
         * collections will contain no elements in common after the call.</p>
         * @implSpec <p>The default implementation is equivalent to
         * {@code removeIf(c::containsDouble)}.</p>
         * @param c collection containing elements to be removed from this collection
         * @return {@code true} if this collection changed as a result of the call
         * @throws UnsupportedOperationException if the {@code removeAll} operation
         * is not supported by this collection
         * @throws NullPointerException if this collection is null
         */
        default boolean removeAll(OfDouble c) {
            return removeIf((DoublePredicate) c::containsDouble);
        }

        /**
         * <p>Removes a single instance of the specified element from this collection,
         * if it is present (optional operation). More formally, removes an element
         * {@code e} such that {@code d == e}, if this collection
         * contains one or more such elements. Returns {@code true} if this collection
         * contained the specified element (or equivalently, if this collection changed
         * as a result of the call).</p>
         * <p>Implementations may optionally return {@code false} when the specified
         * element is not present in the collection, even if the operation would
         * otherwise be unsupported.</p>
         * @implSpec <p>The default implementation obtains an iterator over the collection,
         * and checks each returned element until it finds one equal to the specified
         * element, in which even it calls the iterator's
         * {@link Iterator#remove() remove} method (propagating any resulting
         * {@code UnsupportedOperationException}) and returns {@code true}; if the end
         * of the iteration is reached without finding an element equal to the
         * specified element it returns {@code false}.</p>
         * @param d element to be removed from this collection, if present
         * @return {@code true} if an element was removed as a result of this call
         * @throws UnsupportedOperationException if the {@code remove} operation is
         * not supported by this collection
         */
        default boolean removeDouble(double d) {
            var iterator = iterator();
            while (iterator.hasNext())
                if (iterator.nextDouble() == d) {
                    iterator.remove();
                    return true;
                }
            return false;
        }

        /**
         * <p>Removes all of the elements of this collection that satisfy the given
         * predicate (optional operation). Errors or runtime exceptions thrown
         * during iteration or my the predicate are relayed to the caller.</p>
         * @implSpec <p>The default implementation obtains an iterator over this
         * collection, passes each element returned to the predicate, and calls
         * the iterator's {@code remove} method if the predicate returns
         * {@code true}.</p>
         * @param filter a predicate which returns {@code true} for elements to be
         *               removed
         * @return {@code true} if any elements were removed
         * @throws NullPointerException if the specified filter is null
         * @throws UnsupportedOperationException if the {@code removeIf} operation
         * is not supported by this collection
         * @see #removeIf(Predicate)
         */
        default boolean removeIf(DoublePredicate filter) {
            var changed = false;
            var iterator = iterator();
            while (iterator.hasNext())
                if (filter.test(iterator.nextDouble())) {
                    iterator.remove();
                    changed = true;
                }
            return changed;
        }

        /**
         * <p>Removes all of the elements of this collection that satisfy the given
         * predicate (optional operation). Errors or runtime exceptions thrown
         * during iteration or by the predicate are relayed to the caller.</p>
         * @implSpec <p>The default implementation checks the runtime type of the predicate
         * to determine if it is an instance of {@code DoublePredicate}, and if so
         * passes it to {@link #removeIf(DoublePredicate)}; otherwise, it uses
         * the default implementation inherited from {@link Collection}.</p>
         * @param filter a predicate which returns {@code true} for elements to be
         *               removed
         * @return {@code true} if any elements were removed
         * @throws NullPointerException if the specified filter is null
         * @throws UnsupportedOperationException if the {@code removeIf} operation
         * is not supported by this collection
         * @see Collection#removeIf(Predicate)
         */
        default boolean removeIf(Predicate<? super Double> filter) {
            if (filter instanceof DoublePredicate doublePredicate)
                return removeIf(doublePredicate);
            return PrimitiveCollection.super.removeIf(filter);
        }

        /**
         * <p>Retains only the elements in this collection that are contained in the
         * specified collection (optional operation). In other words, removes from
         * this collection all of its elements that are not contained in the
         * specified collection.</p>
         * @implSpec <p>The default implementation checks the runtime type of the collection
         * to determine if it is an instance of {@code OfDouble}, in which case it
         * passes it to {@link #retainAll(OfDouble)}; otherwise it is equivalent to
         * {@code removeIf(t -> !c.contains(t))}.</p>
         * @param c collection containing elements to be retained in this collection
         * @return {@code true} if this collection changed as a result of the call
         * @throws UnsupportedOperationException if the {@code retainAll} operation is
         * not supported by this collection
         * @throws ClassCastException if the types of one or more elements in this
         * collection are incompatible with the specified collection (optional)
         * @throws NullPointerException if the specified collection is null
         * @see #remove(Object)
         * @see #contains(Object)
         */
        default boolean retainAll(Collection<?> c) {
            if (c instanceof OfDouble ofDouble)
                return retainAll(ofDouble);
            return PrimitiveCollection.super.retainAll(c);
        }

        /**
         * <p>Retains only the elements in this collection that are contained in the
         * specified collection (optional operation). In other words, removes from
         * this collection all of its elements that are not contained in the
         * specified collection.</p>
         * @implSpec <p>The default implementation is equivalent to
         * {@code removeIf(t -> !c.containsDouble(t))}.</p>
         * @param c collection containing elements to be retained in this collection
         * @return {@code true} if this collection changed as a result of the call
         * @throws UnsupportedOperationException if the {@code retainAll} operation is
         * not supported by this collection
         * @throws NullPointerException if the specified collection is null
         * @see #containsDouble(double)
         */
        default boolean retainAll(OfDouble c) {
            return removeIf(((DoublePredicate) c::containsDouble).negate());
        }

        /**
         * <p>Creates a {@code Spliterator.OfDouble} over the elements in this
         * collection. Implementations should document characteristic values
         * reported by the spliterator. Such characteristic values are not
         * required to be reported if the spliterator reports
         * {@link Spliterator#SIZED} and this collection contains no elements.</p>
         * <p>The default implementation should be overridden by subclasses that can
         * return a more efficient spliterator. In order to preserve expected
         * laziness behavior for the {@link #stream()}, {@link #primitiveStream()},
         * {@link #parallelPrimitiveStream()} and {@link #parallelStream}
         * methods, spliterators should either have the characteristic of
         * {@code IMMUTABLE} or {@code CONCURRENT}, or be late-binding. If none of
         * these is practical, the overriding class should describe the spliterator's
         * documented policy of binding and structural interference, and should
         * override the {@link #stream()}, {@link #primitiveStream()},
         * {@link #parallelPrimitiveStream()} and {@link #parallelStream()} methods to
         * create streams using a {@code Supplier} of the spliterator, as in:</p>
         * <code>Stream&lt;E&gt; s = StreamSupport.stream(() -> spliterator(), spliteratorCharacteristics)</code>
         * <p>These requirements ensure that streams produced by these
         * methods will reflect the contents of the
         * collection as of initiation of the terminal stream operation.</p>
         * @implSpec <p>The default implementation creates a <em>late-binding</em>
         * spliterator from the collection's {@code PrimitiveIterator.OfDouble}.
         * The spliterator inherits the <em>fail-fast</em> properties of the
         * collection's iterator. The created {@code Spliterator.OfDouble} reports
         * {@link Spliterator#SIZED}.</p>
         * @implNote <p>The created {@code Spliterator.OfDouble} additionally
         * reports {@link Spliterator#SUBSIZED}. If a spliterator covers no elements
         * then the reporting of additional characteristic values, beyond that of
         * {@code SIZED} and {@code SUBSIZED}, does not aid clients to control,
         * specialize or simplify computation. However, this does enable shared use
         * of an immutable and empty spliterator instance (see
         * {@link Spliterators#emptyDoubleSpliterator()}) for empty collections, and
         * enables clients to determine if such a spliterator covers no elements.</p>
         * @return a {@code Spliterator.OfDouble} over the elements in this collection
         */
        default Spliterator.OfDouble spliterator() {
            return new PrimitiveCollectionSpliterator.OfDouble(this, 0);
        }

        /**
         * <p>Returns an array containing all of the elements in this collection. If
         * this collection makes any guarantees as to what order its elements are
         * returned by its iterator, this method must return the elements in the
         * same order.</p>
         * <p>The returned array will be "safe" in that no references to it are
         * maintained by this collection. (In other words, this method must allocate
         * a new array even if this collection is backed by an array). The caller is
         * thus free to modify the returned array.</p>
         * @implSpec <p>The default implementation is based on
         * {@link java.util.AbstractCollection}, differing only in terms of the
         * runtime type of the array. (See {@link #toArray()} for details.)</p>
         * @return an array containing all of the elements in this collection
         */
        default double[] toPrimitiveArray() {
            return PrimitiveCollections.toPrimitiveArray(this);
        }

        /**
         * <p>Returns a collection view over the specified collection. This view will
         * not permit any operations which modify the collection.</p>
         * <p>The returned view is backed by the collection, so any changes to the
         * backing collection will be visible in the returned collection.</p>
         * @param coll collection to create an unmodifiable view over
         * @return an unmodifiable view over the specified collection.
         */
        static PrimitiveCollection.OfDouble unmodifiableCollection(PrimitiveCollection.OfDouble coll) {
            return ForwardingPrimitiveCollection.OfDouble.unmodifiable(coll);
        }
    }

    /**
     * <p>A Collection specialized for {@code int} values.</p>
     */
    @PrereleaseContent
    interface OfInt extends PrimitiveCollection<Integer,int[],IntConsumer,
            IntPredicate,Spliterator.OfInt,IntStream> {
        /**
         * <p>Ensures that this collection contains the specified element (optional
         * operation). Returns {@code true} if this collection changed as a result of
         * the call. (Returns {@code false} if this collection does not permit
         * duplicates and already contains the specified element.)</p>
         * <p>If a collection refuses to add a particular element for any reason
         * other than that it already contains the element, it <em>must</em> throw
         * an exception (rather than returning {@code false}). This preserves the
         * invariant that a collection always contains the specified element after
         * this call returns.</p>
         * @implSpec <p>The default implementation is equivalent to
         * {@code addInt(t.intValue())}.</p>
         * @param t element whose presence in this collection is to be ensured
         * @return {@code true} if this collection changed as a result of the call
         * @throws UnsupportedOperationException if the {@code add} operation is not
         * supported by this collection
         * @throws NullPointerException if the specified element is null
         * @throws IllegalArgumentException if some property of this element prevents
         * it from being added to this collection
         * @throws IllegalStateException if the element cannot be added at this time
         * due to insertion restrictions
         */
        default boolean add(Integer t) {
            return addInt(t);
        }

        /**
         * <p>Adds all of the elements in the specified collection to this collection
         * (optional operation). The behavior of this operation is undefined if the
         * specified collection is modified while the operation is in progress. (This
         * implies that the behavior of this call is undefined if the specified
         * collection is this collection, and this collection is nonempty.) If the
         * specified collection has a defined encounter order, processing of its
         * elements generally occurs in that order.</p>
         * <p>Optionally, an implementation may return {@code false} when the
         * specified collection contains no elements not already present in this
         * collection, even if the operation would otherwise be unsupported; however,
         * if the specified collection contains any elements not present in this
         * collection that cannot be added to this collection, an exception must be
         * thrown.</p>
         * @implSpec <p>The default implementation will check whether the runtime type of
         * the collection is an instance of {@code PrimitiveCollection.OfInt},
         * and if so will pass it to {@link #addAll(OfInt)}. Otherwise, it
         * iterates over all elements of the specified
         * collection, calling {@code add} with each element, and returns
         * {@code false} only if each invocation of the operation returns
         * {@code false}.</p>
         * @param c collection containing elements to be added to this collection
         * @return {@code true} if this collection changed as a result of the call
         * @throws UnsupportedOperationException if the {@code addAll} operation is
         * not supported by this collection
         * @throws ClassCastException if the class of an element of the specified
         * collection prevents it from being added to this collection
         * @throws NullPointerException if the specified collection contains a null
         * element, or the specified collection is null
         * @throws IllegalArgumentException if some property of an element of the
         * specified collection prevents it from being added to this collection
         * @throws IllegalStateException if not all the elements can be added at
         * this time due to insertion restrictions
         * @see #add(Object)
         * @see #addAll(OfInt)
         */
        default boolean addAll(Collection<? extends Integer> c) {
            if (c instanceof OfInt ofInt)
                return addAll(ofInt);
            return PrimitiveCollection.super.addAll(c);
        }

        /**
         * <p>Adds all of the elements in the specified collection to this collection
         * (optional operation). The behavior of this operation is undefined if the
         * specified collection is modified while the operation is in progress. (This
         * implies that the behavior of this call is undefined if the specified
         * collection is this collection, and this collection is nonempty.) If the
         * specified collection has a defined encounter order, processing of its
         * elements generally occurs in that order.</p>
         * <p>Optionally, an implementation may return {@code false} when the
         * specified collection contains no elements not already present in this
         * collection, even if the operation would otherwise be unsupported; however,
         * if the specified collection contains any elements not present in this
         * collection that cannot be added to this collection, an exception must be
         * thrown.</p>
         * @implSpec <p>The default implementation iterates over all elements of the specified
         * collection, calling {@code addInt} with each element, and returns
         * {@code false} only if each invocation of the operation returns
         * {@code false}.</p>
         * @param c collection containing elements to be added to this collection
         * @return {@code true} if this collection changed as a result of the call
         * @throws UnsupportedOperationException if the {@code addAll} operation is
         * not supported by this collection
         * @throws NullPointerException if the specified collection is null
         * @throws IllegalArgumentException if some property of an element of the
         * specified collection prevents it from being added to this collection
         * @throws IllegalStateException if not all the elements can be added at
         * this time due to insertion restrictions
         * @see #addAll(Collection)
         * @see #addInt(int)
         */
        default boolean addAll(OfInt c) {
            var changed = false;
            var iterator = c.iterator();
            while (iterator.hasNext())
                changed |= addInt(iterator.nextInt());
            return changed;
        }

        /**
         * <p>Ensures that this collection contains the specified element (optional
         * operation). Returns {@code true} if this collection changed as a result of
         * the call. (Returns {@code false} if this collection does not permit
         * duplicates and already contains the specified element.)</p>
         * <p>If a collection refuses to add a particular element for any reason
         * other than that it already contains the element, it <em>must</em> throw
         * an exception (rather than returning {@code false}). This preserves the
         * invariant that a collection always contains the specified element after
         * this call returns.</p>
         * @implSpec <p>The default implementation always throws
         * {@code UnsupportedOperationException}.</p>
         * @param i element whose presence in this collection is to be ensured
         * @return {@code true} if this collection changed as a result of the call
         * @throws UnsupportedOperationException if the {@code add} operation is not
         * supported by this collection
         * @throws IllegalArgumentException if some property of this element prevents
         * it from being added to this collection
         * @throws IllegalStateException if the element cannot be added at this time
         * due to insertion restrictions
         */
        default boolean addInt(int i) {
            throw new UnsupportedOperationException();
        }

        /**
         * <p>Returns {@code true} if this collection contains the specified element. More
         * formally, returns {@code true} if and only if this collection contains at least
         * one element {@code e} such that {@code Objects.equals(o, e)}.</p>
         * @implSpec <p>The default implementation checks whether the runtime type of the
         * specified element is {@code Integer}, and if so passes it to
         * {@link #containsInt(int)}; otherwise, it returns {@code false}.</p>
         * @param o element whose presence in this collection is to be tested
         * @return {@code true} if this collection contains the specified element
         * @throws ClassCastException if the type of the specified element is
         * incompatible with this collection (optional)
         * @throws NullPointerException if the specified element is null (optional)
         */
        default boolean contains(Object o) {
            return o instanceof Integer i && containsInt(i);
        }

        /**
         * <p>Returns {@code true} if this collection contains all of the elements in
         * the specified collection.</p>
         * @implSpec <p>The default implementation checks whether the runtime type of the
         * specified collection is an instance of {@link OfInt}, and if so
         * passes it to {@link #containsAll(OfInt)}. Otherwise, it is equivalent to
         * {@code c.stream().allMatch(this::contains)}.</p>
         * @param c collection to be checked for containment in this collection
         * @return {@code true} if this collection contains all elements in the
         * specified collection
         * @throws ClassCastException if the types of one or more elements in the
         * specified collection are incompatible with this collection (optional)
         * @throws NullPointerException if the specified collection contains one or
         * more null elements (optional) or if the specified collection is null.
         * @see #contains(Object)
         */
        default boolean containsAll(Collection<?> c) {
            if (c instanceof OfInt ofInt)
                return containsAll(ofInt);
            return c.stream().allMatch(this::contains);
        }

        /**
         * <p>Returns {@code true} if this collection contains all of the elements in
         * the specified collection.</p>
         * @implSpec <p>The default implementation is equivalent to
         * {@code c.primitiveStream().allMatch(this::containsInt)}.</p>
         * @param c collection to be checked for containment in this collection
         * @return {@code true} if this collection contains all elements in the
         * specified collection
         * @throws NullPointerException if the specified collection is null.
         * @see #containsAll(Collection)
         * @see #containsInt(int)
         */
        default boolean containsAll(OfInt c) {
            return c.primitiveStream().allMatch(this::containsInt);
        }

        /**
         * <p>Returns {@code true} if this collection contains the specified
         * element. More formally, returns {@code true} if and only if
         * this collection contains at least one element {@code e} such that
         * {@code i == e}.</p>
         * @implSpec <p>The default implementation is equivalent to
         * {@code primitiveStream().anyMatch(e -> e == i)}</p>
         * @param i element whose presence in this collection is to be tested
         * @return {@code true} if this collection contains the specified
         * element
         * @see #contains(Object)
         * @see OfDouble#containsDouble(double)
         * @see OfLong#containsLong(long)
         */
        default boolean containsInt(int i) {
            return primitiveStream().anyMatch(e -> e == i);
        }

        /**
         * Returns an empty {@code PrimitiveCollection.OfInt}. The returned class is
         * immutable and may safely be shared between threads.
         * @return an empty int collection
         */
        static OfInt empty() {
            return EmptyPrimitiveCollection.OfInt.INSTANCE;
        }

        /**
         * Returns a primitive iterator over the elements in this collection. There
         * are no guarantees concerning the order in which the elements are returned
         * (unless this collection is an instance of some class that provides a
         * guarantee).
         * @return a {@code PrimitiveIterator.OfInt} over the elements in this collection
         */
        PrimitiveIterator.OfInt iterator();

        /**
         * <p>Returns a possibly parallel {@code IntStream} with this
         * collection as its source. It is allowable for this method to return a
         * sequential double stream.</p>
         * <p>This method should be overridden when the {@link #spliterator()}
         * method cannot return a spliterator that is {@code IMMUTABLE},
         * {@code CONCURRENT}, or <em>late-binding</em>. (See
         * {@link #spliterator()} for details.)</p>
         * @implSpec <p>The default implementation creates a parallel
         * {@code IntStream} from the collection's
         * {@code Spliterator.OfInt}.</p>
         * @return a possibly parallel {@code IntStream} over the elements
         * in this collection
         */
        default IntStream parallelPrimitiveStream() {
            return StreamSupport.intStream(spliterator(), true);
        }

        /**
         * <p>Returns a sequential {@code IntStream} with this
         * collection as its source.</p>
         * <p>This method should be overridden when the {@link #spliterator()} method
         * cannot return a spliterator that is {@code IMMUTABLE}, {@code CONCURRENT},
         * or <em>late-binding</em>. (See {@link #spliterator()} for detains.)</p>
         * @implSpec <p>The default implementation creates a sequential
         * {@code IntStream} from the collection's
         * {@code Spliterator.OfInt}.</p>
         * @return a sequential {@code IntStream} over the elements in
         * this collection
         */
        default IntStream primitiveStream() {
            return StreamSupport.intStream(spliterator(), false);
        }

        /**
         * <p>Removes a single instance of the specified element from this collection,
         * if it is present (optional operation). More formally, removes an element
         * {@code e} such that {@code Objects.equals(o, e)}, if this collection
         * contains one or more such elements. Returns {@code true} if this collection
         * contained the specified element (or equivalently, if this collection changed
         * as a result of the call).</p>
         * <p>Implementations may optionally return {@code false} when the specified
         * element is not present in the collection, even if the operation would
         * otherwise be unsupported.</p>
         * @implSpec <p>The default implementation checks whether the specified element is an
         * {@code Integer}, and if so passes it to {@link #removeInt(int)};
         * otherwise it returns {@code false}.</p>
         * @param o element to be removed from this collection, if present
         * @return {@code true} if an element was removed as a result of this call
         * @throws ClassCastException if the type of the specified element is
         * incompatible with this collection (optional)
         * @throws NullPointerException if the specified element is null (optional)
         * @throws UnsupportedOperationException if the {@code remove} operation is
         * not supported by this collection
         */
        default boolean remove(Object o) {
            return o instanceof Integer i && removeInt(i);
        }

        /**
         * <p>Removes all of this collection's elements that are also contained in the
         * specified collection (optional operation). After this call returns, this
         * collection will contain no elements in common with the specified
         * collection.</p>
         * <p>Implementations may optionally return {@code false} when this collection
         * contains no elements in common with the specified collection, even if the
         * operation would otherwise be unsupported; however, if any element present in
         * the specified collection cannot be removed from this collection, an
         * exception <em>must</em> be thrown to preserve the invariant that the
         * collections will contain no elements in common after the call.</p>
         * @implSpec <p>The default implementation checks the runtime type of the specified
         * collection to determine whether it is an instance of {@link OfInt},
         * and if so passes it to {@link #removeAll(OfInt)}; otherwise it is
         * equivalent to {@code removeIf(c::contains)}.</p>
         * @param c collection containing elements to be removed from this collection
         * @return {@code true} if this collection changed as a result of the call
         * @throws UnsupportedOperationException if the {@code removeAll} operation
         * is not supported by this collection
         * @throws ClassCastException if the types of one or more elements in this
         * collection are incompatible with the specified collection (optional)
         * @throws NullPointerException if this collection contains one or more
         * null elements (optional) or if the specified collection is null
         */
        default boolean removeAll(Collection<?> c) {
            if (c instanceof OfInt ofInt)
                return removeAll(ofInt);
            return removeIf((IntPredicate) c::contains);
        }

        /**
         * <p>Removes all of this collection's elements that are also contained in the
         * specified collection (optional operation). After this call returns, this
         * collection will contain no elements in common with the specified
         * collection.</p>
         * <p>Implementations may optionally return {@code false} when this collection
         * contains no elements in common with the specified collection, even if the
         * operation would otherwise be unsupported; however, if any element present in
         * the specified collection cannot be removed from this collection, an
         * exception <em>must</em> be thrown to preserve the invariant that the
         * collections will contain no elements in common after the call.</p>
         * @implSpec <p>The default implementation is equivalent to
         * {@code removeIf(c::containsInt)}.</p>
         * @param c collection containing elements to be removed from this collection
         * @return {@code true} if this collection changed as a result of the call
         * @throws UnsupportedOperationException if the {@code removeAll} operation
         * is not supported by this collection
         * @throws NullPointerException if this collection is null
         */
        default boolean removeAll(OfInt c) {
            return removeIf((IntPredicate) c::containsInt);
        }

        /**
         * <p>Removes all of the elements of this collection that satisfy the given
         * predicate (optional operation). Errors or runtime exceptions thrown
         * during iteration or my the predicate are relayed to the caller.</p>
         * @implSpec <p>The default implementation obtains an iterator over this
         * collection, passes each element returned to the predicate, and calls
         * the iterator's {@code remove} method if the predicate returns
         * {@code true}.</p>
         * @param filter a predicate which returns {@code true} for elements to be
         *               removed
         * @return {@code true} if any elements were removed
         * @throws NullPointerException if the specified filter is null
         * @throws UnsupportedOperationException if the {@code removeIf} operation
         * is not supported by this collection
         * @see #removeIf(Predicate)
         */
        default boolean removeIf(IntPredicate filter) {
            var changed = false;
            var iterator = iterator();
            while (iterator.hasNext())
                if (filter.test(iterator.nextInt())) {
                    iterator.remove();
                    changed = true;
                }
            return changed;
        }

        /**
         * <p>Removes all of the elements of this collection that satisfy the given
         * predicate (optional operation). Errors or runtime exceptions thrown
         * during iteration or by the predicate are relayed to the caller.</p>
         * @implSpec <p>The default implementation checks the runtime type of the predicate
         * to determine if it is an instance of {@code IntPredicate}, and if so
         * passes it to {@link #removeIf(IntPredicate)}; otherwise, it uses
         * the default implementation inherited from {@link Collection}.</p>
         * @param filter a predicate which returns {@code true} for elements to be
         *               removed
         * @return {@code true} if any elements were removed
         * @throws NullPointerException if the specified filter is null
         * @throws UnsupportedOperationException if the {@code removeIf} operation
         * is not supported by this collection
         * @see Collection#removeIf(Predicate)
         */
        default boolean removeIf(Predicate<? super Integer> filter) {
            if (filter instanceof IntPredicate intPredicate)
                return removeIf(intPredicate);
            return PrimitiveCollection.super.removeIf(filter);
        }

        /**
         * <p>Removes a single instance of the specified element from this collection,
         * if it is present (optional operation). More formally, removes an element
         * {@code e} such that {@code d == e}, if this collection
         * contains one or more such elements. Returns {@code true} if this collection
         * contained the specified element (or equivalently, if this collection changed
         * as a result of the call).</p>
         * <p>Implementations may optionally return {@code false} when the specified
         * element is not present in the collection, even if the operation would
         * otherwise be unsupported.</p>
         * @implSpec <p>The default implementation obtains an iterator over the collection,
         * and checks each returned element until it finds one equal to the specified
         * element, in which even it calls the iterator's
         * {@link Iterator#remove() remove} method (propagating any resulting
         * {@code UnsupportedOperationException}) and returns {@code true}; if the end
         * of the iteration is reached without finding an element equal to the
         * specified element it returns {@code false}.</p>
         * @param i element to be removed from this collection, if present
         * @return {@code true} if an element was removed as a result of this call
         * @throws UnsupportedOperationException if the {@code remove} operation is
         * not supported by this collection
         */
        default boolean removeInt(int i) {
            var iterator = iterator();
            while (iterator.hasNext())
                if (iterator.nextInt() == i) {
                    iterator.remove();
                    return true;
                }
            return false;
        }

        /**
         * <p>Retains only the elements in this collection that are contained in the
         * specified collection (optional operation). In other words, removes from
         * this collection all of its elements that are not contained in the
         * specified collection.</p>
         * @implSpec <p>The default implementation checks the runtime type of the collection
         * to determine if it is an instance of {@code OfInt}, in which case it
         * passes it to {@link #retainAll(OfInt)}; otherwise it is equivalent to
         * {@code removeIf(t -> !c.contains(t))}.</p>
         * @param c collection containing elements to be retained in this collection
         * @return {@code true} if this collection changed as a result of the call
         * @throws UnsupportedOperationException if the {@code retainAll} operation is
         * not supported by this collection
         * @throws ClassCastException if the types of one or more elements in this
         * collection are incompatible with the specified collection (optional)
         * @throws NullPointerException if the specified collection is null
         * @see #remove(Object)
         * @see #contains(Object)
         */
        default boolean retainAll(Collection<?> c) {
            if (c instanceof OfInt ofInt)
                return retainAll(ofInt);
            return PrimitiveCollection.super.retainAll(c);
        }

        /**
         * <p>Retains only the elements in this collection that are contained in the
         * specified collection (optional operation). In other words, removes from
         * this collection all of its elements that are not contained in the
         * specified collection.</p>
         * @implSpec <p>The default implementation is equivalent to
         * {@code removeIf(t -> !c.containsInt(t))}.</p>
         * @param c collection containing elements to be retained in this collection
         * @return {@code true} if this collection changed as a result of the call
         * @throws UnsupportedOperationException if the {@code retainAll} operation is
         * not supported by this collection
         * @throws NullPointerException if the specified collection is null
         * @see #containsInt(int)
         */
        default boolean retainAll(OfInt c) {
            return removeIf(((IntPredicate) c::containsInt).negate());
        }

        /**
         * <p>Creates a {@code Spliterator.OfInt} over the elements in this
         * collection. Implementations should document characteristic values
         * reported by the spliterator. Such characteristic values are not
         * required to be reported if the spliterator reports
         * {@link Spliterator#SIZED} and this collection contains no elements.</p>
         * <p>The default implementation should be overridden by subclasses that can
         * return a more efficient spliterator. In order to preserve expected
         * laziness behavior for the {@link #stream()}, {@link #primitiveStream()},
         * {@link #parallelPrimitiveStream()} and {@link #parallelStream}
         * methods, spliterators should either have the characteristic of
         * {@code IMMUTABLE} or {@code CONCURRENT}, or be late-binding. If none of
         * these is practical, the overriding class should describe the spliterator's
         * documented policy of binding and structural interference, and should
         * override the {@link #stream()}, {@link #primitiveStream()},
         * {@link #parallelPrimitiveStream()} and {@link #parallelStream()} methods to
         * create streams using a {@code Supplier} of the spliterator, as in:</p>
         * <code>Stream&lt;E&gt; s = StreamSupport.stream(() -> spliterator(), spliteratorCharacteristics)</code>
         * <p>These requirements ensure that streams produced by
         * these methods will reflect the contents of the
         * collection as of initiation of the terminal stream operation.</p>
         * @implSpec <p>The default implementation creates a <em>late-binding</em>
         * spliterator from the collection's {@code PrimitiveIterator.OfInt}.
         * The spliterator inherits the <em>fail-fast</em> properties of the
         * collection's iterator. The created {@code Spliterator.OfInt} reports
         * {@link Spliterator#SIZED}.</p>
         * @implNote <p>The created {@code Spliterator.OfInt} additionally
         * reports {@link Spliterator#SUBSIZED}. If a spliterator covers no elements
         * then the reporting of additional characteristic values, beyond that of
         * {@code SIZED} and {@code SUBSIZED}, does not aid clients to control,
         * specialize or simplify computation. However, this does enable shared use
         * of an immutable and empty spliterator instance (see
         * {@link Spliterators#emptyIntSpliterator()}) for empty collections, and
         * enables clients to determine if such a spliterator covers no elements.</p>
         * @return a {@code Spliterator.OfInt} over the elements in this collection
         */
        default Spliterator.OfInt spliterator() {
            return new PrimitiveCollectionSpliterator.OfInt(this, 0);
        }

        /**
         * <p>Returns an array containing all of the elements in this collection. If
         * this collection makes any guarantees as to what order its elements are
         * returned by its iterator, this method must return the elements in the
         * same order.</p>
         * <p>The returned array will be "safe" in that no references to it are
         * maintained by this collection. (In other words, this method must allocate
         * a new array even if this collection is backed by an array). The caller is
         * thus free to modify the returned array.</p>
         * @implSpec <p>The default implementation is based on
         * {@link java.util.AbstractCollection}, differing only in terms of the
         * runtime type of the array. (See {@link #toArray()} for details.)</p>
         * @return an array containing all of the elements in this collection
         */
        default int[] toPrimitiveArray() {
            return PrimitiveCollections.toPrimitiveArray(this);
        }

        /**
         * <p>Returns a collection view over the specified collection. This view will
         * not permit any operations which modify the collection.</p>
         * <p>The returned view is backed by the collection, so any changes to the
         * backing collection will be visible in the returned collection.</p>
         * @param coll collection to create an unmodifiable view over
         * @return an unmodifiable view over the specified collection.
         */
        static PrimitiveCollection.OfInt unmodifiableCollection(PrimitiveCollection.OfInt coll) {
            return ForwardingPrimitiveCollection.OfInt.unmodifiable(coll);
        }
    }

    /**
     * <p>A Collection specialized for {@code long} values.</p>
     */
    @PrereleaseContent
    interface OfLong extends PrimitiveCollection<Long,long[],LongConsumer,
            LongPredicate,Spliterator.OfLong,LongStream> {
        /**
         * <p>Ensures that this collection contains the specified element (optional
         * operation). Returns {@code true} if this collection changed as a result of
         * the call. (Returns {@code false} if this collection does not permit
         * duplicates and already contains the specified element.)</p>
         * <p>If a collection refuses to add a particular element for any reason
         * other than that it already contains the element, it <em>must</em> throw
         * an exception (rather than returning {@code false}). This preserves the
         * invariant that a collection always contains the specified element after
         * this call returns.</p>
         * @implSpec <p>The default implementation is equivalent to
         * {@code addLong(t.longValue())}.</p>
         * @param t element whose presence in this collection is to be ensured
         * @return {@code true} if this collection changed as a result of the call
         * @throws UnsupportedOperationException if the {@code add} operation is not
         * supported by this collection
         * @throws NullPointerException if the specified element is null
         * @throws IllegalArgumentException if some property of this element prevents
         * it from being added to this collection
         * @throws IllegalStateException if the element cannot be added at this time
         * due to insertion restrictions
         */
        default boolean add(Long t) {
            return addLong(t);
        }

        /**
         * <p>Adds all of the elements in the specified collection to this collection
         * (optional operation). The behavior of this operation is undefined if the
         * specified collection is modified while the operation is in progress. (This
         * implies that the behavior of this call is undefined if the specified
         * collection is this collection, and this collection is nonempty.) If the
         * specified collection has a defined encounter order, processing of its
         * elements generally occurs in that order.</p>
         * <p>Optionally, an implementation may return {@code false} when the
         * specified collection contains no elements not already present in this
         * collection, even if the operation would otherwise be unsupported; however,
         * if the specified collection contains any elements not present in this
         * collection that cannot be added to this collection, an exception must be
         * thrown.</p>
         * @implSpec <p>The default implementation will check whether the runtime type of
         * the collection is an instance of {@code PrimitiveCollection.OfLong},
         * and if so will pass it to {@link #addAll(OfLong)}. Otherwise, it
         * iterates over all elements of the specified
         * collection, calling {@code add} with each element, and returns
         * {@code false} only if each invocation of the operation returns
         * {@code false}.</p>
         * @param c collection containing elements to be added to this collection
         * @return {@code true} if this collection changed as a result of the call
         * @throws UnsupportedOperationException if the {@code addAll} operation is
         * not supported by this collection
         * @throws ClassCastException if the class of an element of the specified
         * collection prevents it from being added to this collection
         * @throws NullPointerException if the specified collection contains a null
         * element, or the specified collection is null
         * @throws IllegalArgumentException if some property of an element of the
         * specified collection prevents it from being added to this collection
         * @throws IllegalStateException if not all the elements can be added at
         * this time due to insertion restrictions
         * @see #add(Object)
         * @see #addAll(OfLong)
         */
        default boolean addAll(Collection<? extends Long> c) {
            if (c instanceof OfLong ofLong)
                return addAll(ofLong);
            return PrimitiveCollection.super.addAll(c);
        }

        /**
         * <p>Adds all of the elements in the specified collection to this collection
         * (optional operation). The behavior of this operation is undefined if the
         * specified collection is modified while the operation is in progress. (This
         * implies that the behavior of this call is undefined if the specified
         * collection is this collection, and this collection is nonempty.) If the
         * specified collection has a defined encounter order, processing of its
         * elements generally occurs in that order.</p>
         * <p>Optionally, an implementation may return {@code false} when the
         * specified collection contains no elements not already present in this
         * collection, even if the operation would otherwise be unsupported; however,
         * if the specified collection contains any elements not present in this
         * collection that cannot be added to this collection, an exception must be
         * thrown.</p>
         * @implSpec <p>The default implementation iterates over all elements of the specified
         * collection, calling {@code addLong} with each element, and returns
         * {@code false} only if each invocation of the operation returns
         * {@code false}.</p>
         * @param c collection containing elements to be added to this collection
         * @return {@code true} if this collection changed as a result of the call
         * @throws UnsupportedOperationException if the {@code addAll} operation is
         * not supported by this collection
         * @throws NullPointerException if the specified collection is null
         * @throws IllegalArgumentException if some property of an element of the
         * specified collection prevents it from being added to this collection
         * @throws IllegalStateException if not all the elements can be added at
         * this time due to insertion restrictions
         * @see #addAll(Collection)
         * @see #addLong(long)
         */
        default boolean addAll(OfLong c) {
            var changed = false;
            var iterator = c.iterator();
            while (iterator.hasNext())
                changed |= addLong(iterator.nextLong());
            return changed;
        }

        /**
         * <p>Ensures that this collection contains the specified element (optional
         * operation). Returns {@code true} if this collection changed as a result of
         * the call. (Returns {@code false} if this collection does not permit
         * duplicates and already contains the specified element.)</p>
         * <p>If a collection refuses to add a particular element for any reason
         * other than that it already contains the element, it <em>must</em> throw
         * an exception (rather than returning {@code false}). This preserves the
         * invariant that a collection always contains the specified element after
         * this call returns.</p>
         * @implSpec <p>The default implementation always throws
         * {@code UnsupportedOperationException}.</p>
         * @param l element whose presence in this collection is to be ensured
         * @return {@code true} if this collection changed as a result of the call
         * @throws UnsupportedOperationException if the {@code add} operation is not
         * supported by this collection
         * @throws IllegalArgumentException if some property of this element prevents
         * it from being added to this collection
         * @throws IllegalStateException if the element cannot be added at this time
         * due to insertion restrictions
         */
        default boolean addLong(long l) {
            throw new UnsupportedOperationException();
        }

        /**
         * <p>Returns {@code true} if this collection contains the specified element. More
         * formally, returns {@code true} if and only if this collection contains at least
         * one element {@code e} such that {@code Objects.equals(o, e)}.</p>
         * @implSpec <p>The default implementation checks whether the runtime type of the
         * specified element is {@code Long}, and if so passes it to
         * {@link #containsLong(long)}; otherwise, it returns {@code false}.</p>
         * @param o element whose presence in this collection is to be tested
         * @return {@code true} if this collection contains the specified element
         * @throws ClassCastException if the type of the specified element is
         * incompatible with this collection (optional)
         * @throws NullPointerException if the specified element is null (optional)
         */
        default boolean contains(Object o) {
            return o instanceof Long l && containsLong(l);
        }

        /**
         * <p>Returns {@code true} if this collection contains all of the elements in
         * the specified collection.</p>
         * @implSpec <p>The default implementation checks whether the runtime type of the
         * specified collection is an instance of {@link OfLong}, and if so
         * passes it to {@link #containsAll(OfLong)}. Otherwise, it is equivalent to
         * {@code c.stream().allMatch(this::contains)}.</p>
         * @param c collection to be checked for containment in this collection
         * @return {@code true} if this collection contains all elements in the
         * specified collection
         * @throws ClassCastException if the types of one or more elements in the
         * specified collection are incompatible with this collection (optional)
         * @throws NullPointerException if the specified collection contains one or
         * more null elements (optional) or if the specified collection is null.
         * @see #contains(Object)
         */
        default boolean containsAll(Collection<?> c) {
            if (c instanceof OfLong ofLong)
                return containsAll(ofLong);
            return c.stream().allMatch(this::contains);
        }

        /**
         * <p>Returns {@code true} if this collection contains all of the elements in
         * the specified collection.</p>
         * @implSpec <p>The default implementation is equivalent to
         * {@code c.primitiveStream().allMatch(this::containsLong)}.</p>
         * @param c collection to be checked for containment in this collection
         * @return {@code true} if this collection contains all elements in the
         * specified collection
         * @throws NullPointerException if the specified collection is null.
         * @see #containsAll(Collection)
         * @see #containsLong(long)
         */
        default boolean containsAll(OfLong c) {
            return c.primitiveStream().allMatch(this::containsLong);
        }

        /**
         * <p>Returns {@code true} if this collection contains the specified
         * element. More formally, returns {@code true} if and only if
         * this collection contains at least one element {@code e} such that
         * {@code l == e}.</p>
         * @implSpec <p>The default implementation is equivalent to
         * {@code primitiveStream().anyMatch(e -> e == l)}.</p>
         * @param l element whose presence in this collection is to be tested
         * @return {@code true} if this collection contains the specified
         * element
         * @see #contains(Object)
         * @see OfDouble#containsDouble(double)
         * @see OfInt#containsInt(int)
         */
        default boolean containsLong(long l) {
            return primitiveStream().anyMatch(e -> e == l);
        }

        /**
         * Returns an empty {@code PrimitiveCollection.OfLong}. The returned class is
         * immutable and may safely be shared between threads.
         * @return an empty long collection
         */
        static OfLong empty() {
            return EmptyPrimitiveCollection.OfLong.INSTANCE;
        }

        /**
         * Returns a primitive iterator over the elements in this collection. There
         * are no guarantees concerning the order in which the elements are returned
         * (unless this collection is an instance of some class that provides a
         * guarantee).
         * @return a {@code PrimitiveIterator.OfLong} over the elements in this collection
         */
        PrimitiveIterator.OfLong iterator();

        /**
         * <p>Returns a possibly parallel {@code LongStream} with this
         * collection as its source. It is allowable for this method to return a
         * sequential double stream.</p>
         * <p>This method should be overridden when the {@link #spliterator()}
         * method cannot return a spliterator that is {@code IMMUTABLE},
         * {@code CONCURRENT}, or <em>late-binding</em>. (See
         * {@link #spliterator()} for details.)</p>
         * @implSpec <p>The default implementation creates a parallel
         * {@code LongStream} from the collection's
         * {@code Spliterator.OfLong}.</p>
         * @return a possibly parallel {@code LongStream} over the elements
         * in this collection
         */
        default LongStream parallelPrimitiveStream() {
            return StreamSupport.longStream(spliterator(), true);
        }

        /**
         * <p>Returns a sequential {@code LongStream} with this
         * collection as its source.</p>
         * <p>This method should be overridden when the {@link #spliterator()} method
         * cannot return a spliterator that is {@code IMMUTABLE}, {@code CONCURRENT},
         * or <em>late-binding</em>. (See {@link #spliterator()} for detains.)</p>
         * @implSpec <p>The default implementation creates a sequential
         * {@code LongStream} from the collection's
         * {@code Spliterator.OfLong}.</p>
         * @return a sequential {@code LongStream} over the elements in
         * this collection
         */
        default LongStream primitiveStream() {
            return StreamSupport.longStream(spliterator(), false);
        }

        /**
         * <p>Removes a single instance of the specified element from this collection,
         * if it is present (optional operation). More formally, removes an element
         * {@code e} such that {@code Objects.equals(o, e)}, if this collection
         * contains one or more such elements. Returns {@code true} if this collection
         * contained the specified element (or equivalently, if this collection changed
         * as a result of the call).</p>
         * <p>Implementations may optionally return {@code false} when the specified
         * element is not present in the collection, even if the operation would
         * otherwise be unsupported.</p>
         * @implSpec <p>The default implementation checks whether the specified element is a
         * {@code Long}, and if so passes it to {@link #removeLong(long)};
         * otherwise it returns {@code false}.</p>
         * @param o element to be removed from this collection, if present
         * @return {@code true} if an element was removed as a result of this call
         * @throws ClassCastException if the type of the specified element is
         * incompatible with this collection (optional)
         * @throws NullPointerException if the specified element is null (optional)
         * @throws UnsupportedOperationException if the {@code remove} operation is
         * not supported by this collection
         */
        default boolean remove(Object o) {
            return o instanceof Long l && removeLong(l);
        }

        /**
         * <p>Removes all of this collection's elements that are also contained in the
         * specified collection (optional operation). After this call returns, this
         * collection will contain no elements in common with the specified
         * collection.</p>
         * <p>Implementations may optionally return {@code false} when this collection
         * contains no elements in common with the specified collection, even if the
         * operation would otherwise be unsupported; however, if any element present in
         * the specified collection cannot be removed from this collection, an
         * exception <em>must</em> be thrown to preserve the invariant that the
         * collections will contain no elements in common after the call.</p>
         * @implSpec <p>The default implementation checks the runtime type of the specified
         * collection to determine whether it is an instance of {@link OfLong},
         * and if so passes it to {@link #removeAll(OfLong)}; otherwise it is
         * equivalent to {@code removeIf(c::contains)}.</p>
         * @param c collection containing elements to be removed from this collection
         * @return {@code true} if this collection changed as a result of the call
         * @throws UnsupportedOperationException if the {@code removeAll} operation
         * is not supported by this collection
         * @throws ClassCastException if the types of one or more elements in this
         * collection are incompatible with the specified collection (optional)
         * @throws NullPointerException if this collection contains one or more
         * null elements (optional) or if the specified collection is null
         */
        default boolean removeAll(Collection<?> c) {
            if (c instanceof OfLong ofLong)
                return removeAll(ofLong);
            return removeIf((LongPredicate) c::contains);
        }

        /**
         * <p>Removes all of this collection's elements that are also contained in the
         * specified collection (optional operation). After this call returns, this
         * collection will contain no elements in common with the specified
         * collection.</p>
         * <p>Implementations may optionally return {@code false} when this collection
         * contains no elements in common with the specified collection, even if the
         * operation would otherwise be unsupported; however, if any element present in
         * the specified collection cannot be removed from this collection, an
         * exception <em>must</em> be thrown to preserve the invariant that the
         * collections will contain no elements in common after the call.</p>
         * @implSpec <p>The default implementation is equivalent to
         * {@code removeIf(c::containsLong)}.</p>
         * @param c collection containing elements to be removed from this collection
         * @return {@code true} if this collection changed as a result of the call
         * @throws UnsupportedOperationException if the {@code removeAll} operation
         * is not supported by this collection
         * @throws NullPointerException if this collection is null
         */
        default boolean removeAll(OfLong c) {
            return removeIf((LongPredicate) c::containsLong);
        }

        /**
         * <p>Removes all of the elements of this collection that satisfy the given
         * predicate (optional operation). Errors or runtime exceptions thrown
         * during iteration or my the predicate are relayed to the caller.</p>
         * @implSpec <p>The default implementation obtains an iterator over this
         * collection, passes each element returned to the predicate, and calls
         * the iterator's {@code remove} method if the predicate returns
         * {@code true}.</p>
         * @param filter a predicate which returns {@code true} for elements to be
         *               removed
         * @return {@code true} if any elements were removed
         * @throws NullPointerException if the specified filter is null
         * @throws UnsupportedOperationException if the {@code removeIf} operation
         * is not supported by this collection
         * @see #removeIf(Predicate)
         */
        default boolean removeIf(LongPredicate filter) {
            var changed = false;
            var iterator = iterator();
            while (iterator.hasNext())
                if (filter.test(iterator.nextLong())) {
                    iterator.remove();
                    changed = true;
                }
            return changed;
        }

        /**
         * <p>Removes all of the elements of this collection that satisfy the given
         * predicate (optional operation). Errors or runtime exceptions thrown
         * during iteration or by the predicate are relayed to the caller.</p>
         * @implSpec <p>The default implementation checks the runtime type of the predicate
         * to determine if it is an instance of {@code LongPredicate}, and if so
         * passes it to {@link #removeIf(LongPredicate)}; otherwise, it uses
         * the default implementation inherited from {@link Collection}.</p>
         * @param filter a predicate which returns {@code true} for elements to be
         *               removed
         * @return {@code true} if any elements were removed
         * @throws NullPointerException if the specified filter is null
         * @throws UnsupportedOperationException if the {@code removeIf} operation
         * is not supported by this collection
         * @see Collection#removeIf(Predicate)
         */
        default boolean removeIf(Predicate<? super Long> filter) {
            if (filter instanceof LongPredicate longPredicate)
                return removeIf(longPredicate);
            return PrimitiveCollection.super.removeIf(filter);
        }

        /**
         * <p>Removes a single instance of the specified element from this collection,
         * if it is present (optional operation). More formally, removes an element
         * {@code e} such that {@code d == e}, if this collection
         * contains one or more such elements. Returns {@code true} if this collection
         * contained the specified element (or equivalently, if this collection changed
         * as a result of the call).</p>
         * <p>Implementations may optionally return {@code false} when the specified
         * element is not present in the collection, even if the operation would
         * otherwise be unsupported.</p>
         * @implSpec <p>The default implementation obtains an iterator over the collection,
         * and checks each returned element until it finds one equal to the specified
         * element, in which even it calls the iterator's
         * {@link Iterator#remove() remove} method (propagating any resulting
         * {@code UnsupportedOperationException}) and returns {@code true}; if the end
         * of the iteration is reached without finding an element equal to the
         * specified element it returns {@code false}.</p>
         * @param l element to be removed from this collection, if present
         * @return {@code true} if an element was removed as a result of this call
         * @throws UnsupportedOperationException if the {@code remove} operation is
         * not supported by this collection
         */
        default boolean removeLong(long l) {
            var iterator = iterator();
            while (iterator.hasNext())
                if (iterator.nextLong() == l) {
                    iterator.remove();
                    return true;
                }
            return false;
        }

        /**
         * <p>Retains only the elements in this collection that are contained in the
         * specified collection (optional operation). In other words, removes from
         * this collection all of its elements that are not contained in the
         * specified collection.</p>
         * @implSpec <p>The default implementation checks the runtime type of the collection
         * to determine if it is an instance of {@code OfLong}, in which case it
         * passes it to {@link #retainAll(OfLong)}; otherwise it is equivalent to
         * {@code removeIf(t -> !c.contains(t))}.</p>
         * @param c collection containing elements to be retained in this collection
         * @return {@code true} if this collection changed as a result of the call
         * @throws UnsupportedOperationException if the {@code retainAll} operation is
         * not supported by this collection
         * @throws ClassCastException if the types of one or more elements in this
         * collection are incompatible with the specified collection (optional)
         * @throws NullPointerException if the specified collection is null
         * @see #remove(Object)
         * @see #contains(Object)
         */
        default boolean retainAll(Collection<?> c) {
            if (c instanceof OfLong ofLong)
                return retainAll(ofLong);
            return PrimitiveCollection.super.retainAll(c);
        }

        /**
         * <p>Retains only the elements in this collection that are contained in the
         * specified collection (optional operation). In other words, removes from
         * this collection all of its elements that are not contained in the
         * specified collection.</p>
         * @implSpec <p>The default implementation is equivalent to
         * {@code removeIf(t -> !c.containsLong(t))}.</p>
         * @param c collection containing elements to be retained in this collection
         * @return {@code true} if this collection changed as a result of the call
         * @throws UnsupportedOperationException if the {@code retainAll} operation is
         * not supported by this collection
         * @throws NullPointerException if the specified collection is null
         * @see #containsLong(long)
         */
        default boolean retainAll(OfLong c) {
            return removeIf(((LongPredicate) c::containsLong).negate());
        }

        /**
         * <p>Creates a {@code Spliterator.OfLong} over the elements in this
         * collection. Implementations should document characteristic values
         * reported by the spliterator. Such characteristic values are not
         * required to be reported if the spliterator reports
         * {@link Spliterator#SIZED} and this collection contains no elements.</p>
         * <p>The default implementation should be overridden by subclasses that can
         * return a more efficient spliterator. In order to preserve expected
         * laziness behavior for the {@link #stream()}, {@link #primitiveStream()},
         * {@link #parallelPrimitiveStream()} and {@link #parallelStream}
         * methods, spliterators should either have the characteristic of
         * {@code IMMUTABLE} or {@code CONCURRENT}, or be late-binding. If none of
         * these is practical, the overriding class should describe the spliterator's
         * documented policy of binding and structural interference, and should
         * override the {@link #stream()}, {@link #primitiveStream()},
         * {@link #parallelPrimitiveStream()} and {@link #parallelStream()} methods to
         * create streams using a {@code Supplier} of the spliterator, as in:</p>
         * <code>Stream&lt;E&gt; s = StreamSupport.stream(() -> spliterator(), spliteratorCharacteristics)</code>
         * <p>These requirements ensure that streams produced by these
         * methods will reflect the contents of the
         * collection as of initiation of the terminal stream operation.</p>
         * @implSpec <p>The default implementation creates a <em>late-binding</em>
         * spliterator from the collection's {@code PrimitiveIterator.OfLong}.
         * The spliterator inherits the <em>fail-fast</em> properties of the
         * collection's iterator. The created {@code Spliterator.OfLong} reports
         * {@link Spliterator#SIZED}.</p>
         * @implNote <p>The created {@code Spliterator.OfLong} additionally
         * reports {@link Spliterator#SUBSIZED}. If a spliterator covers no elements
         * then the reporting of additional characteristic values, beyond that of
         * {@code SIZED} and {@code SUBSIZED}, does not aid clients to control,
         * specialize or simplify computation. However, this does enable shared use
         * of an immutable and empty spliterator instance (see
         * {@link Spliterators#emptyLongSpliterator()}) for empty collections, and
         * enables clients to determine if such a spliterator covers no elements.</p>
         * @return a {@code Spliterator.OfLong} over the elements in this collection
         */
        default Spliterator.OfLong spliterator() {
            return new PrimitiveCollectionSpliterator.OfLong(this, 0);
        }

        /**
         * <p>Returns an array containing all of the elements in this collection. If
         * this collection makes any guarantees as to what order its elements are
         * returned by its iterator, this method must return the elements in the
         * same order.</p>
         * <p>The returned array will be "safe" in that no references to it are
         * maintained by this collection. (In other words, this method must allocate
         * a new array even if this collection is backed by an array). The caller is
         * thus free to modify the returned array.</p>
         * @implSpec <p>The default implementation is based on
         * {@link java.util.AbstractCollection}, differing only in terms of the
         * runtime type of the array. (See {@link #toArray()} for details.)</p>
         * @return an array containing all of the elements in this collection
         */
        default long[] toPrimitiveArray() {
            return PrimitiveCollections.toPrimitiveArray(this);
        }

        /**
         * <p>Returns a collection view over the specified collection. This view will
         * not permit any operations which modify the collection.</p>
         * <p>The returned view is backed by the collection, so any changes to the
         * backing collection will be visible in the returned collection.</p>
         * @param coll collection to create an unmodifiable view over
         * @return an unmodifiable view over the specified collection.
         */
        static PrimitiveCollection.OfLong unmodifiableCollection(PrimitiveCollection.OfLong coll) {
            return ForwardingPrimitiveCollection.OfLong.unmodifiable(coll);
        }
    }
}
