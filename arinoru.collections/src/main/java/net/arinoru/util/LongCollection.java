package net.arinoru.util;

import net.arinoru.annotation.Unstable;

import java.util.Collection;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.function.LongConsumer;
import java.util.function.LongPredicate;
import java.util.function.Predicate;
import java.util.stream.LongStream;
import java.util.stream.StreamSupport;

@Unstable
public interface LongCollection extends PrimitiveCollection<Long,long[],LongConsumer,
        LongPredicate, PrimitiveIterator.OfLong,Spliterator.OfLong,LongStream,
        LongCollection> {
    default boolean add(Long element) {
        return addLong(element);
    }
    default boolean addAll(Collection<? extends Long> collection) {
        if (collection instanceof LongCollection longs)
            return addAll(longs);
        var changed = false;
        for (Long e : collection)
            changed |= addLong(e);
        return changed;
    }
    default boolean addAll(LongCollection collection) {
        var changed = false;
        var iterator = collection.iterator();
        while (iterator.hasNext())
            changed |= addLong(iterator.nextLong());
        return changed;
    }
    default boolean addLong(long element) {
        throw new UnsupportedOperationException();
    }
    default void clear() {
        var iterator = iterator();
        while (iterator.hasNext()) {
            iterator.nextLong();
            iterator.remove();
        }
    }
    default boolean contains(Object o) {
        return o instanceof Long l && containsLong(l);
    }
    default boolean containsAll(Collection<?> collection) {
        if (collection instanceof LongCollection longs)
            return containsAll(longs);
        return collection.parallelStream().allMatch(this::contains);
    }
    default boolean containsAll(LongCollection collection) {
        return collection.parallelPrimitiveStream().allMatch(this::containsLong);
    }
    default boolean containsLong(long element) {
        return parallelPrimitiveStream().anyMatch(l -> l == element);
    }
    PrimitiveIterator.OfLong iterator();
    default LongStream parallelPrimitiveStream() {
        return StreamSupport.longStream(spliterator(), true);
    }
    default LongStream primitiveStream() {
        return StreamSupport.longStream(spliterator(), false);
    }
    default boolean remove(Object o) {
        return o instanceof Long l && removeLong(l);
    }
    default boolean removeAll(Collection<?> collection) {
        if (collection instanceof LongCollection longs)
            return removeAll(longs);
        return removeIfLong(collection::contains);
    }
    default boolean removeAll(LongCollection collection) {
        return removeIfLong(collection::containsLong);
    }
    default boolean removeIf(LongPredicate filter) {
        return removeIfLong(filter);
    }
    default boolean removeIf(Predicate<? super Long> filter) {
        return removeIfLong(filter::test);
    }
    default boolean removeIfLong(LongPredicate filter) {
        var changed = false;
        var iterator = iterator();
        while (iterator.hasNext())
            if (filter.test(iterator.nextLong())) {
                iterator.remove();
                changed = true;
            }
        return changed;
    }
    default boolean removeLong(long element) {
        var iterator = iterator();
        while (iterator.hasNext())
            if (iterator.nextLong() == element) {
                iterator.remove();
                return true;
            }
        return false;
    }
    default boolean retainAll(Collection<?> collection) {
        if (collection instanceof LongCollection longs)
            return retainAll(longs);
        return removeIfLong(((LongPredicate) collection::contains).negate());
    }
    default boolean retainAll(LongCollection collection) {
        return removeIfLong(((LongPredicate) collection::containsLong).negate());
    }
    default Spliterator.OfLong spliterator() {
        return PrimitiveCollections.longSpliterator(this, 0);
    }
    default long[] toPrimitiveArray() {
        return PrimitiveCollections.toPrimitiveArray(this);
    }
    static LongCollection unmodifiableCollection(LongCollection collection) {
        return PrimitiveCollections.unmodifiableCollection(collection);
    }
}
