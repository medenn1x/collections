package net.arinoru.collections;

import org.junit.jupiter.api.Test;

import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.*;

class IntSpliteratorTest {
    @Test
    void estimateSize__always__returnsCollectionSizeAtTimeOfBind() {
        var collection = mock(PrimitiveCollection.OfInt.class);
        var iterator = mock(PrimitiveIterator.OfInt.class);
        var cut = PrimitiveCollections.intSpliterator(collection, 0);
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
        var collection = mock(PrimitiveCollection.OfInt.class);
        var cut = PrimitiveCollections.intSpliterator(collection, Spliterator.SORTED);

        var result = cut.getComparator();

        assertThat(result).isNull();
        verifyNoMoreInteractions(collection);
    }

    @Test
    void getComparator__spliteratorNotSorted__throwsException() {
        var collection = mock(PrimitiveCollection.OfInt.class);
        var cut = PrimitiveCollections.intSpliterator(collection, 0);

        var t = catchThrowable(cut::getComparator);

        assertThat(t).isInstanceOf(IllegalStateException.class);
        verifyNoMoreInteractions(collection);
    }

    @Test
    void trySplit__emptyCollection__returnsNull() {
        var collection = mock(PrimitiveCollection.OfInt.class);
        var cut = PrimitiveCollections.intSpliterator(collection, 0);
        when(collection.iterator()).thenReturn(PrimitiveCollections.emptyIntIterator());

        var result = cut.trySplit();

        assertThat(result).isNull();
        verify(collection).size();
        verify(collection).iterator();
        verifyNoMoreInteractions(collection);
    }

    @Test
    void trySplit__smallCollection__returnsNewSpliteratorOnce() {
        var collection = mock(PrimitiveCollection.OfInt.class);
        var cut = PrimitiveCollections.intSpliterator(collection, 0);
        when(collection.size()).thenReturn(5);
        when(collection.iterator()).thenReturn(IntStream.of(
                1, 2, 3, 4, 5).iterator());

        var result1 = cut.trySplit();
        var result2 = cut.trySplit();

        assertThat(result1).isInstanceOf(Spliterator.OfInt.class);
        assertThat(result1.estimateSize()).isEqualTo(5);
        assertThat(result2).isNull();
        verify(collection).size();
        verify(collection).iterator();
    }

    @Test
    void trySplit__smallCollection__returnedSpliteratorContainsAllElements() {
        var collection = mock(PrimitiveCollection.OfInt.class);
        var consumer = mock(IntConsumer.class);
        var cut = PrimitiveCollections.intSpliterator(collection, 0);
        when(collection.size()).thenReturn(5);
        when(collection.iterator()).thenReturn(IntStream.of(
                1, 2, 3, 4, 5).iterator());

        cut.trySplit().forEachRemaining(consumer);

        verify(consumer).accept(1);
        verify(consumer).accept(2);
        verify(consumer).accept(3);
        verify(consumer).accept(4);
        verify(consumer).accept(5);
        verify(collection).size();
        verify(collection).iterator();
        verifyNoMoreInteractions(collection, consumer);
    }

    @Test
    void trySplit__largeCollection__returnsMultipleSpliterators() {
        var collection = mock(PrimitiveCollection.OfInt.class);
        var consumers = Stream.generate(() -> mock(IntConsumer.class)).limit(4)
                .toArray(IntConsumer[]::new);
        var cut = PrimitiveCollections.intSpliterator(collection, 0);
        when(collection.size()).thenReturn(10240);
        when(collection.iterator()).thenReturn(IntStream.iterate(0, d -> d + 1)
                .limit(10240).iterator());

        // Spliterator batches start at 1kiB and increase by 1kiB per spliterator
        // generated up to a maximum of 32MiB. Thus, 10kiB elements would split
        // across 4 spliterators, covering 1024, 2048, 3072, and 4096 elements
        // respectively.
        var spliterators = Stream.generate(cut::trySplit).limit(5)
                .toArray(Spliterator.OfInt[]::new);

        assertThat(spliterators[4]).isNull();
        IntStream.range(0, 4).forEach(i -> spliterators[i].forEachRemaining(consumers[i]));
        IntStream.range(0, 4).forEach(i -> verify(consumers[i],
                times(1024 * (i + 1))).accept(anyInt()));
    }

    @Test
    void forEachRemaining_IntConsumer__passedNull__throwsException() {
        var collection = mock(PrimitiveCollection.OfInt.class);
        var cut = PrimitiveCollections.intSpliterator(collection, 0);

        var t = catchThrowable(() -> cut.forEachRemaining((IntConsumer) null));

        assertThat(t).isInstanceOf(NullPointerException.class);
    }

    @Test
    void forEachRemaining_IntConsumer__collectionWithElements__processesAllElements() {
        var consumer = mock(IntConsumer.class);
        var collection = mock(PrimitiveCollection.OfInt.class);
        var cut = PrimitiveCollections.intSpliterator(collection, 0);
        when(collection.size()).thenReturn(5);
        when(collection.iterator()).thenReturn(IntStream.of(
                1, 2, 3, 4, 5).iterator());

        cut.forEachRemaining(consumer);

        verify(consumer).accept(1);
        verify(consumer).accept(2);
        verify(consumer).accept(3);
        verify(consumer).accept(4);
        verify(consumer).accept(5);
        verify(collection).size();
        verify(collection).iterator();
        verifyNoMoreInteractions(collection, consumer);
    }

    @Test
    void tryAdvance__emptyCollection__returnsFalse() {

    }
}
