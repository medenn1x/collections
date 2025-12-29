package net.arinoru.util;

import net.arinoru.annotation.Unstable;

import java.util.Collection;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.function.DoubleConsumer;
import java.util.function.DoublePredicate;
import java.util.stream.DoubleStream;
import java.util.stream.StreamSupport;

@Unstable
public interface DoubleSet extends PrimitiveSet<Double,double[], DoubleConsumer,
        DoublePredicate, PrimitiveIterator.OfDouble, Spliterator.OfDouble, DoubleStream,
        DoubleCollection>, DoubleCollection {
    default boolean add(Double element) {
        return DoubleCollection.super.add(element);
    }
    default boolean addAll(Collection<? extends Double> collection) {
        return DoubleCollection.super.addAll(collection);
    }
    default boolean addAll(DoubleCollection collection) {
        return DoubleCollection.super.addAll(collection);
    }
    default boolean addDouble(double element) {
        return DoubleCollection.super.addDouble(element);
    }
    default void clear() {
        DoubleCollection.super.clear();
    }
    default boolean contains(Object o) {
        return DoubleCollection.super.contains(o);
    }
    default boolean containsAll(Collection<?> collection) {
        return DoubleCollection.super.containsAll(collection);
    }
    default boolean containsAll(DoubleCollection collection) {
        return DoubleCollection.super.containsAll(collection);
    }
    default boolean containsDouble(double element) {
        return DoubleCollection.super.containsDouble(element);
    }
    static DoubleSet copyOf(DoubleCollection collection) {
        return PrimitiveCollections.setOf(collection);
    }
    PrimitiveIterator.OfDouble iterator();
    static DoubleSet of() {
        return PrimitiveCollections.emptyDoubleSet();
    }
    static DoubleSet of(double e) {
        return PrimitiveCollections.singleton(e);
    }
    static DoubleSet of(double... elements) {
        return PrimitiveCollections.setOf(elements);
    }
    default DoubleStream parallelPrimitiveStream() {
        return StreamSupport.doubleStream(spliterator(), true);
    }
    default DoubleStream primitiveStream() {
        return StreamSupport.doubleStream(spliterator(), false);
    }
    default boolean remove(Object o) {
        return DoubleCollection.super.remove(o);
    }
    default boolean removeAll(Collection<?> collection) {
        return DoubleCollection.super.removeAll(collection);
    }
    default boolean removeAll(DoubleCollection collection) {
        return DoubleCollection.super.removeAll(collection);
    }
    default boolean removeDouble(double element) {
        return DoubleCollection.super.removeDouble(element);
    }
    default boolean retainAll(Collection<?> collection) {
        return DoubleCollection.super.retainAll(collection);
    }
    default boolean retainAll(DoubleCollection collection) {
        return DoubleCollection.super.retainAll(collection);
    }
    default Spliterator.OfDouble spliterator() {
        return PrimitiveCollections.doubleSpliterator(this, Spliterator.DISTINCT);
    }
    default double[] toPrimitiveArray() {
        return DoubleCollection.super.toPrimitiveArray();
    }
    static DoubleSet unmodifiableSet(DoubleSet set) {
        return PrimitiveCollections.unmodifiableSet(set);
    }
}
