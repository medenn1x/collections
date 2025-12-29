package net.arinoru.util.views;

import net.arinoru.util.DoubleCollection;
import net.arinoru.util.DoubleSet;

import java.util.Collection;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.function.DoubleConsumer;
import java.util.function.DoublePredicate;
import java.util.function.Predicate;
import java.util.stream.DoubleStream;

public abstract class AbstractDoubleSetView extends AbstractPrimitiveSetView<Double,
        double[],DoubleConsumer,DoublePredicate,PrimitiveIterator.OfDouble,
        Spliterator.OfDouble,DoubleStream,DoubleCollection,DoubleSet>
        implements DoubleSet {
    @Override
    public boolean add(Double element) {
        checkModifiable();
        return forwarder().predicateOp(delegate -> delegate.add(element),
                () -> DoubleSet.super.add(element));
    }

    @Override
    public boolean addAll(Collection<? extends Double> collection) {
        checkModifiable();
        return forwarder().predicateOp(delegate -> delegate.addAll(collection),
                () -> DoubleSet.super.addAll(collection));
    }

    @Override
    public boolean addAll(DoubleCollection collection) {
        checkModifiable();
        return forwarder().predicateOp(delegate -> delegate.addAll(collection),
                () -> DoubleSet.super.addAll(collection));
    }

    @Override
    public boolean addDouble(double element) {
        checkModifiable();
        return forwarder().predicateOp(delegate -> delegate.addDouble(element),
                () -> DoubleSet.super.addDouble(element));
    }

    @Override
    public void clear() {
        checkModifiable();
        forwarder().voidOp(DoubleSet::clear, DoubleSet.super::clear);
    }

    @Override
    public boolean contains(Object o) {
        return forwarder().predicateOp(delegate -> delegate.contains(o),
                () -> DoubleSet.super.contains(o));
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return forwarder().predicateOp(delegate ->
                        delegate.containsAll(collection),
                () -> DoubleSet.super.containsAll(collection));
    }

    @Override
    public boolean containsAll(DoubleCollection collection) {
        return forwarder().predicateOp(delegate ->
                        delegate.containsAll(collection),
                () -> DoubleSet.super.containsAll(collection));
    }

    @Override
    public boolean containsDouble(double element) {
        return forwarder().predicateOp(delegate ->
                        delegate.containsDouble(element),
                () -> DoubleSet.super.containsDouble(element));
    }

    @Override
    public PrimitiveIterator.OfDouble iterator() {
        return forwarder().maskIfNeeded(forwarder().boxedOp(DoubleSet::iterator),
                DoubleIteratorView::unmodifiable);
    }

    @Override
    public DoubleStream parallelPrimitiveStream() {
        return forwarder().boxedOp(DoubleSet::parallelPrimitiveStream,
                DoubleSet.super::parallelPrimitiveStream);
    }

    @Override
    public DoubleStream primitiveStream() {
        return forwarder().boxedOp(DoubleSet::primitiveStream,
                DoubleSet.super::primitiveStream);
    }

    @Override
    public boolean remove(Object o) {
        checkModifiable();
        return forwarder().predicateOp(delegate -> delegate.remove(o),
                () -> DoubleSet.super.remove(o));
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        checkModifiable();
        return forwarder().predicateOp(delegate ->
                        delegate.removeAll(collection),
                () -> DoubleSet.super.removeAll(collection));
    }

    @Override
    public boolean removeAll(DoubleCollection collection) {
        checkModifiable();
        return forwarder().predicateOp(delegate ->
                        delegate.removeAll(collection),
                () -> DoubleSet.super.removeAll(collection));
    }

    @Override
    public boolean removeDouble(double element) {
        checkModifiable();
        return forwarder().predicateOp(delegate ->
                        delegate.removeDouble(element),
                () -> DoubleSet.super.removeDouble(element));
    }

    @Override
    public boolean removeIf(DoublePredicate filter) {
        checkModifiable();
        return forwarder().predicateOp(delegate -> delegate.removeIf(filter),
                () -> DoubleSet.super.removeIf(filter));
    }

    @Override
    public boolean removeIf(Predicate<? super Double> filter) {
        checkModifiable();
        return forwarder().predicateOp(delegate -> delegate.removeIf(filter),
                () -> DoubleSet.super.removeIf(filter));
    }

    @Override
    public boolean removeIfDouble(DoublePredicate filter) {
        checkModifiable();
        return forwarder().predicateOp(delegate ->
                        delegate.removeIfDouble(filter),
                () -> DoubleSet.super.removeIfDouble(filter));
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        checkModifiable();
        return forwarder().predicateOp(delegate ->
                        delegate.retainAll(collection),
                () -> DoubleSet.super.retainAll(collection));
    }

    @Override
    public boolean retainAll(DoubleCollection collection) {
        checkModifiable();
        return forwarder().predicateOp(delegate ->
                        delegate.retainAll(collection),
                () -> DoubleSet.super.retainAll(collection));
    }

    @Override
    public Spliterator.OfDouble spliterator() {
        return forwarder().boxedOp(DoubleSet::spliterator,
                DoubleSet.super::spliterator);
    }

    @Override
    public double[] toPrimitiveArray() {
        return forwarder().boxedOp(DoubleSet::toPrimitiveArray,
                DoubleSet.super::toPrimitiveArray);
    }
}
