package net.arinoru.collections;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Collection;
import java.util.PrimitiveIterator;
import java.util.function.LongPredicate;
import java.util.function.Predicate;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.*;

// Tests for default methods of PrimitiveCollection.OfLong
@SuppressWarnings({"unchecked", "SuspiciousMethodCalls"})
class LongCollectionDefaultTest {
    @Test
    void add__always__delegatesToAddLong() {
        var collection = mock(PrimitiveCollection.OfLong.class);
        doCallRealMethod().when(collection).add(1L);
        when(collection.addLong(1L)).thenReturn(true);

        var result = collection.add(1L);

        assertThat(result).isTrue();
        verify(collection).add(1L);
        verify(collection).addLong(1L);
        verifyNoMoreInteractions(collection);
    }

    @Test
    void addAll_Collection__passedPrimitiveCollection__delegatesToAddAll_OfLong() {
        var collection1 = mock(PrimitiveCollection.OfLong.class);
        var collection2 = mock(PrimitiveCollection.OfLong.class);
        doCallRealMethod().when(collection1).addAll((Collection<Long>) collection2);
        when(collection1.addAll(collection2)).thenReturn(true);

        var result = collection1.addAll((Collection<Long>) collection2);

        assertThat(result).isTrue();
        verify(collection1).addAll((Collection<Long>) collection2);
        verify(collection1).addAll(collection2);
        verifyNoMoreInteractions(collection1, collection2);
    }

    @Test
    void addAll_Collection__passedBoxedCollection__iteratesAndCallsAddLong() {
        var collection1 = mock(PrimitiveCollection.OfLong.class);
        var collection2 = (Collection<Long>) mock(Collection.class);
        doCallRealMethod().when(collection1).addAll(collection2);
        when(collection2.iterator()).thenReturn(Stream.of(1L, 2L).iterator());
        when(collection1.addLong(anyLong())).thenReturn(true);

        var result = collection1.addAll(collection2);

        assertThat(result).isTrue();
        verify(collection1).addAll(collection2);
        verify(collection1).addLong(1L);
        verify(collection1).addLong(2L);
        verify(collection2).iterator();
        verifyNoMoreInteractions(collection1, collection2);
    }

    @Test
    void addAll_OfLong__always__iteratesAndCallsAddLong() {
        var collection1 = mock(PrimitiveCollection.OfLong.class);
        var collection2 = mock(PrimitiveCollection.OfLong.class);
        doCallRealMethod().when(collection1).addAll(collection2);
        when(collection2.iterator()).thenReturn(LongStream.of(1L, 2L).iterator());
        when(collection1.addLong(anyLong())).thenReturn(true);

        var result = collection1.addAll(collection2);

        assertThat(result).isTrue();
        verify(collection1).addAll(collection2);
        verify(collection1).addLong(1L);
        verify(collection1).addLong(2L);
        verify(collection2).iterator();
        verifyNoMoreInteractions(collection1, collection2);
    }

    @Test
    void addLong__always__throwsException() {
        var collection = mock(PrimitiveCollection.OfLong.class);
        doCallRealMethod().when(collection).addLong(1L);

        var t = catchThrowable(() -> collection.addLong(1L));

        assertThat(t).isInstanceOf(UnsupportedOperationException.class);
        verify(collection).addLong(1L);
        verifyNoMoreInteractions(collection);
    }

    @Test
    void contains__passedLong__delegatesToContainsLong() {
        var collection = mock(PrimitiveCollection.OfLong.class);
        doCallRealMethod().when(collection).contains(1L);
        when(collection.containsLong(1L)).thenReturn(true);

        var result = collection.contains(1L);

        assertThat(result).isTrue();
        verify(collection).contains(1L);
        verify(collection).containsLong(1L);
        verifyNoMoreInteractions(collection);
    }

    @Test
    void contains__passedObject__returnsFalse() {
        var collection = mock(PrimitiveCollection.OfLong.class);
        var ob = new Object();
        doCallRealMethod().when(collection).contains(ob);

        var result = collection.contains(ob);

        assertThat(result).isFalse();
        verify(collection).contains(ob);
        verifyNoMoreInteractions(collection);
    }

    @Test
    void containsAll_Collection__passedPrimitiveCollection__delegatesToContainsAll_OfLong() {
        var collection1 = mock(PrimitiveCollection.OfLong.class);
        var collection2 = mock(PrimitiveCollection.OfLong.class);
        doCallRealMethod().when(collection1).containsAll((Collection<Long>) collection2);
        when(collection1.containsAll(collection2)).thenReturn(true);

        var result = collection1.containsAll((Collection<Long>) collection2);

        assertThat(result).isTrue();
        verify(collection1).containsAll((Collection<Long>) collection2);
        verify(collection1).containsAll(collection2);
        verifyNoMoreInteractions(collection1, collection2);
    }

    @Test
    void containsAll_Collection__passedBoxedCollection__streamCalledWithAllMatchContains() {
        var collection1 = mock(PrimitiveCollection.OfLong.class);
        var collection2 = (Collection<Long>) mock(Collection.class);
        var stream = spy(Stream.of(1L, 2L));
        doCallRealMethod().when(collection1).containsAll(collection2);
        when(collection2.stream()).thenReturn(stream);
        when(collection1.contains(any())).thenReturn(true);

        var result = collection1.containsAll(collection2);

        assertThat(result).isTrue();
        verify(collection1).containsAll(collection2);
        verify(collection1).contains(1L);
        verify(collection1).contains(2L);
        verify(collection2).stream();
        verify(stream).allMatch(any());
        verifyNoMoreInteractions(collection1, collection2);
    }

    @Test
    void containsAll_OfLong__always__primitiveStreamCalledWithAllMatchContainsLong() {
        var collection1 = mock(PrimitiveCollection.OfLong.class);
        var collection2 = mock(PrimitiveCollection.OfLong.class);
        var stream = spy(LongStream.of(1L, 2L));
        doCallRealMethod().when(collection1).containsAll(collection2);
        when(collection2.primitiveStream()).thenReturn(stream);
        when(collection1.containsLong(anyLong())).thenReturn(true);

        var result = collection1.containsAll(collection2);

        assertThat(result).isTrue();
        verify(collection1).containsAll(collection2);
        verify(collection1).containsLong(1L);
        verify(collection1).containsLong(2L);
        verify(collection2).primitiveStream();
        verify(stream).allMatch(any());
        verifyNoMoreInteractions(collection1, collection2);
    }

    @ParameterizedTest
    @ValueSource(ints = { 1, 2, 3, 4 })
    void containsLong__always__primitiveStreamCalledWithAnyMatchEquals(int v) {
        var values = new long[] { 1L, 2L };
        var checkValue = (long) v;
        // Careful: very test-structure-specific logic here
        var expectedResult = v < 3;
        var collection = mock(PrimitiveCollection.OfLong.class);
        var stream = spy(LongStream.of(values));
        doCallRealMethod().when(collection).containsLong(anyLong());
        when(collection.primitiveStream()).thenReturn(stream);

        var result = collection.containsLong(checkValue);

        assertThat(result).isEqualTo(expectedResult);
        verify(collection).containsLong(checkValue);
        verify(collection).primitiveStream();
        verify(stream).anyMatch(any());
        verifyNoMoreInteractions(collection);
    }

    @Test
    void remove__passedLong__delegatesToRemoveLong() {
        var collection = mock(PrimitiveCollection.OfLong.class);
        doCallRealMethod().when(collection).remove(1L);
        when(collection.removeLong(1L)).thenReturn(true);

        var result = collection.remove(1L);

        assertThat(result).isTrue();
        verify(collection).remove(1L);
        verify(collection).removeLong(1L);
        verifyNoMoreInteractions(collection);
    }

    @Test
    void remove__passedObject__returnsFalse() {
        var collection = mock(PrimitiveCollection.OfLong.class);
        var ob = new Object();
        doCallRealMethod().when(collection).remove(ob);

        var result = collection.remove(ob);

        assertThat(result).isFalse();
        verify(collection).remove(ob);
        verifyNoMoreInteractions(collection);
    }

    @Test
    void removeAll_Collection__passedPrimitiveCollection__delegatesToRemoveAll_OfLong() {
        var collection1 = mock(PrimitiveCollection.OfLong.class);
        var collection2 = mock(PrimitiveCollection.OfLong.class);
        doCallRealMethod().when(collection1).removeAll((Collection<Long>) collection2);
        when(collection1.removeAll(collection2)).thenReturn(true);

        var result = collection1.removeAll((Collection<Long>) collection2);

        assertThat(result).isTrue();
        verify(collection1).removeAll((Collection<Long>) collection2);
        verify(collection1).removeAll(collection2);
        verifyNoMoreInteractions(collection1, collection2);
    }

    @Test
    void removeAll_Collection__passedBoxedCollection__delegatesToRemoveIfLong() {
        var collection1 = mock(PrimitiveCollection.OfLong.class);
        var collection2 = (Collection<Long>) mock(Collection.class);
        doCallRealMethod().when(collection1).removeAll(collection2);
        when(collection1.removeIfLong(any())).thenAnswer(invocationOnMock ->
                ((LongPredicate) invocationOnMock.getArgument(0)).test(1L));
        when(collection2.contains(1L)).thenReturn(true);

        var result = collection1.removeAll(collection2);

        assertThat(result).isTrue();
        verify(collection1).removeAll(collection2);
        verify(collection1).removeIfLong(any());
        verify(collection2).contains(1L);
        verifyNoMoreInteractions(collection1, collection2);
    }

    @Test
    void removeAll_OfLong__always__delegatesToRemoveIfLong() {
        var collection1 = mock(PrimitiveCollection.OfLong.class);
        var collection2 = mock(PrimitiveCollection.OfLong.class);
        doCallRealMethod().when(collection1).removeAll(collection2);
        when(collection1.removeIfLong(any())).thenAnswer(invocationOnMock ->
                ((LongPredicate) invocationOnMock.getArgument(0)).test(1L));
        when(collection2.containsLong(1L)).thenReturn(true);

        var result = collection1.removeAll(collection2);

        assertThat(result).isTrue();
        verify(collection1).removeAll(collection2);
        verify(collection1).removeIfLong(any());
        verify(collection2).containsLong(1L);
        verifyNoMoreInteractions(collection1, collection2);
    }

    @ParameterizedTest
    @ValueSource(ints = { 1, 2, 3, 4 })
    void removeLong__always__iteratesAndCallsRemoveOnEqual(int v) {
        var removeValue = (long) v;
        // Careful: very test-structure-specific logic here
        var expectedResult = v < 3;
        var collection = mock(PrimitiveCollection.OfLong.class);
        var iterator = mock(PrimitiveIterator.OfLong.class);
        doCallRealMethod().when(collection).removeLong(removeValue);
        when(collection.iterator()).thenReturn(iterator);
        when(iterator.hasNext()).thenReturn(true, true, false);
        when(iterator.nextLong()).thenReturn(1L, 2L);

        var result = collection.removeLong(removeValue);

        assertThat(result).isEqualTo(expectedResult);
        verify(collection).removeLong(removeValue);
        verify(collection).iterator();
        verify(iterator, times(Math.min(v, 3))).hasNext();
        verify(iterator, times(Math.min(v, 2))).nextLong();
        if(expectedResult)
            verify(iterator).remove();
        verifyNoMoreInteractions(collection, iterator);
    }

    @Test
    void removeIf_LongPredicate__always__delegatesToRemoveIfLong() {
        var collection = mock(PrimitiveCollection.OfLong.class);
        var filter = mock(LongPredicate.class);
        doCallRealMethod().when(collection).removeIf(filter);
        when(collection.removeIfLong(filter)).thenReturn(true);

        var result = collection.removeIf(filter);

        assertThat(result).isTrue();
        verify(collection).removeIf(filter);
        verify(collection).removeIfLong(filter);
        verifyNoMoreInteractions(collection, filter);
    }

    @Test
    void removeIf_Predicate__always__delegatesToRemoveIfLong() {
        var collection = mock(PrimitiveCollection.OfLong.class);
        var filter = (Predicate<Long>) mock(Predicate.class);
        doCallRealMethod().when(collection).removeIf(filter);
        when(collection.removeIfLong(any())).thenAnswer(invocationOnMock ->
                ((LongPredicate) invocationOnMock.getArgument(0)).test(1L));
        when(filter.test(1L)).thenReturn(true);

        var result = collection.removeIf(filter);

        assertThat(result).isTrue();
        verify(collection).removeIf(filter);
        verify(collection).removeIfLong(any());
        verify(filter).test(1L);
        verifyNoMoreInteractions(collection, filter);
    }

    @Test
    void removeIfLong__always__iteratesOverCollectionAndCallsRemoveOnTrue() {
        var collection = mock(PrimitiveCollection.OfLong.class);
        var filter = mock(LongPredicate.class);
        var iterator = mock(PrimitiveIterator.OfLong.class);
        doCallRealMethod().when(collection).removeIfLong(filter);
        when(collection.iterator()).thenReturn(iterator);
        when(iterator.hasNext()).thenReturn(true, true, true, false);
        when(iterator.nextLong()).thenReturn(1L, 2L, 3L);
        when(filter.test(1L)).thenReturn(true);

        var result = collection.removeIfLong(filter);

        assertThat(result).isTrue();
        verify(collection).removeIfLong(filter);
        verify(collection).iterator();
        verify(filter).test(1L);
        verify(filter).test(2L);
        verify(filter).test(3L);
        verify(iterator, times(4)).hasNext();
        verify(iterator, times(3)).nextLong();
        verify(iterator).remove();
        verifyNoMoreInteractions(collection, filter, iterator);
    }

    @Test
    void retainAll_Collection__passedPrimitiveCollection__delegatesToRetainAll_OfLong() {
        var collection1 = mock(PrimitiveCollection.OfLong.class);
        var collection2 = mock(PrimitiveCollection.OfLong.class);
        doCallRealMethod().when(collection1).retainAll((Collection<Long>) collection2);
        when(collection1.retainAll(collection2)).thenReturn(true);

        var result = collection1.retainAll((Collection<Long>) collection2);

        assertThat(result).isTrue();
        verify(collection1).retainAll((Collection<Long>) collection2);
        verify(collection1).retainAll(collection2);
        verifyNoMoreInteractions(collection1, collection2);
    }

    @Test
    void retainAll_Collection__passedBoxedCollection__delegatesToRemoveIfLong() {
        var collection1 = mock(PrimitiveCollection.OfLong.class);
        var collection2 = (Collection<Long>) mock(Collection.class);
        doCallRealMethod().when(collection1).retainAll(collection2);
        when(collection1.removeIfLong(any())).thenAnswer(invocationOnMock ->
                ((LongPredicate) invocationOnMock.getArgument(0)).test(1L));
        when(collection2.contains(1L)).thenReturn(true);

        var result = collection1.retainAll(collection2);

        assertThat(result).isFalse();
        verify(collection1).retainAll(collection2);
        verify(collection1).removeIfLong(any());
        verify(collection2).contains(1L);
        verifyNoMoreInteractions(collection1, collection2);
    }

    @Test
    void retainAll_OfLong__always__delegatesToRetainIfLong() {
        var collection1 = mock(PrimitiveCollection.OfLong.class);
        var collection2 = mock(PrimitiveCollection.OfLong.class);
        doCallRealMethod().when(collection1).retainAll(collection2);
        when(collection1.removeIfLong(any())).thenAnswer(invocationOnMock ->
                ((LongPredicate) invocationOnMock.getArgument(0)).test(1L));
        when(collection2.containsLong(1L)).thenReturn(true);

        var result = collection1.retainAll(collection2);

        assertThat(result).isFalse();
        verify(collection1).retainAll(collection2);
        verify(collection1).removeIfLong(any());
        verify(collection2).containsLong(1L);
        verifyNoMoreInteractions(collection1, collection2);
    }
}
