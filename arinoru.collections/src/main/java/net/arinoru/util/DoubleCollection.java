package net.arinoru.util;

import net.arinoru.annotation.Unstable;

import java.util.Collection;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.function.DoubleConsumer;
import java.util.function.DoublePredicate;
import java.util.function.Predicate;
import java.util.stream.DoubleStream;
import java.util.stream.StreamSupport;

@Unstable
public interface DoubleCollection extends PrimitiveCollection<Double,double[],
        DoubleConsumer,DoublePredicate,PrimitiveIterator.OfDouble,Spliterator.OfDouble,
        DoubleStream,DoubleCollection> {
    default boolean add(Double element) {
        return addDouble(element);
    }
    default boolean addAll(Collection<? extends Double> collection) {
        if (collection instanceof DoubleCollection doubles)
            return addAll(doubles);
        var changed = false;
        for (Double e : collection)
            changed |= addDouble(e);
        return changed;
    }
    default boolean addAll(DoubleCollection collection) {
        var changed = false;
        var iterator = collection.iterator();
        while (iterator.hasNext())
            changed |= addDouble(iterator.nextDouble());
        return changed;
    }
    default boolean addDouble(double element) {
        throw new UnsupportedOperationException();
    }
    default void clear() {
        var iterator = iterator();
        while (iterator.hasNext()) {
            iterator.nextDouble();
            iterator.remove();
        }
    }
    default boolean contains(Object o) {
        return o instanceof Double d && containsDouble(d);
    }
    default boolean containsAll(Collection<?> collection) {
        if (collection instanceof DoubleCollection doubles)
            return containsAll(doubles);
        return collection.parallelStream().allMatch(this::contains);
    }
    default boolean containsAll(DoubleCollection collection) {
        return collection.parallelPrimitiveStream().allMatch(this::containsDouble);
    }
    default boolean containsDouble(double element) {
        return parallelPrimitiveStream().anyMatch(d -> d == element);
    }
    PrimitiveIterator.OfDouble iterator();
    default DoubleStream parallelPrimitiveStream() {
        return StreamSupport.doubleStream(spliterator(), true);
    }
    default DoubleStream primitiveStream() {
        return StreamSupport.doubleStream(spliterator(), false);
    }
    default boolean remove(Object o) {
        return o instanceof Double d && removeDouble(d);
    }
    default boolean removeAll(Collection<?> collection) {
        if (collection instanceof DoubleCollection doubles)
            return removeAll(doubles);
        return removeIfDouble(collection::contains);
    }
    default boolean removeAll(DoubleCollection collection) {
        return removeIfDouble(collection::containsDouble);
    }
    default boolean removeDouble(double element) {
        var iterator = iterator();
        while (iterator.hasNext())
            if (iterator.nextDouble() == element) {
                iterator.remove();
                return true;
            }
        return false;
    }
    default boolean removeIf(DoublePredicate filter) {
        return removeIfDouble(filter);
    }
    default boolean removeIf(Predicate<? super Double> filter) {
        return removeIfDouble(filter::test);
    }
    default boolean removeIfDouble(DoublePredicate filter) {
        var changed = false;
        var iterator = iterator();
        while (iterator.hasNext())
            if (filter.test(iterator.nextDouble())) {
                iterator.remove();
                changed = true;
            }
        return changed;
    }
    default boolean retainAll(Collection<?> collection) {
        if (collection instanceof DoubleCollection doubles)
            return retainAll(doubles);
        return removeIfDouble(((DoublePredicate) collection::contains).negate());
    }
    default boolean retainAll(DoubleCollection collection) {
        return removeIfDouble(((DoublePredicate) collection::containsDouble).negate());
    }
    default Spliterator.OfDouble spliterator() {
        return PrimitiveCollections.doubleSpliterator(this, 0);
    }
    default double[] toPrimitiveArray() {
        return PrimitiveCollections.toPrimitiveArray(this);
    }
    static DoubleCollection unmodifiableCollection(DoubleCollection collection) {
        return PrimitiveCollections.unmodifiableCollection(collection);
    }
}
