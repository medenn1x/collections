package net.arinoru.util;

import net.arinoru.annotation.Unstable;

import java.util.Collection;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.function.DoubleConsumer;
import java.util.function.DoublePredicate;
import java.util.stream.DoubleStream;
import java.util.stream.StreamSupport;

/**
 * <p>A Set specialized for {@code double} values.</p>
 */
@Unstable
public interface DoubleSet extends PrimitiveSet<Double,double[], DoubleConsumer,
        DoublePredicate, PrimitiveIterator.OfDouble, Spliterator.OfDouble, DoubleStream,
        DoubleCollection>, DoubleCollection {
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
     * {@code DoubleCollection}.</p>
     * @param element element to be added to this set
     * @return {@code true} if this set did not already contain the specified
     * element
     * @throws UnsupportedOperationException if the {@code add} operation is not
     * supported by this set
     * @throws NullPointerException if the specified element is null
     * @throws IllegalArgumentException if some property of the specified element
     * prevents it from being added to this set
     * @see #addDouble(double)
     * @see DoubleCollection#add(Double)
     */
    default boolean add(Double element) {
        return DoubleCollection.super.add(element);
    }

    /**
     * <p>Adds all of the elements in the specified collection to this set if
     * they're not already present (optional operation). If the specified
     * collection is also a set, the {@code addAll} operation effectively
     * modifies this set so that its value is the <em>union</em> of the two
     * sets. The behavior of this operation is undefined if the specified
     * collection is modified while the operation is in progress.</p>
     * @implSpec <p>The default implementation is inherited from
     * {@code DoubleCollection}.</p>
     * @param collection collection containing elements to be added to this set
     * @return {@code true} if this set changed as a result of the call
     * @throws UnsupportedOperationException if the {@code addAll} operation is
     * not supported by this set.
     * @throws ClassCastException if the class of an element of the specified
     * collection prevents it from being added to this set
     * @throws NullPointerException if the specified collection contains one or
     * more null elements, or if the specified collection is null
     * @throws IllegalArgumentException if some property of an element of the
     * specified collection prevents it from being added to this set
     * @see #addAll(DoubleCollection)
     * @see DoubleCollection#addAll(Collection)
     * @see #add(Double)
     */
    default boolean addAll(Collection<? extends Double> collection) {
        return DoubleCollection.super.addAll(collection);
    }

    /**
     * <p>Adds all of the elements in the specified collection to this set if
     * they're not already present (optional operation). If the specified
     * collection is also a set, the {@code addAll} operation effectively
     * modifies this set so that its value is the <em>union</em> of the two
     * sets. The behavior of this operation is undefined if the specified
     * collection is modified while the operation is in progress.</p>
     * @implSpec <p>The default implementation is inherited from
     * {@code DoubleCollection}.</p>
     * @param collection collection containing elements to be added to this set
     * @return {@code true} if this set changed as a result of the call
     * @throws UnsupportedOperationException if the {@code addAll} operation is
     * not supported by this set.
     * @throws IllegalArgumentException if some property of an element of the
     * specified collection prevents it from being added to this set
     * @see #addAll(Collection)
     * @see DoubleCollection#addAll(DoubleCollection)
     * @see #addDouble(double)
     */
    default boolean addAll(DoubleCollection collection) {
        return DoubleCollection.super.addAll(collection);
    }

    /**
     * <p>Adds the specified element to this set if it is not already present
     * (optional operation). More formally, adds the specified element
     * {@code element} to this set if the set contains no element {@code e} such that
     * {@code element == e}. If this set already contains the element, the call leaves
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
     * {@code DoubleCollection}.</p>
     * @param element element to be added to this set
     * @return {@code true} if this set did not already contain the specified
     * element
     * @throws UnsupportedOperationException if the {@code add} operation is not
     * supported by this set
     * @throws IllegalArgumentException if some property of the specified element
     * prevents it from being added to this set
     * @see #add(Double)
     * @see DoubleCollection#addDouble(double)
     */
    default boolean addDouble(double element) {
        return DoubleCollection.super.addDouble(element);
    }

    /**
     * <p>Removes all of the elements from this set (optional operation). The set
     * will be empty after this call returns.</p>
     * @implSpec <p>The default implementation is inherited from
     * {@code DoubleCollection}.</p>
     * @throws UnsupportedOperationException if the {@code clear} method is not
     * supported by this set
     */
    default void clear() {
        DoubleCollection.super.clear();
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
     * {@code DoubleCollection}.</p>
     * @param o element whose presence in this set is to be tested
     * @return {@code true} if this set contains the specified element
     * @throws ClassCastException if the type of the specified element is
     * incompatible with this set (optional)
     * @throws NullPointerException if the specified element is null (optional)
     */
    default boolean contains(Object o) {
        return DoubleCollection.super.contains(o);
    }

    /**
     * <p>Returns {@code true} if this set contains all of the elements of the
     * specified collection. If the specified collection is also a set, this
     * method returns {@code true} if it is a <em>subset</em> of this set.</p>
     * @implSpec <p>The default implementation is inherited from
     * {@code DoubleCollection}.</p>
     * @param collection collection to be checked for containment in this set
     * @return {@code true} if this set contains all of the elements of the
     * specified collection
     * @throws ClassCastException if the types of one or more elements in the
     * specified collection are incompatible with this set (optional)
     * @throws NullPointerException if the specified collection contains one or
     * more null elements (optional), or if the specified collection is null
     * @see #containsAll(DoubleCollection)
     * @see DoubleCollection#containsAll(Collection)
     * @see #contains(Object)
     */
    default boolean containsAll(Collection<?> collection) {
        return DoubleCollection.super.containsAll(collection);
    }

    /**
     * <p>Returns {@code true} if this set contains all of the elements of the
     * specified collection. If the specified collection is also a set, this
     * method returns {@code true} if it is a <em>subset</em> of this set.</p>
     * @implSpec <p>The default implementation is inherited from
     * {@code DoubleCollection}.</p>
     * @param collection collection to be checked for containment in this set
     * @return {@code true} if this set contains all of the elements of the
     * specified collection
     * @throws NullPointerException if the specified collection is null
     * @see #containsAll(Collection)
     * @see DoubleCollection#containsAll(DoubleCollection)
     * @see #containsDouble(double)
     */
    default boolean containsAll(DoubleCollection collection) {
        return DoubleCollection.super.containsAll(collection);
    }

    /**
     * <p>Returns {@code true} if this set contains the specified element.
     * More formally, returns {@code true} if and only if this set contains an
     * element {@code e} such that {@code element == e}.</p>
     * @implSpec <p>The default implementation is inherited from
     * {@code DoubleCollection}.</p>
     * @param element element whose presence in this set is to be tested
     * @return {@code true} if this set contains the specified element
     * @see #contains(Object)
     * @see DoubleCollection#containsDouble(double)
     */
    default boolean containsDouble(double element) {
        return DoubleCollection.super.containsDouble(element);
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
     * @param collection a primitive collection from which elements are drawn,
     *             must be non-null
     * @return a {@code DoubleSet} containing the elements of
     * the given collection
     * @throws NullPointerException if coll is null
     */
    static DoubleSet copyOf(DoubleCollection collection) {
        return PrimitiveCollections.setOf(collection);
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
     * @return an empty {@code DoubleSet}.
     */
    static DoubleSet of() {
        return PrimitiveCollections.emptyDoubleSet();
    }

    /**
     * <p>Returns an unmodifiable set containing one element.</p>
     * @param e the single element
     * @return a {@code DoubleSet} containing the specified element
     */
    static DoubleSet of(double e) {
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
    static DoubleSet of(double... elements) {
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
     * this set
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
     * @see DoubleCollection#remove(Object)
     */
    default boolean remove(Object o) {
        return DoubleCollection.super.remove(o);
    }

    /**
     * <p>Removes from this set all of its elements that are contained in the
     * specified collection (optional operation). If the specified collection is
     * also a set, this operation effectively modifies this set so that its
     * value is the <em>asymmetric set difference</em> of the two sets.</p>
     * @implSpec <p>The default implementation is inherited from
     * {@code DoubleCollection}.</p>
     * @param collection collection containing elements to be removed from this set
     * @return {@code true} if this set changed as a result of the call
     * @throws UnsupportedOperationException if the {@code removeAll} operation
     * is not supported by this set
     * @throws ClassCastException if the class of an element of this set is
     * incompatible with the specified collection (optional)
     * @throws NullPointerException if the specified collection is null
     */
    default boolean removeAll(Collection<?> collection) {
        return DoubleCollection.super.removeAll(collection);
    }

    /**
     * <p>Removes from this set all of its elements that are contained in the
     * specified collection (optional operation). If the specified collection is
     * also a set, this operation effectively modifies this set so that its
     * value is the <em>asymmetric set difference</em> of the two sets.</p>
     * @implSpec <p>The default implementation is inherited from
     * {@code DoubleCollection}.</p>
     * @param collection collection containing elements to be removed from this set
     * @return {@code true} if this set changed as a result of the call
     * @throws UnsupportedOperationException if the {@code removeAll} operation
     * is not supported by this set
     * @throws NullPointerException if the specified collection is null
     */
    default boolean removeAll(DoubleCollection collection) {
        return DoubleCollection.super.removeAll(collection);
    }

    /**
     * <p>Removes the specified element from this set if it is present (optional
     * operation). Move formally, removes an element {@code e} such that
     * {@code element == e}, if this set contains such an element.
     * Returns {@code true} if this set contained the element (or equivalently,
     * if this set changed as a result of the call). (This set will not contain
     * the element once the call returns.)</p>
     * @implSpec <p>The default implementation is inherited from
     * {@code DoubleCollection}.</p>
     * @param element element to be removed from this set, if present
     * @return {@code true} if this set contained the specified element
     * @throws UnsupportedOperationException if the {@code remove} operation
     * is not supported by this set
     * @see #remove(Object)
     * @see DoubleCollection#removeDouble(double)
     */
    default boolean removeDouble(double element) {
        return DoubleCollection.super.removeDouble(element);
    }

    /**
     * <p>Retains only the elements in this set that are contained in the
     * specified collection (optional operation). In other words, removes from
     * this set all of its elements that are not contained in the specified
     * collection. If the specified collection is also a set, this operation
     * effectively modifies this set so that its value is the
     * <em>intersection</em> of the two sets.</p>
     * @implSpec <p>The default implementation is inherited from
     * {@code DoubleCollection}.</p>
     * @param collection collection containing elements to be retained in this set
     * @return {@code true} if this set changes as a result of the call
     * @throws UnsupportedOperationException if the {@code retainAll}
     * operation is not supported by this set
     * @throws ClassCastException if the class of an element of this set is
     * incompatible with the specified collection (optional)
     * @throws NullPointerException if the specified collection is null
     * @see #retainAll(DoubleCollection)
     * @see DoubleCollection#retainAll(Collection)
     * @see #remove(Object)
     */
    default boolean retainAll(Collection<?> collection) {
        return DoubleCollection.super.retainAll(collection);
    }

    /**
     * <p>Retains only the elements in this set that are contained in the
     * specified collection (optional operation). In other words, removes from
     * this set all of its elements that are not contained in the specified
     * collection. If the specified collection is also a set, this operation
     * effectively modifies this set so that its value is the
     * <em>intersection</em> of the two sets.</p>
     * @implSpec <p>The default implementation is inherited from
     * {@code DoubleCollection}.</p>
     * @param collection collection containing elements to be retained in this set
     * @return {@code true} if this set changes as a result of the call
     * @throws UnsupportedOperationException if the {@code retainAll}
     * operation is not supported by this set
     * @throws NullPointerException if the specified collection is null
     * @see #retainAll(Collection)
     * @see DoubleCollection#retainAll(DoubleCollection)
     * @see #removeDouble(double)
     */
    default boolean retainAll(DoubleCollection collection) {
        return DoubleCollection.super.retainAll(collection);
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
     * {@code DoubleCollection}.</p>
     * @return an array containing all the elements in this set
     */
    default double[] toPrimitiveArray() {
        return DoubleCollection.super.toPrimitiveArray();
    }

    /**
     * <p>Returns a set view over the specified set. This view will not permit
     * any operations which modify the set.</p>
     * <p>The returned view is backed by the set, so any changes to the backing
     * set will be visible in the returned set.</p>
     * @param set set to create an unmodifiable view over
     * @return an unmodifiable view over the specified set
     */
    static DoubleSet unmodifiableSet(DoubleSet set) {
        return PrimitiveCollections.unmodifiableSet(set);
    }
}
