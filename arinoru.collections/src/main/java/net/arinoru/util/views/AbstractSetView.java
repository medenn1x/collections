package net.arinoru.util.views;

import net.arinoru.util.CollectionNotModifiableException;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * <p>An internal implementation of a set view. This class defines a series of
 * default behaviors for set operations based on a {@link Forwarder}, an object
 * that provides access to a backing set and means of providing varying levels
 * of access to functionality based on a configured {@link ForwardingType}.</p>
 * <p>The requirements for the backing set vary based on the forwarding type,
 * with {@link ForwardingType#MINIMAL} allowing the use of any backing set,
 * while {@link ForwardingType#PURE} and {@link ForwardingType#SHALLOW} both
 * require the backing element type to match the view's element type.</p>
 * <p>The general rule for the implementation of set operations in this class
 * depends on the forwarding type and whether or not the {@link Set} interface
 * provides a default implementation for that operation. In general:</p>
 * <ol>
 *     <li>Operations that (potentially) modify the contents of the set will
 *     throw a {@link CollectionNotModifiableException} if this view
 *     implements {@link UnmodifiableView}.</li>
 *     <li>Operations that are not sensitive to the element type of the set
 *     and which do not have default implementations are forwarded directly to
 *     the backing set, regardless of forwarding type.</li>
 *     <li>Operations which have a default method defined in the {@link Set}
 *     interface are forwarded to the backing set if the forwarding type is
 *     {@link ForwardingType#PURE}; otherwise, they are implemented in terms
 *     of the default method, which may itself invoke other set operations on
 *     the view.</li>
 *     <li>Operations that are sensitive to the element type of the set (e.g.
 *     those that add or remove elements or are otherwise bounded by subtype)
 *     and do not have a default implementation in {@link Set} will throw an
 *     {@link InvalidForwardingContextException} if the forwarding type is
 *     {@link ForwardingType#MINIMAL}.</li>
 *     <li>Operations not meeting the above criteria will generally forward
 *     to the backing set. If some aspect of the type difference between the
 *     view and the backing set prevents the proper operation of the forward,
 *     an exception may be thrown (e.g.
 *     {@link InvalidForwardingContextException}, {@link ClassCastException},
 *     or {@link IllegalStateException}).</li>
 * </ol>
 * @param <E> element type of the view
 */
public abstract class AbstractSetView<E> implements View, Set<E> {
    /**
     * <p>Returns the {@link Forwarder} managing access to the backing set.</p>
     * @implSpec <p>Implementing classes must override this to return a forwarder
     * that represents the backing set and forwarding type. The forwarder is
     * not stored by this class directly.</p>
     * @return forwarder that manages access to the backing set
     */
    protected abstract Forwarder<Set<?>,Set<E>> forwarder();

    /**
     * <p>Adds the specified element to this set if it is not already present
     * (optional operation).</p>
     * @implSpec <p>This method must be overridden if forwarding type is
     * {@link ForwardingType#MINIMAL}.</p>
     * @implNote <p>This method will throw an exception if the view implements
     * {@link UnmodifiableView}. Otherwise, it forwards the request to the
     * backing set unless the forwarding type forbids automatic forwarding of
     * type-sensitive methods.</p>
     * @param element element to be added to this set
     * @return {@code true} if this set did not already contain the specified
     * element
     * @throws CollectionNotModifiableException if this view is unmodifiable
     * @throws InvalidForwardingContextException if the forwarding type is
     * {@code MINIMAL}
     * @throws UnsupportedOperationException if the {@code add} operations is
     * not supported by the set
     * @throws ClassCastException if the class of the specified element
     * prevents it from being added to the set
     * @throws NullPointerException if the specified element is null and this
     * set does not support null elements
     * @throws IllegalArgumentException if some property of the specified
     * element prevents it from being added to this set
     */
    @Override
    public boolean add(E element) {
        checkModifiable();
        return forwarder().predicateOp(delegate -> delegate.add(element));
    }

    /**
     * <p>Adds all of the elements in the specified collection to this set if
     * they're not already present (optional operation).</p>
     * @implSpec <p>This method must be overridden if forwarding type is
     * {@link ForwardingType#MINIMAL}.</p>
     * @implNote <p>This method will throw an exception if the view implements
     * {@link UnmodifiableView}. Otherwise, it forwards the request to the
     * backing set unless the forwarding type forbids automatic forwarding of
     * type-sensitive methods.</p>
     * @param collection collection containing elements to be added to this set
     * @return {@code true} if this set changed as a result of the call
     * @throws CollectionNotModifiableException if this view is unmodifiable
     * @throws InvalidForwardingContextException if the forwarding type is
     * {@code MINIMAL}
     * @throws UnsupportedOperationException if the {@code addAll} operation is
     * not supported by this set
     * @throws ClassCastException if the class of an element of the specified
     * collection prevents it from being added to this set
     * @throws NullPointerException if the specified collection contains one or
     * more null elements and this set does not support null elements, or if the
     * specified collection is null
     * @throws IllegalArgumentException if some property of an element of the
     * specified collection prevents it from being added to this set
     */
    @Override
    public boolean addAll(Collection<? extends E> collection) {
        checkModifiable();
        return forwarder().predicateOp(delegate -> delegate.addAll(collection));
    }

    /**
     * <p>Removes all of the elements from this set (optional operation).</p>
     * @implNote <p>This method will throw an exception if the view implements
     * {@link UnmodifiableView}. Otherwise, it forwards the request to the
     * backing set.</p>
     * @throws CollectionNotModifiableException if this view is unmodifiable
     * @throws UnsupportedOperationException if the {@code clear} operation is
     * not suppored by this set
     */
    @Override
    public void clear() {
        checkModifiable();
        forwarder().asDelegateType().clear();
    }

    /**
     * <p>Returns {@code true} if this set contains the specified element.</p>
     * @apiNote <p>The default behavior for minimal forwarding is under review.
     * If use cases are discovered that make more lenient implementation
     * useful, the override requirement may be eliminated.</p>
     * @implSpec <p>This method must be overridden if forwarding type is
     * {@link ForwardingType#MINIMAL}.</p>
     * @implNote <p>This method forwards the request to the backing set
     * unless the forwarding type forbids automatic forwarding of
     * type-sensitive methods.</p>
     * @param o element whose presence in this set is to be tested
     * @return {@code true} if this set contains the specified element
     * @throws InvalidForwardingContextException if forwarding type is
     * {@code MINIMAL}
     * @throws ClassCastException if the type of the specified element is
     * incompatible with this set (optional)
     * @throws NullPointerException if the specified element is null and this
     * set does not support null elements (optional)
     */
    @Override
    public boolean contains(Object o) {
        return forwarder().predicateOp(delegate -> delegate.contains(o));
    }

    /**
     * <p>Returns {@code true} if this set contains all of the elements of the
     * specified collection.</p>
     * @apiNote <p>The default behavior for minimal forwarding is under review.
     * If use cases are discovered that make more lenient implementation
     * useful, the override requirement may be eliminated.</p>
     * @implSpec <p>This method must be overridden if forwarding type is
     * {@link ForwardingType#MINIMAL}.</p>
     * @implNote <p>This method forwards the request to the backing set unless
     * the forwarding type forbids automatic forwarding of type-sensitive
     * methods.</p>
     * @param collection collection to be checked for containment in this set
     * @return {@code true} if this set contains all of the elements of the
     * specified collection
     * @throws InvalidForwardingContextException if forwarding type is
     * {@code MINIMAL}
     * @throws ClassCastException if the types of one or more elements in the
     * specified collection are incompatible with this set (optional)
     * @throws NullPointerException if the specified collection contains one
     * or more null elements and this set does not support null elements
     * (optional), or if the specified collection is null
     */
    @Override
    public boolean containsAll(Collection<?> collection) {
        return forwarder().predicateOp(delegate ->
                delegate.containsAll(collection));
    }

    /**
     * <p>Compares the specified object with this set for equality. Returns
     * {@code true} if the specified object is also a set, the two sets have
     * the same size, and every member of the specified set is contained in
     * this set (or equivalently, every member of this set is contained in
     * the specified set). This definition ensures that the equals method
     * works properly across different implementations of the set
     * interface.</p>
     * @apiNote <p>The default behavior for minimal forwarding is under review.
     * While deliberately allowing the equals method of an object to throw an
     * exception may be questionable, the failure to override this method in
     * the circumstances intended to be supported by the minimal forwarding
     * type may result in the violation of the contract of
     * {@link Set#equals(Object)}.</p>
     * <p>One possible alternative in consideration is to create an internal
     * default implementation of an equals operation that matches the
     * contract imposed by {@link Set#equals(Object)}. This may be pursued if
     * there is sufficient demand.</p>
     * @implSpec <p>To ensure correct behavior, implementing classes
     * <em>must</em> override this method if the elements returned by the
     * view do not <em>precisely</em> match those returned by the backing
     * set (e.g. if a filter or transform is present).</p>
     * @implNote <p>This method forwards the request to the backing set.</p>
     * @param o object to be compared for equality with this set
     * @return {@code true} if the specified object is equal to this set
     */
    @Override
    @SuppressWarnings("EqualsDoesntCheckParameterClass")
    public boolean equals(Object o) {
        return forwarder().asDelegateType().equals(o);
    }

    /**
     * <p>Performs the given action for each element of the set until all
     * elements have been processed or the action throws an exception.</p>
     * @implNote <p>The default implementation forwards the request to
     * the backing set if the forwarding type is {@link ForwardingType#PURE};
     * otherwise, it invokes the default implementation from
     * {@link Set#forEach(Consumer)}.</p>
     * @param action action to perform on each element of the set
     * @throws NullPointerException if the specified action is null
     */
    @Override
    public void forEach(Consumer<? super E> action) {
        forwarder().voidOp(delegate -> delegate.forEach(action),
                () -> Set.super.forEach(action));
    }

    /**
     * <p>Returns the hash code value for this set. The hash code of a set is
     * defined to be the sum of the hash codes of the elements in the set. This
     * ensures that {@code s1.equals(s2)} implies that
     * {@code s1.hashCode()==s2.hashCode()} for any two sets {@code s1} and
     * {@code s2}, as required by the general contract of
     * {@link Object#hashCode()}.</p>
     * @apiNote <p>The default behavior for minimal forwarding is under review.
     * While deliberately allowing the hashCode method of an object to throw an
     * exception may be questionable, the failure to override this method in
     * the circumstances intended to be supported by the minimal forwarding
     * type may result in the violation of the contract of
     * {@link Set#hashCode()}.</p>
     * <p>One possible alternative in consideration is to create an internal
     * default implementation of a hashCode operation that matches the
     * contract imposed by {@link Set#hashCode()}. This may be pursued if
     * there is sufficient demand.</p>
     * @implSpec <p>To ensure correct behavior, implementing classes
     * <em>must</em> override this method if the elements returned by the
     * view do not <em>precisely</em> match those returned by the backing
     * set (e.g. if a filter or transform is present).</p>
     * @implNote <p>This method forwards the request to the backing set.</p>
     * @return the hash code value for this set
     */
    @Override
    public int hashCode() {
        return forwarder().asDelegateType().hashCode();
    }

    /**
     * <p>Returns {@code true} if this set contains no elements.</p>
     * @implNote <p>This method forwards the request to the backing set.</p>
     * @return {@code true} if this set contains no elements
     */
    @Override
    public boolean isEmpty() {
        return forwarder().asDelegateType().isEmpty();
    }

    /**
     * <p>Returns an iterator over the elements in this set.</p>
     * @implSpec <p>This method must be overridden if forwarding type is
     * {@link ForwardingType#MINIMAL}. Additionally, if both this view and
     * the backing set implement {@link UnmodifiableView}, the correct
     * behavior of this method is dependent on the correctness of the
     * backing set's implementation (i.e. the backing set must not return
     * an iterator capable of modifying its elements).</p>
     * @implNote <p>This method forwards the request to the backing set
     * unless automatic forwarding of type-sensitive operations is forbidden
     * by the forwarding type. Additionally, if this view implements
     * {@link UnmodifiableView} and neither the backing set nor the iterator
     * it returns do, an unmodifiable view of the iterator will be
     * returned.</p>
     * @return an iterator over the elements in this set
     */
    @Override
    public Iterator<E> iterator() {
        return forwarder().maskIfNeeded(forwarder().boxedOp(Set::iterator),
                IteratorView::unmodifiable);
    }

    /**
     * <p>Returns a possibly parallel {@code Stream} with this set as its
     * source. It is allowable for this method to return a sequential
     * stream.</p>
     * @apiNote <p>This method assumes that any stream returned will be
     * implicitly read-only. This should generally be a safe assumption,
     * but implementers are advised that returning an implementing class
     * which includes operations (beyond those specified by {@link Stream})
     * that modify the elements of the set will violate the contract of
     * {@link UnmodifiableView}, and this class makes no attempt to guard
     * against a backing set that behaves in this way.</p>
     * @implNote <p>If the forwarding type is {@link ForwardingType#PURE},
     * the method forwards the request to the backing set; otherwise, it
     * invokes the default implementation from
     * {@link Set#parallelStream()}.</p>
     * @return a possibly parallel stream over the elements in this set
     */
    @Override
    public Stream<E> parallelStream() {
        return forwarder().boxedOp(Set::parallelStream, Set.super::parallelStream);
    }

    /**
     * <p>Removes the specified element from this set if it is present
     * (optional operation).</p>
     * @implSpec <p>This method must be overridden if forwarding type is
     * {@link ForwardingType#MINIMAL}.</p>
     * @implNote <p>This method will throw an exception if the view implements
     * {@link UnmodifiableView}. Otherwise, it forwards the request to the
     * backing set unless the forwarding type forbids automatic forwarding of
     * type-sensitive methods.</p>
     * @param o element to be removed from this set, if present
     * @return {@code true} if this set contained the specified element
     * @throws CollectionNotModifiableException if this view is unmodifiable
     * @throws InvalidForwardingContextException if the forwarding type is
     * {@code MINIMAL}
     * @throws UnsupportedOperationException if the {@code remove} operation
     * is not supported by this set
     * @throws ClassCastException if the type of the specified element is
     * incompatible with this set (optional)
     * @throws NullPointerException if the specified element is null and this
     * set does not support null elements (optional)
     */
    @Override
    public boolean remove(Object o) {
        checkModifiable();
        return forwarder().predicateOp(delegate -> delegate.remove(o));
    }

    /**
     * <p>Removes from this set all of its elements that are contained in the
     * specified collection (optional operation).</p>
     * @implSpec <p>This method must be overridden if forwarding type is
     * {@link ForwardingType#MINIMAL}.</p>
     * @implNote <p>This method will throw an exception if the view implements
     * {@link UnmodifiableView}. Otherwise, it forwards the request to the
     * backing set unless the forwarding type forbids automatic forwarding of
     * type-sensitive methods.</p>
     * @param collection collection containing elements to be removed from this
     *                   set
     * @return {@code true} if this set changed as a result of the call
     * @throws CollectionNotModifiableException if this view is unmodifiable
     * @throws InvalidForwardingContextException if the forwarding type is
     * {@code MINIMAL}
     * @throws UnsupportedOperationException if the {@code removeAll} operation
     * is not supported by this set
     * @throws ClassCastException if the class of an element of this set is
     * incompatible with the specified collection (optional)
     * @throws NullPointerException if this set contains a null element and the
     * specified collection does not support null elements (optional), or the
     * specified collection is null
     */
    @Override
    public boolean removeAll(Collection<?> collection) {
        checkModifiable();
        return forwarder().predicateOp(delegate -> delegate.removeAll(collection));
    }

    /**
     * <p>Removes from this set all of its elements that match the specified
     * filter (optional operation).</p>
     * @implNote <p>This method will throw an exception if the view implements
     * {@link UnmodifiableView}. If it does not, and the forwarding type is
     * {@link ForwardingType#PURE}, the request is forwarded to the backing
     * set; otherwise, the default implementation from
     * {@link Set#removeIf(Predicate)} is used.</p>
     * @param filter filter to determine whether an element should be removed
     * @return {@code true} if this set changed as a result of the call
     * @throws CollectionNotModifiableException if this view is unmodifiable
     * @throws UnsupportedOperationException if the {@code removeIf} operation
     * is not supported by this set
     * @throws NullPointerException if the specified filter is null
     */
    @Override
    public boolean removeIf(Predicate<? super E> filter) {
        checkModifiable();
        return forwarder().predicateOp(delegate -> delegate.removeIf(filter),
                () -> Set.super.removeIf(filter));
    }

    /**
     * <p>Retains only the elements in this set that are contained in the
     * specified collection (optional operation).</p>
     * @implSpec <p>This method must be overridden if forwarding type is
     * {@link ForwardingType#MINIMAL}.</p>
     * @implNote <p>This method will throw an exception if the view implements
     * {@link UnmodifiableView}. Otherwise, it forwards the request to the
     * backing set unless the forwarding type forbids automatic forwarding of
     * type-sensitive methods.</p>
     * @param collection collection containing elements to be retained in this
     *                   set
     * @return {@code true} if this set changed as a result of the call
     * @throws CollectionNotModifiableException if this view is unmodifiable
     * @throws InvalidForwardingContextException if the forwarding type is
     * {@code MINIMAL}
     * @throws UnsupportedOperationException if the {@code retainAll}
     * operation is not supported by this set
     * @throws ClassCastException if the class of an element of this set is
     * incompatible with the specified collection (optional)
     * @throws NullPointerException if this set contains a null element
     * (optional), or if the specified collection is null
     */
    @Override
    public boolean retainAll(Collection<?> collection) {
        checkModifiable();
        return forwarder().predicateOp(delegate ->
                delegate.retainAll(collection));
    }

    /**
     * <p>Returns the number of elements in this set (its cardinality). If this
     * set contains more than {@code Integer.MAX_VALUE} elements, returns
     * {@code Integer.MAX_VALUE}.</p>
     * @implNote <p>This method forwards the request to the backing set.</p>
     * @return the number of elements in this set (its cardinality)
     */
    @Override
    public int size() {
        return forwarder().asDelegateType().size();
    }

    /**
     * <p>Creates a spliterator over the elements in this set.</p>
     * @apiNote <p>This method assumes that any spliterator returned will be
     * implicitly read-only. This should generally be a safe assumption,
     * but implementers are advised that returning an implementing class
     * which includes operations (beyond those specified by
     * {@link Spliterator}) that modify the elements of the set will violate
     * the contract of {@link UnmodifiableView}, and this class makes no
     * attempt to guard against a backing set that behaves in this way.</p>
     * @implNote <p>If the forwarding type is {@link ForwardingType#PURE},
     * the method forwards the request to the backing set; otherwise, it
     * invokes the default implementation from
     * {@link Set#spliterator()}.</p>
     * @return a spliterator over the elements in this set
     */
    @Override
    public Spliterator<E> spliterator() {
        return forwarder().boxedOp(Set::spliterator, Set.super::spliterator);
    }

    /**
     * <p>Returns a sequential {@code Stream} with this set as its source.</p>
     * @apiNote <p>This method assumes that any stream returned will be
     * implicitly read-only. This should generally be a safe assumption,
     * but implementers are advised that returning an implementing class
     * which includes operations (beyond those specified by
     * {@link Stream} that modify the elements of the set will violate
     * the contract of {@link UnmodifiableView}, and this class makes no
     * attempt to guard against a backing set that behaves this way.</p>
     * @implNote <p>If the forwarding type is {@link ForwardingType#PURE},
     * the method forwards the request to the backing set; otherwise, it
     * invokes the default implementation from
     * {@link Set#stream()}.</p>
     * @return a sequential stream over the elements in this set
     */
    @Override
    public Stream<E> stream() {
        return forwarder().boxedOp(Set::stream, Set.super::stream);
    }

    /**
     * <p>Returns an array containing all of the elements in this set.</p>
     * @implSpec <p>This method must be overridden if forwarding type is
     * {@link ForwardingType#MINIMAL}.</p>
     * @implNote <p>This method forwards the request to the backing set unless
     * the forwarding type forbids automatic forwarding of type-sensitive
     * methods.</p>
     * @return an array containing all the elements in this set
     */
    @Override
    public Object[] toArray() {
        return forwarder().boxedOp(Set::toArray);
    }

    /**
     * <p>Returns an array containing all of the elements in this set; the
     * runtime type of the returned array is that returned by the specified
     * generator function.</p>
     * @implNote <p>If the forwarding type is {@link ForwardingType#PURE},
     * the method forwards the request to the backing set; otherwise, it
     * invokes the default implementation from
     * {@link Set#toArray(IntFunction)}.</p>
     * @param generator function that returns an array
     * @return an array containing all of the elements in this set
     * @param <T> the component type of the array to contain the set
     * @throws ArrayStoreException if the runtime type of any element in this
     * set is not assignable to the runtime component type of the array
     * returned by the generator
     * @throws NullPointerException if generator is null or returns null
     * @throws OutOfMemoryError if it is not possible to allocate a sufficiently
     * large array to contain this set (i.e. if the size of the set
     * exceeds the maximum capacity of a Java array)
     */
    @Override
    public <T> T[] toArray(IntFunction<T[]> generator) {
        return forwarder().boxedOp(delegate -> delegate.toArray(generator),
                () -> Set.super.toArray(generator));
    }

    /**
     * <p>Returns an array containing all of the elements in this set; the
     * runtime type of the returned array is that of the specified array.</p>
     * @implSpec <p>This method must be overridden if forwarding type is
     * {@link ForwardingType#MINIMAL}.</p>
     * @implNote <p>This method forwards the request to the backing set
     * unless the forwarding type forbids automatic forwarding of
     * type-sensitive methods.</p>
     * @param a the array into which the elements of this set are to be
     *          stored, if it is big enough; otherwise, a new array of the
     *          same runtime type is allocated for this purpose.
     * @return an array containing all of the elements in this set
     * @param <T> the component type of the array to contain the set
     * @throws ArrayStoreException if the runtime type of any element in this
     * set is not assignable to the runtime component type of the
     * specified array
     * @throws NullPointerException if the specified array is null
     * @throws OutOfMemoryError if it is not possible to allocate a sufficiently
     * large array to contain this set (i.e. if the size of the set
     * exceeds the maximum capacity of a Java array)
     */
    @Override
    public <T> T[] toArray(T[] a) {
        return forwarder().boxedOp(delegate -> delegate.toArray(a));
    }
}
