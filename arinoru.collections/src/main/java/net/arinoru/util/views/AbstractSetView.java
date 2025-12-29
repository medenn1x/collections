package net.arinoru.util.views;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;

public abstract class AbstractSetView<E> implements View, Set<E> {
    protected abstract Forwarder<Set<?>,Set<E>> forwarder();

    @Override
    public boolean add(E element) {
        checkModifiable();
        return forwarder().predicateOp(delegate -> delegate.add(element));
    }

    @Override
    public boolean addAll(Collection<? extends E> collection) {
        checkModifiable();
        return forwarder().predicateOp(delegate -> delegate.addAll(collection));
    }

    @Override
    public void clear() {
        checkModifiable();
        forwarder().asDelegateType().clear();
    }

    @Override
    public boolean contains(Object o) {
        return forwarder().predicateOp(delegate -> delegate.contains(o));
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return forwarder().predicateOp(delegate ->
                delegate.containsAll(collection));
    }

    @Override
    @SuppressWarnings("EqualsDoesntCheckParameterClass")
    public boolean equals(Object o) {
        return forwarder().asDelegateType().equals(o);
    }

    @Override
    public void forEach(Consumer<? super E> action) {
        forwarder().voidOp(delegate -> delegate.forEach(action),
                () -> Set.super.forEach(action));
    }

    @Override
    public int hashCode() {
        return forwarder().asDelegateType().hashCode();
    }

    @Override
    public boolean isEmpty() {
        return forwarder().asDelegateType().isEmpty();
    }

    @Override
    public Iterator<E> iterator() {
        return forwarder().maskIfNeeded(forwarder().boxedOp(Set::iterator),
                IteratorView::unmodifiable);
    }

    @Override
    public Stream<E> parallelStream() {
        return forwarder().boxedOp(Set::parallelStream, Set.super::parallelStream);
    }

    @Override
    public boolean remove(Object o) {
        checkModifiable();
        return forwarder().predicateOp(delegate -> delegate.remove(o));
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        checkModifiable();
        return forwarder().predicateOp(delegate -> delegate.removeAll(collection));
    }

    @Override
    public boolean removeIf(Predicate<? super E> filter) {
        checkModifiable();
        return forwarder().predicateOp(delegate -> delegate.removeIf(filter),
                () -> Set.super.removeIf(filter));
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
        return forwarder().boxedOp(Set::spliterator, Set.super::spliterator);
    }

    @Override
    public Stream<E> stream() {
        return forwarder().boxedOp(Set::stream, Set.super::stream);
    }

    @Override
    public Object[] toArray() {
        return forwarder().boxedOp(Set::toArray);
    }

    @Override
    public <T> T[] toArray(IntFunction<T[]> generator) {
        return forwarder().boxedOp(delegate -> delegate.toArray(generator),
                () -> Set.super.toArray(generator));
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return forwarder().boxedOp(delegate -> delegate.toArray(a));
    }
}
