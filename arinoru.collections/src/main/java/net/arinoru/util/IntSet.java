package net.arinoru.util;

import net.arinoru.annotation.Unstable;

import java.util.Collection;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.function.IntConsumer;
import java.util.function.IntPredicate;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

@Unstable
public interface IntSet extends PrimitiveSet<Integer,int[], IntConsumer,
        IntPredicate, PrimitiveIterator.OfInt, Spliterator.OfInt, IntStream,
        IntCollection>, IntCollection {
    default boolean add(Integer element) {
        return IntCollection.super.add(element);
    }
    default boolean addAll(Collection<? extends Integer> collection) {
        return IntCollection.super.addAll(collection);
    }
    default boolean addAll(IntCollection collection) {
        return IntCollection.super.addAll(collection);
    }
    default boolean addInt(int element) {
        return IntCollection.super.addInt(element);
    }
    default void clear() {
        IntCollection.super.clear();
    }
    default boolean contains(Object o) {
        return IntCollection.super.contains(o);
    }
    default boolean containsAll(Collection<?> collection) {
        return IntCollection.super.containsAll(collection);
    }
    default boolean containsAll(IntCollection collection) {
        return IntCollection.super.containsAll(collection);
    }
    default boolean containsInt(int element) {
        return IntCollection.super.containsInt(element);
    }
    static IntSet copyOf(IntCollection collection) {
        return PrimitiveCollections.setOf(collection);
    }
    PrimitiveIterator.OfInt iterator();
    static IntSet of() {
        return PrimitiveCollections.emptyIntSet();
    }
    static IntSet of(int e) {
        return PrimitiveCollections.singleton(e);
    }
    static IntSet of(int... elements) {
        return PrimitiveCollections.setOf(elements);
    }
    default IntStream parallelPrimitiveStream() {
        return StreamSupport.intStream(spliterator(), true);
    }
    default IntStream primitiveStream() {
        return StreamSupport.intStream(spliterator(), false);
    }
    default boolean remove(Object o) {
        return IntCollection.super.remove(o);
    }
    default boolean removeAll(Collection<?> collection) {
        return IntCollection.super.removeAll(collection);
    }
    default boolean removeAll(IntCollection collection) {
        return IntCollection.super.removeAll(collection);
    }
    default boolean removeInt(int element) {
        return IntCollection.super.removeInt(element);
    }
    default boolean retainAll(Collection<?> collection) {
        return IntCollection.super.retainAll(collection);
    }
    default boolean retainAll(IntCollection collection) {
        return IntCollection.super.retainAll(collection);
    }
    default Spliterator.OfInt spliterator() {
        return PrimitiveCollections.intSpliterator(this, Spliterator.DISTINCT);
    }
    default int[] toPrimitiveArray() {
        return IntCollection.super.toPrimitiveArray();
    }
    static IntSet unmodifiableSet(IntSet set) {
        return PrimitiveCollections.unmodifiableSet(set);
    }
}
