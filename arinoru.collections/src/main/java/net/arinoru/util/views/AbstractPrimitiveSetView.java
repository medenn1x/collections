package net.arinoru.util.views;

import net.arinoru.util.CollectionNotModifiableException;
import net.arinoru.util.PrimitiveCollection;
import net.arinoru.util.PrimitiveCollections;
import net.arinoru.util.PrimitiveSet;

import java.util.Collection;
import java.util.PrimitiveIterator;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.BaseStream;
import java.util.stream.Stream;

/**
 * <p>An internal implementation of a primitive set view. This class defines a
 * series of default behaviors for set operations based on a {@link Forwarder},
 * an object that provides access to a backing set and means of providing
 * varying levels of access to functionality based on a configured
 * {@link ForwardingType}.</p>
 * <p>The requirements for the backing set vary based on the forwarding type,
 * with {@link ForwardingType#MINIMAL} allowing the use of any backing set,
 * while {@link ForwardingType#PURE} and {@link ForwardingType#SHALLOW} both
 * require a primitive set whose backing element type matches the view's
 * element type.</p>
 * <p>Methods which have default implementations in specializations of
 * {@link PrimitiveSet} are declared {@code abstract} to ensure that
 * implementing collections provide proper behavior for
 * {@link ForwardingType#SHALLOW} and {@link ForwardingType#MINIMAL}.</p>
 * <p>The general rule for the implementation of set operations in this class
 * depends on the forwarding type and whether or not the {@link PrimitiveSet}
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
 *     {@link PrimitiveSet} interface are forwarded to the backing set if the
 *     forwarding type is {@link ForwardingType#PURE}; otherwise, they are
 *     implemented in terms of the default method, which may itself invoke
 *     other set operations on the view.</li>
 *     <li>Operations that are sensitive to the element type of the set (e.g.
 *     those that add or remove elements or are otherwise bounded by subtype,
 *     or those provided only by {@link PrimitiveSet}) and do not have a
 *     default implementation in {@link PrimitiveSet} will throw an
 *     {@link InvalidForwardingContextException} if the forwarding type is
 *     {@link ForwardingType#MINIMAL}.</li>
 *     <li>Operations not meeting the above criteria will generally forward
 *     to the backing set. If some aspect of the type difference between the
 *     view and the backing set prevents the proper operation of the forward,
 *     an exception may be thrown (e.g.
 *     {@link InvalidForwardingContextException}, {@link ClassCastException},
 *     or {@link IllegalStateException}).</li>
 * </ol>
 * @param <T> boxed element type of the view
 * @param <T_ARR> the type of arrays who have the set's primitive element
 *                type as their runtime component type
 * @param <T_CONS> the type of primitive consumer
 * @param <T_PRED> the type of primitive predicate
 * @param <T_ITER> the type of primitive iterator
 * @param <T_SPLITR> the type of primitive spliterator
 * @param <T_STR> the type of primitive stream
 * @param <T_COLL> the type of primitive collection
 * @param <T_SET> the type of primitive set
 */
public abstract class AbstractPrimitiveSetView<T,T_ARR,T_CONS,T_PRED,
        T_ITER extends PrimitiveIterator<T,T_CONS>,
        T_SPLITR extends Spliterator.OfPrimitive<T,T_CONS,T_SPLITR>,
        T_STR extends BaseStream<T,T_STR>,
        T_COLL extends PrimitiveCollection<T,T_ARR,T_CONS,T_PRED,T_ITER,T_SPLITR,T_STR,T_COLL>,
        T_SET extends PrimitiveSet<T,T_ARR,T_CONS,T_PRED,T_ITER,T_SPLITR,T_STR,T_COLL>>
        implements View, PrimitiveSet<T,T_ARR,T_CONS,T_PRED,T_ITER,T_SPLITR,T_STR,T_COLL> {
    /**
     * <p>Returns the {@link Forwarder} managing access to the backing set.</p>
     * @implSpec <p>Implementing classes must override this to return a forwarder
     * that represents the backing set and forwarding type. The forwarder is
     * not stored by this class directly.</p>
     * @return forwarder that manages access to the backing set
     */
    protected abstract Forwarder<Set<?>,T_SET> forwarder();

    /**
     * <p>Adds the specified element to this set if it is not already present
     * (optional operation).</p>
     * @implSpec <p>This method must be implemented by a specialization-aware
     * subclass to account for specialization-dependent default behavior.</p>
     * @param element element to be added to this set
     * @return {@code true} if this set did not already contain the specified
     * element
     * @throws CollectionNotModifiableException if this view is unmodifiable
     * @throws InvalidForwardingContextException if some aspect of the backing
     * set is invalid for this forwarding type (optional), or the forwarding
     * type is {@link ForwardingType#MINIMAL}
     * @throws UnsupportedOperationException if the {@code add} operation is
     * not supported by this set
     * @throws ClassCastException if the type of the backing set is not
     * compatible with this view for the specified forwarding type (optional),
     * or if the class of the specified element prevents it from being added
     * to this set
     * @throws NullPointerException if the specified element is null
     * @throws IllegalArgumentException if some property of the specified
     * element prevents it from being added to this set
     * @throws IllegalStateException if some aspect of the backing set
     * prevents this operation from being resolved (optional)
     */
    @Override
    public abstract boolean add(T element);

    /**
     * <p>Adds all of the elements in the specified collection to this set if
     * they're not already present (optional operation).</p>
     * @implSpec <p>This method must be implemented by a specialization-aware
     * subclass to account for specialization-dependent default behavior.</p>
     * @param collection collection containing elements to be added to this
     *                   set
     * @return {@code true} if this set changed as a result of the call
     * @throws CollectionNotModifiableException if this view is unmodifiable
     * @throws InvalidForwardingContextException if some aspect of the backing
     * set is invalid for this forwarding type (optional), or the forwarding
     * type is {@link ForwardingType#MINIMAL}
     * @throws UnsupportedOperationException if the {@code addAll} operation
     * is not supported by this set
     * @throws ClassCastException if the type of the backing set is not
     * compatible with this view for the specified forwarding type (optional),
     * or if the class of an element of the specified collection prevents it
     * from being added to this set
     * @throws NullPointerException if the specified collection contains one
     * or more null elements, or if the specified collection is null
     * @throws IllegalArgumentException if some property of an element of the
     * specified collection prevents it from being added to this set
     * @throws IllegalStateException if some aspect of the backing set
     * prevents this operation from being resolved (optional)
     */
    @Override
    public abstract boolean addAll(Collection<? extends T> collection);

    /**
     * <p>Adds all of the elements in the specified collection to this set if
     * they're not already present (optional operation).</p>
     * @implSpec <p>This method must be implemented by a specialization-aware
     * subclass to account for specialization-dependent default behavior.</p>
     * @param collection collection containing elements to be added to this
     *                   set
     * @return {@code true} if this set changed as a result of the call
     * @throws CollectionNotModifiableException if this view is unmodifiable
     * @throws InvalidForwardingContextException if some aspect of the backing
     * set is invalid for this forwarding type (optional), or the forwarding
     * type is {@link ForwardingType#MINIMAL}
     * @throws UnsupportedOperationException if the {@code addAll} operation
     * is not supported by this set
     * @throws ClassCastException if the type of the backing set is not
     * compatible with this view for the specified forwarding type (optional),
     * or if the class of an element of the specified collection prevents it
     * from being added to this set
     * @throws NullPointerException if the specified collection is null
     * @throws IllegalArgumentException if some property of an element of the
     * specified collection prevents it from being added to this set
     * @throws IllegalStateException if some aspect of the backing set
     * prevents this operation from being resolved (optional)
     */
    @Override
    public abstract boolean addAll(T_COLL collection);

    /**
     * <p>Removes all of the elements from this set (optional operation). The
     * set will be empty after this call returns.</p>
     * @apiNote <p>This is under review, since a default implementation of
     * {@code clear} is possible at this level at the cost of unnecessary
     * boxing; since default implementations are not required to be efficient,
     * this may be deemed acceptable. However, leaving default implementations
     * to specializations is not especially burdensome in this case.</p>
     * @implSpec <p>This method must be implemented by a specialization-aware
     * subclass to account for specialization-dependent default behavior.</p>
     * @throws CollectionNotModifiableException if this view is unmodifiable
     * @throws InvalidForwardingContextException if some aspect of the backing
     * set is invalid for this forwarding type (optional)
     * @throws UnsupportedOperationException if the {@code clear} operation is
     * not supported by this set
     * @throws ClassCastException if the type of the backing set is not
     * compatible with this view for the specified forwarding type (optional)
     * @throws IllegalStateException if some aspect of the backing set
     * prevents this operation from being resolved (optional)
     */
    @Override
    public abstract void clear();

    /**
     * <p>Returns {@code true} if this set contains the specified element.</p>
     * @apiNote <p>This is under review, since a default implementation of
     * {@code contains} is possible at this level at the cost of unnecessary
     * boxing; since default implementations are not required to be efficient,
     * this may be deemed acceptable. However, leaving default implementations
     * to specializations is not especially burdensome in this case.</p>
     * @implSpec <p>This method must be implemented by a specialization-aware
     * subclass to account for specialization-dependent default behavior.</p>
     * @param o element whose presence in this set is to be tested
     * @return {@code true} if this set contains the specified element
     * @throws InvalidForwardingContextException if some aspect of the backing
     * set is invalid for this forwarding type (optional), or if forwarding
     * type is {@code MINIMAL}
     * @throws ClassCastException if the type of the backing set is not
     * compatible with this view for the specified forwarding type, or if the
     * type of the specified element is incompatible with this set (optional)
     * @throws NullPointerException if the specified element is null
     * (optional)
     * @throws IllegalStateException if some aspect of the backing set
     * prevents this operation from being resolved (optional)
     */
    @Override
    public abstract boolean contains(Object o);

    /**
     * <p>Returns {@code true} if this set contains all of the elements of the
     * specified collection.</p>
     * @apiNote <p>This is under review, since a default implementation of
     * {@code containsAll} is possible at this level at the possible cost of
     * unnecessary boxing; since default implementations are not required to
     * be efficient, this may be deemed acceptable. However, leaving default
     * implementations to specializations is not especially burdensome in this
     * case.</p>
     * @implSpec <p>This method must be implemented by a specialization-aware
     * subclass to account for specialization-dependent default behavior.</p>
     * @param collection collection to be checked for containment in this set
     * @return {@code true} if this set contains all of the elements of the
     * specified collection
     * @throws InvalidForwardingContextException if some aspect of the backing
     * set is invalid for this forwarding type (optional)
     * @throws ClassCastException if the type of the backing set is not
     * compatible with this view for the specified forwarding type, or if the
     * types of one or more elements in the specified collection are
     * incompatible with this set (optional)
     * @throws NullPointerException if the specified collection contains one
     * or more null elements (optional), or if the specified collection is
     * null
     * @throws IllegalStateException if some aspect of the backing set
     * prevents this operation from being resolved (optional)
     */
    @Override
    public abstract boolean containsAll(Collection<?> collection);

    /**
     * <p>Returns {@code true} if this set contains all of the elements of the
     * specified collection.</p>
     * @apiNote <p>This is under review, since a default implementation of
     * {@code containsAll} is possible at this level at the possible cost of
     * unnecessary boxing; since default implementations are not required to
     * be efficient, this may be deemed acceptable. However, leaving default
     * implementations to specializations is not especially burdensome in this
     * case.</p>
     * @implSpec <p>This method must be implemented by a specialization-aware
     * subclass to account for specialization-dependent default behavior.</p>
     * @param collection collection to be checked for containment in this set
     * @return {@code true} if this set contains all of the elements of the
     * specified collection
     * @throws InvalidForwardingContextException if some aspect of the backing
     * set is invalid for this forwarding type (optional)
     * @throws ClassCastException if the type of the backing set is not
     * compatible with this view for the specified forwarding type (optional)
     * @throws NullPointerException if the specified collection is null
     * @throws IllegalStateException if some aspect of the backing set
     * prevents this operation from being resolved (optional)
     */
    @Override
    public abstract boolean containsAll(T_COLL collection);

    /**
     * <p>Compares the specified object with this set for equalty. Returns
     * {@code true} if the specified object is also a set, the two sets have
     * the same size, and every member of the specified set is contained in
     * this set (or equivalently, every member of this set is contained in
     * the specified set). This definition ensures that the equals method
     * works properly across different implementations of the set
     * interface.</p>
     * @implNote <p>If the forwarding type is {@link ForwardingType#PURE}, the
     * request is forwarded to the backing set; otherwise, the
     * {@link PrimitiveCollections#equals(PrimitiveSet, Object)} method is
     * used to determine equality.</p>
     * @param o object to be compared for equality with this set
     * @return {@code true} if the specified object is equal to this set
     */
    @Override
    @SuppressWarnings("EqualsDoesntCheckParameterClass")
    public boolean equals(Object o) {
        return forwarder().predicateOp(delegate -> delegate.equals(o),
                () -> PrimitiveCollections.equals(this, o));
    }

    /**
     * <p>Performs the given action for each element of the set until all
     * elements have been processed or the action throws an exception.</p>
     * @implNote <p>This method forwards the request to the backing set if
     * the forwarding type is {@link ForwardingType#PURE}; otherwise, it
     * invokes the default implementation from
     * {@link PrimitiveSet#forEach(Consumer)}.</p>
     * @param action action to perform on each element of the set
     * @throws NullPointerException if the specified action is null
     */
    @Override
    public void forEach(Consumer<? super T> action) {
        forwarder().voidOp(delegate -> delegate.forEach(action),
                () -> PrimitiveSet.super.forEach(action));
    }

    /**
     * <p>Performs the given action for each element of the set until all
     * elements have been processed or the action throws an exception.</p>
     * @implNote <p>This method forwards the request to the backing set if
     * the forwarding type is {@link ForwardingType#PURE}; otherwise, it
     * invoked the default implementation from
     * {@link PrimitiveSet#forEach(Object) PrimitiveSet.forEach(T_CONS)}.</p>
     * @param action action to perform on each element of the set
     * @throws NullPointerException if the specified action is null
     */
    @Override
    public void forEach(T_CONS action) {
        forwarder().voidOp(delegate -> delegate.forEach(action),
                () -> PrimitiveSet.super.forEach(action));
    }

    /**
     * <p>Returns the hash code value for this set. The hash code of a set is
     * defined to be the sum of the hash codes of the elements in the set. This
     * ensures that {@code s1.equals(s2)} implies that
     * {@code s1.hashCode()==s2.hashCode()} for any two sets {@code s1} and
     * {@code s2}, as required by the general contract of
     * {@link Object#equals(Object)}.</p>
     * @implNote <p>If the forwarding type is {@link ForwardingType#PURE}, the
     * request is forwarded to the backing set; otherwise, the reference
     * implementation of {@link PrimitiveCollections#hashCode(PrimitiveSet)}
     * will be used.</p>
     * @return the hash code value for this set
     */
    @Override
    public int hashCode() {
        return forwarder().intOp(PrimitiveSet::hashCode,
                () -> PrimitiveCollections.hashCode(this));
    }

    /**
     * <p>Returns {@code true} if this set contains no elements.</p>
     * @implNote <p>If the forwarding type is {@link ForwardingType#PURE}, the
     * request is forwarded to the backing set; otherwise, the default
     * implementation from {@link PrimitiveSet#isEmpty()} is used.</p>
     * @return {@code true} if this set contains no elements
     */
    @Override
    public boolean isEmpty() {
        return forwarder().predicateOp(PrimitiveSet::isEmpty,
                PrimitiveSet.super::isEmpty);
    }

    /**
     * <p>Returns a primitive iterator over the elements in this set.</p>
     * @implSpec <p>This method must be implemented by a specialization-aware
     * subclass to account for specialization-dependent default behavior.</p>
     * @return a {@code PrimitiveIterator} over the elements in this set
     * @throws InvalidForwardingContextException if some aspect of the backing
     * set is invalid for this forwarding type (optional)
     * @throws ClassCastException if the type of the backing set is not
     * compatible with this view for the specified forwarding type (optional)
     * @throws IllegalStateException if some aspect of the backing set
     * prevents this operation from being resolved (optional)
     */
    @Override
    public abstract T_ITER iterator();

    /**
     * <p>Returns a possibly parallel primitive stream with this set as its
     * source. It is allowable for this method to return a sequential
     * primitive stream.</p>
     * @implSpec <p>This method must be implemented by a specialization-aware
     * subclass to account for specialization-dependent default behavior.</p>
     * @return a possibly parallel primitive stream over the elements in this
     * set
     * @throws InvalidForwardingContextException if some aspect of the backing
     * set is invalid for this forwarding type (optional)
     * @throws ClassCastException if the type of the backing set is not
     * compatible with this view for the specified forwarding type (optional)
     * @throws IllegalStateException if some aspect of the backing set
     * prevents this operation from being resolved (optional)
     */
    @Override
    public abstract T_STR parallelPrimitiveStream();

    /**
     * <p>Returns a possibly parallel {@code Stream} with this set as its
     * source. It is allowable for this method to return a sequential
     * stream.</p>
     * @implNote <p>If the forwarding type is {@link ForwardingType#PURE},
     * this method will forward the request to the backing set; otherwise,
     * the default implementation from {@link PrimitiveSet#parallelStream()}
     * will be used.</p>
     * @return a possibly parallel stream over the elements in this set
     */
    @Override
    public Stream<T> parallelStream() {
        return forwarder().boxedOp(PrimitiveSet::parallelStream,
                PrimitiveSet.super::parallelStream);
    }

    /**
     * <p>Returns a sequential primitive stream with this set as its
     * source.</p>
     * @implSpec <p>This method must be implemented by a specialization-aware
     * subclass to account for specialization-dependent default behavior.</p>
     * @return a possibly parallel primitive stream over the elements in this
     * set
     * @throws InvalidForwardingContextException if some aspect of the backing
     * set is invalid for this forwarding type (optional)
     * @throws ClassCastException if the type of the backing set is not
     * compatible with this view for the specified forwarding type (optional)
     * @throws IllegalStateException if some aspect of the backing set
     * prevents this operation from being resolved (optional)
     */
    @Override
    public abstract T_STR primitiveStream();

    /**
     * <p>Removes the specified element from this set if it is present
     * (optional operation).</p>
     * @implSpec <p>This method must be implemented by a specialization-aware
     * subclass to account for specialization-dependent default behavior.</p>
     * @param o element to be removed from this set, if present
     * @return {@code true} if this set contained the specified element
     * @throws CollectionNotModifiableException if this view is unmodifiable
     * @throws InvalidForwardingContextException if some aspect of the backing
     * set is invalid for this forwarding type (optional)
     * @throws UnsupportedOperationException if the {@code remove} operation
     * is not supported by this set
     * @throws ClassCastException if the type of the backing set is not
     * compatible with this view for the specified forwarding type, or if the
     * type of the specified element is incompatible with this set (optional)
     * @throws NullPointerException if the specified element is null (optional)
     * @throws IllegalStateException if some aspect of the backing set
     * prevents this operation from being resolved (optional)
     */
    @Override
    public abstract boolean remove(Object o);

    /**
     * <p>Removes from this set all of its elements that are contained in the
     * specified collection (optional operation).</p>
     * @implSpec <p>This method must be implemented by a specialization-aware
     * subclass to account for specialization-dependent default behavior.</p>
     * @param collection collection containing elements to be removed from
     *                   this set
     * @return {@code true} if this set changed as a result of the call
     * @throws CollectionNotModifiableException if this view is unmodifiable
     * @throws InvalidForwardingContextException if some aspect of the backing
     * set is invalid for this forwarding type (optional)
     * @throws UnsupportedOperationException if the {@code removeAll} operation
     * is not supported by this set
     * @throws ClassCastException if the type of the backing set is not
     * compatible with this view for the specified forwarding type, or if the
     * class of an element of this set is incompatible with the specified
     * collection (optional)
     * @throws NullPointerException if one or more elements of the specified
     * collection is null (optional) or if the specified collection is null
     * @throws IllegalStateException if some aspect of the backing set
     * prevents this operation from being resolved (optional)
     */
    @Override
    public abstract boolean removeAll(Collection<?> collection);

    /**
     * <p>Removes from this set all of its elements that are contained in the
     * specified collection (optional operation).</p>
     * @implSpec <p>This method must be implemented by a specialization-aware
     * subclass to account for specialization-dependent default behavior.</p>
     * @param collection collection containing elements to be removed from
     *                   this set
     * @return {@code true} if this set changed as a result of the call
     * @throws CollectionNotModifiableException if this view is unmodifiable
     * @throws InvalidForwardingContextException if some aspect of the backing
     * set is invalid for this forwarding type (optional)
     * @throws UnsupportedOperationException if the {@code removeAll} operation
     * is not supported by this set
     * @throws ClassCastException if the type of the backing set is not
     * compatible with this view for the specified forwarding type (optional)
     * @throws NullPointerException if the specified collection is null
     * @throws IllegalStateException if some aspect of the backing set
     * prevents this operation from being resolved (optional)
     */
    @Override
    public abstract boolean removeAll(T_COLL collection);

    /**
     * <p>Removes from this set all of its elements that match the specified
     * filter (optional operation).</p>
     * @implSpec <p>This method must be implemented by a specialization-aware
     * subclass to account for specialization-dependent default behavior.</p>
     * @param filter filter to determine whether an element should be removed
     * @return {@code true} if this set changed as a result of the call
     * @throws CollectionNotModifiableException if this view is unmodifiable
     * @throws InvalidForwardingContextException if some aspect of the backing
     * set is invalid for this forwarding type (optional)
     * @throws UnsupportedOperationException if the {@code removeIf} operation
     * is not supported by this set
     * @throws ClassCastException if the type of the backing set is not
     * compatible with this view for the specified forwarding type (optional)
     * @throws NullPointerException if the specified filter is null
     * @throws IllegalStateException if some aspect of the backing set
     * prevents this operation from being resolved (optional)
     */
    @Override
    public abstract boolean removeIf(Predicate<? super T> filter);

    /**
     * <p>Removes from this set all of its elements that match the specified
     * filter (optional operation).</p>
     * @implSpec <p>this method must be implemented by a specialization-aware
     * subclass to account for specialization-dependent default behavior.</p>
     * @param filter filter to determine whether an element should be removed
     * @return {@code true} if this set changed as a result of the call
     * @throws CollectionNotModifiableException if this view is unmodifiable
     * @throws InvalidForwardingContextException if some aspect of the backing
     * set is invalid for this forwarding type (optional)
     * @throws UnsupportedOperationException if the {@code removeIf} operation
     * is not supported by this set
     * @throws ClassCastException if the type of the backing set is not
     * compatible with this view for the specified forwarding type (optional)
     * @throws NullPointerException if the specified filter is null
     * @throws IllegalStateException if some aspect of the backing set
     * prevents this operation from being resolved (optional)
     */
    @Override
    public abstract boolean removeIf(T_PRED filter);

    /**
     * <p>Retains only the elements in this set that are contained in the
     * specified collection (optional operation).</p>
     * @implSpec <p>This method must be implemented by a specialization-aware
     * subclass to account for specialization-dependent default behavior.</p>
     * @param collection collection containing elements to be retained in
     *                   this set
     * @return {@code true} if this set changed as a result of the call
     * @throws CollectionNotModifiableException if this view is unmodifiable
     * @throws InvalidForwardingContextException if some aspect of the backing
     * set is invalid for this forwarding type (optional)
     * @throws UnsupportedOperationException if the {@code retainAll} operation
     * is not supported by this set
     * @throws ClassCastException if the type of the backing set is not
     * compatible with this view for the specified forwarding type, or if the
     * class of an element of this set is incompatible with the specified
     * collection (optional)
     * @throws NullPointerException if the specified collection is null
     * @throws IllegalStateException if some aspect of the backing set
     * prevents this operation from being resolved (optional)
     */
    @Override
    public abstract boolean retainAll(Collection<?> collection);

    /**
     * <p>Retains only the elements in this set that are contained in the
     * specified collection (optional operation).</p>
     * @implSpec <p>This method must be implemented by a specialization-aware
     * subclass to account for specialization-dependent default behavior.</p>
     * @param collection collection containing elements to be retained in
     *                    this set
     * @return {@code true} if this set changed as a result of the call
     * @throws CollectionNotModifiableException if this view is unmodifiable
     * @throws InvalidForwardingContextException if some aspect of the backing
     * set is invalid for this forwarding type (optional)
     * @throws UnsupportedOperationException if the {@code retainAll} operation
     * is not supported by this set
     * @throws ClassCastException if the type of the backing set is not
     * compatible with this view for the specified forwarding type (optional)
     * @throws NullPointerException if the specified collection is null
     * @throws IllegalStateException if some aspect of the backing set
     * prevents this operation from being resolved (optional)
     */
    @Override
    public abstract boolean retainAll(T_COLL collection);

    /**
     * <p>Returns the number of elements in this set (its cardinality). If this
     * set contains more than {@link Integer#MAX_VALUE} element, returns
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
     * @implSpec <p>This method must be implemented by a specialization-aware
     * subclass to account for specialization-dependent default behavior.</p>
     * @return a spliterator over the elements in this set
     * @throws InvalidForwardingContextException if some aspect of the backing
     * set is invalid for this forwarding type (optional) or the forwarding
     * type is {@link ForwardingType#MINIMAL}
     * @throws ClassCastException if the type of the backing set is not
     * compatible with this view for the specified forwarding type (optional)
     * @throws IllegalStateException if some aspect of the backing set
     * prevents this operation from being resolved (optional)
     */
    @Override
    public abstract T_SPLITR spliterator();

    /**
     * <p>Returns a sequential {@code Stream} with this set as its source.</p>
     * @implNote <p>If forwarding type is {@link ForwardingType#PURE}, the
     * request is forwarded to the backing set; otherwise, the default
     * implementation from {@link PrimitiveSet#stream()} is used.</p>
     * @return a sequential stream over the elements in this set
     */
    @Override
    public Stream<T> stream() {
        return forwarder().boxedOp(PrimitiveSet::stream, PrimitiveSet.super::stream);
    }

    /**
     * <p>Returns an array containing all of the elements in this set.</p>
     * @implNote <p>If forwarding type is {@link ForwardingType#PURE}, the
     * request is forwarded to the backing set; otherwise, the default
     * implementation from {@link PrimitiveSet#toArray()} is used.</p>
     * @return an array containing all the elements in this set
     */
    @Override
    public Object[] toArray() {
        return forwarder().boxedOp(PrimitiveSet::toArray, PrimitiveSet.super::toArray);
    }

    /**
     * <p>Returns an array containing all of the elements in this set; the
     * runtime type of the returned array is that returned by the specified
     * generator function.</p>
     * @implNote <p>If forwarding type is {@link ForwardingType#PURE}, the
     * request is forwarded to the backing set; otherwise, the default
     * implementation from {@link PrimitiveSet#toArray(IntFunction)} is
     * used.</p>
     * @param generator function that returns an array
     * @return an array containing all of the elements in this set
     * @param <U> the component type of the array to contain the set
     * @throws ArrayStoreException if the runtime type of any element in
     * this set is not assignable to the runtime component type of the
     * array returned by the generator
     * @throws NullPointerException if generator is null or returns null
     * @throws OutOfMemoryError if it is not possible to allocate a
     * sufficiently large array to contain this set (i.e. if the size of
     * the set exceeds the maximum capacity of a Java array)
     */
    @Override
    public <U> U[] toArray(IntFunction<U[]> generator) {
        return forwarder().boxedOp(delegate -> delegate.toArray(generator),
                () -> PrimitiveSet.super.toArray(generator));
    }

    /**
     * <p>Returns an array containing all of the elements in this set; the
     * runtime type of the returned array is that of the specified
     * array.</p>
     * @implNote <p>If forwarding type is {@link ForwardingType#PURE}, the
     * request is forwarded to the backing set; otherwise, the default
     * implementation from {@link PrimitiveSet#toArray(Object[])} is
     * used.</p>
     * @param a the array into which the elements of this set are to be
     *          stored, if it is big enough; otherwise, a new array of the
     *          same runtime type is allocated for this purpose.
     * @return an array containing all of the elements in this set
     * @param <U> the component type of the array to contain the set
     * @throws ArrayStoreException if the runtime type of any element in
     * this set is not assignable to the runtime component type of the
     * specified array
     * @throws NullPointerException if the specified array is null
     * @throws OutOfMemoryError if it is not possible to allocate a
     * sufficiently large array to contain this set (i.e. if the size of
     * the set exceeds the maximum capacity of a Java array)
     */
    @Override
    public <U> U[] toArray(U[] a) {
        return forwarder().boxedOp(delegate -> delegate.toArray(a),
                () -> PrimitiveSet.super.toArray(a));
    }

    /**
     * <p>Returns an array containing all of the elements in this set.</p>
     * @implSpec <p>This method must be implemented by a specialization-aware
     * subclass to account for specialization-dependent default behavior.</p>
     * @return an array containing all of the elements in this set
     * @throws InvalidForwardingContextException if some aspect of the backing
     * set is invalid for this forwarding type (optional) or the forwarding
     * type is {@link ForwardingType#MINIMAL}
     * @throws ClassCastException if the type of the backing set is not
     * compatible with this view for the specified forwarding type (optional)
     * @throws IllegalStateException if some aspect of the backing set
     * prevents this operation from being resolved (optional)
     */
    @Override
    public abstract T_ARR toPrimitiveArray();
}
