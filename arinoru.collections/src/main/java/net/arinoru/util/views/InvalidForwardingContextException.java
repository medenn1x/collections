package net.arinoru.util.views;

/**
 * <p>Exception thrown by views when an implementation has an uncovered context.
 * This typically means that a view has failed to implement an appropriate override
 * for a minimal forwarder, but it can also cover contexts where primitive type
 * extensions from outside this package lack a method delegation for said type
 * within the view.</p>
 */
public class InvalidForwardingContextException extends IllegalStateException {
    /**
     * <p>Create an invalid forwarding context exception.</p>
     */
    public InvalidForwardingContextException() {}

    /**
     * <p>Create an invalid forwarding context exception with the specified
     * message.</p>
     * @param message exception method
     */
    public InvalidForwardingContextException(String message) {
        super(message);
    }
}
