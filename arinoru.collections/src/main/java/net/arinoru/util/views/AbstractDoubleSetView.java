package net.arinoru.util.views;

import net.arinoru.util.CollectionNotModifiableException;
import net.arinoru.util.DoubleCollection;
import net.arinoru.util.DoubleSet;
import net.arinoru.util.PrimitiveSet;

import java.util.Collection;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.function.DoubleConsumer;
import java.util.function.DoublePredicate;
import java.util.function.Predicate;
import java.util.stream.DoubleStream;

/**
 * <p>An internal implementation of a double set view. This class defines a
 * series of default behaviors for set operations based on a {@link Forwarder},
 * an object that provides access to a backing set and means of providing
 * varying levels of access to functionality based on a configured
 * {@link ForwardingType}.</p>
 * <p>The requirements for the backing set vary based on the forwarding type,
 * with {@link ForwardingType#MINIMAL} allowing the use of any backing set,
 * while {@link ForwardingType#PURE} and {@link ForwardingType#SHALLOW} both
 * require the backing set to be a {@link DoubleSet}.</p>
 * <p>The general rule for the implementation of set operations in this class
 * depends on the forwarding type and whether or not the {@link DoubleSet}
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
 *     {@link DoubleSet} interface are forwarded to the backing set if the
 *     forwarding type is {@link ForwardingType#PURE}; otherwise, they are
 *     implemented in terms of the default method, which may itself invoke
 *     other set operations on the view.</li>
 *     <li>Operations that are sensitive to the element type of the set (e.g.
 *     those that add or remove elements or are otherwise bounded by subtype,
 *     or methods that are only specified by {@link DoubleSet} or
 *     {@link PrimitiveSet}) and do not have a default implementation in one
 *     of those interfaces will throw an
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
public abstract class AbstractDoubleSetView extends AbstractPrimitiveSetView<Double,
        double[],DoubleConsumer,DoublePredicate,PrimitiveIterator.OfDouble,
        Spliterator.OfDouble,DoubleStream,DoubleCollection,DoubleSet>
        implements DoubleSet {
    /**
     * <p>Adds the specified element to this set if it is not already present
     * (optional operation).</p>
     * @implNote <p>This method will throw an exception if the view implements
     * {@link UnmodifiableView}. If it does not, and the forwarding type is
     * {@link ForwardingType#PURE}, this method forwards the request to the
     * backing set; otherwise, the default implementation from
     * {@link DoubleSet#add(Double)} is used.</p>
     * @param element element to be added to this set
     * @return {@code true} if this set did not already contain the specified
     * element
     * @throws CollectionNotModifiableException if this view is unmodifiable
     * @throws UnsupportedOperationException if the {@code add} operation is
     * not supported by the set
     * @throws NullPointerException if the specified element is null
     */
    @Override
    public boolean add(Double element) {
        checkModifiable();
        return forwarder().predicateOp(delegate -> delegate.add(element),
                () -> DoubleSet.super.add(element));
    }

    /**
     * <p>Adds all of the elements in the specified collection to this set if
     * they're not already present (optional operation).</p>
     * @implNote <p>This method will throw an exception if the view implements
     * {@link UnmodifiableView}. If it does not, and the forwarding type is
     * {@link ForwardingType#PURE}, this method forwards the request to the
     * backing set; otherwise, the default implementation from
     * {@link DoubleSet#addAll(Collection)} is used.</p>
     * @param collection collection containing elements to be added to this set
     * @return {@code true} if this set changed as a result of the call
     * @throws CollectionNotModifiableException if this view is unmodifiable
     * @throws UnsupportedOperationException if the {@code addAll} operation is
     * not supported by the set
     * @throws NullPointerException if the specified collection contains one or
     * more null elements, or if the specified collection is null
     */
    @Override
    public boolean addAll(Collection<? extends Double> collection) {
        checkModifiable();
        return forwarder().predicateOp(delegate -> delegate.addAll(collection),
                () -> DoubleSet.super.addAll(collection));
    }

    /**
     * <p>Adds all of the elements in the specified collection to this set if
     * they're not already present (optional operation).</p>
     * @implNote <p>This method will throw an exception if the view implements
     * {@link UnmodifiableView}. If it does not, and the forwarding type is
     * {@link ForwardingType#PURE}, this method forwards the request to the
     * backing set; otherwise, the default implementation from
     * {@link DoubleSet#addAll(DoubleCollection)} is used.</p>
     * @param collection collection containing elements to be added to this set
     * @return {@code true} if this set changed as a result of the call
     * @throws CollectionNotModifiableException if this view is unmodifiable
     * @throws UnsupportedOperationException if the {@code addAll} operation is
     * not supported by the set
     * @throws NullPointerException if the specified collection contains one or
     * more null elements, or if the specified collection is null
     */
    @Override
    public boolean addAll(DoubleCollection collection) {
        checkModifiable();
        return forwarder().predicateOp(delegate -> delegate.addAll(collection),
                () -> DoubleSet.super.addAll(collection));
    }

    /**
     * <p>Adds the specified element to this set if it is not already present
     * (optional operation).</p>
     * @implNote <p>This method will throw an exception if the view implements
     * {@link UnmodifiableView}. If it does not, and the forwarding type is
     * {@link ForwardingType#PURE}, this method forwards the request to the
     * backing set; otherwise, the default implementation from
     * {@link DoubleSet#addDouble(double)} is used.</p>
     * @param element element to be added to this set
     * @return {@code true} if this set did not already contain the specified
     * element
     * @throws CollectionNotModifiableException if this view is unmodifiable
     * @throws UnsupportedOperationException if the {@code addDouble} operation is
     * not supported by this set
     */
    @Override
    public boolean addDouble(double element) {
        checkModifiable();
        return forwarder().predicateOp(delegate -> delegate.addDouble(element),
                () -> DoubleSet.super.addDouble(element));
    }

    /**
     * <p>Removes all of the elements from this set (optional operation).</p>
     * @implNote <p>This method will throw an exception if the view implements
     * {@link UnmodifiableView}. If it does not, and the forwarding type is
     * {@link ForwardingType#PURE}, this method forwards the request to the
     * backing set; otherwise, the default implementation from
     * {@link DoubleSet#clear()} is used.</p>
     * @throws CollectionNotModifiableException if this view is unmodifiable
     * @throws UnsupportedOperationException if the {@code clear} operation
     * is not supported by this set
     */
    @Override
    public void clear() {
        checkModifiable();
        forwarder().voidOp(DoubleSet::clear, DoubleSet.super::clear);
    }

    /**
     * <p>Returns {@code true} if this set contains the specified element.</p>
     * @implNote <p>If forwarding type is {@link ForwardingType#PURE}, the
     * request is forwarded to the backing set; otherwise, the default
     * implementation from {@link DoubleSet#contains(Object)} is used.</p>
     * @param o element whose presence in this set is to be tested
     * @return {@code true} if this set contains the specified element
     * @throws ClassCastException if the type of the specified element is
     * incompatible with this set (optional)
     * @throws NullPointerException if the specified element is null (optional)
     */
    @Override
    public boolean contains(Object o) {
        return forwarder().predicateOp(delegate -> delegate.contains(o),
                () -> DoubleSet.super.contains(o));
    }

    /**
     * <p>Returns {@code true} if this set contains all of the elements of the
     * specified collection.</p>
     * @implNote <p>If forwarding type is {@link ForwardingType#PURE}, the
     * request is forwarded to the backing set; otherwise, the default
     * implementation from {@link DoubleSet#containsAll(Collection)} is
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
                () -> DoubleSet.super.containsAll(collection));
    }

    /**
     * <p>Returns {@code true} if this set contains all of the elements of the
     * specified collection.</p>
     * @implNote <p>If forwarding type is {@link ForwardingType#PURE}, the
     * request is forwarded to the backing set; otherwise, the default
     * implementation from {@link DoubleSet#containsAll(DoubleCollection)} is
     * used.</p>
     * @param collection collection to be checked for containment in this set
     * @return {@code true} if this set contains all of the elements of the
     * specified collection
     * @throws NullPointerException if the specified collection is null
     */
    @Override
    public boolean containsAll(DoubleCollection collection) {
        return forwarder().predicateOp(delegate ->
                        delegate.containsAll(collection),
                () -> DoubleSet.super.containsAll(collection));
    }

    /**
     * <p>Returns {@code true} if this set contains the specified element.</p>
     * @implNote <p>If forwarding type is {@link ForwardingType#PURE}, the
     * request is forwarded to the backing set; otherwise, the default
     * implementation from {@link DoubleSet#containsDouble(double)} is used.</p>
     * @param element element whose presence in this set is to be tested
     * @return {@code true} if this set contains the specified element
     */
    @Override
    public boolean containsDouble(double element) {
        return forwarder().predicateOp(delegate ->
                        delegate.containsDouble(element),
                () -> DoubleSet.super.containsDouble(element));
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
    public PrimitiveIterator.OfDouble iterator() {
        return forwarder().maskIfNeeded(forwarder().boxedOp(DoubleSet::iterator),
                DoubleIteratorView::unmodifiable);
    }

    /**
     * <p>Returns a possibly parallel {@code DoubleStream} with this set as
     * its source. It is allowable for this method to return a sequential
     * double stream.</p>
     * @implNote <p>If forwarding type is {@link ForwardingType#PURE}, the
     * request is forwarded to the backing set; otherwise, the default
     * implementation from {@link DoubleSet#parallelPrimitiveStream()} is
     * used.</p>
     * @return a possibly parallel double stream over the elements in this set
     */
    @Override
    public DoubleStream parallelPrimitiveStream() {
        return forwarder().boxedOp(DoubleSet::parallelPrimitiveStream,
                DoubleSet.super::parallelPrimitiveStream);
    }

    /**
     * <p>Returns a sequential {@code DoubleStream} with this set as its
     * source.</p>
     * @implNote <p>If forwarding type is {@link ForwardingType#PURE}, the
     * request is forwarded to the backing set; otherwise, the default
     * implementation from {@link DoubleSet#primitiveStream()} is used.</p>
     * @return a sequential double stream over the elements in this set
     */
    @Override
    public DoubleStream primitiveStream() {
        return forwarder().boxedOp(DoubleSet::primitiveStream,
                DoubleSet.super::primitiveStream);
    }

    /**
     * <p>Removes the specified element from this set if it is present
     * (optional operation).</p>
     * @implNote <p>This method will throw an exception if the view implements
     * {@link UnmodifiableView}. If it does not, and forwarding type is
     * {@link ForwardingType#PURE}, the request is forwarded to the backing
     * set; otherwise, the default implementation from
     * {@link DoubleSet#remove(Object)} is used.</p>
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
                () -> DoubleSet.super.remove(o));
    }

    /**
     * <p>Removes from this set all of its elements that are contained in the
     * specified collection (optional operation).</p>
     * @implNote <p>This method will throw an exception if the view implements
     * {@link UnmodifiableView}. If it does not, and forwarding type is
     * {@link ForwardingType#PURE}, the request is forwarded to the backing
     * set; otherwise, the default implementation from
     * {@link DoubleSet#removeAll(Collection)} is used.</p>
     * @param collection collection containing elements to be removed from
     *                   this set
     * @return {@code true} if this set changed as a result of the call
     * @throws CollectionNotModifiableException if this view is unmodifiable
     * @throws UnsupportedOperationException if the {@code removeAll} operation
     * is not supported by this set
     * @throws ClassCastException if the {@code Double} type is incompatible
     * with the specified collection (optional)
     * @throws NullPointerException if the specified collection is null
     */
    @Override
    public boolean removeAll(Collection<?> collection) {
        checkModifiable();
        return forwarder().predicateOp(delegate ->
                        delegate.removeAll(collection),
                () -> DoubleSet.super.removeAll(collection));
    }

    /**
     * <p>Removes from this set all of its elements that are contained in the
     * specified collection (optional operation).</p>
     * @implNote <p>This method will throw an exception if the view implements
     * {@link UnmodifiableView}. If it does not, and forwarding type is
     * {@link ForwardingType#PURE}, the request is forwarded to the backing
     * set; otherwise, the default implementation from
     * {@link DoubleSet#removeAll(DoubleCollection)} is used.</p>
     * @param collection collection containing elements to be removed from
     *                  this set
     * @return {@code true} if this set changed as a result of the call
     * @throws CollectionNotModifiableException if this view is unmodifiable
     * @throws UnsupportedOperationException if the {@code removeAll} operation
     * is not supported by this set
     * @throws NullPointerException if the specified collection is null
     */
    @Override
    public boolean removeAll(DoubleCollection collection) {
        checkModifiable();
        return forwarder().predicateOp(delegate ->
                        delegate.removeAll(collection),
                () -> DoubleSet.super.removeAll(collection));
    }

    /**
     * <p>Removes the specified element from this set if it is present
     * (optional operation).</p>
     * @implNote <p>This method will throw an exception if the view implements
     * {@link UnmodifiableView}. If it does not, and forwarding type is
     * {@link ForwardingType#PURE}, the request is forwarded to the backing
     * set; otherwise, the default implementation from
     * {@link DoubleSet#removeDouble(double)} is used.</p>
     * @param element element to be removed from this set, if present
     * @return {@code true} if this set contained the specified element
     * @throws CollectionNotModifiableException if this view is unmodifiable
     * @throws UnsupportedOperationException if the {@code remove} operation
     * is not supported by this set
     */
    @Override
    public boolean removeDouble(double element) {
        checkModifiable();
        return forwarder().predicateOp(delegate ->
                        delegate.removeDouble(element),
                () -> DoubleSet.super.removeDouble(element));
    }

    /**
     * <p>Removes from this set all of its elements that match the specified
     * filter (optional operation).</p>
     * @implNote <p>This method will throw an exception if the view implements
     * {@link UnmodifiableView}. If it does not, and the forwarding type is
     * {@link ForwardingType#PURE}, the request is forwarded to the backing
     * set; otherwise, the default implementation from
     * {@link DoubleSet#removeIf(DoublePredicate)} is used.</p>
     * @param filter a predicate which returns {@code true} for elements to be
     *               removed
     * @return {@code true} if this set changed as a result of the call
     * @throws CollectionNotModifiableException if this view is unmodifiable
     * @throws UnsupportedOperationException if the {@code removeIf} operation
     * is not supported by this set
     * @throws NullPointerException if the specified filter is null
     */
    @Override
    public boolean removeIf(DoublePredicate filter) {
        checkModifiable();
        return forwarder().predicateOp(delegate -> delegate.removeIf(filter),
                () -> DoubleSet.super.removeIf(filter));
    }

    /**
     * <p>Removes from this set all of its elements that match the specified
     * filter (optional operation).</p>
     * @implNote <p>This method will throw an exception if the view implements
     * {@link UnmodifiableView}. If it does not, and the forwarding type is
     * {@link ForwardingType#PURE}, the request is forwarded to the backing
     * set; otherwise, the default implementation from
     * {@link DoubleSet#removeIf(Predicate)} is used.</p>
     * @param filter a predicate which returns {@code true} for elements to be
     *               removed
     * @return {@code true} if this set changed as a result of the call
     * @throws CollectionNotModifiableException if this view is unmodifiable
     * @throws UnsupportedOperationException if the {@code removeIf} operation
     * is not supported by this set
     * @throws NullPointerException if the specified filter is null
     */
    @Override
    public boolean removeIf(Predicate<? super Double> filter) {
        checkModifiable();
        return forwarder().predicateOp(delegate -> delegate.removeIf(filter),
                () -> DoubleSet.super.removeIf(filter));
    }

    /**
     * <p>Removes from this set all of its elements that match the specified
     * filter (optional operation).</p>
     * @implNote <p>This method will throw an exception if the view implements
     * {@link UnmodifiableView}. If it does not, and the forwarding type is
     * {@link ForwardingType#PURE}, the request is forwarded to the backing
     * set; otherwise, the default implementation from
     * {@link DoubleSet#removeIfDouble(DoublePredicate)} is used.</p>
     * @param filter a predicate which returns {@code true} for elements to be
     *               removed
     * @return {@code true} if this set changed as a result of the call
     * @throws CollectionNotModifiableException if this view is unmodifiable
     * @throws UnsupportedOperationException if the {@code removeIf} operation
     * is not supported by this set
     * @throws NullPointerException if the specified filter is null
     */
    @Override
    public boolean removeIfDouble(DoublePredicate filter) {
        checkModifiable();
        return forwarder().predicateOp(delegate ->
                        delegate.removeIfDouble(filter),
                () -> DoubleSet.super.removeIfDouble(filter));
    }

    /**
     * <p>Retains only the elements in this set that are contained in the
     * specified collection (optional operation).</p>
     * @implNote <p>This method will throw an exception if the view implements
     * {@link UnmodifiableView}. If it does not, and forwarding type is
     * {@link ForwardingType#PURE}, the request is forwarded to the backing
     * set; otherwise, the default implementation from
     * {@link DoubleSet#retainAll(Collection)} is used.</p>
     * @param collection collection containing elements to be retained in
     *                   this set
     * @return {@code true} if this set changed as a result of the call
     * @throws CollectionNotModifiableException if this view is unmodifiable
     * @throws UnsupportedOperationException if the {@code retainAll}
     * operation is not supported by this set
     * @throws ClassCastException if the {@code Double} type is incompatible
     * with the specified collection (optional)
     * @throws NullPointerException if the specified collection is null
     */
    @Override
    public boolean retainAll(Collection<?> collection) {
        checkModifiable();
        return forwarder().predicateOp(delegate ->
                        delegate.retainAll(collection),
                () -> DoubleSet.super.retainAll(collection));
    }

    /**
     * <p>Returns only the elements in this set that are contained in the
     * specified collection (optional operation).</p>
     * @implNote <p>This method will throw an exception if the view implements
     * {@link UnmodifiableView}. If it does not, and forwarding type is
     * {@link ForwardingType#PURE}, the request is forwarded to the backing
     * set; otherwise, the default implementation from
     * {@link DoubleSet#retainAll(DoubleCollection)} is used.</p>
     * @param collection collection containing elements to be retained in this
     *                  set
     * @return {@code true} if this set changed a a result of the call
     * @throws CollectionNotModifiableException if this view is unmodifiable
     * @throws UnsupportedOperationException if the {@code retainAll}
     * operation is not supported by this set
     * @throws NullPointerException if the specified collection is null
     */
    @Override
    public boolean retainAll(DoubleCollection collection) {
        checkModifiable();
        return forwarder().predicateOp(delegate ->
                        delegate.retainAll(collection),
                () -> DoubleSet.super.retainAll(collection));
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
     * {@link DoubleSet#spliterator()}.</p>
     * @return a spliterator over the elements in this set
     */
    @Override
    public Spliterator.OfDouble spliterator() {
        return forwarder().boxedOp(DoubleSet::spliterator,
                DoubleSet.super::spliterator);
    }

    /**
     * <p>Returns an array containing all of the elements in this set.</p>
     * @implNote <p>If the forwarding type is {@link ForwardingType#PURE},
     * the method forwards the request to the backing set; otherwise, it
     * invokes the default implementation from
     * {@link DoubleSet#toPrimitiveArray()}.</p>
     * @return an array containing all the elements in this set
     */
    @Override
    public double[] toPrimitiveArray() {
        return forwarder().boxedOp(DoubleSet::toPrimitiveArray,
                DoubleSet.super::toPrimitiveArray);
    }
}
