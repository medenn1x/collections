package net.arinoru.collections;

import net.arinoru.function.TriFunction;
import net.arinoru.prerelease.PrereleaseContent;

import java.util.Iterator;
import java.util.PrimitiveIterator;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;

@PrereleaseContent
abstract class ForwardingPrimitiveIterator<T,T_CONS,T_PURE extends Iterator<?>,
        F extends ForwardingPrimitiveIterator<T,T_CONS,T_PURE,F>>
        extends AbstractForwardingClass<Iterator<?>,T_PURE,F>
        implements PrimitiveIterator<T,T_CONS> {
    ForwardingPrimitiveIterator(
            Iterator<?> iterator,
            ForwardingType forwardingType,
            MaskingType maskingType,
            TriFunction<Iterator<?>,ForwardingType,MaskingType,F> constructor
    ) {
        super(iterator, forwardingType, maskingType, constructor);
    }

    @Override
    public boolean hasNext() {
        return composedClass.hasNext();
    }

    @Override
    public void remove() {
        checkNotUnmodifiable();
        composedClass.remove();
    }

    static PrimitiveIterator.OfDouble unmodifiable(PrimitiveIterator.OfDouble iterator) {
        if (iterator instanceof AbstractForwardingClass<?,?,?> f && f.unmodifiable)
            return iterator;
        return new ForwardingPrimitiveIterator.OfDouble(iterator, ForwardingType.PURE,
                MaskingType.UNMODIFIABLE);
    }

    static PrimitiveIterator.OfInt unmodifiable(PrimitiveIterator.OfInt iterator) {
        if (iterator instanceof AbstractForwardingClass<?,?,?> f && f.unmodifiable)
            return iterator;
        return new ForwardingPrimitiveIterator.OfInt(iterator, ForwardingType.PURE,
                MaskingType.UNMODIFIABLE);
    }

    static PrimitiveIterator.OfLong unmodifiable(PrimitiveIterator.OfLong iterator) {
        if (iterator instanceof AbstractForwardingClass<?,?,?> f && f.unmodifiable)
            return iterator;
        return new ForwardingPrimitiveIterator.OfLong(iterator, ForwardingType.PURE,
                MaskingType.UNMODIFIABLE);
    }

    @PrereleaseContent
    static class OfDouble extends ForwardingPrimitiveIterator<Double,DoubleConsumer,
            PrimitiveIterator.OfDouble,ForwardingPrimitiveIterator.OfDouble>
            implements PrimitiveIterator.OfDouble {
        OfDouble(Iterator<?> iterator, ForwardingType forwardingType, MaskingType maskingType) {
            super(iterator, forwardingType, maskingType, ForwardingPrimitiveIterator.OfDouble::new);
        }

        @Override
        public void forEachRemaining(Consumer<? super Double> action) {
            switch (forwardingType) {
                case PURE -> pure().forEachRemaining(action);
                case SHALLOW, MINIMAL -> PrimitiveIterator.OfDouble.super.forEachRemaining(action);
            }
        }

        @Override
        public void forEachRemaining(DoubleConsumer action) {
            switch (forwardingType) {
                case PURE -> pure().forEachRemaining(action);
                case SHALLOW, MINIMAL -> PrimitiveIterator.OfDouble.super.forEachRemaining(action);
            }
        }

        @Override
        public Double next() {
            return switch (forwardingType) {
                case PURE -> pure().next();
                case SHALLOW, MINIMAL -> PrimitiveIterator.OfDouble.super.next();
            };
        }

        @Override
        public double nextDouble() {
            checkNotMinimal();
            return pure().nextDouble();
        }
    }

    @PrereleaseContent
    static class OfInt extends ForwardingPrimitiveIterator<Integer,IntConsumer,
            PrimitiveIterator.OfInt,ForwardingPrimitiveIterator.OfInt>
            implements PrimitiveIterator.OfInt {
        OfInt(Iterator<?> iterator, ForwardingType forwardingType, MaskingType maskingType) {
            super(iterator, forwardingType, maskingType, ForwardingPrimitiveIterator.OfInt::new);
        }

        @Override
        public void forEachRemaining(Consumer<? super Integer> action) {
            switch (forwardingType) {
                case PURE -> pure().forEachRemaining(action);
                case SHALLOW, MINIMAL -> PrimitiveIterator.OfInt.super.forEachRemaining(action);
            }
        }

        @Override
        public void forEachRemaining(IntConsumer action) {
            switch (forwardingType) {
                case PURE -> pure().forEachRemaining(action);
                case SHALLOW, MINIMAL -> PrimitiveIterator.OfInt.super.forEachRemaining(action);
            }
        }

        @Override
        public Integer next() {
            return switch (forwardingType) {
                case PURE -> pure().next();
                case SHALLOW, MINIMAL -> PrimitiveIterator.OfInt.super.next();
            };
        }

        @Override
        public int nextInt() {
            checkNotMinimal();
            return pure().nextInt();
        }
    }

    @PrereleaseContent
    static class OfLong extends ForwardingPrimitiveIterator<Long,LongConsumer,
            PrimitiveIterator.OfLong,ForwardingPrimitiveIterator.OfLong>
            implements PrimitiveIterator.OfLong {
        OfLong(Iterator<?> iterator, ForwardingType forwardingType, MaskingType maskingType) {
            super(iterator, forwardingType, maskingType, ForwardingPrimitiveIterator.OfLong::new);
        }

        @Override
        public void forEachRemaining(Consumer<? super Long> action) {
            switch (forwardingType) {
                case PURE -> pure().forEachRemaining(action);
                case SHALLOW, MINIMAL -> PrimitiveIterator.OfLong.super.forEachRemaining(action);
            }
        }

        @Override
        public void forEachRemaining(LongConsumer action) {
            switch (forwardingType) {
                case PURE -> pure().forEachRemaining(action);
                case SHALLOW, MINIMAL -> PrimitiveIterator.OfLong.super.forEachRemaining(action);
            }
        }

        @Override
        public Long next() {
            return switch (forwardingType) {
                case PURE -> pure().next();
                case SHALLOW, MINIMAL -> PrimitiveIterator.OfLong.super.next();
            };
        }

        @Override
        public long nextLong() {
            checkNotMinimal();
            return pure().nextLong();
        }
    }
}
