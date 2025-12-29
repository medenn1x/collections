package net.arinoru.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.function.IntPredicate;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

// Tests for default methods of PrimitiveCollection
@SuppressWarnings("unchecked")
class PrimitiveCollectionDefaultTest {
    @Test
    void forEach_Consumer__always__delegatesToSpliterator() {
        var consumer = (Consumer<Integer>) mock(Consumer.class);
        var collection = (PrimitiveCollection<Integer,int[],IntConsumer,IntPredicate,
                PrimitiveIterator.OfInt,Spliterator.OfInt,IntStream,?>)
                mock(PrimitiveCollection.class);
        var spliterator = mock(Spliterator.OfInt.class);
        doCallRealMethod().when(collection).forEach(consumer);
        when(collection.spliterator()).thenReturn(spliterator);

        collection.forEach(consumer);

        verify(collection).forEach(consumer);
        verify(collection).spliterator();
        verify(spliterator).forEachRemaining(consumer);
        verifyNoMoreInteractions(consumer, collection, spliterator);
    }

    @Test
    void forEach_T_CONS__always__delegatesToSpliterator() {
        var consumer = mock(IntConsumer.class);
        var collection = (PrimitiveCollection<Integer,int[],IntConsumer,IntPredicate,
                PrimitiveIterator.OfInt,Spliterator.OfInt,IntStream,?>)
                mock(PrimitiveCollection.class);
        var spliterator = mock(Spliterator.OfInt.class);
        doCallRealMethod().when(collection).forEach(consumer);
        when(collection.spliterator()).thenReturn(spliterator);

        collection.forEach(consumer);

        verify(collection).forEach(consumer);
        verify(collection).spliterator();
        verify(spliterator).forEachRemaining(consumer);
        verifyNoMoreInteractions(consumer, collection, spliterator);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 5})
    void isEmpty__always__comparesResultOfSize(int size) {
        var collection = (PrimitiveCollection<Integer,int[],IntConsumer,IntPredicate,
                PrimitiveIterator.OfInt,Spliterator.OfInt,IntStream,?>)
                mock(PrimitiveCollection.class);
        doCallRealMethod().when(collection).isEmpty();
        when(collection.size()).thenReturn(size);

        var result = collection.isEmpty();

        assertThat(result).isEqualTo(size == 0);
        verify(collection).isEmpty();
        verify(collection).size();
        verifyNoMoreInteractions(collection);
    }
}
