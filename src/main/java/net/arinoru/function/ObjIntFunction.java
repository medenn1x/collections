package net.arinoru.function;

import net.arinoru.prerelease.PrereleaseContent;

@PrereleaseContent
@FunctionalInterface
public interface ObjIntFunction<T,R> {
    R apply(T obj, int i);
}
