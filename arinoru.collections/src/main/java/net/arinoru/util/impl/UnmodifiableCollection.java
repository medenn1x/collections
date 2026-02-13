package net.arinoru.util.impl;

import net.arinoru.util.CollectionNotModifiableException;
import net.arinoru.util.views.UnmodifiableView;

import java.util.Collection;
import java.util.function.Predicate;

public abstract class UnmodifiableCollection<E>
        implements Collection<E>, UnmodifiableView {
    @Override
    public boolean add(E e) {
        throw new CollectionNotModifiableException();
    }

    @Override
    public boolean addAll(Collection<? extends E> collection) {
        throw new CollectionNotModifiableException();
    }

    @Override
    public void clear() {
        throw new CollectionNotModifiableException();
    }

    @Override
    public boolean remove(Object o) {
        throw new CollectionNotModifiableException();
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        throw new CollectionNotModifiableException();
    }

    @Override
    public boolean removeIf(Predicate<? super E> filter) {
        throw new CollectionNotModifiableException();
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        throw new CollectionNotModifiableException();
    }
}
