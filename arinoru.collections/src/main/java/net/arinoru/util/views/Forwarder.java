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

public class Forwarder<DELEGATE_TYPE, VIEW_TYPE extends DELEGATE_TYPE>
        implements Serializable {
    private final DELEGATE_TYPE delegate;
    private final ForwardingType forwardingType;
    private final boolean subViewsRequireMasking;

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

    public DELEGATE_TYPE asDelegateType() {
        return delegate;
    }

    @SuppressWarnings("unchecked")
    public VIEW_TYPE asViewType() {
        return switch (forwardingType) {
            case PURE, SHALLOW -> (VIEW_TYPE) delegate;
            case MINIMAL ->
                    throw new InvalidForwardingContextException("Internal error");
        };
    }

    public <T> T maskIfNeeded(T t, UnaryOperator<T> factory) {
        if (t instanceof UnmodifiableView || !subViewsRequireMasking)
            return t;
        return factory.apply(t);
    }

    public void voidOp(Consumer<? super VIEW_TYPE> pureImpl) {
        switch (forwardingType) {
            case PURE, SHALLOW -> pureImpl.accept(asViewType());
            case MINIMAL ->
                    throw new InvalidForwardingContextException("Missing override");
        }
    }

    public void voidOp(Consumer<? super VIEW_TYPE> pureImpl, Runnable shallowImpl) {
        switch (forwardingType) {
            case PURE -> pureImpl.accept(asViewType());
            case SHALLOW, MINIMAL -> shallowImpl.run();
        }
    }

    public void voidOpOrThrow(Consumer<? super VIEW_TYPE> pureImpl,
                              Supplier<? extends RuntimeException> ifMinimal) {
        switch (forwardingType) {
            case PURE, SHALLOW -> pureImpl.accept(asViewType());
            case MINIMAL -> throw ifMinimal.get();
        }
    }

    public void voidOpOrThrow(Consumer<? super VIEW_TYPE> pureImpl, Runnable shallowImpl,
                              Supplier<? extends RuntimeException> ifMinimal) {
        switch (forwardingType) {
            case PURE -> pureImpl.accept(asViewType());
            case SHALLOW -> shallowImpl.run();
            case MINIMAL -> throw ifMinimal.get();
        }
    }

    public boolean predicateOp(Predicate<? super VIEW_TYPE> pureImpl) {
        return switch (forwardingType) {
            case PURE, SHALLOW -> pureImpl.test(asViewType());
            case MINIMAL ->
                    throw new InvalidForwardingContextException("Missing override");
        };
    }

    public boolean predicateOp(Predicate<? super VIEW_TYPE> pureImpl,
                               BooleanSupplier shallowImpl) {
        return switch (forwardingType) {
            case PURE -> pureImpl.test(asViewType());
            case SHALLOW, MINIMAL -> shallowImpl.getAsBoolean();
        };
    }

    public boolean predicateOpOrThrow(Predicate<? super VIEW_TYPE> pureImpl,
                                      Supplier<? extends RuntimeException> ifMinimal) {
        return switch (forwardingType) {
            case PURE, SHALLOW -> pureImpl.test(asViewType());
            case MINIMAL -> throw ifMinimal.get();
        };
    }

    public <R> R boxedOp(Function<? super VIEW_TYPE,R> pureImpl) {
        return switch (forwardingType) {
            case PURE, SHALLOW -> pureImpl.apply(asViewType());
            case MINIMAL ->
                    throw new InvalidForwardingContextException("Missing override");
        };
    }

    public <R> R boxedOp(Function<? super VIEW_TYPE,R> pureImpl,
                         Supplier<R> shallowImpl) {
        return switch (forwardingType) {
            case PURE -> pureImpl.apply(asViewType());
            case SHALLOW, MINIMAL -> shallowImpl.get();
        };
    }

    public <R> R boxedOpOrThrow(Function<? super VIEW_TYPE,R> pureImpl,
                                Supplier<R> shallowImpl,
                                Supplier<? extends RuntimeException> ifMinimal) {
        return switch (forwardingType) {
            case PURE -> pureImpl.apply(asViewType());
            case SHALLOW -> shallowImpl.get();
            case MINIMAL -> throw ifMinimal.get();
        };
    }

    public double doubleOp(ToDoubleFunction<? super VIEW_TYPE> pureImpl) {
        return switch (forwardingType) {
            case PURE, SHALLOW -> pureImpl.applyAsDouble(asViewType());
            case MINIMAL ->
                    throw new InvalidForwardingContextException("Missing override");
        };
    }

    public double doubleOp(ToDoubleFunction<? super VIEW_TYPE> pureImpl,
                           DoubleSupplier shallowImpl) {
        return switch (forwardingType) {
            case PURE -> pureImpl.applyAsDouble(asViewType());
            case SHALLOW, MINIMAL -> shallowImpl.getAsDouble();
        };
    }

    public int intOp(ToIntFunction<? super VIEW_TYPE> pureImpl) {
        return switch (forwardingType) {
            case PURE, SHALLOW -> pureImpl.applyAsInt(asViewType());
            case MINIMAL ->
                    throw new InvalidForwardingContextException("Missing override");
        };
    }

    public int intOp(ToIntFunction<? super VIEW_TYPE> pureImpl, IntSupplier shallowImpl) {
        return switch (forwardingType) {
            case PURE -> pureImpl.applyAsInt(asViewType());
            case SHALLOW, MINIMAL -> shallowImpl.getAsInt();
        };
    }

    public long longOp(ToLongFunction<? super VIEW_TYPE> pureImpl) {
        return switch (forwardingType) {
            case PURE, SHALLOW -> pureImpl.applyAsLong(asViewType());
            case MINIMAL ->
                throw new InvalidForwardingContextException("Missing override");
        };
    }

    public long longOp(ToLongFunction<? super VIEW_TYPE> pureImpl,
                       LongSupplier shallowImpl) {
        return switch (forwardingType) {
            case PURE, SHALLOW -> pureImpl.applyAsLong(asViewType());
            case MINIMAL ->
                throw new InvalidForwardingContextException("Missing override");
        };
    }
}
