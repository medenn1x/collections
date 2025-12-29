package net.arinoru.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Marks an unstable class or interface. Any such class or interface is not considered
 * to be part of a stable API, but serves as a "preview" of the unfinalized API.
 * Such classes are used at the developer's own risk; no guarantee or either binary
 * or source compatibility exists between versions.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Unstable {
}
