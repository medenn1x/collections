package net.arinoru.util.views;

import net.arinoru.util.DoubleCollection;

import java.util.Collection;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.function.DoubleConsumer;
import java.util.function.DoublePredicate;
import java.util.function.Predicate;
import java.util.stream.DoubleStream;

public abstract class AbstractDoubleCollectionView
        extends AbstractPrimitiveCollectionView<Double,double[],DoubleConsumer,
        DoublePredicate,PrimitiveIterator.OfDouble,Spliterator.OfDouble,DoubleStream,
        DoubleCollection> implements DoubleCollection {
    @Override
    public boolean add(Double element) {
        checkModifiable();
        return forwarder().predicateOp(delegate -> delegate.add(element),
                () -> DoubleCollection.super.add(element));
    }

    @Override
    public boolean addAll(Collection<? extends Double> collection) {
        checkModifiable();
        return forwarder().predicateOp(delegate ->
                        delegate.addAll(collection),
                () -> DoubleCollection.super.addAll(collection));
    }

    @Override
    public boolean addAll(DoubleCollection collection) {
        checkModifiable();
        return forwarder().predicateOp(delegate ->
                        delegate.addAll(collection),
                () -> DoubleCollection.super.addAll(collection));
    }

    @Override
    public boolean addDouble(double element) {
        checkModifiable();
        return forwarder().predicateOp(delegate ->
                        delegate.addDouble(element),
                () -> DoubleCollection.super.addDouble(element));
    }

    @Override
    public void clear() {
        checkModifiable();
        forwarder().voidOp(DoubleCollection::clear, DoubleCollection.super::clear);
    }

    @Override
    public boolean contains(Object o) {
        return forwarder().predicateOp(delegate -> delegate.contains(o),
                () -> DoubleCollection.super.contains(o));
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return forwarder().predicateOp(delegate ->
                        delegate.containsAll(collection),
                () -> DoubleCollection.super.containsAll(collection));
    }

    @Override
    public boolean containsAll(DoubleCollection collection) {
        return forwarder().predicateOp(delegate ->
                        delegate.containsAll(collection),
                () -> DoubleCollection.super.containsAll(collection));
    }

    @Override
    public boolean containsDouble(double element) {
        return forwarder().predicateOp(delegate ->
                        delegate.containsDouble(element),
                () -> DoubleCollection.super.containsDouble(element));
    }

    @Override
    public PrimitiveIterator.OfDouble iterator() {
        return forwarder().maskIfNeeded(forwarder().boxedOp(DoubleCollection::iterator),
                DoubleIteratorView::unmodifiable);
    }

    @Override
    public DoubleStream parallelPrimitiveStream() {
        return forwarder().boxedOp(DoubleCollection::parallelPrimitiveStream,
                DoubleCollection.super::parallelPrimitiveStream);
    }

    @Override
    public DoubleStream primitiveStream() {
        return forwarder().boxedOp(DoubleCollection::primitiveStream,
                DoubleCollection.super::primitiveStream);
    }

    @Override
    public boolean remove(Object o) {
        checkModifiable();
        return forwarder().predicateOp(delegate -> delegate.remove(o),
                () -> DoubleCollection.super.remove(o));
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        checkModifiable();
        return forwarder().predicateOp(delegate ->
                        delegate.removeAll(collection),
                () -> DoubleCollection.super.removeAll(collection));
    }

    @Override
    public boolean removeAll(DoubleCollection collection) {
        checkModifiable();
        return forwarder().predicateOp(delegate ->
                        delegate.removeAll(collection),
                () -> DoubleCollection.super.removeAll(collection));
    }

    @Override
    public boolean removeDouble(double element) {
        checkModifiable();
        return forwarder().predicateOp(delegate ->
                        delegate.removeDouble(element),
                () -> DoubleCollection.super.removeDouble(element));
    }

    @Override
    public boolean removeIf(DoublePredicate filter) {
        checkModifiable();
        return forwarder().predicateOp(delegate ->
                        delegate.removeIf(filter),
                () -> DoubleCollection.super.removeIf(filter));
    }

    @Override
    public boolean removeIf(Predicate<? super Double> filter) {
        checkModifiable();
        return forwarder().predicateOp(delegate ->
                        delegate.removeIf(filter),
                () -> DoubleCollection.super.removeIf(filter));
    }

    @Override
    public boolean removeIfDouble(DoublePredicate filter) {
        checkModifiable();
        return forwarder().predicateOp(delegate ->
                        delegate.removeIfDouble(filter),
                () -> DoubleCollection.super.removeIfDouble(filter));
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        checkModifiable();
        return forwarder().predicateOp(delegate ->
                        delegate.retainAll(collection),
                () -> DoubleCollection.super.retainAll(collection));
    }

    @Override
    public boolean retainAll(DoubleCollection collection) {
        checkModifiable();
        return forwarder().predicateOp(delegate ->
                        delegate.retainAll(collection),
                () -> DoubleCollection.super.retainAll(collection));
    }

    @Override
    public Spliterator.OfDouble spliterator() {
        return forwarder().boxedOp(DoubleCollection::spliterator,
                DoubleCollection.super::spliterator);
    }

    @Override
    public double[] toPrimitiveArray() {
        return forwarder().boxedOp(DoubleCollection::toPrimitiveArray,
                DoubleCollection.super::toPrimitiveArray);
    }
}
