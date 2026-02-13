package net.arinoru.util.views;

import net.arinoru.util.CollectionNotModifiableException;
import net.arinoru.util.LongCollection;
import net.arinoru.util.LongSet;
import net.arinoru.util.PrimitiveSet;

import java.util.Collection;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.function.LongConsumer;
import java.util.function.LongPredicate;
import java.util.function.Predicate;
import java.util.stream.LongStream;

/**
 * <p>An internal implementation of a long set view. This class defines a
 * series of default behaviors for set operations based on a {@link Forwarder},
 * an object that provides access to a backing set and means of providing
 * varying levels of access to functionality based on a configured
 * {@link ForwardingType}.</p>
 * <p>The requirements for the backing set vary based on the forwarding type,
 * with {@link ForwardingType#MINIMAL} allowing the use of any backing set,
 * while {@link ForwardingType#PURE} and {@link ForwardingType#SHALLOW} both
 * require the backing set to be a {@link LongSet}.</p>
 * <p>The general rule for the implementation of set operations in this class
 * depends on the forwarding type and whether or not the {@link LongSet}
 * interface provides a default implementation for that operation. In
 * general:</p>
 * <ol>
 *     <li>Operations that (potentially) modify the contents of the set will
 *     throw a {@link CollectionNotModifiableException} if this view
 *     implements {@link UnmodifiableView}.</li>
 *     <li>Operations that are not sensitive to the element type of the set
 *     and which do not have default implementations are forwarded directly to
 *     the backing set, regardless of forwarding type.</li>
 *     <li>Operations which have a default method defined in the
 *     {@link LongSet} interface are forwarded to the backing set if the
 *     forwarding type is {@link ForwardingType#PURE}; otherwise, they are
 *     implemented in terms of the default method, which may itself invoke
 *     other set operations on the view.</li>
 *     <li>Operations that are sensitive to the element type of the set (e.g.
 *     those that add or remove elements or are otherwise bounded by subtype,
 *     or methods that are only specified by {@link LongSet} or
 *     {@link PrimitiveSet}) and do not have a default implementation in
 *     {@link LongSet} will throw an
 *     {@link InvalidForwardingContextException} if the forwarding type is
 *     {@link ForwardingType#MINIMAL}.</li>
 *     <li>Operations not meeting the above criteria will generally forward
 *     to the backing set. If some aspect of the type difference between the
 *     view and the backing set prevents the proper operation of the forward,
 *     an exception may be thrown (e.g.
 *     {@link InvalidForwardingContextException}, {@link ClassCastException},
 *     or {@link IllegalStateException}).</li>
 * </ol>
 */
public abstract class AbstractLongSetView extends AbstractPrimitiveSetView<Long,long[],
        LongConsumer,LongPredicate,PrimitiveIterator.OfLong,Spliterator.OfLong,LongStream,
        LongCollection,LongSet> implements LongSet {
    /**
     * <p>Adds the specified element to this set if it is not already present
     * (optional operation).</p>
     * @implNote <p>This method will throw an exception if the view implements
     * {@link UnmodifiableView}. If it does not, and the forwarding type is
     * {@link ForwardingType#PURE}, this method forwards the request to the
     * backing set; otherwise, the default implementation from
     * {@link LongSet#add(Long)} is used.</p>
     * @param element element to be added to this set
     * @return {@code true} if this set did not already contain the specified
     * element
     * @throws CollectionNotModifiableException if this view is unmodifiable
     * @throws UnsupportedOperationException if the {@code add} operation is
     * not supported by the set
     * @throws NullPointerException if the specified element is null
     */
    @Override
    public boolean add(Long element) {
        checkModifiable();
        return forwarder().predicateOp(delegate -> delegate.add(element),
                () -> LongSet.super.add(element));
    }

    /**
     * <p>Adds all of the elements in the specified collection to this set if
     * they're not already present (optional operation).</p>
     * @implNote <p>This method will throw an exception if the view implements
     * {@link UnmodifiableView}. If it does not, and the forwarding type is
     * {@link ForwardingType#PURE}, this method forwards the request to the
     * backing set; otherwise, the default implementation from
     * {@link LongSet#addAll(Collection)} is used.</p>
     * @param collection collection containing elements to be added to this set
     * @return {@code true} if this set changed as a result of the call
     * @throws CollectionNotModifiableException if this view is unmodifiable
     * @throws UnsupportedOperationException if the {@code addAll} operation is
     * not supported by the set
     * @throws NullPointerException if the specified collection contains one or
     * more null elements, or if the specified collection is null
     */
    @Override
    public boolean addAll(Collection<? extends Long> collection) {
        checkModifiable();
        return forwarder().predicateOp(delegate -> delegate.addAll(collection),
                () -> LongSet.super.addAll(collection));
    }

    /**
     * <p>Adds all of the elements in the specified collection to this set if
     * they're not already present (optional operation).</p>
     * @implNote <p>This method will throw an exception if the view implements
     * {@link UnmodifiableView}. If it does not, and the forwarding type is
     * {@link ForwardingType#PURE}, this method forwards the request to the
     * backing set; otherwise, the default implementation from
     * {@link LongSet#addAll(LongCollection)} is used.</p>
     * @param collection collection containing elements to be added to this set
     * @return {@code true} if this set changed as a result of the call
     * @throws CollectionNotModifiableException if this view is unmodifiable
     * @throws UnsupportedOperationException if the {@code addAll} operation is
     * not supported by the set
     * @throws NullPointerException if the specified collection contains one or
     * more null elements, or if the specified collection is null
     */
    @Override
    public boolean addAll(LongCollection collection) {
        checkModifiable();
        return forwarder().predicateOp(delegate -> delegate.addAll(collection),
                () -> LongSet.super.addAll(collection));
    }

    /**
     * <p>Adds the specified element to this set if it is not already present
     * (optional operation).</p>
     * @implNote <p>This method will throw an exception if the view implements
     * {@link UnmodifiableView}. If it does not, and the forwarding type is
     * {@link ForwardingType#PURE}, this method forwards the request to the
     * backing set; otherwise, the default implementation from
     * {@link LongSet#addLong(long)} is used.</p>
     * @param element element to be added to this set
     * @return {@code true} if this set did not already contain the specified
     * element
     * @throws CollectionNotModifiableException if this view is unmodifiable
     * @throws UnsupportedOperationException if the {@code addLong} operation is
     * not supported by this set
     */
    @Override
    public boolean addLong(long element) {
        checkModifiable();
        return forwarder().predicateOp(delegate -> delegate.addLong(element),
                () -> LongSet.super.addLong(element));
    }

    /**
     * <p>Removes all of the elements from this set (optional operation).</p>
     * @implNote <p>This method will throw an exception if the view implements
     * {@link UnmodifiableView}. If it does not, and the forwarding type is
     * {@link ForwardingType#PURE}, this method forwards the request to the
     * backing set; otherwise, the default implementation from
     * {@link LongSet#clear()} is used.</p>
     * @throws CollectionNotModifiableException if this view is unmodifiable
     * @throws UnsupportedOperationException if the {@code clear} operation
     * is not supported by this set
     */
    @Override
    public void clear() {
        checkModifiable();
        forwarder().voidOp(LongSet::clear, LongSet.super::clear);
    }

    /**
     * <p>Returns {@code true} if this set contains the specified element.</p>
     * @implNote <p>If forwarding type is {@link ForwardingType#PURE}, the
     * request is forwarded to the backing set; otherwise, the default
     * implementation from {@link LongSet#contains(Object)} is used.</p>
     * @param o element whose presence in this set is to be tested
     * @return {@code true} if this set contains the specified element
     * @throws ClassCastException if the type of the specified element is
     * incompatible with this set (optional)
     * @throws NullPointerException if the specified element is null (optional)
     */
    @Override
    public boolean contains(Object o) {
        return forwarder().predicateOp(delegate -> delegate.contains(o),
                () -> LongSet.super.contains(o));
    }

    /**
     * <p>Returns {@code true} if this set contains all of the elements of the
     * specified collection.</p>
     * @implNote <p>If forwarding type is {@link ForwardingType#PURE}, the
     * request is forwarded to the backing set; otherwise, the default
     * implementation from {@link LongSet#containsAll(Collection)} is used.</p>
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
                () -> LongSet.super.containsAll(collection));
    }

    /**
     * <p>Returns {@code true} if this set contains all of the elements of the
     * specified collection.</p>
     * @implNote <p>If forwarding type is {@link ForwardingType#PURE}, the
     * request is forwarded to the backing set; otherwise, the default
     * implementation from {@link LongSet#containsAll(LongCollection)} is
     * used.</p>
     * @param collection collection to be checked for containment in this set
     * @return {@code true} if this set contains all of the elements of the
     * specified collection
     * @throws NullPointerException if the specified collection is null
     */
    @Override
    public boolean containsAll(LongCollection collection) {
        return forwarder().predicateOp(delegate ->
                        delegate.containsAll(collection),
                () -> LongSet.super.containsAll(collection));
    }

    /**
     * <p>Returns {@code true} if this set contains the specified element.</p>
     * @implNote <p>If forwarding type is {@link ForwardingType#PURE}, the
     * request is forwarded to the backing set; otherwise, the default
     * implementation from {@link LongSet#containsLong(long)} is used.</p>
     * @param element element whose presence in this set is to be tested
     * @return {@code true} if this set contains the specified element
     */
    @Override
    public boolean containsLong(long element) {
        return forwarder().predicateOp(delegate -> delegate.containsLong(element),
                () -> LongSet.super.containsLong(element));
    }

    /**
     * <p>Returns a primitive iterator over the elements in this set.</p>
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
     * @return a primitive iterator over the elements in this set
     * @throws InvalidForwardingContextException if forwarding type is
     * {@link ForwardingType#MINIMAL}
     */
    @Override
    public PrimitiveIterator.OfLong iterator() {
        return forwarder().maskIfNeeded(forwarder().boxedOp(LongSet::iterator),
                LongIteratorView::unmodifiable);
    }

    /**
     * <p>Returns a possibly parallel {@code LongStream} with this set as its
     * source. It is allowable for this method to return a sequential long
     * stream.</p>
     * @implNote <p>If forwarding type is {@link ForwardingType#PURE}, the
     * request is forwarded to the backing set; otherwise, the default
     * implementation from {@link LongSet#parallelPrimitiveStream()} is
     * used.</p>
     * @return a possibly parallel long stream over the elements in this set
     */
    @Override
    public LongStream parallelPrimitiveStream() {
        return forwarder().boxedOp(LongSet::parallelPrimitiveStream,
                LongSet.super::parallelPrimitiveStream);
    }

    /**
     * <p>Returns a sequential {@code LongStream} with this set as its
     * source.</p>
     * @implNote <p>If forwarding type is {@link ForwardingType#PURE}, the
     * request is forwarded to the backing set; otherwise, the default
     * implementation from {@link LongSet#primitiveStream()} is used.</p>
     * @return a sequential long stream over the elements in this set
     */
    @Override
    public LongStream primitiveStream() {
        return forwarder().boxedOp(LongSet::primitiveStream,
                LongSet.super::primitiveStream);
    }

    /**
     * <p>Removes the specified element from this set if it is present
     * (optional operation).</p>
     * @implNote <p>This method will throw an exception if the view implements
     * {@link UnmodifiableView}. If it does not, and forwarding type is
     * {@link ForwardingType#PURE}, the request is forwarded to the backing
     * set; otherwise, the default implementation from
     * {@link LongSet#remove(Object)} is used.</p>
     * @param o element to be removed from this set, if present
     * @return {@code true} if this set contained the specified element
     * @throws CollectionNotModifiableException if this view is unmodifiable
     * @throws UnsupportedOperationException if the {@code remove} operation
     * is not supported by this set
     * @throws ClassCastException if the type of the specified element is
     * incompatible with this set (optional)
     * @throws NullPointerException if the specified element is null (optional)
     */
    @Override
    public boolean remove(Object o) {
        checkModifiable();
        return forwarder().predicateOp(delegate -> delegate.remove(o),
                () -> LongSet.super.remove(o));
    }

    /**
     * <p>Removes from this set all of its elements that are contained in the
     * specified collection (optional operation).</p>
     * @implNote <p>This method will throw an exception if the view implements
     * {@link UnmodifiableView}. If it does not, and forwarding type is
     * {@link ForwardingType#PURE}, the request is forwarded to the backing
     * set; otherwise, the default implementation from
     * {@link LongSet#removeAll(Collection)} is used.</p>
     * @param collection collection containing elements to be removed from
     *                   this set
     * @return {@code true} if this set changed as a result of the call
     * @throws CollectionNotModifiableException if this view is unmodifiable
     * @throws UnsupportedOperationException if the {@code removeAll} operation
     * is not supported by this set
     * @throws ClassCastException if the {@code Long} type is incompatible with
     * the specified collection (optional)
     * @throws NullPointerException if the specified collection is null
     */
    @Override
    public boolean removeAll(Collection<?> collection) {
        checkModifiable();
        return forwarder().predicateOp(delegate ->
                        delegate.removeAll(collection),
                () -> LongSet.super.removeAll(collection));
    }

    /**
     * <p>Removes from this set all of its elements that are contained in the
     * specified collection (optional operation).</p>
     * @implNote <p>This method will throw an exception if the view implements
     * {@link UnmodifiableView}. If it does not, and forwarding type is
     * {@link ForwardingType#PURE}, the request is forwarded to the backing
     * set; otherwise, the default implementation from
     * {@link LongSet#removeAll(LongCollection)} is used.</p>
     * @param collection collection containing elements to be removed from
     *                  this set
     * @return {@code true} if this set changed as a result of the call
     * @throws CollectionNotModifiableException if this view is unmodifiable
     * @throws UnsupportedOperationException if the {@code removeAll} operation
     * is not supported by this set
     * @throws NullPointerException if the specified collection is null
     */
    @Override
    public boolean removeAll(LongCollection collection) {
        checkModifiable();
        return forwarder().predicateOp(delegate ->
                        delegate.removeAll(collection),
                () -> LongSet.super.removeAll(collection));
    }

    /**
     * <p>Removes from this set all of its elements that match the specified
     * filter (optional operation).</p>
     * @implNote <p>This method will throw an exception if the view implements
     * {@link UnmodifiableView}. If it does not, and the forwarding type is
     * {@link ForwardingType#PURE}, the request is forwarded to the backing
     * set; otherwise, the default implementation from
     * {@link LongSet#removeIf(LongPredicate)} is used.</p>
     * @param filter a predicate which returns {@code true} for elements to be
     *               removed
     * @return {@code true} if this set changed as a result of the call
     * @throws CollectionNotModifiableException if this view is unmodifiable
     * @throws UnsupportedOperationException if the {@code removeIf} operation
     * is not supported by this set
     * @throws NullPointerException if the specified filter is null
     */
    @Override
    public boolean removeIf(LongPredicate filter) {
        checkModifiable();
        return forwarder().predicateOp(delegate -> delegate.removeIf(filter),
                () -> LongSet.super.removeIf(filter));
    }

    /**
     * <p>Removes from this set all of its elements that match the specified
     * filter (optional operation).</p>
     * @implNote <p>This method will throw an exception if the view implements
     * {@link UnmodifiableView}. If it does not, and the forwarding type is
     * {@link ForwardingType#PURE}, the request is forwarded to the backing
     * set; otherwise, the default implementation from
     * {@link LongSet#removeIf(Predicate)} is used.</p>
     * @param filter a predicate which returns {@code true} for elements to be
     *               removed
     * @return {@code true} if this set changed as a result of the call
     * @throws CollectionNotModifiableException if this view is unmodifiable
     * @throws UnsupportedOperationException if the {@code removeIf} operation
     * is not supported by this set
     * @throws NullPointerException if the specified filter is null
     */
    @Override
    public boolean removeIf(Predicate<? super Long> filter) {
        checkModifiable();
        return forwarder().predicateOp(delegate -> delegate.removeIf(filter),
                () -> LongSet.super.removeIf(filter));
    }

    /**
     * <p>Removes from this set all of its elements that match the specified
     * filter (optional operation).</p>
     * @implNote <p>This method will throw an exception if the view implements
     * {@link UnmodifiableView}. If it does not, and the forwarding type is
     * {@link ForwardingType#PURE}, the request is forwarded to the backing
     * set; otherwise, the default implementation from
     * {@link LongSet#removeIfLong(LongPredicate)} is used.</p>
     * @param filter a predicate which returns {@code true} for elements to be
     *               removed
     * @return {@code true} if this set changed as a result of the call
     * @throws CollectionNotModifiableException if this view is unmodifiable
     * @throws UnsupportedOperationException if the {@code removeIf} operation
     * is not supported by this set
     * @throws NullPointerException if the specified filter is null
     */
    @Override
    public boolean removeIfLong(LongPredicate filter) {
        checkModifiable();
        return forwarder().predicateOp(delegate -> delegate.removeIfLong(filter),
                () -> LongSet.super.removeIfLong(filter));
    }

    /**
     * <p>Removes the specified element from this set if it is present
     * (optional operation).</p>
     * @implNote <p>This method will throw an exception if the view implements
     * {@link UnmodifiableView}. If it does not, and forwarding type is
     * {@link ForwardingType#PURE}, the request is forwarded to the backing
     * set; otherwise, the default implementation from
     * {@link LongSet#removeLong(long)} is used.</p>
     * @param element element to be removed from this set, if present
     * @return {@code true} if this set contained the specified element
     * @throws CollectionNotModifiableException if this view is unmodifiable
     * @throws UnsupportedOperationException if the {@code remove} operation
     * is not supported by this set
     */
    @Override
    public boolean removeLong(long element) {
        checkModifiable();
        return forwarder().predicateOp(delegate -> delegate.removeLong(element),
                () -> LongSet.super.removeLong(element));
    }

    /**
     * <p>Retains only the elements in this set that are contained in the
     * specified collection (optional operation).</p>
     * @implNote <p>This method will throw an exception if the view implements
     * {@link UnmodifiableView}. If it does not, and forwarding type is
     * {@link ForwardingType#PURE}, the request is forwarded to the backing
     * set; otherwise, the default implementation from
     * {@link LongSet#retainAll(Collection)} is used.</p>
     * @param collection collection containing elements to be retained in
     *                   this set
     * @return {@code true} if this set changed as a result of the call
     * @throws CollectionNotModifiableException if this view is unmodifiable
     * @throws UnsupportedOperationException if the {@code retainAll}
     * operation is not supported by this set
     * @throws ClassCastException if the {@code Long} type is incompatible with
     * the specified collection (optional)
     * @throws NullPointerException if the specified collection is null
     */
    @Override
    public boolean retainAll(Collection<?> collection) {
        checkModifiable();
        return forwarder().predicateOp(delegate -> delegate.retainAll(collection),
                () -> LongSet.super.retainAll(collection));
    }

    /**
     * <p>Returns only the elements in this set that are contained in the
     * specified collection (optional operation).</p>
     * @implNote <p>This method will throw an exception if the view implements
     * {@link UnmodifiableView}. If it does not, and forwarding type is
     * {@link ForwardingType#PURE}, the request is forwarded to the backing
     * set; otherwise, the default implementation from
     * {@link LongSet#retainAll(LongCollection)} is used.</p>
     * @param collection collection containing elements to be retained in this
     *                  set
     * @return {@code true} if this set changed a a result of the call
     * @throws CollectionNotModifiableException if this view is unmodifiable
     * @throws UnsupportedOperationException if the {@code retainAll}
     * operation is not supported by this set
     * @throws NullPointerException if the specified collection is null
     */
    @Override
    public boolean retainAll(LongCollection collection) {
        checkModifiable();
        return forwarder().predicateOp(delegate -> delegate.retainAll(collection),
                () -> LongSet.super.retainAll(collection));
    }

    /**
     * <p>Creates a spliterator over the elements in this set.</p>
     * @apiNote <p>This method assumes that any spliterator returned will be
     * implicitly read-only. This should generally be a safe assumption, but
     * implementers are advised that returning an implementing class which
     * includes operations (beyond those specified by {@link Spliterator})
     * that modify the elements of the set will violate the contract of
     * {@link UnmodifiableView}, and this class makes no attempt to guard
     * against a backing set that behaves in this way.</p>
     * @implNote <p>If the forwarding type is {@link ForwardingType#PURE},
     * the method forwards the request to the backing set; otherwise, it
     * invokes the default implementation from
     * {@link LongSet#spliterator()}.</p>
     * @return a spliterator over the elements in this set
     */
    @Override
    public Spliterator.OfLong spliterator() {
        return forwarder().boxedOp(LongSet::spliterator, LongSet.super::spliterator);
    }

    /**
     * <p>Returns an array containing all of the elements in this set.</p>
     * @implNote <p>If the forwarding type is {@link ForwardingType#PURE},
     * the method forwards the request to the backing set; otherwise, it
     * invokes the default implementation from
     * {@link LongSet#toPrimitiveArray()}.</p>
     * @return an array containing all the elements in this set
     */
    @Override
    public long[] toPrimitiveArray() {
        return forwarder().boxedOp(LongSet::toPrimitiveArray,
                LongSet.super::toPrimitiveArray);
    }
}
