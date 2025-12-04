package net.arinoru.collections;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Collection;
import java.util.function.LongConsumer;
import java.util.function.LongPredicate;
import java.util.function.Predicate;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
public class EmptyLongCollectionTest {
    @Test
    void add__always__throwsException() {
        var cut = PrimitiveCollections.emptyLongCollection();

        var t = catchThrowable(() -> cut.add(1L));

        assertThat(t).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void addAll_Collection__always__throwsException() {
        var collection = (Collection<Long>) mock(Collection.class);
        var cut = PrimitiveCollections.emptyLongCollection();

        var t = catchThrowable(() -> cut.addAll(collection));

        assertThat(t).isInstanceOf(UnsupportedOperationException.class);
        verifyNoMoreInteractions(collection);
    }

    @Test
    void addAll_OfLong__always__throwsException() {
        var collection = mock(PrimitiveCollection.OfLong.class);
        var cut = PrimitiveCollections.emptyLongCollection();

        var t = catchThrowable(() -> cut.addAll(collection));

        assertThat(t).isInstanceOf(UnsupportedOperationException.class);
        verifyNoMoreInteractions(collection);
    }

    @Test
    void clear__always__throwsException() {
        var cut = PrimitiveCollections.emptyLongCollection();

        var t = catchThrowable(cut::clear);

        assertThat(t).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void contains__always__returnsFalse() {
        var cut = PrimitiveCollections.emptyLongCollection();

        var result = cut.contains(1L);

        assertThat(result).isFalse();
    }

    @ParameterizedTest
    @ValueSource(booleans = { true, false })
    void containsAll_Collection__always__returnsTrueOnlyIfComparedCollectionIsEmpty(boolean isEmpty) {
        var collection = (Collection<Long>) mock(Collection.class);
        var cut = PrimitiveCollections.emptyLongCollection();
        when(collection.isEmpty()).thenReturn(isEmpty);

        var result = cut.containsAll(collection);

        assertThat(result).isEqualTo(isEmpty);
        verify(collection).isEmpty();
        verifyNoMoreInteractions(collection);
    }

    @ParameterizedTest
    @ValueSource(booleans = { true, false })
    void containsAll_OfLong__always__returnsTrueOnlyIfComparedCollectionIsEmpty(boolean isEmpty) {
        var collection = mock(PrimitiveCollection.OfLong.class);
        var cut = PrimitiveCollections.emptyLongCollection();
        when(collection.isEmpty()).thenReturn(isEmpty);

        var result = cut.containsAll(collection);

        assertThat(result).isEqualTo(isEmpty);
        verify(collection).isEmpty();
        verifyNoMoreInteractions(collection);
    }

    @Test
    void containsLong__always__returnsFalse() {
        var cut = PrimitiveCollections.emptyLongCollection();

        var result = cut.containsLong(1L);

        assertThat(result).isFalse();
    }

    @Test
    @SuppressWarnings("EqualsWithItself")
    void equals__comparedToSelf__returnsTrue() {
        var cut = PrimitiveCollections.emptyLongCollection();

        var result = cut.equals(cut);

        assertThat(result).isTrue();
    }

    @Test
    void equals__comparedToAnotherCollection__returnsFalse() {
        var collection = mock(PrimitiveCollection.OfLong.class);
        var cut = PrimitiveCollections.emptyLongCollection();

        var result = cut.equals(collection);

        assertThat(result).isFalse();
        verifyNoMoreInteractions(collection);
    }

    @Test
    void isEmpty__always__returnsTrue() {
        var cut = PrimitiveCollections.emptyLongCollection();

        var result = cut.isEmpty();

        assertThat(result).isTrue();
    }

    @Test
    void iterator__always__returnsEmptyIterator() {
        var cut = PrimitiveCollections.emptyLongCollection();

        var result = cut.iterator();

        assertThat(result.hasNext()).isFalse();
    }

    @Test
    void parallelPrimitiveStream__always__returnsEmptyStream() {
        var cut = PrimitiveCollections.emptyLongCollection();

        var result = cut.parallelPrimitiveStream();

        assertThat(result.count()).isZero();
    }

    @Test
    void parallelStream__always__returnsEmptyStream() {
        var cut = PrimitiveCollections.emptyLongCollection();

        var result = cut.parallelStream();

        assertThat(result.count()).isZero();
    }

    @Test
    void primitiveStream__always__returnsEmptyStream() {
        var cut = PrimitiveCollections.emptyLongCollection();

        var result = cut.primitiveStream();

        assertThat(result.count()).isZero();
    }

    @Test
    void remove__always__throwsException() {
        var cut = PrimitiveCollections.emptyLongCollection();

        var t = catchThrowable(() -> cut.remove(1L));

        assertThat(t).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void removeAll_Collection__always__throwsException() {
        var collection = (Collection<Long>) mock(Collection.class);
        var cut = PrimitiveCollections.emptyLongCollection();

        var t = catchThrowable(() -> cut.removeAll(collection));

        assertThat(t).isInstanceOf(UnsupportedOperationException.class);
        verifyNoMoreInteractions(collection);
    }

    @Test
    void removeAll_OfLong__always__throwsException() {
        var collection = mock(PrimitiveCollection.OfLong.class);
        var cut = PrimitiveCollections.emptyLongCollection();

        var t = catchThrowable(() -> cut.removeAll(collection));

        assertThat(t).isInstanceOf(UnsupportedOperationException.class);
        verifyNoMoreInteractions(collection);
    }

    @Test
    void removeIf_LongPredicate__always__throwsException() {
        var predicate = mock(LongPredicate.class);
        var cut = PrimitiveCollections.emptyLongCollection();

        var t = catchThrowable(() -> cut.removeIf(predicate));

        assertThat(t).isInstanceOf(UnsupportedOperationException.class);
        verifyNoMoreInteractions(predicate);
    }

    @Test
    void removeIf_Predicate__always__throwsException() {
        var predicate = (Predicate<Long>) mock(Predicate.class);
        var cut = PrimitiveCollections.emptyLongCollection();

        var t = catchThrowable(() -> cut.removeIf(predicate));

        assertThat(t).isInstanceOf(UnsupportedOperationException.class);
        verifyNoMoreInteractions(predicate);
    }

    @Test
    void retainAll_Collection__always__throwsException() {
        var collection = (Collection<Long>) mock(Collection.class);
        var cut = PrimitiveCollections.emptyLongCollection();

        var t = catchThrowable(() -> cut.retainAll(collection));

        assertThat(t).isInstanceOf(UnsupportedOperationException.class);
        verifyNoMoreInteractions(collection);
    }

    @Test
    void retainAll_OfLong__always__throwsException() {
        var collection = mock(PrimitiveCollection.OfLong.class);
        var cut = PrimitiveCollections.emptyLongCollection();

        var t = catchThrowable(() -> cut.retainAll(collection));

        assertThat(t).isInstanceOf(UnsupportedOperationException.class);
        verifyNoMoreInteractions(collection);
    }

    @Test
    void size__always__returnsZero() {
        var cut = PrimitiveCollections.emptyLongCollection();

        var result = cut.size();

        assertThat(result).isZero();
    }

    @Test
    void spliterator__always__returnsEmptySpliterator() {
        var consumer = mock(LongConsumer.class);
        var cut = PrimitiveCollections.emptyLongCollection();

        var result = cut.spliterator();

        assertThat(result.estimateSize()).isZero();
        assertThat(result.tryAdvance(consumer)).isFalse();
        verifyNoMoreInteractions(consumer);
    }

    @Test
    void stream__always__returnsEmptyStream() {
        var cut = PrimitiveCollections.emptyLongCollection();

        var result = cut.stream();

        assertThat(result.count()).isZero();
    }

    @Test
    void toArray__always__returnsEmptyArray() {
        var cut = PrimitiveCollections.emptyLongCollection();

        var result = cut.toArray();

        assertThat(result).isInstanceOf(Object[].class).isEmpty();
    }

    @Test
    void toArray_array__providesEmptyArray__returnsProvidedArray() {
        var arr = new Long[0];
        var cut = PrimitiveCollections.emptyLongCollection();

        var result = cut.toArray(arr);

        assertThat(result).isSameAs(arr);
    }

    @Test
    void toArray_array__providesNonEmptyArray__setsFirstElementToNull() {
        var arr = IntStream.rangeClosed(1, 5).asLongStream().boxed()
                .toArray(Long[]::new);
        var cut = PrimitiveCollections.emptyLongCollection();

        var result = cut.toArray(arr);

        assertThat(result).isSameAs(arr).containsExactly(null, 2L, 3L, 4L, 5L);
    }

    @Test
    void toPrimitiveArray__always__returnsEmptyArray() {
        var cut = PrimitiveCollections.emptyLongCollection();

        var result = cut.toPrimitiveArray();

        assertThat(result).isInstanceOf(long[].class).isEmpty();
    }
}
