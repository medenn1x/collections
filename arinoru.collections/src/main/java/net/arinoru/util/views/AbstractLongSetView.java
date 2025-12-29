package net.arinoru.util.views;

import net.arinoru.util.LongCollection;
import net.arinoru.util.LongSet;

import java.util.Collection;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.function.LongConsumer;
import java.util.function.LongPredicate;
import java.util.function.Predicate;
import java.util.stream.LongStream;

public abstract class AbstractLongSetView extends AbstractPrimitiveSetView<Long,long[],
        LongConsumer,LongPredicate,PrimitiveIterator.OfLong,Spliterator.OfLong,LongStream,
        LongCollection,LongSet> implements LongSet {
    @Override
    public boolean add(Long element) {
        checkModifiable();
        return forwarder().predicateOp(delegate -> delegate.add(element),
                () -> LongSet.super.add(element));
    }

    @Override
    public boolean addAll(Collection<? extends Long> collection) {
        checkModifiable();
        return forwarder().predicateOp(delegate -> delegate.addAll(collection),
                () -> LongSet.super.addAll(collection));
    }

    @Override
    public boolean addAll(LongCollection collection) {
        checkModifiable();
        return forwarder().predicateOp(delegate -> delegate.addAll(collection),
                () -> LongSet.super.addAll(collection));
    }

    @Override
    public boolean addLong(long element) {
        checkModifiable();
        return forwarder().predicateOp(delegate -> delegate.addLong(element),
                () -> LongSet.super.addLong(element));
    }

    @Override
    public void clear() {
        checkModifiable();
        forwarder().voidOp(LongSet::clear, LongSet.super::clear);
    }

    @Override
    public boolean contains(Object o) {
        return forwarder().predicateOp(delegate -> delegate.contains(o),
                () -> LongSet.super.contains(o));
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return forwarder().predicateOp(delegate ->
                        delegate.containsAll(collection),
                () -> LongSet.super.containsAll(collection));
    }

    @Override
    public boolean containsAll(LongCollection collection) {
        return forwarder().predicateOp(delegate ->
                        delegate.containsAll(collection),
                () -> LongSet.super.containsAll(collection));
    }

    @Override
    public boolean containsLong(long element) {
        return forwarder().predicateOp(delegate -> delegate.containsLong(element),
                () -> LongSet.super.containsLong(element));
    }

    @Override
    public PrimitiveIterator.OfLong iterator() {
        return forwarder().maskIfNeeded(forwarder().boxedOp(LongSet::iterator),
                LongIteratorView::unmodifiable);
    }

    @Override
    public LongStream parallelPrimitiveStream() {
        return forwarder().boxedOp(LongSet::parallelPrimitiveStream,
                LongSet.super::parallelPrimitiveStream);
    }

    @Override
    public LongStream primitiveStream() {
        return forwarder().boxedOp(LongSet::primitiveStream,
                LongSet.super::primitiveStream);
    }

    @Override
    public boolean remove(Object o) {
        checkModifiable();
        return forwarder().predicateOp(delegate -> delegate.remove(o),
                () -> LongSet.super.remove(o));
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        checkModifiable();
        return forwarder().predicateOp(delegate ->
                        delegate.removeAll(collection),
                () -> LongSet.super.removeAll(collection));
    }

    @Override
    public boolean removeAll(LongCollection collection) {
        checkModifiable();
        return forwarder().predicateOp(delegate ->
                        delegate.removeAll(collection),
                () -> LongSet.super.removeAll(collection));
    }

    @Override
    public boolean removeIf(LongPredicate filter) {
        checkModifiable();
        return forwarder().predicateOp(delegate -> delegate.removeIf(filter),
                () -> LongSet.super.removeIf(filter));
    }

    @Override
    public boolean removeIf(Predicate<? super Long> filter) {
        checkModifiable();
        return forwarder().predicateOp(delegate -> delegate.removeIf(filter),
                () -> LongSet.super.removeIf(filter));
    }

    @Override
    public boolean removeIfLong(LongPredicate filter) {
        checkModifiable();
        return forwarder().predicateOp(delegate -> delegate.removeIfLong(filter),
                () -> LongSet.super.removeIfLong(filter));
    }

    @Override
    public boolean removeLong(long element) {
        checkModifiable();
        return forwarder().predicateOp(delegate -> delegate.removeLong(element),
                () -> LongSet.super.removeLong(element));
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        checkModifiable();
        return forwarder().predicateOp(delegate -> delegate.retainAll(collection),
                () -> LongSet.super.retainAll(collection));
    }

    @Override
    public boolean retainAll(LongCollection collection) {
        checkModifiable();
        return forwarder().predicateOp(delegate -> delegate.retainAll(collection),
                () -> LongSet.super.retainAll(collection));
    }

    @Override
    public Spliterator.OfLong spliterator() {
        return forwarder().boxedOp(LongSet::spliterator, LongSet.super::spliterator);
    }

    @Override
    public long[] toPrimitiveArray() {
        return forwarder().boxedOp(LongSet::toPrimitiveArray,
                LongSet.super::toPrimitiveArray);
    }
}
