package net.arinoru.collections;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Collection;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.function.IntFunction;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
public class IntSingletonTest {
    @Test
    void contains__matchingElement__returnsTrue() {
        var cut = PrimitiveCollections.singleton(1);

        var result = cut.contains(1);

        assertThat(result).isTrue();
    }

    @Test
    void contains__nonMatchingElement__returnsFalse() {
        var cut = PrimitiveCollections.singleton(1);

        var result = cut.contains(2);

        assertThat(result).isFalse();
    }

    @Test
    void containsAll_Collection__emptyCollection__returnsTrue() {
        var collection = (Collection<Integer>) mock(Collection.class);
        var cut = PrimitiveCollections.singleton(1);

        var result = cut.containsAll(collection);

        assertThat(result).isTrue();
        verify(collection).size();
        verifyNoMoreInteractions(collection);
    }

    @ParameterizedTest
    @ValueSource(booleans = { true, false })
    void containsAll_Collection__singleElementCollection__returnsTrueOnlyIfCollectionContainsValue(boolean contains) {
        var collection = (Collection<Integer>) mock(Collection.class);
        var cut = PrimitiveCollections.singleton(1);
        when(collection.size()).thenReturn(1);
        when(collection.contains(1)).thenReturn(contains);

        var result = cut.containsAll(collection);

        assertThat(result).isEqualTo(contains);
        verify(collection).size();
        verify(collection).contains(1);
        verifyNoMoreInteractions(collection);
    }

    @Test
    void containsAll_Collection__multiElementCollection__returnsFalse() {
        var collection = (Collection<Integer>) mock(Collection.class);
        var cut = PrimitiveCollections.singleton(1);
        when(collection.size()).thenReturn(2);

        var result = cut.containsAll(collection);

        assertThat(result).isFalse();
        verify(collection).size();
        verifyNoMoreInteractions(collection);
    }

    @Test
    void containsAll_Collection__emptyIntCollection__returnsTrue() {
        var collection = mock(PrimitiveCollection.OfInt.class);
        var cut = spy(PrimitiveCollections.singleton(1));

        var result = cut.containsAll((Collection<Integer>) collection);

        assertThat(result).isTrue();
        verify(collection).size();
        verify(cut).containsAll((Collection<Integer>) collection);
        verify(cut).containsAll(collection);
        verifyNoMoreInteractions(collection);
    }

    @ParameterizedTest
    @ValueSource(booleans = { true, false })
    void containsAll_Collection__singleElementIntCollection__returnsTrueOnlyIfCollectionContainsValue(boolean contains) {
        var collection = mock(PrimitiveCollection.OfInt.class);
        var cut = spy(PrimitiveCollections.singleton(1));
        when(collection.size()).thenReturn(1);
        when(collection.containsInt(1)).thenReturn(contains);

        var result = cut.containsAll((Collection<Integer>) collection);

        assertThat(result).isEqualTo(contains);
        verify(collection).size();
        verify(collection).containsInt(1);
        verify(cut).containsAll((Collection<Integer>) collection);
        verify(cut).containsAll(collection);
        verifyNoMoreInteractions(collection);
    }

    @Test
    void containsAll_Collection__multiElementIntCollection__returnsFalse() {
        var collection = mock(PrimitiveCollection.OfInt.class);
        var cut = spy(PrimitiveCollections.singleton(1));
        when(collection.size()).thenReturn(2);

        var result = cut.containsAll((Collection<Integer>) collection);

        assertThat(result).isFalse();
        verify(collection).size();
        verify(cut).containsAll((Collection<Integer>) collection);
        verify(cut).containsAll(collection);
        verifyNoMoreInteractions(collection);
    }

    @Test
    void containsAll_OfInt__emptyIntCollection__returnsTrue() {
        var collection = mock(PrimitiveCollection.OfInt.class);
        var cut = PrimitiveCollections.singleton(1);

        var result = cut.containsAll(collection);

        assertThat(result).isTrue();
        verify(collection).size();
        verifyNoMoreInteractions(collection);
    }

    @ParameterizedTest
    @ValueSource(booleans = { true, false })
    void containsAll_OfInt__singleElementIntCollection__returnsTrueOnlyIfCollectionContainsValue(boolean contains) {
        var collection = mock(PrimitiveCollection.OfInt.class);
        var cut = PrimitiveCollections.singleton(1);
        when(collection.size()).thenReturn(1);
        when(collection.containsInt(1)).thenReturn(contains);

        var result = cut.containsAll(collection);

        assertThat(result).isEqualTo(contains);
        verify(collection).size();
        verify(collection).containsInt(1);
        verifyNoMoreInteractions(collection);
    }

    @Test
    void containsAll_OfInt__multiElementIntCollection__returnsFalse() {
        var collection = mock(PrimitiveCollection.OfInt.class);
        var cut = PrimitiveCollections.singleton(1);
        when(collection.size()).thenReturn(2);

        var result = cut.containsAll(collection);

        assertThat(result).isFalse();
        verify(collection).size();
        verifyNoMoreInteractions(collection);
    }

    @Test
    void containsInt__matchingElement__returnsTrue() {
        var cut = PrimitiveCollections.singleton(1);

        var result = cut.containsInt(1);

        assertThat(result).isTrue();
    }

    @Test
    void containsInt__nonMatchingElement__returnsFalse() {
        var cut = PrimitiveCollections.singleton(1);

        var result = cut.containsInt(2);

        assertThat(result).isFalse();
    }

    @Test
    @SuppressWarnings("EqualsWithItself")
    void equals__comparedToSelf__returnsTrue() {
        var cut = PrimitiveCollections.singleton(1);

        var result = cut.equals(cut);

        assertThat(result).isTrue();
    }

    @Test
    void equals__comparedToIntCollection__returnsFalse() {
        var collection = mock(PrimitiveCollection.OfInt.class);
        var cut = PrimitiveCollections.singleton(1);

        var result = cut.equals(collection);

        assertThat(result).isFalse();
        verifyNoMoreInteractions(collection);
    }

    @Test
    void equals__comparedToEmptySet__returnsFalse() {
        var set = mock(Set.class);
        var cut = PrimitiveCollections.singleton(1);

        var result = cut.equals(set);

        assertThat(result).isFalse();
        verify(set).size();
        verifyNoMoreInteractions(set);
    }

    @Test
    void equals__comparedToEmptyIntSet__returnsFalse() {
        var set = mock(PrimitiveSet.OfInt.class);
        var cut = PrimitiveCollections.singleton(1);

        var result = cut.equals(set);

        assertThat(result).isFalse();
        verify(set).size();
        verifyNoMoreInteractions(set);
    }

    @ParameterizedTest
    @ValueSource(booleans = { true, false })
    void equals__comparedToSingleElementSet__returnsTrueIfElementMatches(boolean match) {
        var set = mock(Set.class);
        var cut = PrimitiveCollections.singleton(1);
        when(set.size()).thenReturn(1);
        when(set.contains(1)).thenReturn(match);

        var result = cut.equals(set);

        assertThat(result).isEqualTo(match);
        verify(set).size();
        verify(set).contains(1);
        verifyNoMoreInteractions(set);
    }

    @ParameterizedTest
    @ValueSource(booleans = { true, false })
    void equals__comparedToSingleElementIntSet__returnsTrueIfElementMatches(boolean match) {
        var set = mock(PrimitiveSet.OfInt.class);
        var cut = PrimitiveCollections.singleton(1);
        when(set.size()).thenReturn(1);
        when(set.containsInt(1)).thenReturn(match);

        var result = cut.equals(set);

        assertThat(result).isEqualTo(match);
        verify(set).size();
        verify(set).containsInt(1);
        verifyNoMoreInteractions(set);
    }

    @Test
    void equals__comparedToMultiElementSet__returnsFalse() {
        var set = mock(Set.class);
        var cut = PrimitiveCollections.singleton(1);
        when(set.size()).thenReturn(2);

        var result = cut.equals(set);

        assertThat(result).isFalse();
        verify(set).size();
        verifyNoMoreInteractions(set);
    }

    @Test
    void equals__comparedToMultiElementIntSet__returnsFalse() {
        var set = mock(PrimitiveSet.OfInt.class);
        var cut = PrimitiveCollections.singleton(1);
        when(set.size()).thenReturn(2);

        var result = cut.equals(set);

        assertThat(result).isFalse();
        verify(set).size();
        verifyNoMoreInteractions(set);
    }

    @Test
    void forEach_Consumer__always__callsConsumerWithValue() {
        var consumer = (Consumer<Integer>) mock(Consumer.class);
        var cut = PrimitiveCollections.singleton(1);

        cut.forEach(consumer);

        verify(consumer).accept(1);
        verifyNoMoreInteractions(consumer);
    }

    @Test
    void forEach_IntConsumer__always__callsIntConsumerWithValue() {
        var consumer = mock(IntConsumer.class);
        var cut = PrimitiveCollections.singleton(1);

        cut.forEach(consumer);

        verify(consumer).accept(1);
        verifyNoMoreInteractions(consumer);
    }

    @Test
    void hashCode__always__returnsHashCodeOfValue() {
        var cut = PrimitiveCollections.singleton(1);

        var result = cut.hashCode();

        assertThat(result).isEqualTo(1);
    }

    @Test
    void isEmpty__always__returnsFalse() {
        var cut = PrimitiveCollections.singleton(1);

        var result = cut.isEmpty();

        assertThat(result).isFalse();
    }

    @Test
    void iterator__always__returnsSingletonIterator() {
        var cut = PrimitiveCollections.singleton(1);

        var iterator = cut.iterator();
        var initialHasNext = iterator.hasNext();
        var element = iterator.nextInt();
        var finalHasNext = iterator.hasNext();

        assertThat(initialHasNext).isTrue();
        assertThat(finalHasNext).isFalse();
        assertThat(element).isEqualTo(1);
    }

    @Test
    void parallelPrimitiveStream__always__returnsSingletonStream() {
        var cut = PrimitiveCollections.singleton(1);

        var result = cut.parallelPrimitiveStream();

        assertThat(result.toArray()).containsExactly(1);
    }

    @Test
    void parallelStream__always__returnsSingletonStream() {
        var cut = PrimitiveCollections.singleton(1);

        var result = cut.parallelStream();

        assertThat(result.toArray()).containsExactly(1);
    }

    @Test
    void primitiveStream__always__returnsSingletonStream() {
        var cut = PrimitiveCollections.singleton(1);

        var result = cut.primitiveStream();

        assertThat(result.toArray()).containsExactly(1);
    }

    @Test
    void size__always__returnsOne() {
        var cut = PrimitiveCollections.singleton(1);

        var result = cut.size();

        assertThat(result).isOne();
    }

    @Test
    void spliterator__always__returnsSingletonSpliterator() {
        var consumer = mock(IntConsumer.class);
        var cut = PrimitiveCollections.singleton(1);

        var spliterator = cut.spliterator();
        var estimatedSize = spliterator.estimateSize();
        spliterator.forEachRemaining(consumer);

        assertThat(estimatedSize).isOne();
        verify(consumer).accept(1);
        verifyNoMoreInteractions(consumer);
    }

    @Test
    void stream__always__returnsSingletonStream() {
        var cut = PrimitiveCollections.singleton(1);

        var result = cut.stream();

        assertThat(result.toArray()).containsExactly(1);
    }

    @Test
    void toArray__always__returnsSingleElementArray() {
        var cut = PrimitiveCollections.singleton(1);

        var result = cut.toArray();

        assertThat(result).isInstanceOf(Object[].class).hasSize(1)
                .containsExactly(1);
    }

    @Test
    void toArray_array__providedEmptyArray__returnsArrayOfProvidedComponentType() {
        var arr = new Integer[0];
        var cut = PrimitiveCollections.singleton(1);

        var result = cut.toArray(arr);

        assertThat(result).isInstanceOf(Integer[].class).hasSize(1)
                .containsExactly(1);
    }

    @Test
    void toArray_array__providedSingleElementArray__populatesAndReturns() {
        var arr = new Integer[1];
        var cut = PrimitiveCollections.singleton(1);

        var result = cut.toArray(arr);

        assertThat(result).isSameAs(arr).containsExactly(1);
    }

    @Test
    void toArray_array__providedMultiElementArray__populatesAndMarksEnd() {
        var arr = Stream.generate(() -> 0).limit(5).toArray(Integer[]::new);
        var cut = PrimitiveCollections.singleton(1);

        var result = cut.toArray(arr);

        assertThat(result).isSameAs(arr)
                .containsExactly(1, null, 0, 0, 0);
    }

    @Test
    void toArray_IntFunction__always__callsGeneratorWithSize() {
        var generator = (IntFunction<Integer[]>) mock(IntFunction.class);
        var arr = new Integer[1];
        var cut = PrimitiveCollections.singleton(1);
        when(generator.apply(1)).thenReturn(arr);

        var result = cut.toArray(generator);

        assertThat(result).isSameAs(arr).containsExactly(1);
    }

    @Test
    void toPrimitiveArray__always__returnsSingleElementArray() {
        var cut = PrimitiveCollections.singleton(1);

        var result = cut.toPrimitiveArray();

        assertThat(result).isInstanceOf(int[].class).hasSize(1)
                .containsExactly(1);
    }
}
