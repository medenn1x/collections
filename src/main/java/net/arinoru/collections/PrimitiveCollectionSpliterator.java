package net.arinoru.collections;

import net.arinoru.prerelease.PrereleaseContent;

import java.util.Comparator;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.DoubleConsumer;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;

// The class was adapted from java.util.Spliterators in OpenJDK 11.
@PrereleaseContent
abstract class PrimitiveCollectionSpliterator<T,T_CONS,
        T_SPLITR extends Spliterator.OfPrimitive<T,T_CONS,T_SPLITR>>
        implements Spliterator.OfPrimitive<T,T_CONS,T_SPLITR> {
    static final int BATCH_UNIT = 1 << 10;  // batch array size increment
    static final int MAX_BATCH = 1 << 25;  // max batch array size
    private final int characteristics;
    long estimatedSize;
    int batchSize;

    PrimitiveCollectionSpliterator(
            int characteristics
    ) {
        this.characteristics = (characteristics & Spliterator.CONCURRENT) == 0 ?
                characteristics | Spliterator.SIZED | Spliterator.SUBSIZED :
                characteristics;
    }

    protected abstract void bindIterator();

    @Override
    public long estimateSize() {
        bindIterator();
        return estimatedSize;
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

    @PrereleaseContent
    static class OfDouble extends PrimitiveCollectionSpliterator<Double, DoubleConsumer,
            Spliterator.OfDouble> implements Spliterator.OfDouble {
        private final PrimitiveCollection.OfDouble doubleCollection;
        private PrimitiveIterator.OfDouble doubleIterator;

        OfDouble(
                PrimitiveCollection.OfDouble doubleCollection,
                int characteristics
        ) {
            super(characteristics);
            this.doubleCollection = doubleCollection;
        }

        @Override
        protected void bindIterator() {
            if (doubleIterator == null) {
                doubleIterator = doubleCollection.iterator();
                estimatedSize = doubleCollection.size();
            }
        }

        @Override
        public Spliterator.OfDouble trySplit() {
            // Estimating size will bind the iterator if needed
            long size = estimateSize();
            var iterator = doubleIterator;
            if (size > 1 && iterator.hasNext()) {
                int n = batchSize + BATCH_UNIT;
                if (n > size)
                    n = (int) size;
                if (n > MAX_BATCH)
                    n = MAX_BATCH;
                double[] a = new double[n];
                int j = 0;
                do {
                    a[j] = iterator.nextDouble();
                } while (++j < n && iterator.hasNext());
                batchSize = j;
                if (estimatedSize != Long.MAX_VALUE)
                    estimatedSize -= j;
                return Spliterators.spliterator(a, 0, j, characteristics());
            }
            return null;
        }

        @Override
        public void forEachRemaining(DoubleConsumer action) {
            if (action == null)
                throw new NullPointerException();
            bindIterator();
            var iterator = doubleIterator;
            iterator.forEachRemaining(action);
        }

        @Override
        public boolean tryAdvance(DoubleConsumer action) {
            if (action == null)
                throw new NullPointerException();
            bindIterator();
            if (doubleIterator.hasNext()) {
                action.accept(doubleIterator.nextDouble());
                return true;
            }
            return false;
        }
    }

    @PrereleaseContent
    static class OfInt extends PrimitiveCollectionSpliterator<Integer, IntConsumer,
            Spliterator.OfInt> implements Spliterator.OfInt {
        private final PrimitiveCollection.OfInt intCollection;
        private PrimitiveIterator.OfInt intIterator;

        OfInt(
                PrimitiveCollection.OfInt intCollection,
                int characteristics
        ) {
            super(characteristics);
            this.intCollection = intCollection;
        }

        @Override
        protected void bindIterator() {
            if (intIterator == null) {
                intIterator = intCollection.iterator();
                estimatedSize = intCollection.size();
            }
        }

        @Override
        public Spliterator.OfInt trySplit() {
            // Estimating size will bind the iterator if needed
            long size = estimateSize();
            var iterator = intIterator;
            if (size > 1 && iterator.hasNext()) {
                int n = batchSize + BATCH_UNIT;
                if (n > size)
                    n = (int) size;
                if (n > MAX_BATCH)
                    n = MAX_BATCH;
                int[] a = new int[n];
                int j = 0;
                do {
                    a[j] = iterator.nextInt();
                } while (++j < n && iterator.hasNext());
                batchSize = j;
                if (estimatedSize != Long.MAX_VALUE)
                    estimatedSize -= j;
                return Spliterators.spliterator(a, 0, j, characteristics());
            }
            return null;
        }

        @Override
        public void forEachRemaining(IntConsumer action) {
            if (action == null)
                throw new NullPointerException();
            bindIterator();
            var iterator = intIterator;
            iterator.forEachRemaining(action);
        }

        @Override
        public boolean tryAdvance(IntConsumer action) {
            if (action == null)
                throw new NullPointerException();
            bindIterator();
            if (intIterator.hasNext()) {
                action.accept(intIterator.nextInt());
                return true;
            }
            return false;
        }
    }

    @PrereleaseContent
    static class OfLong extends PrimitiveCollectionSpliterator<Long, LongConsumer,
            Spliterator.OfLong> implements Spliterator.OfLong {
        private final PrimitiveCollection.OfLong longCollection;
        private PrimitiveIterator.OfLong longIterator;

        OfLong(
                PrimitiveCollection.OfLong longCollection,
                int characteristics
        ) {
            super(characteristics);
            this.longCollection = longCollection;
        }

        @Override
        protected void bindIterator() {
            if (longIterator == null) {
                longIterator = longCollection.iterator();
                estimatedSize = longCollection.size();
            }
        }

        @Override
        public Spliterator.OfLong trySplit() {
            // Estimating size will bind the iterator if needed
            long size = estimateSize();
            var iterator = longIterator;
            if (size > 1 && iterator.hasNext()) {
                int n = batchSize + BATCH_UNIT;
                if (n > size)
                    n = (int) size;
                if (n > MAX_BATCH)
                    n = MAX_BATCH;
                long[] a = new long[n];
                int j = 0;
                do {
                    a[j] = iterator.nextLong();
                } while (++j < n && iterator.hasNext());
                batchSize = j;
                if (estimatedSize != Long.MAX_VALUE)
                    estimatedSize -= j;
                return Spliterators.spliterator(a, 0, j, characteristics());
            }
            return null;
        }

        @Override
        public void forEachRemaining(LongConsumer action) {
            if (action == null)
                throw new NullPointerException();
            bindIterator();
            var iterator = longIterator;
            iterator.forEachRemaining(action);
        }

        @Override
        public boolean tryAdvance(LongConsumer action) {
            if (action == null)
                throw new NullPointerException();
            bindIterator();
            if (longIterator.hasNext()) {
                action.accept(longIterator.nextLong());
                return true;
            }
            return false;
        }
    }
}
