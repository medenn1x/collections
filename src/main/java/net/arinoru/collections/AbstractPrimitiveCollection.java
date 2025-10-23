package net.arinoru.collections;

import net.arinoru.function.ObjIntFunction;
import net.arinoru.prerelease.PrereleaseContent;

import java.util.Arrays;
import java.util.Collection;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.function.*;
import java.util.stream.BaseStream;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

@PrereleaseContent
public abstract class AbstractPrimitiveCollection<T,T_ARR,T_CONS,T_PRED,
        T_SPLITR extends Spliterator.OfPrimitive<T,T_CONS,T_SPLITR>,
        T_STR extends BaseStream<?,?>>
        implements PrimitiveCollection<T,T_ARR,T_CONS,T_PRED,T_SPLITR,T_STR> {
    @PrereleaseContent
    public static abstract class OfDouble extends AbstractPrimitiveCollection<Double,
            double[], DoubleConsumer, DoublePredicate, Spliterator.OfDouble, DoubleStream>
            implements PrimitiveCollection.OfDouble {
        @Override
        public abstract PrimitiveIterator.OfDouble iterator();

        @Override
        public boolean removeAll(Collection<?> c) {
            if (c instanceof PrimitiveCollection.OfDouble ofDouble)
                return removeAll(ofDouble);
            return super.removeAll(c);
        }

        @Override
        public boolean removeAll(PrimitiveCollection.OfDouble c) {
            var changed = false;
            if (c.size() > size()) {
                var iterator = iterator();
                while (iterator.hasNext())
                    if (c.contains(iterator.nextDouble())) {
                        iterator.remove();
                        changed = true;
                    }
            } else {
                var iterator = c.iterator();
                while (iterator.hasNext())
                    changed |= removeDouble(iterator.nextDouble());
            }
            return changed;
        }

        @Override
        public double[] toPrimitiveArray() {
            return toPrimitiveArray(this);
        }

        static double[] toPrimitiveArray(PrimitiveCollection.OfDouble c) {
            var r = new double[c.size()];
            var iterator = c.iterator();
            return toArrayGeneric(r, arr -> arr.length, iterator::hasNext,
                    Arrays::copyOf, (arr, idx) -> arr[idx] = iterator.nextDouble());
        }
    }

    @PrereleaseContent
    public static abstract class OfInt extends AbstractPrimitiveCollection<Integer,
            int[], IntConsumer, IntPredicate, Spliterator.OfInt, IntStream>
            implements PrimitiveCollection.OfInt {
        @Override
        public abstract PrimitiveIterator.OfInt iterator();

        @Override
        public boolean removeAll(Collection<?> c) {
            if (c instanceof PrimitiveCollection.OfInt ofInt)
                return removeAll(ofInt);
            return super.removeAll(c);
        }

        @Override
        public boolean removeAll(PrimitiveCollection.OfInt c) {
            var changed = false;
            if (c.size() > size()) {
                var iterator = iterator();
                while (iterator.hasNext())
                    if (c.contains(iterator.nextInt())) {
                        iterator.remove();
                        changed = true;
                    }
            } else {
                var iterator = c.iterator();
                while (iterator.hasNext())
                    changed |= removeInt(iterator.nextInt());
            }
            return changed;
        }

        @Override
        public int[] toPrimitiveArray() {
            return toPrimitiveArray(this);
        }

        static int[] toPrimitiveArray(PrimitiveCollection.OfInt c) {
            var r = new int[c.size()];
            var iterator = c.iterator();
            return toArrayGeneric(r, arr -> arr.length, iterator::hasNext,
                    Arrays::copyOf, (arr, idx) -> arr[idx] = iterator.nextInt());
        }
    }

    @PrereleaseContent
    public static abstract class OfLong extends AbstractPrimitiveCollection<Long, long[],
            LongConsumer, LongPredicate, Spliterator.OfLong, LongStream>
            implements PrimitiveCollection.OfLong {
        @Override
        public abstract PrimitiveIterator.OfLong iterator();

        @Override
        public boolean removeAll(Collection<?> c) {
            if (c instanceof PrimitiveCollection.OfLong ofLong)
                return removeAll(ofLong);
            return super.removeAll(c);
        }

        @Override
        public boolean removeAll(PrimitiveCollection.OfLong c) {
            var changed = false;
            if (c.size() > size()) {
                var iterator = iterator();
                while (iterator.hasNext())
                    if (c.contains(iterator.nextLong())) {
                        iterator.remove();
                        changed = true;
                    }
            } else {
                var iterator = c.iterator();
                while (iterator.hasNext())
                    changed |= removeLong(iterator.nextLong());
            }
            return changed;
        }

        @Override
        public long[] toPrimitiveArray() {
            return toPrimitiveArray(this);
        }

        static long[] toPrimitiveArray(PrimitiveCollection.OfLong c) {
            var r = new long[c.size()];
            var iterator = c.iterator();
            return toArrayGeneric(r, arr -> arr.length, iterator::hasNext,
                    Arrays::copyOf, (arr, idx) -> arr[idx] = iterator.nextLong());
        }
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        var changed = false;
        if (c.size() > size()) {
            var iterator = iterator();
            while (iterator.hasNext())
                if (c.contains(iterator.next())) {
                    iterator.remove();
                    changed = true;
                }
        } else
            for (var o : c)
                changed |= remove(o);
        return changed;
    }

    // The following code is imported and adapted from OpenJDK 11 AbstractCollection
    private static <T> T toArrayGeneric(
            T r,
            ToIntFunction<T> getLength,
            BooleanSupplier hasNext,
            ObjIntFunction<T, T> reallocate,
            ObjIntConsumer<T> assignNext
    ) {
        for (int i = 0; i < getLength.applyAsInt(r); i++) {
            if (!hasNext.getAsBoolean()) // fewer elements than expected
                return reallocate.apply(r, i);
            assignNext.accept(r, i);
        }
        return hasNext.getAsBoolean() ?
                finishToArray(r, getLength, hasNext, reallocate, assignNext) : r;
    }

    static Object[] toArray(Collection<?> c) {
        // Estimate size of array; be prepared to see more or fewer elements
        var r = new Object[c.size()];
        var iterator = c.iterator();
        return toArrayGeneric(r, arr -> arr.length, iterator::hasNext,
                Arrays::copyOf, (arr, idx) -> arr[idx] = iterator.next());
    }

    private static <T> T toArrayGeneric(
            T a,
            T r,
            ToIntFunction<T> getLength,
            BooleanSupplier hasNext,
            ObjIntFunction<T, T> reallocate,
            ObjIntConsumer<T> assignNext,
            ObjIntConsumer<T> nullTerminate,
            IntConsumer copyArray
    ) {
        for (int i = 0; i < getLength.applyAsInt(r); i++) {
            if (!hasNext.getAsBoolean()) { // fewer elements than expected
                if (a == r)
                    nullTerminate.accept(r, i);
                else if (getLength.applyAsInt(a) < i)
                    return reallocate.apply(r, i);
                else {
                    copyArray.accept(i);
                    if (getLength.applyAsInt(a) > i)
                        nullTerminate.accept(a, i);
                }
                return a;
            }
            assignNext.accept(r, i);
        }
        return hasNext.getAsBoolean() ?
                finishToArray(r, getLength, hasNext, reallocate, assignNext) : r;
    }

    @SuppressWarnings("unchecked")
    static <U> U[] toArray(Collection<?> c, U[] a) {
        // Estimate size of array; be prepared to see more or fewer elements
        int size = c.size();
        U[] r = a.length >= size ? a :
                (U[]) java.lang.reflect.Array.newInstance(
                        a.getClass().getComponentType(), size);
        var iterator = c.iterator();
        return toArrayGeneric(a, r, arr -> arr.length, iterator::hasNext,
                Arrays::copyOf, (arr, idx) -> arr[idx] = (U) iterator.next(),
                (arr, idx) -> arr[idx] = null,
                idx -> System.arraycopy(r, 0, a, 0, idx));
    }

    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

    private static <T> T finishToArray(
            T r,
            ToIntFunction<T> getLength,
            BooleanSupplier hasNext,
            ObjIntFunction<T,T> reallocate,
            ObjIntConsumer<T> assignNext
    ) {
        int i = getLength.applyAsInt(r);
        while (hasNext.getAsBoolean()) {
            int cap = getLength.applyAsInt(r);
            if (i == cap) {
                int newCap = cap + (cap >> 1) + 1;
                // overflow-conscious code
                if (newCap - MAX_ARRAY_SIZE > 0)
                    newCap = hugeCapacity(cap + 1);
                r = reallocate.apply(r, newCap);
            }
            assignNext.accept(r, i++);
        }
        // trim if overallocated
        return i == getLength.applyAsInt(r) ? r : reallocate.apply(r, i);
    }

    private static int hugeCapacity(int minCapacity) {
        if (minCapacity < 0) // overflow
            throw new OutOfMemoryError("Required array size too large");
        return minCapacity > MAX_ARRAY_SIZE ?
                Integer.MAX_VALUE :
                MAX_ARRAY_SIZE;
    }
}
