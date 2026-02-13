package net.arinoru.function;

import net.arinoru.annotation.Unstable;

/**
 * <p>Represents a function that accepts an object-valued and a {@code int}-valued
 * argument and produces a result. This is the {@code (reference, int)}
 * specialization of {@link java.util.function.BiFunction}.</p>
 * <p>This is a <em>functional interface</em> whose functional method is
 * {@link #apply(Object, int)}.</p>
 * @param <T> the type of the object argument to the function
 * @param <R> the type of the result of the function
 * @see java.util.function.BiFunction
 */
@Unstable
@FunctionalInterface
public interface ObjIntFunction<T,R> {
    /**
     * <p>Applies this function to the given arguments.</p>
     * @param t the first input argument
     * @param value the second input argument
     * @return the function result
     */
    R apply(T t, int value);
}
