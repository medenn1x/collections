package net.arinoru.util.views;

import net.arinoru.util.CollectionNotModifiableException;
import net.arinoru.util.PrimitiveCollection;

import java.util.Collection;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.BaseStream;
import java.util.stream.Stream;

/**
 * <p>An internal implementation of a primitive collection view. This class
 * defines a series of default behaviors for collection operations based on a
 * {@link Forwarder}, an object that provides access to a backing collection
 * and means of providing varying levels of access to functionality based on a
 * configured {@link ForwardingType}.</p>
 * <p>The requirements for the backing collection vary based on the forwarding
 * type, with {@link ForwardingType#MINIMAL} allowing the use of any backing
 * collection, while {@link ForwardingType#PURE} and
 * {@link ForwardingType#SHALLOW} both require a primitive collection whose
 * backing element type matches the view's element type.</p>
 * <p>Methods which have default implementations in specializations of
 * {@link PrimitiveCollection} are declared {@code abstract} to ensure that
 * implementing collections provide proper behavior for
 * {@link ForwardingType#SHALLOW} and {@link ForwardingType#MINIMAL}.</p>
 * <p>The general rule for the implementation of collection operations in this
 * class depends on the forwarding type and whether or not the
 * {@link PrimitiveCollection} interface provides a default implementation for
 * that operation. In general:</p>
 * <ol>
 *     <li>Operations that (potentially) modify the contents of the collection
 *     will throw a {@link CollectionNotModifiableException} if this view
 *     implements {@link UnmodifiableView}.</li>
 *     <li>Operations that are not sensitive to the element type of the
 *     collection and which do not have default implementations are forwarded
 *     directly to the backing collection, regardless of forwarding type.</li>
 *     <li>Operations which have a default method defined in the
 *     {@link PrimitiveCollection} interface are forwarded to the backing
 *     collection if the forwarding type is {@link ForwardingType#PURE};
 *     otherwise, they are implemented in terms of the default method, which
 *     may itself invoke other collection operations on the view.</li>
 *     <li>Operations that are sensitive to the element type of the collection
 *     (e.g. those that add or remove elements or are otherwise bounded by
 *     subtype, or those provided only by {@link PrimitiveCollection}) and do
 *     not have a default implementation in {@link PrimitiveCollection} will
 *     throw an {@link InvalidForwardingContextException} if the forwarding
 *     type is {@link ForwardingType#MINIMAL}.</li>
 *     <li>Operations not meeting the above criteria will generally forward
 *     to the backing collection. If some aspect of the type difference
 *     between the view and the backing collection prevents the proper
 *     operation of the forward, an exception may be thrown (e.g.
 *     {@link InvalidForwardingContextException}, {@link ClassCastException},
 *     or {@link IllegalStateException}).</li>
 * </ol>
 * @param <T> boxed element type of the view
 * @param <T_ARR> the type of arrays who have the collection's primitive
 *                element type as their runtime component type
 * @param <T_CONS> the type of primitive consumer
 * @param <T_PRED> the type of primitive predicate
 * @param <T_ITER> the type of primitive iterator
 * @param <T_SPLITR> the type of primitive spliterator
 * @param <T_STR> the type of primitive stream
 * @param <T_COLL> the type of primitive collection
 */
public abstract class AbstractPrimitiveCollectionView<T,T_ARR,T_CONS,T_PRED,
        T_ITER extends PrimitiveIterator<T,T_CONS>,
        T_SPLITR extends Spliterator.OfPrimitive<T,T_CONS,T_SPLITR>,
        T_STR extends BaseStream<T,T_STR>,
        T_COLL extends PrimitiveCollection<T,T_ARR,T_CONS,T_PRED,T_ITER,T_SPLITR,T_STR,T_COLL>>
        implements View, PrimitiveCollection<T,T_ARR,T_CONS,T_PRED,T_ITER,T_SPLITR,T_STR,T_COLL> {
    /**
     * <p>Returns the {@link Forwarder} managing access to the backing
     * collection.</p>
     * @implSpec <p>Implementing classes must override this to return a
     * forwarder that represents the backing collection and forwarding type.
     * The forwarder is not stored by this class directly.</p>
     * @return forwarder that manages access to the backing collection
     */
    protected abstract Forwarder<Collection<?>,T_COLL> forwarder();

    /**
     * <p>Ensures that this collection contains the specified element (optional
     * operation).</p>
     * @implSpec <p>This method must be implemented by a specialization-aware
     * subclass to account for specialization-dependent default behavior.</p>
     * @param t element whose presence in this collection is to be ensured
     * @return {@code true} if this collection changed as a result of the call
     * @throws CollectionNotModifiableException if this view is unmodifiable
     * @throws InvalidForwardingContextException if some aspect of the backing
     * collection is invalid for this forwarding type (optional), or the
     * forwarding type is {@code MINIMAL}
     * @throws UnsupportedOperationException if the {@code add} operation is
     * not supported by this collection
     * @throws ClassCastException if the type of the backing collection is not
     * compatible with this view for the specified forwarding type (optional),
     * or if the class of the specified element prevents it from being added
     * to this collection
     * @throws NullPointerException if the specified element is null
     * @throws IllegalArgumentException if some property of the specified
     * element prevents it from being added to this collection
     * @throws IllegalStateException if some aspect of the backing collection
     * prevents this operation from being resolved (optional), or the element
     * cannot be added at this time due to insertion restrictions
     */
    @Override
    public abstract boolean add(T t);

    /**
     * <p>Adds all of the elements in the specified collection to this
     * collection (optional operation).</p>
     * @implSpec <p>This method must be implemented by a specialization-aware
     * subclass to account for specialization-dependent default behavior.</p>
     * @param collection collection containing elements to be added to this
     *                  collection
     * @return {@code true} if this collection changed as a result of the call
     * @throws CollectionNotModifiableException if this view is unmodifiable
     * @throws InvalidForwardingContextException if some aspect of the backing
     * collection is invalid for this forwarding type (optional), or the
     * forwarding type is {@code MINIMAL}
     * @throws UnsupportedOperationException if the {@code addAll} operation is
     * not supported by this collection
     * @throws ClassCastException if the type of the backing collection is not
     * compatible with this view for the specified forwarding type (optional),
     * or if the class of an element of the specified collection prevents it
     * from being added to this collection
     * @throws NullPointerException if the specified collection contains one
     * or more null elements, or the specified collection is null
     * @throws IllegalArgumentException if some property of an element of the
     * specified collection prevents it from being added to this collection
     * @throws IllegalStateException if some aspect of the backing collection
     * prevents this operation from being resolved (optional), or if not all
     * the elements can be added at this time due to insertion restrictions
     */
    @Override
    public abstract boolean addAll(Collection<? extends T> collection);

    /**
     * <p>Adds all of the elements in the specified collection to this
     * collection (optional operation).</p>
     * @implSpec <p>This method must be implemented by a specialization-aware
     * subclass to account for specialization-dependent default behavior.</p>
     * @param collection collection containing elements to be added to this
     *                  collection
     * @return {@code true} if this collection changed as a result of the call
     * @throws CollectionNotModifiableException if this view is unmodifiable
     * @throws InvalidForwardingContextException if some aspect of the backing
     * collection is invalid for this forwarding type (optional), or the
     * forwarding type is {@code MINIMAL}
     * @throws UnsupportedOperationException if the {@code addAll} operation is
     * not supported by this collection
     * @throws ClassCastException if the type of the backing collection is not
     * compatible with this view for the specified forwarding type (optional),
     * or if the class of an element of the specified collection prevents it
     * from being added to this collection
     * @throws NullPointerException if the specified collection is null
     * @throws IllegalArgumentException if some property of an element of the
     * specified collection prevents it from being added to this collection
     * @throws IllegalStateException if some aspect of the backing collection
     * prevents this operation from being resolved (optional), or if not all
     * the elements can be added at this time due to insertion restrictions
     */
    @Override
    public abstract boolean addAll(T_COLL collection);

    /**
     * <p>Removes all of the elements from this collection (optional
     * operation).</p>
     * @apiNote <p>This is under review, since a default implementation of
     * {@code clear} is possible at this level at the cost of unnecessary
     * boxing; since default implementations are not required to be efficient,
     * this may be deemed acceptable. However, leaving default implementations
     * to specializations is not especially burdensome in this case.</p>
     * @implSpec <p>This method must be implemented by a specialization-aware
     * subclass to account for specialization-dependent default behavior.</p>
     * @throws CollectionNotModifiableException if this view is unmodifiable
     * @throws InvalidForwardingContextException if some aspect of the backing
     * collection is invalid for this forwarding type (optional)
     * @throws UnsupportedOperationException if the {@code clear} operation is
     * not supported by this collection
     * @throws ClassCastException if the type of the backing collection is not
     * compatible with this view for the specified forwarding type (optional)
     * @throws IllegalStateException if some aspect of the backing collection
     * prevents this operation from being resolved (optional)
     */
    @Override
    public abstract void clear();

    /**
     * <p>Returns {@code true} if this collection contains the specified
     * element.</p>
     * @apiNote <p>This is under review, since a default implementation of
     * {@code contains} is possible at this level at the cost of unnecessary
     * boxing; since default implementations are not required to be efficient,
     * this may be deemed acceptable. However, leaving default implementations
     * to specializations is not especially burdensome in this case.</p>
     * @implSpec <p>This method must be implemented by a specialization-aware
     * subclass to account for specialization-dependent default behavior.</p>
     * @param o element whose presence in this collection is to be tested
     * @return {@code true} if this collection contains the specified element
     * @throws InvalidForwardingContextException if some aspect of the backing
     * collection is invalid for this forwarding type (optional)
     * @throws ClassCastException if the type of the backing collection is not
     * compatible with this view for the specified forwarding type, or if the
     * type of the specified element is incompatible with this collection
     * (optional)
     * @throws NullPointerException if the specified element is null
     * (optional)
     * @throws IllegalStateException if some aspect of the backing collection
     * prevents this operation from being resolved (optional)
     */
    @Override
    public abstract boolean contains(Object o);

    /**
     * <p>Returns {@code true} if this collection contains all of the elements
     * in the specified collection.</p>
     * @apiNote <p>This is under review, since a default implementation of
     * {@code containsAll} is possible at this level at the possible cost of
     * unnecessary boxing; since default implementations are not required to
     * be efficient, this may be deemed acceptable. However, leaving default
     * implementations to specializations is not especially burdensome in this
     * case.</p>
     * @implSpec <p>This method must be implemented by a specialization-aware
     * subclass to account for specialization-dependent default behavior.</p>
     * @param collection collection to be checked for containment in this
     *                   collection
     * @return {@code true} if this collection contains all elements in the
     * specified collection
     * @throws InvalidForwardingContextException if some aspect of the backing
     * collection is invalid for this forwarding type (optional)
     * @throws ClassCastException if the type of the backing collection is not
     * compatible with this view for the specified forwarding type, or if the
     * types of one or more elements in the specified collection are
     * incompatible with this collection (optional)
     * @throws NullPointerException if the specified collection contains one or
     * more null elements (optional) or if the specified collection is null.
     * @throws IllegalStateException if some aspect of the backing collection
     * prevents this operation from being resolved (optional)
     */
    @Override
    public abstract boolean containsAll(Collection<?> collection);

    /**
     * <p>Returns {@code true} if this collection contains all of the elements
     * in the specified collection.</p>
     * @apiNote <p>This is under review, since a default implementation of
     * {@code containsAll} is possible at this level at the possible cost of
     * unnecessary boxing; since default implementations are not required to
     * be efficient, this may be deemed acceptable. However, leaving default
     * implementations to specializations is not especially burdensome in this
     * case.</p>
     * @implSpec <p>This method must be implemented by a specialization-aware
     * subclass to account for specialization-dependent default behavior.</p>
     * @param collection collection to be checked for containment in this
     *                   collection
     * @return {@code true} if this collection contains all elements in the
     * specified collection
     * @throws InvalidForwardingContextException if some aspect of the backing
     * collection is invalid for this forwarding type (optional)
     * @throws ClassCastException if the type of the backing collection is not
     * compatible with this view for the specified forwarding type, or if the
     * types of one or more elements in the specified collection are
     * incompatible with this collection (optional)
     * @throws NullPointerException if the specified collection contains one or
     * more null elements (optional) or if the specified collection is null.
     * @throws IllegalStateException if some aspect of the backing collection
     * prevents this operation from being resolved (optional)
     */
    @Override
    public abstract boolean containsAll(T_COLL collection);

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
     * implementation from
     * {@link PrimitiveCollection#forEach(Consumer)}.</p>
     * @param action action to perform on each element of the collection
     * @throws NullPointerException if the specified action is null
     */
    @Override
    public void forEach(Consumer<? super T> action) {
        forwarder().voidOp(delegate -> delegate.forEach(action),
                () -> PrimitiveCollection.super.forEach(action));
    }

    /**
     * <p>Performs the given action for each element of the collection
     * until all elements have been processed or the action throws an
     * exception.</p>
     * @implNote <p>The default implementation forwards the request to
     * the backing collection if the forwarding type is
     * {@link ForwardingType#PURE}; otherwise, it invokes the default
     * implementation from
     * {@link PrimitiveCollection#forEach(Object) PrimitiveCollection.forEach(T_CONS)}.
     * </p>
     * @param action action to perform on each element of the collection
     * @throws NullPointerException if the specified action is null
     */
    @Override
    public void forEach(T_CONS action) {
        forwarder().voidOp(delegate -> delegate.forEach(action),
                () -> PrimitiveCollection.super.forEach(action));
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
        return forwarder().intOp(Object::hashCode, super::hashCode);
    }

    /**
     * <p>Returns {@code true} if this collection contains no elements.</p>
     * @implNote <p>If the forwarding type is {@link ForwardingType#PURE}, the
     * request is forwarded to the backing collection; otherwise, the default
     * implementation from {@link PrimitiveCollection#isEmpty()} is used.</p>
     * @return {@code true} if this collection contains no elements
     */
    @Override
    public boolean isEmpty() {
        return forwarder().predicateOp(PrimitiveCollection::isEmpty,
                PrimitiveCollection.super::isEmpty);
    }

    /**
     * <p>Returns a primitive iterator over the elements in this collection.
     * </p>
     * @implSpec <p>This method must be implemented by a specialization-aware
     * subclass to account for specialization-dependent default behavior.</p>
     * @return a {@code PrimitiveIterator} over the elements in this
     * collection
     * @throws InvalidForwardingContextException if some aspect of the backing
     * collection is invalid for this forwarding type (optional)
     * @throws ClassCastException if the type of the backing collection is not
     * compatible with this view for the specified forwarding type (optional)
     * @throws IllegalStateException if some aspect of the backing collection
     * prevents this operation from being resolved (optional)
     */
    @Override
    public abstract T_ITER iterator();

    /**
     * <p>Returns a possibly parallel primitive stream with this collection as
     * its source. It is allowable for this method to return a sequential
     * primitive stream.</p>
     * @implSpec <p>This method must be implemented by a specialization-aware
     * subclass to account for specialization-dependent default behavior.</p>
     * @return a possibly parallel primitive stream over the elements in this
     * collection
     * @throws InvalidForwardingContextException if some aspect of the backing
     * collection is invalid for this forwarding type (optional)
     * @throws ClassCastException if the type of the backing collection is not
     * compatible with this view for the specified forwarding type (optional)
     * @throws IllegalStateException if some aspect of the backing collection
     * prevents this operation from being resolved (optional)
     */
    @Override
    public abstract T_STR parallelPrimitiveStream();

    /**
     * <p>Returns a possibly parallel {@code Stream} with this collection as
     * its source. It is allowable for this method to return a sequential
     * stream.</p>
     * @implNote <p>If the forwarding type is {@link ForwardingType#PURE},
     * this method will forward the request to the backing collection;
     * otherwise, the default implementation from
     * {@link PrimitiveCollection#parallelStream()} will be used.</p>
     * @return a possibly parallel stream over the elements in this collection
     */
    @Override
    public Stream<T> parallelStream() {
        return forwarder().boxedOp(PrimitiveCollection::parallelStream,
                PrimitiveCollection.super::parallelStream);
    }

    /**
     * <p>Returns a sequential primitive stream with this collection as its
     * source.</p>
     * @implSpec <p>This method must be implemented by a specialization-aware
     * subclass to account for specialization-dependent default behavior.</p>
     * @return a possibly parallel primitive stream over the elements in this
     * collection
     * @throws InvalidForwardingContextException if some aspect of the backing
     * collection is invalid for this forwarding type (optional)
     * @throws ClassCastException if the type of the backing collection is not
     * compatible with this view for the specified forwarding type (optional)
     * @throws IllegalStateException if some aspect of the backing collection
     * prevents this operation from being resolved (optional)
     */
    @Override
    public abstract T_STR primitiveStream();

    /**
     * <p>Removes a single instance of the specified element from this
     * collection, if it is present (optional operation).</p>
     * @implSpec <p>This method must be implemented by a specialization-aware
     * subclass to account for specialization-dependent default behavior.</p>
     * @param o element to be removed from this collection, if present
     * @return {@code true} if an element was removed as a result of this
     * call
     * @throws CollectionNotModifiableException if this view is unmodifiable
     * @throws InvalidForwardingContextException if some aspect of the backing
     * collection is invalid for this forwarding type (optional), or the
     * forwarding type is {@link ForwardingType#MINIMAL}
     * @throws UnsupportedOperationException if the {@code remove} operation
     * is not supported by this collection
     * @throws ClassCastException if the type of the backing collection is not
     * compatible with this view for the specified forwarding type, or if the
     * type of the specified element is incompatible with this collection
     * (optional)
     * @throws NullPointerException if the specified element is null (optional)
     * @throws IllegalStateException if some aspect of the backing collection
     * prevents this operation from being resolved (optional)
     */
    @Override
    public abstract boolean remove(Object o);

    /**
     * <p>Removes all of this collection's elements that are also contained in the
     * specified collection (optional operation).</p>
     * @implSpec <p>This method must be implemented by a specialization-aware
     * subclass to account for specialization-dependent default behavior.</p>
     * @param collection collection containing elements to be removed from this
     *                  collection
     * @return {@code true} if this collection changed as a result of the call
     * @throws CollectionNotModifiableException if this view is unmodifiable
     * @throws InvalidForwardingContextException if some aspect of the backing
     * collection is invalid for this forwarding type (optional) or the
     * forwarding type is {@link ForwardingType#MINIMAL}
     * @throws UnsupportedOperationException if the {@code removeAll} operation
     * is not supported by this collection
     * @throws ClassCastException if the type of the backing collection is not
     * compatible with this view for the specified forwarding type, or if the
     * class of an element of this collection is incompatible with the
     * specified collection (optional)
     * @throws NullPointerException if one or more elements of the specified
     * collection is null (optional) or if the specified collection is null
     * @throws IllegalStateException if some aspect of the backing collection
     * prevents this operation from being resolved (optional)
     */
    @Override
    public abstract boolean removeAll(Collection<?> collection);

    /**
     * <p>Removes all of this collection's elements that are also contained in the
     * specified collection (optional operation).</p>
     * @implSpec <p>This method must be implemented by a specialization-aware
     * subclass to account for specialization-dependent default behavior.</p>
     * @param collection collection containing elements to be removed from this
     *                  collection
     * @return {@code true} if this collection changed as a result of the call
     * @throws CollectionNotModifiableException if this view is unmodifiable
     * @throws InvalidForwardingContextException if some aspect of the backing
     * collection is invalid for this forwarding type (optional) or the
     * forwarding type is {@link ForwardingType#MINIMAL}
     * @throws UnsupportedOperationException if the {@code removeAll} operation
     * is not supported by this collection
     * @throws ClassCastException if the type of the backing collection is not
     * compatible with this view for the specified forwarding type (optional)
     * @throws NullPointerException if the specified collection is null
     * @throws IllegalStateException if some aspect of the backing collection
     * prevents this operation from being resolved (optional)
     */
    @Override
    public abstract boolean removeAll(T_COLL collection);

    /**
     * <p>Removes all of the elements of this collection that satisfy the given
     * predicate (optional operation).</p>
     * @implSpec <p>This method must be implemented by a specialization-aware
     * subclass to account for specialization-dependent default behavior.</p>
     * @param filter a predicate which returns {@code true} for elements to be
     *               removed
     * @return {@code true} if any elements were removed
     * @throws CollectionNotModifiableException if this view is unmodifiable
     * @throws InvalidForwardingContextException if some aspect of the backing
     * collection is invalid for this forwarding type (optional)
     * @throws UnsupportedOperationException if the {@code removeIf} operation
     * is not supported by this collection
     * @throws ClassCastException if the type of the backing collection is not
     * compatible with this view for the specified forwarding type (optional)
     * @throws NullPointerException if the specified filter is null
     * @throws IllegalStateException if some aspect of the backing collection
     * prevents this operation from being resolved (optional)
     */
    @Override
    public abstract boolean removeIf(Predicate<? super T> filter);

    /**
     * <p>Removes all of the elements of this collection that satisfy the given
     * predicate (optional operation).</p>
     * @implSpec <p>This method must be implemented by a specialization-aware
     * subclass to account for specialization-dependent default behavior.</p>
     * @param filter a predicate which returns {@code true} for elements to be
     *               removed
     * @return {@code true} if any elements were removed
     * @throws CollectionNotModifiableException if this view is unmodifiable
     * @throws InvalidForwardingContextException if some aspect of the backing
     * collection is invalid for this forwarding type (optional)
     * @throws UnsupportedOperationException if the {@code removeIf} operation
     * is not supported by this collection
     * @throws ClassCastException if the type of the backing collection is not
     * compatible with this view for the specified forwarding type (optional)
     * @throws NullPointerException if the specified filter is null
     * @throws IllegalStateException if some aspect of the backing collection
     * prevents this operation from being resolved (optional)
     */
    @Override
    public abstract boolean removeIf(T_PRED filter);

    /**
     * <p>Retains only the elements in this collection that are contained in the
     * specified collection (optional operation).</p>
     * @implSpec <p>This method must be implemented by a specialization-aware
     * subclass to account for specialization-dependent default behavior.</p>
     * @param collection collection containing elements to be retained in this
     *                  collection
     * @return {@code true} if this collection changed as a result of the call
     * @throws CollectionNotModifiableException if this view is unmodifiable
     * @throws InvalidForwardingContextException if some aspect of the backing
     * collection is invalid for this forwarding type (optional)
     * @throws UnsupportedOperationException if the {@code retainAll}
     * operation is not supported by this collection
     * @throws ClassCastException if the type of the backing collection is not
     * compatible with this view for the specified forwarding type, or if the
     * type of one or more elements in this collection are incompatible with
     * the specified collection (optional)
     * @throws NullPointerException if the specified collection is null
     * @throws IllegalStateException if some aspect of the backing collection
     * prevents this operation from being resolved (optional)
     */
    @Override
    public abstract boolean retainAll(Collection<?> collection);

    /**
     * <p>Retains only the elements in this collection that are contained in the
     * specified collection (optional operation).</p>
     * @implSpec <p>This method must be implemented by a specialization-aware
     * subclass to account for specialization-dependent default behavior.</p>
     * @param collection collection containing elements to be retained in this
     *                  collection
     * @return {@code true} if this collection changed as a result of the call
     * @throws CollectionNotModifiableException if this view is unmodifiable
     * @throws InvalidForwardingContextException if some aspect of the backing
     * collection is invalid for this forwarding type (optional)
     * @throws UnsupportedOperationException if the {@code retainAll}
     * operation is not supported by this collection
     * @throws ClassCastException if the type of the backing collection is not
     * compatible with this view for the specified forwarding type (optional)
     * @throws NullPointerException if the specified collection is null
     * @throws IllegalStateException if some aspect of the backing collection
     * prevents this operation from being resolved (optional)
     */
    @Override
    public abstract boolean retainAll(T_COLL collection);

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
     * <p>Creates a spliterator over the elements in this collection.</p>
     * @implSpec <p>This method must be implemented by a specialization-aware
     * subclass to account for specialization-dependent default behavior.</p>
     * @return a spliterator over the elements in this collection
     * @throws InvalidForwardingContextException if some aspect of the backing
     * collection is invalid for this forwarding type (optional) or the
     * forwarding type is {@link ForwardingType#MINIMAL}
     * @throws ClassCastException if the type of the backing collection is not
     * compatible with this view for the specified forwarding type (optional)
     * @throws IllegalStateException if some aspect of the backing collection
     * prevents this operation from being resolved (optional)
     */
    @Override
    public abstract T_SPLITR spliterator();

    /**
     * <p>Returns a sequential {@code Stream} with this collection as its
     * source.</p>
     * @implNote <p>If forwarding type is {@link ForwardingType#PURE}, the
     * request is forwarded to the backing collection; otherwise, the default
     * implementation from {@link PrimitiveCollection#stream()} is used.</p>
     * @return a sequential stream over the elements in this collection
     */
    @Override
    public Stream<T> stream() {
        return forwarder().boxedOp(PrimitiveCollection::stream,
                PrimitiveCollection.super::stream);
    }

    /**
     * <p>Returns an array containing all of the elements in this collection.
     * </p>
     * @implNote <p>If forwarding type is {@link ForwardingType#PURE}, the
     * request is forwarded to the backing collection; otherwise, the default
     * implementation from {@link PrimitiveCollection#toArray()} is used.</p>
     * @return an array containing all the elements in this collection
     */
    @Override
    public Object[] toArray() {
        return forwarder().boxedOp(PrimitiveCollection::toArray,
                PrimitiveCollection.super::toArray);
    }

    /**
     * <p>Returns an array containing all of the elements in this collection;
     * the runtime type of the returned array is that returned by the
     * specified generator function.</p>
     * @implNote <p>If forwarding type is {@link ForwardingType#PURE}, the
     * request is forwarded to the backing collection; otherwise, the default
     * implementation from {@link PrimitiveCollection#toArray(IntFunction)} is
     * used.</p>
     * @param generator function that returns an array
     * @return an array containing all of the elements in this collection
     * @param <U> the component type of the array to contain the collection
     * @throws ArrayStoreException if the runtime type of any element in
     * this collection is not assignable to the runtime component type of the
     * array returned by the generator
     * @throws NullPointerException if generator is null or returns null
     * @throws OutOfMemoryError if it is not possible to allocate a
     * sufficiently large array to contain this collection (i.e. if the size
     * of the collection exceeds the maximum capacity of a Java array)
     */
    @Override
    public <U> U[] toArray(IntFunction<U[]> generator) {
        return forwarder().boxedOp(delegate -> delegate.toArray(generator),
                () -> PrimitiveCollection.super.toArray(generator));
    }

    /**
     * <p>Returns an array containing all of the elements in this collection;
     * the runtime type of the returned array is that of the specified
     * array.</p>
     * @implNote <p>If forwarding type is {@link ForwardingType#PURE}, the
     * request is forwarded to the backing collection; otherwise, the default
     * implementation from {@link PrimitiveCollection#toArray(Object[])} is
     * used.</p>
     * @param a the array into which the elements of this collection are to be
     *          stored, if it is big enough; otherwise, a new array of the
     *          same runtime type is allocated for this purpose.
     * @return an array containing all of the elements in this collection
     * @param <U> the component type of the array to contain the collection
     * @throws ArrayStoreException if the runtime type of any element in
     * this collection is not assignable to the runtime component type of the
     * specified array
     * @throws NullPointerException if the specified array is null
     * @throws OutOfMemoryError if it is not possible to allocate a
     * sufficiently large array to contain this collection (i.e. if the size
     * of the collection exceeds the maximum capacity of a Java array)
     */
    @Override
    public <U> U[] toArray(U[] a) {
        return forwarder().boxedOp(delegate -> delegate.toArray(a),
                () -> PrimitiveCollection.super.toArray(a));
    }
}
