package net.arinoru.collections;

import net.arinoru.prerelease.PrereleaseContent;

import java.io.Serializable;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

/**
 * Internal utility class for managing classes that provide various view wrappers for
 * this package.
 */
@PrereleaseContent
class Views {
    private Views() {}

    static void checkNotUnmodifiable(Object o) {
        if (o instanceof UnmodifiableView)
            throw new UnsupportedOperationException();
    }

    static <E> Iterator<E> unmodifiableIteratorView(Iterator<E> delegate) {
        if (delegate instanceof UnmodifiableView)
            return delegate;
        var forwarder = new Forwarder<Iterator<?>,Iterator<E>>(delegate,
                ForwardingType.PURE, MaskingType.UNMODIFIABLE);
        return delegate instanceof Serializable ?
                new SerializableUnmodifiableIteratorView<>(forwarder) :
                new UnmodifiableIteratorView<>(forwarder);
    }

    static <E> Iterator<E> iteratorView(
            Iterator<?> delegate,
            ForwardingType forwardingType,
            MaskingType maskingType) {
        var forwarder = new Forwarder<Iterator<?>,Iterator<E>>(delegate,
                forwardingType, maskingType);
        return switch (delegate) {
            case UnmodifiableView ignored -> delegate instanceof Serializable ?
                    new SerializableUnmodifiableIteratorView<>(forwarder) :
                    new UnmodifiableIteratorView<>(forwarder);
            case Serializable ignored -> new SerializableIteratorView<>(forwarder);
            default -> new IteratorView<>(forwarder);
        };
    }

    static PrimitiveIterator.OfDouble unmodifiableDoubleIteratorView(
            PrimitiveIterator.OfDouble delegate) {
        if (delegate instanceof UnmodifiableView)
            return delegate;
        var forwarder = new Forwarder<Iterator<?>,PrimitiveIterator.OfDouble>(
                delegate, ForwardingType.PURE, MaskingType.UNMODIFIABLE);
        return delegate instanceof Serializable ?
                new SerializableUnmodifiableDoubleIteratorView(forwarder) :
                new UnmodifiableDoubleIteratorView(forwarder);
    }

    static PrimitiveIterator.OfDouble doubleIteratorView(
            Iterator<?> delegate,
            ForwardingType forwardingType,
            MaskingType maskingType) {
        var forwarder = new Forwarder<Iterator<?>,PrimitiveIterator.OfDouble>(delegate,
                forwardingType, maskingType);
        return switch (delegate) {
            case UnmodifiableView ignored -> delegate instanceof Serializable ?
                    new SerializableUnmodifiableDoubleIteratorView(forwarder) :
                    new UnmodifiableDoubleIteratorView(forwarder);
            case Serializable ignored -> new SerializableDoubleIteratorView(forwarder);
            default -> new DoubleIteratorView(forwarder);
        };
    }

    static PrimitiveIterator.OfInt unmodifiableIntIteratorView(
            PrimitiveIterator.OfInt delegate) {
        if (delegate instanceof UnmodifiableView)
            return delegate;
        var forwarder = new Forwarder<Iterator<?>,PrimitiveIterator.OfInt>(delegate,
                ForwardingType.PURE, MaskingType.UNMODIFIABLE);
        return delegate instanceof Serializable ?
                new SerializableUnmodifiableIntIteratorView(forwarder) :
                new UnmodifiableIntIteratorView(forwarder);
    }

    static PrimitiveIterator.OfInt intIteratorView(
            Iterator<?> delegate,
            ForwardingType forwardingType,
            MaskingType maskingType) {
        var forwarder = new Forwarder<Iterator<?>,PrimitiveIterator.OfInt>(delegate,
                forwardingType, maskingType);
        return switch (delegate) {
            case UnmodifiableView ignored -> delegate instanceof Serializable ?
                    new SerializableUnmodifiableIntIteratorView(forwarder) :
                    new UnmodifiableIntIteratorView(forwarder);
            case Serializable ignored -> new SerializableIntIteratorView(forwarder);
            default -> new IntIteratorView(forwarder);
        };
    }

    static PrimitiveIterator.OfLong unmodifiableLongIteratorView(
            PrimitiveIterator.OfLong delegate) {
        if (delegate instanceof UnmodifiableView)
            return delegate;
        var forwarder = new Forwarder<Iterator<?>,PrimitiveIterator.OfLong>(delegate,
                ForwardingType.PURE, MaskingType.UNMODIFIABLE);
        return delegate instanceof Serializable ?
                new SerializableUnmodifiableLongIteratorView(forwarder) :
                new UnmodifiableLongIteratorView(forwarder);
    }

    static PrimitiveIterator.OfLong longIteratorView(
            Iterator<?> delegate,
            ForwardingType forwardingType,
            MaskingType maskingType) {
        var forwarder = new Forwarder<Iterator<?>,PrimitiveIterator.OfLong>(delegate,
                forwardingType, maskingType);
        return switch (delegate) {
            case UnmodifiableView ignored -> delegate instanceof Serializable ?
                    new SerializableUnmodifiableLongIteratorView(forwarder) :
                    new UnmodifiableLongIteratorView(forwarder);
            case Serializable ignored -> new SerializableLongIteratorView(forwarder);
            default -> new LongIteratorView(forwarder);
        };
    }

    static <E> Collection<E> unmodifiableCollectionView(Collection<E> delegate) {
        if (delegate instanceof UnmodifiableView)
            return delegate;
        var forwarder = new Forwarder<Collection<?>,Collection<E>>(delegate,
                ForwardingType.PURE, MaskingType.UNMODIFIABLE);
        return delegate instanceof Serializable ?
                new SerializableUnmodifiableCollectionView<>(forwarder) :
                new UnmodifiableCollectionView<>(forwarder);
    }

    static <E> Collection<E> collectionView(
            Collection<?> delegate,
            ForwardingType forwardingType,
            MaskingType maskingType) {
        var forwarder = new Forwarder<Collection<?>,Collection<E>>(delegate,
                forwardingType, maskingType);
        return switch (delegate) {
            case UnmodifiableView ignored -> delegate instanceof Serializable ?
                    new SerializableUnmodifiableCollectionView<>(forwarder) :
                    new UnmodifiableCollectionView<>(forwarder);
            case Serializable ignored -> new SerializableCollectionView<>(forwarder);
            default -> new CollectionView<>(forwarder);
        };
    }

    static PrimitiveCollection.OfDouble unmodifiableDoubleCollectionView(
            PrimitiveCollection.OfDouble delegate) {
        if (delegate instanceof UnmodifiableView)
            return delegate;
        var forwarder = new Forwarder<Collection<?>,PrimitiveCollection.OfDouble>(
                delegate, ForwardingType.PURE, MaskingType.UNMODIFIABLE);
        return delegate instanceof Serializable ?
                new SerializableUnmodifiableDoubleCollectionView(forwarder) :
                new UnmodifiableDoubleCollectionView(forwarder);
    }

    static PrimitiveCollection.OfDouble doubleCollectionView(
            Collection<?> delegate,
            ForwardingType forwardingType,
            MaskingType maskingType) {
        var forwarder = new Forwarder<Collection<?>,PrimitiveCollection.OfDouble>(
                delegate, forwardingType, maskingType);
        return switch (delegate) {
            case UnmodifiableView ignored -> delegate instanceof  Serializable ?
                    new SerializableUnmodifiableDoubleCollectionView(forwarder) :
                    new UnmodifiableDoubleCollectionView(forwarder);
            case Serializable ignored -> new SerializableDoubleCollectionView(forwarder);
            default -> new DoubleCollectionView(forwarder);
        };
    }

    static PrimitiveCollection.OfInt unmodifiableIntCollectionView(
            PrimitiveCollection.OfInt delegate) {
        if (delegate instanceof UnmodifiableView)
            return delegate;
        var forwarder = new Forwarder<Collection<?>,PrimitiveCollection.OfInt>(
                delegate, ForwardingType.PURE, MaskingType.UNMODIFIABLE);
        return delegate instanceof Serializable ?
                new SerializableUnmodifiableIntCollectionView(forwarder) :
                new UnmodifiableIntCollectionView(forwarder);
    }

    static PrimitiveCollection.OfInt intCollectionView(
            Collection<?> delegate,
            ForwardingType forwardingType,
            MaskingType maskingType) {
        var forwarder = new Forwarder<Collection<?>,PrimitiveCollection.OfInt>(
                delegate, forwardingType, maskingType);
        return switch (delegate) {
            case UnmodifiableView ignored -> delegate instanceof Serializable ?
                    new SerializableUnmodifiableIntCollectionView(forwarder) :
                    new UnmodifiableIntCollectionView(forwarder);
            case Serializable ignored -> new SerializableIntCollectionView(forwarder);
            default -> new IntCollectionView(forwarder);
        };
    }

    static PrimitiveCollection.OfLong unmodifiableLongCollectionView(
            PrimitiveCollection.OfLong delegate) {
        if (delegate instanceof UnmodifiableView)
            return delegate;
        var forwarder = new Forwarder<Collection<?>,PrimitiveCollection.OfLong>(
                delegate, ForwardingType.PURE, MaskingType.UNMODIFIABLE);
        return delegate instanceof Serializable ?
                new SerializableUnmodifiableLongCollectionView(forwarder) :
                new UnmodifiableLongCollectionView(forwarder);
    }

    static PrimitiveCollection.OfLong longCollectionView(
            Collection<?> delegate,
            ForwardingType forwardingType,
            MaskingType maskingType) {
        var forwarder = new Forwarder<Collection<?>,PrimitiveCollection.OfLong>(
                delegate, forwardingType, maskingType);
        return switch (delegate) {
            case UnmodifiableView ignored -> delegate instanceof Serializable ?
                    new SerializableUnmodifiableLongCollectionView(forwarder) :
                    new UnmodifiableLongCollectionView(forwarder);
            case Serializable ignored -> new SerializableLongCollectionView(forwarder);
            default -> new LongCollectionView(forwarder);
        };
    }

    static PrimitiveSet.OfDouble unmodifiableDoubleSetView(
            PrimitiveSet.OfDouble delegate) {
        if (delegate instanceof UnmodifiableView)
            return delegate;
        var forwarder = new Forwarder<Set<?>,PrimitiveSet.OfDouble>(delegate,
                ForwardingType.PURE, MaskingType.UNMODIFIABLE);
        return delegate instanceof Serializable ?
                new SerializableUnmodifiableDoubleSetView(forwarder) :
                new UnmodifiableDoubleSetView(forwarder);
    }

    static PrimitiveSet.OfDouble doubleSetView(
            Set<?> delegate,
            ForwardingType forwardingType,
            MaskingType maskingType) {
        var forwarder = new Forwarder<Set<?>,PrimitiveSet.OfDouble>(
                delegate, forwardingType, maskingType);
        return switch (delegate) {
            case UnmodifiableView ignored -> delegate instanceof Serializable ?
                    new SerializableUnmodifiableDoubleSetView(forwarder) :
                    new UnmodifiableDoubleSetView(forwarder);
            case Serializable ignored -> new SerializableDoubleSetView(forwarder);
            default -> new DoubleSetView(forwarder);
        };
    }

    static PrimitiveSet.OfInt unmodifiableIntSetView(PrimitiveSet.OfInt delegate) {
        if (delegate instanceof UnmodifiableView)
            return delegate;
        var forwarder = new Forwarder<Set<?>,PrimitiveSet.OfInt>(delegate,
                ForwardingType.PURE, MaskingType.UNMODIFIABLE);
        return delegate instanceof Serializable ?
                new SerializableUnmodifiableIntSetView(forwarder) :
                new UnmodifiableIntSetView(forwarder);
    }

    static PrimitiveSet.OfInt intSetView(
            Set<?> delegate,
            ForwardingType forwardingType,
            MaskingType maskingType) {
        var forwarder = new Forwarder<Set<?>,PrimitiveSet.OfInt>(delegate,
                forwardingType, maskingType);
        return switch (delegate) {
            case UnmodifiableView ignored -> delegate instanceof Serializable ?
                    new SerializableUnmodifiableIntSetView(forwarder) :
                    new UnmodifiableIntSetView(forwarder);
            case Serializable ignored -> new SerializableIntSetView(forwarder);
            default -> new IntSetView(forwarder);
        };
    }

    static PrimitiveSet.OfLong unmodifiableLongSetView(PrimitiveSet.OfLong delegate) {
        if (delegate instanceof UnmodifiableView)
            return delegate;
        var forwarder = new Forwarder<Set<?>,PrimitiveSet.OfLong>(delegate,
                ForwardingType.PURE, MaskingType.UNMODIFIABLE);
        return delegate instanceof Serializable ?
                new SerializableUnmodifiableLongSetView(forwarder) :
                new UnmodifiableLongSetView(forwarder);
    }

    static PrimitiveSet.OfLong longSetView(
            Set<?> delegate,
            ForwardingType forwardingType,
            MaskingType maskingType) {
        var forwarder = new Forwarder<Set<?>,PrimitiveSet.OfLong>(delegate,
                forwardingType, maskingType);
        return switch (delegate) {
            case UnmodifiableView ignored -> delegate instanceof Serializable ?
                    new SerializableUnmodifiableLongSetView(forwarder) :
                    new UnmodifiableLongSetView(forwarder);
            case Serializable ignored -> new SerializableLongSetView(forwarder);
            default -> new LongSetView(forwarder);
        };
    }

    @PrereleaseContent
    enum ForwardingType {
        /**
         * <p>Denotes "pure" forwarding (all known methods are forwarded unless overridden).</p>
         * <p>This asserts that the composed class has an element type consistent with
         * the {@code PURE} type for the forwarding class, and that if
         * the forwarding class is a primitive-handling class, then the composed class
         * is also a primitive-handling class of the same type. Failure to adhere to this
         * contract may result in unexpected {@link ClassCastException}s.</p>
         */
        PURE,
        /**
         * <p>Denotes "shallow" forwarding (default methods are used when available)</p>
         * <p>This carries the same type assertions as PURE.</p>
         */
        SHALLOW,
        /**
         * <p>Denotes "minimal" forwarding (only methods insensitive to type are forwarded)</p>
         * <p>This puts no type requirements on the composed class beyond the base type required
         * by the forwarding class constructor; however, any methods
         * dealing with elements will throw IllegalStateException unless overridden.</p>
         */
        MINIMAL
    }

    @PrereleaseContent
    enum MaskingType {
        /**
         * Denotes that this wrapper should enforce immutability. Any methods which
         * return a view of the class will return an unmodifiable view if not
         * overridden; most forwarding classes will provide one or more
         * {@code maskIfNeeded} methods that overriding methods may use to ensure that
         * any backing class they expose is exposed only through an unmodifiable
         * view.
         */
        UNMODIFIABLE,
        /**
         * Denotes that this wrapper should inherit immutability from the backing
         * class if possible. If the backing class is not a view implemented
         * by a forwarding class, and is not implemented by a class known to be
         * unmodifiable, it will be assumed be mutable.
         */
        DELEGATE
    }

    @PrereleaseContent
    @FunctionalInterface
    interface ViewFactory<DELEGATE> {
        Object newInstance(DELEGATE delegate, ForwardingType forwardingType,
                           MaskingType maskingType);
    }

    @PrereleaseContent
    static class Forwarder<DELEGATE_TYPE, VIEW_TYPE extends DELEGATE_TYPE>
            implements Serializable {
        private final DELEGATE_TYPE delegate;
        private final ForwardingType forwardingType;
        private final boolean subViewsRequireMasking;

        Forwarder(DELEGATE_TYPE delegate,
                  ForwardingType forwardingType,
                  MaskingType maskingType) {
            this.delegate = delegate;
            this.forwardingType = forwardingType;
            if (delegate instanceof UnmodifiableView)
                subViewsRequireMasking = false;
            else
                subViewsRequireMasking = maskingType == MaskingType.UNMODIFIABLE;
        }

        @SuppressWarnings("unchecked")
        VIEW_TYPE asViewType() {
            return switch (forwardingType) {
                case PURE, SHALLOW -> (VIEW_TYPE) delegate;
                case MINIMAL -> throw new ClassCastException("Internal view error");
            };
        }

        <T> T maskIfNeeded(T t, UnaryOperator<T> factory) {
            if (t instanceof UnmodifiableView || !subViewsRequireMasking)
                return t;
            return factory.apply(t);
        }

        void voidOp(Consumer<? super VIEW_TYPE> pureImpl) {
            switch (forwardingType) {
                case PURE, SHALLOW -> pureImpl.accept(asViewType());
                case MINIMAL -> throw new IllegalStateException();
            }
        }

        void voidOp(Consumer<? super VIEW_TYPE> pureImpl, Runnable shallowImpl) {
            switch (forwardingType) {
                case PURE -> pureImpl.accept(asViewType());
                case SHALLOW, MINIMAL -> shallowImpl.run();
            }
        }

        void voidOpOrThrow(Consumer<? super VIEW_TYPE> pureImpl,
                           Supplier<? extends RuntimeException> ifMinimal) {
            switch (forwardingType) {
                case PURE, SHALLOW -> pureImpl.accept(asViewType());
                case MINIMAL -> throw ifMinimal.get();
            }
        }

        void voidOpOrThrow(Consumer<? super VIEW_TYPE> pureImpl, Runnable shallowImpl,
                           Supplier<? extends RuntimeException> ifMinimal) {
            switch (forwardingType) {
                case PURE -> pureImpl.accept(asViewType());
                case SHALLOW -> shallowImpl.run();
                case MINIMAL -> throw ifMinimal.get();
            }
        }

        boolean predicateOp(Predicate<? super VIEW_TYPE> pureImpl) {
            return switch (forwardingType) {
                case PURE, SHALLOW -> pureImpl.test(asViewType());
                case MINIMAL -> throw new IllegalStateException();
            };
        }

        boolean predicateOp(Predicate<? super VIEW_TYPE> pureImpl,
                            BooleanSupplier shallowImpl) {
            return switch (forwardingType) {
                case PURE -> pureImpl.test(asViewType());
                case SHALLOW, MINIMAL -> shallowImpl.getAsBoolean();
            };
        }

        boolean predicateOpOrThrow(Predicate<? super VIEW_TYPE> pureImpl,
                                   Supplier<? extends RuntimeException> ifMinimal) {
            return switch (forwardingType) {
                case PURE, SHALLOW -> pureImpl.test(asViewType());
                case MINIMAL -> throw ifMinimal.get();
            };
        }

        <R> R boxedOp(Function<? super VIEW_TYPE,R> pureImpl) {
            return switch (forwardingType) {
                case PURE, SHALLOW -> pureImpl.apply(asViewType());
                case MINIMAL -> throw new IllegalStateException();
            };
        }

        <R> R boxedOp(Function<? super VIEW_TYPE,R> pureImpl, Supplier<R> shallowImpl) {
            return switch (forwardingType) {
                case PURE -> pureImpl.apply(asViewType());
                case SHALLOW, MINIMAL -> shallowImpl.get();
            };
        }

        <R> R boxedOpOrThrow(Function<? super VIEW_TYPE,R> pureImpl,
                             Supplier<R> shallowImpl,
                             Supplier<? extends RuntimeException> ifMinimal) {
            return switch (forwardingType) {
                case PURE -> pureImpl.apply(asViewType());
                case SHALLOW -> shallowImpl.get();
                case MINIMAL -> throw ifMinimal.get();
            };
        }

        double doubleOp(ToDoubleFunction<? super VIEW_TYPE> pureImpl) {
            return switch (forwardingType) {
                case PURE, SHALLOW -> pureImpl.applyAsDouble(asViewType());
                case MINIMAL -> throw new IllegalStateException();
            };
        }

        int intOp(ToIntFunction<? super VIEW_TYPE> pureImpl) {
            return switch (forwardingType) {
                case PURE, SHALLOW -> pureImpl.applyAsInt(asViewType());
                case MINIMAL -> throw new IllegalStateException();
            };
        }

        int intOp(ToIntFunction<? super VIEW_TYPE> pureImpl, IntSupplier shallowImpl) {
            return switch (forwardingType) {
                case PURE -> pureImpl.applyAsInt(asViewType());
                case SHALLOW, MINIMAL -> shallowImpl.getAsInt();
            };
        }

        long longOp(ToLongFunction<? super VIEW_TYPE> pureImpl) {
            return switch (forwardingType) {
                case PURE, SHALLOW -> pureImpl.applyAsLong(asViewType());
                case MINIMAL -> throw new IllegalStateException();
            };
        }
    }

    @PrereleaseContent
    static abstract class AbstractIteratorView<E> implements Iterator<E> {
        protected abstract Forwarder<Iterator<?>, Iterator<E>> forwarder();

        @Override
        public void forEachRemaining(Consumer<? super E> action) {
            forwarder().voidOp(delegate -> delegate.forEachRemaining(action),
                    () -> Iterator.super.forEachRemaining(action));
        }

        @Override
        public boolean hasNext() {
            return forwarder().delegate.hasNext();
        }

        @SuppressWarnings("unchecked")
        private E delegateNext() {
            // Since we cannot access default methods, we bake in some logic for
            // primitive iterators at SHALLOW level of forwarding.
            var delegate = forwarder().delegate;
            return switch (delegate) {
                case PrimitiveIterator.OfDouble iterator ->
                        (E) Double.valueOf(iterator.nextDouble());
                case PrimitiveIterator.OfInt iterator ->
                        (E) Integer.valueOf(iterator.nextInt());
                case PrimitiveIterator.OfLong iterator ->
                        (E) Long.valueOf(iterator.nextLong());
                case PrimitiveIterator<?,?> ignored ->
                    throw new ClassCastException("Internal view error");
                default -> (E) delegate.next();
            };
        }

        @Override
        public E next() {
            return forwarder().boxedOp(Iterator::next, this::delegateNext);
        }

        @Override
        public void remove() {
            checkNotUnmodifiable(this);
            forwarder().delegate.remove();
        }
    }

    @PrereleaseContent
    static class IteratorView<E> extends AbstractIteratorView<E> {
        private final Forwarder<Iterator<?>,Iterator<E>> forwarder;

        IteratorView(Forwarder<Iterator<?>,Iterator<E>> forwarder) {
            this.forwarder = forwarder;
        }

        @Override
        protected Forwarder<Iterator<?>, Iterator<E>> forwarder() {
            return forwarder;
        }
    }

    @PrereleaseContent
    static class SerializableIteratorView<E> extends AbstractIteratorView<E>
            implements Serializable {
        private final Forwarder<Iterator<?>,Iterator<E>> forwarder;

        SerializableIteratorView(Forwarder<Iterator<?>,Iterator<E>> forwarder) {
            this.forwarder = forwarder;
        }

        @Override
        protected Forwarder<Iterator<?>, Iterator<E>> forwarder() {
            return forwarder;
        }
    }

    @PrereleaseContent
    static class UnmodifiableIteratorView<E> extends AbstractIteratorView<E>
            implements UnmodifiableView {
        private final Forwarder<Iterator<?>,Iterator<E>> forwarder;

        UnmodifiableIteratorView(Forwarder<Iterator<?>,Iterator<E>> forwarder) {
            this.forwarder = forwarder;
        }

        @Override
        protected Forwarder<Iterator<?>,Iterator<E>> forwarder() {
            return forwarder;
        }
    }

    @PrereleaseContent
    static class SerializableUnmodifiableIteratorView<E> extends AbstractIteratorView<E>
            implements Serializable, UnmodifiableView {
        private final Forwarder<Iterator<?>,Iterator<E>> forwarder;

        SerializableUnmodifiableIteratorView(Forwarder<Iterator<?>,Iterator<E>> forwarder) {
            this.forwarder = forwarder;
        }

        @Override
        protected Forwarder<Iterator<?>,Iterator<E>> forwarder() {
            return forwarder;
        }
    }

    @PrereleaseContent
    static abstract class AbstractDoubleIteratorView implements PrimitiveIterator.OfDouble {
        protected abstract Forwarder<Iterator<?>,PrimitiveIterator.OfDouble> forwarder();

        @Override
        public void forEachRemaining(Consumer<? super Double> action) {
            forwarder().voidOp(delegate -> delegate.forEachRemaining(action),
                    () -> OfDouble.super.forEachRemaining(action));
        }

        @Override
        public void forEachRemaining(DoubleConsumer action) {
            forwarder().voidOp(delegate -> delegate.forEachRemaining(action),
                    () -> OfDouble.super.forEachRemaining(action));
        }

        @Override
        public boolean hasNext() {
            return forwarder().delegate.hasNext();
        }

        @Override
        public Double next() {
            return forwarder().boxedOp(Iterator::next, OfDouble.super::next);
        }

        @Override
        public double nextDouble() {
            return forwarder().doubleOp(OfDouble::nextDouble);
        }

        @Override
        public void remove() {
            checkNotUnmodifiable(this);
            forwarder().delegate.remove();
        }
    }

    @PrereleaseContent
    static class DoubleIteratorView extends AbstractDoubleIteratorView {
        private final Forwarder<Iterator<?>,PrimitiveIterator.OfDouble> forwarder;

        DoubleIteratorView(Forwarder<Iterator<?>,PrimitiveIterator.OfDouble> forwarder) {
            this.forwarder = forwarder;
        }

        @Override
        protected Forwarder<Iterator<?>,PrimitiveIterator.OfDouble> forwarder() {
            return forwarder;
        }
    }

    @PrereleaseContent
    static class SerializableDoubleIteratorView extends AbstractDoubleIteratorView
            implements Serializable {
        private final Forwarder<Iterator<?>,PrimitiveIterator.OfDouble> forwarder;

        SerializableDoubleIteratorView(Forwarder<Iterator<?>,PrimitiveIterator.OfDouble> forwarder) {
            this.forwarder = forwarder;
        }

        @Override
        protected Forwarder<Iterator<?>,PrimitiveIterator.OfDouble> forwarder() {
            return forwarder;
        }
    }

    @PrereleaseContent
    static class UnmodifiableDoubleIteratorView extends AbstractDoubleIteratorView
            implements UnmodifiableView {
        private final Forwarder<Iterator<?>,PrimitiveIterator.OfDouble> forwarder;

        UnmodifiableDoubleIteratorView(Forwarder<Iterator<?>,PrimitiveIterator.OfDouble> forwarder) {
            this.forwarder = forwarder;
        }

        @Override
        protected Forwarder<Iterator<?>,PrimitiveIterator.OfDouble> forwarder() {
            return forwarder;
        }
    }

    @PrereleaseContent
    static class SerializableUnmodifiableDoubleIteratorView extends AbstractDoubleIteratorView
            implements Serializable, UnmodifiableView {
        private final Forwarder<Iterator<?>,PrimitiveIterator.OfDouble> forwarder;

        SerializableUnmodifiableDoubleIteratorView(Forwarder<Iterator<?>,PrimitiveIterator.OfDouble> forwarder) {
            this.forwarder = forwarder;
        }

        @Override
        protected Forwarder<Iterator<?>,PrimitiveIterator.OfDouble> forwarder() {
            return forwarder;
        }
    }

    @PrereleaseContent
    static abstract class AbstractIntIteratorView implements PrimitiveIterator.OfInt {
        protected abstract Forwarder<Iterator<?>,PrimitiveIterator.OfInt> forwarder();

        @Override
        public void forEachRemaining(Consumer<? super Integer> action) {
            forwarder().voidOp(delegate -> delegate.forEachRemaining(action),
                    () -> OfInt.super.forEachRemaining(action));
        }

        @Override
        public void forEachRemaining(IntConsumer action) {
            forwarder().voidOp(delegate -> delegate.forEachRemaining(action),
                    () -> OfInt.super.forEachRemaining(action));
        }

        @Override
        public boolean hasNext() {
            return forwarder().delegate.hasNext();
        }

        @Override
        public Integer next() {
            return forwarder().boxedOp(Iterator::next, OfInt.super::next);
        }

        @Override
        public int nextInt() {
            return forwarder().intOp(OfInt::nextInt);
        }

        @Override
        public void remove() {
            checkNotUnmodifiable(this);
            forwarder().delegate.remove();
        }
    }

    @PrereleaseContent
    static class IntIteratorView extends AbstractIntIteratorView {
        private final Forwarder<Iterator<?>,PrimitiveIterator.OfInt> forwarder;

        IntIteratorView(Forwarder<Iterator<?>,PrimitiveIterator.OfInt> forwarder) {
            this.forwarder = forwarder;
        }

        @Override
        protected Forwarder<Iterator<?>,PrimitiveIterator.OfInt> forwarder() {
            return forwarder;
        }
    }

    @PrereleaseContent
    static class SerializableIntIteratorView extends AbstractIntIteratorView
            implements Serializable {
        private final Forwarder<Iterator<?>,PrimitiveIterator.OfInt> forwarder;

        SerializableIntIteratorView(Forwarder<Iterator<?>,PrimitiveIterator.OfInt> forwarder) {
            this.forwarder = forwarder;
        }

        @Override
        protected Forwarder<Iterator<?>,PrimitiveIterator.OfInt> forwarder() {
            return forwarder;
        }
    }

    @PrereleaseContent
    static class UnmodifiableIntIteratorView extends AbstractIntIteratorView
            implements UnmodifiableView {
        private final Forwarder<Iterator<?>,PrimitiveIterator.OfInt> forwarder;

        UnmodifiableIntIteratorView(Forwarder<Iterator<?>,PrimitiveIterator.OfInt> forwarder) {
            this.forwarder = forwarder;
        }

        @Override
        protected Forwarder<Iterator<?>,PrimitiveIterator.OfInt> forwarder() {
            return forwarder;
        }
    }

    @PrereleaseContent
    static class SerializableUnmodifiableIntIteratorView extends AbstractIntIteratorView
            implements Serializable, UnmodifiableView {
        private final Forwarder<Iterator<?>,PrimitiveIterator.OfInt> forwarder;

        SerializableUnmodifiableIntIteratorView(Forwarder<Iterator<?>,PrimitiveIterator.OfInt> forwarder) {
            this.forwarder = forwarder;
        }

        @Override
        protected Forwarder<Iterator<?>,PrimitiveIterator.OfInt> forwarder() {
            return forwarder;
        }
    }

    @PrereleaseContent
    static abstract class AbstractLongIteratorView implements PrimitiveIterator.OfLong {
        protected abstract Forwarder<Iterator<?>,PrimitiveIterator.OfLong> forwarder();

        @Override
        public void forEachRemaining(Consumer<? super Long> action) {
            forwarder().voidOp(delegate -> delegate.forEachRemaining(action),
                    () -> OfLong.super.forEachRemaining(action));
        }

        @Override
        public void forEachRemaining(LongConsumer action) {
            forwarder().voidOp(delegate -> delegate.forEachRemaining(action),
                    () -> OfLong.super.forEachRemaining(action));
        }

        @Override
        public boolean hasNext() {
            return forwarder().delegate.hasNext();
        }

        @Override
        public Long next() {
            return forwarder().boxedOp(Iterator::next, OfLong.super::next);
        }

        @Override
        public long nextLong() {
            return forwarder().longOp(OfLong::nextLong);
        }

        @Override
        public void remove() {
            checkNotUnmodifiable(this);
            forwarder().delegate.remove();
        }
    }

    @PrereleaseContent
    static class LongIteratorView extends AbstractLongIteratorView {
        private final Forwarder<Iterator<?>,PrimitiveIterator.OfLong> forwarder;

        LongIteratorView(Forwarder<Iterator<?>,PrimitiveIterator.OfLong> forwarder) {
            this.forwarder = forwarder;
        }

        @Override
        protected Forwarder<Iterator<?>,PrimitiveIterator.OfLong> forwarder() {
            return forwarder;
        }
    }

    @PrereleaseContent
    static class SerializableLongIteratorView extends AbstractLongIteratorView
            implements Serializable {
        private final Forwarder<Iterator<?>,PrimitiveIterator.OfLong> forwarder;

        SerializableLongIteratorView(Forwarder<Iterator<?>,PrimitiveIterator.OfLong> forwarder) {
            this.forwarder = forwarder;
        }

        @Override
        protected Forwarder<Iterator<?>,PrimitiveIterator.OfLong> forwarder() {
            return forwarder;
        }
    }

    @PrereleaseContent
    static class UnmodifiableLongIteratorView extends AbstractLongIteratorView
            implements UnmodifiableView {
        private final Forwarder<Iterator<?>,PrimitiveIterator.OfLong> forwarder;

        UnmodifiableLongIteratorView(Forwarder<Iterator<?>,PrimitiveIterator.OfLong> forwarder) {
            this.forwarder = forwarder;
        }

        @Override
        protected Forwarder<Iterator<?>,PrimitiveIterator.OfLong> forwarder() {
            return forwarder;
        }
    }

    @PrereleaseContent
    static class SerializableUnmodifiableLongIteratorView extends AbstractLongIteratorView
            implements Serializable, UnmodifiableView {
        private final Forwarder<Iterator<?>,PrimitiveIterator.OfLong> forwarder;

        SerializableUnmodifiableLongIteratorView(Forwarder<Iterator<?>,PrimitiveIterator.OfLong> forwarder) {
            this.forwarder = forwarder;
        }

        @Override
        protected Forwarder<Iterator<?>,PrimitiveIterator.OfLong> forwarder() {
            return forwarder;
        }
    }

    @PrereleaseContent
    static abstract class AbstractCollectionView<E> implements Collection<E> {
        protected abstract Forwarder<Collection<?>,Collection<E>> forwarder();

        @Override
        public boolean add(E e) {
            checkNotUnmodifiable(this);
            return forwarder().predicateOp(delegate -> delegate.add(e));
        }

        @Override
        public boolean addAll(Collection<? extends E> collection) {
            checkNotUnmodifiable(this);
            return forwarder().predicateOp(delegate ->
                    delegate.addAll(collection));
        }

        @Override
        public void clear() {
            checkNotUnmodifiable(this);
            forwarder().delegate.clear();
        }

        @Override
        public boolean contains(Object o) {
            return forwarder().predicateOp(delegate ->
                    delegate.contains(o));
        }

        @Override
        public boolean containsAll(Collection<?> collection) {
            return forwarder().predicateOp(delegate ->
                    delegate.containsAll(collection));
        }

        @Override
        public boolean equals(Object o) {
            return forwarder().predicateOp(delegate -> delegate.equals(o),
                    () -> super.equals(o));
        }

        @Override
        public void forEach(Consumer<? super E> action) {
            forwarder().voidOp(delegate -> delegate.forEach(action),
                    () -> Collection.super.forEach(action));
        }

        @Override
        public int hashCode() {
            return forwarder().intOp(Collection::hashCode, super::hashCode);
        }

        @Override
        public boolean isEmpty() {
            return forwarder().delegate.isEmpty();
        }

        @Override
        public Iterator<E> iterator() {
            return forwarder().maskIfNeeded(forwarder().boxedOp(Collection::iterator),
                    Views::unmodifiableIteratorView);
        }

        @Override
        public Stream<E> parallelStream() {
            return forwarder().boxedOp(Collection::parallelStream,
                    Collection.super::parallelStream);
        }

        @Override
        public boolean remove(Object o) {
            checkNotUnmodifiable(this);
            return forwarder().predicateOp(delegate -> delegate.remove(o));
        }

        @Override
        public boolean removeAll(Collection<?> collection) {
            checkNotUnmodifiable(this);
            return forwarder().predicateOp(delegate ->
                    delegate.removeAll(collection));
        }

        @Override
        public boolean removeIf(Predicate<? super E> filter) {
            checkNotUnmodifiable(this);
            return forwarder().predicateOp(delegate ->
                            delegate.removeIf(filter),
                    () -> Collection.super.removeIf(filter));
        }

        @Override
        public boolean retainAll(Collection<?> collection) {
            checkNotUnmodifiable(this);
            return forwarder().predicateOp(delegate ->
                    delegate.retainAll(collection));
        }

        @Override
        public int size() {
            return forwarder().delegate.size();
        }

        @Override
        public Spliterator<E> spliterator() {
            return forwarder().boxedOp(Collection::spliterator,
                    Collection.super::spliterator);
        }

        @Override
        public Stream<E> stream() {
            return forwarder().boxedOp(Collection::stream,
                    Collection.super::stream);
        }

        @Override
        public Object[] toArray() {
            return forwarder().boxedOp(Collection::toArray);
        }

        @Override
        public <T> T[] toArray(IntFunction<T[]> generator) {
            return forwarder().boxedOp(
                    delegate -> delegate.toArray(generator),
                    () -> Collection.super.toArray(generator));
        }

        @Override
        public <T> T[] toArray(T[] a) {
            return forwarder().boxedOp(delegate -> delegate.toArray(a));
        }
    }

    @PrereleaseContent
    static class CollectionView<E> extends AbstractCollectionView<E> {
        private final Forwarder<Collection<?>,Collection<E>> forwarder;

        CollectionView(Forwarder<Collection<?>,Collection<E>> forwarder) {
            this.forwarder = forwarder;
        }

        @Override
        protected Forwarder<Collection<?>,Collection<E>> forwarder() {
            return forwarder;
        }
    }

    @PrereleaseContent
    static class SerializableCollectionView<E> extends AbstractCollectionView<E>
            implements Serializable {
        private final Forwarder<Collection<?>,Collection<E>> forwarder;

        SerializableCollectionView(Forwarder<Collection<?>,Collection<E>> forwarder) {
            this.forwarder = forwarder;
        }

        @Override
        protected Forwarder<Collection<?>,Collection<E>> forwarder() {
            return forwarder;
        }
    }

    @PrereleaseContent
    static class UnmodifiableCollectionView<E> extends AbstractCollectionView<E>
            implements UnmodifiableView {
        private final Forwarder<Collection<?>,Collection<E>> forwarder;

        UnmodifiableCollectionView(Forwarder<Collection<?>,Collection<E>> forwarder) {
            this.forwarder = forwarder;
        }

        @Override
        protected Forwarder<Collection<?>,Collection<E>> forwarder() {
            return forwarder;
        }
    }

    @PrereleaseContent
    static class SerializableUnmodifiableCollectionView<E> extends AbstractCollectionView<E>
            implements Serializable, UnmodifiableView {
        private final Forwarder<Collection<?>,Collection<E>> forwarder;

        SerializableUnmodifiableCollectionView(Forwarder<Collection<?>,Collection<E>> forwarder) {
            this.forwarder = forwarder;
        }

        @Override
        protected Forwarder<Collection<?>,Collection<E>> forwarder() {
            return forwarder;
        }
    }

    @PrereleaseContent
    static abstract class PrimitiveCollectionView<T,T_ARR,T_CONS,T_PRED,
            T_SPLITR extends Spliterator.OfPrimitive<T,T_CONS,T_SPLITR>,
            T_STR extends BaseStream<T,T_STR>,
            T_COLL extends PrimitiveCollection<T,T_ARR,T_CONS,T_PRED,T_SPLITR,T_STR,T_COLL>>
            implements PrimitiveCollection<T,T_ARR,T_CONS,T_PRED,T_SPLITR,T_STR,T_COLL> {
        protected abstract Forwarder<Collection<?>,T_COLL> forwarder();

        @Override
        public void clear() {
            checkNotUnmodifiable(this);
            forwarder().voidOp(Collection::clear, PrimitiveCollection.super::clear);
        }

        @Override
        public boolean isEmpty() {
            return forwarder().predicateOp(Collection::isEmpty,
                    PrimitiveCollection.super::isEmpty);
        }

        @Override
        public Stream<T> parallelStream() {
            return forwarder().boxedOp(Collection::parallelStream,
                    PrimitiveCollection.super::parallelStream);
        }

        @Override
        public int size() {
            return forwarder().delegate.size();
        }

        @Override
        public Stream<T> stream() {
            return forwarder().boxedOp(Collection::stream,
                    PrimitiveCollection.super::stream);
        }

        @Override
        public Object[] toArray() {
            return forwarder().boxedOp(Collection::toArray,
                    PrimitiveCollection.super::toArray);
        }

        @Override
        public <U> U[] toArray(IntFunction<U[]> generator) {
            return forwarder().boxedOp(delegate -> delegate.toArray(generator),
                    () -> PrimitiveCollection.super.toArray(generator));
        }

        @Override
        public <U> U[] toArray(U[] a) {
            return forwarder().boxedOp(delegate -> delegate.toArray(a),
                    () -> PrimitiveCollection.super.toArray(a));
        }
    }

    @PrereleaseContent
    static abstract class AbstractDoubleCollectionView
            extends PrimitiveCollectionView<Double, double[], DoubleConsumer,
            DoublePredicate, Spliterator.OfDouble, DoubleStream,
            PrimitiveCollection.OfDouble> implements PrimitiveCollection.OfDouble {
        @Override
        public boolean add(Double t) {
            checkNotUnmodifiable(this);
            return forwarder().predicateOp(delegate -> delegate.add(t),
                    () -> OfDouble.super.add(t));
        }

        @Override
        public boolean addAll(Collection<? extends Double> collection) {
            checkNotUnmodifiable(this);
            return forwarder().predicateOp(delegate ->
                            delegate.addAll(collection),
                    () -> OfDouble.super.addAll(collection));
        }

        @Override
        public boolean addAll(OfDouble collection) {
            checkNotUnmodifiable(this);
            return forwarder().predicateOp(delegate ->
                            delegate.addAll(collection),
                    () -> OfDouble.super.addAll(collection));
        }

        @Override
        public boolean addDouble(double d) {
            checkNotUnmodifiable(this);
            return forwarder().predicateOp(delegate -> delegate.addDouble(d),
                    () -> OfDouble.super.containsDouble(d));
        }

        @Override
        public boolean contains(Object o) {
            return forwarder().predicateOp(delegate -> delegate.contains(o),
                    () -> OfDouble.super.contains(o));
        }

        @Override
        public boolean containsAll(Collection<?> collection) {
            return forwarder().predicateOp(delegate ->
                            delegate.containsAll(collection),
                    () -> OfDouble.super.containsAll(collection));
        }

        @Override
        public boolean containsAll(OfDouble collection) {
            return forwarder().predicateOp(delegate ->
                    delegate.containsAll(collection),
                    () -> OfDouble.super.containsAll(collection));
        }

        @Override
        public boolean containsDouble(double d) {
            return forwarder().predicateOp(delegate ->
                            delegate.containsDouble(d),
                    () -> OfDouble.super.containsDouble(d));
        }

        @Override
        public PrimitiveIterator.OfDouble iterator() {
            return forwarder().maskIfNeeded(forwarder().boxedOp(OfDouble::iterator),
                    Views::unmodifiableDoubleIteratorView);
        }

        @Override
        public DoubleStream parallelPrimitiveStream() {
            return forwarder().boxedOp(OfDouble::parallelPrimitiveStream,
                    OfDouble.super::parallelPrimitiveStream);
        }

        @Override
        public DoubleStream primitiveStream() {
            return forwarder().boxedOp(OfDouble::primitiveStream,
                    OfDouble.super::primitiveStream);
        }

        @Override
        public boolean remove(Object o) {
            checkNotUnmodifiable(this);
            return forwarder().predicateOp(delegate -> delegate.remove(o),
                    () -> OfDouble.super.remove(o));
        }

        @Override
        public boolean removeAll(Collection<?> collection) {
            checkNotUnmodifiable(this);
            return forwarder().predicateOp(delegate ->
                            delegate.removeAll(collection),
                    () -> OfDouble.super.removeAll(collection));
        }

        @Override
        public boolean removeAll(OfDouble collection) {
            checkNotUnmodifiable(this);
            return forwarder().predicateOp(delegate ->
                            delegate.removeAll(collection),
                    () -> OfDouble.super.removeAll(collection));
        }

        @Override
        public boolean removeDouble(double d) {
            checkNotUnmodifiable(this);
            return forwarder().predicateOp(delegate ->
                            delegate.removeDouble(d),
                    () -> OfDouble.super.removeDouble(d));
        }

        @Override
        public boolean removeIf(DoublePredicate filter) {
            checkNotUnmodifiable(this);
            return forwarder().predicateOp(delegate ->
                            delegate.removeIf(filter),
                    () -> OfDouble.super.removeIf(filter));
        }

        @Override
        public boolean removeIf(Predicate<? super Double> filter) {
            checkNotUnmodifiable(this);
            return forwarder().predicateOp(delegate ->
                            delegate.removeIf(filter),
                    () -> OfDouble.super.removeIf(filter));
        }

        @Override
        public boolean removeIfDouble(DoublePredicate filter) {
            checkNotUnmodifiable(this);
            return forwarder().predicateOp(delegate ->
                            delegate.removeIfDouble(filter),
                    () -> OfDouble.super.removeIfDouble(filter));
        }

        @Override
        public boolean retainAll(Collection<?> collection) {
            checkNotUnmodifiable(this);
            return forwarder().predicateOp(delegate ->
                            delegate.retainAll(collection),
                    () -> OfDouble.super.retainAll(collection));
        }

        @Override
        public boolean retainAll(OfDouble collection) {
            checkNotUnmodifiable(this);
            return forwarder().predicateOp(delegate ->
                            delegate.retainAll(collection),
                    () -> OfDouble.super.retainAll(collection));
        }

        @Override
        public Spliterator.OfDouble spliterator() {
            return forwarder().boxedOp(OfDouble::spliterator,
                    OfDouble.super::spliterator);
        }

        @Override
        public double[] toPrimitiveArray() {
            return forwarder().boxedOp(OfDouble::toPrimitiveArray,
                    OfDouble.super::toPrimitiveArray);
        }
    }

    @PrereleaseContent
    static class DoubleCollectionView extends AbstractDoubleCollectionView {
        private final Forwarder<Collection<?>,PrimitiveCollection.OfDouble> forwarder;

        DoubleCollectionView(Forwarder<Collection<?>,PrimitiveCollection.OfDouble> forwarder) {
            this.forwarder = forwarder;
        }

        @Override
        public Forwarder<Collection<?>,PrimitiveCollection.OfDouble> forwarder() {
            return forwarder;
        }
    }

    @PrereleaseContent
    static class SerializableDoubleCollectionView extends AbstractDoubleCollectionView
            implements Serializable {
        private final Forwarder<Collection<?>,PrimitiveCollection.OfDouble> forwarder;

        SerializableDoubleCollectionView(Forwarder<Collection<?>,PrimitiveCollection.OfDouble> forwarder) {
            this.forwarder = forwarder;
        }

        @Override
        public Forwarder<Collection<?>,PrimitiveCollection.OfDouble> forwarder() {
            return forwarder;
        }
    }

    @PrereleaseContent
    static class UnmodifiableDoubleCollectionView extends AbstractDoubleCollectionView
            implements UnmodifiableView {
        private final Forwarder<Collection<?>,PrimitiveCollection.OfDouble> forwarder;

        UnmodifiableDoubleCollectionView(Forwarder<Collection<?>,PrimitiveCollection.OfDouble> forwarder) {
            this.forwarder = forwarder;
        }

        @Override
        public Forwarder<Collection<?>,PrimitiveCollection.OfDouble> forwarder() {
            return forwarder;
        }
    }

    @PrereleaseContent
    static class SerializableUnmodifiableDoubleCollectionView
            extends AbstractDoubleCollectionView
            implements Serializable, UnmodifiableView {
        private final Forwarder<Collection<?>,PrimitiveCollection.OfDouble> forwarder;

        SerializableUnmodifiableDoubleCollectionView(Forwarder<Collection<?>,PrimitiveCollection.OfDouble> forwarder) {
            this.forwarder = forwarder;
        }

        @Override
        public Forwarder<Collection<?>,PrimitiveCollection.OfDouble> forwarder() {
            return forwarder;
        }
    }

    @PrereleaseContent
    static abstract class AbstractIntCollectionView
            extends PrimitiveCollectionView<Integer, int[], IntConsumer,
            IntPredicate, Spliterator.OfInt, IntStream, PrimitiveCollection.OfInt>
            implements PrimitiveCollection.OfInt {
        @Override
        public boolean add(Integer t) {
            checkNotUnmodifiable(this);
            return forwarder().predicateOp(delegate -> delegate.add(t),
                    () -> OfInt.super.add(t));
        }

        @Override
        public boolean addAll(Collection<? extends Integer> collection) {
            checkNotUnmodifiable(this);
            return forwarder().predicateOp(delegate -> delegate.addAll(collection),
                    () -> OfInt.super.addAll(collection));
        }

        @Override
        public boolean addAll(OfInt collection) {
            checkNotUnmodifiable(this);
            return forwarder().predicateOp(delegate -> delegate.addAll(collection),
                    () -> OfInt.super.addAll(collection));
        }

        @Override
        public boolean addInt(int i) {
            checkNotUnmodifiable(this);
            return forwarder().predicateOp(delegate -> delegate.addInt(i),
                    () -> OfInt.super.addInt(i));
        }

        @Override
        public boolean contains(Object o) {
            return forwarder().predicateOp(delegate -> delegate.contains(o),
                    () -> OfInt.super.contains(o));
        }

        @Override
        public boolean containsAll(Collection<?> collection) {
            return forwarder().predicateOp(delegate -> delegate.containsAll(collection),
                    () -> OfInt.super.containsAll(collection));
        }

        @Override
        public boolean containsAll(OfInt collection) {
            return forwarder().predicateOp(delegate -> delegate.containsAll(collection),
                    () -> OfInt.super.containsAll(collection));
        }

        @Override
        public boolean containsInt(int i) {
            return forwarder().predicateOp(delegate -> delegate.containsInt(i),
                    () -> OfInt.super.containsInt(i));
        }

        @Override
        public PrimitiveIterator.OfInt iterator() {
            return forwarder().maskIfNeeded(forwarder().boxedOp(OfInt::iterator),
                    Views::unmodifiableIntIteratorView);
        }

        @Override
        public IntStream parallelPrimitiveStream() {
            return forwarder().boxedOp(OfInt::parallelPrimitiveStream,
                    OfInt.super::parallelPrimitiveStream);
        }

        @Override
        public IntStream primitiveStream() {
            return forwarder().boxedOp(OfInt::primitiveStream, OfInt.super::primitiveStream);
        }

        @Override
        public boolean remove(Object o) {
            checkNotUnmodifiable(this);
            return forwarder().predicateOp(delegate -> delegate.remove(o),
                    () -> OfInt.super.remove(o));
        }

        @Override
        public boolean removeAll(Collection<?> collection) {
            checkNotUnmodifiable(this);
            return forwarder().predicateOp(delegate -> delegate.removeAll(collection),
                    () -> OfInt.super.removeAll(collection));
        }

        @Override
        public boolean removeAll(OfInt collection) {
            checkNotUnmodifiable(this);
            return forwarder().predicateOp(delegate -> delegate.removeAll(collection),
                    () -> OfInt.super.removeAll(collection));
        }

        @Override
        public boolean removeIf(IntPredicate filter) {
            checkNotUnmodifiable(this);
            return forwarder().predicateOp(delegate -> delegate.removeIf(filter),
                    () -> OfInt.super.removeIf(filter));
        }

        @Override
        public boolean removeIf(Predicate<? super Integer> filter) {
            checkNotUnmodifiable(this);
            return forwarder().predicateOp(delegate -> delegate.removeIf(filter),
                    () -> OfInt.super.removeIf(filter));
        }

        @Override
        public boolean removeIfInt(IntPredicate filter) {
            checkNotUnmodifiable(this);
            return forwarder().predicateOp(delegate -> delegate.removeIfInt(filter),
                    () -> OfInt.super.removeIfInt(filter));
        }

        @Override
        public boolean removeInt(int i) {
            checkNotUnmodifiable(this);
            return forwarder().predicateOp(delegate -> delegate.removeInt(i),
                    () -> OfInt.super.removeInt(i));
        }

        @Override
        public boolean retainAll(Collection<?> collection) {
            checkNotUnmodifiable(this);
            return forwarder().predicateOp(delegate -> delegate.retainAll(collection),
                    () -> OfInt.super.retainAll(collection));
        }

        @Override
        public boolean retainAll(OfInt collection) {
            checkNotUnmodifiable(this);
            return forwarder().predicateOp(delegate -> delegate.retainAll(collection),
                    () -> OfInt.super.retainAll(collection));
        }

        @Override
        public Spliterator.OfInt spliterator() {
            return forwarder().boxedOp(OfInt::spliterator, OfInt.super::spliterator);
        }

        @Override
        public int[] toPrimitiveArray() {
            return forwarder().boxedOp(OfInt::toPrimitiveArray,
                    OfInt.super::toPrimitiveArray);
        }
    }

    @PrereleaseContent
    static class IntCollectionView extends AbstractIntCollectionView {
        private final Forwarder<Collection<?>,PrimitiveCollection.OfInt> forwarder;

        IntCollectionView(Forwarder<Collection<?>,PrimitiveCollection.OfInt> forwarder) {
            this.forwarder = forwarder;
        }

        @Override
        protected Forwarder<Collection<?>,PrimitiveCollection.OfInt> forwarder() {
            return forwarder;
        }
    }

    @PrereleaseContent
    static class SerializableIntCollectionView extends AbstractIntCollectionView
            implements Serializable {
        private final Forwarder<Collection<?>,PrimitiveCollection.OfInt> forwarder;

        SerializableIntCollectionView(Forwarder<Collection<?>,PrimitiveCollection.OfInt> forwarder) {
            this.forwarder = forwarder;
        }

        @Override
        protected Forwarder<Collection<?>,PrimitiveCollection.OfInt> forwarder() {
            return forwarder;
        }
    }

    @PrereleaseContent
    static class UnmodifiableIntCollectionView extends AbstractIntCollectionView
            implements UnmodifiableView {
        private final Forwarder<Collection<?>,PrimitiveCollection.OfInt> forwarder;

        UnmodifiableIntCollectionView(Forwarder<Collection<?>,PrimitiveCollection.OfInt> forwarder) {
            this.forwarder = forwarder;
        }

        @Override
        protected Forwarder<Collection<?>,PrimitiveCollection.OfInt> forwarder() {
            return forwarder;
        }
    }

    @PrereleaseContent
    static class SerializableUnmodifiableIntCollectionView
            extends AbstractIntCollectionView implements Serializable, UnmodifiableView {
        private final Forwarder<Collection<?>,PrimitiveCollection.OfInt> forwarder;

        SerializableUnmodifiableIntCollectionView(Forwarder<Collection<?>,PrimitiveCollection.OfInt> forwarder) {
            this.forwarder = forwarder;
        }

        @Override
        protected Forwarder<Collection<?>,PrimitiveCollection.OfInt> forwarder() {
            return forwarder;
        }
    }

    @PrereleaseContent
    static abstract class AbstractLongCollectionView extends PrimitiveCollectionView<Long,
            long[], LongConsumer, LongPredicate, Spliterator.OfLong, LongStream,
            PrimitiveCollection.OfLong> implements PrimitiveCollection.OfLong {
        @Override
        public boolean add(Long t) {
            checkNotUnmodifiable(this);
            return forwarder().predicateOp(delegate -> delegate.add(t),
                    () -> OfLong.super.add(t));
        }

        @Override
        public boolean addAll(Collection<? extends Long> collection) {
            checkNotUnmodifiable(this);
            return forwarder().predicateOp(delegate -> delegate.addAll(collection),
                    () -> OfLong.super.addAll(collection));
        }

        @Override
        public boolean addAll(OfLong collection) {
            checkNotUnmodifiable(this);
            return forwarder().predicateOp(delegate -> delegate.addAll(collection),
                    () -> OfLong.super.addAll(collection));
        }

        @Override
        public boolean addLong(long l) {
            checkNotUnmodifiable(this);
            return forwarder().predicateOp(delegate -> delegate.addLong(l),
                    () -> OfLong.super.addLong(l));
        }

        @Override
        public boolean contains(Object o) {
            return forwarder().predicateOp(delegate -> delegate.contains(o),
                    () -> OfLong.super.contains(o));
        }

        @Override
        public boolean containsAll(Collection<?> collection) {
            return forwarder().predicateOp(delegate ->
                            delegate.containsAll(collection),
                    () -> OfLong.super.containsAll(collection));
        }

        @Override
        public boolean containsAll(OfLong collection) {
            return forwarder().predicateOp(delegate ->
                            delegate.containsAll(collection),
                    () -> OfLong.super.containsAll(collection));
        }

        @Override
        public boolean containsLong(long l) {
            return forwarder().predicateOp(delegate -> delegate.containsLong(l),
                    () -> OfLong.super.containsLong(l));
        }

        @Override
        public PrimitiveIterator.OfLong iterator() {
            return forwarder().maskIfNeeded(forwarder().boxedOp(OfLong::iterator),
                    Views::unmodifiableLongIteratorView);
        }

        @Override
        public LongStream parallelPrimitiveStream() {
            return forwarder().boxedOp(OfLong::parallelPrimitiveStream,
                    OfLong.super::parallelPrimitiveStream);
        }

        @Override
        public LongStream primitiveStream() {
            return forwarder().boxedOp(OfLong::primitiveStream,
                    OfLong.super::primitiveStream);
        }

        @Override
        public boolean remove(Object o) {
            checkNotUnmodifiable(this);
            return forwarder().predicateOp(delegate -> delegate.remove(o),
                    () -> OfLong.super.remove(o));
        }

        @Override
        public boolean removeAll(Collection<?> collection) {
            checkNotUnmodifiable(this);
            return forwarder().predicateOp(delegate -> delegate.removeAll(collection),
                    () -> OfLong.super.removeAll(collection));
        }

        @Override
        public boolean removeAll(OfLong collection) {
            checkNotUnmodifiable(this);
            return forwarder().predicateOp(delegate -> delegate.removeAll(collection),
                    () -> OfLong.super.removeAll(collection));
        }

        @Override
        public boolean removeIf(LongPredicate filter) {
            checkNotUnmodifiable(this);
            return forwarder().predicateOp(delegate -> delegate.removeIf(filter),
                    () -> OfLong.super.removeIf(filter));
        }

        @Override
        public boolean removeIf(Predicate<? super Long> filter) {
            checkNotUnmodifiable(this);
            return forwarder().predicateOp(delegate -> delegate.removeIf(filter),
                    () -> OfLong.super.removeIf(filter));
        }

        @Override
        public boolean removeIfLong(LongPredicate filter) {
            checkNotUnmodifiable(this);
            return forwarder().predicateOp(delegate -> delegate.removeIfLong(filter),
                    () -> OfLong.super.removeIfLong(filter));
        }

        @Override
        public boolean removeLong(long l) {
            checkNotUnmodifiable(this);
            return forwarder().predicateOp(delegate -> delegate.removeLong(l),
                    () -> OfLong.super.removeLong(l));
        }

        @Override
        public boolean retainAll(Collection<?> collection) {
            checkNotUnmodifiable(this);
            return forwarder().predicateOp(delegate ->
                    delegate.retainAll(collection),
                    () -> OfLong.super.retainAll(collection));
        }

        @Override
        public boolean retainAll(OfLong collection) {
            checkNotUnmodifiable(this);
            return forwarder().predicateOp(delegate ->
                            delegate.retainAll(collection),
                    () -> OfLong.super.retainAll(collection));
        }

        @Override
        public Spliterator.OfLong spliterator() {
            return forwarder().boxedOp(OfLong::spliterator, OfLong.super::spliterator);
        }

        @Override
        public long[] toPrimitiveArray() {
            return forwarder().boxedOp(OfLong::toPrimitiveArray,
                    OfLong.super::toPrimitiveArray);
        }
    }

    @PrereleaseContent
    static class LongCollectionView extends AbstractLongCollectionView {
        private final Forwarder<Collection<?>,PrimitiveCollection.OfLong> forwarder;

        LongCollectionView(Forwarder<Collection<?>,PrimitiveCollection.OfLong> forwarder) {
            this.forwarder = forwarder;
        }

        @Override
        protected Forwarder<Collection<?>,PrimitiveCollection.OfLong> forwarder() {
            return forwarder;
        }
    }

    @PrereleaseContent
    static class SerializableLongCollectionView extends AbstractLongCollectionView
            implements Serializable {
        private final Forwarder<Collection<?>,PrimitiveCollection.OfLong> forwarder;

        SerializableLongCollectionView(Forwarder<Collection<?>,PrimitiveCollection.OfLong> forwarder) {
            this.forwarder = forwarder;
        }

        @Override
        protected Forwarder<Collection<?>,PrimitiveCollection.OfLong> forwarder() {
            return forwarder;
        }
    }

    @PrereleaseContent
    static class UnmodifiableLongCollectionView extends AbstractLongCollectionView
            implements UnmodifiableView {
        private final Forwarder<Collection<?>,PrimitiveCollection.OfLong> forwarder;

        UnmodifiableLongCollectionView(Forwarder<Collection<?>,PrimitiveCollection.OfLong> forwarder) {
            this.forwarder = forwarder;
        }

        @Override
        protected Forwarder<Collection<?>,PrimitiveCollection.OfLong> forwarder() {
            return forwarder;
        }
    }

    @PrereleaseContent
    static class SerializableUnmodifiableLongCollectionView
            extends AbstractLongCollectionView implements Serializable, UnmodifiableView {
        private final Forwarder<Collection<?>,PrimitiveCollection.OfLong> forwarder;

        SerializableUnmodifiableLongCollectionView(Forwarder<Collection<?>,PrimitiveCollection.OfLong> forwarder) {
            this.forwarder = forwarder;
        }

        @Override
        protected Forwarder<Collection<?>,PrimitiveCollection.OfLong> forwarder() {
            return forwarder;
        }
    }

    @PrereleaseContent
    static abstract class AbstractSetView<E> implements Set<E> {
        protected abstract Forwarder<Set<?>,Set<E>> forwarder();

        @Override
        public boolean add(E e) {
            checkNotUnmodifiable(this);
            return forwarder().predicateOp(delegate -> delegate.add(e));
        }

        @Override
        public boolean addAll(Collection<? extends E> collection) {
            checkNotUnmodifiable(this);
            return forwarder().predicateOp(delegate ->
                    delegate.addAll(collection));
        }

        @Override
        public void clear() {
            checkNotUnmodifiable(this);
            forwarder().delegate.clear();
        }

        @Override
        public boolean contains(Object o) {
            return forwarder().predicateOp(delegate -> delegate.contains(o));
        }

        @Override
        public boolean containsAll(Collection<?> collection) {
            return forwarder().predicateOp(delegate ->
                    delegate.containsAll(collection));
        }

        @Override
        @SuppressWarnings("EqualsDoesntCheckParameterClass")
        public boolean equals(Object o) {
            return forwarder().delegate.equals(o);
        }

        @Override
        public void forEach(Consumer<? super E> action) {
            forwarder().voidOp(delegate -> delegate.forEach(action),
                    () -> Set.super.forEach(action));
        }

        @Override
        public int hashCode() {
            return forwarder().delegate.hashCode();
        }

        @Override
        public boolean isEmpty() {
            return forwarder().delegate.isEmpty();
        }

        @Override
        public Iterator<E> iterator() {
            return forwarder().maskIfNeeded(forwarder().boxedOp(Set::iterator),
                    Views::unmodifiableIteratorView);
        }

        @Override
        public boolean remove(Object o) {
            checkNotUnmodifiable(this);
            return forwarder().predicateOp(delegate -> delegate.remove(o));
        }

        @Override
        public boolean removeAll(Collection<?> collection) {
            checkNotUnmodifiable(this);
            return forwarder().predicateOp(delegate ->
                    delegate.removeAll(collection));
        }

        @Override
        public boolean removeIf(Predicate<? super E> filter) {
            checkNotUnmodifiable(this);
            return forwarder().predicateOp(delegate -> delegate.removeIf(filter),
                    () -> Set.super.removeIf(filter));
        }

        @Override
        public boolean retainAll(Collection<?> collection) {
            checkNotUnmodifiable(this);
            return forwarder().predicateOp(delegate ->
                    delegate.retainAll(collection));
        }

        @Override
        public int size() {
            return forwarder().delegate.size();
        }

        @Override
        public Spliterator<E> spliterator() {
            return forwarder().boxedOp(Set::spliterator);
        }

        @Override
        public Object[] toArray() {
            return forwarder().boxedOp(Set::toArray);
        }

        @Override
        public <T> T[] toArray(IntFunction<T[]> generator) {
            return forwarder().boxedOp(delegate -> delegate.toArray(generator),
                    () -> Set.super.toArray(generator));
        }

        @Override
        public <T> T[] toArray(T[] a) {
            return forwarder().boxedOp(delegate -> delegate.toArray(a));
        }
    }

    @PrereleaseContent
    static class SetView<E> extends AbstractSetView<E> {
        private final Forwarder<Set<?>,Set<E>> forwarder;

        SetView(Forwarder<Set<?>,Set<E>> forwarder) {
            this.forwarder = forwarder;
        }

        @Override
        protected Forwarder<Set<?>,Set<E>> forwarder() {
            return forwarder;
        }
    }

    @PrereleaseContent
    static class SerializableSetView<E> extends AbstractSetView<E>
            implements Serializable {
        private final Forwarder<Set<?>,Set<E>> forwarder;

        SerializableSetView(Forwarder<Set<?>,Set<E>> forwarder) {
            this.forwarder = forwarder;
        }

        @Override
        protected Forwarder<Set<?>,Set<E>> forwarder() {
            return forwarder;
        }
    }

    @PrereleaseContent
    static class UnmodifiableSetView<E> extends AbstractSetView<E>
            implements UnmodifiableView {
        private final Forwarder<Set<?>,Set<E>> forwarder;

        UnmodifiableSetView(Forwarder<Set<?>,Set<E>> forwarder) {
            this.forwarder = forwarder;
        }

        @Override
        protected Forwarder<Set<?>,Set<E>> forwarder() {
            return forwarder;
        }
    }

    @PrereleaseContent
    static class SerializableUnmodifiableSetView<E> extends AbstractSetView<E>
            implements Serializable, UnmodifiableView {
        private final Forwarder<Set<?>,Set<E>> forwarder;

        SerializableUnmodifiableSetView(Forwarder<Set<?>,Set<E>> forwarder) {
            this.forwarder = forwarder;
        }

        @Override
        protected Forwarder<Set<?>,Set<E>> forwarder() {
            return forwarder;
        }
    }

    @PrereleaseContent
    static abstract class PrimitiveSetView<T,T_ARR,T_CONS,T_PRED,
            T_SPLITR extends Spliterator.OfPrimitive<T,T_CONS,T_SPLITR>,
            T_STR extends BaseStream<T,T_STR>,
            T_COLL extends PrimitiveCollection<T,T_ARR,T_CONS,T_PRED,T_SPLITR,T_STR,T_COLL>,
            T_SET extends PrimitiveSet<T,T_ARR,T_CONS,T_PRED,T_SPLITR,T_STR,T_COLL>>
            implements PrimitiveSet<T,T_ARR,T_CONS,T_PRED,T_SPLITR,T_STR,T_COLL> {
        protected abstract Forwarder<Set<?>,T_SET> forwarder();

        @Override
        public void clear() {
            checkNotUnmodifiable(this);
            forwarder().voidOp(Set::clear, PrimitiveSet.super::clear);
        }

        @Override
        public void forEach(Consumer<? super T> action) {
            forwarder().voidOp(delegate -> delegate.forEach(action),
                    () -> PrimitiveSet.super.forEach(action));
        }

        @Override
        public void forEach(T_CONS action) {
            forwarder().voidOp(delegate -> delegate.forEach(action),
                    () -> PrimitiveSet.super.forEach(action));
        }

        @Override
        public boolean isEmpty() {
            return forwarder().predicateOp(Set::isEmpty, PrimitiveSet.super::isEmpty);
        }

        @Override
        public Stream<T> parallelStream() {
            return forwarder().boxedOp(Set::parallelStream,
                    PrimitiveSet.super::parallelStream);
        }

        @Override
        public int size() {
            return forwarder().delegate.size();
        }

        @Override
        public Stream<T> stream() {
            return forwarder().boxedOp(Set::stream, PrimitiveSet.super::stream);
        }

        @Override
        public Object[] toArray() {
            return forwarder().boxedOp(Set::toArray, PrimitiveSet.super::toArray);
        }

        @Override
        public <U> U[] toArray(IntFunction<U[]> generator) {
            return forwarder().boxedOp(delegate -> delegate.toArray(generator),
                    () -> PrimitiveSet.super.toArray(generator));
        }

        @Override
        public <U> U[] toArray(U[] a) {
            return forwarder().boxedOp(delegate -> delegate.toArray(a),
                    () -> PrimitiveSet.super.toArray(a));
        }
    }

    @PrereleaseContent
    static abstract class AbstractDoubleSetView extends PrimitiveSetView<Double,
            double[],DoubleConsumer,DoublePredicate,Spliterator.OfDouble,DoubleStream,
            PrimitiveCollection.OfDouble,PrimitiveSet.OfDouble>
            implements PrimitiveSet.OfDouble {
        @Override
        public boolean add(Double t) {
            checkNotUnmodifiable(this);
            return forwarder().predicateOp(delegate -> delegate.add(t));
        }

        @Override
        public boolean addAll(Collection<? extends Double> collection) {
            checkNotUnmodifiable(this);
            return forwarder().predicateOp(delegate ->
                            delegate.addAll(collection),
                    () -> OfDouble.super.addAll(collection));
        }

        @Override
        public boolean addAll(PrimitiveCollection.OfDouble collection) {
            checkNotUnmodifiable(this);
            return forwarder().predicateOp(delegate ->
                            delegate.addAll(collection),
                    () -> OfDouble.super.addAll(collection));
        }

        @Override
        public boolean addDouble(double d) {
            checkNotUnmodifiable(this);
            return forwarder().predicateOp(delegate -> delegate.addDouble(d),
                    () -> OfDouble.super.addDouble(d));
        }

        @Override
        public boolean contains(Object o) {
            return forwarder().predicateOp(delegate -> delegate.contains(o),
                    () -> OfDouble.super.contains(o));
        }

        @Override
        public boolean containsAll(Collection<?> collection) {
            return forwarder().predicateOp(delegate ->
                            delegate.containsAll(collection),
                    () -> OfDouble.super.containsAll(collection));
        }

        @Override
        public boolean containsAll(PrimitiveCollection.OfDouble collection) {
            return forwarder().predicateOp(delegate ->
                            delegate.containsAll(collection),
                    () -> OfDouble.super.containsAll(collection));
        }

        @Override
        public boolean containsDouble(double d) {
            return forwarder().predicateOp(delegate ->
                            delegate.containsDouble(d),
                    () -> OfDouble.super.containsDouble(d));
        }

        @Override
        public PrimitiveIterator.OfDouble iterator() {
            return forwarder().maskIfNeeded(forwarder().boxedOp(OfDouble::iterator),
                    Views::unmodifiableDoubleIteratorView);
        }

        @Override
        public DoubleStream parallelPrimitiveStream() {
            return forwarder().boxedOp(OfDouble::parallelPrimitiveStream,
                    OfDouble.super::parallelPrimitiveStream);
        }

        @Override
        public DoubleStream primitiveStream() {
            return forwarder().boxedOp(OfDouble::primitiveStream,
                    OfDouble.super::primitiveStream);
        }

        @Override
        public boolean remove(Object o) {
            checkNotUnmodifiable(this);
            return forwarder().predicateOp(delegate -> delegate.remove(o),
                    () -> OfDouble.super.remove(o));
        }

        @Override
        public boolean removeAll(Collection<?> collection) {
            checkNotUnmodifiable(this);
            return forwarder().predicateOp(delegate ->
                            delegate.removeAll(collection),
                    () -> OfDouble.super.removeAll(collection));
        }

        @Override
        public boolean removeAll(PrimitiveCollection.OfDouble collection) {
            checkNotUnmodifiable(this);
            return forwarder().predicateOp(delegate ->
                            delegate.removeAll(collection),
                    () -> OfDouble.super.removeAll(collection));
        }

        @Override
        public boolean removeDouble(double d) {
            checkNotUnmodifiable(this);
            return forwarder().predicateOp(delegate ->
                            delegate.removeDouble(d),
                    () -> OfDouble.super.removeDouble(d));
        }

        @Override
        public boolean removeIf(DoublePredicate filter) {
            checkNotUnmodifiable(this);
            return forwarder().predicateOp(delegate ->
                            delegate.removeIf(filter),
                    () -> OfDouble.super.removeIf(filter));
        }

        @Override
        public boolean removeIf(Predicate<? super Double> filter) {
            checkNotUnmodifiable(this);
            return forwarder().predicateOp(delegate ->
                            delegate.removeIf(filter),
                    () -> OfDouble.super.removeIf(filter));
        }

        @Override
        public boolean removeIfDouble(DoublePredicate filter) {
            checkNotUnmodifiable(this);
            return forwarder().predicateOp(delegate ->
                            delegate.removeIfDouble(filter),
                    () -> OfDouble.super.removeIfDouble(filter));
        }

        @Override
        public boolean retainAll(Collection<?> collection) {
            checkNotUnmodifiable(this);
            return forwarder().predicateOp(delegate ->
                            delegate.retainAll(collection),
                    () -> OfDouble.super.retainAll(collection));
        }

        @Override
        public boolean retainAll(PrimitiveCollection.OfDouble collection) {
            checkNotUnmodifiable(this);
            return forwarder().predicateOp(delegate ->
                            delegate.retainAll(collection),
                    () -> OfDouble.super.retainAll(collection));
        }

        @Override
        public Spliterator.OfDouble spliterator() {
            return forwarder().boxedOp(OfDouble::spliterator,
                    OfDouble.super::spliterator);
        }

        @Override
        public double[] toPrimitiveArray() {
            return forwarder().boxedOp(OfDouble::toPrimitiveArray,
                    OfDouble.super::toPrimitiveArray);
        }
    }

    @PrereleaseContent
    static class DoubleSetView extends AbstractDoubleSetView {
        private final Forwarder<Set<?>,PrimitiveSet.OfDouble> forwarder;

        DoubleSetView(Forwarder<Set<?>,PrimitiveSet.OfDouble> forwarder) {
            this.forwarder = forwarder;
        }

        @Override
        protected Forwarder<Set<?>,PrimitiveSet.OfDouble> forwarder() {
            return forwarder;
        }
    }

    @PrereleaseContent
    static class SerializableDoubleSetView extends AbstractDoubleSetView
            implements Serializable {
        private final Forwarder<Set<?>,PrimitiveSet.OfDouble> forwarder;

        SerializableDoubleSetView(Forwarder<Set<?>,PrimitiveSet.OfDouble> forwarder) {
            this.forwarder = forwarder;
        }

        @Override
        protected Forwarder<Set<?>,PrimitiveSet.OfDouble> forwarder() {
            return forwarder;
        }
    }

    @PrereleaseContent
    static class UnmodifiableDoubleSetView extends AbstractDoubleSetView
            implements UnmodifiableView {
        private final Forwarder<Set<?>,PrimitiveSet.OfDouble> forwarder;

        UnmodifiableDoubleSetView(Forwarder<Set<?>,PrimitiveSet.OfDouble> forwarder) {
            this.forwarder = forwarder;
        }

        @Override
        protected Forwarder<Set<?>,PrimitiveSet.OfDouble> forwarder() {
            return forwarder;
        }
    }

    @PrereleaseContent
    static class SerializableUnmodifiableDoubleSetView extends AbstractDoubleSetView
            implements Serializable, UnmodifiableView {
        private final Forwarder<Set<?>,PrimitiveSet.OfDouble> forwarder;

        SerializableUnmodifiableDoubleSetView(Forwarder<Set<?>,PrimitiveSet.OfDouble> forwarder) {
            this.forwarder = forwarder;
        }

        @Override
        protected Forwarder<Set<?>,PrimitiveSet.OfDouble> forwarder() {
            return forwarder;
        }
    }

    @PrereleaseContent
    static abstract class AbstractIntSetView extends PrimitiveSetView<Integer,int[],
            IntConsumer, IntPredicate, Spliterator.OfInt, IntStream,
            PrimitiveCollection.OfInt, PrimitiveSet.OfInt>
            implements PrimitiveSet.OfInt {
        @Override
        public boolean add(Integer t) {
            checkNotUnmodifiable(this);
            return forwarder().predicateOp(delegate -> delegate.add(t),
                    () -> OfInt.super.add(t));
        }

        @Override
        public boolean addAll(Collection<? extends Integer> collection) {
            checkNotUnmodifiable(this);
            return forwarder().predicateOp(delegate ->
                            delegate.addAll(collection),
                    () -> OfInt.super.addAll(collection));
        }

        @Override
        public boolean addAll(PrimitiveCollection.OfInt collection) {
            checkNotUnmodifiable(this);
            return forwarder().predicateOp(delegate ->
                            delegate.addAll(collection),
                    () -> OfInt.super.addAll(collection));
        }

        @Override
        public boolean addInt(int i) {
            checkNotUnmodifiable(this);
            return forwarder().predicateOp(delegate -> delegate.addInt(i),
                    () -> OfInt.super.addInt(i));
        }

        @Override
        public boolean contains(Object o) {
            return forwarder().predicateOp(delegate -> delegate.contains(o),
                    () -> OfInt.super.contains(o));
        }

        @Override
        public boolean containsAll(Collection<?> collection) {
            return forwarder().predicateOp(delegate ->
                            delegate.containsAll(collection),
                    () -> OfInt.super.containsAll(collection));
        }

        @Override
        public boolean containsAll(PrimitiveCollection.OfInt collection) {
            return forwarder().predicateOp(delegate ->
                            delegate.containsAll(collection),
                    () -> OfInt.super.containsAll(collection));
        }

        @Override
        public boolean containsInt(int i) {
            return forwarder().predicateOp(delegate -> delegate.containsInt(i),
                    () -> OfInt.super.containsInt(i));
        }

        @Override
        public PrimitiveIterator.OfInt iterator() {
            return forwarder().maskIfNeeded(forwarder().boxedOp(OfInt::iterator),
                    Views::unmodifiableIntIteratorView);
        }

        @Override
        public IntStream parallelPrimitiveStream() {
            return forwarder().boxedOp(OfInt::parallelPrimitiveStream,
                    OfInt.super::parallelPrimitiveStream);
        }

        @Override
        public IntStream primitiveStream() {
            return forwarder().boxedOp(OfInt::primitiveStream,
                    OfInt.super::primitiveStream);
        }

        @Override
        public boolean remove(Object o) {
            checkNotUnmodifiable(this);
            return forwarder().predicateOp(delegate -> delegate.remove(o),
                    () -> OfInt.super.remove(o));
        }

        @Override
        public boolean removeAll(Collection<?> collection) {
            checkNotUnmodifiable(this);
            return forwarder().predicateOp(delegate ->
                            delegate.removeAll(collection),
                    () -> OfInt.super.removeAll(collection));
        }

        @Override
        public boolean removeAll(PrimitiveCollection.OfInt collection) {
            checkNotUnmodifiable(this);
            return forwarder().predicateOp(delegate ->
                            delegate.removeAll(collection),
                    () -> OfInt.super.removeAll(collection));
        }

        @Override
        public boolean removeIf(IntPredicate filter) {
            checkNotUnmodifiable(this);
            return forwarder().predicateOp(delegate -> delegate.removeIf(filter),
                    () -> OfInt.super.removeIf(filter));
        }

        @Override
        public boolean removeIf(Predicate<? super Integer> filter) {
            checkNotUnmodifiable(this);
            return forwarder().predicateOp(delegate -> delegate.removeIf(filter),
                    () -> OfInt.super.removeIf(filter));
        }

        @Override
        public boolean removeIfInt(IntPredicate filter) {
            checkNotUnmodifiable(this);
            return forwarder().predicateOp(delegate ->
                            delegate.removeIfInt(filter),
                    () -> OfInt.super.removeIfInt(filter));
        }

        @Override
        public boolean removeInt(int i) {
            checkNotUnmodifiable(this);
            return forwarder().predicateOp(delegate -> delegate.removeInt(i),
                    () -> OfInt.super.removeInt(i));
        }

        @Override
        public boolean retainAll(Collection<?> collection) {
            checkNotUnmodifiable(this);
            return forwarder().predicateOp(delegate ->
                            delegate.retainAll(collection),
                    () -> OfInt.super.retainAll(collection));
        }

        @Override
        public boolean retainAll(PrimitiveCollection.OfInt collection) {
            checkNotUnmodifiable(this);
            return forwarder().predicateOp(delegate ->
                            delegate.retainAll(collection),
                    () -> OfInt.super.retainAll(collection));
        }

        @Override
        public Spliterator.OfInt spliterator() {
            return forwarder().boxedOp(OfInt::spliterator, OfInt.super::spliterator);
        }

        @Override
        public int[] toPrimitiveArray() {
            return forwarder().boxedOp(OfInt::toPrimitiveArray,
                    OfInt.super::toPrimitiveArray);
        }
    }

    @PrereleaseContent
    static class IntSetView extends AbstractIntSetView {
        private final Forwarder<Set<?>,PrimitiveSet.OfInt> forwarder;

        IntSetView(Forwarder<Set<?>,PrimitiveSet.OfInt> forwarder) {
            this.forwarder = forwarder;
        }

        @Override
        protected Forwarder<Set<?>,PrimitiveSet.OfInt> forwarder() {
            return forwarder;
        }
    }

    @PrereleaseContent
    static class SerializableIntSetView extends AbstractIntSetView
            implements Serializable {
        private final Forwarder<Set<?>,PrimitiveSet.OfInt> forwarder;

        SerializableIntSetView(Forwarder<Set<?>,PrimitiveSet.OfInt> forwarder) {
            this.forwarder = forwarder;
        }

        @Override
        protected Forwarder<Set<?>,PrimitiveSet.OfInt> forwarder() {
            return forwarder;
        }
    }

    @PrereleaseContent
    static class UnmodifiableIntSetView extends AbstractIntSetView
            implements UnmodifiableView {
        private final Forwarder<Set<?>,PrimitiveSet.OfInt> forwarder;

        UnmodifiableIntSetView(Forwarder<Set<?>,PrimitiveSet.OfInt> forwarder) {
            this.forwarder = forwarder;
        }

        @Override
        protected Forwarder<Set<?>,PrimitiveSet.OfInt> forwarder() {
            return forwarder;
        }
    }

    @PrereleaseContent
    static class SerializableUnmodifiableIntSetView extends AbstractIntSetView
            implements Serializable, UnmodifiableView {
        private final Forwarder<Set<?>,PrimitiveSet.OfInt> forwarder;

        SerializableUnmodifiableIntSetView(Forwarder<Set<?>,PrimitiveSet.OfInt> forwarder) {
            this.forwarder = forwarder;
        }

        @Override
        protected Forwarder<Set<?>,PrimitiveSet.OfInt> forwarder() {
            return forwarder;
        }
    }

    @PrereleaseContent
    static abstract class AbstractLongSetView extends PrimitiveSetView<Long, long[],
            LongConsumer, LongPredicate, Spliterator.OfLong, LongStream,
            PrimitiveCollection.OfLong, PrimitiveSet.OfLong>
            implements PrimitiveSet.OfLong {
        @Override
        public boolean add(Long t) {
            checkNotUnmodifiable(this);
            return forwarder().predicateOp(delegate -> delegate.add(t),
                    () -> OfLong.super.add(t));
        }

        @Override
        public boolean addAll(Collection<? extends Long> collection) {
            checkNotUnmodifiable(this);
            return forwarder().predicateOp(delegate ->
                            delegate.addAll(collection),
                    () -> OfLong.super.addAll(collection));
        }

        @Override
        public boolean addAll(PrimitiveCollection.OfLong collection) {
            checkNotUnmodifiable(this);
            return forwarder().predicateOp(delegate ->
                            delegate.addAll(collection),
                    () -> OfLong.super.addAll(collection));
        }

        @Override
        public boolean addLong(long l) {
            checkNotUnmodifiable(this);
            return forwarder().predicateOp(delegate -> delegate.addLong(l),
                    () -> OfLong.super.addLong(l));
        }

        @Override
        public boolean contains(Object o) {
            return forwarder().predicateOp(delegate -> delegate.contains(o),
                    () -> OfLong.super.contains(o));
        }

        @Override
        public boolean containsAll(Collection<?> collection) {
            return forwarder().predicateOp(delegate ->
                            delegate.containsAll(collection),
                    () -> OfLong.super.containsAll(collection));
        }

        @Override
        public boolean containsAll(PrimitiveCollection.OfLong collection) {
            return forwarder().predicateOp(delegate ->
                            delegate.containsAll(collection),
                    () -> OfLong.super.containsAll(collection));
        }

        @Override
        public boolean containsLong(long l) {
            return forwarder().predicateOp(delegate -> delegate.containsLong(l),
                    () -> OfLong.super.containsLong(l));
        }

        @Override
        public PrimitiveIterator.OfLong iterator() {
            return forwarder().maskIfNeeded(forwarder().boxedOp(OfLong::iterator),
                    Views::unmodifiableLongIteratorView);
        }

        @Override
        public LongStream parallelPrimitiveStream() {
            return forwarder().boxedOp(OfLong::parallelPrimitiveStream,
                    OfLong.super::parallelPrimitiveStream);
        }

        @Override
        public LongStream primitiveStream() {
            return forwarder().boxedOp(OfLong::primitiveStream,
                    OfLong.super::primitiveStream);
        }

        @Override
        public boolean remove(Object o) {
            checkNotUnmodifiable(this);
            return forwarder().predicateOp(delegate -> delegate.remove(o),
                    () -> OfLong.super.remove(o));
        }

        @Override
        public boolean removeAll(Collection<?> collection) {
            checkNotUnmodifiable(this);
            return forwarder().predicateOp(delegate ->
                            delegate.removeAll(collection),
                    () -> OfLong.super.removeAll(collection));
        }

        @Override
        public boolean removeAll(PrimitiveCollection.OfLong collection) {
            checkNotUnmodifiable(this);
            return forwarder().predicateOp(delegate ->
                            delegate.removeAll(collection),
                    () -> OfLong.super.removeAll(collection));
        }

        @Override
        public boolean removeIf(LongPredicate filter) {
            checkNotUnmodifiable(this);
            return forwarder().predicateOp(delegate -> delegate.removeIf(filter),
                    () -> OfLong.super.removeIf(filter));
        }

        @Override
        public boolean removeIf(Predicate<? super Long> filter) {
            checkNotUnmodifiable(this);
            return forwarder().predicateOp(delegate -> delegate.removeIf(filter),
                    () -> OfLong.super.removeIf(filter));
        }

        @Override
        public boolean removeIfLong(LongPredicate filter) {
            checkNotUnmodifiable(this);
            return forwarder().predicateOp(delegate ->
                            delegate.removeIfLong(filter),
                    () -> OfLong.super.removeIfLong(filter));
        }

        @Override
        public boolean removeLong(long l) {
            checkNotUnmodifiable(this);
            return forwarder().predicateOp(delegate -> delegate.removeLong(l),
                    () -> OfLong.super.removeLong(l));
        }

        @Override
        public boolean retainAll(Collection<?> collection) {
            checkNotUnmodifiable(this);
            return forwarder().predicateOp(delegate ->
                            delegate.retainAll(collection),
                    () -> OfLong.super.retainAll(collection));
        }

        @Override
        public boolean retainAll(PrimitiveCollection.OfLong collection) {
            checkNotUnmodifiable(this);
            return forwarder().predicateOp(delegate ->
                            delegate.retainAll(collection),
                    () -> OfLong.super.retainAll(collection));
        }

        @Override
        public Spliterator.OfLong spliterator() {
            return forwarder().boxedOp(OfLong::spliterator,
                    OfLong.super::spliterator);
        }

        @Override
        public long[] toPrimitiveArray() {
            return forwarder().boxedOp(OfLong::toPrimitiveArray,
                    OfLong.super::toPrimitiveArray);
        }
    }

    @PrereleaseContent
    static class LongSetView extends AbstractLongSetView {
        private final Forwarder<Set<?>,PrimitiveSet.OfLong> forwarder;

        LongSetView(Forwarder<Set<?>,PrimitiveSet.OfLong> forwarder) {
            this.forwarder = forwarder;
        }

        @Override
        protected Forwarder<Set<?>,PrimitiveSet.OfLong> forwarder() {
            return forwarder;
        }
    }

    @PrereleaseContent
    static class SerializableLongSetView extends AbstractLongSetView
            implements Serializable {
        private final Forwarder<Set<?>,PrimitiveSet.OfLong> forwarder;

        SerializableLongSetView(Forwarder<Set<?>,PrimitiveSet.OfLong> forwarder) {
            this.forwarder = forwarder;
        }

        @Override
        protected Forwarder<Set<?>,PrimitiveSet.OfLong> forwarder() {
            return forwarder;
        }
    }

    @PrereleaseContent
    static class UnmodifiableLongSetView extends AbstractLongSetView
            implements UnmodifiableView {
        private final Forwarder<Set<?>,PrimitiveSet.OfLong> forwarder;

        UnmodifiableLongSetView(Forwarder<Set<?>,PrimitiveSet.OfLong> forwarder) {
            this.forwarder = forwarder;
        }

        @Override
        protected Forwarder<Set<?>,PrimitiveSet.OfLong> forwarder() {
            return forwarder;
        }
    }

    @PrereleaseContent
    static class SerializableUnmodifiableLongSetView extends AbstractLongSetView
            implements Serializable, UnmodifiableView {
        private final Forwarder<Set<?>,PrimitiveSet.OfLong> forwarder;

        SerializableUnmodifiableLongSetView(Forwarder<Set<?>,PrimitiveSet.OfLong> forwarder) {
            this.forwarder = forwarder;
        }

        @Override
        protected Forwarder<Set<?>,PrimitiveSet.OfLong> forwarder() {
            return forwarder;
        }
    }
}
