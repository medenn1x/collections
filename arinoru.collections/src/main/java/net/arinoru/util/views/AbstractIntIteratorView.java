package net.arinoru.util.views;

import net.arinoru.util.CollectionNotModifiableException;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

/**
 * <p>An internal implementation of an int iterator view. This class defines
 * a series of default behaviors for iterator operations based on a
 * {@link Forwarder}, an object that provides access to a backing iterator and
 * means of providing varying levels of access to functionality based on a
 * configured {@link ForwardingType}.</p>
 * <p>The requirements for the backing iterator vary based on the forwarding
 * type, with {@link ForwardingType#MINIMAL} allowing the use of any backing
 * iterator, while {@link ForwardingType#PURE} and
 * {@link ForwardingType#SHALLOW} both require the backing iterator to be a
 * {@link OfInt}.</p>
 * <p>The general rule for the implementation of iterator operations in this
 * class depends on the forwarding type and whether or not the
 * {@link OfInt} interface provides a default
 * implementation for that operation. In general:</p>
 * <ol>
 *     <li>Operations that (potentially) modify the contents of a collection
 *     backed by the iterator will throw a
 *     {@link CollectionNotModifiableException} if this view implements
 *     {@link UnmodifiableView}.</li>
 *     <li>Operations that are not sensitive to the element type of the
 *     iterator and which do not have default implementations are forwarded
 *     directly to the backing iterator, regardless of forwarding type.</li>
 *     <li>Operations which have a default method defined in the
 *     {@link OfInt} interface are forwarded
 *     to the backing iterator if the forwarding type is
 *     {@link ForwardingType#PURE}; otherwise, they are implemented in terms
 *     of the default method, which may itself invoke other iterator
 *     operations on the view.</li>
 *     <li>Operations that are sensitive to the element type of the iterator
 *     (e.g. those that add or remove elements or are otherwise bounded by
 *     subtype, or methods that are only specified by
 *     {@link PrimitiveIterator} or {@link OfInt}) and do
 *     not have a default implementation in one of those interfaces will throw
 *     an {@link InvalidForwardingContextException} if the forwarding type is
 *     {@link ForwardingType#MINIMAL}.</li>
 *     <li>Operations not meeting the above criteria will generally forward
 *     to the backing iterator. If some aspect of the type difference between
 *     the view and the backing iterator prevents the proper operation of the
 *     forward, an exception may be thrown (e.g.
 *     {@link InvalidForwardingContextException}, {@link ClassCastException},
 *     or {@link IllegalStateException}).</li>
 * </ol>
 */
public abstract class AbstractIntIteratorView implements View, PrimitiveIterator.OfInt {
    /**
     * <p>Returns the {@link Forwarder} managing access to the backing
     * iterator.</p>
     * @implSpec <p>Implementing classes must override this to return a
     * forwarder that represents the backing iterator and forwarding type. The
     * forwarder is not stored by this class directly.</p>
     * @return forwarder that manages access to the backing iterator
     */
    protected abstract Forwarder<Iterator<?>,PrimitiveIterator.OfInt> forwarder();

    /**
     * <p>Performs the given action for each remaining element.</p>
     * @implNote <p>If forwarding type is {@link ForwardingType#PURE}, the
     * request is forwarded to the backing iterator; otherwise, the default
     * implementation from
     * {@link OfInt#forEachRemaining(Consumer)} is used.
     * </p>
     * @param action The action to be performed for each element
     * @throws NullPointerException if the specified action is null
     */
    @Override
    public void forEachRemaining(Consumer<? super Integer> action) {
        forwarder().voidOp(delegate -> delegate.forEachRemaining(action),
                () -> OfInt.super.forEachRemaining(action));
    }

    /**
     * <p>Performs the given action for each remaining element.</p>
     * @implNote <p>If forwarding type is {@link ForwardingType#PURE}, the
     * request is forwarded to the backing iterator; otherwise, the default
     * implementation from
     * {@link OfInt#forEachRemaining(Object) OfInt.forEachRemaining(T_CONS)}
     * is used.</p>
     * @param action The action to be performed for each element
     * @throws NullPointerException if the specified action is null
     */
    @Override
    public void forEachRemaining(IntConsumer action) {
        forwarder().voidOp(delegate -> delegate.forEachRemaining(action),
                () -> OfInt.super.forEachRemaining(action));
    }

    /**
     * <p>Returns {@code true} if the iteration has more elements.</p>
     * @implNote <p>This method forwards the request to the backing iterator.
     * </p>
     * @return {@code true} if the iteration has more elements
     */
    @Override
    public boolean hasNext() {
        return forwarder().asDelegateType().hasNext();
    }

    /**
     * <p>Returns the next element in the iteration.</p>
     * @implNote <p>If forwarding type is {@link ForwardingType#PURE}, the
     * request is forwarded to the backing iterator; otherwise, the default
     * implementation from {@link OfInt#next()} is used.</p>
     * @return the next element in the iteration
     * @throws NoSuchElementException if the iteration has no more elements
     */
    @Override
    public Integer next() {
        return forwarder().boxedOp(OfInt::next, OfInt.super::next);
    }

    /**
     * <p>Returns the next {@code int} element in the iteration.</p>
     * @implSpec <p>This method must be overridden if forwarding type is
     * {@link ForwardingType#MINIMAL}.</p>
     * @implNote <p>This method forwards the request to the backing iterator
     * unless automatic forwarding of type-sensitive operations is forbidden
     * by the forwarding type.</p>
     * @return the next {@code int} element in the iteration
     * @throws InvalidForwardingContextException if forwarding type is
     * {@link ForwardingType#MINIMAL}
     * @throws NoSuchElementException if the iteration has no more elements
     */
    @Override
    public int nextInt() {
        return forwarder().intOp(OfInt::nextInt);
    }

    /**
     * <p>Removes from the underlying collection the last element returned by
     * this iterator (optional operation).</p>
     * @apiNote <p>This method is an exception to the general policy on the
     * use of default implementations for {@code SHALLOW} and {@code MINIMAL}
     * views.</p>
     * @implNote <p>This method will throw an exception if the view implements
     * {@link UnmodifiableView}. Otherwise, it forwards the request to the
     * backing iterator.</p>
     * @throws CollectionNotModifiableException if this view is unmodifiable
     * @throws UnsupportedOperationException if the {@code remove} operation
     * is not supported by this iterator
     */
    @Override
    public void remove() {
        checkModifiable();
        forwarder().asDelegateType().remove();
    }
}
