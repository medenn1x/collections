package net.arinoru.util;

import net.arinoru.annotation.Unstable;

import java.util.Collection;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.function.IntConsumer;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

@Unstable
public interface IntCollection extends PrimitiveCollection<Integer,int[],IntConsumer,
        IntPredicate,PrimitiveIterator.OfInt,Spliterator.OfInt,IntStream,IntCollection> {
    default boolean add(Integer element) {
        return addInt(element);
    }
    default boolean addAll(Collection<? extends Integer> collection) {
        if (collection instanceof IntCollection integers)
            return addAll(integers);
        var changed = false;
        for (Integer e : collection)
            changed |= addInt(e);
        return changed;
    }
    default boolean addAll(IntCollection collection) {
        var changed = false;
        var iterator = collection.iterator();
        while (iterator.hasNext())
            changed |= addInt(iterator.nextInt());
        return changed;
    }
    default boolean addInt(int element) {
        throw new UnsupportedOperationException();
    }
    default void clear() {
        var iterator = iterator();
        while (iterator.hasNext()) {
            iterator.nextInt();
            iterator.remove();
        }
    }
    default boolean contains(Object o) {
        return o instanceof Integer i && containsInt(i);
    }
    default boolean containsAll(Collection<?> collection) {
        if (collection instanceof IntCollection integers)
            return containsAll(integers);
        return collection.parallelStream().allMatch(this::contains);
    }
    default boolean containsAll(IntCollection collection) {
        return collection.parallelPrimitiveStream().allMatch(this::containsInt);
    }
    default boolean containsInt(int element) {
        return parallelPrimitiveStream().anyMatch(i -> i == element);
    }
    PrimitiveIterator.OfInt iterator();
    default IntStream parallelPrimitiveStream() {
        return StreamSupport.intStream(spliterator(), true);
    }
    default IntStream primitiveStream() {
        return StreamSupport.intStream(spliterator(), false);
    }
    default boolean remove(Object o) {
        return o instanceof Integer i && removeInt(i);
    }
    default boolean removeAll(Collection<?> collection) {
        if (collection instanceof IntCollection integers)
            return removeAll(integers);
        return removeIfInt(collection::contains);
    }
    default boolean removeAll(IntCollection collection) {
        return removeIfInt(collection::containsInt);
    }
    default boolean removeIf(IntPredicate filter) {
        return removeIfInt(filter);
    }
    default boolean removeIf(Predicate<? super Integer> filter) {
        return removeIfInt(filter::test);
    }
    default boolean removeIfInt(IntPredicate filter) {
        var changed = false;
        var iterator = iterator();
        while (iterator.hasNext())
            if (filter.test(iterator.nextInt())) {
                iterator.remove();
                changed = true;
            }
        return changed;
    }
    default boolean removeInt(int element) {
        var iterator = iterator();
        while (iterator.hasNext())
            if (iterator.nextInt() == element) {
                iterator.remove();
                return true;
            }
        return false;
    }
    default boolean retainAll(Collection<?> collection) {
        if (collection instanceof IntCollection integers)
            return retainAll(integers);
        return removeIfInt(((IntPredicate) collection::contains).negate());
    }
    default boolean retainAll(IntCollection collection) {
        return removeIfInt(((IntPredicate) collection::containsInt).negate());
    }
    default Spliterator.OfInt spliterator() {
        return PrimitiveCollections.intSpliterator(this, 0);
    }
    default int[] toPrimitiveArray() {
        return PrimitiveCollections.toPrimitiveArray(this);
    }
    static IntCollection unmodifiableCollection(IntCollection collection) {
        return PrimitiveCollections.unmodifiableCollection(collection);
    }
}
