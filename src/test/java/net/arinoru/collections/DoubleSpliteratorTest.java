package net.arinoru.collections;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.*;

@SuppressWarnings({"unchecked","StatementWithEmptyBody"})
class DoubleSpliteratorTest {
    @Test
    void estimateSize__always__returnsCollectionSizeAtTimeOfBind() {
        var collection = mock(PrimitiveCollection.OfDouble.class);
        var iterator = mock(PrimitiveIterator.OfDouble.class);
        var cut = PrimitiveCollections.doubleSpliterator(collection, 0);
        when(collection.size()).thenReturn(5, 4);
        when(collection.iterator()).thenReturn(iterator);

        var result1 = cut.estimateSize();
        var result2 = cut.estimateSize();

        assertThat(result1).isEqualTo(5);
        assertThat(result2).isEqualTo(5);
        verify(collection).size();
        verify(collection).iterator();
        verifyNoMoreInteractions(collection, iterator);
    }

    @Test
    void getComparator__sortedSpliterator__returnsNull() {
        var collection = mock(PrimitiveCollection.OfDouble.class);
        var cut = PrimitiveCollections.doubleSpliterator(collection, Spliterator.SORTED);

        var result = cut.getComparator();

        assertThat(result).isNull();
        verifyNoMoreInteractions(collection);
    }

    @Test
    void getComparator__spliteratorNotSorted__throwsException() {
        var collection = mock(PrimitiveCollection.OfDouble.class);
        var cut = PrimitiveCollections.doubleSpliterator(collection, 0);

        var t = catchThrowable(cut::getComparator);

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verifyNoMoreInteractions(collection);
    }

    @Test
    void trySplit__emptyCollection__returnsNull() {
        var collection = mock(PrimitiveCollection.OfDouble.class);
        var cut = PrimitiveCollections.doubleSpliterator(collection, 0);
        when(collection.iterator()).thenReturn(PrimitiveCollections.emptyDoubleIterator());

        var result = cut.trySplit();

        assertThat(result).isNull();
        verify(collection).size();
        verify(collection).iterator();
        verifyNoMoreInteractions(collection);
    }

    @Test
    void trySplit__smallCollection__returnsNewSpliteratorOnce() {
        var collection = mock(PrimitiveCollection.OfDouble.class);
        var cut = PrimitiveCollections.doubleSpliterator(collection, 0);
        when(collection.size()).thenReturn(5);
        when(collection.iterator()).thenReturn(DoubleStream.of(
                1.0, 2.0, 3.0, 4.0, 5.0).iterator());

        var result1 = cut.trySplit();
        var result2 = cut.trySplit();

        assertThat(result1).isInstanceOf(Spliterator.OfDouble.class);
        assertThat(result1.estimateSize()).isEqualTo(5);
        assertThat(result2).isNull();
        verify(collection).size();
        verify(collection).iterator();
    }

    @Test
    void trySplit__smallCollection__returnedSpliteratorContainsAllElements() {
        var collection = mock(PrimitiveCollection.OfDouble.class);
        var consumer = mock(DoubleConsumer.class);
        var cut = PrimitiveCollections.doubleSpliterator(collection, 0);
        when(collection.size()).thenReturn(5);
        when(collection.iterator()).thenReturn(DoubleStream.of(
                1.0, 2.0, 3.0, 4.0, 5.0).iterator());

        cut.trySplit().forEachRemaining(consumer);

        verify(consumer).accept(1.0);
        verify(consumer).accept(2.0);
        verify(consumer).accept(3.0);
        verify(consumer).accept(4.0);
        verify(consumer).accept(5.0);
        verify(collection).size();
        verify(collection).iterator();
        verifyNoMoreInteractions(collection, consumer);
    }

    @Test
    void trySplit__largeCollection__returnsMultipleSpliterators() {
        var collection = mock(PrimitiveCollection.OfDouble.class);
        var consumers = Stream.generate(() -> mock(DoubleConsumer.class)).limit(4)
                .toArray(DoubleConsumer[]::new);
        var cut = PrimitiveCollections.doubleSpliterator(collection, 0);
        when(collection.size()).thenReturn(10240);
        when(collection.iterator()).thenReturn(DoubleStream.iterate(0, d -> d + 1.0)
                .limit(10240).iterator());

        // Spliterator batches start at 1kiB and increase by 1kiB per spliterator
        // generated up to a maximum of 32MiB. Thus, 10kiB elements would split
        // across 4 spliterators, covering 1024, 2048, 3072, and 4096 elements
        // respectively.
        var spliterators = Stream.generate(cut::trySplit).limit(5)
                .toArray(Spliterator.OfDouble[]::new);

        assertThat(spliterators[4]).isNull();
        IntStream.range(0, 4).forEach(i -> spliterators[i].forEachRemaining(consumers[i]));
        IntStream.range(0, 4).forEach(i -> verify(consumers[i],
                times(1024 * (i + 1))).accept(anyDouble()));
    }

    @Test
    void forEachRemaining_DoubleConsumer__passedNull__throwsException() {
        var collection = mock(PrimitiveCollection.OfDouble.class);
        var cut = PrimitiveCollections.doubleSpliterator(collection, 0);

        var t = catchThrowable(() -> cut.forEachRemaining((DoubleConsumer) null));

        assertThat(t).isInstanceOf(NullPointerException.class);
    }

    @Test
    void forEachRemaining_DoubleConsumer__collectionWithElements__processesAllElements() {
        var consumer = mock(DoubleConsumer.class);
        var collection = mock(PrimitiveCollection.OfDouble.class);
        var cut = PrimitiveCollections.doubleSpliterator(collection, 0);
        when(collection.size()).thenReturn(5);
        when(collection.iterator()).thenReturn(DoubleStream.of(
                1.0, 2.0, 3.0, 4.0, 5.0).iterator());

        cut.forEachRemaining(consumer);

        verify(consumer).accept(1.0);
        verify(consumer).accept(2.0);
        verify(consumer).accept(3.0);
        verify(consumer).accept(4.0);
        verify(consumer).accept(5.0);
        verify(collection).size();
        verify(collection).iterator();
        verifyNoMoreInteractions(collection, consumer);
    }

    @Test
    void tryAdvance_DoubleConsumer__emptyCollection__returnsFalse() {
        var consumer = mock(DoubleConsumer.class);
        var collection = mock(PrimitiveCollection.OfDouble.class);
        var cut = PrimitiveCollections.doubleSpliterator(collection, 0);
        when(collection.iterator()).thenReturn(PrimitiveCollections.emptyDoubleIterator());

        var result = cut.tryAdvance(consumer);

        assertThat(result).isFalse();
        verify(collection).size();
        verify(collection).iterator();
        verifyNoMoreInteractions(collection, consumer);
    }

    @Test
    void tryAdvance_DoubleConsumer__hasElements__processesFirstElement() {
        var consumer = mock(DoubleConsumer.class);
        var collection = mock(PrimitiveCollection.OfDouble.class);
        var cut = PrimitiveCollections.doubleSpliterator(collection, 0);
        when(collection.size()).thenReturn(5);
        when(collection.iterator()).thenReturn(DoubleStream.of(
                1.0, 2.0, 3.0, 4.0, 5.0).iterator());

        var result = cut.tryAdvance(consumer);

        assertThat(result).isTrue();
        verify(consumer).accept(1.0);
        verify(collection).size();
        verify(collection).iterator();
        verifyNoMoreInteractions(collection, consumer);
    }

    @Test
    void tryAdvance_DoubleConsumer__hasElements__repeatedInvocationsProcessElementsInOrder() {
        var bucket = new ArrayList<Double>();
        var collection = mock(PrimitiveCollection.OfDouble.class);
        var cut = PrimitiveCollections.doubleSpliterator(collection, 0);
        when(collection.size()).thenReturn(5);
        when(collection.iterator()).thenReturn(DoubleStream.of(
                1.0, 2.0, 3.0, 4.0, 5.0).iterator());

        while (cut.tryAdvance((DoubleConsumer) bucket::add));

        assertThat(bucket).containsExactly(1.0, 2.0, 3.0, 4.0, 5.0);
    }

    @Test
    void tryAdvance_Consumer__emptyCollection__returnsFalse() {
        var consumer = (Consumer<Double>) mock(Consumer.class);
        var collection = mock(PrimitiveCollection.OfDouble.class);
        var cut = PrimitiveCollections.doubleSpliterator(collection, 0);
        when(collection.iterator()).thenReturn(PrimitiveCollections.emptyDoubleIterator());

        var result = cut.tryAdvance(consumer);

        assertThat(result).isFalse();
        verify(collection).size();
        verify(collection).iterator();
        verifyNoMoreInteractions(collection, consumer);
    }

    @Test
    void tryAdvance_Consumer__hasElements__processesFirstElement() {
        var consumer = (Consumer<Double>) mock(Consumer.class);
        var collection = mock(PrimitiveCollection.OfDouble.class);
        var cut = PrimitiveCollections.doubleSpliterator(collection, 0);
        when(collection.size()).thenReturn(5);
        when(collection.iterator()).thenReturn(DoubleStream.of(
                1.0, 2.0, 3.0, 4.0, 5.0).iterator());

        var result = cut.tryAdvance(consumer);

        assertThat(result).isTrue();
        verify(consumer).accept(1.0);
        verify(collection).size();
        verify(collection).iterator();
        verifyNoMoreInteractions(collection, consumer);
    }

    @Test
    void tryAdvance_Consumer__hasElements__repeatedInvocationsProcessElementsInOrder() {
        var bucket = new ArrayList<Double>();
        var collection = mock(PrimitiveCollection.OfDouble.class);
        var cut = PrimitiveCollections.doubleSpliterator(collection, 0);
        when(collection.size()).thenReturn(5);
        when(collection.iterator()).thenReturn(DoubleStream.of(
                1.0, 2.0, 3.0, 4.0, 5.0).iterator());

        while (cut.tryAdvance((Consumer<Double>) bucket::add));

        assertThat(bucket).containsExactly(1.0, 2.0, 3.0, 4.0, 5.0);
    }
}
