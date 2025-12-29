package net.arinoru.util.views;

public class InvalidForwardingContextException extends IllegalStateException {
    public InvalidForwardingContextException() {}
    public InvalidForwardingContextException(String message) {
        super(message);
    }
}
