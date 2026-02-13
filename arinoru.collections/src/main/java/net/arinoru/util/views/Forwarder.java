package net.arinoru.util.views;

import java.io.Serializable;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.DoubleSupplier;
import java.util.function.Function;
import java.util.function.IntSupplier;
import java.util.function.LongSupplier;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.function.UnaryOperator;

/**
 * <p>An object that manages access to a backing class (the "delegate") based
 * on a configured {@link ForwardingType}. This object will be serializable if
 * the delegate is serializable.</p>
 * <p>The delegate must be an instance of {@code DELEGATE_TYPE}; it is also
 * required to be an instance of {@code VIEW_TYPE} if the forwarding type is
 * {@link ForwardingType#PURE} or {@link ForwardingType#SHALLOW}. The
 * distinction between {@code DELEGATE_TYPE} and {@code VIEW_TYPE}, along with
 * {@link ForwardingType#MINIMAL}, allows the implementation of views backed
 * by collections of other types (e.g. by way of a transformation algorithm).
 * </p>
 * @param <DELEGATE_TYPE> the base required type for the delegate
 * @param <VIEW_TYPE> the base type of the view; this must be a subtype of
 *                    DELEGATE_TYPE
 */
public class Forwarder<DELEGATE_TYPE, VIEW_TYPE extends DELEGATE_TYPE>
        implements Serializable {
    private final DELEGATE_TYPE delegate;
    private final ForwardingType forwardingType;
    private final boolean subViewsRequireMasking;

    /**
     * <p>Creates a new forwarder with a given delegate and forwarding type.
     * </p>
     * <p>Warning: It is a violation of the API contract to specify a
     * forwarding type other than {@link ForwardingType#MINIMAL} when the
     * delegate is not an instance of {@code VIEW_TYPE}! This class
     * <em>will</em> perform unchecked casts of the delegate to
     * {@code VIEW_TYPE} in both {@code PURE} and {@code SHALLOW} modes!</p>
     * @param delegate the backing object to be managed by this forwarder
     * @param forwardingType the default forwarding behavior
     * @param isUnmodifiableView this should be true if and only if the view
     *                           using this forwarder implements
     *                           {@link UnmodifiableView}
     */
    public Forwarder(DELEGATE_TYPE delegate,
              ForwardingType forwardingType,
              boolean isUnmodifiableView) {
        this.delegate = delegate;
        this.forwardingType = forwardingType;
        if (delegate instanceof UnmodifiableView)
            subViewsRequireMasking = false;
        else
            subViewsRequireMasking = isUnmodifiableView;
    }

    /**
     * <p>Returns a reference to the delegate. This is intended for use with
     * operations that are insensitive to the type difference between
     * {@code VIEW_TYPE} and {@code DELEGATE_TYPE}.</p>
     * @return a reference to the delegate.
     */
    public DELEGATE_TYPE asDelegateType() {
        return delegate;
    }

    /**
     * <p>Returns a reference to the delegate, typed as {@code VIEW_TYPE}.</p>
     * <p>Calls to this method are only considered valid if the forwarding
     * type is not {@link ForwardingType#MINIMAL}.</p>
     * @return a reference to the delegate, typed as {@code VIEW_TYPE}
     * @throws InvalidForwardingContextException if forwarding type is
     * {@link ForwardingType#MINIMAL}
     */
    @SuppressWarnings("unchecked")
    public VIEW_TYPE asViewType() {
        return switch (forwardingType) {
            case PURE, SHALLOW -> (VIEW_TYPE) delegate;
            case MINIMAL ->
                    throw new InvalidForwardingContextException("Internal error");
        };
    }

    /**
     * <p>Wraps the specified object in an unmodifiable view if necessary to
     * meet the view's contract requirements.</p>
     * <p>This method will simply return the provided object directly if it
     * implements {@link UnmodifiableView}, or if the view for this forwarder
     * does not. Otherwise, it passes the object to the specified factory and
     * returns the result.</p>
     * @param t object to be wrapped
     * @param factory unmodifiable view factory for the object
     * @return the provided object, possibly wrapped in an unmodifiable view
     * @param <T> type of the object to wrap
     */
    public <T> T maskIfNeeded(T t, UnaryOperator<T> factory) {
        if (t instanceof UnmodifiableView || !subViewsRequireMasking)
            return t;
        return factory.apply(t);
    }

    /**
     * <p>Perform a void (non-value-returning) operation on the delegate.</p>
     * <p>This method passes the delegate, typed as {@code VIEW_TYPE}, to the
     * specified "pure" implementation, so long as the contract allows
     * type-sensitive forwarding. If forwarding type is
     * {@link ForwardingType#MINIMAL}, an exception is thrown.</p>
     * @param pureImpl operation to perform on the delegate
     * @throws InvalidForwardingContextException if forwarding type is
     * {@link ForwardingType#MINIMAL}
     */
    public void voidOp(Consumer<? super VIEW_TYPE> pureImpl) {
        switch (forwardingType) {
            case PURE, SHALLOW -> pureImpl.accept(asViewType());
            case MINIMAL ->
                    throw new InvalidForwardingContextException("Missing override");
        }
    }

    /**
     * <p>Perform a void (non-value-returning) operation on the delegate, or
     * use the specified "shallow" fallback.</p>
     * <p>This method passes the delegate, typed as {@code VIEW_TYPE}, to the
     * specified "pure" implementation only if the forwarding type is
     * {@link ForwardingType#PURE}; otherwise, it executes the "shallow"
     * implementation.</p>
     * @param pureImpl operation to perform on the delegate
     * @param shallowImpl operation to perform when not forwarding to the
     *                    delegate
     */
    public void voidOp(Consumer<? super VIEW_TYPE> pureImpl, Runnable shallowImpl) {
        switch (forwardingType) {
            case PURE -> pureImpl.accept(asViewType());
            case SHALLOW, MINIMAL -> shallowImpl.run();
        }
    }

    /**
     * <p>Perform a void (non-value-returning) operation on the delegate,
     * or throw the specified exception.</p>
     * <p>This method passes the delegate, typed as {@code VIEW_TYPE}, to the
     * specified "pure" implementation, so long as the contract allows
     * type-sensitive forwarding. If forwarding type is
     * {@link ForwardingType#MINIMAL}, an exception is retrieved from the
     * specified factory and thrown.</p>
     * @param pureImpl operation to perform on the delegate
     * @param ifMinimal factory to obtain an exception from when forwarding
     *                  type is {@link ForwardingType#MINIMAL}
     * @throws RuntimeException throws the exception returned by
     * {@code ifMinimal} if forwarding type is {@code MINIMAL}
     */
    public void voidOpOrThrow(Consumer<? super VIEW_TYPE> pureImpl,
                              Supplier<? extends RuntimeException> ifMinimal) {
        switch (forwardingType) {
            case PURE, SHALLOW -> pureImpl.accept(asViewType());
            case MINIMAL -> throw ifMinimal.get();
        }
    }

    /**
     * <p>Perform a void (non-value-returning) operation on the delegate,
     * use the specified "shallow" fallback, or throw the specified exception.
     * </p>
     * <p>This method passes the delegate, typed as {@code VIEW_TYPE}, to the
     * specified "pure" implementation only if the forwarding type is
     * {@link ForwardingType#PURE}; if the forwarding type is
     * {@link ForwardingType#SHALLOW}, it executes the "shallow"
     * implementation. If forwarding type is {@link ForwardingType#MINIMAL},
     * an exception is retrieved from the specified factory and thrown.</p>
     * @param pureImpl operation to perform on the delegate
     * @param shallowImpl operation to perform when not forwarding to the
     *                    delegate
     * @param ifMinimal factory to obtain an exception from when forwarding
     *                  type is {@link ForwardingType#MINIMAL}
     * @throws RuntimeException throws the exception returned by
     * {@code ifMinimal} if forwarding type is {@code MINIMAL}
     */
    public void voidOpOrThrow(Consumer<? super VIEW_TYPE> pureImpl, Runnable shallowImpl,
                              Supplier<? extends RuntimeException> ifMinimal) {
        switch (forwardingType) {
            case PURE -> pureImpl.accept(asViewType());
            case SHALLOW -> shallowImpl.run();
            case MINIMAL -> throw ifMinimal.get();
        }
    }

    /**
     * <p>Perform a predicate (boolean-returning) operation on the delegate.
     * </p>
     * <p>This method passes the delegate, typed as {@code VIEW_TYPE}, to the
     * specified "pure" implementation, so long as the contract allows
     * type-sensitive forwarding. If forwarding type is
     * {@link ForwardingType#MINIMAL}, an exception is thrown.</p>
     * @param pureImpl operation to perform on the delegate
     * @return boolean value returned by operation
     * @throws InvalidForwardingContextException if forwarding type is
     * {@link ForwardingType#MINIMAL}
     */
    public boolean predicateOp(Predicate<? super VIEW_TYPE> pureImpl) {
        return switch (forwardingType) {
            case PURE, SHALLOW -> pureImpl.test(asViewType());
            case MINIMAL ->
                    throw new InvalidForwardingContextException("Missing override");
        };
    }

    /**
     * <p>Perform a predicate (boolean-returning) operation on the delegate,
     * or use the specified "shallow" fallback.</p>
     * <p>This method passes the delegate, typed as {@code VIEW_TYPE}, to the
     * specified "pure" implementation only if the forwarding type is
     * {@link ForwardingType#PURE}; otherwise, it executes the "shallow"
     * implementation.</p>
     * @param pureImpl operation to perform on the delegate
     * @param shallowImpl operation to perform when not forwarding to the
     *                    delegate
     * @return boolean value returned by operation
     */
    public boolean predicateOp(Predicate<? super VIEW_TYPE> pureImpl,
                               BooleanSupplier shallowImpl) {
        return switch (forwardingType) {
            case PURE -> pureImpl.test(asViewType());
            case SHALLOW, MINIMAL -> shallowImpl.getAsBoolean();
        };
    }

    /**
     * <p>Perform a predicate (boolean-returning) operation on the delegate,
     * or throw the specified exception.</p>
     * <p>This method passes the delegate, typed as {@code VIEW_TYPE}, to the
     * specified "pure" implementation, so long as the contract allows
     * type-sensitive forwarding. If forwarding type is
     * {@link ForwardingType#MINIMAL}, an exception is retrieved from the
     * specified factory and thrown.</p>
     * @param pureImpl operation to perform on the delegate
     * @param ifMinimal factory to obtain an exception from when forwarding
     *                  type is {@link ForwardingType#MINIMAL}
     * @return boolean value returned by operation
     * @throws RuntimeException throws the exception returned by
     * {@code ifMinimal} if forwarding type is {@code MINIMAL}
     */
    public boolean predicateOpOrThrow(Predicate<? super VIEW_TYPE> pureImpl,
                                      Supplier<? extends RuntimeException> ifMinimal) {
        return switch (forwardingType) {
            case PURE, SHALLOW -> pureImpl.test(asViewType());
            case MINIMAL -> throw ifMinimal.get();
        };
    }

    /**
     * <p>Perform a boxed (object-returning) operation on the delegate.</p>
     * <p>This method passes the delegate, typed as {@code VIEW_TYPE}, to the
     * specified "pure" implementation, so long as the contract allows
     * type-sensitive forwarding. If forwarding type is
     * {@link ForwardingType#MINIMAL}, an exception is thrown.</p>
     * @param pureImpl operation to perform on the delegate
     * @return object value returned by operation
     * @param <R> type of object returned by operation
     * @throws InvalidForwardingContextException if forwarding type is
     * {@link ForwardingType#MINIMAL}
     */
    public <R> R boxedOp(Function<? super VIEW_TYPE,R> pureImpl) {
        return switch (forwardingType) {
            case PURE, SHALLOW -> pureImpl.apply(asViewType());
            case MINIMAL ->
                    throw new InvalidForwardingContextException("Missing override");
        };
    }

    /**
     * <p>Perform a boxed (object-returning) operation on the delegate, or use
     * the specified "shallow" fallback.</p>
     * <p>This method passes the delegate, typed as {@code VIEW_TYPE}, to the
     * specified "pure" implementation only if the forwarding type is
     * {@link ForwardingType#PURE}; otherwise, it executes the "shallow"
     * implementation.</p>
     * @param pureImpl operation to perform on the delegate
     * @param shallowImpl operation to perform when not forwarding to the
     *                    delegate
     * @return object value returned by operation
     * @param <R> type of object returned by operation
     */
    public <R> R boxedOp(Function<? super VIEW_TYPE,R> pureImpl,
                         Supplier<R> shallowImpl) {
        return switch (forwardingType) {
            case PURE -> pureImpl.apply(asViewType());
            case SHALLOW, MINIMAL -> shallowImpl.get();
        };
    }

    /**
     * <p>Perform a boxed (object-returning) operation on the delegate, use
     * the specified "shallow" fallback, or throw the specified exception.</p>
     * <p>This method passes the delegate, typed as {@code VIEW_TYPE}, to the
     * specified "pure" implementation only if the forwarding type is
     * {@link ForwardingType#PURE}; if the forwarding type is
     * {@link ForwardingType#SHALLOW}, it executes the "shallow"
     * implementation. If forwarding type is {@link ForwardingType#MINIMAL},
     * an exception is retrieved from the specified factory and thrown.</p>
     * @param pureImpl operation to perform on the delegate
     * @param shallowImpl operation to perform when not forwarding to the
     *                    delegate
     * @param ifMinimal factory to obtain an exception from when forwarding
     *                  type is {@link ForwardingType#MINIMAL}
     * @return object value returned by operation
     * @param <R> type of object returned by operation
     * @throws RuntimeException throws the exception returned by
     * {@code ifMinimal} if forwarding type is {@code MINIMAL}
     */
    public <R> R boxedOpOrThrow(Function<? super VIEW_TYPE,R> pureImpl,
                                Supplier<R> shallowImpl,
                                Supplier<? extends RuntimeException> ifMinimal) {
        return switch (forwardingType) {
            case PURE -> pureImpl.apply(asViewType());
            case SHALLOW -> shallowImpl.get();
            case MINIMAL -> throw ifMinimal.get();
        };
    }

    /**
     * <p>Perform a double-returning operation on the delegate.</p>
     * <p>This method passes the delegate, typed as {@code VIEW_TYPE}, to the
     * specified "pure" implementation, so long as the contract allows
     * type-sensitive forwarding. If forwarding type is
     * {@link ForwardingType#MINIMAL}, an exception is thrown.</p>
     * @param pureImpl operation to perform on the delegate
     * @return double value returned by operation
     * @throws InvalidForwardingContextException if forwarding type is
     * {@link ForwardingType#MINIMAL}
     */
    public double doubleOp(ToDoubleFunction<? super VIEW_TYPE> pureImpl) {
        return switch (forwardingType) {
            case PURE, SHALLOW -> pureImpl.applyAsDouble(asViewType());
            case MINIMAL ->
                    throw new InvalidForwardingContextException("Missing override");
        };
    }

    /**
     * <p>Perform a double-returning operation on the delegate, or use the
     * specified "shallow" fallback.</p>
     * <p>This method passes the delegate, typed as {@code VIEW_TYPE}, to the
     * specified "pure" implementation only if the forwarding type is
     * {@link ForwardingType#PURE}; otherwise, it executes the "shallow"
     * implementation.</p>
     * @param pureImpl operation to perform on the delegate
     * @param shallowImpl operation to perform when not forwarding to the
     *                    delegate
     * @return double value returned by operation
     */
    public double doubleOp(ToDoubleFunction<? super VIEW_TYPE> pureImpl,
                           DoubleSupplier shallowImpl) {
        return switch (forwardingType) {
            case PURE -> pureImpl.applyAsDouble(asViewType());
            case SHALLOW, MINIMAL -> shallowImpl.getAsDouble();
        };
    }

    /**
     * <p>Perform an int-returning operation on the delegate.</p>
     * <p>This method passes the delegate, typed as {@code VIEW_TYPE}, to the
     * specified "pure" implementation, so long as the contract allows
     * type-sensitive forwarding. If forwarding type is
     * {@link ForwardingType#MINIMAL}, an exception is thrown.</p>
     * @param pureImpl operation to perform on the delegate
     * @return int value returned by operation
     * @throws InvalidForwardingContextException if forwarding type is
     * {@link ForwardingType#MINIMAL}
     */
    public int intOp(ToIntFunction<? super VIEW_TYPE> pureImpl) {
        return switch (forwardingType) {
            case PURE, SHALLOW -> pureImpl.applyAsInt(asViewType());
            case MINIMAL ->
                    throw new InvalidForwardingContextException("Missing override");
        };
    }

    /**
     * <p>Perform an int-returning operation on the delegate, or use the
     * specified "shallow" fallback.</p>
     * <p>This method passes the delegate, typed as {@code VIEW_TYPE}, to the
     * specified "pure" implementation only if the forwarding type is
     * {@link ForwardingType#PURE}; otherwise, it executes the "shallow"
     * implementation.</p>
     * @param pureImpl operation to perform on the delegate
     * @param shallowImpl operation to perform when not forwarding to the
     *                    delegate
     * @return int value returned by operation
     */
    public int intOp(ToIntFunction<? super VIEW_TYPE> pureImpl, IntSupplier shallowImpl) {
        return switch (forwardingType) {
            case PURE -> pureImpl.applyAsInt(asViewType());
            case SHALLOW, MINIMAL -> shallowImpl.getAsInt();
        };
    }

    /**
     * <p>Perform a long-returning operation on the delegate.</p>
     * <p>This method passes the delegate, typed as {@code VIEW_TYPE}, to the
     * specified "pure" implementation, so long as the contract allows
     * type-sensitive forwarding. If forwarding type is
     * {@link ForwardingType#MINIMAL}, an exception is thrown.</p>
     * @param pureImpl operation to perform on the delegate
     * @return long value returned by operation
     * @throws InvalidForwardingContextException if forwarding type is
     * {@link ForwardingType#MINIMAL}
     */
    public long longOp(ToLongFunction<? super VIEW_TYPE> pureImpl) {
        return switch (forwardingType) {
            case PURE, SHALLOW -> pureImpl.applyAsLong(asViewType());
            case MINIMAL ->
                throw new InvalidForwardingContextException("Missing override");
        };
    }

    /**
     * <p>Perform a long-returning operation on the delegate, or use the
     * specified "shallow" fallback.</p>
     * <p>This method passes the delegate, typed as {@code VIEW_TYPE}, to the
     * specified "pure" implementation only if the forwarding type is
     * {@link ForwardingType#PURE}; otherwise, it executes the "shallow"
     * implementation.</p>
     * @param pureImpl operation to perform on the delegate
     * @param shallowImpl operation to perform when not forwarding to the
     *                    delegate
     * @return long value returned by operation
     */
    public long longOp(ToLongFunction<? super VIEW_TYPE> pureImpl,
                       LongSupplier shallowImpl) {
        return switch (forwardingType) {
            case PURE, SHALLOW -> pureImpl.applyAsLong(asViewType());
            case MINIMAL ->
                throw new InvalidForwardingContextException("Missing override");
        };
    }
}
