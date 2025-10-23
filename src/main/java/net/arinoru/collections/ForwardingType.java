package net.arinoru.collections;

import net.arinoru.prerelease.PrereleaseContent;

@PrereleaseContent
enum ForwardingType {
    /**
     * <p>Denotes "pure" forwarding (all known methods are forwarded unless overridden).</p>
     * <p>This asserts that the composed class has an element type consistent with
     * the {@code PURE} type for the forwarding class, and that if
     * the forwarding class is a primitive-handling class, then the composed class
     * is also a primitive-handling class of the same type. Failure to adhere to this
     * contract may result in unexpected {@link ClassCastException}s.</p>
     */
    PURE,
    /**
     * <p>Denotes "shallow" forwarding (default methods are used when available)</p>
     * <p>This carries the same type assertions as PURE.</p>
     */
    SHALLOW,
    /**
     * <p>Denotes "minimal" forwarding (only methods insensitive to type are forwarded)</p>
     * <p>This puts no type requirements on the composed class beyond the base type required
     * by the forwarding class constructor; however, any methods
     * dealing with elements will throw IllegalStateException unless overridden.</p>
     */
    MINIMAL
}
