package net.arinoru.collections;

import net.arinoru.function.TriFunction;
import net.arinoru.prerelease.PrereleaseContent;

import java.util.Collection;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.function.*;
import java.util.stream.*;

@PrereleaseContent
abstract class ForwardingPrimitiveCollection<T,T_ARR,T_CONS,T_PRED,
        T_SPLITR extends Spliterator.OfPrimitive<T,T_CONS,T_SPLITR>,
        T_STR extends BaseStream<?,?>, PURE extends Collection<?>,
        F extends ForwardingPrimitiveCollection<T,T_ARR,T_CONS,T_PRED,T_SPLITR,T_STR,PURE,F>>
        extends AbstractForwardingClass<Collection<?>,PURE,F>
        implements PrimitiveCollection<T,T_ARR,T_CONS,T_PRED,T_SPLITR,T_STR> {
    ForwardingPrimitiveCollection(
            Collection<?> collection,
            ForwardingType forwardingType,
            MaskingType maskingType,
            TriFunction<Collection<?>,ForwardingType,MaskingType,F> constructor
    ) {
        super(collection, forwardingType, maskingType, constructor);
    }

    @Override
    public void clear() {
        checkNotUnmodifiable();
        switch (forwardingType) {
            case PURE -> composedClass.clear();
            case SHALLOW, MINIMAL -> PrimitiveCollection.super.clear();
        }
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return switch (forwardingType) {
            case PURE -> composedClass.containsAll(c);
            case SHALLOW, MINIMAL -> PrimitiveCollection.super.containsAll(c);
        };
    }

    @Override
    public boolean equals(Object o) {
        return switch (forwardingType) {
            case PURE -> composedClass.equals(o);
            case SHALLOW, MINIMAL -> super.equals(o);
        };
    }

    @Override
    public int hashCode() {
        return switch (forwardingType) {
            case PURE -> composedClass.hashCode();
            case SHALLOW, MINIMAL -> super.hashCode();
        };
    }

    @Override
    public boolean isEmpty() {
        return switch (forwardingType) {
            case PURE -> composedClass.isEmpty();
            case SHALLOW, MINIMAL -> PrimitiveCollection.super.isEmpty();
        };
    }

    private static boolean isUnmodifiable(PrimitiveCollection<?,?,?,?,?,?> collection) {
        return switch (collection) {
            case UnmodifiablePrimitiveCollection<?,?,?,?,?,?,?> ignored -> true;
            case AbstractForwardingClass<?,?,?> f -> f.unmodifiable;
            default -> false;
        };
    }

    @Override
    public abstract PrimitiveIterator<T,T_CONS> iterator();

    @Override
    public abstract T_STR parallelPrimitiveStream();

    @Override
    public Stream<T> parallelStream() {
        return PrimitiveCollection.super.parallelStream();
    }

    @Override
    public abstract T_STR primitiveStream();

    @Override
    public abstract boolean removeIf(T_PRED filter);

    @Override
    public boolean removeIf(Predicate<? super T> filter) {
        return PrimitiveCollection.super.removeIf(filter);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        checkNotUnmodifiable();
        return switch (forwardingType) {
            case PURE -> composedClass.retainAll(c);
            case SHALLOW, MINIMAL -> PrimitiveCollection.super.retainAll(c);
        };
    }

    @Override
    public int size() {
        return composedClass.size();
    }

    @Override
    public abstract Spliterator.OfPrimitive<T,T_CONS,T_SPLITR> spliterator();

    @Override
    public Stream<T> stream() {
        return PrimitiveCollection.super.stream();
    }

    @Override
    public Object[] toArray() {
        return composedClass.toArray();
    }

    @Override
    public <U> U[] toArray(IntFunction<U[]> generator) {
        return switch (forwardingType) {
            case PURE -> composedClass.toArray(generator);
            case SHALLOW, MINIMAL -> PrimitiveCollection.super.toArray(generator);
        };
    }

    @Override
    public <U> U[] toArray(U[] a) {
        return composedClass.toArray(a);
    }

    @PrereleaseContent
    static class OfDouble extends ForwardingPrimitiveCollection<Double,double[],
            DoubleConsumer, DoublePredicate, Spliterator.OfDouble, DoubleStream,
            PrimitiveCollection.OfDouble, ForwardingPrimitiveCollection.OfDouble>
            implements PrimitiveCollection.OfDouble {
        OfDouble(
                Collection<?> collection,
                ForwardingType forwardingType,
                MaskingType maskingType
        ) {
            super(collection, forwardingType, maskingType, ForwardingPrimitiveCollection.OfDouble::new);
        }

        OfDouble(
                Collection<?> collection,
                ForwardingType forwardingType,
                MaskingType maskingType,
                TriFunction<Collection<?>,ForwardingType,MaskingType,ForwardingPrimitiveCollection.OfDouble> constructor
        ) {
            super(collection, forwardingType, maskingType, constructor);
        }

        @Override
        public boolean add(Double t) {
            checkNotUnmodifiable();
            return switch (forwardingType) {
                case PURE -> pure().add(t);
                case SHALLOW, MINIMAL -> PrimitiveCollection.OfDouble.super.add(t);
            };
        }

        @Override
        public boolean addAll(Collection<? extends Double> c) {
            checkNotUnmodifiable();
            return switch (forwardingType) {
                case PURE -> pure().addAll(c);
                case SHALLOW, MINIMAL -> PrimitiveCollection.OfDouble.super.addAll(c);
            };
        }

        @Override
        public boolean addAll(PrimitiveCollection.OfDouble c) {
            checkNotMinimal();
            return switch (forwardingType) {
                case PURE -> pure().addAll(c);
                case SHALLOW, MINIMAL -> PrimitiveCollection.OfDouble.super.addAll(c);
            };
        }

        @Override
        public boolean addDouble(double d) {
            checkNotUnmodifiable();
            checkNotMinimal();
            return pure().addDouble(d);
        }

        @Override
        public boolean contains(Object o) {
            return switch (forwardingType) {
                case PURE -> composedClass.contains(o);
                case SHALLOW, MINIMAL -> PrimitiveCollection.OfDouble.super.contains(o);
            };
        }

        @Override
        public boolean containsAll(PrimitiveCollection.OfDouble c) {
            return switch (forwardingType) {
                case PURE -> pure().containsAll(c);
                case SHALLOW, MINIMAL -> PrimitiveCollection.OfDouble.super.containsAll(c);
            };
        }

        @Override
        public boolean containsDouble(double d) {
            return switch (forwardingType) {
                case PURE -> pure().containsDouble(d);
                case SHALLOW, MINIMAL -> PrimitiveCollection.OfDouble.super.containsDouble(d);
            };
        }

        @Override
        public PrimitiveIterator.OfDouble iterator() {
            checkNotMinimal();
            return needsMask ? ForwardingPrimitiveIterator.unmodifiable(
                    pure().iterator()) : pure().iterator();
        }

        @Override
        public DoubleStream parallelPrimitiveStream() {
            return switch (forwardingType) {
                case PURE -> pure().parallelPrimitiveStream();
                case SHALLOW, MINIMAL -> PrimitiveCollection.OfDouble.super.parallelPrimitiveStream();
            };
        }

        @Override
        public Stream<Double> parallelStream() {
            return switch (forwardingType) {
                case PURE -> pure().parallelStream();
                case SHALLOW, MINIMAL -> super.parallelStream();
            };
        }

        @Override
        public DoubleStream primitiveStream() {
            return switch (forwardingType) {
                case PURE -> pure().primitiveStream();
                case SHALLOW, MINIMAL -> PrimitiveCollection.OfDouble.super.primitiveStream();
            };
        }

        @Override
        public boolean remove(Object o) {
            checkNotUnmodifiable();
            return switch (forwardingType) {
                case PURE -> composedClass.remove(o);
                case SHALLOW, MINIMAL -> PrimitiveCollection.OfDouble.super.remove(o);
            };
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            checkNotUnmodifiable();
            return switch (forwardingType) {
                case PURE -> composedClass.removeAll(c);
                case SHALLOW, MINIMAL -> PrimitiveCollection.OfDouble.super.removeAll(c);
            };
        }

        @Override
        public boolean removeAll(PrimitiveCollection.OfDouble c) {
            checkNotUnmodifiable();
            return switch (forwardingType) {
                case PURE -> pure().removeAll(c);
                case SHALLOW, MINIMAL -> PrimitiveCollection.OfDouble.super.removeAll(c);
            };
        }

        @Override
        public boolean removeDouble(double d) {
            checkNotUnmodifiable();
            return switch (forwardingType) {
                case PURE -> pure().removeDouble(d);
                case SHALLOW, MINIMAL -> PrimitiveCollection.OfDouble.super.removeDouble(d);
            };
        }

        @Override
        public boolean removeIf(DoublePredicate filter) {
            checkNotUnmodifiable();
            return switch (forwardingType) {
                case PURE -> pure().removeIf(filter);
                case SHALLOW, MINIMAL -> PrimitiveCollection.OfDouble.super.removeIf(filter);
            };
        }

        @Override
        public boolean removeIf(Predicate<? super Double> filter) {
            checkNotUnmodifiable();
            return switch (forwardingType) {
                case PURE -> pure().removeIf(filter);
                case SHALLOW, MINIMAL -> super.removeIf(filter);
            };
        }

        @Override
        public boolean retainAll(PrimitiveCollection.OfDouble c) {
            checkNotUnmodifiable();
            return switch (forwardingType) {
                case PURE -> pure().retainAll(c);
                case SHALLOW, MINIMAL -> PrimitiveCollection.OfDouble.super.retainAll(c);
            };
        }

        @Override
        public Spliterator.OfDouble spliterator() {
            return switch (forwardingType) {
                case PURE -> pure().spliterator();
                case SHALLOW, MINIMAL -> PrimitiveCollection.OfDouble.super.spliterator();
            };
        }

        @Override
        public Stream<Double> stream() {
            return switch (forwardingType) {
                case PURE -> pure().stream();
                case SHALLOW, MINIMAL -> super.stream();
            };
        }

        @Override
        public double[] toPrimitiveArray() {
            checkNotMinimal();
            return pure().toPrimitiveArray();
        }

        static PrimitiveCollection.OfDouble unmodifiable(PrimitiveCollection.OfDouble collection) {
            return isUnmodifiable(collection) ? collection :
                    new ForwardingPrimitiveCollection.OfDouble(collection,
                        ForwardingType.PURE, MaskingType.UNMODIFIABLE);
        }
    }

    @PrereleaseContent
    static class OfInt extends ForwardingPrimitiveCollection<Integer,int[],
            IntConsumer,IntPredicate,Spliterator.OfInt,IntStream, PrimitiveCollection.OfInt,
            ForwardingPrimitiveCollection.OfInt> implements PrimitiveCollection.OfInt {
        OfInt(
                Collection<?> collection,
                ForwardingType forwardingType,
                MaskingType maskingType
        ) {
            super(collection, forwardingType, maskingType, ForwardingPrimitiveCollection.OfInt::new);
        }

        OfInt(
                Collection<?> collection,
                ForwardingType forwardingType,
                MaskingType maskingType,
                TriFunction<Collection<?>,ForwardingType,MaskingType,ForwardingPrimitiveCollection.OfInt> constructor
        ) {
            super(collection, forwardingType, maskingType, constructor);
        }

        @Override
        public boolean add(Integer t) {
            checkNotUnmodifiable();
            return switch (forwardingType) {
                case PURE -> pure().add(t);
                case SHALLOW, MINIMAL -> PrimitiveCollection.OfInt.super.add(t);
            };
        }

        @Override
        public boolean addAll(Collection<? extends Integer> c) {
            checkNotUnmodifiable();
            return switch (forwardingType) {
                case PURE -> pure().addAll(c);
                case SHALLOW, MINIMAL -> PrimitiveCollection.OfInt.super.addAll(c);
            };
        }

        @Override
        public boolean addAll(PrimitiveCollection.OfInt c) {
            checkNotUnmodifiable();
            return switch (forwardingType) {
                case PURE -> pure().addAll(c);
                case SHALLOW, MINIMAL -> PrimitiveCollection.OfInt.super.addAll(c);
            };
        }

        @Override
        public boolean addInt(int i) {
            checkNotUnmodifiable();
            checkNotMinimal();
            return pure().addInt(i);
        }

        @Override
        public boolean contains(Object o) {
            return switch (forwardingType) {
                case PURE -> composedClass.contains(o);
                case SHALLOW, MINIMAL -> PrimitiveCollection.OfInt.super.contains(o);
            };
        }

        @Override
        public boolean containsAll(PrimitiveCollection.OfInt c) {
            return switch (forwardingType) {
                case PURE -> pure().containsAll(c);
                case SHALLOW, MINIMAL -> PrimitiveCollection.OfInt.super.containsAll(c);
            };
        }

        @Override
        public boolean containsInt(int i) {
            return switch (forwardingType) {
                case PURE -> pure().containsInt(i);
                case SHALLOW, MINIMAL -> PrimitiveCollection.OfInt.super.containsInt(i);
            };
        }

        @Override
        public PrimitiveIterator.OfInt iterator() {
            checkNotMinimal();
            return needsMask ? ForwardingPrimitiveIterator.unmodifiable(
                    pure().iterator()) : pure().iterator();
        }

        @Override
        public IntStream parallelPrimitiveStream() {
            return switch (forwardingType) {
                case PURE -> pure().parallelPrimitiveStream();
                case SHALLOW, MINIMAL -> PrimitiveCollection.OfInt.super.parallelPrimitiveStream();
            };
        }

        @Override
        public Stream<Integer> parallelStream() {
            return switch (forwardingType) {
                case PURE -> pure().parallelStream();
                case SHALLOW, MINIMAL -> super.parallelStream();
            };
        }

        @Override
        public IntStream primitiveStream() {
            return switch (forwardingType) {
                case PURE -> pure().primitiveStream();
                case SHALLOW, MINIMAL -> PrimitiveCollection.OfInt.super.primitiveStream();
            };
        }

        @Override
        public boolean remove(Object o) {
            checkNotUnmodifiable();
            return switch (forwardingType) {
                case PURE -> composedClass.remove(o);
                case SHALLOW, MINIMAL -> PrimitiveCollection.OfInt.super.remove(o);
            };
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            checkNotUnmodifiable();
            return switch (forwardingType) {
                case PURE -> composedClass.removeAll(c);
                case SHALLOW, MINIMAL -> PrimitiveCollection.OfInt.super.removeAll(c);
            };
        }

        @Override
        public boolean removeAll(PrimitiveCollection.OfInt c) {
            checkNotUnmodifiable();
            return switch (forwardingType) {
                case PURE -> pure().removeAll(c);
                case SHALLOW, MINIMAL -> PrimitiveCollection.OfInt.super.removeAll(c);
            };
        }

        @Override
        public boolean removeIf(IntPredicate filter) {
            checkNotUnmodifiable();
            return switch (forwardingType) {
                case PURE -> pure().removeIf(filter);
                case SHALLOW, MINIMAL -> PrimitiveCollection.OfInt.super.removeIf(filter);
            };
        }

        @Override
        public boolean removeIf(Predicate<? super Integer> filter) {
            checkNotUnmodifiable();
            return switch (forwardingType) {
                case PURE -> pure().removeIf(filter);
                case SHALLOW, MINIMAL -> super.removeIf(filter);
            };
        }

        @Override
        public boolean removeInt(int i) {
            checkNotUnmodifiable();
            return switch (forwardingType) {
                case PURE -> pure().removeInt(i);
                case SHALLOW, MINIMAL -> PrimitiveCollection.OfInt.super.removeInt(i);
            };
        }

        @Override
        public boolean retainAll(PrimitiveCollection.OfInt c) {
            checkNotUnmodifiable();
            return switch (forwardingType) {
                case PURE -> pure().retainAll(c);
                case SHALLOW, MINIMAL -> PrimitiveCollection.OfInt.super.retainAll(c);
            };
        }

        @Override
        public Spliterator.OfInt spliterator() {
            return switch (forwardingType) {
                case PURE -> pure().spliterator();
                case SHALLOW, MINIMAL -> PrimitiveCollection.OfInt.super.spliterator();
            };
        }

        @Override
        public Stream<Integer> stream() {
            return switch (forwardingType) {
                case PURE -> pure().stream();
                case SHALLOW, MINIMAL -> super.stream();
            };
        }

        @Override
        public int[] toPrimitiveArray() {
            checkNotMinimal();
            return pure().toPrimitiveArray();
        }

        static PrimitiveCollection.OfInt unmodifiable(PrimitiveCollection.OfInt collection) {
            return isUnmodifiable(collection) ? collection :
                    new ForwardingPrimitiveCollection.OfInt(collection,
                            ForwardingType.PURE, MaskingType.UNMODIFIABLE);
        }
    }

    @PrereleaseContent
    static class OfLong extends ForwardingPrimitiveCollection<Long,long[],
            LongConsumer,LongPredicate,Spliterator.OfLong,LongStream,
            PrimitiveCollection.OfLong, ForwardingPrimitiveCollection.OfLong>
            implements PrimitiveCollection.OfLong {
        OfLong(
                Collection<?> collection,
                ForwardingType forwardingType,
                MaskingType maskingType
        ) {
            super(collection, forwardingType, maskingType, ForwardingPrimitiveCollection.OfLong::new);
        }

        OfLong(
                Collection<?> collection,
                ForwardingType forwardingType,
                MaskingType maskingType,
                TriFunction<Collection<?>,ForwardingType,MaskingType,ForwardingPrimitiveCollection.OfLong> constructor
        ) {
            super(collection, forwardingType, maskingType, constructor);
        }

        @Override
        public boolean add(Long t) {
            checkNotUnmodifiable();
            return switch (forwardingType) {
                case PURE -> pure().add(t);
                case SHALLOW, MINIMAL -> PrimitiveCollection.OfLong.super.add(t);
            };
        }

        @Override
        public boolean addAll(Collection<? extends Long> c) {
            checkNotUnmodifiable();
            return switch (forwardingType) {
                case PURE -> pure().addAll(c);
                case SHALLOW, MINIMAL -> PrimitiveCollection.OfLong.super.addAll(c);
            };
        }

        @Override
        public boolean addAll(PrimitiveCollection.OfLong c) {
            checkNotUnmodifiable();
            return switch (forwardingType) {
                case PURE -> pure().addAll(c);
                case SHALLOW, MINIMAL -> PrimitiveCollection.OfLong.super.addAll(c);
            };
        }

        @Override
        public boolean addLong(long l) {
            checkNotUnmodifiable();
            checkNotMinimal();
            return pure().addLong(l);
        }

        @Override
        public boolean contains(Object o) {
            return switch (forwardingType) {
                case PURE -> composedClass.contains(o);
                case SHALLOW, MINIMAL -> PrimitiveCollection.OfLong.super.contains(o);
            };
        }

        @Override
        public boolean containsAll(PrimitiveCollection.OfLong c) {
            return switch (forwardingType) {
                case PURE -> pure().containsAll(c);
                case SHALLOW, MINIMAL -> PrimitiveCollection.OfLong.super.containsAll(c);
            };
        }

        @Override
        public boolean containsLong(long l) {
            return switch (forwardingType) {
                case PURE -> pure().containsLong(l);
                case SHALLOW, MINIMAL -> PrimitiveCollection.OfLong.super.containsLong(l);
            };
        }

        @Override
        public PrimitiveIterator.OfLong iterator() {
            checkNotMinimal();
            return needsMask ? ForwardingPrimitiveIterator.unmodifiable(
                    pure().iterator()) : pure().iterator();
        }

        @Override
        public LongStream parallelPrimitiveStream() {
            return switch (forwardingType) {
                case PURE -> pure().parallelPrimitiveStream();
                case SHALLOW, MINIMAL -> PrimitiveCollection.OfLong.super.parallelPrimitiveStream();
            };
        }

        @Override
        public Stream<Long> parallelStream() {
            return switch (forwardingType) {
                case PURE -> pure().parallelStream();
                case SHALLOW, MINIMAL -> super.parallelStream();
            };
        }

        @Override
        public LongStream primitiveStream() {
            return switch (forwardingType) {
                case PURE -> pure().primitiveStream();
                case SHALLOW, MINIMAL -> PrimitiveCollection.OfLong.super.primitiveStream();
            };
        }

        @Override
        public boolean remove(Object o) {
            checkNotUnmodifiable();
            return switch (forwardingType) {
                case PURE -> composedClass.remove(o);
                case SHALLOW, MINIMAL -> PrimitiveCollection.OfLong.super.remove(o);
            };
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            checkNotUnmodifiable();
            return switch (forwardingType) {
                case PURE -> composedClass.removeAll(c);
                case SHALLOW, MINIMAL -> PrimitiveCollection.OfLong.super.removeAll(c);
            };
        }

        @Override
        public boolean removeAll(PrimitiveCollection.OfLong c) {
            checkNotUnmodifiable();
            return switch (forwardingType) {
                case PURE -> pure().removeAll(c);
                case SHALLOW, MINIMAL -> PrimitiveCollection.OfLong.super.removeAll(c);
            };
        }

        @Override
        public boolean removeIf(LongPredicate filter) {
            checkNotUnmodifiable();
            return switch (forwardingType) {
                case PURE -> pure().removeIf(filter);
                case SHALLOW, MINIMAL -> PrimitiveCollection.OfLong.super.removeIf(filter);
            };
        }

        @Override
        public boolean removeIf(Predicate<? super Long> filter) {
            checkNotUnmodifiable();
            return switch (forwardingType) {
                case PURE -> pure().removeIf(filter);
                case SHALLOW, MINIMAL -> super.removeIf(filter);
            };
        }

        @Override
        public boolean removeLong(long l) {
            checkNotUnmodifiable();
            return switch (forwardingType) {
                case PURE -> pure().removeLong(l);
                case SHALLOW, MINIMAL -> PrimitiveCollection.OfLong.super.removeLong(l);
            };
        }

        @Override
        public boolean retainAll(PrimitiveCollection.OfLong c) {
            checkNotUnmodifiable();
            return switch (forwardingType) {
                case PURE -> pure().retainAll(c);
                case SHALLOW, MINIMAL -> PrimitiveCollection.OfLong.super.retainAll(c);
            };
        }

        @Override
        public Spliterator.OfLong spliterator() {
            return switch (forwardingType) {
                case PURE -> pure().spliterator();
                case SHALLOW, MINIMAL -> PrimitiveCollection.OfLong.super.spliterator();
            };
        }

        @Override
        public Stream<Long> stream() {
            return switch (forwardingType) {
                case PURE -> pure().stream();
                case SHALLOW, MINIMAL -> super.stream();
            };
        }

        @Override
        public long[] toPrimitiveArray() {
            checkNotMinimal();
            return pure().toPrimitiveArray();
        }

        static PrimitiveCollection.OfLong unmodifiable(PrimitiveCollection.OfLong collection) {
            return isUnmodifiable(collection) ? collection :
                    new ForwardingPrimitiveCollection.OfLong(collection,
                            ForwardingType.PURE, MaskingType.UNMODIFIABLE);
        }
    }
}
