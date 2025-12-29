package net.arinoru.util.impl;

// Some of the content of this class may be imported and adapted from OpenJDK 11
// java.util.Spliterators

import net.arinoru.util.PrimitiveCollection;

import java.util.Comparator;
import java.util.Objects;
import java.util.PrimitiveIterator;
import java.util.Spliterator;

public abstract class PrimitiveCollectionSpliterator<T,T_CONS,
        T_ITER extends PrimitiveIterator<T,T_CONS>,
        T_SPLITR extends Spliterator.OfPrimitive<T,T_CONS,T_SPLITR>,
        T_COLL extends PrimitiveCollection<T,?,T_CONS,?,T_ITER,T_SPLITR,?,T_COLL>>
        implements Spliterator.OfPrimitive<T,T_CONS,T_SPLITR> {
    protected static final int BATCH_UNIT = 1 << 10;  // batch array size increment
    protected static final int MAX_BATCH = 1 << 25;  // max batch array size
    private final int characteristics;
    protected final T_COLL collection;
    protected T_ITER iterator;
    protected int size;
    protected int batchSize;

    protected PrimitiveCollectionSpliterator(T_COLL collection, int characteristics) {
        this.collection = collection;
        this.characteristics = (characteristics & Spliterator.CONCURRENT) == 0 ?
                characteristics | Spliterator.SIZED | Spliterator.SUBSIZED :
                characteristics;
    }

    protected void bindIterator() {
        if (iterator == null) {
            iterator = collection.iterator();
            size = collection.size();
        }
    }

    @Override
    public long estimateSize() {
        bindIterator();
        return size;
    }

    @Override
    @SuppressWarnings("MagicConstant")
    public int characteristics() {
        return characteristics;
    }

    @Override
    public Comparator<? super T> getComparator() {
        if (hasCharacteristics(SORTED))
            return null;
        throw new IllegalStateException();
    }

    @Override
    public abstract T_SPLITR trySplit();

    @Override
    public void forEachRemaining(T_CONS action) {
        Objects.requireNonNull(action);
        bindIterator();
        iterator.forEachRemaining(action);
    }
}
