package net.arinoru.util.views;

import net.arinoru.util.CollectionNotModifiableException;

import java.util.Collection;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * <p>An internal implementation of a collection view. This class defines a
 * series of default behaviors for collection operations based on a
 * {@link Forwarder}, an object that provides access to a backing collection
 * and means of providing varying levels of access to functionality based on
 * a configured {@link ForwardingType}.</p>
 * <p>The requirements for the backing collection vary based on the forwarding
 * type, with {@link ForwardingType#MINIMAL} allowing the use of any backing
 * collection, while {@link ForwardingType#PURE} and
 * {@link ForwardingType#SHALLOW} both require the backing element type to
 * match the view's element type.</p>
 * <p>The general rule for the implementation of collection operations in
 * this class depends on the forwarding type and whether or not the
 * {@link Collection} interface provides a default implementation for that
 * operation. In general:</p>
 * <ol>
 *     <li>Operations that (potentially) modify the contents of the collection
 *     will throw a {@link CollectionNotModifiableException} if this view
 *     implements {@link UnmodifiableView}.</li>
 *     <li>Operations that are not sensitive to the element type of the
 *     collection and which do not have default implementations are forwarded
 *     directly to the backing set, regardless of forwarding type.</li>
 *     <li>Operations which have a default method defined in the
 *     {@link Collection} interface are forwarded to the backing collection if
 *     the forwarding type is {@link ForwardingType#PURE}; otherwise, they are
 *     implemented in terms of the default method, which may itself invoke
 *     other collection operations on the view.</li>
 *     <li>Operations that are sensitive to the element type of the collection
 *     (e.g. those that add or remove elements or are otherwise bounded by
 *     subtype) and do not have a default implementation in {@link Collection}
 *     will throw an {@link InvalidForwardingContextException} if the
 *     forwarding type is {@link ForwardingType#MINIMAL}.</li>
 *     <li>Operations not meeting the above criteria will generally forward
 *     to the backing collection. If some aspect of the type difference
 *     between the view and the backing collection prevents the proper
 *     operation of the forward, an exception may be thrown (e.g.
 *     {@link InvalidForwardingContextException}, {@link ClassCastException},
 *     or {@link IllegalStateException}.</li>
 * </ol>
 * @param <E> element type of the view
 */
public abstract class AbstractCollectionView<E> implements View, Collection<E> {
    /**
     * <p>Returns the {@link Forwarder} managing access to the backing
     * collection.</p>
     * @implSpec <p>Implementing classes must override this to return a forwarder
     * that represents the backing collection and forwarding type. The forwarder is
     * not stored by this class directly.</p>
     * @return forwarder that manages access to the backing collection
     */
    protected abstract Forwarder<Collection<?>,Collection<E>> forwarder();

    /**
     * <p>Ensures that this collection contains the specified element (optional
     * operation).</p>
     * @implSpec <p>This method must be overridden if forwarding type is
     * {@link ForwardingType#MINIMAL}.</p>
     * @implNote <p>This method will throw an exception if the view implements
     * {@link UnmodifiableView}. Otherwise, it forwards the request to the
     * backing set unless the forwarding type forbids automatic forwarding of
     * type-sensitive methods.</p>
     * @param e element whose presence in this collection is to be ensured
     * @return {@code true} if this collection changed as a result of the call
     * @throws CollectionNotModifiableException if this view is unmodifiable
     * @throws InvalidForwardingContextException if the forwarding type is
     * {@code MINIMAL}
     * @throws UnsupportedOperationException if the {@code add} operation is
     * not supported by this collection
     * @throws ClassCastException if the class of the specified element prevents
     * it from being added to this collection
     * @throws NullPointerException if the specified element is null and this
     * collection does not support null elements.
     * @throws IllegalArgumentException if some property of this element prevents
     * it from being added to this collection
     * @throws IllegalStateException if the element cannot be added at this time
     * due to insertion restrictions
     */
    @Override
    public boolean add(E e) {
        checkModifiable();
        return forwarder().predicateOp(delegate -> delegate.add(e));
    }

    /**
     * <p>Adds all of the elements in the specified collection to this collection
     * (optional operation).</p>
     * @implSpec <p>This method must be overridden if forwarding type is
     * {@link ForwardingType#MINIMAL}.</p>
     * @implNote <p>This method will throw an exception if the view implements
     * {@link UnmodifiableView}. Otherwise, it forwards the request to the
     * backing collection unless the forwarding type forbids automatic forwarding
     * of type-sensitive methods.</p>
     * @param collection collection containing elements to be added to this
     *                  collection
     * @return {@code true} if this collection changed as a result of the call
     * @throws CollectionNotModifiableException if this view is unmodifiable
     * @throws InvalidForwardingContextException if the forwarding type is
     * {@code MINIMAL}
     * @throws UnsupportedOperationException if the {@code addAll} operation is
     * not supported by this collection
     * @throws ClassCastException if the class of an element of the specified
     * collection prevents it from being added to this collection
     * @throws NullPointerException if the specified collection contains a null
     * element and this collection does not support null elements, or the
     * specified collection is null
     * @throws IllegalArgumentException if some property of an element of the
     * specified collection prevents it from being added to this collection
     * @throws IllegalStateException if not all the elements can be added at
     * this time due to insertion restrictions
     */
    @Override
    public boolean addAll(Collection<? extends E> collection) {
        checkModifiable();
        return forwarder().predicateOp(delegate ->
                delegate.addAll(collection));
    }

    /**
     * <p>Removes all of the elements from this collection (optional
     * operation).</p>
     * @implNote <p>This method will throw an exception if the view implements
     * {@link UnmodifiableView}. Otherwise, it forwards the request to the
     * backing set.</p>
     * @throws CollectionNotModifiableException if this view is unmodifiable
     * @throws UnsupportedOperationException if the {@code clear} operation is
     * not supported by this collection
     */
    @Override
    public void clear() {
        checkModifiable();
        forwarder().asDelegateType().clear();
    }

    /**
     * <p>Returns {@code true} if this collection contains the specified
     * element.</p>
     * @apiNote <p>The default behavior for minimal forwarding is under review.
     * If use cases are discovered that make more lenient implementation
     * useful, the override requirement may be eliminated.</p>
     * @implSpec <p>This method must be overridden if forwarding type is
     * {@link ForwardingType#MINIMAL}.</p>
     * @implNote <p>This method forwards the request to the backing collection
     * unless the forwarding type forbids automatic forwarding of
     * type-sensitive methods.</p>
     * @param o element whose presence in this collection is to be tested
     * @return {@code true} if this collection contains the specified element
     * @throws InvalidForwardingContextException if the forwarding type is
     * {@code MINIMAL}
     * @throws ClassCastException if the type of the specified element is
     * incompatible with this collection (optional)
     * @throws NullPointerException if the specified element is null and this
     * collection does not support null elements (optional)
     */
    @Override
    public boolean contains(Object o) {
        return forwarder().predicateOp(delegate ->
                delegate.contains(o));
    }

    /**
     * <p>Returns {@code true} if this collection contains all of the elements in
     * the specified collection.</p>
     * @apiNote <p>The default behavior for minimal forwarding is under review.
     * If use cases are discovered that make more lenient implementation
     * useful, the override requirement may be eliminated.</p>
     * @implSpec <p>This method must be overridden if forwarding type is
     * {@link ForwardingType#MINIMAL}.</p>
     * @implNote <p>This method forwards the request to the backing collection
     * unless the forwarding type forbids automatic forwarding of type-sensitive
     * methods.</p>
     * @param collection collection to be checked for containment in this
     *                   collection
     * @return {@code true} if this collection contains all elements in the
     * specified collection
     * @throws InvalidForwardingContextException if forwarding type is
     * {@code MINIMAL}
     * @throws ClassCastException if the types of one or more elements in the
     * specified collection are incompatible with this collection (optional)
     * @throws NullPointerException if the specified collection contains one or
     * more null elements and this collection does not support null elements
     * (optional) or if the specified collection is null.
     */
    @Override
    public boolean containsAll(Collection<?> collection) {
        return forwarder().predicateOp(delegate ->
                delegate.containsAll(collection));
    }

    /**
     * <p>Compares the specified object with this collection for equality.</p>
     * @implSpec <p>Implementing classes should exercise caution when
     * overriding this method to ensure that it does not violate the contract
     * of equality for any object comparison that may occur, including the
     * general requirement that sets and lists must not be equal, and that
     * equality should be reflexive (i.e. {@code a.equals(b)} implies
     * {@code b.equals(a)} for any objects {@code a} and {@code b}.</p>
     * @implNote <p>This method always invokes the superclass method
     * {@link Object#equals(Object)}. This ensures that the default
     * equality behavior for collection views is reference equality (i.e.
     * {@code a.equals(b)} implies {@code a==b}).</p>
     * @param o object to be compared for equality with this collection
     * @return {@code true} if the specified object is equal to this
     * collection
     */
    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    /**
     * <p>Performs the given action for each element of the collection
     * until all elements have been processed or the action throws an
     * exception.</p>
     * @implNote <p>The default implementation forwards the request to
     * the backing collection if the forwarding type is
     * {@link ForwardingType#PURE}; otherwise, it invokes the default
     * implementation from {@link Collection#forEach(Consumer)}.</p>
     * @param action action to perform on each element of the collection
     * @throws NullPointerException if the specified action is null
     */
    @Override
    public void forEach(Consumer<? super E> action) {
        forwarder().voidOp(delegate -> delegate.forEach(action),
                () -> Collection.super.forEach(action));
    }

    /**
     * <p>Returns the hash code vale for this collection.</p>
     * @implSpec <p>Implementing classes which override the
     * {@link #equals(Object)} method should also override this method
     * to ensure that {@code a.equals(b)} implies
     * {@code a.hashCode()==b.hashCode()} fpr any objects {@code a} and
     * {@code b}, as required by the general contract of
     * {@link Object#hashCode()}.</p>
     * @implNote <p>If the forwarding type is {@link ForwardingType#PURE},
     * this method forwards the request to the backing collection;
     * otherwise, it invokes the superclass method
     * {@link Object#hashCode()}.</p>
     * @return the hash code value for this collection
     */
    @Override
    public int hashCode() {
        return forwarder().intOp(Collection::hashCode, super::hashCode);
    }

    /**
     * <p>Returns {@code true} if this collection contains no elements.</p>
     * @implNote <p>This method forwards the request to the backing
     * collection.</p>
     * @return {@code true} if this collection contains no elements
     */
    @Override
    public boolean isEmpty() {
        return forwarder().asDelegateType().isEmpty();
    }

    /**
     * <p>Returns an iterator over the elements in this collection.</p>
     * @implSpec <p>This method must be overridden if forwarding type is
     * {@link ForwardingType#MINIMAL}. Additionally, if both this view and
     * the backing collection implement {@link UnmodifiableView}, the
     * correct behavior of this method is dependent on the correctness of
     * the backing collection's implementation (i.e. the backing collection
     * must not return an iterator capable of modifying its elements).</p>
     * @implNote <p>This method forwards the request to the backing
     * collection unless automatic forwarding of type-sensitive operations
     * is forbidden by the forwarding type. Additionally, if this view
     * implements {@link UnmodifiableView} and neither the backing
     * collection nor the iterator it returns do, an unmodifiable view of
     * the iterator will be returned.</p>
     * @return an iterator over the elements in this collection
     */
    @Override
    public Iterator<E> iterator() {
        return forwarder().maskIfNeeded(forwarder().boxedOp(Collection::iterator),
                IteratorView::unmodifiable);
    }

    /**
     * <p>Returns a possibly parallel {@code Stream} with this collection
     * as its source. It is allowable for this method to return a
     * sequential stream.</p>
     * @apiNote <p>This method assumes that any stream returned will be
     * implicitly read-only. This should generally be a safe assumption,
     * but implementers are advised that returning an implementing class
     * which includes operations (beyond those specified by {@link Stream})
     * that modify the elements of the collection will violate the contract
     * of {@link UnmodifiableView}, and this class makes no attempt to guard
     * against a backing collection that behaves in this way.</p>
     * @implNote <p>If the forwarding type is {@link ForwardingType#PURE},
     * the method forwards the request to the backing collection; otherwise,
     * it invokes the default implementation from
     * {@link Collection#parallelStream()}.</p>
     * @return a possibly parallel stream over the elements in this
     * collection
     */
    @Override
    public Stream<E> parallelStream() {
        return forwarder().boxedOp(Collection::parallelStream,
                Collection.super::parallelStream);
    }

    /**
     * <p>Removes a single instance of the specified element from this
     * collection, if it is present (optional operation).</p>
     * @implSpec <p>This method must be overridden if forwarding type is
     * {@link ForwardingType#MINIMAL}.</p>
     * @implNote <p>This method will throw an exception if the view implements
     * {@link UnmodifiableView}. Otherwise, it forwards the request to the
     * backing collection unless the forwarding type forbids automatic forwarding
     * of type-sensitive methods.</p>
     * @param o element to be removed from this collection, if present
     * @return {@code true} if an element was removed as a result of this
     * call
     * @throws CollectionNotModifiableException if this view is unmodifiable
     * @throws InvalidForwardingContextException if the forwarding type is
     * {@code MINIMAL}
     * @throws ClassCastException if the type of the specified element is
     * incompatible with this collection (optional)
     * @throws NullPointerException if the specified element is null and
     * this collection does not support null elements (optional)
     * @throws UnsupportedOperationException if the {@code remove}
     * operation is not supported by this collection
     */
    @Override
    public boolean remove(Object o) {
        checkModifiable();
        return forwarder().predicateOp(delegate -> delegate.remove(o));
    }

    /**
     * <p>Removes all of this collection's elements that are also contained in the
     * specified collection (optional operation).</p>
     * @implSpec <p>This method must be overridden if forwarding type is
     * {@link ForwardingType#MINIMAL}.</p>
     * @implNote <p>This method will throw an exception if the view implements
     * {@link UnmodifiableView}. Otherwise, it forwards the request to the
     * backing collection unless the forwarding type forbids automatic forwarding
     * of type-sensitive methods.</p>
     * @param collection collection containing elements to be removed from this
     *                  collection
     * @return {@code true} if this collection changed as a result of the call
     * @throws CollectionNotModifiableException if this view is unmodifiable
     * @throws InvalidForwardingContextException if the forwarding type is
     * {@code MINIMAL}
     * @throws UnsupportedOperationException if the {@code removeAll} operation
     * is not supported by this collection
     * @throws ClassCastException if the types of one or more elements in this
     * collection are incompatible with the specified collection (optional)
     * @throws NullPointerException if this collection contains one or more
     * null elements (optional) or if the specified collection is null
     */
    @Override
    public boolean removeAll(Collection<?> collection) {
        checkModifiable();
        return forwarder().predicateOp(delegate ->
                delegate.removeAll(collection));
    }

    /**
     * <p>Removes all of the elements of this collection that satisfy the given
     * predicate (optional operation).</p>
     * @implNote <p>This method will throw an exception if the view implements
     * {@link UnmodifiableView}. If it does not, and the forwarding type is
     * {@link ForwardingType#PURE}, the request is forwarded to the backing
     * collection; otherwise, the default implementation from
     * {@link Collection#removeIf(Predicate)} is used.</p>
     * @param filter a predicate which returns {@code true} for elements to be
     *               removed
     * @return {@code true} if any elements were removed
     * @throws CollectionNotModifiableException if this view is unmodifiable
     * @throws NullPointerException if the specified filter is null
     * @throws UnsupportedOperationException if the {@code removeIf} operation
     * is not supported by this collection
     */
    @Override
    public boolean removeIf(Predicate<? super E> filter) {
        checkModifiable();
        return forwarder().predicateOp(delegate -> delegate.removeIf(filter),
                () -> Collection.super.removeIf(filter));
    }

    /**
     * <p>Retains only the elements in this collection that are contained in the
     * specified collection (optional operation).</p>
     * @implSpec <p>This method must be overridden if forwarding type is
     * {@link ForwardingType#MINIMAL}.</p>
     * @implNote <p>This method will throw an exception if the view implements
     * {@link UnmodifiableView}. Otherwise, it forwards the request to the
     * backing collection unless the forwarding type forbids automatic forwarding
     * of type-sensitive methods.</p>
     * @param collection collection containing elements to be retained in this
     *                  collection
     * @return {@code true} if this collection changed as a result of the call
     * @throws CollectionNotModifiableException if this view is unmodifiable
     * @throws InvalidForwardingContextException if the forwarding type is
     * {@code MINIMAL}
     * @throws UnsupportedOperationException if the {@code retainAll} operation is
     * not supported by this collection
     * @throws ClassCastException if the types of one or more elements in this
     * collection are incompatible with the specified collection (optional)
     * @throws NullPointerException if the specified collection is null
     */
    @Override
    public boolean retainAll(Collection<?> collection) {
        checkModifiable();
        return forwarder().predicateOp(delegate ->
                delegate.retainAll(collection));
    }

    /**
     * Returns the number of elements in this collection. If this collection
     * contains more than {@link Integer#MAX_VALUE} elements, returns
     * {@code Integer.MAX_VALUE}.
     * @implNote <p>This method forwards the request to the backing
     * collection.</p>
     * @return the number of elements in this collection
     */
    @Override
    public int size() {
        return forwarder().asDelegateType().size();
    }

    /**
     * <p>Creates a {@code Spliterator} over the elements in this
     * collection.</p>
     * @implNote <p>If the forwarding type is {@link ForwardingType#PURE},
     * the method forwards the request to the backing collection; otherwise,
     * it invokes the default implementation from
     * {@link Collection#spliterator()}.</p>
     * @return a {@code Spliterator} over the elements in this collection
     */
    @Override
    public Spliterator<E> spliterator() {
        return forwarder().boxedOp(Collection::spliterator,
                Collection.super::spliterator);
    }

    /**
     * <p>Returns a sequential {@code Stream} with this collection as its
     * source.</p>
     * @apiNote <p>This method assumes that any stream returned will be
     * implicitly read-only. This should generally be a safe assumption,
     * but implementers are advised that returning an implementing class
     * which includes operations (beyond those specified by
     * {@link Stream} that modify the elements of the collection will
     * violate the contract of {@link UnmodifiableView}, and this class
     * makes no attempt to guard against a backing collection that
     * behaves this way.</p>
     * @implNote <p>If the forwarding type is {@link ForwardingType#PURE},
     * the method forwards the request to the backing collection;
     * otherwise, it invokes the default implementation from
     * {@link Collection#stream()}.</p>
     * @return a sequential stream over the elements in this collection
     */
    @Override
    public Stream<E> stream() {
        return forwarder().boxedOp(Collection::stream,
                Collection.super::stream);
    }

    /**
     * <p>Returns an array containing all of the elements in this
     * collection.</p>
     * @implSpec <p>This method must be overridden if forwarding type is
     * {@link ForwardingType#MINIMAL}.</p>
     * @implNote <p>This method forwards the request to the backing
     * collection unless the forwarding type forbids automatic forwarding
     * of type-sensitive methods.</p>
     * @return an array containing all of the elements in this collection
     */
    @Override
    public Object[] toArray() {
        return forwarder().boxedOp(Collection::toArray);
    }

    /**
     * <p>Returns an array containing all of the elements in this collection;
     * the runtime type of the returned array is that returned by the specified
     * generator function.</p>
     * @implNote <p>If the forwarding type is {@link ForwardingType#PURE},
     * the method forwards the request to the backing collection; otherwise, it
     * invokes the default implementation from
     * {@link Collection#toArray(IntFunction)}.</p>
     * @param generator function that returns an array
     * @return an array containing all of the elements in this collection
     * @param <T> the component type of the array to contain the collection
     * @throws ArrayStoreException if the runtime type of any element in this
     * collection is not assignable to the runtime component type of the array
     * returned by the generator
     * @throws NullPointerException if generator is null
     * @throws OutOfMemoryError if it is not possible to allocate a sufficiently
     * large array to contain this collection (i.e. if the size of the
     * collection exceeds the maximum capacity of a Java array)
     */
    @Override
    public <T> T[] toArray(IntFunction<T[]> generator) {
        return forwarder().boxedOp(
                delegate -> delegate.toArray(generator),
                () -> Collection.super.toArray(generator));
    }

    /**
     * <p>Returns an array containing all of the elements in this collection;
     * the runtime type of the returned array is that of the specified
     * array.</p>
     * @implSpec <p>This method must be overridden if forwarding type is
     * {@link ForwardingType#MINIMAL}.</p>
     * @implNote <p>This method forwards the request to the backing collection
     * unless the forwarding type forbids automatic forwarding of
     * type-sensitive methods.</p>
     * @param a the array into which the elements of this collection are to be
     *          stored, if it is big enough; otherwise, a new array of the
     *          same runtime type is allocated for this purpose.
     * @return an array containing all of the elements in this collection
     * @param <T> the component type of the array to contain the collection
     * @throws ArrayStoreException if the runtime type of any element in this
     * collection is not assignable to the runtime component type of the
     * specified array
     * @throws NullPointerException if the specified array is null
     * @throws OutOfMemoryError if it is not possible to allocate a sufficiently
     * large array to contain this collection (i.e. if the size of the
     * collection exceeds the maximum capacity of a Java array)
     */
    @Override
    public <T> T[] toArray(T[] a) {
        return forwarder().boxedOp(delegate -> delegate.toArray(a));
    }
}
