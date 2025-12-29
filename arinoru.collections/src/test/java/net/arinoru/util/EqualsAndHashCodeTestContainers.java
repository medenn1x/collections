package net.arinoru.util;

import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.PrimitiveIterator;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

@SuppressWarnings("EqualsDoesntCheckParameterClass")
public abstract class EqualsAndHashCodeTestContainers {
    public static class Collection<E> extends AbstractCollection<E> {
        private final E[] elements;
        private boolean calledEquals;
        private boolean calledHashCode;

        @SafeVarargs
        public Collection(E... elements) {
            this.elements = elements;
        }

        public boolean calledEquals() {
            return calledEquals;
        }

        public boolean calledHashCode() {
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

    public static class Set<E> extends AbstractSet<E> {
        private final E[] elements;
        private boolean calledEquals;
        private boolean calledHashCode;

        @SafeVarargs
        public Set(E... elements) {
            this.elements = elements;
        }

        public boolean calledEquals() {
            return calledEquals;
        }

        public boolean calledHashCode() {
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

    public static class DoubleCollection implements net.arinoru.util.DoubleCollection {
        private final double[] elements;
        private boolean calledEquals;
        private boolean calledHashCode;

        public DoubleCollection(double... elements) {
            this.elements = elements;
        }

        public boolean calledEquals() {
            return calledEquals;
        }

        public boolean calledHashCode() {
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

    public static class DoubleSet implements net.arinoru.util.DoubleSet {
        private final double[] elements;
        private boolean calledEquals;
        private boolean calledHashCode;

        public DoubleSet(double... elements) {
            this.elements = elements;
        }

        public boolean calledEquals() {
            return calledEquals;
        }

        public boolean calledHashCode() {
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

    public static class IntCollection implements net.arinoru.util.IntCollection {
        private final int[] elements;
        private boolean calledEquals;
        private boolean calledHashCode;

        public IntCollection(int... elements) {
            this.elements = elements;
        }

        public boolean calledEquals() {
            return calledEquals;
        }

        public boolean calledHashCode() {
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

    public static class IntSet implements net.arinoru.util.IntSet {
        private final int[] elements;
        private boolean calledEquals;
        private boolean calledHashCode;

        public IntSet(int... elements) {
            this.elements = elements;
        }

        public boolean calledEquals() {
            return calledEquals;
        }

        public boolean calledHashCode() {
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

    public static class LongCollection implements net.arinoru.util.LongCollection {
        private final long[] elements;
        private boolean calledEquals;
        private boolean calledHashCode;

        public LongCollection(long... elements) {
            this.elements = elements;
        }

        public boolean calledEquals() {
            return calledEquals;
        }

        public boolean calledHashCode() {
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

    public static class LongSet implements net.arinoru.util.LongSet {
        private final long[] elements;
        private boolean calledEquals;
        private boolean calledHashCode;

        public LongSet(long... elements) {
            this.elements = elements;
        }

        public boolean calledEquals() {
            return calledEquals;
        }

        public boolean calledHashCode() {
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
