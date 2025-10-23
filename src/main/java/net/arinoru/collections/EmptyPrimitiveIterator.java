package net.arinoru.collections;

import net.arinoru.prerelease.PrereleaseContent;

import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;

@PrereleaseContent
abstract class EmptyPrimitiveIterator<T,T_CONS> implements PrimitiveIterator<T,T_CONS> {
    @Override
    public void forEachRemaining(Consumer<? super T> action) {}

    @Override
    public void forEachRemaining(T_CONS action) {}

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public T next() {
        throw new NoSuchElementException();
    }

    @Override
    public void remove() {
        throw new IllegalStateException();
    }

    @PrereleaseContent
    static class OfDouble extends EmptyPrimitiveIterator<Double, DoubleConsumer>
            implements PrimitiveIterator.OfDouble {
        static final EmptyPrimitiveIterator.OfDouble INSTANCE =
                new EmptyPrimitiveIterator.OfDouble();

        @Override
        public double nextDouble() {
            throw new NoSuchElementException();
        }
    }

    @PrereleaseContent
    static class OfInt extends EmptyPrimitiveIterator<Integer, IntConsumer>
            implements PrimitiveIterator.OfInt {
        static final EmptyPrimitiveIterator.OfInt INSTANCE =
                new EmptyPrimitiveIterator.OfInt();

        @Override
        public int nextInt() {
            throw new NoSuchElementException();
        }
    }

    @PrereleaseContent
    static class OfLong extends EmptyPrimitiveIterator<Long, LongConsumer>
            implements PrimitiveIterator.OfLong {
        static final EmptyPrimitiveIterator.OfLong INSTANCE =
                new EmptyPrimitiveIterator.OfLong();

        @Override
        public long nextLong() {
            throw new NoSuchElementException();
        }
    }
}
