package net.arinoru.util.views;

import java.io.Serializable;

/**
 * <p>Enumerates the various types of view contracts. This provides an easy
 * way to perform case delegation for a series of implementing classes for
 * views based on the properties of the backing class.</p>
 */
public enum ContractType {
    /**
     * <p>Standard contract. This provides no special requirements or promises
     * for the view.</p>
     */
    STANDARD,
    /**
     * <p>Serializable contract. The view implements {@link Serializable} and
     * should be designed to ensure that it will be serializable so long as
     * its backing class is serializable.</p>
     */
    SERIALIZABLE,
    /**
     * <p>Unmodifiable contract. The view implements {@link UnmodifiableView}
     * and should be designed to ensure that it will not permit any operations
     * that modify the backing class.</p>
     */
    UNMODIFIABLE,
    /**
     * <p>Serializable and unmodifiable contract. The view implements both
     * {@link Serializable} and {@link UnmodifiableView} and should be
     * designed to ensure that it will be serializable so long as its backing
     * class is serializable, and it will not permit any operations that
     * modify the backing class.</p>
     */
    SERIALIZABLE_AND_UNMODIFIABLE;

    /**
     * <p>Returns the appropriate contract type for a view or backing class.
     * This method checks the runtime type of the specified object to
     * determine which interfaces required by contracts are implemented.</p>
     * @param o object to determine a contract type for
     * @return contract type for the specified view or backing class
     */
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
