package net.arinoru.util;

import net.arinoru.annotation.Unstable;

import java.util.Collection;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.stream.BaseStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * <p>A base type for primitive specializations of {@code Collection}. Specialized
 * subtypes are provided for {@code int}, {@code long} and {@code double}
 * values.</p>
 * <p>The specialized subtype default implementations for various
 * {@code Collection} methods rely on boxing and unboxing primitive values to and
 * from instances of their corresponding wrapper class. Such boxing may offset any
 * advantages gained when using the primitive specializations. To avoid boxing and
 * unboxing, the corresponding primitive-based methods should be used. For example,
 * {@link IntCollection#containsInt(int)} and {@link IntCollection#removeInt(int)}
 * should be used in favor of {@link IntCollection#contains(Object)} and
 * {@link IntCollection#remove(Object)}.</p>
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
 * @param <T_COLL> the type of the collection. This should be the specific
 *                specialization collection type for {@code T}, such as
 *                {@link IntCollection} for {@code Integer}. More specific collection
 *                types like sets or lists will use the underlying collection
 *                interface type here for the sake of supporting generic
 *                operations on collections, such as creating a set copy of a
 *                collection, or confirming set membership of elements in a
 *                collection.
 */
@Unstable
public interface PrimitiveCollection<T,T_ARR,T_CONS,T_PRED,
        T_ITER extends PrimitiveIterator<T,T_CONS>,
        T_SPLITR extends Spliterator.OfPrimitive<T,T_CONS,T_SPLITR>,
        T_STR extends BaseStream<T,T_STR>,
        T_COLL extends PrimitiveCollection<T,T_ARR,T_CONS,T_PRED,T_ITER,T_SPLITR,T_STR,T_COLL>>
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
     * @param element element whose presence in this collection is to be ensured
     * @return {@code true} if this collection changed as a result of the call
     * @throws UnsupportedOperationException if the {@code add} operation is not
     * supported by this collection
     * @throws ClassCastException if the class of the specified element prevents
     * it from being added to this collection
     * @throws NullPointerException if the specified element is null
     * @throws IllegalArgumentException if some property of the element prevents
     * it from being added to this collection
     * @throws IllegalStateException if the element cannot be added at this time
     * due to insertion restrictions
     */
    boolean add(T element);

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
     * @param collection collection containing elements to be added to this
     *                  collection
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
     * @see #add(Object) add(T)
     */
    boolean addAll(Collection<? extends T> collection);

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
     * @param collection collection containing elements to be added to this
     *                  collection
     * @return {@code true} if this collection changed as a result of the call
     * @throws UnsupportedOperationException if the {@code addAll} operation is
     * not supported by this collection
     * @throws NullPointerException if the specified collection is null
     * @throws IllegalArgumentException if some property of an element of the
     * specified collection prevents it from being added to this collection
     * @throws IllegalStateException if not all the elements can be added at
     * this time due to insertion restrictions
     */
    boolean addAll(T_COLL collection);

    /**
     * <p>Removes all of the elements from this collection (optional operation).
     * The collection will be empty after this method returns.</p>
     * @throws UnsupportedOperationException if the {@code clear} operation is
     * not supported by this collection
     */
    void clear();

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
     * @param collection collection to be checked for containment in this collection
     * @return {@code true} if this collection contains all elements in the
     * specified collection
     * @throws ClassCastException if the types of one or more elements in the
     * specified collection are incompatible with this collection (optional)
     * @throws NullPointerException if the specified collection contains one or
     * more null elements (optional) or if the specified collection is null.
     * @see #contains(Object)
     * @see #containsAll(PrimitiveCollection)
     */
    boolean containsAll(Collection<?> collection);

    /**
     * <p>Returns {@code true} if this collection contains all of the elements in
     * the specified collection.</p>
     * @param collection collection to be checked for containment in this collection
     * @return {@code true} if this collection contains all elements in the
     * specified collection
     * @throws NullPointerException if the specified collection is null.
     * @see #containsAll(Collection)
     */
    boolean containsAll(T_COLL collection);

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
     * <p>Performs the given action for each element of the collection
     * until all elements have been processed or the action throws an exception.
     * Actions are performed in the order of iteration, if that order is
     * specified. Exceptions thrown by the action are relayed to the caller.</p>
     * <p>The behavior of this method is unspecified if the action performs
     * side-effects that modify the underlying source of elements, unless an
     * overriding class has specified a concurrent modification policy.</p>
     * @implSpec <p>The default implementation obtains a spliterator for
     * the collection and passes the specified action to the
     * {@link Spliterator#forEachRemaining(Consumer)} method.</p>
     * @param action action to perform on each element of the collection
     * @throws NullPointerException if the specified action is null
     * @see #forEach(Object) forEach(T_CONS)
     * @see Spliterator#forEachRemaining(Consumer)
     */
    default void forEach(Consumer<? super T> action) {
        spliterator().forEachRemaining(action);
    }

    /**
     * <p>Performs the given action for each element of the collection
     * until all elements have been processed or the action throws an exception.
     * Actions are performed in the order of iteration, if that order is
     * specified. Exceptions thrown by the action are relayed to the caller.</p>
     * <p>The behavior of this method is unspecified if the action performs
     * side-effects that modify the underlying source of elements, unless an
     * overriding class has specified a concurrent modification policy.</p>
     * @implSpec <p>The default implementation obtains a spliterator for
     * the collection and passes the specified action to the
     * {@link Spliterator.OfPrimitive#forEachRemaining(Object) forEachRemaining(T_CONS)} method.</p>
     * @param action action to perform on each element of the collection
     * @throws NullPointerException if the specified action is null
     * @see #forEach(Consumer)
     * @see Spliterator.OfPrimitive#forEachRemaining(Object) forEachRemaining(T_CONS)
     */
    default void forEach(T_CONS action) {
        spliterator().forEachRemaining(action);
    }

    /**
     * <p>Returns the hash code value for this collection. While the
     * {@code PrimitiveCollection} interface adds no stipulations to the
     * general contract for the {@code Object.hashCode} method, programmers
     * should take note that any class that overrides the {@code Object.equals}
     * method must also override the {@code Object.hashCode} method in order to
     * satisfy the general contract for the {@code Object.hashCode} method. In
     * particular, {@code c1.equals(c2)} implies that
     * {@code c1.hashCode()==c2.hashCode()}. Programmers should take further
     * note that as with {@link Collection}, a primitive collection may likely
     * implement further interfaces with equality contract stipulations, including
     * those that may be mutually exclusive (e.g. list vs. set). Because of this,
     * collections which do not implement a further subtype with value equality
     * stipulations are recommended to rely on reference equality (as inherited from
     * {@code Object.equals} and {@code Object.hashCode}) rather than overriding
     * these methods.</p>
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
    T_ITER iterator();

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
     * <p>This method should be overridden by an implementing class when the
     * {@link #spliterator()} method cannot return a spliterator that is
     * {@code IMMUTABLE}, {@code CONCURRENT}, or <em>late-binding</em>. (See
     * {@link #spliterator()} for details.) Otherwise, any primitive type
     * specialization of this interface should provide an acceptable default
     * implementation.</p>
     * @return a possibly parallel primitive stream over the elements
     * in this collection
     */
    T_STR parallelPrimitiveStream();

    /**
     * <p>Returns a sequential primitive stream with this collection as
     * its source.</p>
     * <p>This method should be overridden by an implementing class when the
     * {@link #spliterator()} method cannot return a spliterator that is
     * {@code IMMUTABLE}, {@code CONCURRENT}, or <em>late-binding</em>. (See
     * {@link #spliterator()} for details.) Otherwise, any primitive type
     * specialization of this interface should provide an acceptable default
     * implementation.</p>
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
     * @param collection collection containing elements to be removed from this
     *                  collection
     * @return {@code true} if this collection changed as a result of the call
     * @throws UnsupportedOperationException if the {@code removeAll} operation
     * is not supported by this collection
     * @throws ClassCastException if the types of one or more elements in this
     * collection are incompatible with the specified collection (optional)
     * @throws NullPointerException if this collection contains one or more
     * null elements (optional) or if the specified collection is null
     */
    boolean removeAll(Collection<?> collection);

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
     * @param collection collection containing elements to be removed from this
     *                  collection
     * @return {@code true} if this collection changed as a result of the call
     * @throws UnsupportedOperationException if the {@code removeAll} operation
     * is not supported by this collection
     * @throws NullPointerException if this collection is null
     */
    boolean removeAll(T_COLL collection);

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
     * @param filter a predicate which returns {@code true} for elements to be
     *               removed
     * @return {@code true} if any elements were removed
     * @throws NullPointerException if the specified filter is null
     * @throws UnsupportedOperationException if the {@code removeIf} operation
     * is not supported by this collection
     * @see Collection#removeIf(Predicate)
     */
    boolean removeIf(Predicate<? super T> filter);

    /**
     * <p>Retains only the elements in this collection that are contained in the
     * specified collection (optional operation). In other words, removes from
     * this collection all of its elements that are not contained in the
     * specified collection.</p>
     * @param collection collection containing elements to be retained in this
     *                  collection
     * @return {@code true} if this collection changed as a result of the call
     * @throws UnsupportedOperationException if the {@code retainAll} operation is
     * not supported by this collection
     * @throws ClassCastException if the types of one or more elements in this
     * collection are incompatible with the specified collection (optional)
     * @throws NullPointerException if the specified collection is null
     * @see #contains(Object)
     */
    boolean retainAll(Collection<?> collection);

    /**
     * <p>Retains only the elements in this collection that are contained in the
     * specified collection (optional operation). In other words, removes from
     * this collection all of its elements that are not contained in the
     * specified collection.</p>
     * @param collection collection containing elements to be retained in this
     *                  collection
     * @return {@code true} if this collection changed as a result of the call
     * @throws UnsupportedOperationException if the {@code retainAll} operation is
     * not supported by this collection
     * @throws NullPointerException if the specified collection is null
     */
    boolean retainAll(T_COLL collection);

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
    T_SPLITR spliterator();

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
     * immediately following the end of the collection is set to {@code null}.</p>
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
}
