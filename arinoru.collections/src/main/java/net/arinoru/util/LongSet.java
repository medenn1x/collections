package net.arinoru.util;

import net.arinoru.annotation.Unstable;

import java.util.Collection;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.function.LongConsumer;
import java.util.function.LongPredicate;
import java.util.stream.LongStream;
import java.util.stream.StreamSupport;

@Unstable
public interface LongSet extends PrimitiveSet<Long,long[],LongConsumer,LongPredicate,
        PrimitiveIterator.OfLong,Spliterator.OfLong,LongStream,LongCollection>,
        LongCollection {
    default boolean add(Long element) {
        return LongCollection.super.add(element);
    }
    default boolean addAll(Collection<? extends Long> collection) {
        return LongCollection.super.addAll(collection);
    }
    default boolean addAll(LongCollection collection) {
        return LongCollection.super.addAll(collection);
    }
    default boolean addLong(long element) {
        return LongCollection.super.addLong(element);
    }
    default void clear() {
        LongCollection.super.clear();
    }
    default boolean contains(Object o) {
        return LongCollection.super.contains(o);
    }
    default boolean containsAll(Collection<?> collection) {
        return LongCollection.super.containsAll(collection);
    }
    default boolean containsAll(LongCollection collection) {
        return LongCollection.super.containsAll(collection);
    }
    default boolean containsLong(long element) {
        return LongCollection.super.containsLong(element);
    }
    static LongSet copyOf(LongCollection collection) {
        return PrimitiveCollections.setOf(collection);
    }
    PrimitiveIterator.OfLong iterator();
    static LongSet of() {
        return PrimitiveCollections.emptyLongSet();
    }
    static LongSet of(long e) {
        return PrimitiveCollections.singleton(e);
    }
    static LongSet of(long... elements) {
        return PrimitiveCollections.setOf(elements);
    }
    default LongStream parallelPrimitiveStream() {
        return StreamSupport.longStream(spliterator(), true);
    }
    default LongStream primitiveStream() {
        return StreamSupport.longStream(spliterator(), false);
    }
    default boolean remove(Object o) {
        return LongCollection.super.remove(o);
    }
    default boolean removeAll(Collection<?> collection) {
        return LongCollection.super.removeAll(collection);
    }
    default boolean removeAll(LongCollection collection) {
        return LongCollection.super.removeAll(collection);
    }
    default boolean removeLong(long element) {
        return LongCollection.super.removeLong(element);
    }
    default boolean retainAll(Collection<?> collection) {
        return LongCollection.super.retainAll(collection);
    }
    default boolean retainAll(LongCollection collection) {
        return LongCollection.super.retainAll(collection);
    }
    default Spliterator.OfLong spliterator() {
        return PrimitiveCollections.longSpliterator(this, Spliterator.DISTINCT);
    }
    default long[] toPrimitiveArray() {
        return LongCollection.super.toPrimitiveArray();
    }
    static LongSet unmodifiableSet(LongSet set) {
        return PrimitiveCollections.unmodifiableSet(set);
    }
}
