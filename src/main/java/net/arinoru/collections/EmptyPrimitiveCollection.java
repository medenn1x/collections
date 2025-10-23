package net.arinoru.collections;

import net.arinoru.prerelease.PrereleaseContent;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

@PrereleaseContent
abstract class EmptyPrimitiveCollection<T,T_ARR,T_CONS,T_PRED,
        T_SPLITR extends Spliterator.OfPrimitive<T,T_CONS,T_SPLITR>,
        T_STR extends BaseStream<?,?>,
        T_COLL extends PrimitiveCollection<T,T_ARR,T_CONS,T_PRED,T_SPLITR,T_STR>>
        extends UnmodifiablePrimitiveCollection<T,T_ARR,T_CONS,T_PRED,T_SPLITR,T_STR,T_COLL>
        implements PrimitiveCollection<T,T_ARR,T_CONS,T_PRED,T_SPLITR,T_STR> {
    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return c.isEmpty();
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public abstract PrimitiveIterator<T,T_CONS> iterator();

    @Override
    public T_STR parallelPrimitiveStream() {
        return primitiveStream();
    }

    @Override
    public Stream<T> parallelStream() {
        return Stream.empty();
    }

    @Override
    public abstract T_STR primitiveStream();

    @Override
    public int size() {
        return 0;
    }

    @Override
    public abstract Spliterator.OfPrimitive<T,T_CONS,T_SPLITR> spliterator();

    @Override
    public Stream<T> stream() {
        return Stream.empty();
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <U> U[] toArray(IntFunction<U[]> generator) {
        return generator.apply(0);
    }

    @Override
    public <U> U[] toArray(U[] a) {
        if (a.length > 0)
            a[0] = null;
        return a;
    }

    @Override
    public abstract T_ARR toPrimitiveArray();

    @PrereleaseContent
    static class OfDouble extends EmptyPrimitiveCollection<Double,double[], DoubleConsumer,
            DoublePredicate, Spliterator.OfDouble, DoubleStream, PrimitiveCollection.OfDouble>
            implements PrimitiveCollection.OfDouble {
        static final PrimitiveCollection.OfDouble INSTANCE =
                new EmptyPrimitiveCollection.OfDouble();

        private OfDouble() {}

        @Override
        public boolean containsAll(PrimitiveCollection.OfDouble c) {
            return c.isEmpty();
        }

        @Override
        public boolean containsDouble(double d) {
            return false;
        }

        @Override
        public PrimitiveIterator.OfDouble iterator() {
            return EmptyPrimitiveIterator.OfDouble.INSTANCE;
        }

        @Override
        public DoubleStream primitiveStream() {
            return DoubleStream.empty();
        }

        @Override
        public Spliterator.OfDouble spliterator() {
            return Spliterators.emptyDoubleSpliterator();
        }

        @Override
        public double[] toPrimitiveArray() {
            return new double[0];
        }
    }

    @PrereleaseContent
    static class OfInt extends EmptyPrimitiveCollection<Integer,int[], IntConsumer,
            IntPredicate, Spliterator.OfInt, IntStream, PrimitiveCollection.OfInt>
            implements PrimitiveCollection.OfInt {
        static final PrimitiveCollection.OfInt INSTANCE =
                new EmptyPrimitiveCollection.OfInt();

        private OfInt() {}

        @Override
        public boolean containsAll(PrimitiveCollection.OfInt c) {
            return c.isEmpty();
        }

        @Override
        public boolean containsInt(int i) {
            return false;
        }

        @Override
        public PrimitiveIterator.OfInt iterator() {
            return EmptyPrimitiveIterator.OfInt.INSTANCE;
        }

        @Override
        public IntStream primitiveStream() {
            return IntStream.empty();
        }

        @Override
        public Spliterator.OfInt spliterator() {
            return Spliterators.emptyIntSpliterator();
        }

        @Override
        public int[] toPrimitiveArray() {
            return new int[0];
        }
    }

    @PrereleaseContent
    static class OfLong extends EmptyPrimitiveCollection<Long,long[], LongConsumer,
            LongPredicate, Spliterator.OfLong, LongStream, PrimitiveCollection.OfLong>
            implements PrimitiveCollection.OfLong {
        static final PrimitiveCollection.OfLong INSTANCE =
                new EmptyPrimitiveCollection.OfLong();

        private OfLong() {}

        @Override
        public boolean containsAll(PrimitiveCollection.OfLong c) {
            return c.isEmpty();
        }

        @Override
        public boolean containsLong(long l) {
            return false;
        }

        @Override
        public PrimitiveIterator.OfLong iterator() {
            return EmptyPrimitiveIterator.OfLong.INSTANCE;
        }

        @Override
        public LongStream primitiveStream() {
            return LongStream.empty();
        }

        @Override
        public Spliterator.OfLong spliterator() {
            return Spliterators.emptyLongSpliterator();
        }

        @Override
        public long[] toPrimitiveArray() {
            return new long[0];
        }
    }
}
