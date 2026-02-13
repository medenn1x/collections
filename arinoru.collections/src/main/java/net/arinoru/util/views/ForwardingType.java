package net.arinoru.util.views;

/**
 * <p>Forwarding level supported by this view. This determines the "default"
 * behavior of forwarding methods when not overridden.</p>
 */
public enum ForwardingType {
    /**
     * <p>Denotes "pure" forwarding (all known methods are forwarded unless
     * overridden).</p>
     * <p>This asserts that the composed class has an element type consistent with
     * the {@code VIEW_TYPE} type for the forwarding class, and that if the
     * forwarding class is a primitive-handling class, then the composed class is
     * also a primitive-handling class of the same type. Failure to adhere to this
     * contract may result in unexpected {@link ClassCastException}s.</p>
     */
    PURE,
    /**
     * <p>Denotes "shallow" forwarding (default methods are used when
     * available).</p>
     * <p>This carries the same type assertions as {@link #PURE}.</p>
     */
    SHALLOW,
    /**
     * <p>Denotes "minimal" forwarding (only methods insensitive to type are
     * forwarded).</p>
     * <p>This puts no type requirements on the composed class beyond the base
     * type required by the forwarding class constructor; however, any methods
     * dealing with elements will throw {@link IllegalStateException} unless
     * overridden.</p>
     * <p>Views with this forwarding type must override all methods without a
     * specified default behavior (e.g. a default implementation of an interface
     * method or a canonical reference behavior required by specification).
     * Such methods will throw an {@link InvalidForwardingContextException} if
     * they have not been overridden, unless otherwise specified by the view.</p>
     */
    MINIMAL
}
