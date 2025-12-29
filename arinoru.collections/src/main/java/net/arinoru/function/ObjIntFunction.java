package net.arinoru.function;

import net.arinoru.annotation.Unstable;

@Unstable
@FunctionalInterface
public interface ObjIntFunction<T,R> {
    R apply(T obj, int i);
}
