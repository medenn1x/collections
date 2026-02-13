package net.arinoru.util;

import net.arinoru.annotation.Unstable;

import java.util.Collection;
import java.util.Iterator;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.DoubleConsumer;
import java.util.function.DoublePredicate;
import java.util.function.Predicate;
import java.util.stream.DoubleStream;
import java.util.stream.StreamSupport;

/**
 * <p>A Collection specialized for {@code double} values.</p>
 */
@Unstable
public interface DoubleCollection extends PrimitiveCollection<Double,double[],
        DoubleConsumer,DoublePredicate,PrimitiveIterator.OfDouble,Spliterator.OfDouble,
        DoubleStream,DoubleCollection> {
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
     * <p>Because this method takes boxed values, reliance on it may eliminate any
     * performance advantage obtained by using a primitive collection; where
     * possible, users may wish to utilize the {@link #addDouble(double)} method
     * instead,</p>
     * @implSpec <p>The default implementation is equivalent to
     * {@code addDouble(t.doubleValue())}.</p>
     * @param element element whose presence in this collection is to be ensured
     * @return {@code true} if this collection changed as a result of the call
     * @throws UnsupportedOperationException if the {@code add} operation is not
     * supported by this collection
     * @throws NullPointerException if the specified element is null
     * @throws IllegalArgumentException if some property of this element prevents
     * it from being added to this collection
     * @throws IllegalStateException if the element cannot be added at this time
     * due to insertion restrictions
     */
    default boolean add(Double element) {
        return addDouble(element);
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
     * of the collection is an instance of {@code DoubleCollection},
     * and if so will pass it to {@link #addAll(DoubleCollection)}. Otherwise, it
     * iterates over all elements of the specified
     * collection, calling {@code add} with each element, and returns
     * {@code false} only if each invocation of the operation returns
     * {@code false}.</p>
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
     * @see #add(Double)
     * @see #addAll(DoubleCollection)
     */
    default boolean addAll(Collection<? extends Double> collection) {
        if (collection instanceof DoubleCollection doubles)
            return addAll(doubles);
        var changed = false;
        for (Double e : collection)
            changed |= addDouble(e);
        return changed;
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
     * @see #addAll(Collection)
     * @see #addDouble(double)
     */
    default boolean addAll(DoubleCollection collection) {
        var changed = false;
        var iterator = collection.iterator();
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
     * @param element element whose presence in this collection is to be ensured
     * @return {@code true} if this collection changed as a result of the call
     * @throws UnsupportedOperationException if the {@code add} operation is not
     * supported by this collection
     * @throws IllegalArgumentException if some property of this element prevents
     * it from being added to this collection
     * @throws IllegalStateException if the element cannot be added at this time
     * due to insertion restrictions
     * @see #add(Double)
     */
    default boolean addDouble(double element) {
        throw new UnsupportedOperationException();
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
            iterator.nextDouble();
            iterator.remove();
        }
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
     * specified collection is an instance of {@code  DoubleCollection}, and if so
     * passes it to {@link #containsAll(DoubleCollection)}. Otherwise, it is
     * equivalent to {@code c.stream().allMatch(this::contains)}.</p>
     * @param collection collection to be checked for containment in this
     *                  collection
     * @return {@code true} if this collection contains all elements in the
     * specified collection
     * @throws ClassCastException if the types of one or more elements in the
     * specified collection are incompatible with this collection (optional)
     * @throws NullPointerException if the specified collection contains one or
     * more null elements (optional) or if the specified collection is null.
     * @see #contains(Object)
     */
    default boolean containsAll(Collection<?> collection) {
        if (collection instanceof DoubleCollection doubles)
            return containsAll(doubles);
        return collection.parallelStream().allMatch(this::contains);
    }

    /**
     * <p>Returns {@code true} if this collection contains all of the elements in
     * the specified collection.</p>
     * @implSpec <p>The default implementation is equivalent to
     * {@code c.primitiveStream().allMatch(this::containsDouble)}.</p>
     * @param collection collection to be checked for containment in this collection
     * @return {@code true} if this collection contains all elements in the
     * specified collection
     * @throws NullPointerException if the specified collection is null.
     * @see #containsAll(Collection)
     * @see #containsDouble(double)
     */
    default boolean containsAll(DoubleCollection collection) {
        return collection.parallelPrimitiveStream().allMatch(this::containsDouble);
    }

    /**
     * <p>Returns {@code true} if this collection contains the specified
     * element. More formally, returns {@code true} if and only if
     * this collection contains at least one element {@code e} such that
     * {@code d == e}.</p>
     * @implSpec <p>The default implementation is equivalent to
     * {@code primitiveStream().anyMatch(e -> e == d)}</p>
     * @param element element whose presence in this collection is to be tested
     * @return {@code true} if this collection contains the specified
     * element
     * @see #contains(Object)
     * @see IntCollection#containsInt(int)
     * @see LongCollection#containsLong(long)
     */
    default boolean containsDouble(double element) {
        return parallelPrimitiveStream().anyMatch(d -> d == element);
    }

    /**
     * Returns an empty {@code DoubleCollection}. The returned class is immutable
     * and may safely be shared between threads.
     * @return an empty double collection
     */
    static DoubleCollection empty() {
        return PrimitiveCollections.emptyDoubleCollection();
    }

    /**
     * Returns a primitive iterator over the elements in this collection. There
     * are no guarantees concerning the order in which the elements are returned
     * (unless this collection is an instance of some class that provides a
     * guarantee).
     * @return a {@code PrimitiveIterator.OfDouble} over the elements in this
     * collection
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
     * <p>As this method requires boxing the element to be removed, reliance upon
     * it may eliminate any benefit from using a primitive collection. Developers
     * should prefer using {@link #removeDouble(double)} where possible.</p>
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
     * @implSpec <p>The default implementation checks the runtime type of the
     * specified collection to determine whether it is an instance of
     * {@code DoubleCollection}, and if so passes it to
     * {@link #removeAll(DoubleCollection)}; otherwise it is equivalent to
     * {@code removeIfDouble(collection::contains)}.</p>
     * @param collection collection containing elements to be removed from this
     *                  collection
     * @return {@code true} if this collection changed as a result of the call
     * @throws UnsupportedOperationException if the {@code removeAll} operation
     * is not supported by this collection
     * @throws ClassCastException if the types of one or more elements in this
     * collection are incompatible with the specified collection (optional)
     * @throws NullPointerException if the specified collection is null
     */
    default boolean removeAll(Collection<?> collection) {
        if (collection instanceof DoubleCollection doubles)
            return removeAll(doubles);
        return removeIfDouble(collection::contains);
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
     * {@code removeIfDouble(collection::containsDouble)}.</p>
     * @param collection collection containing elements to be removed from this
     *                  collection
     * @return {@code true} if this collection changed as a result of the call
     * @throws UnsupportedOperationException if the {@code removeAll} operation
     * is not supported by this collection
     * @throws NullPointerException if the specified collection is null
     */
    default boolean removeAll(DoubleCollection collection) {
        return removeIfDouble(collection::containsDouble);
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
     * @param element element to be removed from this collection, if present
     * @return {@code true} if an element was removed as a result of this call
     * @throws UnsupportedOperationException if the {@code remove} operation is
     * not supported by this collection
     */
    default boolean removeDouble(double element) {
        var iterator = iterator();
        while (iterator.hasNext())
            if (iterator.nextDouble() == element) {
                iterator.remove();
                return true;
            }
        return false;
    }

    /**
     * <p>Removes all of the elements of this collection that satisfy the given
     * predicate (optional operation). Errors or runtime exceptions thrown
     * during iteration or by the predicate are relayed to the caller.</p>
     * @apiNote <p>This method exists here to ensure that when a specialized
     * predicate can be used, we don't force the use of a boxed type predicate.
     * In many cases, however, this will actually produce a type ambiguity for
     * overload, requiring the user to cast their method reference or lambda
     * to a particular interface. When this ambiguity is encountered, developers
     * are encouraged to replace this will a call to
     * {@link #removeIfDouble(DoublePredicate)}.</p>
     * @implSpec <p>The default implementation forwards to
     * {@link #removeIfDouble(DoublePredicate)}.</p>
     * @param filter a predicate which returns {@code true} for elements to be
     *               removed
     * @return {@code true} if any elements were removed
     * @throws NullPointerException if the specified filter is null
     * @throws UnsupportedOperationException if the {@code removeIf} operation
     * is not supported by this collection
     * @see #removeIf(Predicate)
     * @see #removeIfDouble(DoublePredicate)
     */
    default boolean removeIf(DoublePredicate filter) {
        return removeIfDouble(filter);
    }

    /**
     * <p>Removes all of the elements of this collection that satisfy the given
     * predicate (optional operation). Errors or runtime exceptions thrown
     * during iteration or by the predicate are relayed to the caller.</p>
     * @implSpec <p>The default implementation implicitly converts the
     * specified predicate into a {@code DoublePredicate} and passes it to
     * {@link #removeIfDouble(DoublePredicate)}.</p>
     * @param filter a predicate which returns {@code true} for elements to be
     *               removed
     * @return {@code true} if any elements were removed
     * @throws NullPointerException if the specified filter is null
     * @throws UnsupportedOperationException if the {@code removeIf} operation
     * is not supported by this collection
     * @see #removeIfDouble(DoublePredicate)
     */
    default boolean removeIf(Predicate<? super Double> filter) {
        return removeIfDouble(filter::test);
    }

    /**
     * <p>Removes all of the elements of this collection that satisfy the given
     * predicate (optional operation). Errors or runtime exceptions thrown
     * during iteration or by the predicate are relayed to the caller.</p>
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
    default boolean removeIfDouble(DoublePredicate filter) {
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
     * <p>Retains only the elements in this collection that are contained in the
     * specified collection (optional operation). In other words, removes from
     * this collection all of its elements that are not contained in the
     * specified collection.</p>
     * @implSpec <p>The default implementation checks the runtime type of the
     * collection to determine if it is an instance of {@code DoubleCollection},
     * in which case it passes it to {@link #retainAll(DoubleCollection)};
     * otherwise it is equivalent to
     * {@code removeIfDouble(t -> !collection.contains(t))}.</p>
     * @param collection collection containing elements to be retained in this
     *                  collection
     * @return {@code true} if this collection changed as a result of the call
     * @throws UnsupportedOperationException if the {@code retainAll} operation is
     * not supported by this collection
     * @throws ClassCastException if the types of one or more elements in this
     * collection are incompatible with the specified collection (optional)
     * @throws NullPointerException if the specified collection is null
     * @see #remove(Object)
     * @see #contains(Object)
     */
    default boolean retainAll(Collection<?> collection) {
        if (collection instanceof DoubleCollection doubles)
            return retainAll(doubles);
        return removeIfDouble(((DoublePredicate) collection::contains).negate());
    }

    /**
     * <p>Retains only the elements in this collection that are contained in the
     * specified collection (optional operation). In other words, removes from
     * this collection all of its elements that are not contained in the
     * specified collection.</p>
     * @implSpec <p>The default implementation is equivalent to
     * {@code removeIfDouble(t -> !collection.containsDouble(t))}.</p>
     * @param collection collection containing elements to be retained in this
     *                  collection
     * @return {@code true} if this collection changed as a result of the call
     * @throws UnsupportedOperationException if the {@code retainAll} operation is
     * not supported by this collection
     * @throws NullPointerException if the specified collection is null
     * @see #containsDouble(double)
     */
    default boolean retainAll(DoubleCollection collection) {
        return removeIfDouble(((DoublePredicate) collection::containsDouble).negate());
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
        return PrimitiveCollections.doubleSpliterator(this, 0);
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
     * @param collection collection to create an unmodifiable view over
     * @return an unmodifiable view over the specified collection.
     */
    static DoubleCollection unmodifiableCollection(DoubleCollection collection) {
        return PrimitiveCollections.unmodifiableCollection(collection);
    }
}
