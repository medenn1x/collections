package net.arinoru.function;

import net.arinoru.prerelease.PrereleaseContent;

@FunctionalInterface
@PrereleaseContent
public interface TriFunction<T,U,V,R> {
    R apply(T t, U u, V v);
}
