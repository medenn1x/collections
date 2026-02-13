package net.arinoru.util.views;

import net.arinoru.util.CollectionNotModifiableException;
import net.arinoru.util.IntCollection;
import net.arinoru.util.IntSet;
import net.arinoru.util.PrimitiveSet;

import java.util.Collection;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.function.IntConsumer;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.stream.IntStream;

/**
 * <p>An internal implementation of an int set view. This class defines a
 * series of default behaviors for set operations based on a {@link Forwarder},
 * an object that provides access to a backing set and means of providing
 * varying levels of access to functionality based on a configured
 * {@link ForwardingType}.</p>
 * <p>The requirements for the backing set vary based on the forwarding type,
 * with {@link ForwardingType#MINIMAL} allowing the use of any backing set,
 * while {@link ForwardingType#PURE} and {@link ForwardingType#SHALLOW} both
 * require the backing set to be an {@link IntSet}.</p>
 * <p>The general rule for the implementation of set operations in this class
 * depends on the forwarding type and whether or not the {@link IntSet}
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
 *     {@link IntSet} interface are forwarded to the backing set if the
 *     forwarding type is {@link ForwardingType#PURE}; otherwise, they are
 *     implemented in terms of the default method, which may itself invoke
 *     other set operations on the view.</li>
 *     <li>Operations that are sensitive to the element type of the set (e.g.
 *     those that add or remove elements or are otherwise bounded by subtype,
 *     or methods that are only specified by {@link IntSet} or
 *     {@link PrimitiveSet}) and do not have a default implementation in
 *     {@link IntSet} will throw an
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
public abstract class AbstractIntSetView extends AbstractPrimitiveSetView<Integer,int[],
        IntConsumer,IntPredicate,PrimitiveIterator.OfInt,Spliterator.OfInt,IntStream,
        IntCollection,IntSet> implements IntSet {
    /**
     * <p>Adds the specified element to this set if it is not already present
     * (optional operation).</p>
     * @implNote <p>This method will throw an exception if the view implements
     * {@link UnmodifiableView}. If it does not, and the forwarding type is
     * {@link ForwardingType#PURE}, this method forwards the request to the
     * backing set; otherwise, the default implementation from
     * {@link IntSet#add(Integer)} is used.</p>
     * @param element element to be added to this set
     * @return {@code true} if this set did not already contain the specified
     * element
     * @throws CollectionNotModifiableException if this view is unmodifiable
     * @throws UnsupportedOperationException if the {@code add} operation is
     * not supported by the set
     * @throws NullPointerException if the specified element is null
     */
    @Override
    public boolean add(Integer element) {
        checkModifiable();
        return forwarder().predicateOp(delegate -> delegate.add(element),
                () -> IntSet.super.add(element));
    }

    /**
     * <p>Adds all of the elements in the specified collection to this set if
     * they're not already present (optional operation).</p>
     * @implNote <p>This method will throw an exception if the view implements
     * {@link UnmodifiableView}. If it does not, and the forwarding type is
     * {@link ForwardingType#PURE}, this method forwards the request to the
     * backing set; otherwise, the default implementation from
     * {@link IntSet#addAll(Collection)} is used.</p>
     * @param collection collection containing elements to be added to this set
     * @return {@code true} if this set changed as a result of the call
     * @throws CollectionNotModifiableException if this view is unmodifiable
     * @throws UnsupportedOperationException if the {@code addAll} operation is
     * not supported by the set
     * @throws NullPointerException if the specified collection contains one or
     * more null elements, or if the specified collection is null
     */
    @Override
    public boolean addAll(Collection<? extends Integer> collection) {
        checkModifiable();
        return forwarder().predicateOp(delegate -> delegate.addAll(collection),
                () -> IntSet.super.addAll(collection));
    }

    /**
     * <p>Adds all of the elements in the specified collection to this set if
     * they're not already present (optional operation).</p>
     * @implNote <p>This method will throw an exception if the view implements
     * {@link UnmodifiableView}. If it does not, and the forwarding type is
     * {@link ForwardingType#PURE}, this method forwards the request to the
     * backing set; otherwise, the default implementation from
     * {@link IntSet#addAll(IntCollection)} is used.</p>
     * @param collection collection containing elements to be added to this set
     * @return {@code true} if this set changed as a result of the call
     * @throws CollectionNotModifiableException if this view is unmodifiable
     * @throws UnsupportedOperationException if the {@code addAll} operation is
     * not supported by the set
     * @throws NullPointerException if the specified collection contains one or
     * more null elements, or if the specified collection is null
     */
    @Override
    public boolean addAll(IntCollection collection) {
        checkModifiable();
        return forwarder().predicateOp(delegate -> delegate.addAll(collection),
                () -> IntSet.super.addAll(collection));
    }

    /**
     * <p>Adds the specified element to this set if it is not already present
     * (optional operation).</p>
     * @implNote <p>This method will throw an exception if the view implements
     * {@link UnmodifiableView}. If it does not, and the forwarding type is
     * {@link ForwardingType#PURE}, this method forwards the request to the
     * backing set; otherwise, the default implementation from
     * {@link IntSet#addInt(int)} is used.</p>
     * @param element element to be added to this set
     * @return {@code true} if this set did not already contain the specified
     * element
     * @throws CollectionNotModifiableException if this view is unmodifiable
     * @throws UnsupportedOperationException if the {@code addInt} operation is
     * not supported by this set
     */
    @Override
    public boolean addInt(int element) {
        checkModifiable();
        return forwarder().predicateOp(delegate -> delegate.addInt(element),
                () -> IntSet.super.addInt(element));
    }

    /**
     * <p>Removes all of the elements from this set (optional operation).</p>
     * @implNote <p>This method will throw an exception if the view implements
     * {@link UnmodifiableView}. If it does not, and the forwarding type is
     * {@link ForwardingType#PURE}, this method forwards the request to the
     * backing set; otherwise, the default implementation from
     * {@link IntSet#clear()} is used.</p>
     * @throws CollectionNotModifiableException if this view is unmodifiable
     * @throws UnsupportedOperationException if the {@code clear} operation
     * is not supported by this set
     */
    @Override
    public void clear() {
        checkModifiable();
        forwarder().voidOp(IntSet::clear, IntSet.super::clear);
    }

    /**
     * <p>Returns {@code true} if this set contains the specified element.</p>
     * @implNote <p>If forwarding type is {@link ForwardingType#PURE}, the
     * request is forwarded to the backing set; otherwise, the default
     * implementation from {@link IntSet#contains(Object)} is used.</p>
     * @param o element whose presence in this set is to be tested
     * @return {@code true} if this set contains the specified element
     * @throws ClassCastException if the type of the specified element is
     * incompatible with this set (optional)
     * @throws NullPointerException if the specified element is null (optional)
     */
    @Override
    public boolean contains(Object o) {
        return forwarder().predicateOp(delegate -> delegate.contains(o),
                () -> IntSet.super.contains(o));
    }

    /**
     * <p>Returns {@code true} if this set contains all of the elements of the
     * specified collection.</p>
     * @implNote <p>If forwarding type is {@link ForwardingType#PURE}, the
     * request is forwarded to the backing set; otherwise, the default
     * implementation from {@link IntSet#containsAll(Collection)} is used.</p>
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
        return forwarder().predicateOp(delegate -> delegate.containsAll(collection),
                () -> IntSet.super.containsAll(collection));
    }

    /**
     * <p>Returns {@code true} if this set contains all of the elements of the
     * specified collection.</p>
     * @implNote <p>If forwarding type is {@link ForwardingType#PURE}, the
     * request is forwarded to the backing set; otherwise, the default
     * implementation from {@link IntSet#containsAll(IntCollection)} is
     * used.</p>
     * @param collection collection to be checked for containment in this set
     * @return {@code true} if this set contains all of the elements of the
     * specified collection
     * @throws NullPointerException if the specified collection is null
     */
    @Override
    public boolean containsAll(IntCollection collection) {
        return forwarder().predicateOp(delegate -> delegate.containsAll(collection),
                () -> IntSet.super.containsAll(collection));
    }

    /**
     * <p>Returns {@code true} if this set contains the specified element.</p>
     * @implNote <p>If forwarding type is {@link ForwardingType#PURE}, the
     * request is forwarded to the backing set; otherwise, the default
     * implementation from {@link IntSet#containsInt(int)} is used.</p>
     * @param element element whose presence in this set is to be tested
     * @return {@code true} if this set contains the specified element
     */
    @Override
    public boolean containsInt(int element) {
        return forwarder().predicateOp(delegate -> delegate.containsInt(element),
                () -> IntSet.super.containsInt(element));
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
    public PrimitiveIterator.OfInt iterator() {
        return forwarder().maskIfNeeded(forwarder().boxedOp(IntSet::iterator),
                IntIteratorView::unmodifiable);
    }

    /**
     * <p>Returns a possibly parallel {@code IntStream} with this set as its
     * source. It is allowable for this method to return a sequential int
     * stream.</p>
     * @implNote <p>If forwarding type is {@link ForwardingType#PURE}, the
     * request is forwarded to the backing set; otherwise, the default
     * implementation from {@link IntSet#parallelPrimitiveStream()} is
     * used.</p>
     * @return a possibly parallel int stream over the elements in this set
     */
    @Override
    public IntStream parallelPrimitiveStream() {
        return forwarder().boxedOp(IntSet::parallelPrimitiveStream,
                IntSet.super::parallelPrimitiveStream);
    }

    /**
     * <p>Returns a sequential {@code IntStream} with this set as its
     * source.</p>
     * @implNote <p>If forwarding type is {@link ForwardingType#PURE}, the
     * request is forwarded to the backing set; otherwise, the default
     * implementation from {@link IntSet#primitiveStream()} is used.</p>
     * @return a sequential int stream over the elements in this set
     */
    @Override
    public IntStream primitiveStream() {
        return forwarder().boxedOp(IntSet::primitiveStream,
                IntSet.super::primitiveStream);
    }

    /**
     * <p>Removes the specified element from this set if it is present
     * (optional operation).</p>
     * @implNote <p>This method will throw an exception if the view implements
     * {@link UnmodifiableView}. If it does not, and forwarding type is
     * {@link ForwardingType#PURE}, the request is forwarded to the backing
     * set; otherwise, the default implementation from
     * {@link IntSet#remove(Object)} is used.</p>
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
                () -> IntSet.super.remove(o));
    }

    /**
     * <p>Removes from this set all of its elements that are contained in the
     * specified collection (optional operation).</p>
     * @implNote <p>This method will throw an exception if the view implements
     * {@link UnmodifiableView}. If it does not, and forwarding type is
     * {@link ForwardingType#PURE}, the request is forwarded to the backing
     * set; otherwise, the default implementation from
     * {@link IntSet#removeAll(Collection)} is used.</p>
     * @param collection collection containing elements to be removed from
     *                   this set
     * @return {@code true} if this set changed as a result of the call
     * @throws CollectionNotModifiableException if this view is unmodifiable
     * @throws UnsupportedOperationException if the {@code removeAll} operation
     * is not supported by this set
     * @throws ClassCastException if the {@code Integer} type is incompatible
     * with the specified collection (optional)
     * @throws NullPointerException if the specified collection is null
     */
    @Override
    public boolean removeAll(Collection<?> collection) {
        checkModifiable();
        return forwarder().predicateOp(delegate -> delegate.removeAll(collection),
                () -> IntSet.super.removeAll(collection));
    }

    /**
     * <p>Removes from this set all of its elements that are contained in the
     * specified collection (optional operation).</p>
     * @implNote <p>This method will throw an exception if the view implements
     * {@link UnmodifiableView}. If it does not, and forwarding type is
     * {@link ForwardingType#PURE}, the request is forwarded to the backing
     * set; otherwise, the default implementation from
     * {@link IntSet#removeAll(IntCollection)} is used.</p>
     * @param collection collection containing elements to be removed from
     *                  this set
     * @return {@code true} if this set changed as a result of the call
     * @throws CollectionNotModifiableException if this view is unmodifiable
     * @throws UnsupportedOperationException if the {@code removeAll} operation
     * is not supported by this set
     * @throws NullPointerException if the specified collection is null
     */
    @Override
    public boolean removeAll(IntCollection collection) {
        checkModifiable();
        return forwarder().predicateOp(delegate -> delegate.removeAll(collection),
                () -> IntSet.super.removeAll(collection));
    }

    /**
     * <p>Removes from this set all of its elements that match the specified
     * filter (optional operation).</p>
     * @implNote <p>This method will throw an exception if the view implements
     * {@link UnmodifiableView}. If it does not, and the forwarding type is
     * {@link ForwardingType#PURE}, the request is forwarded to the backing
     * set; otherwise, the default implementation from
     * {@link IntSet#removeIf(IntPredicate)} is used.</p>
     * @param filter a predicate which returns {@code true} for elements to be
     *               removed
     * @return {@code true} if this set changed as a result of the call
     * @throws CollectionNotModifiableException if this view is unmodifiable
     * @throws UnsupportedOperationException if the {@code removeIf} operation
     * is not supported by this set
     * @throws NullPointerException if the specified filter is null
     */
    @Override
    public boolean removeIf(IntPredicate filter) {
        checkModifiable();
        return forwarder().predicateOp(delegate -> delegate.removeIf(filter),
                () -> IntSet.super.removeIf(filter));
    }

    /**
     * <p>Removes from this set all of its elements that match the specified
     * filter (optional operation).</p>
     * @implNote <p>This method will throw an exception if the view implements
     * {@link UnmodifiableView}. If it does not, and the forwarding type is
     * {@link ForwardingType#PURE}, the request is forwarded to the backing
     * set; otherwise, the default implementation from
     * {@link IntSet#removeIf(Predicate)} is used.</p>
     * @param filter a predicate which returns {@code true} for elements to be
     *               removed
     * @return {@code true} if this set changed as a result of the call
     * @throws CollectionNotModifiableException if this view is unmodifiable
     * @throws UnsupportedOperationException if the {@code removeIf} operation
     * is not supported by this set
     * @throws NullPointerException if the specified filter is null
     */
    @Override
    public boolean removeIf(Predicate<? super Integer> filter) {
        checkModifiable();
        return forwarder().predicateOp(delegate -> delegate.removeIf(filter),
                () -> IntSet.super.removeIf(filter));
    }

    /**
     * <p>Removes from this set all of its elements that match the specified
     * filter (optional operation).</p>
     * @implNote <p>This method will throw an exception if the view implements
     * {@link UnmodifiableView}. If it does not, and the forwarding type is
     * {@link ForwardingType#PURE}, the request is forwarded to the backing
     * set; otherwise, the default implementation from
     * {@link IntSet#removeIfInt(IntPredicate)} is used.</p>
     * @param filter a predicate which returns {@code true} for elements to be
     *               removed
     * @return {@code true} if this set changed as a result of the call
     * @throws CollectionNotModifiableException if this view is unmodifiable
     * @throws UnsupportedOperationException if the {@code removeIf} operation
     * is not supported by this set
     * @throws NullPointerException if the specified filter is null
     */
    @Override
    public boolean removeIfInt(IntPredicate filter) {
        checkModifiable();
        return forwarder().predicateOp(delegate -> delegate.removeIfInt(filter),
                () -> IntSet.super.removeIfInt(filter));
    }

    /**
     * <p>Removes the specified element from this set if it is present
     * (optional operation).</p>
     * @implNote <p>This method will throw an exception if the view implements
     * {@link UnmodifiableView}. If it does not, and forwarding type is
     * {@link ForwardingType#PURE}, the request is forwarded to the backing
     * set; otherwise, the default implementation from
     * {@link IntSet#removeInt(int)} is used.</p>
     * @param element element to be removed from this set, if present
     * @return {@code true} if this set contained the specified element
     * @throws CollectionNotModifiableException if this view is unmodifiable
     * @throws UnsupportedOperationException if the {@code remove} operation
     * is not supported by this set
     */
    @Override
    public boolean removeInt(int element) {
        checkModifiable();
        return forwarder().predicateOp(delegate -> delegate.removeInt(element),
                () -> IntSet.super.removeInt(element));
    }

    /**
     * <p>Retains only the elements in this set that are contained in the
     * specified collection (optional operation).</p>
     * @implNote <p>This method will throw an exception if the view implements
     * {@link UnmodifiableView}. If it does not, and forwarding type is
     * {@link ForwardingType#PURE}, the request is forwarded to the backing
     * set; otherwise, the default implementation from
     * {@link IntSet#retainAll(Collection)} is used.</p>
     * @param collection collection containing elements to be retained in
     *                   this set
     * @return {@code true} if this set changed as a result of the call
     * @throws CollectionNotModifiableException if this view is unmodifiable
     * @throws UnsupportedOperationException if the {@code retainAll}
     * operation is not supported by this set
     * @throws ClassCastException if the {@code Integer} type is incompatible
     * with the specified collection (optional)
     * @throws NullPointerException if the specified collection is null
     */
    @Override
    public boolean retainAll(Collection<?> collection) {
        checkModifiable();
        return forwarder().predicateOp(delegate -> delegate.retainAll(collection),
                () -> IntSet.super.retainAll(collection));
    }

    /**
     * <p>Returns only the elements in this set that are contained in the
     * specified collection (optional operation).</p>
     * @implNote <p>This method will throw an exception if the view implements
     * {@link UnmodifiableView}. If it does not, and forwarding type is
     * {@link ForwardingType#PURE}, the request is forwarded to the backing
     * set; otherwise, the default implementation from
     * {@link IntSet#retainAll(IntCollection)} is used.</p>
     * @param collection collection containing elements to be retained in this
     *                  set
     * @return {@code true} if this set changed a a result of the call
     * @throws CollectionNotModifiableException if this view is unmodifiable
     * @throws UnsupportedOperationException if the {@code retainAll}
     * operation is not supported by this set
     * @throws NullPointerException if the specified collection is null
     */
    @Override
    public boolean retainAll(IntCollection collection) {
        checkModifiable();
        return forwarder().predicateOp(delegate -> delegate.retainAll(collection),
                () -> IntSet.super.retainAll(collection));
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
     * {@link IntSet#spliterator()}.</p>
     * @return a spliterator over the elements in this set
     */
    @Override
    public Spliterator.OfInt spliterator() {
        return forwarder().boxedOp(IntSet::spliterator, IntSet.super::spliterator);
    }

    /**
     * <p>Returns an array containing all of the elements in this set.</p>
     * @implNote <p>If the forwarding type is {@link ForwardingType#PURE},
     * the method forwards the request to the backing set; otherwise, it
     * invokes the default implementation from
     * {@link IntSet#toPrimitiveArray()}.</p>
     * @return an array containing all the elements in this set
     */
    @Override
    public int[] toPrimitiveArray() {
        return forwarder().boxedOp(IntSet::toPrimitiveArray,
                IntSet.super::toPrimitiveArray);
    }
}
