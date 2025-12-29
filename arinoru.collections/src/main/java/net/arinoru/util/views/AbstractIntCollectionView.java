package net.arinoru.util.views;

import net.arinoru.util.IntCollection;

import java.util.Collection;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.function.IntConsumer;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.stream.IntStream;

public abstract class AbstractIntCollectionView
        extends AbstractPrimitiveCollectionView<Integer,int[],IntConsumer,IntPredicate,
        PrimitiveIterator.OfInt,Spliterator.OfInt,IntStream,IntCollection>
        implements IntCollection {
    @Override
    public boolean add(Integer element) {
        checkModifiable();
        return forwarder().predicateOp(delegate -> delegate.add(element),
                () -> IntCollection.super.add(element));
    }

    @Override
    public boolean addAll(Collection<? extends Integer> collection) {
        checkModifiable();
        return forwarder().predicateOp(delegate -> delegate.addAll(collection),
                () -> IntCollection.super.addAll(collection));
    }

    @Override
    public boolean addAll(IntCollection collection) {
        checkModifiable();
        return forwarder().predicateOp(delegate -> delegate.addAll(collection),
                () -> IntCollection.super.addAll(collection));
    }

    @Override
    public boolean addInt(int element) {
        checkModifiable();
        return forwarder().predicateOp(delegate -> delegate.addInt(element),
                () -> IntCollection.super.addInt(element));
    }

    @Override
    public void clear() {
        checkModifiable();
        forwarder().voidOp(IntCollection::clear, IntCollection.super::clear);
    }

    @Override
    public boolean contains(Object o) {
        return forwarder().predicateOp(delegate -> delegate.contains(o),
                () -> IntCollection.super.contains(o));
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return forwarder().predicateOp(delegate ->
                        delegate.containsAll(collection),
                () -> IntCollection.super.containsAll(collection));
    }

    @Override
    public boolean containsAll(IntCollection collection) {
        return forwarder().predicateOp(delegate ->
                        delegate.containsAll(collection),
                () -> IntCollection.super.containsAll(collection));
    }

    @Override
    public boolean containsInt(int element) {
        return forwarder().predicateOp(delegate ->
                        delegate.containsInt(element),
                () -> IntCollection.super.containsInt(element));
    }

    @Override
    public PrimitiveIterator.OfInt iterator() {
        return forwarder().maskIfNeeded(forwarder().boxedOp(IntCollection::iterator),
                IntIteratorView::unmodifiable);
    }

    @Override
    public IntStream parallelPrimitiveStream() {
        return forwarder().boxedOp(IntCollection::parallelPrimitiveStream,
                IntCollection.super::parallelPrimitiveStream);
    }

    @Override
    public IntStream primitiveStream() {
        return forwarder().boxedOp(IntCollection::primitiveStream,
                IntCollection.super::primitiveStream);
    }

    @Override
    public boolean remove(Object o) {
        checkModifiable();
        return forwarder().predicateOp(delegate -> delegate.remove(o),
                () -> IntCollection.super.remove(o));
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        checkModifiable();
        return forwarder().predicateOp(delegate ->
                        delegate.removeAll(collection),
                () -> IntCollection.super.removeAll(collection));
    }

    @Override
    public boolean removeAll(IntCollection collection) {
        checkModifiable();
        return forwarder().predicateOp(delegate ->
                        delegate.removeAll(collection),
                () -> IntCollection.super.removeAll(collection));
    }

    @Override
    public boolean removeIf(IntPredicate filter) {
        checkModifiable();
        return forwarder().predicateOp(delegate -> delegate.removeIf(filter),
                () -> IntCollection.super.removeIf(filter));
    }

    @Override
    public boolean removeIf(Predicate<? super Integer> filter) {
        checkModifiable();
        return forwarder().predicateOp(delegate -> delegate.removeIf(filter),
                () -> IntCollection.super.removeIf(filter));
    }

    @Override
    public boolean removeIfInt(IntPredicate filter) {
        checkModifiable();
        return forwarder().predicateOp(delegate ->
                        delegate.removeIfInt(filter),
                () -> IntCollection.super.removeIfInt(filter));
    }

    @Override
    public boolean removeInt(int element) {
        checkModifiable();
        return forwarder().predicateOp(delegate ->
                        delegate.removeInt(element),
                () -> IntCollection.super.removeInt(element));
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        checkModifiable();
        return forwarder().predicateOp(delegate ->
                        delegate.retainAll(collection),
                () -> IntCollection.super.retainAll(collection));
    }

    @Override
    public boolean retainAll(IntCollection collection) {
        checkModifiable();
        return forwarder().predicateOp(delegate ->
                        delegate.retainAll(collection),
                () -> IntCollection.super.retainAll(collection));
    }

    @Override
    public Spliterator.OfInt spliterator() {
        return forwarder().boxedOp(IntCollection::spliterator,
                IntCollection.super::spliterator);
    }

    @Override
    public int[] toPrimitiveArray() {
        return forwarder().boxedOp(IntCollection::toPrimitiveArray,
                IntCollection.super::toPrimitiveArray);
    }
}
