package net.arinoru.util.views;

import java.util.function.BiFunction;

class ViewImpl {
    private ViewImpl() {}

    static <T> T unmodifiable(
            T t,
            BiFunction<T,ForwardingType,T> unmodifiableWrapper,
            BiFunction<T,ForwardingType,T> serializableUnmodifiableWrapper) {
        return switch (ContractType.of(t)) {
            case STANDARD -> unmodifiableWrapper.apply(t, ForwardingType.PURE);
            case SERIALIZABLE -> serializableUnmodifiableWrapper.apply(t,
                    ForwardingType.PURE);
            case UNMODIFIABLE, SERIALIZABLE_AND_UNMODIFIABLE -> t;
        };
    }
}
