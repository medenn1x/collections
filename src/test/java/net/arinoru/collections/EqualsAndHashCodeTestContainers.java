package net.arinoru.collections;

import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.PrimitiveIterator;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

@SuppressWarnings("EqualsDoesntCheckParameterClass")
abstract class EqualsAndHashCodeTestContainers {
    static class Collection<E> extends AbstractCollection<E> {
        private final E[] elements;
        private boolean calledEquals;
        private boolean calledHashCode;

        @SafeVarargs
        Collection(E... elements) {
            this.elements = elements;
        }

        boolean calledEquals() {
            return calledEquals;
        }

        boolean calledHashCode() {
            return calledHashCode;
        }

        @Override
        public boolean equals(Object o) {
            calledEquals = true;
            return super.equals(o);
        }

        @Override
        public int hashCode() {
            calledHashCode = true;
            return super.hashCode();
        }

        @Override
        public Iterator<E> iterator() {
            return Stream.of(elements).iterator();
        }

        @Override
        public int size() {
            return elements.length;
        }
    }

    static class Set<E> extends AbstractSet<E> {
        private final E[] elements;
        private boolean calledEquals;
        private boolean calledHashCode;

        @SafeVarargs
        Set(E... elements) {
            this.elements = elements;
        }

        boolean calledEquals() {
            return calledEquals;
        }

        boolean calledHashCode() {
            return calledHashCode;
        }

        @Override
        public boolean equals(Object o) {
            calledEquals = true;
            return super.equals(o);
        }

        @Override
        public int hashCode() {
            calledHashCode = true;
            return super.hashCode();
        }

        @Override
        public Iterator<E> iterator() {
            return Stream.of(elements).iterator();
        }

        @Override
        public int size() {
            return elements.length;
        }
    }

    static class DoubleCollection implements PrimitiveCollection.OfDouble {
        private final double[] elements;
        private boolean calledEquals;
        private boolean calledHashCode;

        DoubleCollection(double... elements) {
            this.elements = elements;
        }

        boolean calledEquals() {
            return calledEquals;
        }

        boolean calledHashCode() {
            return calledHashCode;
        }

        @Override
        public boolean equals(Object o) {
            calledEquals = true;
            return super.equals(o);
        }

        @Override
        public int hashCode() {
            calledHashCode = true;
            return super.hashCode();
        }

        @Override
        public PrimitiveIterator.OfDouble iterator() {
            return DoubleStream.of(elements).iterator();
        }

        @Override
        public int size() {
            return elements.length;
        }
    }

    static class DoubleSet implements PrimitiveSet.OfDouble {
        private final double[] elements;
        private boolean calledEquals;
        private boolean calledHashCode;

        DoubleSet(double... elements) {
            this.elements = elements;
        }

        boolean calledEquals() {
            return calledEquals;
        }

        boolean calledHashCode() {
            return calledHashCode;
        }

        @Override
        public boolean equals(Object o) {
            calledEquals = true;
            return PrimitiveCollections.equals(this, o);
        }

        @Override
        public int hashCode() {
            calledHashCode = true;
            return PrimitiveCollections.hashCode(this);
        }

        @Override
        public PrimitiveIterator.OfDouble iterator() {
            return DoubleStream.of(elements).iterator();
        }

        @Override
        public int size() {
            return elements.length;
        }
    }

    static class IntCollection implements PrimitiveCollection.OfInt {
        private final int[] elements;
        private boolean calledEquals;
        private boolean calledHashCode;

        IntCollection(int... elements) {
            this.elements = elements;
        }

        boolean calledEquals() {
            return calledEquals;
        }

        boolean calledHashCode() {
            return calledHashCode;
        }

        @Override
        public boolean equals(Object o) {
            calledEquals = true;
            return super.equals(o);
        }

        @Override
        public int hashCode() {
            calledHashCode = true;
            return super.hashCode();
        }

        @Override
        public PrimitiveIterator.OfInt iterator() {
            return IntStream.of(elements).iterator();
        }

        @Override
        public int size() {
            return elements.length;
        }
    }

    static class IntSet implements PrimitiveSet.OfInt {
        private final int[] elements;
        private boolean calledEquals;
        private boolean calledHashCode;

        IntSet(int... elements) {
            this.elements = elements;
        }

        boolean calledEquals() {
            return calledEquals;
        }

        boolean calledHashCode() {
            return calledHashCode;
        }

        @Override
        public boolean equals(Object o) {
            calledEquals = true;
            return PrimitiveCollections.equals(this, o);
        }

        @Override
        public int hashCode() {
            calledHashCode = true;
            return PrimitiveCollections.hashCode(this);
        }

        @Override
        public PrimitiveIterator.OfInt iterator() {
            return IntStream.of(elements).iterator();
        }

        @Override
        public int size() {
            return elements.length;
        }
    }

    static class LongCollection implements PrimitiveCollection.OfLong {
        private final long[] elements;
        private boolean calledEquals;
        private boolean calledHashCode;

        LongCollection(long... elements) {
            this.elements = elements;
        }

        boolean calledEquals() {
            return calledEquals;
        }

        boolean calledHashCode() {
            return calledHashCode;
        }

        @Override
        public boolean equals(Object o) {
            calledEquals = true;
            return super.equals(o);
        }

        @Override
        public int hashCode() {
            calledHashCode = true;
            return super.hashCode();
        }

        @Override
        public PrimitiveIterator.OfLong iterator() {
            return LongStream.of(elements).iterator();
        }

        @Override
        public int size() {
            return elements.length;
        }
    }

    static class LongSet implements PrimitiveSet.OfLong {
        private final long[] elements;
        private boolean calledEquals;
        private boolean calledHashCode;

        LongSet(long... elements) {
            this.elements = elements;
        }

        boolean calledEquals() {
            return calledEquals;
        }

        boolean calledHashCode() {
            return calledHashCode;
        }

        @Override
        public boolean equals(Object o) {
            calledEquals = true;
            return PrimitiveCollections.equals(this, o);
        }

        @Override
        public int hashCode() {
            calledHashCode = true;
            return PrimitiveCollections.hashCode(this);
        }

        @Override
        public PrimitiveIterator.OfLong iterator() {
            return LongStream.of(elements).iterator();
        }

        @Override
        public int size() {
            return elements.length;
        }
    }
}
