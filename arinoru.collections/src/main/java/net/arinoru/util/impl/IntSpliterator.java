package net.arinoru.util.impl;

import net.arinoru.util.IntCollection;

import java.util.Objects;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.IntConsumer;

public class IntSpliterator extends PrimitiveCollectionSpliterator<Integer,IntConsumer,
        PrimitiveIterator.OfInt,Spliterator.OfInt,IntCollection>
        implements Spliterator.OfInt {
    public IntSpliterator(IntCollection collection, int characteristics) {
        super(collection, characteristics);
    }

    @Override
    public Spliterator.OfInt trySplit() {
        bindIterator();
        if (size > 1 && iterator.hasNext()) {
            int n = batchSize + BATCH_UNIT;
            if (n > size)
                n = size;
            if (n > MAX_BATCH)
                n = MAX_BATCH;
            int[] a = new int[n];
            int j = 0;
            do {
                a[j] = iterator.nextInt();
            } while (++j < n && iterator.hasNext());
            batchSize = j;
            size -= j;
            return Spliterators.spliterator(a, 0, j, characteristics());
        }
        return null;
    }

    @Override
    public boolean tryAdvance(IntConsumer action) {
        Objects.requireNonNull(action);
        bindIterator();
        if (iterator.hasNext()) {
            action.accept(iterator.nextInt());
            return true;
        }
        return false;
    }
}
