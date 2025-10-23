package net.arinoru.collections;

import net.arinoru.function.TriFunction;
import net.arinoru.prerelease.PrereleaseContent;

import java.util.Objects;

@PrereleaseContent
abstract class AbstractForwardingClass<T,PURE extends T,F extends AbstractForwardingClass<T, PURE,F>> {
    protected final T composedClass;
    protected final ForwardingType forwardingType;
    protected final boolean unmodifiable;
    protected final boolean needsMask;
    protected final TriFunction<? super T,ForwardingType,MaskingType,? extends F> constructor;

    AbstractForwardingClass(
            T composedClass,
            ForwardingType forwardingType,
            MaskingType maskingType,
            TriFunction<? super T,ForwardingType,MaskingType,? extends F> constructor
    ) {
        this.composedClass = Objects.requireNonNull(composedClass);
        this.forwardingType = Objects.requireNonNull(forwardingType);
        this.constructor = Objects.requireNonNull(constructor);
        if (composedClass instanceof AbstractForwardingClass<?,?,?> f) {
            unmodifiable = maskingType == MaskingType.UNMODIFIABLE || f.unmodifiable;
            needsMask = maskingType == MaskingType.UNMODIFIABLE && !f.unmodifiable;
        } else {
            unmodifiable = needsMask = maskingType == MaskingType.UNMODIFIABLE;
        }
    }

    protected void checkNotMinimal() {
        if (forwardingType == ForwardingType.MINIMAL)
            throw new IllegalStateException();
    }

    protected void checkNotUnmodifiable() {
        if (unmodifiable)
            throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    protected PURE pure() {
        return switch (forwardingType) {
            case PURE, SHALLOW -> (PURE) composedClass;
            case MINIMAL -> throw new ClassCastException("Internal error");
        };
    }

    @SuppressWarnings("unchecked")
    protected PURE mask() {
        return (PURE) (needsMask ? constructor.apply(composedClass, ForwardingType.PURE,
                MaskingType.UNMODIFIABLE) : composedClass);
    }
}
