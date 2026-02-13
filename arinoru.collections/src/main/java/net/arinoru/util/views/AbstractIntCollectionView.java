package net.arinoru.util.views;

import net.arinoru.util.CollectionNotModifiableException;
import net.arinoru.util.IntCollection;
import net.arinoru.util.PrimitiveCollection;

import java.util.Collection;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.function.IntConsumer;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.stream.IntStream;

/**
 * <p>An internal implementation of an int collection view. This class defines
 * a series of default behaviors for collection operations based on a
 * {@link Forwarder}, an object that provides access to a backing collection
 * and means of providing varying levels of access to functionality based on a
 * configured {@link ForwardingType}.</p>
 * <p>The requirements for the backing collection vary based on the forwarding
 * type, with {@link ForwardingType#MINIMAL} allowing the use of any backing
 * collection, while {@link ForwardingType#PURE} and
 * {@link ForwardingType#SHALLOW} both require the backing collection to be a
 * {@link IntCollection}.</p>
 * <p>The general rule for the implementation of collection operations in this
 * class depends on the forwarding type and whether or not the
 * {@link IntCollection} interface provides a default implementation for that
 * operation. In general:</p>
 * <ol>
 *     <li>Operations that (potentially) modify the contents of the collection
 *     will throw a {@link CollectionNotModifiableException} if this view
 *     implements {@link UnmodifiableView}.</li>
 *     <li>Operations that are not sensitive to the element type of the
 *     collection and which do not have default implementations are forwarded
 *     directly to the backing collection, regardless of forwarding type.</li>
 *     <li>Operations which have a default method defined in the
 *     {@link IntCollection} interface are forwarded to the backing
 *     collection if the forwarding type is {@link ForwardingType#PURE};
 *     otherwise, they are implemented in terms of the default method, which
 *     may itself invoke other collection operations on the view.</li>
 *     <li>Operations that are sensitive to the element type of the collection
 *     (e.g. those that add or remove elements or are otherwise bounded by
 *     subtype, or methods that are only specified by {@link IntCollection}
 *     or {@link PrimitiveCollection}) and do not have a default
 *     implementation in {@link IntCollection} will throw an
 *     {@link InvalidForwardingContextException} if the forwarding type is
 *     {@link ForwardingType#MINIMAL}.</li>
 *     <li>Operations not meeting the above criteria will generally forward
 *     to the backing collection. If some aspect of the type difference
 *     between the view and the backing collection prevents the proper
 *     operation of the forward, an exception may be thrown (e.g.
 *     {@link InvalidForwardingContextException}, {@link ClassCastException},
 *     or {@link IllegalStateException}).</li>
 * </ol>
 */
public abstract class AbstractIntCollectionView
        extends AbstractPrimitiveCollectionView<Integer,int[],IntConsumer,IntPredicate,
        PrimitiveIterator.OfInt,Spliterator.OfInt,IntStream,IntCollection>
        implements IntCollection {
    /**
     * <p>Ensures that this collection contains the specified element
     * (optional operation).</p>
     * @implNote <p>This method will throw an exception if the view implements
     * {@link UnmodifiableView}. If it does not, and the forwarding type is
     * {@link ForwardingType#PURE}, this method forwards the request to the
     * backing collection; otherwise, the default implementation from
     * {@link IntCollection#add(Integer)} is used.</p>
     * @param element element whose presence in this collection is to be
     *               ensured
     * @return {@code true} if this collection changed as a result of the call
     * @throws CollectionNotModifiableException if this view is unmodifiable
     * @throws UnsupportedOperationException if the {@code add} operation is
     * not supported by this collection
     * @throws NullPointerException if the specified element is null
     * @throws IllegalArgumentException if some property of the element
     * prevents it from being added to this collection
     * @throws IllegalStateException if the element cannot be added at this
     * time due to insertion restrictions
     */
    @Override
    public boolean add(Integer element) {
        checkModifiable();
        return forwarder().predicateOp(delegate -> delegate.add(element),
                () -> IntCollection.super.add(element));
    }

    /**
     * <p>Adds all of the elements in the specified collection to this
     * collection (optional operation).</p>
     * @implNote <p>This method will throw an exception if the view implements
     * {@link UnmodifiableView}. If it does not, and the forwarding type is
     * {@link ForwardingType#PURE}, this method forwards the request to the
     * backing collection; otherwise, the default implementation from
     * {@link IntCollection#addAll(Collection)} is used.</p>
     * @param collection collection containing elements to be added to this
     *                  collection
     * @return {@code true} if this collection changed as a result of the call
     * @throws CollectionNotModifiableException if this view is unmodifiable
     * @throws UnsupportedOperationException if the {@code addAll} operation
     * is not supported by this collection
     * @throws ClassCastException if the class of an element of the specified
     * collection prevents it form being added to this collection
     * @throws NullPointerException if the specified collection contains a
     * null element, or the specified collection is null
     * @throws IllegalArgumentException if some property of an element of the
     * specified collection prevents it from being added to this collection
     * @throws IllegalStateException if not all the elements can be added at
     * this time due to insertion restrictions
     */
    @Override
    public boolean addAll(Collection<? extends Integer> collection) {
        checkModifiable();
        return forwarder().predicateOp(delegate -> delegate.addAll(collection),
                () -> IntCollection.super.addAll(collection));
    }

    /**
     * <p>Adds all of the elements in the specified collection to this
     * collection (optional operation).</p>
     * @implNote <p>This method will throw an exception if the view implements
     * {@link UnmodifiableView}. If it does not, and the forwarding type is
     * {@link ForwardingType#PURE}, this method forwards the request to the
     * backing collection; otherwise, the default implementation from
     * {@link IntCollection#addAll(IntCollection)} is used.</p>
     * @param collection collection containing elements to be added to this
     *                  collection
     * @return {@code true} if this collection changed as a result of the call
     * @throws CollectionNotModifiableException if this view is unmodifiable
     * @throws UnsupportedOperationException if the {@code addAll} operation
     * is not supported by this collection
     * @throws NullPointerException if the specified collection is null
     * @throws IllegalArgumentException if some property of an element of the
     * specified collection prevents it from being added to this collection
     * @throws IllegalStateException if not all the elements can be added at
     * this time due to insertion restrictions
     */
    @Override
    public boolean addAll(IntCollection collection) {
        checkModifiable();
        return forwarder().predicateOp(delegate -> delegate.addAll(collection),
                () -> IntCollection.super.addAll(collection));
    }

    /**
     * <p>Ensures that this collection contains the specified element
     * (optional operation).</p>
     * @implNote <p>This method will throw an exception if the view implements
     * {@link UnmodifiableView}. If it does not, and the forwarding type is
     * {@link ForwardingType#PURE}, this method forwards the request to the
     * backing collection; otherwise, the default implementation from
     * {@link IntCollection#addInt(int)} ia used.</p>
     * @param element element whose presence in this collection is to be ensured
     * @return {@code true} if this collection changed as a result of the call
     * @throws CollectionNotModifiableException if this view is unmodifiable
     * @throws UnsupportedOperationException if the {@code addInt} operation
     * is not supported by this collection
     * @throws IllegalArgumentException if some property of the element
     * prevents it from being added to this collection
     * @throws IllegalStateException if the element cannot be added at this
     * time due to insertion restrictions
     */
    @Override
    public boolean addInt(int element) {
        checkModifiable();
        return forwarder().predicateOp(delegate -> delegate.addInt(element),
                () -> IntCollection.super.addInt(element));
    }

    /**
     * <p>Removes all of the elements from this collection (optional
     * operation).</p>
     * @implNote <p>This method will throw an exception if the view implements
     * {@link UnmodifiableView}. If it does not, and the forwarding type is
     * {@link ForwardingType#PURE}, this method forwards the request to the
     * backing collection; otherwise, the default implementation from
     * {@link IntCollection#clear()} is used.</p>
     * @throws CollectionNotModifiableException if this view is unmodifiable
     * @throws UnsupportedOperationException if the {@code clear} operation
     * is not supported by this collection
     */
    @Override
    public void clear() {
        checkModifiable();
        forwarder().voidOp(IntCollection::clear, IntCollection.super::clear);
    }

    /**
     * <p>Returns {@code true} if this collection contains the specified
     * element.</p>
     * @implNote <p>If forwarding type is {@link ForwardingType#PURE}, the
     * request is forwarded to the backing collection; otherwise, the default
     * implementation from {@link IntCollection#contains(Object)} is
     * used.</p>
     * @param o element whose presence in this set is to be tested
     * @return {@code true} if this set contains the specified element
     * @throws ClassCastException if the type of the specified element is
     * incompatible with this set (optional)
     * @throws NullPointerException if the specified element is null (optional)
     */
    @Override
    public boolean contains(Object o) {
        return forwarder().predicateOp(delegate -> delegate.contains(o),
                () -> IntCollection.super.contains(o));
    }

    /**
     * <p>Returns {@code true} if this collection contains all of the elements
     * of the specified collection.</p>
     * @implNote <p>If forwarding type is {@link ForwardingType#PURE}, the
     * request is forwarded to the backing connection; otherwise, the default
     * implementation from {@link IntCollection#containsAll(Collection)} is
     * used.</p>
     * @param collection collection to be checked for containment in this set
     * @return {@code true} if this set contains all of the elements of the
     * specified collection
     * @throws ClassCastException if the types of one or more elements in the
     * specified collection are incompatible with this set (optional)
     * @throws NullPointerException if the specified collection contains one
     * or more null elements (optional), or if the specified collection is null
     */
    @Override
    public boolean containsAll(Collection<?> collection) {
        return forwarder().predicateOp(delegate ->
                        delegate.containsAll(collection),
                () -> IntCollection.super.containsAll(collection));
    }

    /**
     * <p>Returns {@code true} if this collection contains all of the elements
     * of the specified collection.</p>
     * @implNote <p>If forwarding type is {@link ForwardingType#PURE}, the
     * request is forwarded to the backing collection; otherwise, the default
     * implementation from {@link IntCollection#containsAll(IntCollection)}
     * is used.</p>
     * @param collection collection to be checked for containment in this set
     * @return {@code true} if this set contains all of the elements of the
     * specified collection
     * @throws NullPointerException if the specified collection is null
     */
    @Override
    public boolean containsAll(IntCollection collection) {
        return forwarder().predicateOp(delegate ->
                        delegate.containsAll(collection),
                () -> IntCollection.super.containsAll(collection));
    }

    /**
     * <p>Returns {@code true} if this collection contains the specified
     * element.</p>
     * @implNote <p>If forwarding type is {@link ForwardingType#PURE}, the
     * request is forwarded to the backing collection; otherwise, the default
     * implementation from {@link IntCollection#containsInt(int)} is
     * used.</p>
     * @param element element whose presence in this set is to be tested
     * @return {@code true} if this set contains the specified element
     */
    @Override
    public boolean containsInt(int element) {
        return forwarder().predicateOp(delegate ->
                        delegate.containsInt(element),
                () -> IntCollection.super.containsInt(element));
    }

    /**
     * <p>Returns a primitive iterator over the elements in this
     * collection.</p>
     * @implSpec <p>This method must be overridden if forwarding type is
     * {@link ForwardingType#MINIMAL}. Additionally, if both this view and
     * the backing collection implement {@link UnmodifiableView}, the correct
     * behavior of this method is dependent on the correctness of the
     * backing collection's implementation (i.e. the backing collection must
     * not return an iterator capable of modifying its elements).</p>
     * @implNote <p>This method forwards the request to the backing collection
     * unless automatic forwarding of type-sensitive operations is forbidden
     * by the forwarding type. Additionally, if this view implements
     * {@link UnmodifiableView} and neither the backing collection nor the
     * iterator it returns do, an unmodifiable view of the iterator will be
     * returned.</p>
     * @return a primitive iterator over the elements in this collection
     * @throws InvalidForwardingContextException if forwarding type is
     * {@link ForwardingType#MINIMAL}
     */
    @Override
    public PrimitiveIterator.OfInt iterator() {
        return forwarder().maskIfNeeded(forwarder().boxedOp(IntCollection::iterator),
                IntIteratorView::unmodifiable);
    }

    /**
     * <p>Returns a possibly parallel {@code IntStream} with this collection
     * as its source. It is allowable for this method to return a sequential
     * int stream.</p>
     * @implNote <p>If forwarding type is {@link ForwardingType#PURE}, the
     * request is forwarded to the backing collection; otherwise, the default
     * implementation from {@link IntCollection#parallelPrimitiveStream()} is
     * used.</p>
     * @return a possibly parallel int stream over the elements in this
     * collection
     */
    @Override
    public IntStream parallelPrimitiveStream() {
        return forwarder().boxedOp(IntCollection::parallelPrimitiveStream,
                IntCollection.super::parallelPrimitiveStream);
    }

    /**
     * <p>Returns a sequential {@code IntStream} with this collection as its
     * source.</p>
     * @implNote <p>If forwarding type is {@link ForwardingType#PURE}, the
     * request is forwarded to the backing collection; otherwise, the default
     * implementation from {@link IntCollection#primitiveStream()} is
     * used.</p>
     * @return a sequential int stream over the elements in this collection
     */
    @Override
    public IntStream primitiveStream() {
        return forwarder().boxedOp(IntCollection::primitiveStream,
                IntCollection.super::primitiveStream);
    }

    /**
     * <p>Removes a single instance of the specified element from this
     * collection, if it is present (optional operation).</p>
     * @implNote <p>This method will throw an exception if the view implements
     * {@link UnmodifiableView}. If it does not, and forwarding type is
     * {@link ForwardingType#PURE}, the request is forwarded to the backing
     * collection; otherwise, the default implementation from
     * {@link IntCollection#remove(Object)} is used.</p>
     * @param o element to be removed from this collection, if present
     * @return {@code true} if an element was removed as a result of this call
     * @throws CollectionNotModifiableException if this view is unmodifiable
     * @throws ClassCastException if the type of the specified element is
     * incompatible with this collection (optional)
     * @throws NullPointerException if the specified element is null
     * (optional)
     * @throws UnsupportedOperationException if the {@code remove} operation
     * is not supported by this collection
     */
    @Override
    public boolean remove(Object o) {
        checkModifiable();
        return forwarder().predicateOp(delegate -> delegate.remove(o),
                () -> IntCollection.super.remove(o));
    }

    /**
     * <p>Removes all of this collection's elements that are also contained in
     * the specified collection (optional operation).</p>
     * @implNote <p>This method will throw an exception if the view implements
     * {@link UnmodifiableView}. If it does not, and forwarding type is
     * {@link ForwardingType#PURE}, the request is forwarded to the backing
     * collection; otherwise, the default implementation from
     * {@link IntCollection#retainAll(Collection)} is used.</p>
     * @param collection collection containing elements to be removed from this
     *                  collection
     * @return {@code true} if this collection changed as a result of the call
     * @throws CollectionNotModifiableException if this view is unmodifiable
     * @throws UnsupportedOperationException if the {@code removeAll}
     * operation is not supported by this collection
     * @throws ClassCastException if the {@code Integer} type is incompatible
     * with the specified collection (optional)
     * @throws NullPointerException if the specified collection is null
     */
    @Override
    public boolean removeAll(Collection<?> collection) {
        checkModifiable();
        return forwarder().predicateOp(delegate ->
                        delegate.removeAll(collection),
                () -> IntCollection.super.removeAll(collection));
    }

    /**
     * <p>Removes all of this collection's elements that are also contained in
     * the specified collection (optional operation).</p>
     * @implNote <p>This method will throw an exception if the view implements
     * {@link UnmodifiableView}. If it does not, and forwarding type is
     * {@link ForwardingType#PURE}, the request is forwarded to the backing
     * collection; otherwise, the default implementation from
     * {@link IntCollection#retainAll(IntCollection)} is used.</p>
     * @param collection collection containing elements to be removed from this
     *                  collection
     * @return {@code true} if this collection changed as a result of the call
     * @throws CollectionNotModifiableException if this view is unmodifiable
     * @throws UnsupportedOperationException if the {@code removeAll}
     * operation is not supported by this collection
     * @throws NullPointerException if the specified collection is null
     */
    @Override
    public boolean removeAll(IntCollection collection) {
        checkModifiable();
        return forwarder().predicateOp(delegate ->
                        delegate.removeAll(collection),
                () -> IntCollection.super.removeAll(collection));
    }

    /**
     * <p>Removes all of the elements of this collection that satisfy the
     * given predicate (optional operation).</p>
     * @implNote <p>This method will throw an exception if the view implements
     * {@link UnmodifiableView}. If it does not, and the forwarding type is
     * {@link ForwardingType#PURE}, the request is forwarded to the backing
     * collection; otherwise, the default implementation from
     * {@link IntCollection#removeIf(IntPredicate)} is used.</p>
     * @param filter a predicate which returns {@code true} for elements to be
     *               removed
     * @return {@code true} if any elements were removed
     * @throws CollectionNotModifiableException if this view is unmodifiable
     * @throws UnsupportedOperationException if the {@code removeIf} operation
     * is not supported by this collection
     * @throws NullPointerException if the specified filter is null
     */
    @Override
    public boolean removeIf(IntPredicate filter) {
        checkModifiable();
        return forwarder().predicateOp(delegate -> delegate.removeIf(filter),
                () -> IntCollection.super.removeIf(filter));
    }

    /**
     * <p>Removes all of the elements of this collection that satisfy the
     * given predicate (optional operation).</p>
     * @implNote <p>This method will throw an exception if the view implements
     * {@link UnmodifiableView}. If it does not, and the forwarding type is
     * {@link ForwardingType#PURE}, the request is forwarded to the backing
     * collection; otherwise, the default implementation from
     * {@link IntCollection#removeIf(Predicate)} is used.</p>
     * @param filter a predicate which returns {@code true} for elements to be
     *               removed
     * @return {@code true} if any elements were removed
     * @throws CollectionNotModifiableException if this view is unmodifiable
     * @throws UnsupportedOperationException if the {@code removeIf} operation
     * is not supported by this collection
     * @throws NullPointerException if the specified filter is null
     */
    @Override
    public boolean removeIf(Predicate<? super Integer> filter) {
        checkModifiable();
        return forwarder().predicateOp(delegate -> delegate.removeIf(filter),
                () -> IntCollection.super.removeIf(filter));
    }

    /**
     * <p>Removes all of the elements of this collection that satisfy the
     * given predicate (optional operation).</p>
     * @implNote <p>This method will throw an exception if the view implements
     * {@link UnmodifiableView}. If it does not, and the forwarding type is
     * {@link ForwardingType#PURE}, the request is forwarded to the backing
     * collection; otherwise, the default implementation from
     * {@link IntCollection#removeIfInt(IntPredicate)} is used.</p>
     * @param filter a predicate which returns {@code true} for elements to be
     *               removed
     * @return {@code true} if any elements were removed
     * @throws CollectionNotModifiableException if this view is unmodifiable
     * @throws UnsupportedOperationException if the {@code removeIfInt}
     * operation is not supported by this collection
     * @throws NullPointerException if the specified filter is null
     */
    @Override
    public boolean removeIfInt(IntPredicate filter) {
        checkModifiable();
        return forwarder().predicateOp(delegate ->
                        delegate.removeIfInt(filter),
                () -> IntCollection.super.removeIfInt(filter));
    }

    /**
     * <p>Removes a single instance of the specified element from this
     * collection, if it is present (optional operation).</p>
     * @implNote <p>This method will throw an exception if the view implements
     * {@link UnmodifiableView}. If it does not, and forwarding type is
     * {@link ForwardingType#PURE}, the request is forwarded to the backing
     * collection; otherwise, the default implementation from
     * {@link IntCollection#removeInt(int)} is used.</p>
     * @param element element to be removed from this collection, if present
     * @return {@code true} if an element was removed as a result of this call
     * @throws CollectionNotModifiableException is this view is unmodifiable
     * @throws UnsupportedOperationException if the {@code removeInt}
     * operation is not supported by this collection
     */
    @Override
    public boolean removeInt(int element) {
        checkModifiable();
        return forwarder().predicateOp(delegate ->
                        delegate.removeInt(element),
                () -> IntCollection.super.removeInt(element));
    }

    /**
     * <p>Retains only the elements in this collection that are contained in
     * the specified collection (optional operation).</p>
     * @implNote <p>This method will throw an exception if the view implements
     * {@link UnmodifiableView}. If it does not, and forwarding type is
     * {@link ForwardingType#PURE}m the request is forwarded to the backing
     * collection; otherwise, the default implementation from
     * {@link IntCollection#retainAll(Collection)} is used.</p>
     * @param collection collection containing elements to be retained in this
     *                  collection
     * @return {@code true} if this collection changed as a result of the call
     * @throws CollectionNotModifiableException if this view is unmodifiable
     * @throws UnsupportedOperationException if the {@code retainAll}
     * operation ia not supported by this collection
     * @throws ClassCastException if the {@code Integer} type is incompatible
     * with the specified collection (optional)
     * @throws NullPointerException if the specified collection is null
     */
    @Override
    public boolean retainAll(Collection<?> collection) {
        checkModifiable();
        return forwarder().predicateOp(delegate ->
                        delegate.retainAll(collection),
                () -> IntCollection.super.retainAll(collection));
    }

    /**
     * <p>Retains only the elements in this collection that are contained in
     * the specified collection (optional operation).</p>
     * @implNote <p>This method will throw an exception if the view implements
     * {@link UnmodifiableView}. If it does not, and forwarding type is
     * {@link ForwardingType#PURE}m the request is forwarded to the backing
     * collection; otherwise, the default implementation from
     * {@link IntCollection#retainAll(IntCollection)} is used.</p>
     * @param collection collection containing elements to be retained in this
     *                  collection
     * @return {@code true} if this collection changed as a result of the call
     * @throws CollectionNotModifiableException if this view is unmodifiable
     * @throws UnsupportedOperationException if the {@code retainAll}
     * operation ia not supported by this collection
     * @throws NullPointerException if the specified collection is null
     */
    @Override
    public boolean retainAll(IntCollection collection) {
        checkModifiable();
        return forwarder().predicateOp(delegate ->
                        delegate.retainAll(collection),
                () -> IntCollection.super.retainAll(collection));
    }

    /**
     * <p>Creates a {@code Spliterator} over the elements in this
     * collection.</p>
     * @apiNote <p>This method assumes that any spliterator returned will be
     * implicitly read-only. This should generally be a safe assumption, but
     * implementers are advised that returning an implementing class which
     * includes operations (beyond those specified by {@link Spliterator})
     * that modify the elements of the collection will violate the contract of
     * {@link UnmodifiableView}, this this class makes no attempt to guard
     * against such a backing collection that behaves in this way.</p>
     * @implNote <p>If the forwarding type is {@link ForwardingType#PURE},
     * the method forwards the request to the backing collection; otherwise,
     * it invokes the default implementation from
     * {@link IntCollection#spliterator()}.</p>
     * @return a spliterator over the elements in this collection
     */
    @Override
    public Spliterator.OfInt spliterator() {
        return forwarder().boxedOp(IntCollection::spliterator,
                IntCollection.super::spliterator);
    }

    /**
     * <p>Returns an array containing all of the elements in this
     * collection.</p>
     * @implNote <p>If the forwarding type is {@link ForwardingType#PURE}, the
     * method forwards the result to the backing collection; otherwise, it
     * invokes the default implementation from
     * {@link IntCollection#toPrimitiveArray()}.</p>
     * @return an array containing all the elements in this set
     */
    @Override
    public int[] toPrimitiveArray() {
        return forwarder().boxedOp(IntCollection::toPrimitiveArray,
                IntCollection.super::toPrimitiveArray);
    }
}
