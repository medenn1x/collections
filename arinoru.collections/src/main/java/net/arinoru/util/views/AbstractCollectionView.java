package net.arinoru.util.views;

import java.util.Collection;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;

public abstract class AbstractCollectionView<E> implements View, Collection<E> {
    protected abstract Forwarder<Collection<?>,Collection<E>> forwarder();

    @Override
    public boolean add(E e) {
        checkModifiable();
        return forwarder().predicateOp(delegate -> delegate.add(e));
    }

    @Override
    public boolean addAll(Collection<? extends E> collection) {
        checkModifiable();
        return forwarder().predicateOp(delegate ->
                delegate.addAll(collection));
    }

    @Override
    public void clear() {
        checkModifiable();
        forwarder().asDelegateType().clear();
    }

    @Override
    public boolean contains(Object o) {
        return forwarder().predicateOp(delegate ->
                delegate.contains(o));
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return forwarder().predicateOp(delegate ->
                delegate.containsAll(collection));
    }

    @Override
    public void forEach(Consumer<? super E> action) {
        forwarder().voidOp(delegate -> delegate.forEach(action),
                () -> Collection.super.forEach(action));
    }

    @Override
    public int hashCode() {
        return forwarder().intOp(Collection::hashCode, super::hashCode);
    }

    @Override
    public boolean isEmpty() {
        return forwarder().asDelegateType().isEmpty();
    }

    @Override
    public Iterator<E> iterator() {
        return forwarder().maskIfNeeded(forwarder().boxedOp(Collection::iterator),
                IteratorView::unmodifiable);
    }

    @Override
    public Stream<E> parallelStream() {
        return forwarder().boxedOp(Collection::parallelStream,
                Collection.super::parallelStream);
    }

    @Override
    public boolean remove(Object o) {
        checkModifiable();
        return forwarder().predicateOp(delegate -> delegate.remove(o));
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        checkModifiable();
        return forwarder().predicateOp(delegate ->
                delegate.removeAll(collection));
    }

    @Override
    public boolean removeIf(Predicate<? super E> filter) {
        checkModifiable();
        return forwarder().predicateOp(delegate -> delegate.removeIf(filter),
                () -> Collection.super.removeIf(filter));
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        checkModifiable();
        return forwarder().predicateOp(delegate ->
                delegate.retainAll(collection));
    }

    @Override
    public int size() {
        return forwarder().asDelegateType().size();
    }

    @Override
    public Spliterator<E> spliterator() {
        return forwarder().boxedOp(Collection::spliterator,
                Collection.super::spliterator);
    }

    @Override
    public Stream<E> stream() {
        return forwarder().boxedOp(Collection::stream,
                Collection.super::stream);
    }

    @Override
    public Object[] toArray() {
        return forwarder().boxedOp(Collection::toArray);
    }

    @Override
    public <T> T[] toArray(IntFunction<T[]> generator) {
        return forwarder().boxedOp(
                delegate -> delegate.toArray(generator),
                () -> Collection.super.toArray(generator));
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return forwarder().boxedOp(delegate -> delegate.toArray(a));
    }
}
