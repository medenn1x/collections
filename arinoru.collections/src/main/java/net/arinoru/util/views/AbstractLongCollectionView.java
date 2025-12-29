package net.arinoru.util.views;

import net.arinoru.util.LongCollection;

import java.util.Collection;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.function.LongConsumer;
import java.util.function.LongPredicate;
import java.util.function.Predicate;
import java.util.stream.LongStream;

public abstract class AbstractLongCollectionView
        extends AbstractPrimitiveCollectionView<Long,long[],LongConsumer,LongPredicate,
        PrimitiveIterator.OfLong,Spliterator.OfLong,LongStream,LongCollection>
        implements LongCollection {
    @Override
    public boolean add(Long element) {
        checkModifiable();
        return forwarder().predicateOp(delegate -> delegate.add(element),
                () -> LongCollection.super.add(element));
    }

    @Override
    public boolean addAll(Collection<? extends Long> collection) {
        checkModifiable();
        return forwarder().predicateOp(delegate ->
                        delegate.addAll(collection),
                () -> LongCollection.super.addAll(collection));
    }

    @Override
    public boolean addAll(LongCollection collection) {
        checkModifiable();
        return forwarder().predicateOp(delegate ->
                        delegate.addAll(collection),
                () -> LongCollection.super.addAll(collection));
    }

    @Override
    public boolean addLong(long element) {
        checkModifiable();
        return forwarder().predicateOp(delegate -> delegate.addLong(element),
                () -> LongCollection.super.addLong(element));
    }

    @Override
    public void clear() {
        checkModifiable();
        forwarder().voidOp(LongCollection::clear, LongCollection.super::clear);
    }

    @Override
    public boolean contains(Object o) {
        return forwarder().predicateOp(delegate -> delegate.contains(o),
                () -> LongCollection.super.contains(o));
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return forwarder().predicateOp(delegate ->
                        delegate.containsAll(collection),
                () -> LongCollection.super.containsAll(collection));
    }

    @Override
    public boolean containsAll(LongCollection collection) {
        return forwarder().predicateOp(delegate ->
                        delegate.containsAll(collection),
                () -> LongCollection.super.containsAll(collection));
    }

    @Override
    public boolean containsLong(long element) {
        return forwarder().predicateOp(delegate ->
                        delegate.containsLong(element),
                () -> LongCollection.super.containsLong(element));
    }

    @Override
    public PrimitiveIterator.OfLong iterator() {
        return forwarder().maskIfNeeded(forwarder().boxedOp(LongCollection::iterator),
                LongIteratorView::unmodifiable);
    }

    @Override
    public LongStream parallelPrimitiveStream() {
        return forwarder().boxedOp(LongCollection::parallelPrimitiveStream,
                LongCollection.super::parallelPrimitiveStream);
    }

    @Override
    public LongStream primitiveStream() {
        return forwarder().boxedOp(LongCollection::primitiveStream,
                LongCollection.super::primitiveStream);
    }

    @Override
    public boolean remove(Object o) {
        checkModifiable();
        return forwarder().predicateOp(delegate -> delegate.remove(o),
                () -> LongCollection.super.remove(o));
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        checkModifiable();
        return forwarder().predicateOp(delegate ->
                        delegate.removeAll(collection),
                () -> LongCollection.super.removeAll(collection));
    }

    @Override
    public boolean removeAll(LongCollection collection) {
        checkModifiable();
        return forwarder().predicateOp(delegate ->
                        delegate.removeAll(collection),
                () -> LongCollection.super.removeAll(collection));
    }

    @Override
    public boolean removeIf(LongPredicate filter) {
        checkModifiable();
        return forwarder().predicateOp(delegate ->
                        delegate.removeIf(filter),
                () -> LongCollection.super.removeIf(filter));
    }

    @Override
    public boolean removeIf(Predicate<? super Long> filter) {
        checkModifiable();
        return forwarder().predicateOp(delegate ->
                        delegate.removeIf(filter),
                () -> LongCollection.super.removeIf(filter));
    }

    @Override
    public boolean removeIfLong(LongPredicate filter) {
        checkModifiable();
        return forwarder().predicateOp(delegate ->
                        delegate.removeIfLong(filter),
                () -> LongCollection.super.removeIfLong(filter));
    }

    @Override
    public boolean removeLong(long element) {
        checkModifiable();
        return forwarder().predicateOp(delegate ->
                        delegate.removeLong(element),
                () -> LongCollection.super.removeLong(element));
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        checkModifiable();
        return forwarder().predicateOp(delegate ->
                        delegate.retainAll(collection),
                () -> LongCollection.super.retainAll(collection));
    }

    @Override
    public boolean retainAll(LongCollection collection) {
        checkModifiable();
        return forwarder().predicateOp(delegate ->
                        delegate.retainAll(collection),
                () -> LongCollection.super.retainAll(collection));
    }

    @Override
    public Spliterator.OfLong spliterator() {
        return forwarder().boxedOp(LongCollection::spliterator,
                LongCollection.super::spliterator);
    }

    @Override
    public long[] toPrimitiveArray() {
        return forwarder().boxedOp(LongCollection::toPrimitiveArray,
                LongCollection.super::toPrimitiveArray);
    }
}
