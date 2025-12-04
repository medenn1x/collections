package net.arinoru.collections;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Collection;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.IntFunction;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
public class DoubleSingletonTest {
    @Test
    void contains__matchingElement__returnsTrue() {
        var cut = PrimitiveCollections.singleton(1.0);

        var result = cut.contains(1.0);

        assertThat(result).isTrue();
    }

    @Test
    void contains__nonMatchingElement__returnsFalse() {
        var cut = PrimitiveCollections.singleton(1.0);

        var result = cut.contains(2.0);

        assertThat(result).isFalse();
    }

    @Test
    void containsAll_Collection__emptyCollection__returnsTrue() {
        var collection = (Collection<Double>) mock(Collection.class);
        var cut = PrimitiveCollections.singleton(1.0);

        var result = cut.containsAll(collection);

        assertThat(result).isTrue();
        verify(collection).size();
        verifyNoMoreInteractions(collection);
    }

    @ParameterizedTest
    @ValueSource(booleans = { true, false })
    void containsAll_Collection__singleElementCollection__returnsTrueOnlyIfCollectionContainsValue(boolean contains) {
        var collection = (Collection<Double>) mock(Collection.class);
        var cut = PrimitiveCollections.singleton(1.0);
        when(collection.size()).thenReturn(1);
        when(collection.contains(1.0)).thenReturn(contains);

        var result = cut.containsAll(collection);

        assertThat(result).isEqualTo(contains);
        verify(collection).size();
        verify(collection).contains(1.0);
        verifyNoMoreInteractions(collection);
    }

    @Test
    void containsAll_Collection__multiElementCollection__returnsFalse() {
        var collection = (Collection<Double>) mock(Collection.class);
        var cut = PrimitiveCollections.singleton(1.0);
        when(collection.size()).thenReturn(2);

        var result = cut.containsAll(collection);

        assertThat(result).isFalse();
        verify(collection).size();
        verifyNoMoreInteractions(collection);
    }

    @Test
    void containsAll_Collection__emptyDoubleCollection__returnsTrue() {
        var collection = mock(PrimitiveCollection.OfDouble.class);
        var cut = spy(PrimitiveCollections.singleton(1.0));

        var result = cut.containsAll((Collection<Double>) collection);

        assertThat(result).isTrue();
        verify(collection).size();
        verify(cut).containsAll((Collection<Double>) collection);
        verify(cut).containsAll(collection);
        verifyNoMoreInteractions(collection);
    }

    @ParameterizedTest
    @ValueSource(booleans = { true, false })
    void containsAll_Collection__singleElementDoubleCollection__returnsTrueOnlyIfCollectionContainsValue(boolean contains) {
        var collection = mock(PrimitiveCollection.OfDouble.class);
        var cut = spy(PrimitiveCollections.singleton(1.0));
        when(collection.size()).thenReturn(1);
        when(collection.containsDouble(1.0)).thenReturn(contains);

        var result = cut.containsAll((Collection<Double>) collection);

        assertThat(result).isEqualTo(contains);
        verify(collection).size();
        verify(collection).containsDouble(1.0);
        verify(cut).containsAll((Collection<Double>) collection);
        verify(cut).containsAll(collection);
        verifyNoMoreInteractions(collection);
    }

    @Test
    void containsAll_Collection__multiElementDoubleCollection__returnsFalse() {
        var collection = mock(PrimitiveCollection.OfDouble.class);
        var cut = spy(PrimitiveCollections.singleton(1.0));
        when(collection.size()).thenReturn(2);

        var result = cut.containsAll((Collection<Double>) collection);

        assertThat(result).isFalse();
        verify(collection).size();
        verify(cut).containsAll((Collection<Double>) collection);
        verify(cut).containsAll(collection);
        verifyNoMoreInteractions(collection);
    }

    @Test
    void containsAll_OfDouble__emptyDoubleCollection__returnsTrue() {
        var collection = mock(PrimitiveCollection.OfDouble.class);
        var cut = PrimitiveCollections.singleton(1.0);

        var result = cut.containsAll(collection);

        assertThat(result).isTrue();
        verify(collection).size();
        verifyNoMoreInteractions(collection);
    }

    @ParameterizedTest
    @ValueSource(booleans = { true, false })
    void containsAll_OfDouble__singleElementDoubleCollection__returnsTrueOnlyIfCollectionContainsValue(boolean contains) {
        var collection = mock(PrimitiveCollection.OfDouble.class);
        var cut = PrimitiveCollections.singleton(1.0);
        when(collection.size()).thenReturn(1);
        when(collection.containsDouble(1.0)).thenReturn(contains);

        var result = cut.containsAll(collection);

        assertThat(result).isEqualTo(contains);
        verify(collection).size();
        verify(collection).containsDouble(1.0);
        verifyNoMoreInteractions(collection);
    }

    @Test
    void containsAll_OfDouble__multiElementDoubleCollection__returnsFalse() {
        var collection = mock(PrimitiveCollection.OfDouble.class);
        var cut = PrimitiveCollections.singleton(1.0);
        when(collection.size()).thenReturn(2);

        var result = cut.containsAll(collection);

        assertThat(result).isFalse();
        verify(collection).size();
        verifyNoMoreInteractions(collection);
    }

    @Test
    void containsDouble__matchingElement__returnsTrue() {
        var cut = PrimitiveCollections.singleton(1.0);

        var result = cut.containsDouble(1.0);

        assertThat(result).isTrue();
    }

    @Test
    void containsDouble__nonMatchingElement__returnsFalse() {
        var cut = PrimitiveCollections.singleton(1.0);

        var result = cut.containsDouble(2.0);

        assertThat(result).isFalse();
    }

    @Test
    @SuppressWarnings("EqualsWithItself")
    void equals__comparedToSelf__returnsTrue() {
        var cut = PrimitiveCollections.singleton(1.0);

        var result = cut.equals(cut);

        assertThat(result).isTrue();
    }

    @Test
    void equals__comparedToDoubleCollection__returnsFalse() {
        var collection = mock(PrimitiveCollection.OfDouble.class);
        var cut = PrimitiveCollections.singleton(1.0);

        var result = cut.equals(collection);

        assertThat(result).isFalse();
        verifyNoMoreInteractions(collection);
    }

    @Test
    void equals__comparedToEmptySet__returnsFalse() {
        var set = mock(Set.class);
        var cut = PrimitiveCollections.singleton(1.0);

        var result = cut.equals(set);

        assertThat(result).isFalse();
        verify(set).size();
        verifyNoMoreInteractions(set);
    }

    @Test
    void equals__comparedToEmptyDoubleSet__returnsFalse() {
        var set = mock(PrimitiveSet.OfDouble.class);
        var cut = PrimitiveCollections.singleton(1.0);

        var result = cut.equals(set);

        assertThat(result).isFalse();
        verify(set).size();
        verifyNoMoreInteractions(set);
    }

    @ParameterizedTest
    @ValueSource(booleans = { true, false })
    void equals__comparedToSingleElementSet__returnsTrueIfElementMatches(boolean match) {
        var set = mock(Set.class);
        var cut = PrimitiveCollections.singleton(1.0);
        when(set.size()).thenReturn(1);
        when(set.contains(1.0)).thenReturn(match);

        var result = cut.equals(set);

        assertThat(result).isEqualTo(match);
        verify(set).size();
        verify(set).contains(1.0);
        verifyNoMoreInteractions(set);
    }

    @ParameterizedTest
    @ValueSource(booleans = { true, false })
    void equals__comparedToSingleElementDoubleSet__returnsTrueIfElementMatches(boolean match) {
        var set = mock(PrimitiveSet.OfDouble.class);
        var cut = PrimitiveCollections.singleton(1.0);
        when(set.size()).thenReturn(1);
        when(set.containsDouble(1.0)).thenReturn(match);

        var result = cut.equals(set);

        assertThat(result).isEqualTo(match);
        verify(set).size();
        verify(set).containsDouble(1.0);
        verifyNoMoreInteractions(set);
    }

    @Test
    void equals__comparedToMultiElementSet__returnsFalse() {
        var set = mock(Set.class);
        var cut = PrimitiveCollections.singleton(1.0);
        when(set.size()).thenReturn(2);

        var result = cut.equals(set);

        assertThat(result).isFalse();
        verify(set).size();
        verifyNoMoreInteractions(set);
    }

    @Test
    void equals__comparedToMultiElementDoubleSet__returnsFalse() {
        var set = mock(PrimitiveSet.OfDouble.class);
        var cut = PrimitiveCollections.singleton(1.0);
        when(set.size()).thenReturn(2);

        var result = cut.equals(set);

        assertThat(result).isFalse();
        verify(set).size();
        verifyNoMoreInteractions(set);
    }

    @Test
    void forEach_Consumer__always__callsConsumerWithValue() {
        var consumer = (Consumer<Double>) mock(Consumer.class);
        var cut = PrimitiveCollections.singleton(1.0);

        cut.forEach(consumer);

        verify(consumer).accept(1.0);
        verifyNoMoreInteractions(consumer);
    }

    @Test
    void forEach_DoubleConsumer__always__callsDoubleConsumerWithValue() {
        var consumer = mock(DoubleConsumer.class);
        var cut = PrimitiveCollections.singleton(1.0);

        cut.forEach(consumer);

        verify(consumer).accept(1.0);
        verifyNoMoreInteractions(consumer);
    }

    @Test
    void hashCode__always__returnsHashCodeOfValue() {
        var cut = PrimitiveCollections.singleton(1.0);

        var result = cut.hashCode();

        assertThat(result).isEqualTo(Double.hashCode(1.0));
    }

    @Test
    void isEmpty__always__returnsFalse() {
        var cut = PrimitiveCollections.singleton(1.0);

        var result = cut.isEmpty();

        assertThat(result).isFalse();
    }

    @Test
    void iterator__always__returnsSingletonIterator() {
        var cut = PrimitiveCollections.singleton(1.0);

        var iterator = cut.iterator();
        var initialHasNext = iterator.hasNext();
        var element = iterator.nextDouble();
        var finalHasNext = iterator.hasNext();

        assertThat(initialHasNext).isTrue();
        assertThat(finalHasNext).isFalse();
        assertThat(element).isEqualTo(1.0);
    }

    @Test
    void parallelPrimitiveStream__always__returnsSingletonStream() {
        var cut = PrimitiveCollections.singleton(1.0);

        var result = cut.parallelPrimitiveStream();

        assertThat(result.toArray()).containsExactly(1.0);
    }

    @Test
    void parallelStream__always__returnsSingletonStream() {
        var cut = PrimitiveCollections.singleton(1.0);

        var result = cut.parallelStream();

        assertThat(result.toArray()).containsExactly(1.0);
    }

    @Test
    void primitiveStream__always__returnsSingletonStream() {
        var cut = PrimitiveCollections.singleton(1.0);

        var result = cut.primitiveStream();

        assertThat(result.toArray()).containsExactly(1.0);
    }

    @Test
    void size__always__returnsOne() {
        var cut = PrimitiveCollections.singleton(1.0);

        var result = cut.size();

        assertThat(result).isOne();
    }

    @Test
    void spliterator__always__returnsSingletonSpliterator() {
        var consumer = mock(DoubleConsumer.class);
        var cut = PrimitiveCollections.singleton(1.0);

        var spliterator = cut.spliterator();
        var estimatedSize = spliterator.estimateSize();
        spliterator.forEachRemaining(consumer);

        assertThat(estimatedSize).isOne();
        verify(consumer).accept(1.0);
        verifyNoMoreInteractions(consumer);
    }

    @Test
    void stream__always__returnsSingletonStream() {
        var cut = PrimitiveCollections.singleton(1.0);

        var result = cut.stream();

        assertThat(result.toArray()).containsExactly(1.0);
    }

    @Test
    void toArray__always__returnsSingleElementArray() {
        var cut = PrimitiveCollections.singleton(1.0);

        var result = cut.toArray();

        assertThat(result).isInstanceOf(Object[].class).hasSize(1)
                .containsExactly(1.0);
    }

    @Test
    void toArray_array__providedEmptyArray__returnsArrayOfProvidedComponentType() {
        var arr = new Double[0];
        var cut = PrimitiveCollections.singleton(1.0);

        var result = cut.toArray(arr);

        assertThat(result).isInstanceOf(Double[].class).hasSize(1)
                .containsExactly(1.0);
    }

    @Test
    void toArray_array__providedSingleElementArray__populatesAndReturns() {
        var arr = new Double[1];
        var cut = PrimitiveCollections.singleton(1.0);

        var result = cut.toArray(arr);

        assertThat(result).isSameAs(arr).containsExactly(1.0);
    }

    @Test
    void toArray_array__providedMultiElementArray__populatesAndMarksEnd() {
        var arr = Stream.generate(() -> 0.0).limit(5).toArray(Double[]::new);
        var cut = PrimitiveCollections.singleton(1.0);

        var result = cut.toArray(arr);

        assertThat(result).isSameAs(arr)
                .containsExactly(1.0, null, 0.0, 0.0, 0.0);
    }

    @Test
    void toArray_IntFunction__always__callsGeneratorWithSize() {
        var generator = (IntFunction<Double[]>) mock(IntFunction.class);
        var arr = new Double[1];
        var cut = PrimitiveCollections.singleton(1.0);
        when(generator.apply(1)).thenReturn(arr);

        var result = cut.toArray(generator);

        assertThat(result).isSameAs(arr).containsExactly(1.0);
    }

    @Test
    void toPrimitiveArray__always__returnsSingleElementArray() {
        var cut = PrimitiveCollections.singleton(1.0);

        var result = cut.toPrimitiveArray();

        assertThat(result).isInstanceOf(double[].class).hasSize(1)
                .containsExactly(1.0);
    }
}
