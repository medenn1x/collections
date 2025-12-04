package net.arinoru.collections;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Collection;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.LongConsumer;
import java.util.function.IntFunction;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
public class LongSingletonTest {
    @Test
    void contains__matchingElement__returnsTrue() {
        var cut = PrimitiveCollections.singleton(1L);

        var result = cut.contains(1L);

        assertThat(result).isTrue();
    }

    @Test
    void contains__nonMatchingElement__returnsFalse() {
        var cut = PrimitiveCollections.singleton(1L);

        var result = cut.contains(2L);

        assertThat(result).isFalse();
    }

    @Test
    void containsAll_Collection__emptyCollection__returnsTrue() {
        var collection = (Collection<Long>) mock(Collection.class);
        var cut = PrimitiveCollections.singleton(1L);

        var result = cut.containsAll(collection);

        assertThat(result).isTrue();
        verify(collection).size();
        verifyNoMoreInteractions(collection);
    }

    @ParameterizedTest
    @ValueSource(booleans = { true, false })
    void containsAll_Collection__singleElementCollection__returnsTrueOnlyIfCollectionContainsValue(boolean contains) {
        var collection = (Collection<Long>) mock(Collection.class);
        var cut = PrimitiveCollections.singleton(1L);
        when(collection.size()).thenReturn(1);
        when(collection.contains(1L)).thenReturn(contains);

        var result = cut.containsAll(collection);

        assertThat(result).isEqualTo(contains);
        verify(collection).size();
        verify(collection).contains(1L);
        verifyNoMoreInteractions(collection);
    }

    @Test
    void containsAll_Collection__multiElementCollection__returnsFalse() {
        var collection = (Collection<Long>) mock(Collection.class);
        var cut = PrimitiveCollections.singleton(1L);
        when(collection.size()).thenReturn(2);

        var result = cut.containsAll(collection);

        assertThat(result).isFalse();
        verify(collection).size();
        verifyNoMoreInteractions(collection);
    }

    @Test
    void containsAll_Collection__emptyLongCollection__returnsTrue() {
        var collection = mock(PrimitiveCollection.OfLong.class);
        var cut = spy(PrimitiveCollections.singleton(1L));

        var result = cut.containsAll((Collection<Long>) collection);

        assertThat(result).isTrue();
        verify(collection).size();
        verify(cut).containsAll((Collection<Long>) collection);
        verify(cut).containsAll(collection);
        verifyNoMoreInteractions(collection);
    }

    @ParameterizedTest
    @ValueSource(booleans = { true, false })
    void containsAll_Collection__singleElementLongCollection__returnsTrueOnlyIfCollectionContainsValue(boolean contains) {
        var collection = mock(PrimitiveCollection.OfLong.class);
        var cut = spy(PrimitiveCollections.singleton(1L));
        when(collection.size()).thenReturn(1);
        when(collection.containsLong(1L)).thenReturn(contains);

        var result = cut.containsAll((Collection<Long>) collection);

        assertThat(result).isEqualTo(contains);
        verify(collection).size();
        verify(collection).containsLong(1L);
        verify(cut).containsAll((Collection<Long>) collection);
        verify(cut).containsAll(collection);
        verifyNoMoreInteractions(collection);
    }

    @Test
    void containsAll_Collection__multiElementLongCollection__returnsFalse() {
        var collection = mock(PrimitiveCollection.OfLong.class);
        var cut = spy(PrimitiveCollections.singleton(1L));
        when(collection.size()).thenReturn(2);

        var result = cut.containsAll((Collection<Long>) collection);

        assertThat(result).isFalse();
        verify(collection).size();
        verify(cut).containsAll((Collection<Long>) collection);
        verify(cut).containsAll(collection);
        verifyNoMoreInteractions(collection);
    }

    @Test
    void containsAll_OfLong__emptyLongCollection__returnsTrue() {
        var collection = mock(PrimitiveCollection.OfLong.class);
        var cut = PrimitiveCollections.singleton(1L);

        var result = cut.containsAll(collection);

        assertThat(result).isTrue();
        verify(collection).size();
        verifyNoMoreInteractions(collection);
    }

    @ParameterizedTest
    @ValueSource(booleans = { true, false })
    void containsAll_OfLong__singleElementLongCollection__returnsTrueOnlyIfCollectionContainsValue(boolean contains) {
        var collection = mock(PrimitiveCollection.OfLong.class);
        var cut = PrimitiveCollections.singleton(1L);
        when(collection.size()).thenReturn(1);
        when(collection.containsLong(1L)).thenReturn(contains);

        var result = cut.containsAll(collection);

        assertThat(result).isEqualTo(contains);
        verify(collection).size();
        verify(collection).containsLong(1L);
        verifyNoMoreInteractions(collection);
    }

    @Test
    void containsAll_OfLong__multiElementLongCollection__returnsFalse() {
        var collection = mock(PrimitiveCollection.OfLong.class);
        var cut = PrimitiveCollections.singleton(1L);
        when(collection.size()).thenReturn(2);

        var result = cut.containsAll(collection);

        assertThat(result).isFalse();
        verify(collection).size();
        verifyNoMoreInteractions(collection);
    }

    @Test
    void containsLong__matchingElement__returnsTrue() {
        var cut = PrimitiveCollections.singleton(1L);

        var result = cut.containsLong(1L);

        assertThat(result).isTrue();
    }

    @Test
    void containsLong__nonMatchingElement__returnsFalse() {
        var cut = PrimitiveCollections.singleton(1L);

        var result = cut.containsLong(2L);

        assertThat(result).isFalse();
    }

    @Test
    @SuppressWarnings("EqualsWithItself")
    void equals__comparedToSelf__returnsTrue() {
        var cut = PrimitiveCollections.singleton(1L);

        var result = cut.equals(cut);

        assertThat(result).isTrue();
    }

    @Test
    void equals__comparedToLongCollection__returnsFalse() {
        var collection = mock(PrimitiveCollection.OfLong.class);
        var cut = PrimitiveCollections.singleton(1L);

        var result = cut.equals(collection);

        assertThat(result).isFalse();
        verifyNoMoreInteractions(collection);
    }

    @Test
    void equals__comparedToEmptySet__returnsFalse() {
        var set = mock(Set.class);
        var cut = PrimitiveCollections.singleton(1L);

        var result = cut.equals(set);

        assertThat(result).isFalse();
        verify(set).size();
        verifyNoMoreInteractions(set);
    }

    @Test
    void equals__comparedToEmptyLongSet__returnsFalse() {
        var set = mock(PrimitiveSet.OfLong.class);
        var cut = PrimitiveCollections.singleton(1L);

        var result = cut.equals(set);

        assertThat(result).isFalse();
        verify(set).size();
        verifyNoMoreInteractions(set);
    }

    @ParameterizedTest
    @ValueSource(booleans = { true, false })
    void equals__comparedToSingleElementSet__returnsTrueIfElementMatches(boolean match) {
        var set = mock(Set.class);
        var cut = PrimitiveCollections.singleton(1L);
        when(set.size()).thenReturn(1);
        when(set.contains(1L)).thenReturn(match);

        var result = cut.equals(set);

        assertThat(result).isEqualTo(match);
        verify(set).size();
        verify(set).contains(1L);
        verifyNoMoreInteractions(set);
    }

    @ParameterizedTest
    @ValueSource(booleans = { true, false })
    void equals__comparedToSingleElementLongSet__returnsTrueIfElementMatches(boolean match) {
        var set = mock(PrimitiveSet.OfLong.class);
        var cut = PrimitiveCollections.singleton(1L);
        when(set.size()).thenReturn(1);
        when(set.containsLong(1L)).thenReturn(match);

        var result = cut.equals(set);

        assertThat(result).isEqualTo(match);
        verify(set).size();
        verify(set).containsLong(1L);
        verifyNoMoreInteractions(set);
    }

    @Test
    void equals__comparedToMultiElementSet__returnsFalse() {
        var set = mock(Set.class);
        var cut = PrimitiveCollections.singleton(1L);
        when(set.size()).thenReturn(2);

        var result = cut.equals(set);

        assertThat(result).isFalse();
        verify(set).size();
        verifyNoMoreInteractions(set);
    }

    @Test
    void equals__comparedToMultiElementLongSet__returnsFalse() {
        var set = mock(PrimitiveSet.OfLong.class);
        var cut = PrimitiveCollections.singleton(1L);
        when(set.size()).thenReturn(2);

        var result = cut.equals(set);

        assertThat(result).isFalse();
        verify(set).size();
        verifyNoMoreInteractions(set);
    }

    @Test
    void forEach_Consumer__always__callsConsumerWithValue() {
        var consumer = (Consumer<Long>) mock(Consumer.class);
        var cut = PrimitiveCollections.singleton(1L);

        cut.forEach(consumer);

        verify(consumer).accept(1L);
        verifyNoMoreInteractions(consumer);
    }

    @Test
    void forEach_LongConsumer__always__callsLongConsumerWithValue() {
        var consumer = mock(LongConsumer.class);
        var cut = PrimitiveCollections.singleton(1L);

        cut.forEach(consumer);

        verify(consumer).accept(1L);
        verifyNoMoreInteractions(consumer);
    }

    @Test
    void hashCode__always__returnsHashCodeOfValue() {
        var cut = PrimitiveCollections.singleton(1L);

        var result = cut.hashCode();

        assertThat(result).isEqualTo(Long.hashCode(1L));
    }

    @Test
    void isEmpty__always__returnsFalse() {
        var cut = PrimitiveCollections.singleton(1L);

        var result = cut.isEmpty();

        assertThat(result).isFalse();
    }

    @Test
    void iterator__always__returnsSingletonIterator() {
        var cut = PrimitiveCollections.singleton(1L);

        var iterator = cut.iterator();
        var initialHasNext = iterator.hasNext();
        var element = iterator.nextLong();
        var finalHasNext = iterator.hasNext();

        assertThat(initialHasNext).isTrue();
        assertThat(finalHasNext).isFalse();
        assertThat(element).isEqualTo(1L);
    }

    @Test
    void parallelPrimitiveStream__always__returnsSingletonStream() {
        var cut = PrimitiveCollections.singleton(1L);

        var result = cut.parallelPrimitiveStream();

        assertThat(result.toArray()).containsExactly(1L);
    }

    @Test
    void parallelStream__always__returnsSingletonStream() {
        var cut = PrimitiveCollections.singleton(1L);

        var result = cut.parallelStream();

        assertThat(result.toArray()).containsExactly(1L);
    }

    @Test
    void primitiveStream__always__returnsSingletonStream() {
        var cut = PrimitiveCollections.singleton(1L);

        var result = cut.primitiveStream();

        assertThat(result.toArray()).containsExactly(1L);
    }

    @Test
    void size__always__returnsOne() {
        var cut = PrimitiveCollections.singleton(1L);

        var result = cut.size();

        assertThat(result).isOne();
    }

    @Test
    void spliterator__always__returnsSingletonSpliterator() {
        var consumer = mock(LongConsumer.class);
        var cut = PrimitiveCollections.singleton(1L);

        var spliterator = cut.spliterator();
        var estimatedSize = spliterator.estimateSize();
        spliterator.forEachRemaining(consumer);

        assertThat(estimatedSize).isOne();
        verify(consumer).accept(1L);
        verifyNoMoreInteractions(consumer);
    }

    @Test
    void stream__always__returnsSingletonStream() {
        var cut = PrimitiveCollections.singleton(1L);

        var result = cut.stream();

        assertThat(result.toArray()).containsExactly(1L);
    }

    @Test
    void toArray__always__returnsSingleElementArray() {
        var cut = PrimitiveCollections.singleton(1L);

        var result = cut.toArray();

        assertThat(result).isInstanceOf(Object[].class).hasSize(1)
                .containsExactly(1L);
    }

    @Test
    void toArray_array__providedEmptyArray__returnsArrayOfProvidedComponentType() {
        var arr = new Long[0];
        var cut = PrimitiveCollections.singleton(1L);

        var result = cut.toArray(arr);

        assertThat(result).isInstanceOf(Long[].class).hasSize(1)
                .containsExactly(1L);
    }

    @Test
    void toArray_array__providedSingleElementArray__populatesAndReturns() {
        var arr = new Long[1];
        var cut = PrimitiveCollections.singleton(1L);

        var result = cut.toArray(arr);

        assertThat(result).isSameAs(arr).containsExactly(1L);
    }

    @Test
    void toArray_array__providedMultiElementArray__populatesAndMarksEnd() {
        var arr = Stream.generate(() -> 0L).limit(5).toArray(Long[]::new);
        var cut = PrimitiveCollections.singleton(1L);

        var result = cut.toArray(arr);

        assertThat(result).isSameAs(arr)
                .containsExactly(1L, null, 0L, 0L, 0L);
    }

    @Test
    void toArray_IntFunction__always__callsGeneratorWithSize() {
        var generator = (IntFunction<Long[]>) mock(IntFunction.class);
        var arr = new Long[1];
        var cut = PrimitiveCollections.singleton(1L);
        when(generator.apply(1)).thenReturn(arr);

        var result = cut.toArray(generator);

        assertThat(result).isSameAs(arr).containsExactly(1L);
    }

    @Test
    void toPrimitiveArray__always__returnsSingleElementArray() {
        var cut = PrimitiveCollections.singleton(1L);

        var result = cut.toPrimitiveArray();

        assertThat(result).isInstanceOf(long[].class).hasSize(1)
                .containsExactly(1L);
    }
}
