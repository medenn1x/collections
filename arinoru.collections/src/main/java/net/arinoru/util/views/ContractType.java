package net.arinoru.util.views;

import java.io.Serializable;

public enum ContractType {
    STANDARD, SERIALIZABLE, UNMODIFIABLE, SERIALIZABLE_AND_UNMODIFIABLE;

    static ContractType of(Object o) {
        return switch (o) {
            case Serializable ignored ->
                    o instanceof UnmodifiableView ?
                            SERIALIZABLE_AND_UNMODIFIABLE :
                            SERIALIZABLE;
            case UnmodifiableView ignored -> UNMODIFIABLE;
            default -> STANDARD;
        };
    }
}
