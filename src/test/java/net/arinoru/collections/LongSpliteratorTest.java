package net.arinoru.collections;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.LongConsumer;
import java.util.stream.LongStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.*;

@SuppressWarnings({"unchecked","StatementWithEmptyBody"})
class LongSpliteratorTest {
    @Test
    void estimateSize__always__returnsCollectionSizeAtTimeOfBind() {
        var collection = mock(PrimitiveCollection.OfLong.class);
        var iterator = mock(PrimitiveIterator.OfLong.class);
        var cut = PrimitiveCollections.longSpliterator(collection, 0);
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
        var collection = mock(PrimitiveCollection.OfLong.class);
        var cut = PrimitiveCollections.longSpliterator(collection, Spliterator.SORTED);

        var result = cut.getComparator();

        assertThat(result).isNull();
        verifyNoMoreInteractions(collection);
    }

    @Test
    void getComparator__spliteratorNotSorted__throwsException() {
        var collection = mock(PrimitiveCollection.OfLong.class);
        var cut = PrimitiveCollections.longSpliterator(collection, 0);

        var t = catchThrowable(cut::getComparator);

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verifyNoMoreInteractions(collection);
    }

    @Test
    void trySplit__emptyCollection__returnsNull() {
        var collection = mock(PrimitiveCollection.OfLong.class);
        var cut = PrimitiveCollections.longSpliterator(collection, 0);
        when(collection.iterator()).thenReturn(PrimitiveCollections.emptyLongIterator());

        var result = cut.trySplit();

        assertThat(result).isNull();
        verify(collection).size();
        verify(collection).iterator();
        verifyNoMoreInteractions(collection);
    }

    @Test
    void trySplit__smallCollection__returnsNewSpliteratorOnce() {
        var collection = mock(PrimitiveCollection.OfLong.class);
        var cut = PrimitiveCollections.longSpliterator(collection, 0);
        when(collection.size()).thenReturn(5);
        when(collection.iterator()).thenReturn(LongStream.of(
                1L, 2L, 3L, 4L, 5L).iterator());

        var result1 = cut.trySplit();
        var result2 = cut.trySplit();

        assertThat(result1).isInstanceOf(Spliterator.OfLong.class);
        assertThat(result1.estimateSize()).isEqualTo(5);
        assertThat(result2).isNull();
        verify(collection).size();
        verify(collection).iterator();
    }

    @Test
    void trySplit__smallCollection__returnedSpliteratorContainsAllElements() {
        var collection = mock(PrimitiveCollection.OfLong.class);
        var consumer = mock(LongConsumer.class);
        var cut = PrimitiveCollections.longSpliterator(collection, 0);
        when(collection.size()).thenReturn(5);
        when(collection.iterator()).thenReturn(LongStream.of(
                1L, 2L, 3L, 4L, 5L).iterator());

        cut.trySplit().forEachRemaining(consumer);

        verify(consumer).accept(1L);
        verify(consumer).accept(2L);
        verify(consumer).accept(3L);
        verify(consumer).accept(4L);
        verify(consumer).accept(5L);
        verify(collection).size();
        verify(collection).iterator();
        verifyNoMoreInteractions(collection, consumer);
    }

    @Test
    void trySplit__largeCollection__returnsMultipleSpliterators() {
        var collection = mock(PrimitiveCollection.OfLong.class);
        var consumers = Stream.generate(() -> mock(LongConsumer.class)).limit(4)
                .toArray(LongConsumer[]::new);
        var cut = PrimitiveCollections.longSpliterator(collection, 0);
        when(collection.size()).thenReturn(10240);
        when(collection.iterator()).thenReturn(LongStream.iterate(0, d -> d + 1L)
                .limit(10240).iterator());

        // Spliterator batches start at 1kiB and increase by 1kiB per spliterator
        // generated up to a maximum of 32MiB. Thus, 10kiB elements would split
        // across 4 spliterators, covering 1024, 2048, 3072, and 4096 elements
        // respectively.
        var spliterators = Stream.generate(cut::trySplit).limit(5)
                .toArray(Spliterator.OfLong[]::new);

        assertThat(spliterators[4]).isNull();
        IntStream.range(0, 4).forEach(i -> spliterators[i].forEachRemaining(consumers[i]));
        IntStream.range(0, 4).forEach(i -> verify(consumers[i],
                times(1024 * (i + 1))).accept(anyLong()));
    }

    @Test
    void forEachRemaining_LongConsumer__passedNull__throwsException() {
        var collection = mock(PrimitiveCollection.OfLong.class);
        var cut = PrimitiveCollections.longSpliterator(collection, 0);

        var t = catchThrowable(() -> cut.forEachRemaining((LongConsumer) null));

        assertThat(t).isInstanceOf(NullPointerException.class);
    }

    @Test
    void forEachRemaining_LongConsumer__collectionWithElements__processesAllElements() {
        var consumer = mock(LongConsumer.class);
        var collection = mock(PrimitiveCollection.OfLong.class);
        var cut = PrimitiveCollections.longSpliterator(collection, 0);
        when(collection.size()).thenReturn(5);
        when(collection.iterator()).thenReturn(LongStream.of(
                1L, 2L, 3L, 4L, 5L).iterator());

        cut.forEachRemaining(consumer);

        verify(consumer).accept(1L);
        verify(consumer).accept(2L);
        verify(consumer).accept(3L);
        verify(consumer).accept(4L);
        verify(consumer).accept(5L);
        verify(collection).size();
        verify(collection).iterator();
        verifyNoMoreInteractions(collection, consumer);
    }

    @Test
    void tryAdvance_LongConsumer__emptyCollection__returnsFalse() {
        var consumer = mock(LongConsumer.class);
        var collection = mock(PrimitiveCollection.OfLong.class);
        var cut = PrimitiveCollections.longSpliterator(collection, 0);
        when(collection.iterator()).thenReturn(PrimitiveCollections.emptyLongIterator());

        var result = cut.tryAdvance(consumer);

        assertThat(result).isFalse();
        verify(collection).size();
        verify(collection).iterator();
        verifyNoMoreInteractions(consumer, collection);
    }

    @Test
    void tryAdvance_LongConsumer__hasElements__processesFirstElement() {
        var consumer = mock(LongConsumer.class);
        var collection = mock(PrimitiveCollection.OfLong.class);
        var cut = PrimitiveCollections.longSpliterator(collection, 0);
        when(collection.size()).thenReturn(5);
        when(collection.iterator()).thenReturn(LongStream.of(
                1L, 2L, 3L, 4L, 5L).iterator());

        var result = cut.tryAdvance(consumer);

        assertThat(result).isTrue();
        verify(consumer).accept(1L);
        verify(collection).size();
        verify(collection).iterator();
        verifyNoMoreInteractions(consumer, collection);
    }

    @Test
    void tryAdvance_LongConsumer__hasElements__repeatedInvocationsProcessElementsInOrder() {
        var bucket = new ArrayList<Long>();
        var collection = mock(PrimitiveCollection.OfLong.class);
        var cut = PrimitiveCollections.longSpliterator(collection, 0);
        when(collection.size()).thenReturn(5);
        when(collection.iterator()).thenReturn(LongStream.of(
                1L, 2L, 3L, 4L, 5L).iterator());

        while (cut.tryAdvance((LongConsumer) bucket::add));

        assertThat(bucket).containsExactly(1L, 2L, 3L, 4L, 5L);
    }

    @Test
    void tryAdvance_Consumer__emptyCollection__returnsFalse() {
        var consumer = (Consumer<Long>) mock(Consumer.class);
        var collection = mock(PrimitiveCollection.OfLong.class);
        var cut = PrimitiveCollections.longSpliterator(collection, 0);
        when(collection.iterator()).thenReturn(PrimitiveCollections.emptyLongIterator());

        var result = cut.tryAdvance(consumer);

        assertThat(result).isFalse();
        verify(collection).size();
        verify(collection).iterator();
        verifyNoMoreInteractions(consumer, collection);
    }

    @Test
    void tryAdvance_Consumer__hasElements__processesFirstElement() {
        var consumer = (Consumer<Long>) mock(Consumer.class);
        var collection = mock(PrimitiveCollection.OfLong.class);
        var cut = PrimitiveCollections.longSpliterator(collection, 0);
        when(collection.size()).thenReturn(5);
        when(collection.iterator()).thenReturn(LongStream.of(
                1L, 2L, 3L, 4L, 5L).iterator());

        var result = cut.tryAdvance(consumer);

        assertThat(result).isTrue();
        verify(consumer).accept(1L);
        verify(collection).size();
        verify(collection).iterator();
        verifyNoMoreInteractions(consumer, collection);
    }

    @Test
    void tryAdvance_Consumer__hasElements__repeatedInvocationsProcessElementsInOrder() {
        var bucket = new ArrayList<Long>();
        var collection = mock(PrimitiveCollection.OfLong.class);
        var cut = PrimitiveCollections.longSpliterator(collection, 0);
        when(collection.size()).thenReturn(5);
        when(collection.iterator()).thenReturn(LongStream.of(
                1L, 2L, 3L, 4L, 5L).iterator());

        while (cut.tryAdvance((Consumer<Long>) bucket::add));

        assertThat(bucket).containsExactly(1L, 2L, 3L, 4L, 5L);
    }
}
