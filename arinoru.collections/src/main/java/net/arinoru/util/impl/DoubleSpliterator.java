package net.arinoru.util.impl;

import net.arinoru.util.DoubleCollection;

import java.util.Objects;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.DoubleConsumer;

public class DoubleSpliterator extends PrimitiveCollectionSpliterator<Double,
        DoubleConsumer,PrimitiveIterator.OfDouble,Spliterator.OfDouble,DoubleCollection>
        implements Spliterator.OfDouble {
    public DoubleSpliterator(DoubleCollection collection, int characteristics) {
        super(collection, characteristics);
    }

    @Override
    public Spliterator.OfDouble trySplit() {
        bindIterator();
        if (size > 1 && iterator.hasNext()) {
            int n = batchSize + BATCH_UNIT;
            if (n > size)
                n = size;
            if (n > MAX_BATCH)
                n = MAX_BATCH;
            double[] a = new double[n];
            int j = 0;
            do {
                a[j] = iterator.nextDouble();
            } while (++j < n && iterator.hasNext());
            batchSize = j;
            size -= j;
            return Spliterators.spliterator(a, 0, j, characteristics());
        }
        return null;
    }

    @Override
    public boolean tryAdvance(DoubleConsumer action) {
        Objects.requireNonNull(action);
        bindIterator();
        if (iterator.hasNext()) {
            action.accept(iterator.nextDouble());
            return true;
        }
        return false;
    }
}
