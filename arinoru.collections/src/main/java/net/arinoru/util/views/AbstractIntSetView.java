package net.arinoru.util.views;

import net.arinoru.util.IntCollection;
import net.arinoru.util.IntSet;

import java.util.Collection;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.function.IntConsumer;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.stream.IntStream;

public abstract class AbstractIntSetView extends AbstractPrimitiveSetView<Integer,int[],
        IntConsumer,IntPredicate,PrimitiveIterator.OfInt,Spliterator.OfInt,IntStream,
        IntCollection,IntSet> implements IntSet {
    @Override
    public boolean add(Integer element) {
        checkModifiable();
        return forwarder().predicateOp(delegate -> delegate.add(element),
                () -> IntSet.super.add(element));
    }

    @Override
    public boolean addAll(Collection<? extends Integer> collection) {
        checkModifiable();
        return forwarder().predicateOp(delegate -> delegate.addAll(collection),
                () -> IntSet.super.addAll(collection));
    }

    @Override
    public boolean addAll(IntCollection collection) {
        checkModifiable();
        return forwarder().predicateOp(delegate -> delegate.addAll(collection),
                () -> IntSet.super.addAll(collection));
    }

    @Override
    public boolean addInt(int element) {
        checkModifiable();
        return forwarder().predicateOp(delegate -> delegate.addInt(element),
                () -> IntSet.super.addInt(element));
    }

    @Override
    public void clear() {
        checkModifiable();
        forwarder().voidOp(IntSet::clear, IntSet.super::clear);
    }

    @Override
    public boolean contains(Object o) {
        return forwarder().predicateOp(delegate -> delegate.contains(o),
                () -> IntSet.super.contains(o));
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return forwarder().predicateOp(delegate -> delegate.containsAll(collection),
                () -> IntSet.super.containsAll(collection));
    }

    @Override
    public boolean containsAll(IntCollection collection) {
        return forwarder().predicateOp(delegate -> delegate.containsAll(collection),
                () -> IntSet.super.containsAll(collection));
    }

    @Override
    public boolean containsInt(int element) {
        return forwarder().predicateOp(delegate -> delegate.containsInt(element),
                () -> IntSet.super.containsInt(element));
    }

    @Override
    public PrimitiveIterator.OfInt iterator() {
        return forwarder().maskIfNeeded(forwarder().boxedOp(IntSet::iterator),
                IntIteratorView::unmodifiable);
    }

    @Override
    public IntStream parallelPrimitiveStream() {
        return forwarder().boxedOp(IntSet::parallelPrimitiveStream,
                IntSet.super::parallelPrimitiveStream);
    }

    @Override
    public IntStream primitiveStream() {
        return forwarder().boxedOp(IntSet::primitiveStream,
                IntSet.super::primitiveStream);
    }

    @Override
    public boolean remove(Object o) {
        checkModifiable();
        return forwarder().predicateOp(delegate -> delegate.remove(o),
                () -> IntSet.super.remove(o));
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        checkModifiable();
        return forwarder().predicateOp(delegate -> delegate.removeAll(collection),
                () -> IntSet.super.removeAll(collection));
    }

    @Override
    public boolean removeAll(IntCollection collection) {
        checkModifiable();
        return forwarder().predicateOp(delegate -> delegate.removeAll(collection),
                () -> IntSet.super.removeAll(collection));
    }

    @Override
    public boolean removeIf(IntPredicate filter) {
        checkModifiable();
        return forwarder().predicateOp(delegate -> delegate.removeIf(filter),
                () -> IntSet.super.removeIf(filter));
    }

    @Override
    public boolean removeIf(Predicate<? super Integer> filter) {
        checkModifiable();
        return forwarder().predicateOp(delegate -> delegate.removeIf(filter),
                () -> IntSet.super.removeIf(filter));
    }

    @Override
    public boolean removeIfInt(IntPredicate filter) {
        checkModifiable();
        return forwarder().predicateOp(delegate -> delegate.removeIfInt(filter),
                () -> IntSet.super.removeIfInt(filter));
    }

    @Override
    public boolean removeInt(int element) {
        checkModifiable();
        return forwarder().predicateOp(delegate -> delegate.removeInt(element),
                () -> IntSet.super.removeInt(element));
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        checkModifiable();
        return forwarder().predicateOp(delegate -> delegate.retainAll(collection),
                () -> IntSet.super.retainAll(collection));
    }

    @Override
    public boolean retainAll(IntCollection collection) {
        checkModifiable();
        return forwarder().predicateOp(delegate -> delegate.retainAll(collection),
                () -> IntSet.super.retainAll(collection));
    }

    @Override
    public Spliterator.OfInt spliterator() {
        return forwarder().boxedOp(IntSet::spliterator, IntSet.super::spliterator);
    }

    @Override
    public int[] toPrimitiveArray() {
        return forwarder().boxedOp(IntSet::toPrimitiveArray,
                IntSet.super::toPrimitiveArray);
    }
}
