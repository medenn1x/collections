package net.arinoru.collections;

import net.arinoru.prerelease.PrereleaseContent;

@PrereleaseContent
enum MaskingType {
    /**
     * Denotes that this wrapper should enforce immutability. Any methods which
     * return a view of the class will return an unmodifiable view if not
     * overridden; most forwarding classes will provide one or more
     * {@code maskIfNeeded} methods that overriding methods may use to ensure that
     * any backing class they expose is exposed only through an unmodifiable
     * view.
     */
    UNMODIFIABLE,
    /**
     * Denotes that this wrapper should inherit immutability from the backing
     * class if possible. If the backing class is not a view implemented
     * by a forwarding class, and is not implemented by a class known to be
     * unmodifiable, it will be assumed be mutable.
     */
    DELEGATE
}
