package net.arinoru.util.impl;

import net.arinoru.util.LongCollection;

import java.util.Objects;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.LongConsumer;

public class LongSpliterator extends PrimitiveCollectionSpliterator<Long,LongConsumer,
        PrimitiveIterator.OfLong,Spliterator.OfLong,LongCollection>
        implements Spliterator.OfLong {
    public LongSpliterator(LongCollection collection, int characteristics) {
        super(collection, characteristics);
    }

    @Override
    public Spliterator.OfLong trySplit() {
        bindIterator();
        if (size > 1 && iterator.hasNext()) {
            int n = batchSize + BATCH_UNIT;
            if (n > size)
                n = size;
            if (n > MAX_BATCH)
                n = MAX_BATCH;
            long[] a = new long[n];
            int j = 0;
            do {
                a[j] = iterator.nextLong();
            } while (++j < n && iterator.hasNext());
            batchSize = j;
            size -= j;
            return Spliterators.spliterator(a, 0, j, characteristics());
        }
        return null;
    }

    @Override
    public boolean tryAdvance(LongConsumer action) {
        Objects.requireNonNull(action);
        bindIterator();
        if (iterator.hasNext()) {
            action.accept(iterator.nextLong());
            return true;
        }
        return false;
    }
}
